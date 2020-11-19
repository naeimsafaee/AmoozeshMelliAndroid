package Fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amoozeshmelli.BaseActivity;
import com.amoozeshmelli.MyApplication;
import com.amoozeshmelli.R;
import com.google.android.material.tabs.TabLayout;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import Adapter.TeacherAdapter;
import Data.Subject;
import Data.Teacher;
import Tools.TransitionHelper;
import ViewModel.SubjectViewModel;
import ViewModel.TeacherViewModel;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ShowSubject extends TransitionHelper.BaseFragment {

    public View view;
    private SubjectViewModel subjectViewModel;
    private TeacherViewModel teacherViewModelList;
    private TabLayout tabLayout;

    private TextView is_not_global, is_global;
    private boolean isGlobal = true;

    private RecyclerView teacher_recyclerview;

    private List<Teacher> MainTeacherViewModels = new ArrayList<>();

    private List<Teacher>[] MainTeachers;

    private TeacherAdapter teacherAdapter;
    private int current_position = 0;
    private List<Subject> MainSubject;

    private ShowSubject instance = this;


    public ShowSubject() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.e("Fragments", "ShowSubject Created!");

        view = inflater.inflate(R.layout.show_subject, container, false);

        view.findViewById(R.id.back_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseActivity.of(getActivity()).onBackPressed();
            }
        });

        is_not_global = view.findViewById(R.id.is_not_global);
        is_global = view.findViewById(R.id.is_global);

        tabLayout = view.findViewById(R.id.tabs);
        teacher_recyclerview = view.findViewById(R.id.teacher_recyclerview);

        if (MainTeacherViewModels != null)
            for (int i = MainTeacherViewModels.size() - 1; i > 0; i--)
                if (MainTeacherViewModels.get(i).IsGlobal() != isGlobal)
                    MainTeacherViewModels.remove(i);


        teacherAdapter = new TeacherAdapter(getActivity(), MainTeacherViewModels);

        teacherAdapter.setOnItemClickListener(new TeacherAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                BaseActivity.of(getActivity()).SubjectID = MainSubject.get(MainSubject.size() - tabLayout.getSelectedTabPosition() - 1).getID();
                BaseActivity.of(getActivity()).SubjectTitle = MainSubject.get(MainSubject.size() - tabLayout.getSelectedTabPosition() - 1).getTitle();
                BaseActivity.of(getActivity()).TeacherID = MainTeacherViewModels.get(position).getTeacherID();
                BaseActivity.of(getActivity()).TeacherName = MainTeacherViewModels.get(position).getFullName();

                BaseActivity.of(getActivity()).AddFragments(new ShowSection(), "ShowSection", false);
            }
        });

        teacher_recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        teacher_recyclerview.setAdapter(teacherAdapter);


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                final int position = tab.getPosition();

                current_position = position;

                Log.e("Data", subjectViewModel.getModels().getValue().get(position).getID() + "");

                int size = MainTeacherViewModels.size();
                MainTeacherViewModels.clear();
                teacherAdapter.notifyItemRangeRemoved(0, size);

                if (MainTeachers[MainTeachers.length - position - 1] == null) {

                    teacherViewModelList = ViewModelProviders.of(getActivity()).get(TeacherViewModel.class);
                    teacherViewModelList.init(MyApplication.Domain + "user_search_teacher_with_sub"
                            , subjectViewModel.getModels().getValue().get(MainTeachers.length - position - 1).getID());

                    teacherViewModelList.getModels().observe(getActivity(),
                            new Observer<List<Teacher>>() {
                                @Override
                                public void onChanged(final List<Teacher> teachers) {

                                    teacherViewModelList.getModels().removeObservers(getActivity());

                                    if (teachers.size() == 0)
                                        return;

                                    Log.e("Data", teachers.size() + "/asd");

                                    MainTeacherViewModels.addAll(teachers);
                                    for (int i = MainTeacherViewModels.size() - 1; i >= 0; i--)
//                                        Log.e("isGlobal", MainTeacherViewModels.get(i).IsGlobal() ? "true" : "false");
                                        if (MainTeacherViewModels.get(i).IsGlobal() != isGlobal)
                                            MainTeacherViewModels.remove(i);

                                    MainTeachers[MainTeachers.length - position - 1] = teachers;

                                    if (teacherAdapter == null) {
                                        teacherAdapter = new TeacherAdapter(getActivity(), MainTeacherViewModels);

                                        teacher_recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
                                        teacher_recyclerview.setAdapter(teacherAdapter);
                                    } else
                                        teacherAdapter.notifyDataSetChanged();

                                    view.findViewById(R.id.teacherIsEmpty).setVisibility(MainTeacherViewModels.size() == 0 ? View.VISIBLE : View.INVISIBLE);

                                    Log.e("Data", "teachers are here!");

                                }
                            });
                } else {

                    MainTeacherViewModels.addAll(MainTeachers[MainTeachers.length - position - 1]);
                    for (int i = MainTeacherViewModels.size() - 1; i >= 0; i--)
//                                        Log.e("isGlobal", MainTeacherViewModels.get(i).IsGlobal() ? "true" : "false");
                        if (MainTeacherViewModels.get(i).IsGlobal() != isGlobal)
                            MainTeacherViewModels.remove(i);

                    teacherAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }

        });

        TextView textView = view.findViewById(R.id.title);
        textView.setText(BaseActivity.of(getActivity()).LessonTilte);
        textView.setTypeface(MyApplication.getMyTypeFaceMedium(getActivity()));


        ((TextView) view.findViewById(R.id.teacherIsEmpty))
                .setTypeface(MyApplication.getMyTypeFaceMedium(getActivity()));


        is_global.setTypeface(MyApplication.getMyTypeFaceMedium(getActivity()));
        is_not_global.setTypeface(MyApplication.getMyTypeFaceMedium(getActivity()));


        is_global.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                is_global.setBackgroundResource(R.drawable.bg_10);
                is_global.setTextColor(getResources().getColor(R.color.white));

                is_not_global.setBackgroundResource(R.drawable.bg_1);
                is_not_global.setTextColor(getResources().getColor(R.color.colorPrimaryDark));

                view.findViewById(R.id.pyramid_one).setVisibility(View.INVISIBLE);
                view.findViewById(R.id.pyramid_two).setVisibility(View.VISIBLE);
                isGlobal = true;

                int size = MainTeacherViewModels.size();
                MainTeacherViewModels.clear();
                teacherAdapter.notifyItemRangeRemoved(0, size);

                if (MainTeachers[MainTeachers.length - current_position - 1] != null)
                    for (int i = MainTeachers[MainTeachers.length - current_position - 1].size() - 1; i >= 0; i--)
                        if (MainTeachers[MainTeachers.length - current_position - 1].get(i).IsGlobal() == isGlobal)
                            MainTeacherViewModels.add(MainTeachers[MainTeachers.length - current_position - 1].get(i));
                teacherAdapter.notifyDataSetChanged();

                view.findViewById(R.id.teacherIsEmpty).setVisibility(MainTeacherViewModels.size() == 0 ? View.VISIBLE : View.INVISIBLE);
            }
        });

        is_not_global.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                is_not_global.setBackgroundResource(R.drawable.bg_10);
                is_not_global.setTextColor(getResources().getColor(R.color.white));

                is_global.setBackgroundResource(R.drawable.bg_1);
                is_global.setTextColor(getResources().getColor(R.color.colorPrimaryDark));

                view.findViewById(R.id.pyramid_one).setVisibility(View.VISIBLE);
                view.findViewById(R.id.pyramid_two).setVisibility(View.INVISIBLE);
                isGlobal = false;

                int size = MainTeacherViewModels.size();
                MainTeacherViewModels.clear();
                teacherAdapter.notifyItemRangeRemoved(0, size);

//                Log.e("size" , )

                if (MainTeachers[MainTeachers.length - current_position - 1] != null)
                    for (int i = MainTeachers[MainTeachers.length - current_position - 1].size() - 1; i >= 0; i--)
                        if (MainTeachers[MainTeachers.length - current_position - 1].get(i).IsGlobal() == isGlobal)
                            MainTeacherViewModels.add(MainTeachers[MainTeachers.length - current_position - 1].get(i));
                teacherAdapter.notifyDataSetChanged();
                view.findViewById(R.id.teacherIsEmpty).setVisibility(MainTeacherViewModels.size() == 0 ? View.VISIBLE : View.INVISIBLE);

            }
        });

        if (subjectViewModel != null)
            if (subjectViewModel.getModels() != null)
                if (subjectViewModel.getModels().getValue() != null)
                    setTabs(subjectViewModel.getModels().getValue());

        return view;
    }

    void setTabs(List<Subject> subjects) {

        if (tabLayout == null)
            return;
        if (tabLayout.getTabCount() != 0)
            return;

        for (int i = subjects.size() - 1; i >= 0; i--) {
            View tab_view = LayoutInflater.from(getActivity()).inflate(R.layout.custom_tab, null);

            TextView textView = tab_view.findViewById(R.id.tab_text);
            textView.setText(subjects.get(i).getTitle());
            textView.setTypeface(MyApplication.getMyTypeFaceMedium(getActivity()));

            tabLayout.addTab(tabLayout.newTab().setCustomView(textView), i == 0);
        }

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setScrollX(tabLayout.getWidth() * 10);
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onAttach(@NotNull final Context context) {
        super.onAttach(context);

        BaseActivity.getInstace().loading.setVisibility(View.VISIBLE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                subjectViewModel = ViewModelProviders.of(instance).get(SubjectViewModel.class);
                subjectViewModel.init(MyApplication.Domain + "subjects_of_lesson_of_user", BaseActivity.of(getActivity()).LessonID);

                subjectViewModel.getModels().observe(instance, new Observer<List<Subject>>() {
                    @Override
                    public void onChanged(List<Subject> subjects) {

                        if (subjects.size() == 0)
                            return;
                        MainTeachers = new List[subjects.size()];

                        Log.e("error-1", subjects.get(0).getTitle());

                        setTabs(subjects);

                        MainSubject = subjects;
                        BaseActivity.getInstace().loading.setVisibility(View.GONE);
                    }
                });
            }
        }, 500);

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
        Log.e("Fragments", "ShowSubject Destroyed!");
    }

    public Context getContext() {
        return BaseActivity.of(getActivity());
    }


}

