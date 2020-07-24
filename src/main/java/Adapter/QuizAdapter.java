package Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.amoozeshmelli.BaseActivity;
import com.amoozeshmelli.MyApplication;
import com.amoozeshmelli.R;
import com.amoozeshmelli.ShowPhotoActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import Data.Question;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class QuizAdapter extends RecyclerView.Adapter<QuizAdapter.MyViewHolder> {

    public boolean NeedShowAnswer = false;
    private Context context;
    private List<Question> items;

    private OnItemClickListener onItemClickListener;


    public QuizAdapter(Context context, List<Question> value) {
        this.context = context;
        this.items = value;

    }

    @NotNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        if (viewType == 0)
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_6, parent, false);
        else
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_7, parent, false);
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

        holder.clear_options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.radioGroup.clearCheck();
                BaseActivity.getInstace().mainAnswer[position] = 0;
            }
        });

//        holder.radioGroup.clearCheck();
        if (items.get(position).getAnswered_question() != 0){
            holder.radioGroup.check(items.get(position).getAnswered_question());
        } else {
            holder.radioGroup.clearCheck();
        }
        Log.e("Options" , "position : " + position + " / " + items.get(position).getAnswered_question());

        holder.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                items.get(position).setAnswered_question(checkedId);
            }
        });

        holder.helper_image_view.setImageDrawable(null);
        holder.question_title_image.setImageDrawable(null);

        holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
        holder.question_number.setBackgroundResource(R.drawable.bg_13);
        holder.question_number.setTextColor(context.getResources().getColor(R.color.white));

        holder.question_title.setText("");
        for (int i = 0; i < holder.options_image_view.length; i++) {
            holder.options_image_view[i].setImageDrawable(null);
            holder.options_tv[i].setText("");
//            holder.options_tv[i].setChecked(false);
        }

        holder.question_number.setText((position + 1) + "");

        holder.question_title.setVisibility(View.VISIBLE);

        if (items.get(position).getQuestionTitle().equals("null")) {

            holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.white));
            holder.question_number.setBackgroundResource(R.drawable.bg_25);
            holder.question_number.setTextColor(context.getResources().getColor(R.color.colorPrimary));

            holder.question_title.setVisibility(View.GONE);

            holder.question_title_image.setVisibility(View.VISIBLE);

            Glide.with(context)
                    .load(items.get(position).getImageTitleUrl())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.question_title_image);

            holder.question_title_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(context, ShowPhotoActivity.class);
                    intent.putExtra("image", items.get(position).getImageTitleUrl());
                    context.startActivity(intent);
                }
            });

        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                holder.question_title.setText(Html.fromHtml(items.get(position).getQuestionTitle(), Html.FROM_HTML_MODE_COMPACT));
            } else {
                holder.question_title.setText(Html.fromHtml(items.get(position).getQuestionTitle()));
            }

            if (!items.get(position).getImageTitleUrl().equals("null")) {
                holder.question_title_image.setVisibility(View.VISIBLE);
                holder.helper_image_view.setVisibility(View.VISIBLE);

                Glide.with(context)
                        .load(items.get(position).getImageTitleUrl())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(holder.helper_image_view);

                holder.helper_image_view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Log.e("Click", "Clicked");

                        Intent intent = new Intent(context, ShowPhotoActivity.class);
                        intent.putExtra("image", items.get(position).getImageTitleUrl());
                        context.startActivity(intent);
                    }
                });
            } else {
                holder.question_title_image.setVisibility(View.GONE);
            }

        }

        for (int i = 0; i < items.get(position).getOptions().size(); i++) {
            if (i > 3)
                break;
            holder.options_tv[i].setVisibility(View.VISIBLE);

            /*if (BaseActivity.getInstace().mainAnswer[position] ==
                    items.get(position).getOptions().get(i).getID()) {
                holder.options_tv[i].setChecked(true);
            } else {
                holder.options_tv[i].setChecked(false);
            }*/

            if (!items.get(position).getOptions().get(i).getOptionTitle().equals("null")) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    holder.options_tv[i].setText(Html.fromHtml(items.get(position).getOptions().get(i).getOptionTitle(), Html.FROM_HTML_MODE_COMPACT));
                } else {
                    holder.options_tv[i].setText(Html.fromHtml(items.get(position).getOptions().get(i).getOptionTitle()));
                }
            }

            if (NeedShowAnswer) {
                if (items.get(position).getOptions().get(i).getIs_correct()) {
                    holder.options_tv[i].setChecked(true);
                }
            }

            if (items.get(position).getOptions().get(i).getOptionTitle().equals("null")) {
                holder.options_image_view[i].setVisibility(View.VISIBLE);
                Glide.with(context)
                        .load(items.get(position).getOptions().get(i).getImageUrl())
                        .override(600, 200)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(holder.options_image_view[i]);
            }

            final int finalI = i;
            /*holder.options_tv[i].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if (BaseActivity.getInstace().mainAnswer == null)
                        return;

                    *//*if(!isChecked)
                        return;*//*

                    BaseActivity.getInstace().mainAnswer[position] =
                            items.get(position).getOptions().get(finalI).getID();
                }
            });*/

/*
            if (BaseActivity.getInstace().mainAnswer[position] == 0) {
//                holder.options_tv[i].setChecked(false);
            }*/

            final int finalI2 = i;
            holder.options_image_view[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(context, ShowPhotoActivity.class);
                    intent.putExtra("image", items.get(position).getOptions().get(finalI2).getImageUrl());
                    context.startActivity(intent);
                }
            });
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

        TextView question_title, question_number;

        RadioButton[] options_tv = new RadioButton[4];
        RadioGroup radioGroup;
        ImageView[] options_image_view = new ImageView[4];

        ImageView helper_image_view, clear_options;

        CardView cardView;

        ImageView question_title_image;

        MyViewHolder(View view) {
            super(view);
            question_title = view.findViewById(R.id.question_title);
            question_number = view.findViewById(R.id.question_number);
            cardView = view.findViewById(R.id.cardView);
            radioGroup = view.findViewById(R.id.group);
            clear_options = view.findViewById(R.id.clear_options);

            options_tv[0] = view.findViewById(R.id.radio_one);
            options_tv[1] = view.findViewById(R.id.radio_two);
            options_tv[2] = view.findViewById(R.id.radio_three);
            options_tv[3] = view.findViewById(R.id.radio_four);


            options_image_view[0] = view.findViewById(R.id.option_one_iv);
            options_image_view[1] = view.findViewById(R.id.option_two_iv);
            options_image_view[2] = view.findViewById(R.id.option_three_iv);
            options_image_view[3] = view.findViewById(R.id.option_four_iv);

            question_title_image = view.findViewById(R.id.question_title_image_view);

            helper_image_view = view.findViewById(R.id.helper_image_view);

            options_tv[0].setTypeface(MyApplication.getMyTypeFaceRegular(context));
            options_tv[1].setTypeface(MyApplication.getMyTypeFaceRegular(context));
            options_tv[2].setTypeface(MyApplication.getMyTypeFaceRegular(context));
            options_tv[3].setTypeface(MyApplication.getMyTypeFaceRegular(context));

            question_title.setTypeface(MyApplication.getMyTypeFaceRegular(context));
            question_number.setTypeface(MyApplication.getMyTypeFaceBold(context));
        }

    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(final OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

}
