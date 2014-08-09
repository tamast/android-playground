package com.taktam.android.swiperefreshimagelist;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Gravity;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.taktam.android.swiperefreshimagelist.model.Image;
import com.taktam.android.swiperefreshimagelist.model.ResponseRoot;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;

import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

@ContentView(R.layout.activity_swipe_refresh_images)
public class SwipeRefreshImagesActivity extends RoboActivity implements SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = SwipeRefreshImagesActivity.class.getSimpleName();

    @InjectView(R.id.list)
    private ListView listView;

    @InjectView(R.id.swipe_container)
    private SwipeRefreshLayout swipeLayout;

    private ImageListViewAdapter imageListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        swipeLayout.setOnRefreshListener(this);

        imageListAdapter = new ImageListViewAdapter(this,
                R.layout.list_item, new ArrayList<Image>());

        listView.setAdapter(imageListAdapter);

        downloadImageList();
    }

    @Override
    public void onRefresh() {
        downloadImageList();
    }

    private void downloadImageList() {
        final String URL = "http://thecatapi.com/api/images/get?format=xml&results_per_page=20&size=med&type=png";
        StringRequest req = new StringRequest(URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Serializer serializer = new Persister();
                Reader reader = new StringReader(response);
                try {
                    ResponseRoot root = serializer.read(ResponseRoot.class, reader, false);
                    imageListAdapter.replaceAll(root.data.images);
                    swipeLayout.setRefreshing(false);
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error: ", error.getMessage());
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Cannot load images!", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
                toast.show();
            }
        });

        ApplicationController.getInstance().addToRequestQueue(req);
    }
}
