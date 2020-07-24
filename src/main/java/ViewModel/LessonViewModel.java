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
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LessonViewModel extends ViewModel {

    private MutableLiveData<List<Lesson>> models;

    public void init(String url) {
        setLessons(url);
    }

    public MutableLiveData<List<Lesson>> getModels() {
        if (models == null) {
            MutableLiveData<List<Lesson>> liveData = new MutableLiveData<>();
            liveData.setValue(new ArrayList<Lesson>());
            models = liveData;
        }
        return models;
    }

    private void setLessons(String Url) {

        AndroidNetworking.post(Url)
                .addHeaders("Accept", "application/json")
                .addHeaders("Authorization", "Bearer " + MyApplication.token)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            Log.e("error", "lessons are here!");

                            Type listType = new TypeToken<ArrayList<Lesson>>() {
                            }.getType();
                            List<Lesson> newLessons = new Gson().fromJson(response.getJSONArray("data").toString(), listType);

                            List<Lesson> currentLessons = models.getValue();
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
