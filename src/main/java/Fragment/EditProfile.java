package Fragment;

import android.content.Context;
import android.content.SharedPreferences;
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
import android.widget.Toast;

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

import Data.City;
import Tools.TransitionHelper;
import ViewModel.CityViewModel;
import ViewModel.GradeViewModel;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import static android.content.Context.MODE_PRIVATE;

public class EditProfile extends TransitionHelper.BaseFragment {

    public View view;

    ArrayList<String> cities = new ArrayList<>();
    ArrayList<String> grades = new ArrayList<>();

    Spinner spinner, spinner1;

    private ArrayAdapter<String> adapter;
    private ArrayAdapter<String> adapter1;


    private ArrayList<Data.City> MainCities = new ArrayList<>();
    private ArrayList<Data.Grade> MainGrades = new ArrayList<>();


    private CityViewModel cityViewModel;
    private GradeViewModel gradeViewModel;

    private EditProfile instance = this;

    public EditProfile() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e("Fragments", "EditProfile Created!");


        view = inflater.inflate(R.layout.edit_profile, container, false);


        TextView title = view.findViewById(R.id.title);

        final EditText fullName_et = view.findViewById(R.id.fullName_et);
        Button submit = view.findViewById(R.id.submit);

        title.setTypeface(MyApplication.getMyTypeFaceMedium(getContext()));
        fullName_et.setTypeface(MyApplication.getMyTypeFaceMedium(getContext()));
        submit.setTypeface(MyApplication.getMyTypeFaceBold(getContext()));

        fullName_et.setText(MyApplication.fullName);

        title.setText("ویرایش مشخصات فردی");

        view.findViewById(R.id.back_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseActivity.of(getActivity()).onBackPressed();
            }
        });

        set_spinner();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("fullName", fullName_et.getText().toString());
                    jsonObject.put("grade_id", MainGrades.get(spinner1.getSelectedItemPosition()).getID());
                    jsonObject.put("city_id", MainCities.get(spinner.getSelectedItemPosition()).getID());
                } catch (JSONException e) {
                    Log.e("error" , e.getMessage());
                    e.printStackTrace();
                }

                BaseActivity.getInstace().loading.setVisibility(View.VISIBLE);

                AndroidNetworking.put(MyApplication.Domain + "profile/0")
                        .addHeaders("Accept", "application/json")
                        .addHeaders("Authorization", "Bearer " + MyApplication.token)
                        .addJSONObjectBody(jsonObject)
                        .setPriority(Priority.HIGH)
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject response) {

                                Log.e("data", "code has been sent!");
                                Log.e("data", response.toString());

                                BaseActivity.getInstace().loading.setVisibility(View.GONE);

                                Toast.makeText(getContext(), "مشخصات با موفقیت ثبت شد!", Toast.LENGTH_SHORT).show();

                                BaseActivity.of(getActivity()).onBackPressed();
                            }

                            @Override
                            public void onError(ANError anError) {
                                Log.e("error", anError.getErrorDetail() + " / " + anError.getErrorCode());
                            }

                        });
            }
        });

        return view;
    }


    private void set_spinner() {

        spinner = view.findViewById(R.id.spinner_one);
        spinner1 = view.findViewById(R.id.spinner_two);

        spinner.setBackgroundResource(R.drawable.edit_text_2);
        spinner1.setBackgroundResource(R.drawable.edit_text_2);

        cities.add(MyApplication.City);
        grades.add(MyApplication.Grade);

        adapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_item, cities) {
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
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

        adapter1 = new ArrayAdapter<String>(getContext(), R.layout.spinner_item, grades) {
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

        spinner1.setAdapter(adapter1);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0)
                    return;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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


        new Handler().post(new Runnable() {
            @Override
            public void run() {

                cityViewModel = ViewModelProviders.of(instance).get(CityViewModel.class);
                cityViewModel.init(MyApplication.Domain + "city");

                cityViewModel.getModels().observe(instance, new Observer<List<City>>() {
                    @Override
                    public void onChanged(List<Data.City> lessons) {

                        if (lessons.size() == 0)
                            return;

                        cities.clear();

                        MainCities.addAll(lessons);
                        for (Data.City city : MainCities)
                            cities.add(city.getName());

                        if (adapter != null)
                            adapter.notifyDataSetChanged();

                    }
                });

                gradeViewModel = ViewModelProviders.of(instance).get(GradeViewModel.class);
                gradeViewModel.init(MyApplication.Domain + "grade");

                gradeViewModel.getModels().observe(instance, new Observer<List<Data.Grade>>() {
                    @Override
                    public void onChanged(List<Data.Grade> lessons) {

                        if (lessons.size() == 0)
                            return;

                        grades.clear();

                        MainGrades.addAll(lessons);
                        for (Data.Grade grade : MainGrades)
                            grades.add(grade.getTitle());

                        if (adapter1 != null)
                            adapter1.notifyDataSetChanged();

                    }
                });

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
        Log.e("Fragments", "Shop Destroyed!");
    }

    public Context getContext() {
        return BaseActivity.of(getActivity());
    }

}

