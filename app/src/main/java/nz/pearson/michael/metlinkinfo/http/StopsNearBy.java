package nz.pearson.michael.metlinkinfo.http;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import nz.pearson.michael.metlinkinfo.Loadable;
import nz.pearson.michael.metlinkinfo.http.model.Stop;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class StopsNearBy {

    private final Loadable feedback;

    public interface DataReady {
        void ready(Stop stops[]);
    }

    private final String requestURL = "https://www.metlink.org.nz/api/v1/StopNearby/%s/%s";

    public StopsNearBy(Loadable feedback) {
        this.feedback = feedback;
    }

    public void getNearbyStops(double lng, double lat, DataReady callback) {
        new RunTask(lng, lat, callback).execute();
    }



    private class RunTask extends AsyncTask<Void, Void, Void> {
        private final DataReady callback;
        private final double lng, lat;

        private Stop[] result = null;

        public RunTask(double lng, double lat, DataReady callback) {
            this.lat = lat;
            this.lng = lng;
            this.callback = callback;
        }

        @Override
        protected void onPreExecute() {
            feedback.setLoading(true);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(String.format(requestURL, lat, lng))
                        .get()
                        .build();
                Response response = client.newCall(request).execute();
                List<Stop> build = new ArrayList<>();
                JSONArray stopData = new JSONArray(response.body().string());
                for(int a = 0;a < stopData.length();a++) {
                    build.add(new Stop(
                            stopData.getJSONObject(a).getString("Name"),
                            stopData.getJSONObject(a).getString("Sms")
                    ));
                }
                Stop[] stops = new Stop[build.size()];
                build.toArray(stops);
                result = stops;
            } catch (IOException | JSONException ignore) {
                ignore.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(result == null) {
                return;
            }
            callback.ready(result);
            feedback.setLoading(false);
        }
    }

}
