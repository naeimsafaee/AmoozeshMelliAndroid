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

import Data.Lesson;
import Data.Quiz;
import Data.Subject;
import Data.Teacher;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CompleteQuizViewModel extends ViewModel {

    private MutableLiveData<List<Quiz>> models;

    public void init(String url) {
        MutableLiveData<List<Quiz>> liveData = new MutableLiveData<>();
        liveData.setValue(new ArrayList<Quiz>());
        models = liveData;
        setTeacher(url);
    }

    public MutableLiveData<List<Quiz>> getModels() {
        if (models == null) {
            MutableLiveData<List<Quiz>> liveData = new MutableLiveData<>();
            liveData.setValue(new ArrayList<Quiz>());
            models = liveData;
        }
        return models;
    }

    private void setTeacher(String Url) {

        AndroidNetworking.post(Url)
                .addHeaders("Accept", "application/json")
                .addHeaders("Authorization", "Bearer " + MyApplication.token)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.e("Data", response.toString());
                        Log.e("Data", "complete quiz are here!");

                        try {

                            Type listType = new TypeToken<ArrayList<Quiz>>() {
                            }.getType();
                            List<Quiz> newLessons = new Gson().fromJson(response.getJSONArray("data").toString(), listType);

                            List<Quiz> currentLessons = models.getValue();
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
