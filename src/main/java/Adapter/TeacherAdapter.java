package Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amoozeshmelli.MyApplication;
import com.amoozeshmelli.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import Data.Teacher;
import androidx.recyclerview.widget.RecyclerView;

public class TeacherAdapter extends RecyclerView.Adapter<TeacherAdapter.MyViewHolder> {

    private Context context;
    private List<Teacher> items;

    private OnItemClickListener onItemClickListener;

    public TeacherAdapter(Context context, List<Teacher> value) {
        this.context = context;
        this.items = value;
    }

    @NotNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_3, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.lesson_text.setText(items.get(position).getFullName());

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
            lesson_text = view.findViewById(R.id.teacher_name);

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
