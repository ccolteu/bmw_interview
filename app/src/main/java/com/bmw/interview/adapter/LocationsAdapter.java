package com.bmw.interview.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.bmw.interview.R;
import com.bmw.interview.model.BMWLocation;
import com.bmw.interview.utils.Utils;

import java.util.List;

public class LocationsAdapter extends ArrayAdapter<BMWLocation> {
    int layoutResourceId;
    List<BMWLocation> data;
    LayoutInflater inflater;

    class ViewHolder {
        public TextView name;
        public TextView address;
        public TextView time;
        public TextView distance;
    }

    public LocationsAdapter(Activity activity, int layoutResourceId, List<BMWLocation> data) {
        super(activity.getBaseContext(), layoutResourceId, data);
        this.data = data;
        this.layoutResourceId = layoutResourceId;
        this.inflater = activity.getLayoutInflater();
    }

    @Override
    public BMWLocation getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(layoutResourceId, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.name = (TextView) convertView.findViewById(R.id.name);
            viewHolder.address = (TextView) convertView.findViewById(R.id.address);
            viewHolder.time = (TextView) convertView.findViewById(R.id.time);
            viewHolder.distance = (TextView) convertView.findViewById(R.id.distance);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        BMWLocation bmwLocation = getItem(position);
        viewHolder.name.setText(bmwLocation.getName());
        viewHolder.address.setText(bmwLocation.getAddress());
        viewHolder.time.setText(Utils.prettyTime(bmwLocation.getArrivalTime()));
        viewHolder.distance.setVisibility(View.GONE);
        if (bmwLocation.getDistance() != -1) {
            viewHolder.distance.setText(String.format("%.2f Miles", bmwLocation.getDistance()));
            viewHolder.distance.setVisibility(View.VISIBLE);
        }
        return convertView;
    }
}

