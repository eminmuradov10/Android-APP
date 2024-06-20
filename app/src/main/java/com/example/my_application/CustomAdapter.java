package com.example.my_application;

import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;


import androidx.annotation.Nullable;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

class CustomAdapter implements ListAdapter {
    ArrayList<News> arrayList;
    Context context;
    public CustomAdapter(Context context, ArrayList<News> arrayList) {
        this.arrayList=arrayList;
        this.context=context;
    }
    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }
    @Override
    public boolean isEnabled(int position) {
        return true;
    }
    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
    }
    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
    }
    @Override
    public int getCount() {
        return arrayList.size();
    }
    @Override
    public Object getItem(int position) {
        return position;
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final News subjectData = arrayList.get(position);
        if(convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.list_row, null);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri uri = Uri.parse(subjectData.getLink());
                    Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                    context.startActivity(intent);
                }
            });
            TextView tittle = convertView.findViewById(R.id.title);
            ImageView imag = convertView.findViewById(R.id.list_image);
            tittle.setText(subjectData.getTitle());
            //Picasso.with(context)
            Picasso.get ()
                    .load(subjectData.getImageURL())
                    .into(imag);
        }
        return convertView;
    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }
    @Override
    public int getViewTypeCount() {
        return arrayList.size();
    }
    @Override
    public boolean isEmpty() {
        return false;
    }

    @Nullable
    @Override
    public CharSequence[] getAutofillOptions() {
        return ListAdapter.super.getAutofillOptions();
    }
}