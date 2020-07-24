package ViewModel;

import android.util.Log;

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

import Data.Teacher;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TeacherViewModel extends ViewModel {

    private MutableLiveData<List<Teacher>> models;

    public void init(String url, int lesson_id) {
        models = new MutableLiveData<>();
        setTeacher(url, lesson_id);
    }

    public MutableLiveData<List<Teacher>> getModels() {
        if (models == null) {
            MutableLiveData<List<Teacher>> liveData = new MutableLiveData<>();
            liveData.setValue(new ArrayList<Teacher>());
            models = liveData;
        }
        return models;
    }

    private void setTeacher(String Url, int lesson_id) {

        AndroidNetworking.post(Url)
                .addHeaders("Accept", "application/json")
                .addHeaders("Authorization", "Bearer " + MyApplication.token)
                .addBodyParameter("subject_id", lesson_id + "")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.e("Data", response.toString());

                        try {

                            Type listType = new TypeToken<ArrayList<Teacher>>() {
                            }.getType();
                            List<Teacher> newLessons = new Gson().fromJson(response.getJSONArray("data").toString(), listType);

                            List<Teacher> currentLessons = models.getValue();
                            if(currentLessons == null)
                                currentLessons = new ArrayList<>();

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
