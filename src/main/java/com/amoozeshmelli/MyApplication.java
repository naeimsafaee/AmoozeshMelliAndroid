package com.amoozeshmelli;

import android.app.Activity;
import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import Data.Product;
import Data.QuestionPart;
import androidx.appcompat.app.AlertDialog;


public class MyApplication extends Application implements
        ConnectivityReceiver.ConnectivityReceiverListener {

    final public static String Domain = "https://api.amoozeshmelli.com/api/v1/";

    public static String token = "";
    public static String PhoneNumber = "";
    public static int Wallet = 0;
    public static int GiftWallet = 0;
    public static String fullName = "";
    public static String Grade = "";
    public static String City = "";

    public static void retrieveToken() {
        SharedPreferences prefs = BaseActivity.getInstace().getSharedPreferences("MY_APP", MODE_PRIVATE);
        token = prefs.getString("token", "");
    }

    public static int pxTodp(Context context, float px) {
        if (context == null)
            return 0;
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public static Typeface getMyTypeFaceLight(Context context) {
        return Typeface.createFromAsset(context.getAssets(),
                "font/IRANSans(FaNum)_Light.ttf");
    }

    public static Typeface getMyTypeFaceRegular(Context context) {
        return Typeface.createFromAsset(context.getAssets(),
                "font/IRANSans(FaNum).ttf");
    }

    public static Typeface getMyTypeFaceMedium(Context context) {
        return Typeface.createFromAsset(context.getAssets(),
                "font/IRANSans(FaNum)_Medium.ttf");
    }

    public static Typeface getMyTypeFaceBold(Context context) {
        return Typeface.createFromAsset(context.getAssets(),
                "font/IRANSans(FaNum)_Bold.ttf");
    }

    public static String getVersionName(Context context) {

        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static int getWidthOfScreen(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        return displayMetrics.widthPixels;
    }

    public static int getHeightOfScreen(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        return displayMetrics.heightPixels;
    }

    public static int dpTopx(Context context, float dp) {
        if (context == null)
            return 0;
        Resources r = context.getResources();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
        return (int) px;
    }

    public static boolean isFirstRun() {

        SharedPreferences prefs = BaseActivity.getInstace().getSharedPreferences("MY_APP", MODE_PRIVATE);

        return prefs.getBoolean("is_first_run", true);
    }

    @Override
    public void onCreate() {
        super.onCreate();
//        AndroidNetworking.enableLogging();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            NetworkSchedulerService.listener = this;
        else
            ConnectivityReceiver.mConnectivityReceiverListener = this;
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {

        if (BaseActivity.getInstace() != null)
            BaseActivity.getInstace().onConnectionChanged(isConnected);
    }

    public static class CustomDialogClass extends Dialog {

        Context context;
        public Dialog d;
        String url;

        public CustomDialogClass(Context a, String url) {
            super(a);
            // TODO Auto-generated constructor stub
            this.context = a;
            this.url = url;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.image_view_dialog);

            ImageView imageView = findViewById(R.id.full_screen_image_view);

            Glide.with(context).load(url).into(imageView);
        }

    }
/*

    public static class CustomDialogClass1 extends Dialog {

        Context context;
        public Dialog d;


        private OnOkClickListener onOkClickListener;

        public CustomDialogClass1(Context a , ArrayList<String> cities , ArrayList<String> grades) {
            super(a);
            // TODO Auto-generated constructor stub
            this.context = a;
            this.cities = cities;
            this.grades = grades;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.dialog_1);

            set_spinner();

            Button ok = findViewById(R.id.submit);


            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onOkClickListener.onOkClick();
                }
            });

        }


        public interface OnOkClickListener {
            void onOkClick();
        }

        public void setOnOkClickListener(final OnOkClickListener onOkClickListener) {
            this.onOkClickListener = onOkClickListener;
        }

    }
*/
/*

    public static class CustomDialogClass3 extends Dialog {

        Context context;
        public Dialog d;

        private OnOkClickListener onOkClickListener;

        public CustomDialogClass3(Context a, Product product) {
            super(a);
            // TODO Auto-generated constructor stub
            this.context = a;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.dialog_2);



        }

        public interface OnOkClickListener {
            void onOkClick(Product product);
        }

        public void setOnOkClickListener(final OnOkClickListener onOkClickListener) {
            this.onOkClickListener = onOkClickListener;
        }

    }
*/

    public static class CustomDialogClass2 extends Dialog {

        Context context;
        public Dialog d;

        QuestionPart questionPart;

        private OnOkClickListener onOkClickListener;

        public CustomDialogClass2(Context a, QuestionPart questionPart) {
            super(a);
            // TODO Auto-generated constructor stub
            this.context = a;
            this.questionPart = questionPart;

        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.row_9);

            TextView question_number = findViewById(R.id.question_number);
            TextView question_title = findViewById(R.id.question_title);
            final ImageView question_title_image = findViewById(R.id.question_title_image_view);
            final ImageView helper_question_title_image = findViewById(R.id.helper_image_view);


            RadioButton[] options_tv = new RadioButton[4];
            final ImageView[] options_image_view = new ImageView[4];

            options_tv[0] = findViewById(R.id.radio_one);
            options_tv[1] = findViewById(R.id.radio_two);
            options_tv[2] = findViewById(R.id.radio_three);
            options_tv[3] = findViewById(R.id.radio_four);
            options_image_view[0] = findViewById(R.id.option_one_iv);
            options_image_view[1] = findViewById(R.id.option_two_iv);
            options_image_view[2] = findViewById(R.id.option_three_iv);
            options_image_view[3] = findViewById(R.id.option_four_iv);

            options_tv[0].setTypeface(MyApplication.getMyTypeFaceMedium(context));
            options_tv[1].setTypeface(MyApplication.getMyTypeFaceMedium(context));
            options_tv[2].setTypeface(MyApplication.getMyTypeFaceMedium(context));
            options_tv[3].setTypeface(MyApplication.getMyTypeFaceMedium(context));
            question_title.setTypeface(MyApplication.getMyTypeFaceMedium(context));
            question_number.setTypeface(MyApplication.getMyTypeFaceBold(context));

            question_number.setText("?");

            question_title.setVisibility(View.VISIBLE);

            if (questionPart.getQuestionTitle().equals("null")){
                question_title.setVisibility(View.GONE);

                Glide.with(context)
                        .load(questionPart.getImageTitleUrl())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(question_title_image);

                question_title_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Log.e("Click" , "Clicked");

                        Intent intent = new Intent(context, ShowPhotoActivity.class);
                        intent.putExtra("image", questionPart.getImageTitleUrl());
                        context.startActivity(intent);
                    }
                });


            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    question_title.setText(Html.fromHtml(questionPart.getQuestionTitle(), Html.FROM_HTML_MODE_COMPACT));
                } else {
                    question_title.setText(Html.fromHtml(questionPart.getQuestionTitle()));
                }
                if(!questionPart.getImageTitleUrl().equals("null")){
                    Glide.with(context)
                            .load(questionPart.getImageTitleUrl())
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(helper_question_title_image);

                    helper_question_title_image.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Log.e("Click" , "Clicked");

                            Intent intent = new Intent(context, ShowPhotoActivity.class);
                            intent.putExtra("image", questionPart.getImageTitleUrl());
                            context.startActivity(intent);
                        }
                    });

                }
            }


            for (int i = 0; i < questionPart.getOptions().size(); i++) {
                if (i > 3)
                    break;

                options_tv[i].setVisibility(View.VISIBLE);
                if (!questionPart.getOptions().get(i).getOptionTitle().equals("null")){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        options_tv[i].setText(Html.fromHtml(questionPart.getOptions().get(i).getOptionTitle(), Html.FROM_HTML_MODE_COMPACT));
                    } else {
                        options_tv[i].setText(Html.fromHtml(questionPart.getOptions().get(i).getOptionTitle()));
                    }
                }

                Glide.with(context)
                        .load(questionPart.getOptions().get(i).getImageUrl())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(options_image_view[i]);

                final int finalI = i;

                options_tv[i].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                        onOkClickListener.onOkClick(questionPart.getOptions().get(finalI).isIs_correct());
                    }
                });

                final int finalI1 = i;
                options_image_view[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Log.e("Click" , "Clicked");

                        Intent intent = new Intent(context, ShowPhotoActivity.class);
                        intent.putExtra("image", questionPart.getOptions().get(finalI1).getImageUrl());
                        context.startActivity(intent);
                    }
                });
            }

        }

        public interface OnOkClickListener {
            void onOkClick(boolean hit_true);
        }

        public void setOnOkClickListener(final OnOkClickListener onOkClickListener) {
            this.onOkClickListener = onOkClickListener;
        }

    }

    public static void restartApp(Context context) {
        Intent intent = new Intent(context, BaseActivity.class);
        context.startActivity(intent);
        ((Activity) context).finish();
    }

    public static void increase_wallet(final Context context){

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context, R.style.MyDialogTheme);

        final EditText input = new EditText(context);
        input.setHintTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        input.setTypeface(MyApplication.getMyTypeFaceMedium(context));
        input.setTextSize(14);
        input.setHint("مبلغ را به تومان وارد نمایید!");
        input.setPadding(
                MyApplication.dpTopx(context, 24),
                MyApplication.dpTopx(context, 8),
                MyApplication.dpTopx(context, 24),
                MyApplication.dpTopx(context, 8)
        );
        input.setBackgroundResource(R.drawable.edit_text);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(
                MyApplication.dpTopx(context, 20),
                MyApplication.dpTopx(context, 20),
                MyApplication.dpTopx(context, 20),
                MyApplication.dpTopx(context, 0)
        );
        input.setLayoutParams(lp);

        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                MyApplication.dpTopx(context, 65)));
        linearLayout.setWeightSum(3);

        final TextView tv1 = new TextView(context);
        tv1.setText("10000");
        tv1.setGravity(Gravity.CENTER);
        tv1.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
        tv1.setBackgroundResource(R.drawable.edit_text);
        tv1.setPadding(
                MyApplication.dpTopx(context, 12),
                MyApplication.dpTopx(context, 12),
                MyApplication.dpTopx(context, 12),
                MyApplication.dpTopx(context, 12)
        );
        LinearLayout.LayoutParams layoutParams0 = new LinearLayout.LayoutParams(0,
                LinearLayout.LayoutParams.WRAP_CONTENT, 1);
        layoutParams0.setMargins(
                MyApplication.dpTopx(context , 8),
                MyApplication.dpTopx(context , 8),
                MyApplication.dpTopx(context , 8),
                MyApplication.dpTopx(context , 8)
        );
        tv1.setLayoutParams(layoutParams0);tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pay(tv1.getText().toString() , context);
            }
        });

        final TextView tv2 = new TextView(context);
        tv2.setText("20000");
        tv2.setGravity(Gravity.CENTER);
        tv2.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
        tv2.setBackgroundResource(R.drawable.edit_text);
        tv2.setPadding(
                MyApplication.dpTopx(context, 12),
                MyApplication.dpTopx(context, 12),
                MyApplication.dpTopx(context, 12),
                MyApplication.dpTopx(context, 12)
        );
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
        layoutParams.setMargins(
                MyApplication.dpTopx(context , 8),
                MyApplication.dpTopx(context , 8),
                MyApplication.dpTopx(context , 8),
                MyApplication.dpTopx(context , 8)
        );
        tv2.setLayoutParams(layoutParams);
        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pay(tv2.getText().toString() , context);
            }
        });

        final TextView tv3 = new TextView(context);
        tv3.setText("50000");
        tv3.setGravity(Gravity.CENTER);
        tv3.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
        tv3.setBackgroundResource(R.drawable.edit_text);
        tv3.setPadding(
                MyApplication.dpTopx(context, 12),
                MyApplication.dpTopx(context, 12),
                MyApplication.dpTopx(context, 12),
                MyApplication.dpTopx(context, 12)
        );
        LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
        layoutParams1.setMargins(
                MyApplication.dpTopx(context , 8),
                MyApplication.dpTopx(context , 8),
                MyApplication.dpTopx(context , 8),
                MyApplication.dpTopx(context , 8)
        );
        tv3.setLayoutParams(layoutParams1);
        tv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pay(tv3.getText().toString() , context);
            }
        });

        LinearLayout relativeLayout = new LinearLayout(context);
        relativeLayout.setOrientation(LinearLayout.VERTICAL);

        LinearLayout l1 = new LinearLayout(context);
        LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT , ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams2.setMargins(
                MyApplication.dpTopx(context, 20),
                MyApplication.dpTopx(context, 20),
                MyApplication.dpTopx(context, 20),
                MyApplication.dpTopx(context, 0)
        );
        l1.setLayoutParams(layoutParams2);

        l1.setOrientation(LinearLayout.HORIZONTAL);
        l1.addView(tv1);
        l1.addView(tv2);
        l1.addView(tv3);

        relativeLayout.addView(input);
        relativeLayout.addView(l1);

        alertDialog.setPositiveButton("تایید", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                pay(input.getText().toString() , context);
            }
        });
        alertDialog.setView(relativeLayout);
        alertDialog.show();
    }

    private static void pay(String howMuch , final Context context) {

        AndroidNetworking.post(MyApplication.Domain + "increase_wallet")
                .setPriority(Priority.HIGH)
                .addHeaders("Accept", "application/json")
                .addHeaders("Authorization", "Bearer " + MyApplication.token)
                .addBodyParameter("amount", howMuch)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.e("data", response.toString());

                        try {
                            String url = response.getString("url");

                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                            context.startActivity(browserIntent);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("error", anError.getErrorDetail() + " / " + anError.getErrorCode());
                    }

                });
    }

}
