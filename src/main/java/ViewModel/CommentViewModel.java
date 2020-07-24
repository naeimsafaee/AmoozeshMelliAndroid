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

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import Data.Comment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CommentViewModel extends ViewModel {

    private MutableLiveData<List<Comment>> models;

    public void init(String url) {
        setLessons(url);
    }

    public MutableLiveData<List<Comment>> getModels() {
        if (models == null) {
            MutableLiveData<List<Comment>> liveData = new MutableLiveData<>();
            liveData.setValue(new ArrayList<Comment>());
            models = liveData;
        }
        return models;
    }

    private void setLessons(String Url) {

        AndroidNetworking.get(Url)
                .addHeaders("Accept", "application/json")
                .addHeaders("Authorization", "Bearer " + MyApplication.token)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            Log.e("error", "comments are here!");


                            Type listType = new TypeToken<ArrayList<Comment>>() {
                            }.getType();
                            List<Comment> newLessons = new Gson().fromJson(response.getJSONArray("data").toString(), listType);

                            List<Comment> currentLessons = models.getValue();
                            currentLessons.addAll(newLessons);

                            for (int i = 0; i < newLessons.size(); i++) {

                                Comment reply = null;

                                if(!response.getJSONArray("data")
                                        .getJSONObject(i).getString("reply_to").equals("null")){
                                    reply = new Gson().fromJson(
                                            response.getJSONArray("data").getJSONObject(i).getJSONObject("reply").toString(), Comment.class);
                                }

                                newLessons.get(i).setReply(reply);
                            }

                            models.postValue(currentLessons);

                        } catch (JSONException e) {
                            Log.e("error", Objects.requireNonNull(e.getMessage()));
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        int errorCode = anError.getErrorCode();
                        Log.e("error", anError.getErrorDetail() + " / " + errorCode);
                        if (errorCode == 401 || errorCode == 403)
                            BaseActivity.getInstace().failure();
                    }
                });
    }

    public int count() {
        return models.getValue().size();
    }

}
