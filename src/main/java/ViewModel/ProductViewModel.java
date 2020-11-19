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
import Data.Product;
import Data.QuestionPart;
import Data.Quiz;
import Data.Video;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ProductViewModel extends ViewModel {

    private MutableLiveData<List<Product>> models;

    public void init(String url) {
        setLessons(url);
    }

    public MutableLiveData<List<Product>> getModels() {
        if (models == null) {
            MutableLiveData<List<Product>> liveData = new MutableLiveData<>();
            liveData.setValue(new ArrayList<Product>());
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

                            Log.e("error", "products are here!");

                            Type listType = new TypeToken<ArrayList<Product>>() {
                            }.getType();
                            List<Product> newLessons = new Gson().fromJson(response.getJSONArray("data").toString(), listType);

                            List<Product> currentLessons = models.getValue();
                            currentLessons.addAll(newLessons);

                            models.postValue(currentLessons);

                        } catch (JSONException e) {
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        int errorCode = anError.getErrorCode();
                        Log.e("error", anError.getErrorDetail() + " / " + errorCode);
                        if(errorCode == 403)
                            BaseActivity.getInstace().failure();
                    }
                });
    }

    public int count(){
        return models.getValue().size();
    }

}
