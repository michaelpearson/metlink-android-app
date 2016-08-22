package nz.pearson.michael.metlinkinfo.http;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import nz.pearson.michael.metlinkinfo.Loadable;
import nz.pearson.michael.metlinkinfo.http.model.Service;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class StopInfo {

    private final Loadable feedback;

    public interface DataReady {
        void ready(Service services[]);
    }

    private final String requestURL = "https://www.metlink.org.nz/api/v1/StopDepartures/%s";

    public StopInfo(Loadable feedback) {
        this.feedback = feedback;
    }

    public void getStopInformation(String stopId, DataReady callback) {
        new RunTask(stopId, callback).execute();
    }



    private class RunTask extends AsyncTask<Void, Void, Void> {
        private final DataReady callback;
        private final String stopId;

        private Service[] result = null;

        public RunTask(String stopId, DataReady callback) {
            this.stopId = stopId;
            this.callback = callback;
        }

        @Override
        protected void onPreExecute() {
            if(feedback != null) {
                feedback.setLoading(true);
            }
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(String.format(requestURL, stopId))
                        .get()
                        .build();
                Response response = client.newCall(request).execute();
                List<Service> build = new ArrayList<>();
                JSONArray data = new JSONObject(response.body().string()).getJSONArray("Services");
                for(int a = 0;a < data.length();a++) {
                    build.add(new Service(data.getJSONObject(a)));
                }
                Service[] service = new Service[build.size()];
                build.toArray(service);
                result = service;
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
            if(feedback != null) {
                feedback.setLoading(false);
            }
        }
    }

}
