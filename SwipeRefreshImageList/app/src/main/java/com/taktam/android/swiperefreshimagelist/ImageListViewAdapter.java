package com.taktam.android.swiperefreshimagelist;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.taktam.android.swiperefreshimagelist.model.Image;

import java.util.List;

public class ImageListViewAdapter extends ArrayAdapter<Image> {

    private Context context;

    public ImageListViewAdapter(Context context, int resourceId, List<Image> items) {
        super(context, resourceId, items);
        this.context = context;
    }

    private class ViewHolder {
        ImageView imageView;
        ProgressBar progressBar;
    }

    public void replaceAll(List<Image> items) {
        clear();
        addAll(items);
        notifyDataSetChanged();
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        Image rowItem = getItem(position);

        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item, null);
            holder = new ViewHolder();
            holder.imageView = (ImageView) convertView.findViewById(R.id.image);
            holder.progressBar = (ProgressBar) convertView.findViewById(R.id.progressBar);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final ProgressBar progressBar = holder.progressBar;

        Picasso.with(context).load(rowItem.url).into(holder.imageView, new Callback() {
            @Override
            public void onSuccess() {
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onError() {
                //error
            }
        });

        return convertView;
    }
}