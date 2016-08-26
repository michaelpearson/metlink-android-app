package nz.pearson.michael.metlinkinfo;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import net.danlew.android.joda.JodaTimeAndroid;

import java.util.Arrays;
import java.util.Comparator;

import nz.pearson.michael.metlinkinfo.http.StopInfo;
import nz.pearson.michael.metlinkinfo.http.model.Service;
import nz.pearson.michael.metlinkinfo.http.model.ServiceAdapter;

public class StopActivity extends AppCompatActivity {

    public static final String KEY_STOP_ID = "stop_id";
    private ListView serviceInformationView;
    private ServiceAdapter serviceInformationAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        JodaTimeAndroid.init(this);

        setContentView(R.layout.activity_stop);

        serviceInformationView = (ListView) findViewById(R.id.serviceInformation);
        serviceInformationAdapter = new ServiceAdapter(this, android.R.layout.simple_list_item_2);
        serviceInformationView.setAdapter(serviceInformationAdapter);

        Intent i = getIntent();
        String serviceId = i.getStringExtra(KEY_STOP_ID);
        if(serviceId == null) {
            finish();
            return;
        }

        new StopInfo(null).getStopInformation(serviceId, new StopInfo.DataReady() {
            @Override
            public void ready(Service[] services) {
                Arrays.sort(services, new Comparator<Service>() {
                    @Override
                    public int compare(Service service, Service t1) {
                        return service.getSeconds() > t1.getSeconds() ? 1 : -1;
                    }
                });
                serviceInformationAdapter.addAll(services);
                serviceInformationAdapter.notifyDataSetChanged();
            }
        });
    }


    private void log(String m) {
        Log.i("StopActivity", m);
    }
}
