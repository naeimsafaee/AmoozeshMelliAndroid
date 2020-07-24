package ViewModel;

import android.util.Log;

import com.amoozeshmelli.BaseActivity;
import com.amoozeshmelli.MyApplication;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import Data.Lesson;
import Data.Part;
import Data.QuestionPart;
import Data.Quiz;
import Data.Quiz1;
import Data.Video;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PartViewModel extends ViewModel {

    private MutableLiveData<List<Part>> models;

    public void init(String url) {
        setLessons(url);
    }

    public MutableLiveData<List<Part>> getModels() {
        if (models == null) {
            MutableLiveData<List<Part>> liveData = new MutableLiveData<>();
            liveData.setValue(new ArrayList<Part>());
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

                        List<Part> newParts = new ArrayList<>();

                        Quiz1 quiz1 = new Quiz1();

                        try {
                            quiz1 = new Gson().fromJson(response.getJSONObject("quiz").toString() , Quiz1.class);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        try {

                            Log.e("error", "parts are here!");


                            JSONArray data = response.getJSONArray("data");
                            for (int i = 0; i < data.length(); i++) {

                                JSONObject object = data.getJSONObject(i);

                                Part part = new Part();
                                part.setID(object.getInt("id"));
                                part.setOrder(Integer.parseInt(object.getString("order")));
                                part.setIs_video(object.getBoolean("is_video"));
                                part.setHas_ended(object.getBoolean("has_ended"));
                                part.setQuiz(quiz1);

                                if(object.getBoolean("is_video")){

                                    Video video = new Video();
                                    video.setVideo_url(object.getString("video_url"));

                                    part.setVideo(video);

                                } else {
                                    QuestionPart questionPart = new Gson().fromJson(object.getJSONObject("question").toString() , QuestionPart.class);

                                    part.setQuestionPart(questionPart);
                                }

                                newParts.add(part);
                            }

                            models.postValue(newParts);

                        } catch (JSONException e) {
                            Log.e("error", Objects.requireNonNull(e.getMessage()));
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        int errorCode = anError.getErrorCode();
                        Log.e("error", anError.getErrorDetail() + " / " + errorCode);
                        if(errorCode == 403 )
                            BaseActivity.getInstace().failure();
                    }
                });
    }

    public int count(){
        return models.getValue().size();
    }

}
