package Fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amoozeshmelli.BaseActivity;
import com.amoozeshmelli.MyApplication;
import com.amoozeshmelli.R;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import Adapter.LessonAdapter;
import Adapter.SliderAdapter;
import Data.Lesson;
import Data.Slide;
import Tools.OverRecyclerView;
import Tools.TransitionHelper;
import ViewModel.LessonViewModel;
import ViewModel.OnGoingLessonViewModel;
import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

public class Home extends TransitionHelper.BaseFragment {

    public View view;

    private LessonAdapter lessonAdapter, lessonAdapter_1;
    private OnGoingLessonViewModel onGoingLessonViewModel;
    private LessonViewModel lessonViewModel;
    private List<Slide> slides = new ArrayList<>();
    private SliderAdapter sliderAdapter;
    private RecyclerView slider_recyclerView;

    private int finishHelper = 0;

    private LinearLayoutManager sliderr_layoutManager;

    public Home() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e("Fragments", "Home Created!");

        view = inflater.inflate(R.layout.home, container, false);

        lessonAdapter = new LessonAdapter(getContext(), lessonViewModel.getModels().getValue());
        lessonAdapter_1 = new LessonAdapter(getContext(), onGoingLessonViewModel.getModels().getValue());

        lessonAdapter.setOnItemClickListener(new LessonAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

                if(lessonViewModel.getModels().getValue() == null)
                    return;

                BaseActivity.of(getActivity()).LessonID = lessonViewModel.getModels().getValue().get(position).getID();
                BaseActivity.of(getActivity()).LessonTilte = lessonViewModel.getModels().getValue().get(position).getTitle();
                BaseActivity.of(getActivity()).AddFragments(new ShowSubject(), "ShowSubject", false);
            }
        });

        setUprecycelerView();
        setSlider();

        final TextView slider_text = view.findViewById(R.id.slide_text);
        slider_text.setTypeface(MyApplication.getMyTypeFaceMedium(BaseActivity.of(getActivity())));

        ((TextView) view.findViewById(R.id.title_1)).setTypeface(MyApplication.getMyTypeFaceMedium(BaseActivity.of(getActivity())));
        ((TextView) view.findViewById(R.id.title_2)).setTypeface(MyApplication.getMyTypeFaceMedium(BaseActivity.of(getActivity())));

        slider_recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int item = sliderr_layoutManager.findFirstCompletelyVisibleItemPosition();
                if (item == -1)
                    return;
                slider_text.setText(slides.get(item % slides.size()).title);
            }
        });

        return view;
    }

    private void setSlider() {

        sliderAdapter = new SliderAdapter(getContext(), slides);

        slider_recyclerView = view.findViewById(R.id.slider);

        sliderr_layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, true);

        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(slider_recyclerView);

        slider_recyclerView.setLayoutManager(sliderr_layoutManager);
        slider_recyclerView.setAdapter(sliderAdapter);

    }

    private void setUprecycelerView() {

        OverRecyclerView recyclerView = view.findViewById(R.id.recycler_one);

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, true);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(lessonAdapter);

        recyclerView.setOverScrollListener(new OverRecyclerView.OverScrollListener() {
            @Override
            public boolean onOverScroll(int xDistance, boolean isReleased) {
                return false;
            }
        });

        OverRecyclerView recyclerView_one = view.findViewById(R.id.recycler_two);

        recyclerView_one.setOverScrollListener(new OverRecyclerView.OverScrollListener() {
            @Override
            public boolean onOverScroll(int xDistance, boolean isReleased) {
                return false;
            }
        });

        LinearLayoutManager layoutManager_1
                = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

        recyclerView_one.setLayoutManager(layoutManager_1);
        recyclerView_one.setAdapter(lessonAdapter_1);
    }

    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);

        onGoingLessonViewModel = ViewModelProviders.of(this).get(OnGoingLessonViewModel.class);
        onGoingLessonViewModel.init(MyApplication.Domain + "user_lessons");

        onGoingLessonViewModel.getModels().observe(this, new Observer<List<Lesson>>() {
            @Override
            public void onChanged(List<Lesson> lessons) {

                finishHelper++;
                Log.e("data", finishHelper + "/onGoingLessonViewModel");

                lessonAdapter_1.notifyDataSetChanged();
                if (finishHelper > 3)
                    BaseActivity.of(getActivity()).runHome();
            }
        });

        lessonViewModel = ViewModelProviders.of(this).get(LessonViewModel.class);
        lessonViewModel.init(MyApplication.Domain + "user_lessons");

        lessonViewModel.getModels().observe(this, new Observer<List<Lesson>>() {
            @Override
            public void onChanged(List<Lesson> lessons) {
                finishHelper++;
                Log.e("data", finishHelper + "/lessonViewModel");
                lessonAdapter.notifyDataSetChanged();
                if (finishHelper > 3)
                    BaseActivity.of(getActivity()).runHome();
            }
        });

        getImages();

    }

    private void getImages() {

        AndroidNetworking.get(MyApplication.Domain + "sliders")
                .addHeaders("Accept", "application/json")
                .addHeaders("Authorization", "Bearer " + MyApplication.token)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            Log.e("error", "images are here!");

                            Type listType = new TypeToken<ArrayList<Slide>>() {
                            }.getType();
                            List<Slide> slide = new Gson().fromJson(response.getJSONArray("data").toString(), listType);

                            slides.addAll(slide);

                            sliderAdapter.notifyDataSetChanged();
                            slider_recyclerView.scrollToPosition(Integer.MAX_VALUE / 2);

                            Log.e("error", slides.get(0).getUrl());

                        } catch (JSONException e) {
                            Log.e("error", Objects.requireNonNull(e.getMessage()));
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("error", anError.getErrorDetail());
                    }
                });

    }

    @Override
    public boolean onBeforeBack() {
        BaseActivity activity = BaseActivity.of(getActivity());
        /*if (!activity.animateHomeIcon(MaterialMenuDrawable.IconState.BURGER)) {
            activity.drawerLayout.openDrawer(Gravity.START);
        }*/
        return super.onBeforeBack();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("Fragments", "Home Destroyed!");
    }

    public Context getContext() {
        return BaseActivity.of(getActivity());
    }

}
