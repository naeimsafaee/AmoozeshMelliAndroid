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
import Data.Section;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SectionViewModel extends ViewModel {

    private MutableLiveData<List<Section>> models;

    public void init(String url , int teacher_id , int subject_id) {
        setLessons(url , teacher_id , subject_id);
    }

    public MutableLiveData<List<Section>> getModels() {
        if (models == null) {
            MutableLiveData<List<Section>> liveData = new MutableLiveData<>();
            liveData.setValue(new ArrayList<Section>());
            models = liveData;
        }
        return models;
    }

    private void setLessons(String Url, int teacher_id, int subject_id) {

        AndroidNetworking.post(Url)
                .addHeaders("Accept", "application/json")
                .addHeaders("Authorization", "Bearer " + MyApplication.token)
                .addBodyParameter("teacher_id" , teacher_id + "")
                .addBodyParameter("subject_id" , subject_id + "")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            Log.e("error", "sections are here!");

                            Type listType = new TypeToken<ArrayList<Section>>() {
                            }.getType();
                            List<Section> newLessons = new Gson().fromJson(response.getJSONArray("data").toString(), listType);

                            List<Section> currentLessons = models.getValue();
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
