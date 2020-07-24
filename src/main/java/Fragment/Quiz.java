package Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.amoozeshmelli.BaseActivity;
import com.amoozeshmelli.MyApplication;
import com.amoozeshmelli.R;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.DownloadListener;
import com.androidnetworking.interfaces.DownloadProgressListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import Adapter.QuizAdapter;
import Data.Question;
import Tools.TransitionHelper;
import ViewModel.QuizViewModel;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class Quiz extends TransitionHelper.BaseFragment {

    public View view;

    private List<Question> MainQuiz = new ArrayList<>();
    private QuizAdapter quizAdapter;

//    private QuizViewModel quizViewModel;

    private int time_value;

    private Quiz instance = this;

    private TextView quiz_title;
    private TextView number_of_question;
    private TextView time_tv;

    public Quiz() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e("Fragments", "Quiz Created!");

        view = inflater.inflate(R.layout.quiz, container, false);

        view.findViewById(R.id.back_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseActivity.of(getActivity()).onBackPressed();
            }
        });

        FloatingActionButton floatingActionButton = view.findViewById(R.id.submit);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE);
                sweetAlertDialog.setTitleText("اتمام آزمون");
                sweetAlertDialog.setContentText("آیا می خواهید آزمون را تمام کنید؟");
                sweetAlertDialog.setConfirmText("بله");
                sweetAlertDialog.setCancelText("خیر");
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
                sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        hit_end();
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
        });

        quiz_title = view.findViewById(R.id.quiz_title);

        View time = view.findViewById(R.id.time);
        time_tv = view.findViewById(R.id.time_tv);

        number_of_question = view.findViewById(R.id.number_of_question);

        TextView title = view.findViewById(R.id.title);


        MainQuiz = BaseActivity.of(getActivity()).Quiz;

        quizAdapter = new QuizAdapter(getContext(), MainQuiz);

        if (MainQuiz.size() == 0) {
            get_quiz();
            time.setVisibility(View.VISIBLE);
            title.setText("آزمون");
        } else {
            set_titles();
            time.getLayoutParams().width = 0;
            title.setText("آزمون ساز");
//            quizAdapter.define(MainQuiz.size());
            BaseActivity.getInstace().mainAnswer = new int[MainQuiz.size()];
        }
        Log.e("data", MainQuiz.size() + "");

        quizAdapter.setOnItemClickListener(new QuizAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

            }
        });

        RecyclerView recyclerView = view.findViewById(R.id.quiz_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(quizAdapter);


        quiz_title.setTypeface(MyApplication.getMyTypeFaceMedium(getContext()));
        number_of_question.setTypeface(MyApplication.getMyTypeFaceMedium(getContext()));
        title.setTypeface(MyApplication.getMyTypeFaceMedium(getContext()));
        time_tv.setTypeface(MyApplication.getMyTypeFaceLight(getContext()));

        return view;
    }

    private void hit_end() {

        view.findViewById(R.id.submit).setVisibility(View.GONE);

        int[] temp = BaseActivity.getInstace().mainAnswer;

        if (temp == null)
            return;
        StringBuilder answers = new StringBuilder();
        StringBuilder questions = new StringBuilder();

        for (int i = 0; i < temp.length; i++) {

            answers.append(temp[i]);

            questions.append(MainQuiz.get(i).getID());

            if (i != temp.length - 1) {
                answers.append(",");
                questions.append(",");
            }

        }

        Log.e("quiz_id", BaseActivity.of(getActivity()).QuizID + "");
        Log.e("answers", answers.toString());
        Log.e("questions", questions.toString());
        Log.e("temp", BaseActivity.getInstace().mainAnswer.length + "");

        AndroidNetworking.post(MyApplication.Domain + "quiz_correction")
                .addHeaders("Accept", "application/json")
                .addHeaders("Authorization", "Bearer " + MyApplication.token)
                .addBodyParameter("answers", answers.toString())
                .addBodyParameter("questions", questions.toString())
                .addBodyParameter("quiz_id", BaseActivity.of(getActivity()).QuizID + "")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.e("Data", response.toString());
                        Log.e("Data", "quiz correction are here!");

                        final SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.NORMAL_TYPE);
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
                                text.setMaxLines(12);
                                text.setLines(12);
                            }
                        });
                        sweetAlertDialog.setConfirmText("باشه");

//                        AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                        try {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                sweetAlertDialog.setContentText("درصد شما در این آزمون : " + Html.fromHtml(response.getString("darsad") , Html.FROM_HTML_MODE_COMPACT));
                            } else {
                                sweetAlertDialog.setContentText("درصد شما در این آزمون : " + Html.fromHtml(response.getString("darsad")));
                            }

                            final String answer_file = response.getString("answer_file");

                            if (!answer_file.equals("null")) {
                                sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.dismissWithAnimation();
                                        startDownload(answer_file, BaseActivity.of(getActivity()).QuizTitle);
                                    }
                                });
                            } else {


//                                quizAdapter.NeedShowAnswer = true;
//                                quizAdapter.notifyItemRangeChanged(0, MainQuiz.size());

                                BaseActivity.of(getActivity()).Quiz = MainQuiz;

                                Toast.makeText(getContext(), "می توانید پاسخ سوالات را هم اکنون مشاهده فرمایید!", Toast.LENGTH_SHORT).show();

                                BaseActivity.of(getActivity()).AddFragments(new Answer() , "Answer" , false);

                                sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.dismissWithAnimation();
                                    }
                                });
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        sweetAlertDialog.show();
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("error", anError.getErrorDetail() + anError.getErrorCode());
                    }

                });

    }

    ProgressDialog mProgressDialog;

    private void startDownload(String file_path, String name) {

        File dir = new File(Environment.getExternalStorageDirectory(), "AmoozeshMelli");
        if (!dir.exists())
            dir.mkdirs();

        String ext = file_path.substring(file_path.lastIndexOf('.'));

        if (!file_path.contains("https"))
            file_path = file_path.replaceAll("http", "https");

        Log.e("Download", file_path);

        mProgressDialog = new ProgressDialog(getContext());
        mProgressDialog.setMessage("در حال دانلود فایل");
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setCancelable(true);

        // execute this when the downloader must be fired
        /*final DownloadTask downloadTask = new DownloadTask(getContext());
        downloadTask.execute(file_path);
*/
        mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                AndroidNetworking.forceCancel("download");
                dialog.dismiss();
            }
        });

        mProgressDialog.show();

        AndroidNetworking.download(file_path, dir.getAbsolutePath(), name + ext)
                .setTag("download")
                .setPriority(Priority.MEDIUM)
                .build()
                .setDownloadProgressListener(new DownloadProgressListener() {
                    @Override
                    public void onProgress(long bytesDownloaded, long totalBytes) {

                        Log.e("Download", "onProgress : " + bytesDownloaded + " / " + totalBytes);
                        mProgressDialog.setIndeterminate(false);
                        mProgressDialog.setMax(100);
                        mProgressDialog.setProgress((int) (bytesDownloaded / totalBytes * 100));

                    }
                })
                .startDownload(new DownloadListener() {
                    @Override
                    public void onDownloadComplete() {
                        Log.e("Download", "onDownloadComplete");
                        mProgressDialog.dismiss();

                        Toast.makeText(getContext(), "فایل پاسخ نامه شما در فولدر AmoozeshMelli ذخیره شد!", Toast.LENGTH_LONG).show();

                        BaseActivity.of(getActivity()).onBackPressed();
                        BaseActivity.of(getActivity()).onBackPressed();
                    }

                    @Override
                    public void onError(ANError error) {
                        Log.e("Download", error.getErrorDetail() + error.getErrorCode());

                    }
                });
    }

    private void set_titles() {
        quiz_title.setText(BaseActivity.of(getActivity()).QuizTitle);
        number_of_question.setText("تعداد  سوال  : " + MainQuiz.size());
    }

    private void get_quiz() {

        Log.e("data", "getting quiz!");

        final QuizViewModel quizViewModel = ViewModelProviders.of(instance).get(QuizViewModel.class);
        quizViewModel.init(MyApplication.Domain + "show_questions_of_quiz", BaseActivity.of(getActivity()).QuizID + "");

        quizViewModel.getModels().observe(instance, new Observer<List<Question>>() {
            @Override
            public void onChanged(List<Question> questions) {

                if (questions.size() == 0)
                    return;

                time_value = BaseActivity.of(getActivity()).QuizTime * 60;
                time_value++;

                Log.e("Time", "" + BaseActivity.of(getActivity()).QuizTime);

                MainQuiz.addAll(questions);
                if (quizAdapter != null) {
//                    quizAdapter.define(MainQuiz.size());
                    BaseActivity.getInstace().mainAnswer = new int[MainQuiz.size()];
                    quizAdapter.notifyDataSetChanged();
                }

                quizViewModel.getModels().removeObservers(instance);

                Log.e("data", "question are here , " + questions.size());
                set_titles();

                set_timer();

            }
        });

    }

    private void set_timer() {

        new CountDownTimer(time_value * 1000, 1000 * 60) {

            public void onTick(long millisUntilFinished) {
                time_tv.setText(checkDigit(time_value - 1));
                time_value -= 60;
            }

            public void onFinish() {
                hit_end();
                Log.e("time", "finish");
            }

            String checkDigit(int number) {

                int hour = number / 3600;
                number -= hour * 3600;
                int min = number / 60;

                return (hour <= 9 ? "0" + hour : hour)
                        + ":" +
                        (min <= 9 ? "0" + min : min);
            }

        }.start();

    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
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
        Log.e("Fragments", "Quiz Destroyed!");
    }

    public Context getContext() {
        return BaseActivity.of(getActivity());
    }

}

