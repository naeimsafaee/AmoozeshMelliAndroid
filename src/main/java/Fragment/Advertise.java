package Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.amoozeshmelli.BaseActivity;
import com.amoozeshmelli.MyApplication;
import com.amoozeshmelli.R;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.gson.Gson;
import com.khizar1556.mkvideoplayer.IjkVideoView;
import com.khizar1556.mkvideoplayer.MKPlayer;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import Tools.TransitionHelper;
import ViewModel.CityViewModel;
import ViewModel.GradeViewModel;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class Advertise extends TransitionHelper.BaseFragment {

    public View view;

    private Spinner spinner, spinner1;
    private ArrayAdapter<String> adapter, adapter1;

    private CityViewModel cityViewModel;
    private GradeViewModel gradeViewModel;

    private ArrayList<String> cities = new ArrayList<>();
    private ArrayList<String> grades = new ArrayList<>();

    private ArrayList<Data.City> MainCities = new ArrayList<>();
    private ArrayList<Data.Grade> MainGrades = new ArrayList<>();

    private Advertise instance = this;

    Data.Advertise advertise;

    MKPlayer mkPlayer;

    public Advertise() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e("Fragments", "Advertise Created!");

        view = inflater.inflate(R.layout.advertise, container, false);

        return view;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onResume() {
        super.onResume();

        mkPlayer = new MKPlayer(getActivity());

        SeekBar seekBar = view.findViewById(R.id.app_video_seekBar);
        seekBar.setAlpha(0);
        seekBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        IjkVideoView ijkVideoView = view.findViewById(R.id.video_view);
        ijkVideoView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.e("slide", "slide");
                return true;
            }
        });

        mkPlayer.onComplete(new Runnable() {
            @SuppressLint("SourceLockedOrientationActivity")
            @Override
            public void run() {
                Log.e("Video", "Ended!");

                BaseActivity.getInstace().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

                BaseActivity.of(getActivity()).bottomNavigation.setVisibility(View.VISIBLE);

                AndroidNetworking.post(MyApplication.Domain + "end_advertise/" + advertise.getId())
                        .addHeaders("Accept", "application/json")
                        .addHeaders("Authorization", "Bearer " + MyApplication.token)
                        .setPriority(Priority.HIGH)
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject response) {

                                SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.NORMAL_TYPE);

                                sweetAlertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                                    @Override
                                    public void onShow(DialogInterface dialog) {
                                        SweetAlertDialog alertDialog = (SweetAlertDialog) dialog;
                                        TextView title = alertDialog.findViewById(R.id.title_text);
                                        title.setTextColor(getContext().getResources().getColor(R.color.white));

                                        Button btn = alertDialog.findViewById(R.id.confirm_button);
                                        btn.setBackgroundColor(getContext().getResources().getColor(R.color.colorPrimaryDark));

                                        TextView text = alertDialog.findViewById(R.id.content_text);
                                        text.setTextColor(getContext().getResources().getColor(R.color.white));
                                        text.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                        text.setSingleLine(false);
                                        text.setMaxLines(3);
                                        text.setLines(3);
                                    }
                                });

                                sweetAlertDialog.setContentText("شما با موفقیت این تبلیغ را مشاهده کردید!");
                                sweetAlertDialog.setConfirmText("باشه");
                                sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.dismissWithAnimation();
                                        BaseActivity.of(getActivity()).onBackPressed();
                                    }
                                });

                                sweetAlertDialog.setCancelable(false);
                                sweetAlertDialog.show();

                            }

                            @Override
                            public void onError(ANError anError) {
                                int errorCode = anError.getErrorCode();
                                Log.e("error", anError.getErrorDetail() + " / " + errorCode);
                                if (errorCode == 401 || errorCode == 403)
                                    BaseActivity.getInstace().failure();
                            }
                        });

            }
        });
    }

    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                AndroidNetworking.post(MyApplication.Domain + "get_random_advertise")
                        .addHeaders("Accept", "application/json")
                        .addHeaders("Authorization", "Bearer " + MyApplication.token)
                        .setPriority(Priority.HIGH)
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {

                                    if (response.getInt("data_count") == 0) {
                                        show_error();
                                        return;
                                    }

                                    Log.e("error", "comments are here!");

                                    advertise = new Gson().fromJson(response.getJSONObject("data").toString(), Data.Advertise.class);

                                    mkPlayer.play(advertise.getVideo_url());
//                                    mkPlayer.playInFullScreen(true);
                                    BaseActivity.of(getActivity()).bottomNavigation.setVisibility(View.GONE);

                                    view.findViewById(R.id.progress_circular).setVisibility(View.GONE);

                                } catch (JSONException e) {
                                    show_error();
                                    Log.e("error", Objects.requireNonNull(e.getMessage()));
                                }

                            }

                            @Override
                            public void onError(ANError anError) {
                                int errorCode = anError.getErrorCode();
                                Log.e("error", anError.getErrorDetail() + " / " + errorCode);
                                if (errorCode == 401 || errorCode == 403)
                                    BaseActivity.getInstace().failure();
                            }
                        });

            }
        }, 500);


    }

    private void show_error() {

        view.findViewById(R.id.progress_circular).setVisibility(View.GONE);


        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.NORMAL_TYPE);

        sweetAlertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                SweetAlertDialog alertDialog = (SweetAlertDialog) dialog;
                TextView title = alertDialog.findViewById(R.id.title_text);
                title.setTextColor(getContext().getResources().getColor(R.color.white));

                Button btn = alertDialog.findViewById(R.id.confirm_button);
                btn.setBackgroundColor(getContext().getResources().getColor(R.color.colorPrimaryDark));

                TextView text = alertDialog.findViewById(R.id.content_text);
                text.setTextColor(getContext().getResources().getColor(R.color.white));
                text.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                text.setSingleLine(false);
                text.setMaxLines(6);
                text.setLines(6);
            }
        });

        sweetAlertDialog.setContentText("تبلیغی برای نمایش وجود ندارد!");
        sweetAlertDialog.setConfirmText("باشه");
        sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sDialog) {
                sDialog.dismissWithAnimation();
                BaseActivity.of(getActivity()).onBackPressed();
            }
        });

        sweetAlertDialog.setCancelable(false);
        sweetAlertDialog.show();

    }

    @Override
    public boolean onBeforeBack() {
        return super.onBeforeBack();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mkPlayer.onDestroy();
        Log.e("Fragments", "Advertise Destroyed!");
    }

    public Context getContext() {
        return BaseActivity.of(getActivity());
    }

}

