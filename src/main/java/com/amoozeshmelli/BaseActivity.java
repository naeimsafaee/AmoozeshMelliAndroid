package com.amoozeshmelli;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import Data.Product;
import Data.Question;
import Fragment.EditProfile;
import Fragment.Answer;
import Fragment.ShowProduct;
import Fragment.Home;
import Fragment.Advertise;
import Fragment.Number;
import Fragment.Profile;
import Fragment.Quiz;
import Fragment.Shop;
import Fragment.ShowSection;
import Fragment.ShowSubject;
import Fragment.ShowVideo;
import Fragment.SplashScreen;
import Fragment.Test;
import Tools.MeowBottomNavigation;
import Tools.TransitionHelper;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class BaseActivity extends TransitionHelper.BaseActivity {

    @SuppressLint("StaticFieldLeak")
    private static BaseActivity mInstance;

    public View fragmentBackround;
    public RelativeLayout MainLayout, relativeLayout;
    public FrameLayout container;

    public int LessonID = -1;
    public String LessonTilte = "";

    public int TeacherID = -1;

    public View loading;

    public int SubjectID = -1;
    public String SubjectTitle = "";
    public List<Question> Quiz = new ArrayList<>();

    public String QuizTitle = "";
    public String QuizNumber = "";
    public int QuizID = 0;
    public int QuizTime = -1;

    public String TeacherName = "";

    public String SectionName;
    public int SectionID;
    public int[] mainAnswer;
    public Product product;

    SplashScreen splashScreen;
    public MeowBottomNavigation bottomNavigation;

    ArrayList<String> myFragsTags = new ArrayList<>();

    String CurrentTag = "Home";

    public static BaseActivity of(Activity activity) {
        return (BaseActivity) activity;
    }

    public static BaseActivity getInstace() {
        return mInstance;
    }

    public static int getSoftButtonsBarSizePort(Activity activity) {
        // getRealMetrics is only available with API 17 and +
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            DisplayMetrics metrics = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
            int usableHeight = metrics.heightPixels;
            activity.getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
            int realHeight = metrics.heightPixels;
            if (realHeight > usableHeight)
                return realHeight - usableHeight;
            else
                return 0;
        }
        return 0;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
        );
        AndroidNetworking.initialize(getApplicationContext());
        mInstance = this;


        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        } else {
        }

        getSoftButtonsBarSizePort(getInstace());

        MyApplication.retrieveToken();

        loading = findViewById(R.id.loading);

        relativeLayout = findViewById(R.id.base_fragment_container);
        fragmentBackround = findViewById(R.id.base_fragment_background);
        MainLayout = findViewById(R.id.full_screen);
        container = findViewById(R.id.base_fragment);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            scheduleJob();

        SetUpBottomNavigation();

        initBaseFragment();
    }

    private void initBaseFragment() {

        int fragmentResourceId =
                getIntent().getIntExtra("fragment_resource_id", R.layout.splash_screen);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if (MyApplication.isFirstRun()) {
            bottomNavigation.animate().alpha(0).start();
            transaction.add(R.id.base_fragment, new Number(), "Splash");
            transaction.commit();
            return;
        }

        switch (fragmentResourceId) {
            case R.layout.home:
            default:

                bottomNavigation.animate().alpha(1).setDuration(300).start();
                myFragsTags = new ArrayList<>();

                myFragsTags.add("Home");

                transaction.replace(R.id.base_fragment, new Home(), "Home");
                transaction.commit();
                break;
            case R.layout.splash_screen:

                transaction.add(R.id.base_fragment, new Home(), "Home");
//                transaction.addToBackStack("Home");
                myFragsTags = new ArrayList<>();
                Fragment home = getSupportFragmentManager().findFragmentByTag("Home");
                if (home != null)
                    transaction.hide(home);

                myFragsTags.add("Home");
                bottomNavigation.animate().alpha(0).start();

                splashScreen = new SplashScreen();
                transaction.add(R.id.base_fragment, splashScreen, "Splash");
                transaction.commit();

                break;
        }

    }

    private void SetUpBottomNavigation() {

        bottomNavigation = findViewById(R.id.BottomNavigation);

        bottomNavigation.add(new MeowBottomNavigation.Model(5, R.drawable.ic_tablighat));
        bottomNavigation.add(new MeowBottomNavigation.Model(4, R.drawable.ic_profile));
        bottomNavigation.add(new MeowBottomNavigation.Model(3, R.drawable.ic_shop));
        bottomNavigation.add(new MeowBottomNavigation.Model(2, R.drawable.ic_test));
        bottomNavigation.add(new MeowBottomNavigation.Model(0, R.drawable.ic_home));

        bottomNavigation.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
            @Override
            public void onClickItem(MeowBottomNavigation.Model item) {

                int position = item.getId();
                switch (position) {
                    case 0:
                        AddFragments(new Home(), "Home", true);
                        break;
                    case 2:
                        AddFragments(new Test(), "Test", true);
                        break;
                    case 3:
                        AddFragments(new Shop(), "Shop", true);
                        break;
                    case 4:
                        AddFragments(new Profile(), "Profile", true);
                        break;
                    case 5:
                        AddFragments(new Advertise(), "Advertise", true);
                        break;
                }
            }
        });

        bottomNavigation.setOnReselectListener(new MeowBottomNavigation.ReselectListener() {
            @Override
            public void onReselectItem(MeowBottomNavigation.Model item) {
            }
        });

        bottomNavigation.setOnShowListener(new MeowBottomNavigation.ShowListener() {
            @Override
            public void onShowItem(MeowBottomNavigation.Model item) {

            }
        });


        bottomNavigation.show(0, true);
    }

    public void AddFragments(Fragment fragment, String Tag, boolean needShowBottomNavigation) {

        if (fragment == null)
            return;

        if (needShowBottomNavigation) {
            bottomNavigation.setVisibility(View.VISIBLE);
            bottomNavigation.animate().alpha(1).setDuration(300).start();
        } else {
            bottomNavigation.animate().alpha(0).start();
            bottomNavigation.setVisibility(View.GONE);
        }

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);

        Fragment temp = getSupportFragmentManager().findFragmentByTag(Tag);

        Fragment test = getSupportFragmentManager().findFragmentByTag("Test");
        Fragment advertise = getSupportFragmentManager().findFragmentByTag("Advertise");
        Fragment profile = getSupportFragmentManager().findFragmentByTag("Profile");

        if (test != null) {
            transaction.remove(test);
            for (int i = 0; i < myFragsTags.size(); i++) {
                if (myFragsTags.get(i).equals("Test")) {
                    myFragsTags.remove(i);
                    break;
                }
            }
        }
        if (advertise != null) {
            transaction.remove(advertise);
            for (int i = 0; i < myFragsTags.size(); i++) {
                if (myFragsTags.get(i).equals("Advertise")) {
                    myFragsTags.remove(i);
                    break;
                }
            }
        }
        if (profile != null) {
            transaction.remove(profile);
            for (int i = 0; i < myFragsTags.size(); i++) {
                if (myFragsTags.get(i).equals("Profile")) {
                    myFragsTags.remove(i);
                    break;
                }
            }
        }

        if (temp != null) {
            if (temp.isAdded()) {
                Log.e("route", "showing");

                for (int i = 0; i < myFragsTags.size(); i++) {
                    if (myFragsTags.get(i).equals(Tag)) {
                        myFragsTags.remove(i);
                        break;
                    }
                }

                if (myFragsTags.size() > 0) {
                    Fragment old = getSupportFragmentManager().findFragmentByTag(myFragsTags.get(myFragsTags.size() - 1));
                    if (old != null)
                        transaction.hide(old);
                }

                transaction.show(temp);
                if (splashScreen != null)
                    transaction.remove(splashScreen);
            } else {
                Log.e("route", "adding");
                transaction.add(R.id.base_fragment, fragment, Tag);
            }
        } else {
            Log.e("route", "adding-1");
            transaction.add(R.id.base_fragment, fragment, Tag);
        }

        myFragsTags.add(Tag);

        StringBuilder route = new StringBuilder();
        for (int i = 0; i < myFragsTags.size(); i++)
            route.append(myFragsTags.get(i)).append(" - ");
        Log.e("route", route.toString());

        transaction.commit();
        CurrentTag = Tag;

//        splashScreen.onDestroy();
    }

    @Override
    public void finish() {
        super.finish();
    }

    public void onConnectionChanged(boolean isConnected) {

        Log.e("Connection", isConnected ? "true" : "false");

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void scheduleJob() {

        @SuppressLint("JobSchedulerService") JobInfo myJob = new JobInfo.Builder(0, new ComponentName(this, NetworkSchedulerService.class))
                .setRequiresCharging(true)
                .setMinimumLatency(1000)
                .setOverrideDeadline(2000)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .setPersisted(true)
                .build();

        JobScheduler jobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        assert jobScheduler != null;
        jobScheduler.schedule(myJob);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        stopService(new Intent(this, NetworkSchedulerService.class));
        super.onStop();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onStart() {
        super.onStart();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Intent startServiceIntent = new Intent(this, NetworkSchedulerService.class);
            startService(startServiceIntent);
        }

    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {

        findViewById(R.id.loading).setVisibility(View.GONE);
        AndroidNetworking.forceCancelAll();

        if (myFragsTags.size() <= 1) {
            super.onBackPressed();
            return;
        }

        Fragment current = getSupportFragmentManager().findFragmentByTag(myFragsTags.get(myFragsTags.size() - 1));

        if (current instanceof Quiz) {
            if (!doubleBackToExitPressedOnce) {
                this.doubleBackToExitPressedOnce = true;
                Toast.makeText(this, "برای خروج دوباره برگشت را بزنید!", Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        doubleBackToExitPressedOnce = false;
                    }
                }, 2000);
                return;
            }
        }

        if (current instanceof Home) {
            super.onBackPressed();
            return;
        }

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (current instanceof ShowSection || current instanceof ShowSubject
                || current instanceof Quiz || current instanceof ShowVideo
                || current instanceof EditProfile || current instanceof Test
                || current instanceof Advertise || current instanceof Profile
                || current instanceof Answer || current instanceof ShowProduct) {
            transaction.remove(current);
        } else {
            transaction.hide(current);
        }

        myFragsTags.remove(myFragsTags.get(myFragsTags.size() - 1));

        Fragment current1 = getSupportFragmentManager().findFragmentByTag(myFragsTags.get(myFragsTags.size() - 1));

        transaction.show(current1);

        if (current1 instanceof Home) {
            bottomNavigation.setVisibility(View.VISIBLE);
            bottomNavigation.animate().alpha(1).setDuration(300).start();
            bottomNavigation.show(0, true);

        } else if (current1 instanceof Profile) {
            bottomNavigation.setVisibility(View.VISIBLE);
            bottomNavigation.animate().alpha(1).setDuration(300).start();
            bottomNavigation.show(4, true);

        } else if (current1 instanceof Shop) {
            bottomNavigation.setVisibility(View.VISIBLE);
            bottomNavigation.animate().alpha(1).setDuration(300).start();
            bottomNavigation.show(3, true);

        } else if (current1 instanceof Test) {
            bottomNavigation.setVisibility(View.VISIBLE);
            bottomNavigation.animate().alpha(1).setDuration(300).start();
            bottomNavigation.show(2, true);

        }

        transaction.commit();

        String route = "";
        for (int i = 0; i < myFragsTags.size(); i++)
            route += myFragsTags.get(i) + " - ";
        Log.e("route", route);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void runHome() {

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                if (MyApplication.isFirstRun()) {
                    AddFragments(new Number(), "Number", false);
                } else
                    get_user_info();
            }
        });
    }

    private void get_user_info() {

        AndroidNetworking.post(MyApplication.Domain + "profile")
                .addHeaders("Accept", "application/json")
                .addHeaders("Authorization", "Bearer " + MyApplication.token)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            Log.e("error", "products are here!");

                            JSONObject data = response.getJSONObject("data");
                            MyApplication.PhoneNumber = data.getString("phone");
                            MyApplication.fullName = data.getString("fullName");
                            MyApplication.Wallet = Integer.parseInt(data.getString("wallet"));
                            MyApplication.GiftWallet = Integer.parseInt(data.getString("gift_wallet"));
                            MyApplication.City = data.getJSONObject("city").getString("name");
                            MyApplication.Grade = data.getJSONObject("grade").getString("title");

                            AddFragments(new Home(), "Home", true);

                        } catch (JSONException e) {
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        int errorCode = anError.getErrorCode();
                        Log.e("error", anError.getErrorDetail() + " / " + errorCode);
                        if (errorCode == 403)
                            BaseActivity.getInstace().failure();
                    }
                });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 0:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {

                    File file = new File(Environment.getExternalStorageDirectory() + "/AmoozeshMelli");
                    if (!file.exists())
                        file.mkdirs();
                } else {
                    finish();
                }
                break;

            default:
                break;
        }
    }

    public void failure() {

        SharedPreferences.Editor editor = BaseActivity.getInstace().getSharedPreferences("MY_APP", MODE_PRIVATE).edit();
        editor.putBoolean("is_first_run", true);
        editor.apply();

        MyApplication.restartApp(getApplicationContext());
    }

}
