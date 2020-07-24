package Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amoozeshmelli.BaseActivity;
import com.amoozeshmelli.MyApplication;
import com.amoozeshmelli.R;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.khizar1556.mkvideoplayer.MKPlayer;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import Adapter.CommentAdapter;
import Data.Comment;
import Data.Part;
import Tools.TransitionHelper;
import ViewModel.CommentViewModel;
import ViewModel.PartViewModel;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class ShowVideo extends TransitionHelper.BaseFragment {

    public View view;

    private List<Part> MainPart = new ArrayList<>();
    private List<Comment> MainComments = new ArrayList<>();
    private MKPlayer mkPlayer;

    private int order = 0;
    ShowVideo instance = this;

    int NumberTotalQuestion = 0;
    int Correct = 0;
    int Wrong = 0;

    CommentAdapter commentAdapter;

    public ShowVideo() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e("Fragments", "ShowVideo Created!");

        view = inflater.inflate(R.layout.show_video, container, false);

        view.findViewById(R.id.back_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseActivity.of(getActivity()).onBackPressed();
            }
        });

        ((TextView) view.findViewById(R.id.what_part)).setTypeface(MyApplication.getMyTypeFaceMedium(getContext()));
        ((TextView) view.findViewById(R.id.what_part_number)).setTypeface(MyApplication.getMyTypeFaceMedium(getContext()));
        ((TextView) view.findViewById(R.id.total)).setTypeface(MyApplication.getMyTypeFaceMedium(getContext()));
        ((TextView) view.findViewById(R.id.title_1)).setTypeface(MyApplication.getMyTypeFaceMedium(getContext()));
        ((TextView) view.findViewById(R.id.title)).setTypeface(MyApplication.getMyTypeFaceMedium(getContext()));
        ((TextView) view.findViewById(R.id.get_quiz)).setTypeface(MyApplication.getMyTypeFaceMedium(getContext()));


        ((TextView) view.findViewById(R.id.title)).setText(BaseActivity.of(getActivity()).SectionName);

        final EditText add_comment = view.findViewById(R.id.add_comment);
        add_comment.setTypeface(MyApplication.getMyTypeFaceMedium(getContext()));

        ((TextView) view.findViewById(R.id.add_comment)).setTypeface(MyApplication.getMyTypeFaceMedium(getContext()));

        view.findViewById(R.id.submit).setVisibility(View.GONE);

        add_comment.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(add_comment.getText().length() == 0)
                    view.findViewById(R.id.submit).setVisibility(View.GONE);
                else
                    view.findViewById(R.id.submit).setVisibility(View.VISIBLE);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }

        });

        view.findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final SweetAlertDialog pDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#447a96"));
                pDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialog) {
                        SweetAlertDialog alertDialog = (SweetAlertDialog) dialog;
                        TextView text = alertDialog.findViewById(R.id.content_text);
                        text.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        text.setSingleLine(false);
                        text.setMaxLines(2);
                        text.setLines(2);
                    }
                });
                pDialog.setTitleText("لطفا منتظر بمانید...");
                pDialog.setCancelable(false);
                pDialog.show();

                AndroidNetworking.post(MyApplication.Domain + "comment")
                        .setPriority(Priority.HIGH)
                        .addHeaders("Accept", "application/json")
                        .addHeaders("Authorization", "Bearer " + MyApplication.token)
                        .addBodyParameter("section_id", BaseActivity.of(getActivity()).SectionID + "")
                        .addBodyParameter("text", add_comment.getText().toString())
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject response) {

                                add_comment.setText("");

                                pDialog.dismissWithAnimation();
                                SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE);

                                sweetAlertDialog.setConfirmText("باشه");
                                sweetAlertDialog.setContentText("کامنت شما با موفقیت ثبت شد!");
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
                                sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.dismissWithAnimation();
                                    }
                                });
                                sweetAlertDialog.show();
                            }

                            @Override
                            public void onError(ANError anError) {
                                Log.e("error", anError.getErrorDetail() + " / " + anError.getErrorCode());
                                SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE);
                                add_comment.setText("");

                                sweetAlertDialog.setConfirmText("انصراف");
                                sweetAlertDialog.setContentText("خطا در ارتباط با سرور");
                                sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.dismissWithAnimation();
                                    }
                                });
                                sweetAlertDialog.show();
                            }

                        });
            }
        });

//        add_comment.//

        add_comment.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (add_comment.getText().toString().length() != 0) {
                    view.findViewById(R.id.submit).setVisibility(View.VISIBLE);
                } else {
                    view.findViewById(R.id.submit).setVisibility(View.GONE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.e("TextWatcherTest", "afterTextChanged:\t" + s.toString());
            }
        });

        commentAdapter = new CommentAdapter(getContext(), MainComments);

        RecyclerView comment_recycler_view = view.findViewById(R.id.comment_recyclerView);
        comment_recycler_view.setLayoutManager(new LinearLayoutManager(getContext()));

        comment_recycler_view.setAdapter(commentAdapter);

        ((TextView) view.findViewById(R.id.get_quiz)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                get_quiz_of_section();
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mkPlayer.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();

        Log.e("state", "onResume");

        mkPlayer = new MKPlayer(getActivity());
//        mkPlayer.playInFullScreen(true);

        mkPlayer.onComplete(new Runnable() {
            @SuppressLint("SourceLockedOrientationActivity")
            @Override
            public void run() {
                Log.e("Video", "Ended!");

                BaseActivity.getInstace().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

                order++;
                next_part();
            }
        });

        view.findViewById(R.id.app_video_top_box).setOnClickListener(null);
        view.findViewById(R.id.app_video_next).setOnClickListener(null);
        view.findViewById(R.id.app_video_previous).setOnClickListener(null);
        view.findViewById(R.id.app_video_lock).setOnClickListener(null);
    }

    @Override
    public void onConfigurationChanged(@NotNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        View player = view.findViewById(R.id.app_video_box);

        int currentOrientation = newConfig.orientation;
        if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            Log.e("state", "ORIENTATION_LANDSCAPE");

            player.getLayoutParams().height = MyApplication.getHeightOfScreen(getContext());
            player.getLayoutParams().width = MyApplication.getWidthOfScreen(getContext());

            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) player.getLayoutParams();
            params.setMargins(0, 0, 0, 0);
            player.setLayoutParams(params);

        } else {
            Log.e("state", "ORIENTATION_PORTRAIT");

            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) player.getLayoutParams();
            params.setMargins(
                    MyApplication.dpTopx(getContext(), 40),
                    MyApplication.dpTopx(getContext(), 120),
                    MyApplication.dpTopx(getContext(), 40), 0);
            player.setLayoutParams(params);

            player.getLayoutParams().height = MyApplication.dpTopx(getContext(), 200);

        }
    }

    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);

        BaseActivity.getInstace().loading.setVisibility(View.VISIBLE);

        Log.e("state", "onAttach");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                PartViewModel partViewModel = ViewModelProviders.of(instance).get(PartViewModel.class);
                partViewModel.init(MyApplication.Domain + "user_part/" + BaseActivity.of(getActivity()).SectionID);

                partViewModel.getModels().observe(instance, new Observer<List<Part>>() {
                    @Override
                    public void onChanged(List<Part> parts) {

                        if (parts.size() == 0)
                            return;

                        MainPart.addAll(parts);
                        order = 0;
                        if (MainPart.get(MainPart.size() - 1).getQuiz() == null)
                            ((TextView) view.findViewById(R.id.get_quiz)).setVisibility(View.GONE);
                        else {
                            if (MainPart.get(MainPart.size() - 1).getQuiz().getQuestions() == null)
                                ((TextView) view.findViewById(R.id.get_quiz)).setVisibility(View.GONE);
                            else if (MainPart.get(MainPart.size() - 1).getQuiz().getQuestions().size() == 0)
                                ((TextView) view.findViewById(R.id.get_quiz)).setVisibility(View.GONE);
                        }

                        ((TextView) view.findViewById(R.id.total)).setText(MainPart.size() + "");

                        BaseActivity.getInstace().loading.setVisibility(View.GONE);

                        next_part();
                    }
                });

                Log.e("data", "SectionID : " + BaseActivity.of(getActivity()).SectionID);

                CommentViewModel commentViewModel = ViewModelProviders.of(instance).get(CommentViewModel.class);
                commentViewModel.init(MyApplication.Domain + "comment/" + BaseActivity.of(getActivity()).SectionID);

                commentViewModel.getModels().observe(instance, new Observer<List<Comment>>() {
                    @Override
                    public void onChanged(List<Comment> comments) {

                        if (comments.size() == 0)
                            return;

                        MainComments.addAll(comments);

                        if (commentAdapter != null)
                            commentAdapter.notifyDataSetChanged();

                    }
                });

            }
        }, 100);

    }

    private void next_part() {

        Log.e("order", order + " / " + MainPart.size());

        if (order == MainPart.size()) {
            Log.e("order", MainPart.get(order - 1).isHas_ended() ? "true" : "false");

            final SweetAlertDialog pDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#447a96"));
            pDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialog) {
                    SweetAlertDialog alertDialog = (SweetAlertDialog) dialog;
                    TextView text = alertDialog.findViewById(R.id.content_text);
                    text.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    text.setSingleLine(false);
                    text.setMaxLines(2);
                    text.setLines(2);
                }
            });
            pDialog.setTitleText("لطفا منتظر بمانید...");
            pDialog.setCancelable(false);
            pDialog.show();

            float fomula = ((float) (Correct * 3) - Wrong) / (NumberTotalQuestion * 3);
            fomula *= 100;

            Log.e("fomula", fomula + " / " + Correct + " / " + Wrong + " / " + NumberTotalQuestion);

            AndroidNetworking.post(MyApplication.Domain + "end_section")
                    .setPriority(Priority.HIGH)
                    .addHeaders("Accept", "application/json")
                    .addHeaders("Authorization", "Bearer " + MyApplication.token)
                    .addBodyParameter("section_id", BaseActivity.of(getActivity()).SectionID + "")
                    .addBodyParameter("darsad", fomula + "")
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {

                            pDialog.dismissWithAnimation();
                            try {
                                SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE);
                                sweetAlertDialog.setContentText("درصد مشارکت شما در این بخش : " + response.getInt("darsad"));
                                sweetAlertDialog.setConfirmText("تایید");
                                sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.cancel();
                                        if (MainPart.get(order - 1).isHas_ended()) {
                                            get_quiz_of_section();
                                            return;
                                        }
                                    }
                                });
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
                                sweetAlertDialog.show();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void onError(ANError anError) {
                            Log.e("error", anError.getErrorDetail() + " / " + anError.getErrorCode());
                            SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE);

                            sweetAlertDialog.setConfirmText("انصراف");
                            sweetAlertDialog.setContentText("خطا در ارتباط با سرور");
                            sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                }
                            });
                            sweetAlertDialog.show();
                        }

                    });
            return;
        }

        final Part currentPart = MainPart.get(order);
        if (currentPart.isIs_video()) {

            Log.e("Part" , order + " / its video");


            if (order == 0) {
                if (mkPlayer != null) {
                    mkPlayer.play(currentPart.getVideo().getVideo_url());
//                mkPlayer.playInFullScreen(true);
                }
                ((TextView) view.findViewById(R.id.what_part_number)).setText((order + 1) + "");
                ((TextView) view.findViewById(R.id.title_1)).setText(BaseActivity.of(getActivity()).TeacherName);

            } else {

                if (MainPart.get(order - 1).isIs_video()) {

                    SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.NORMAL_TYPE);
                    sweetAlertDialog.setCancelable(false);
                    sweetAlertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                        @Override
                        public void onShow(DialogInterface dialog) {
                            SweetAlertDialog alertDialog = (SweetAlertDialog) dialog;
                            TextView title = alertDialog.findViewById(R.id.title_text);
                            title.setTextColor(getContext().getResources().getColor(R.color.white));

                            Button btn = alertDialog.findViewById(R.id.confirm_button);
                            btn.setBackgroundColor(getContext().getResources().getColor(R.color.colorPrimaryDark));

                            Button btn1 = alertDialog.findViewById(R.id.cancel_button);
                            btn1.setBackgroundColor(getContext().getResources().getColor(R.color.colorPrimaryDark));

                            TextView text = alertDialog.findViewById(R.id.content_text);
                            text.setTextColor(getContext().getResources().getColor(R.color.white));
                            text.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                            text.setSingleLine(false);
                            text.setMaxLines(4);
                            text.setLines(4);
                        }
                    });

                    sweetAlertDialog.setConfirmText("رفتن به پارت بعد");
                    sweetAlertDialog.setCancelText("مشاهده مجدد");
                    sweetAlertDialog.setContentText("این بخش به اتمام رسید!");
                    sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {

                            if (mkPlayer != null) {
                                mkPlayer.play(currentPart.getVideo().getVideo_url());
                            }
                            ((TextView) view.findViewById(R.id.what_part_number)).setText((order + 1) + "");
                            ((TextView) view.findViewById(R.id.title_1)).setText(BaseActivity.of(getActivity()).TeacherName);


                            sDialog.dismissWithAnimation();
                        }
                    });

                    sweetAlertDialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {

                            order--;
                            next_part();

                            sDialog.dismissWithAnimation();
                        }
                    });

                    sweetAlertDialog.show();
                } else {
                    if (mkPlayer != null) {
                        mkPlayer.play(currentPart.getVideo().getVideo_url());
                    }
                    ((TextView) view.findViewById(R.id.what_part_number)).setText((order + 1) + "");
                    ((TextView) view.findViewById(R.id.title_1)).setText(BaseActivity.of(getActivity()).TeacherName);

                }

            }

        } else {
            Log.e("Part" , order + " / its question");

            SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(getContext(),
                    SweetAlertDialog.NORMAL_TYPE);
            sweetAlertDialog.setCancelable(false);
            sweetAlertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialog) {
                    SweetAlertDialog alertDialog = (SweetAlertDialog) dialog;
                    TextView title = alertDialog.findViewById(R.id.title_text);
                    title.setTextColor(getContext().getResources().getColor(R.color.white));

                    Button btn = alertDialog.findViewById(R.id.confirm_button);
                    btn.setBackgroundColor(getContext().getResources().getColor(R.color.colorPrimaryDark));

                    Button btn1 = alertDialog.findViewById(R.id.cancel_button);
                    btn1.setBackgroundColor(getContext().getResources().getColor(R.color.colorPrimaryDark));

                    TextView text = alertDialog.findViewById(R.id.content_text);
                    text.setTextColor(getContext().getResources().getColor(R.color.white));
                    text.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    text.setSingleLine(false);
                    text.setMaxLines(4);
                    text.setLines(4);
                }
            });

            sweetAlertDialog.setConfirmText("رفتن به پارت بعد");
            sweetAlertDialog.setCancelText("مشاهده مجدد");
            sweetAlertDialog.setContentText("این بخش به اتمام رسید!");
            sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sDialog) {

                    NumberTotalQuestion++;

                    final MyApplication.CustomDialogClass2 cdd = new MyApplication.CustomDialogClass2(getContext(), MainPart.get(order).getQuestionPart());
                    cdd.show();
                    cdd.setCancelable(false);

                    cdd.setOnOkClickListener(new MyApplication.CustomDialogClass2.OnOkClickListener() {
                        @Override
                        public void onOkClick(boolean hit_true) {

                            cdd.dismiss();

                            SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.NORMAL_TYPE);
                            sweetAlertDialog.setCancelable(false);
                            sweetAlertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                                @Override
                                public void onShow(DialogInterface dialog) {
                                    SweetAlertDialog alertDialog = (SweetAlertDialog) dialog;
                                    TextView title = alertDialog.findViewById(R.id.title_text);
                                    title.setTextColor(getContext().getResources().getColor(R.color.white));

                                    Button btn = alertDialog.findViewById(R.id.confirm_button);
                                    btn.setBackgroundColor(getContext().getResources().getColor(R.color.colorPrimaryDark));

                                    Button btn1 = alertDialog.findViewById(R.id.cancel_button);
                                    btn1.setBackgroundColor(getContext().getResources().getColor(R.color.colorPrimaryDark));

                                    TextView text = alertDialog.findViewById(R.id.content_text);
                                    text.setTextColor(getContext().getResources().getColor(R.color.white));
                                    text.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                    text.setSingleLine(false);
                                    text.setMaxLines(4);
                                    text.setLines(4);
                                }
                            });

//                    AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                            if (hit_true) {
                                Correct++;

                                sweetAlertDialog.setConfirmText("پارت بعد");
                                sweetAlertDialog.setContentText("پاسخ شما صحیح بود!");
                                sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {

                                        sDialog.dismissWithAnimation();
                                        order++;
                                        next_part();
                                    }
                                });

                            } else {
                                Wrong++;

                                sweetAlertDialog.setConfirmText("بله");
                                sweetAlertDialog.setContentText("پاسخ شما اشتباه بود! آیا مایلید دوباره این قسمت را تماشا کنید؟");
                                sweetAlertDialog.setCancelText("پارت بعد");
                                sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {

                                        sDialog.dismissWithAnimation();

                                        while(true){
                                            order--;

                                            final Part temp_part = MainPart.get(order);

                                            if(temp_part.isIs_video() || order == 0){

                                                if(!temp_part.isIs_video())
                                                    break;
                                                if (mkPlayer != null) {
                                                    mkPlayer.play(temp_part.getVideo().getVideo_url());
                                                }
                                                ((TextView) view.findViewById(R.id.what_part_number)).setText((order + 1) + "");
                                                ((TextView) view.findViewById(R.id.title_1)).setText(BaseActivity.of(getActivity()).TeacherName);
                                                break;
                                            }
                                        }

//                                        next_part();
                                    }
                                });
                                sweetAlertDialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {

                                        sDialog.dismissWithAnimation();
                                        order++;
                                        next_part();
                                    }
                                });

                            }
                            sweetAlertDialog.setCancelable(false);
                            sweetAlertDialog.show();
                        }
                    });
                    sDialog.dismissWithAnimation();
                }
            });

            sweetAlertDialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sDialog) {
                    order--;
                    next_part();
                    sDialog.dismissWithAnimation();
                }
            });

            sweetAlertDialog.show();
        }

    }

    int Status = 0;

    private void get_quiz_of_section() {

        Log.e(" ", "quiz of section");

        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.NORMAL_TYPE);

        sweetAlertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                SweetAlertDialog alertDialog = (SweetAlertDialog) dialog;
                TextView title = alertDialog.findViewById(R.id.title_text);
                title.setTextColor(getContext().getResources().getColor(R.color.white));

                Button btn = alertDialog.findViewById(R.id.confirm_button);
                btn.setBackgroundColor(getContext().getResources().getColor(R.color.colorPrimaryDark));

                Button btn1 = alertDialog.findViewById(R.id.cancel_button);
                btn1.setBackgroundColor(getContext().getResources().getColor(R.color.colorPrimaryDark));

                TextView text = alertDialog.findViewById(R.id.content_text);
                text.setTextColor(getContext().getResources().getColor(R.color.white));
                text.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                text.setSingleLine(false);
                text.setMaxLines(6);
                text.setLines(6);
            }
        });

        if (MainPart.get(MainPart.size() - 1).getQuiz().getHas_paid()) {
            sweetAlertDialog.setConfirmText("بله");
            sweetAlertDialog.setCancelText("خیر");
            sweetAlertDialog.setContentText("آیا میخواهید آزمون پایان فصل را بدهید؟");
            Status = 1;
        } else {
            sweetAlertDialog.setConfirmText("خرید");
            sweetAlertDialog.setCancelText("بیخیال");

            if (MainPart.get(MainPart.size() - 1).getQuiz().isLocked()) {
                sweetAlertDialog.setContentText("آزمون تا تاریخ " + MainPart.get(MainPart.size() - 1).getQuiz().getShamsiQuizQate() +
                        " در دسترس نیست!" + "\n" + "همین حالا با " + MainPart.get(MainPart.size() - 1).getQuiz().getEarlyPrice() + "تومان بخرید.");
            } else {
                sweetAlertDialog.setContentText("این آزمون را میبایست با مبلغ "
                        + MainPart.get(MainPart.size() - 1).getQuiz().getPrice() + " تومان " + "کیف پول اصلی و " + " مبلغ " +
                        MainPart.get(MainPart.size() - 1).getQuiz().getGiftPrice() + " تومان از کیف پول هدیه تهیه کنید؟");
            }
            Status = 0;
        }

        sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sDialog) {

                if (Status == 1) {
                    BaseActivity.of(getActivity()).Quiz = MainPart.get(MainPart.size() - 1).getQuiz().getQuestions();
                    BaseActivity.of(getActivity()).QuizTitle = MainPart.get(MainPart.size() - 1).getQuiz().getQuizTitle();
                    BaseActivity.of(getActivity()).QuizID = MainPart.get(MainPart.size() - 1).getQuiz().getID();
                    BaseActivity.of(getActivity()).AddFragments(new Quiz(), "Quiz", false);
                    mkPlayer.onDestroy();
                } else {

                    final SweetAlertDialog pDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
                    pDialog.setOnShowListener(new DialogInterface.OnShowListener() {
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
                            text.setMaxLines(2);
                            text.setLines(2);
                        }
                    });
                    pDialog.getProgressHelper().setBarColor(Color.parseColor("#447a96"));
                    pDialog.setTitleText("لطفا چند لحظه صبر کنید!");
                    pDialog.setCancelable(false);
                    pDialog.show();

                    AndroidNetworking.post(MyApplication.Domain + "buy_quiz")
                            .setPriority(Priority.HIGH)
                            .addHeaders("Accept", "application/json")
                            .addHeaders("Authorization", "Bearer " + MyApplication.token)
                            .addBodyParameter("quiz_id", MainPart.get(MainPart.size() - 1).getQuiz().getID() + "")
                            .build()
                            .getAsJSONObject(new JSONObjectRequestListener() {
                                @Override
                                public void onResponse(JSONObject response) {

                                    pDialog.dismissWithAnimation();
                                    if (response.has("error")) {

                                        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE);
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
                                                text.setMaxLines(2);
                                                text.setLines(2);
                                            }
                                        });
                                        sweetAlertDialog.setConfirmText("تایید");

                                        try {
                                            sweetAlertDialog.setContentText(response.getString("error"));
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sDialog) {
                                                sDialog.dismissWithAnimation();
                                            }
                                        });
                                        sweetAlertDialog.show();

                                    } else {

                                        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE);
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

                                        sweetAlertDialog.setConfirmText("تایید");
                                        try {
                                            sweetAlertDialog.setContentText(response.getString("data"));
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                        sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sDialog) {
                                                sDialog.dismissWithAnimation();
                                                BaseActivity.of(getActivity()).Quiz = MainPart.get(MainPart.size() - 1).getQuiz().getQuestions();
                                                BaseActivity.of(getActivity()).QuizTitle = MainPart.get(MainPart.size() - 1).getQuiz().getQuizTitle();
                                                BaseActivity.of(getActivity()).QuizID = MainPart.get(MainPart.size() - 1).getQuiz().getID();
                                                BaseActivity.of(getActivity()).AddFragments(new Quiz(), "Quiz", false);
                                                sDialog.dismissWithAnimation();
                                                mkPlayer.onDestroy();
                                            }
                                        });

                                        sweetAlertDialog.show();
                                    }
                                }

                                @Override
                                public void onError(ANError anError) {
                                    Log.e("error", anError.getErrorDetail() + " / " + anError.getErrorCode());
                                    SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE);
                                    pDialog.dismissWithAnimation();
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
                                            text.setMaxLines(1);
                                            text.setLines(1);
                                        }
                                    });
                                    sweetAlertDialog.setConfirmText("انصراف");
                                    sweetAlertDialog.setContentText("خطا در ارتباط با سرور");
                                    sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            sDialog.dismissWithAnimation();
                                        }
                                    });
                                    sweetAlertDialog.show();
                                }

                            });

                }

                sDialog.dismissWithAnimation();
            }
        });

        sweetAlertDialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sDialog) {
                sDialog.dismissWithAnimation();

            }
        });

        sweetAlertDialog.show();
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
        Log.e("Fragments", "ShowVideo Destroyed!");
    }

    public Context getContext() {
        return BaseActivity.of(getActivity());
    }

}

