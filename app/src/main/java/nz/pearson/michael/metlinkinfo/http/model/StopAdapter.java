package nz.pearson.michael.metlinkinfo.http.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class StopAdapter extends ArrayAdapter<Stop> {
    public StopAdapter(Context context, int resource) {
        super(context, resource);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Stop stop = getItem(position);
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_2, parent, false);
        }
        TextView title = (TextView)convertView.findViewById(android.R.id.text1);
        TextView description = (TextView)convertView.findViewById(android.R.id.text2);
        title.setText(stop.getName());
        description.setText(stop.getStopNumber());



        return convertView;
    }
}
