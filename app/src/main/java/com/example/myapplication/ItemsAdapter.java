package com.example.myapplication;

import android.content.Context;
import android.view.*;
import android.widget.*;

import java.util.List;

public class ItemsAdapter extends BaseAdapter {

    List<String> content;
    Context context;

    public ItemsAdapter(Context c, List<String> content) {
        context = c;
        this.content = content;
    }

    @Override
    public int getCount() {
        return content.size();
    }

    @Override
    public Object getItem(int position) {
        return content.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View gridView;

        if (convertView == null) {

            gridView = new View(context);

            // get layout from resources
            gridView = inflater.inflate(R.layout.item, null);

            // set image based on selected text
            Button btn = (Button) gridView.findViewById(R.id.myButton);
            btn.setText(content.get(position));

            TextView textview = (TextView) gridView.findViewById(R.id.myText);
            textview.setText("Button " + position);

        } else {
            gridView = (View) convertView;
        }

        return gridView;
    }
}
