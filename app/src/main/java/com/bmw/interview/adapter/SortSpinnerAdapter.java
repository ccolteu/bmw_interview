package com.bmw.interview.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.bmw.interview.R;

import java.util.ArrayList;

public class SortSpinnerAdapter  extends ArrayAdapter<String> {
    private Context ctx;
    private LayoutInflater mInflater;
    private ArrayList<String> items;

    public SortSpinnerAdapter(Context context, int resId, ArrayList<String> items) {
        super(context, resId, items);
        this.ctx = context;
        this.items = items;
        mInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return items.size();
    }

    public String getItem(int position) {
        return items.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String item = items.get(position);
        convertView = mInflater.inflate(R.layout.sort_spinner_view, null);
        TextView textView = (TextView) convertView.findViewById(R.id.text);
        String html = "<b>Sort:</b> " + item;
        textView.setText(Html.fromHtml(html));
        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        String item = items.get(position);
        convertView = mInflater.inflate(R.layout.sort_spinner_dropdown_view, null);
        TextView textView = (TextView) convertView.findViewById(R.id.text);
        String html = item;
        textView.setText(Html.fromHtml(html));
        return convertView;
    }
}
