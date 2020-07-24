package Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

import Tools.TransitionHelper;

import static android.content.Context.MODE_PRIVATE;

public class Code extends TransitionHelper.BaseFragment {

    public View view;

    public Code() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e("Fragments", "Code Created!");

        view = inflater.inflate(R.layout.code, container, false);

        ((TextView) view.findViewById(R.id.title_1)).setTypeface(MyApplication.getMyTypeFaceMedium(getContext()));

        final EditText number = view.findViewById(R.id.number);

        Button submit = view.findViewById(R.id.submit);
        submit.setTypeface(MyApplication.getMyTypeFaceBold(getContext()));
        number.setTypeface(MyApplication.getMyTypeFaceMedium(getContext()));

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                BaseActivity.getInstace().loading.setVisibility(View.VISIBLE);

                AndroidNetworking.post(MyApplication.Domain + "verify_sms")
                        .setPriority(Priority.HIGH)
                        .addBodyParameter("phone", MyApplication.PhoneNumber)
                        .addBodyParameter("code", number.getText().toString())
                        .addBodyParameter("is_app", "1")
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject response) {

                                Log.e("data", "code has been sent!");
                                Log.e("data", response.toString());

                                BaseActivity.getInstace().loading.setVisibility(View.GONE);

                                SharedPreferences.Editor editor = getContext().getSharedPreferences("MY_APP", MODE_PRIVATE).edit();
                                editor.putBoolean("is_first_run", false);
                                try {
                                    editor.putString("token", response.getString("token"));
                                } catch (JSONException e) {
                                    BaseActivity.getInstace().failure();
                                    e.printStackTrace();
                                }
                                editor.apply();

                                MyApplication.restartApp(getContext());
//                                BaseActivity.getInstace().recreate();
                            }

                            @Override
                            public void onError(ANError anError) {
                                Toast.makeText(getContext(), "کد وارد شده صحیح نمی باشد.", Toast.LENGTH_SHORT).show();
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
        Log.e("Fragments", "Code Destroyed!");
    }

    public Context getContext() {
        return BaseActivity.of(getActivity());
    }

}

