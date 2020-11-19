package Adapter;

import android.content.Context;
import android.os.Build;
import android.text.Html;
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

import Data.Comment;
import Data.Lesson;
import androidx.recyclerview.widget.RecyclerView;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.MyViewHolder> {

    private Context context;
    private List<Comment> items;

    private OnItemClickListener onItemClickListener;

    public CommentAdapter(Context context, List<Comment> value) {
        this.context = context;
        this.items = value;
    }

    @NotNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_11, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.comment_name.setText(items.get(position).getUser().getFullName());
        holder.comment_text.setText(items.get(position).getText());

        if(items.get(position).getReply() != null){
            holder.reply_to_comment_text.setVisibility(View.VISIBLE);
            holder.reply_to_comment_name.setVisibility(View.VISIBLE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                holder.reply_to_comment_text.setText(Html.fromHtml(items.get(position).getReply().getText(), Html.FROM_HTML_MODE_COMPACT));
            } else {
                holder.reply_to_comment_text.setText(Html.fromHtml(items.get(position).getReply().getText()));
            }

            holder.reply_to_comment_name.setText(items.get(position).getReply().getUser().getFullName());
        } else{
            holder.reply_to_comment_text.setVisibility(View.GONE);
            holder.reply_to_comment_name.setVisibility(View.GONE);
        }


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

        TextView comment_name , comment_text , reply_to_comment_text , reply_to_comment_name;

        MyViewHolder(View view) {
            super(view);
            comment_name = view.findViewById(R.id.comment_name);
            comment_text = view.findViewById(R.id.comment_text);
            reply_to_comment_text = view.findViewById(R.id.reply_to_comment_text);
            reply_to_comment_name = view.findViewById(R.id.reply_to_comment_name);

            comment_text.setTypeface(MyApplication.getMyTypeFaceMedium(context));
            comment_name.setTypeface(MyApplication.getMyTypeFaceMedium(context));
            reply_to_comment_text.setTypeface(MyApplication.getMyTypeFaceMedium(context));
            reply_to_comment_name.setTypeface(MyApplication.getMyTypeFaceBold(context));
        }

    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(final OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

}
