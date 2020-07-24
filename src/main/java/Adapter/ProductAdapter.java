package Adapter;

import android.content.Context;
import android.media.Image;
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
import Data.Product;
import androidx.recyclerview.widget.RecyclerView;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyViewHolder> {

    private Context context;
    private List<Product> items;

    private OnItemClickListener onItemClickListener;

    public ProductAdapter(Context context, List<Product> value) {
        this.context = context;
        this.items = value;
    }

    @NotNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_10, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.title.setText(items.get(position).getTitle());
        holder.price.setText(items.get(position).getPrice() + " تومان");
        holder.mode.setText(items.get(position).isDownload_able() ? "دانلودی" : "فیزیکی");

        Glide.with(context)
                .load(items.get(position).getImage_url())
                .centerCrop()
                .into(holder.image_view);

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

        ImageView image_view , wallet_image_view;

        TextView title, mode , price;

        MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.title);
            mode = view.findViewById(R.id.mode);
            price = view.findViewById(R.id.price);

            image_view = view.findViewById(R.id.image_view);
            wallet_image_view = view.findViewById(R.id.wallet_image_view);

            title.setTypeface(MyApplication.getMyTypeFaceMedium(context));
            mode.setTypeface(MyApplication.getMyTypeFaceMedium(context));
            price.setTypeface(MyApplication.getMyTypeFaceMedium(context));
        }

    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(final OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

}
