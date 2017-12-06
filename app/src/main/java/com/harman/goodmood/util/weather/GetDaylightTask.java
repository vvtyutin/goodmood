package com.harman.goodmood.util.weather;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import java.io.IOException;

/**
 * Created by evgenygusev on 06/12/2017.
 */

public class GetDaylightTask {
    private static final String API_ENDPOINT = "http://api.openweathermap.org/data/2.5/weather";
    private static final String API_TOKEN = "59c1f3468921e1b22fe27e6f911d835b";

    public interface LoadDaylightCallback {
        void onDaylightLoaded(DayLight dayLight);
    }

    public GetDaylightTask(final LoadDaylightCallback loadDaylightCallback) { //MessageLoadedListener downloadCompleteListener
        OkHttpClient client = new OkHttpClient();

        HttpUrl.Builder urlBuilder = HttpUrl.parse(API_ENDPOINT).newBuilder();
        urlBuilder.addQueryParameter("lat", "56");
        urlBuilder.addQueryParameter("lon", "44");
        urlBuilder.addQueryParameter("appid", API_TOKEN);
        String url = urlBuilder.build().toString();

        final Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                loadDaylightCallback.onDaylightLoaded(null);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                try {
                    DayLight daylight = new DayLight(response.body().string());
                    loadDaylightCallback.onDaylightLoaded(daylight);
                } catch (Exception ex) {
                    loadDaylightCallback.onDaylightLoaded(null);
                }
            }
        });
    }

}