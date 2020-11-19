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

import Data.City;
import Data.Lesson;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CityViewModel extends ViewModel {

    private MutableLiveData<List<City>> models;

    public void init(String url) {
        setLessons(url);
    }

    public MutableLiveData<List<City>> getModels() {
        if (models == null) {
            MutableLiveData<List<City>> liveData = new MutableLiveData<>();
            liveData.setValue(new ArrayList<City>());
            models = liveData;
        }
        return models;
    }

    private void setLessons(String Url) {

        AndroidNetworking.get(Url)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            Log.e("error", "lessons are here!");

                            Type listType = new TypeToken<ArrayList<City>>() {
                            }.getType();
                            List<City> newLessons = new Gson().fromJson(response.getJSONArray("data").toString(), listType);

                            List<City> currentLessons = models.getValue();
                            currentLessons.addAll(newLessons);

                            models.postValue(currentLessons);

                        } catch (JSONException e) {
                            Log.e("error", Objects.requireNonNull(e.getMessage()));
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("error", anError.getErrorDetail() + " / " + anError.getErrorCode());
                    }

                });
    }

    public int count() {
        return models.getValue().size();
    }

}
