package Adapter;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.amoozeshmelli.MyApplication;
import com.amoozeshmelli.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestListener;

import java.lang.annotation.Target;
import java.util.List;

import Data.Slide;
import androidx.recyclerview.widget.RecyclerView;

public class SliderAdapter extends RecyclerView.Adapter<SliderAdapter.MyViewHolder> {

    private Context context;
    private List<Slide> items;


    public SliderAdapter(Context context, List<Slide> value) {
        this.context = context;
        this.items = value;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_2, parent, false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {


        Log.e("Slider" , items.get(position % items.size()).getUrl());

        Glide.with(context)
                .load(items.get(position).getUrl())
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .dontTransform()
                .into(holder.image);

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView image;

        public MyViewHolder(View view) {
            super(view);
            image = view.findViewById(R.id.slide_image);
        }

    }

}
