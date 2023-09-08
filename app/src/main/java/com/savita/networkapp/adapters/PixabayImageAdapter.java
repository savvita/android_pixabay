package com.savita.networkapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.savita.networkapp.GalleryActivity;
import com.savita.networkapp.MainActivity;
import com.savita.networkapp.R;
import com.savita.networkapp.controllers.PixabayController;
import com.savita.networkapp.models.PixabayImage;

import java.util.List;

public class PixabayImageAdapter extends ArrayAdapter<PixabayImage> {
    private List<PixabayImage> images;
    private Context context;
    private int resource;
    private LayoutInflater inflater;

    public PixabayImageAdapter(@NonNull Context context, int resource, List<PixabayImage> items) {
        super(context, resource);
        this.context = context;
        this.resource = resource;
        this.images = items;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PixabayItemViewHolder viewHolder;
        if(convertView == null) {
            convertView = inflater.inflate(resource, parent, false);
            viewHolder = new PixabayItemViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (PixabayItemViewHolder) convertView.getTag();
        }

        PixabayImage item = images.get(position);

        if(item != null) {
            PixabayController.loadImage(item.getPreviewUrl(), viewHolder.image);
            viewHolder.container.setOnClickListener((view) -> {
                Intent intent = new Intent(context, GalleryActivity.class);
                intent.putExtra(MainActivity.POSITION, position);
                context.startActivity(intent);
            });
        }

        return convertView;
    }

    private class PixabayItemViewHolder {
        private final ImageView image;
        private final ConstraintLayout container;

        private PixabayItemViewHolder(View view) {
            image = view.findViewById(R.id.pixabay_item_view);
            container = view.findViewById(R.id.gallery_item_container);
        }
    }
}
