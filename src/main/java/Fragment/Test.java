package Fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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

import java.util.ArrayList;
import java.util.List;

import Adapter.CompleteQuizAdapter;
import Data.Lesson;
import Data.Question;
import Data.Subject;
import Tools.TransitionHelper;
import ViewModel.CompleteQuizViewModel;
import ViewModel.LessonViewModel;
import ViewModel.QuizViewModel;
import ViewModel.SubjectViewModel;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class Test extends TransitionHelper.BaseFragment {

    public View view;
    private LessonViewModel lessonViewModel;
    private Test instance = this;

    private ArrayList<String> lesson_titles = new ArrayList<>();
    private ArrayList<String> subject_titles = new ArrayList<>();

    private ArrayList<Lesson> MainLessons = new ArrayList<>();
    private ArrayList<Subject> MainSubject = new ArrayList<>();
    private ArrayList<Data.Quiz> MainQuiz = new ArrayList<>();

    private ArrayAdapter<String> adapter;
    private ArrayAdapter<String> adapter1;

    private EditText q_number;
    private int Status = -1;

    private Spinner spinner, spinner1;

    private RecyclerView complete_quiz_recycler;

    private CompleteQuizAdapter completeQuizAdapter;

    int StartNewFrom = 0;

    public Test() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e("Fragments", "Test Created!");

        view = inflater.inflate(R.layout.test, container, false);

        BaseActivity.of(getActivity()).QuizID = 0;
        BaseActivity.of(getActivity()).Quiz = new ArrayList<>();
        BaseActivity.of(getActivity()).QuizTitle = "";
        BaseActivity.of(getActivity()).QuizTime = -1;

        view.findViewById(R.id.back_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseActivity.of(getActivity()).onBackPressed();
            }
        });

        complete_quiz_recycler = view.findViewById(R.id.complete_quiz_recycler);

        completeQuizAdapter = new CompleteQuizAdapter(getContext(), MainQuiz);
        complete_quiz_recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        complete_quiz_recycler.setAdapter(completeQuizAdapter);

        completeQuizAdapter.setOnItemClickListener(new CompleteQuizAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(final int position) {

                Log.e("QuizID" , MainQuiz.get(position).getID() + "");

                if (MainQuiz.get(position).getHasPaid()) {

                    BaseActivity.of(getActivity()).QuizID = MainQuiz.get(position).getID();
                    BaseActivity.of(getActivity()).QuizTime = MainQuiz.get(position).getQuizTime();
                    BaseActivity.of(getActivity()).QuizTitle = MainQuiz.get(position).getQuizTitle();
                    BaseActivity.of(getActivity()).AddFragments(new Quiz(), "Quiz", false);
                    return;
                } else {

//                    MyApplication.CustomDialogClass1 cdd;

                    SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.NORMAL_TYPE);

                    sweetAlertDialog.setConfirmText("تایید");
                    sweetAlertDialog.setTitleText("خرید آزمون");
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
                            text.setMaxLines(4);
                            text.setLines(4);
                        }
                    });
                    if (MainQuiz.get(position).isLocked()) {
                        if (MyApplication.Wallet >= MainQuiz.get(position).getPrice()) {
                            Status = 1;
                            sweetAlertDialog.setContentText("زمان بازگشایی این آزمون فرانرسیده است.آیا مایل هستید با مبلغ بیشتری آن را بخرید؟");
                        } else {
                            Status = 0;
                            sweetAlertDialog.setContentText("موجودی کیف پول شما کافی نیست؟آیا میخواهیید آن را افزایش دهید؟");
                        }
                    } else {
                        if (MyApplication.Wallet >= MainQuiz.get(position).getPrice()) {
                            Status = 1;
                            sweetAlertDialog.setContentText("آیا میخواهید این آزمون را بخرید؟");
                        } else {
                            Status = 0;
                            sweetAlertDialog.setContentText("موجودی کیف پول شما کافی نیست! آیا میخواهیید آن را افزایش دهید؟");
                        }
                    }

                    sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {

                            if (Status == 0) {
                                //go to profile
                                MyApplication.increase_wallet(getContext());
                            } else {
                                AndroidNetworking.post(MyApplication.Domain + "buy_quiz")
                                        .setPriority(Priority.HIGH)
                                        .addHeaders("Accept", "application/json")
                                        .addHeaders("Authorization", "Bearer " + MyApplication.token)
                                        .addBodyParameter("quiz_id", MainQuiz.get(position).getID() + "")
                                        .build()
                                        .getAsJSONObject(new JSONObjectRequestListener() {
                                            @Override
                                            public void onResponse(JSONObject response) {

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
                                                    sweetAlertDialog.setCancelText("افزایش اعتبار");
                                                    sweetAlertDialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                        @Override
                                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                            MyApplication.increase_wallet(getContext());
                                                            sweetAlertDialog.dismiss();
                                                        }
                                                    });

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
                                                            text.setMaxLines(2);
                                                            text.setLines(2);
                                                        }
                                                    });
                                                    MainQuiz.get(position).setHasPaid(true);
                                                    completeQuizAdapter.notifyDataSetChanged();
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
                                                            BaseActivity.of(getActivity()).QuizID = MainQuiz.get(position).getID();
                                                            BaseActivity.of(getActivity()).QuizTime = MainQuiz.get(position).getQuizTime();
                                                            BaseActivity.of(getActivity()).QuizTitle = MainQuiz.get(position).getQuizTitle();
                                                            BaseActivity.of(getActivity()).AddFragments(new Quiz(), "Quiz", false);

//                                                            BaseActivity.of(getActivity()).AddFragments(new ShowVideo(), "ShowVideo", false);
                                                        }
                                                    });

                                                    sweetAlertDialog.show();
                                                }
                                            }

                                            @Override
                                            public void onError(ANError anError) {
                                                Log.e("error", anError.getErrorDetail() + " / " + anError.getErrorCode());
                                            }

                                        });
                            }

                            sDialog.dismissWithAnimation();
                        }
                    });

                    sweetAlertDialog.show();

                }
            }
        });

        TextView textView = view.findViewById(R.id.title);
        textView.setText("آزمون آنلاین");
        textView.setTypeface(MyApplication.getMyTypeFaceMedium(getActivity()));

        final TextView random_quiz = view.findViewById(R.id.is_global);
        random_quiz.setTypeface(MyApplication.getMyTypeFaceMedium(getActivity()));

        final TextView complete_quiz = view.findViewById(R.id.is_not_global);
        complete_quiz.setTypeface(MyApplication.getMyTypeFaceMedium(getActivity()));

        random_quiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                random_quiz.setBackgroundResource(R.drawable.bg_10);
                random_quiz.setTextColor(getResources().getColor(R.color.white));

                complete_quiz.setBackgroundResource(R.drawable.bg_1);
                complete_quiz.setTextColor(getResources().getColor(R.color.colorPrimaryDark));

                view.findViewById(R.id.random_quiz_relative).setVisibility(View.VISIBLE);
                complete_quiz_recycler.setVisibility(View.GONE);
            }
        });

        complete_quiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                complete_quiz.setBackgroundResource(R.drawable.bg_10);
                complete_quiz.setTextColor(getResources().getColor(R.color.white));

                random_quiz.setBackgroundResource(R.drawable.bg_1);
                random_quiz.setTextColor(getResources().getColor(R.color.colorPrimaryDark));

                view.findViewById(R.id.random_quiz_relative).setVisibility(View.GONE);
                complete_quiz_recycler.setVisibility(View.VISIBLE);
            }
        });

        q_number = view.findViewById(R.id.q_number);
        q_number.setTypeface(MyApplication.getMyTypeFaceMedium(getActivity()));

        Button submit = view.findViewById(R.id.submit);
        submit.setTypeface(MyApplication.getMyTypeFaceMedium(getActivity()));

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (q_number.getText().length() == 0)
                    return;
                if (spinner.getSelectedItemPosition() == 0)
                    return;

                BaseActivity.of(getActivity()).QuizTitle = MainLessons.get(spinner.getSelectedItemPosition() - 1).getTitle();
                BaseActivity.of(getActivity()).QuizNumber = q_number.getText().toString();

                get_quiz();
            }
        });

        spinner = view.findViewById(R.id.spinner_one);
        spinner1 = view.findViewById(R.id.spinner_two);

        spinner.setBackgroundResource(R.drawable.edit_text);
        spinner1.setBackgroundResource(R.drawable.edit_text);

        lesson_titles.add("انتخاب درس مورد نظر");
        adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, lesson_titles) {

            @NotNull
            public View getView(int position, View convertView, @NotNull ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                TextView tv = ((TextView) v);
                tv.setTypeface(MyApplication.getMyTypeFaceMedium(getContext()));
                tv.setPadding(
                        MyApplication.dpTopx(getContext(), 24),
                        MyApplication.dpTopx(getContext(), 8),
                        MyApplication.dpTopx(getContext(), 24),
                        MyApplication.dpTopx(getContext(), 8));

                return v;
            }

            public View getDropDownView(int position, View convertView, @NotNull ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);
                ((TextView) v).setTypeface(MyApplication.getMyTypeFaceMedium(getContext()));
                TextView tv = ((TextView) v);
                tv.setTypeface(MyApplication.getMyTypeFaceMedium(getContext()));
                tv.setPadding(
                        MyApplication.dpTopx(getContext(), 24),
                        MyApplication.dpTopx(getContext(), 8),
                        MyApplication.dpTopx(getContext(), 24),
                        MyApplication.dpTopx(getContext(), 8));
                return v;
            }
        };
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0)
                    return;
                int LessonID = MainLessons.get(position - 1).getID();

                Log.e("data", LessonID + " selected!");

                getSubject(LessonID);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

        subject_titles.add("انتخاب فصل مورد نظر");
        adapter1 = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, subject_titles) {

            @NotNull
            public View getView(int position, View convertView, @NotNull ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                ((TextView) v).setTypeface(MyApplication.getMyTypeFaceMedium(getContext()));

                return v;
            }

            public View getDropDownView(int position, View convertView, @NotNull ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);
                ((TextView) v).setTypeface(MyApplication.getMyTypeFaceMedium(getContext()));
                return v;
            }
        };
        spinner1.setAdapter(adapter1);

        /*ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(
                getActivity(), R.layout.spinner_item, years);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);*/

        return view;
    }

    private void get_quiz() {

        BaseActivity.of(getActivity()).Quiz = new ArrayList<>();

        BaseActivity.getInstace().loading.setVisibility(View.VISIBLE);

        Log.e("lesson id", MainLessons.get(spinner.getSelectedItemPosition() - 1).getID() + "");
        if (spinner1.getSelectedItemPosition() != 0)
            Log.e("subject id", MainSubject.get(spinner1.getSelectedItemPosition() - 1).getID() + "");

        final QuizViewModel quizViewModel = ViewModelProviders.of(instance).get(QuizViewModel.class);

        if (spinner1.getSelectedItemPosition() == 0) {
            quizViewModel.init(MyApplication.Domain + "make_quiz", q_number.getText().toString(),
                    MainLessons.get(spinner.getSelectedItemPosition() - 1).getID()
            );
        } else {
            quizViewModel.init(MyApplication.Domain + "make_quiz", q_number.getText().toString(),
                    MainLessons.get(spinner.getSelectedItemPosition() - 1).getID(),
                    MainSubject.get(spinner1.getSelectedItemPosition() - 1).getID()
            );
        }

        quizViewModel.getModels().observe(instance, new Observer<List<Question>>() {
            @Override
            public void onChanged(List<Question> questions) {

                if (questions.size() == 0)
                    return;

                BaseActivity.of(getActivity()).Quiz.addAll(questions);
                BaseActivity.of(getActivity()).AddFragments(new Quiz(), "Quiz", false);

                quizViewModel.getModels().removeObservers(instance);

                BaseActivity.getInstace().loading.setVisibility(View.GONE);

                Log.e("data", "question are here , " + questions.size());
            }
        });

    }

    private void getSubject(int lessonID) {

        MainSubject.clear();
        subject_titles.clear();

        subject_titles.add("انتخاب فصل مورد نظر");

        if (adapter1 != null)
            adapter1.notifyDataSetChanged();

        SubjectViewModel subjectViewModel = ViewModelProviders.of(instance).get(SubjectViewModel.class);
        subjectViewModel.init(MyApplication.Domain + "subjects_of_lesson_of_user", lessonID);

        subjectViewModel.getModels().observe(instance, new Observer<List<Subject>>() {
            @Override
            public void onChanged(List<Subject> subjects) {

                if (subjects.size() == 0)
                    return;

                Log.e("data", "subjects : " + subjects.get(0).getTitle() + " / " + MainSubject.size());

                MainSubject.addAll(subjects);

                for (Subject subject : subjects) {
                    subject_titles.add(subject.getTitle());
                }

                if (adapter1 != null)
                    adapter1.notifyDataSetChanged();

//                subjectViewModel.getModels().removeObservers(instance);
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

                lessonViewModel = ViewModelProviders.of(instance).get(LessonViewModel.class);
                lessonViewModel.init(MyApplication.Domain + "user_lessons");

                lessonViewModel.getModels().observe(instance, new Observer<List<Lesson>>() {
                    @Override
                    public void onChanged(List<Lesson> lessons) {

                        if (lessons.size() == 0)
                            return;

                        MainLessons.addAll(lessons);
                        for (Lesson lesson : lessons) {
                            lesson_titles.add(lesson.getTitle());
                        }

                        if (adapter != null)
                            adapter.notifyDataSetChanged();

                        get_complete_quiz();
                    }
                });

            }
        }, 500);

    }

    private void get_complete_quiz() {

        CompleteQuizViewModel completeQuizViewModel = ViewModelProviders.of(instance).get(CompleteQuizViewModel.class);
        completeQuizViewModel.init(MyApplication.Domain + "complete_quiz");

        completeQuizViewModel.getModels().observe(instance, new Observer<List<Data.Quiz>>() {
            @Override
            public void onChanged(List<Data.Quiz> quizzes) {

                if (quizzes.size() == 0)
                    return;

                MainQuiz.addAll(quizzes);
                if (completeQuizAdapter != null)
                    completeQuizAdapter.notifyDataSetChanged();
                BaseActivity.getInstace().loading.setVisibility(View.GONE);
            }
        });
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
        Log.e("Fragments", "Test Destroyed!");
    }

    public Context getContext() {
        return BaseActivity.of(getActivity());
    }

}

