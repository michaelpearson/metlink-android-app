package nz.pearson.michael.metlinkinfo.http.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import nz.pearson.michael.metlinkinfo.R;

public class ServiceAdapter extends ArrayAdapter<Service> {
    public ServiceAdapter(Context context, int resource) {
        super(context, resource);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Service service = getItem(position);
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.service_info_view, parent, false);
        }
        TextView title = (TextView)convertView.findViewById(R.id.text1);
        TextView description = (TextView)convertView.findViewById(R.id.text2);
        TextView time = (TextView)convertView.findViewById(R.id.timeDisplay);
        title.setText(service.getDestinationStopName());
        time.setText(service.getDisplayDepartureSeconds());
        description.setText(String.format("%s, %s to %s", service.getServiceID(), service.getOriginStopName(), service.getDestinationStopName()));
        return convertView;
    }
}
