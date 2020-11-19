package Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amoozeshmelli.MyApplication;
import com.amoozeshmelli.R;
import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import Data.Lesson;
import androidx.recyclerview.widget.RecyclerView;

public class LessonAdapter extends RecyclerView.Adapter<LessonAdapter.MyViewHolder> {

    private Context context;
    private List<Lesson> items;

    private OnItemClickListener onItemClickListener;

    public LessonAdapter(Context context, List<Lesson> value) {
        this.context = context;
        this.items = value;
    }

    @NotNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_1, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.lesson_text.setText(items.get(position).getTitle());
        Glide.with(context)
                .load(items.get(position).getImage_url())
                .centerCrop()
                .dontTransform()
                .into(holder.lesson_image);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView lesson_text;
        ImageView lesson_image;

        MyViewHolder(View view) {
            super(view);
            lesson_text = view.findViewById(R.id.lesson_text);
            lesson_image = view.findViewById(R.id.lesson_logo);

            lesson_text.setTypeface(MyApplication.getMyTypeFaceMedium(context));
        }

    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(final OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

}
