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
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import Data.Question;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class QuizViewModel extends ViewModel {

    private MutableLiveData<List<Question>> models;

    public void init(String url , String how_many , int lesson_id ) {
        setLessons(url , how_many , lesson_id);
    }

    public void init(String url , String quiz_id) {
        setLessons(url , quiz_id);
    }

    public void init(String url , String how_many , int lesson_id , int subject_id) {
        setLessons(url , how_many , lesson_id , subject_id);
    }

    public MutableLiveData<List<Question>> getModels() {
        if (models == null) {
            MutableLiveData<List<Question>> liveData = new MutableLiveData<>();
            liveData.setValue(new ArrayList<Question>());
            models = liveData;
        }
        return models;
    }

    private void setLessons(String Url , String how_many , int lesson_id) {

        AndroidNetworking.post(Url)
                .addHeaders("Accept", "application/json")
                .addHeaders("Authorization", "Bearer " + MyApplication.token)
                .addBodyParameter("lesson_id" , lesson_id + "")
                .addBodyParameter("how_many" , how_many + "")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            Log.e("question", "lessons are here!");

                            Question[] myTypes = (new Gson()).fromJson(response.getJSONArray("data").toString(), Question[].class);
/*

                            Type listType = new TypeToken<ArrayList<Question>>() {
                            }.getType();
*/
//                            List<Question> newLessons = new Gson().fromJson(response.getJSONArray("data").toString(), listType);

                            List<Question> newLessons = new ArrayList<>();
                            newLessons.addAll(Arrays.asList(myTypes));

                            List<Question> currentLessons = models.getValue();
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

    private void setLessons(String Url , String quiz_id) {

        AndroidNetworking.post(Url + "/" + quiz_id)
                .addHeaders("Accept", "application/json")
                .addHeaders("Authorization", "Bearer " + MyApplication.token)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            Log.e("question", "questions are here!");

                            Question[] myTypes = (new Gson()).fromJson(response.getJSONArray("data").toString(), Question[].class);
/*

                            Type listType = new TypeToken<ArrayList<Question>>() {
                            }.getType();
*/
//                            List<Question> newLessons = new Gson().fromJson(response.getJSONArray("data").toString(), listType);

                            List<Question> newLessons = new ArrayList<>();
                            newLessons.addAll(Arrays.asList(myTypes));

                            List<Question> currentLessons = models.getValue();
                            currentLessons.addAll(newLessons);

                            models.postValue(currentLessons);

                        } catch (JSONException e) {
                            Log.e("error", Objects.requireNonNull(e.getMessage()));
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("error123", anError.getErrorDetail());
                    }
                });
    }

    private void setLessons(String Url , String how_many , int lesson_id , int subject_id) {

        AndroidNetworking.post(Url)
                .addHeaders("Accept", "application/json")
                .addHeaders("Authorization", "Bearer " + MyApplication.token)
                .addBodyParameter("lesson_id" , lesson_id + "")
                .addBodyParameter("how_many" , how_many + "")
                .addBodyParameter("subject_id" , subject_id + "")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            Log.e("error", "question are here!");


                            Question[] myTypes = (new Gson()).fromJson(response.getJSONArray("data").toString(), Question[].class);
/*

                            Type listType = new TypeToken<ArrayList<Question>>() {
                            }.getType();
*/
//                            List<Question> newLessons = new Gson().fromJson(response.getJSONArray("data").toString(), listType);

                            List<Question> newLessons = new ArrayList<>();
                            newLessons.addAll(Arrays.asList(myTypes));

                            List<Question> currentLessons = models.getValue();
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
