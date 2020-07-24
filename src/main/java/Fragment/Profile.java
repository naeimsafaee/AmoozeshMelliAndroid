package Fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amoozeshmelli.BaseActivity;
import com.amoozeshmelli.MyApplication;
import com.amoozeshmelli.R;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import Tools.TransitionHelper;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;

import static android.content.Context.MODE_PRIVATE;

public class Profile extends TransitionHelper.BaseFragment {

    public View view;

    private Profile instance = this;

    TextView grade_tv, fullName, city_tv, wallet, gift_wallet;

    public Profile() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e("Fragments", "Profile Created!");

        view = inflater.inflate(R.layout.profile, container, false);

        view.findViewById(R.id.back_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseActivity.of(getActivity()).onBackPressed();
            }
        });

        fullName = view.findViewById(R.id.fullName);
        grade_tv = view.findViewById(R.id.grade_tv);
        city_tv = view.findViewById(R.id.city_tv);
        wallet = view.findViewById(R.id.wallet);
        gift_wallet = view.findViewById(R.id.gift_wallet);

        fullName.setText(MyApplication.fullName);
        grade_tv.setText("پایه تحصیلی : " + MyApplication.Grade);
        city_tv.setText("استان : " + MyApplication.City);
        wallet.setText("کیف پول : " + MyApplication.Wallet);
        gift_wallet.setText("کیف پول هدیه : " + MyApplication.GiftWallet);

        grade_tv.setTypeface(MyApplication.getMyTypeFaceMedium(Objects.requireNonNull(getContext())));
        fullName.setTypeface(MyApplication.getMyTypeFaceMedium(Objects.requireNonNull(getContext())));
        city_tv.setTypeface(MyApplication.getMyTypeFaceMedium(Objects.requireNonNull(getContext())));
        wallet.setTypeface(MyApplication.getMyTypeFaceMedium(Objects.requireNonNull(getContext())));
        gift_wallet.setTypeface(MyApplication.getMyTypeFaceMedium(Objects.requireNonNull(getContext())));


        TextView title = view.findViewById(R.id.title);
        title.setTypeface(MyApplication.getMyTypeFaceMedium(Objects.requireNonNull(getContext())));
        ((TextView) view.findViewById(R.id.add_price)).setTypeface(MyApplication.getMyTypeFaceMedium(Objects.requireNonNull(getContext())));
        title.setText("پروفایل");

        view.findViewById(R.id.increase_wallet).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext(), R.style.MyDialogTheme);

                final EditText input = new EditText(getContext());
                input.setHintTextColor(getContext().getResources().getColor(R.color.colorPrimaryDark));
                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                input.setTypeface(MyApplication.getMyTypeFaceMedium(getContext()));
                input.setTextSize(14);
                input.setHint("مبلغ را به تومان وارد نمایید!");
                input.setPadding(
                        MyApplication.dpTopx(getContext(), 24),
                        MyApplication.dpTopx(getContext(), 8),
                        MyApplication.dpTopx(getContext(), 24),
                        MyApplication.dpTopx(getContext(), 8)
                );
                input.setBackgroundResource(R.drawable.edit_text);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                lp.setMargins(
                        MyApplication.dpTopx(getContext(), 20),
                        MyApplication.dpTopx(getContext(), 20),
                        MyApplication.dpTopx(getContext(), 20),
                        MyApplication.dpTopx(getContext(), 0)
                );
                input.setLayoutParams(lp);

                LinearLayout linearLayout = new LinearLayout(getContext());
                linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, MyApplication.dpTopx(getContext(), 65)));
                linearLayout.setWeightSum(3);

                final TextView tv1 = new TextView(getContext());
                tv1.setText("10000");
                tv1.setGravity(Gravity.CENTER);
                tv1.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                tv1.setBackgroundResource(R.drawable.edit_text);
                tv1.setPadding(
                        MyApplication.dpTopx(getContext(), 12),
                        MyApplication.dpTopx(getContext(), 12),
                        MyApplication.dpTopx(getContext(), 12),
                        MyApplication.dpTopx(getContext(), 12)
                );
                LinearLayout.LayoutParams layoutParams0 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
                layoutParams0.setMargins(
                        MyApplication.dpTopx(getContext() , 8),
                        MyApplication.dpTopx(getContext() , 8),
                        MyApplication.dpTopx(getContext() , 8),
                        MyApplication.dpTopx(getContext() , 8)
                );
                tv1.setLayoutParams(layoutParams0);tv1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pay(tv1.getText().toString());
                    }
                });

                final TextView tv2 = new TextView(getContext());
                tv2.setText("20000");
                tv2.setGravity(Gravity.CENTER);
                tv2.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                tv2.setBackgroundResource(R.drawable.edit_text);
                tv2.setPadding(
                        MyApplication.dpTopx(getContext(), 12),
                        MyApplication.dpTopx(getContext(), 12),
                        MyApplication.dpTopx(getContext(), 12),
                        MyApplication.dpTopx(getContext(), 12)
                );
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
                layoutParams.setMargins(
                        MyApplication.dpTopx(getContext() , 8),
                        MyApplication.dpTopx(getContext() , 8),
                        MyApplication.dpTopx(getContext() , 8),
                        MyApplication.dpTopx(getContext() , 8)
                );
                tv2.setLayoutParams(layoutParams);
                tv2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pay(tv2.getText().toString());
                    }
                });

                final TextView tv3 = new TextView(getContext());
                tv3.setText("50000");
                tv3.setGravity(Gravity.CENTER);
                tv3.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                tv3.setBackgroundResource(R.drawable.edit_text);
                tv3.setPadding(
                        MyApplication.dpTopx(getContext(), 12),
                        MyApplication.dpTopx(getContext(), 12),
                        MyApplication.dpTopx(getContext(), 12),
                        MyApplication.dpTopx(getContext(), 12)
                );
                LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
                layoutParams1.setMargins(
                        MyApplication.dpTopx(getContext() , 8),
                        MyApplication.dpTopx(getContext() , 8),
                        MyApplication.dpTopx(getContext() , 8),
                        MyApplication.dpTopx(getContext() , 8)
                );
                tv3.setLayoutParams(layoutParams1);
                tv3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pay(tv3.getText().toString());
                    }
                });

                LinearLayout relativeLayout = new LinearLayout(getContext());
                relativeLayout.setOrientation(LinearLayout.VERTICAL);

                LinearLayout l1 = new LinearLayout(getContext());
                LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT , ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams2.setMargins(
                        MyApplication.dpTopx(getContext(), 20),
                        MyApplication.dpTopx(getContext(), 20),
                        MyApplication.dpTopx(getContext(), 20),
                        MyApplication.dpTopx(getContext(), 0)
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
                        pay(input.getText().toString());
                    }
                });
                alertDialog.setView(relativeLayout);
                alertDialog.show();

            }
        });

        view.findViewById(R.id.item0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*if(cities.size() == 0 || grades.size() == 0)
                    return;*/

                BaseActivity.of(getActivity()).AddFragments(new EditProfile(), "EditProfile", false);
            }
        });

        view.findViewById(R.id.item1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        view.findViewById(R.id.item2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://app.amoozeshmelli.com/terms";

                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(browserIntent);
            }
        });

        view.findViewById(R.id.item3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = BaseActivity.getInstace().getSharedPreferences("MY_APP", MODE_PRIVATE).edit();
                editor.putBoolean("is_first_run", true);
                editor.apply();
                MyApplication.restartApp(getContext());
            }
        });

        ((TextView) view.findViewById(R.id.item0_tv)).setTypeface(MyApplication.getMyTypeFaceMedium(Objects.requireNonNull(getContext())));
        ((TextView) view.findViewById(R.id.item1_tv)).setTypeface(MyApplication.getMyTypeFaceMedium(Objects.requireNonNull(getContext())));
        ((TextView) view.findViewById(R.id.item2_tv)).setTypeface(MyApplication.getMyTypeFaceMedium(Objects.requireNonNull(getContext())));
        ((TextView) view.findViewById(R.id.item3_tv)).setTypeface(MyApplication.getMyTypeFaceMedium(Objects.requireNonNull(getContext())));


        return view;
    }

    private void pay(String howMuch) {

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
                            startActivity(browserIntent);

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

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);

        BaseActivity.getInstace().loading.setVisibility(View.VISIBLE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                AndroidNetworking.post(MyApplication.Domain + "profile")
                        .addHeaders("Accept", "application/json")
                        .addHeaders("Authorization", "Bearer " + MyApplication.token)
                        .setPriority(Priority.HIGH)
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {

                                    Log.e("error", "products are here!");

                                    JSONObject data = response.getJSONObject("data");
                                    MyApplication.PhoneNumber = data.getString("phone");
                                    MyApplication.fullName = data.getString("fullName");
                                    MyApplication.Wallet = Integer.parseInt(data.getString("wallet"));
                                    MyApplication.GiftWallet = Integer.parseInt(data.getString("gift_wallet"));
                                    MyApplication.City = data.getJSONObject("city").getString("name");
                                    MyApplication.Grade = data.getJSONObject("grade").getString("title");

                                    if (fullName != null) {
                                        fullName.setText(MyApplication.fullName);
                                        grade_tv.setText("پایه تحصیلی : " + MyApplication.Grade);
                                        city_tv.setText("استان : " + MyApplication.City);
                                        wallet.setText("کیف پول : " + MyApplication.Wallet);
                                        gift_wallet.setText("کیف پول هدیه : " + MyApplication.GiftWallet);
                                    }

                                    BaseActivity.getInstace().loading.setVisibility(View.GONE);

                                } catch (JSONException e) {
                                }
                            }

                            @Override
                            public void onError(ANError anError) {
                                int errorCode = anError.getErrorCode();
                                Log.e("error", anError.getErrorDetail() + " / " + errorCode);
                                if (errorCode == 403)
                                    BaseActivity.getInstace().failure();
                            }
                        });

            }
        }, 100);

    }


    @Override
    public boolean onBeforeBack() {
//        BaseActivity activity = BaseActivity.of(getActivity());
        /*if (!activity.animateHomeIcon(MaterialMenuDrawable.IconState.BURGER)) {
            activity.drawerLayout.openDrawer(Gravity.START);
        }*/
        return super.onBeforeBack();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("Fragments", "Profile Destroyed!");
    }

    public Context getContext() {
        return BaseActivity.of(getActivity());
    }

}

