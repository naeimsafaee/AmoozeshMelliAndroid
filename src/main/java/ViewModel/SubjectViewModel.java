package ViewModel;

import android.util.Log;
import android.view.View;

import com.amoozeshmelli.BaseActivity;
import com.amoozeshmelli.MyApplication;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import Data.Subject;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SubjectViewModel extends ViewModel {

    private MutableLiveData<List<Subject>> models;

    public void init(String url, int lesson_id) {
        MutableLiveData<List<Subject>> liveData = new MutableLiveData<>();
        liveData.setValue(new ArrayList<Subject>());
        models = liveData;
        setsubject(url, lesson_id);
    }

    public MutableLiveData<List<Subject>> getModels() {
        if (models == null) {
            MutableLiveData<List<Subject>> liveData = new MutableLiveData<>();
            liveData.setValue(new ArrayList<Subject>());
            models = liveData;
        }
        return models;
    }

    private void setsubject(String Url, int lesson_id) {

        AndroidNetworking.post(Url)
                .addHeaders("Accept", "application/json")
                .addHeaders("Authorization", "Bearer " + MyApplication.token)
                .addBodyParameter("lesson_id", lesson_id + "")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            Type listType = new TypeToken<ArrayList<Subject>>() {
                            }.getType();
                            List<Subject> newLessons = new Gson().fromJson(response.getJSONArray("data").toString(), listType);

//                            models.getValue().clear();

                            List<Subject> currentLessons = models.getValue();

                            Log.e("new data" , models.getValue().size() + "");

                            currentLessons.addAll(newLessons);

                            models.postValue(currentLessons);

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

    public int count() {
        return models.getValue().size();
    }

}
