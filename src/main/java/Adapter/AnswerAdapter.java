package Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amoozeshmelli.MyApplication;
import com.amoozeshmelli.R;
import com.amoozeshmelli.ShowPhotoActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.util.List;

import Data.Product;
import Data.Question;
import androidx.recyclerview.widget.RecyclerView;

public class AnswerAdapter extends RecyclerView.Adapter<AnswerAdapter.MyViewHolder>{

    private Context context;
    private List<Question> items;

    public AnswerAdapter(Context context, List<Question> value) {
        this.context = context;
        this.items = value;
    }

    @NotNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_12, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.q_id.setText("جواب سوال " + (position + 1));

        Glide.with(context)
                .load(items.get(position).getAnswer_file_url())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.image_view);


        holder.image_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, ShowPhotoActivity.class);
                intent.putExtra("image" , items.get(position).getAnswer_file_url());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView image_view;
        TextView q_id;

        MyViewHolder(View view) {
            super(view);

            image_view = view.findViewById(R.id.image_view);
            q_id = view.findViewById(R.id.q_id);

            q_id.setTypeface(MyApplication.getMyTypeFaceMedium(context));
        }

    }

}
