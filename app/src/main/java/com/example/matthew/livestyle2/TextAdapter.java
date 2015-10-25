package com.example.matthew.livestyle2;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by Matthew on 10/25/2015.
 */
public class TextAdapter extends BaseAdapter {
    private Context mContext;

    public TextAdapter(Context c) {
        mContext = c;
    }

    public int getCount() {
        return mThumbIds.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new TextView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    200));
        } else {
            imageView = (TextView) convertView;
        }

        imageView.setText(mThumbIds.get(position));
        imageView.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        return imageView;
    }

    private ArrayList<String> mThumbIds = new ArrayList<>();

    public void addText(String title) {
        mThumbIds.add(title);
    }
}
