package Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
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

import Fragment.City;
import Tools.TransitionHelper;
import androidx.fragment.app.Fragment;

import static android.content.Context.MODE_PRIVATE;

public class Number extends TransitionHelper.BaseFragment {

    public View view;

    public Number() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e("Fragments", "Number Created!");

        view = inflater.inflate(R.layout.number, container, false);

        ((TextView) view.findViewById(R.id.title_1)).setTypeface(MyApplication.getMyTypeFaceMedium(getContext()));

        final EditText number = view.findViewById(R.id.number);

        Button submit = view.findViewById(R.id.submit);
        submit.setTypeface(MyApplication.getMyTypeFaceBold(getContext()));
        number.setTypeface(MyApplication.getMyTypeFaceMedium(getContext()));

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MyApplication.PhoneNumber = number.getText().toString();

                BaseActivity.getInstace().loading.setVisibility(View.VISIBLE);

                AndroidNetworking.post(MyApplication.Domain + "login")
                        .setPriority(Priority.HIGH)
                        .addBodyParameter("phone", MyApplication.PhoneNumber)
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject response) {

                                try {
                                    int is_register = response.getInt("is_register");

                                    BaseActivity.getInstace().loading.setVisibility(View.GONE);

                                    if(is_register == 0){
                                        BaseActivity.of(getActivity()).AddFragments(new Code() , "Code" , false);
                                    } else {
                                        BaseActivity.of(getActivity()).AddFragments(new City() , "City" , false);
                                    }

                                } catch (JSONException e) {
                                    Log.e("error" , e.getMessage());
                                    e.printStackTrace();
                                }

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
        Log.e("Fragments", "Number Destroyed!");
    }

    public Context getContext() {
        return BaseActivity.of(getActivity());
    }

}

