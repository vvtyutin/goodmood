package com.harman.goodmood.recognizer;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.cmu.pocketsphinx.Assets;
import edu.cmu.pocketsphinx.Hypothesis;
import edu.cmu.pocketsphinx.RecognitionListener;
import edu.cmu.pocketsphinx.SpeechRecognizer;
import edu.cmu.pocketsphinx.SpeechRecognizerSetup;

/**
 * Created by vtyutin on 04/12/2017.
 */

public class SpeachRecognizerManager implements RecognitionListener {
    private static final String COMMANDS_SEARCH = "commands";
    private static SpeachRecognizerManager instance = null;
    private SpeechRecognizer recognizer;
    private Context context;
    private ArrayList<SpeachRecognizerListener> listeners = new ArrayList<>();

    private SpeachRecognizerManager(Context context) {
        this.context = context;
        new SpeachRecognizerManager.VoiceRecognitionInitTask().execute();
    }

    public static SpeachRecognizerManager getInstance(Context context) {
        if (instance == null) {
            instance = new SpeachRecognizerManager(context);
        }
        return instance;
    }

    public void startListening() {
        if (recognizer != null) {
            recognizer.startListening(COMMANDS_SEARCH);
        }
    }

    public void stopListening() {
        if (recognizer != null) {
            recognizer.stop();
        }
    }

    public void addListener(SpeachRecognizerListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public void removeListener(SpeachRecognizerListener listener) {
        if (listeners.contains(listener)) {
            listeners.remove(listener);
        }
    }

    @Override
    public void onBeginningOfSpeech() {
        for (SpeachRecognizerListener listener: listeners) {
            listener.onBeginSpeach();
        }
    }

    @Override
    public void onEndOfSpeech() {
        for (SpeachRecognizerListener listener: listeners) {
            listener.onEndSpeach();
        }
    }

    @Override
    public void onPartialResult(Hypothesis hypothesis) {
        if (hypothesis == null)
            return;

        Log.d("###", "onPartialResult: " + hypothesis.getHypstr());
        for (SpeachRecognizerListener listener: listeners) {
            listener.onPartialResult(hypothesis.getHypstr());
        }
    }

    @Override
    public void onResult(Hypothesis hypothesis) {
        if (hypothesis == null)
            return;

        Log.d("###", "onPartialResult: " + hypothesis.getHypstr());
        for (SpeachRecognizerListener listener: listeners) {
            listener.onFinalResult(hypothesis.getHypstr());
        }
    }

    @Override
    public void onError(Exception e) {

    }

    @Override
    public void onTimeout() {

    }

    private String createVoiceGrammar() {
        String colors = "black | blue | cyan | dark gray | gray | green | light gray | magenta | red | white";
        String intensity = "low | medium | hight";
        String grammar = "#JSGF V1.0;\n" +
                "\n" +
                "grammar commands;\n" +
                "\n" +
                "    <command> = " +
                " (set (color (" + colors + ") | intensity (" + intensity + ")) | switch (on | off));\n" +
                "\n" +
                "public <commands> = <command>;";
        return grammar;
    }

    private void setupRecognizer(File assetsDir) throws IOException {
        if (recognizer != null) {
            recognizer.cancel();
            recognizer.shutdown();
        }
        recognizer = SpeechRecognizerSetup.defaultSetup()
                .setAcousticModel(new File(assetsDir, "en-us-ptm"))
                .setDictionary(new File(assetsDir, "cmudict-en-us.dict"))
                .setRawLogDir(assetsDir)
                .setKeywordThreshold(1e-45f)
                .setBoolean("-allphone_ci", true)
                .getRecognizer();
        recognizer.addListener(this);

        String newGrammar = createVoiceGrammar();
        Log.d("###", "newGrammar: " + newGrammar);
        if (newGrammar == null) {
            throw new IOException(": no device names found");
        }

        // Create grammar-based search for commands recognition
        File commandsGrammar = new File(assetsDir, "commands.gram");
        FileWriter writer = new FileWriter(commandsGrammar);

        writer.write(newGrammar);
        writer.flush();
        recognizer.addGrammarSearch(COMMANDS_SEARCH, commandsGrammar);
    }

    private class VoiceRecognitionInitTask extends AsyncTask<Void, Void, Exception> {
        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Exception doInBackground(Void... params) {
            try {
                Assets assets = new Assets(context);
                File assetDir = assets.syncAssets();
                setupRecognizer(assetDir);
            } catch (Exception e) {
                return e;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Exception result) {
            if ((result != null) || (recognizer == null)) {
                //Toast.makeText(DeviceListActivity.this, "Voice Recognizer Error" + result.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                Log.e(SpeachRecognizerManager.this.getClass().getSimpleName(), "Voice Recognizer Error" + result.getLocalizedMessage());
            } else {
                for (SpeachRecognizerListener listener: listeners) {
                    listener.onInitCompleted();
                }
            }
        }
    }
}
