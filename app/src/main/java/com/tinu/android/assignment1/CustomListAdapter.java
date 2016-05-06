package com.tinu.android.assignment1;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Tinu on 05-05-2016.
 */
public class CustomListAdapter extends BaseAdapter {

    private final Activity context;
    private List<ListItems> itemListData;
    private LayoutInflater layoutInflator;
    private ViewHolder holder;

    public CustomListAdapter(Activity context, List<ListItems> itemList) {
        super();
        itemListData = itemList;
        this.context = context;
        layoutInflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {

        return itemListData.size();
    }

    @Override
    public ListItems getItem(int position) {

        return itemListData.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ListItems item = getItem(position);
        if (convertView == null) {
            holder = new ViewHolder();

            convertView = layoutInflator.inflate(R.layout.list_item, null);
            holder.text_name = (TextView) convertView.findViewById(R.id.nameValue);
            holder.text_email = (TextView) convertView.findViewById(R.id.emailValue);
            convertView.setTag(holder);

        } else {

            holder = (ViewHolder) convertView.getTag();
        }
        //Get the data from the list and set the data.
        ListItems ListData = itemListData.get(position);

        holder.text_name.setText(ListData.getName());
        holder.text_email.setText(ListData.getEmail());

        return convertView;

    }

    public static class ViewHolder {

        TextView text_name;
        TextView text_email;

    }
}
