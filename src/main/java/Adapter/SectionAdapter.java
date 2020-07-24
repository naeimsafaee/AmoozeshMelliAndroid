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

import Data.Section;
import androidx.recyclerview.widget.RecyclerView;

public class SectionAdapter extends RecyclerView.Adapter<SectionAdapter.MyViewHolder> {

    private Context context;
    private List<Section> items;

    private OnItemClickListener onItemClickListener;

    public SectionAdapter(Context context, List<Section> value) {
        this.context = context;
        this.items = value;
    }

    @NotNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
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

        holder.is_available.setText(items.get(position).IsLocked() ?
                items.get(position).getShamsiOpeningDate()
                : "در دسترس");

        if (items.get(position).hasPaid()){
            holder.is_available.setText("خریداری شده");
            holder.price.setText("پرداخت شده");
        }else if(items.get(position).IsLocked()){
            holder.price.setText(items.get(position).getEarlyPrice() + " تومان پیش گشایش ");
        } else {
            holder.price.setText(items.get(position).getPrice() + " تومان " + "\n" +
                    items.get(position).getGiftPrice() + " تومان کیف هدیه");
        }

        if (position % 2 == 0)
            holder.section_title.setText(" | " + items.get(position).getTitle());
        else
            holder.section_title.setText(items.get(position).getTitle() + " | ");

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
