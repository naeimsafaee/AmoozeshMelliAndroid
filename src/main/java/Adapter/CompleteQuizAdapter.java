package Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amoozeshmelli.MyApplication;
import com.amoozeshmelli.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import Data.Quiz;
import androidx.recyclerview.widget.RecyclerView;

public class CompleteQuizAdapter extends RecyclerView.Adapter<CompleteQuizAdapter.MyViewHolder> {

    private Context context;
    private List<Quiz> items;

    private OnItemClickListener onItemClickListener;

    public CompleteQuizAdapter(Context context, List<Quiz> value) {
        this.context = context;
        this.items = value;
    }

    @NotNull
    @Override
    public MyViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View itemView;
        if (viewType == 0) {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_4, parent, false);
        } else {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_5, parent, false);
        }

        return new MyViewHolder(itemView);
    }

    @Override
    public int getItemViewType(int position) {
        if (position % 2 == 0)
            return 0;
        else
            return 1;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.is_available.setText(items.get(position).isLocked() ?
                items.get(position).getShamsiQuizQate()
                : "در دسترس");

        if (items.get(position).getHasPaid()) {
            holder.is_available.setText("خریداری شده");
            holder.price.setText("پرداخت شده");
        } else if (items.get(position).isLocked()) {
            holder.price.setText(items.get(position).getEarlyPrice() + " تومان برای پیش گشایش ");
        } else {
            holder.price.setText(items.get(position).getPrice() + " تومان " + "\n" +
                    items.get(position).getGiftPrice() + " تومان کیف هدیه");
        }

        if (position % 2 == 0)
            holder.section_title.setText(" | " + items.get(position).getQuizTitle());
        else
            holder.section_title.setText(items.get(position).getQuizTitle() + " | ");

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

        TextView is_available, price, section_title;

        MyViewHolder(View view) {
            super(view);
            is_available = view.findViewById(R.id.is_available);
            price = view.findViewById(R.id.price);
            section_title = view.findViewById(R.id.section_title);

            is_available.setTypeface(MyApplication.getMyTypeFaceMedium(context));
            price.setTypeface(MyApplication.getMyTypeFaceMedium(context));
            section_title.setTypeface(MyApplication.getMyTypeFaceMedium(context));
        }

    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(final OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

}
