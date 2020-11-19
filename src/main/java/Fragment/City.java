package Fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import Tools.TransitionHelper;
import ViewModel.CityViewModel;
import ViewModel.GradeViewModel;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

public class City extends TransitionHelper.BaseFragment {

    public View view;

    private Spinner spinner, spinner1;
    private ArrayAdapter<String> adapter, adapter1;

    private CityViewModel cityViewModel;
    private GradeViewModel gradeViewModel;

    private ArrayList<String> cities = new ArrayList<>();
    private ArrayList<String> grades = new ArrayList<>();

    private ArrayList<Data.City> MainCities = new ArrayList<>();
    private ArrayList<Data.Grade> MainGrades = new ArrayList<>();

    private City instance = this;

    public City() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e("Fragments", "City Created!");

        view = inflater.inflate(R.layout.city, container, false);

        ((TextView) view.findViewById(R.id.title_1)).setTypeface(MyApplication.getMyTypeFaceMedium(getContext()));

        Button submit = view.findViewById(R.id.submit);
        submit.setTypeface(MyApplication.getMyTypeFaceBold(getContext()));

        spinner = view.findViewById(R.id.spinner_one);
        spinner1 = view.findViewById(R.id.spinner_two);

        spinner.setBackgroundResource(R.drawable.edit_text_2);
        spinner1.setBackgroundResource(R.drawable.edit_text_2);

        cities.add("انتخاب شهر");
        grades.add("انتخاب مقطع تحصیلی");


        adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, cities) {
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

        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0)
                    return;
                int CityID = MainCities.get(position - 1).getID();

                Log.e("data", CityID + " selected!");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

        adapter1 = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, grades) {
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
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0)
                    return;
                int GradeID = MainGrades.get(position - 1).getID();

                Log.e("data", GradeID + " selected!");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(spinner1.getSelectedItemPosition() == 0)
                    return;
                if(spinner.getSelectedItemPosition() == 0)
                    return;

                BaseActivity.getInstace().loading.setVisibility(View.VISIBLE);

                AndroidNetworking.post(MyApplication.Domain + "register")
                        .setPriority(Priority.HIGH)
                        .addBodyParameter("phone" , MyApplication.PhoneNumber)
                        .addBodyParameter("grade_id" , MainGrades.get(spinner1.getSelectedItemPosition() - 1).getID() + "")
                        .addBodyParameter("city_id" , MainCities.get(spinner.getSelectedItemPosition() - 1).getID() + "")
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject response) {

                                Log.e("data", "code has been sent!");
                                Log.e("data", response.toString());
                                BaseActivity.getInstace().loading.setVisibility(View.GONE);

                                BaseActivity.of(getActivity()).AddFragments(new Code() , "Code" , false);
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

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                cityViewModel = ViewModelProviders.of(instance).get(CityViewModel.class);
                cityViewModel.init(MyApplication.Domain + "city");

                cityViewModel.getModels().observe(instance, new Observer<List<Data.City>>() {
                    @Override
                    public void onChanged(List<Data.City> lessons) {

                        if (lessons.size() == 0)
                            return;

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

                        MainGrades.addAll(lessons);
                        for (Data.Grade grade : MainGrades)
                            grades.add(grade.getTitle());

                        if (adapter1 != null)
                            adapter1.notifyDataSetChanged();

                    }
                });

            }
        }, 500);

    }


    @Override
    public boolean onBeforeBack() {
        return super.onBeforeBack();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("Fragments", "City Destroyed!");
    }

    public Context getContext() {
        return BaseActivity.of(getActivity());
    }

}

