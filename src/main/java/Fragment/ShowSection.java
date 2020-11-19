package Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.amoozeshmelli.BaseActivity;
import com.amoozeshmelli.MyApplication;
import com.amoozeshmelli.R;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import Adapter.SectionAdapter;
import Data.Section;
import Tools.TransitionHelper;
import ViewModel.SectionViewModel;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class ShowSection extends TransitionHelper.BaseFragment {

    public View view;

    private SectionAdapter sectionAdapter;
    private List<Section> MainSections = new ArrayList<>();
    private ShowSection instance = this;

    int Status = -1;

    public ShowSection() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e("Fragments", "ShowSection Created!");

        view = inflater.inflate(R.layout.show_section, container, false);

        view.findViewById(R.id.back_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseActivity.of(getActivity()).onBackPressed();
            }
        });

        RecyclerView sectionRecyclerView = view.findViewById(R.id.sectionRecyclerView);

        sectionAdapter = new SectionAdapter(getContext(), MainSections);

        sectionAdapter.setOnItemClickListener(new SectionAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(final int position) {

                BaseActivity.of(getActivity()).SectionID = MainSections.get(position).getID();
                BaseActivity.of(getActivity()).SectionName = MainSections.get(position).getTitle();

                if (MainSections.get(position).hasPaid()) {
                    BaseActivity.of(getActivity()).AddFragments(new ShowVideo(), "ShowVideo", false);
                    return;
                } else {

                    SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.NORMAL_TYPE);

                    sweetAlertDialog.setConfirmText("خرید");
                    sweetAlertDialog.setTitleText("خرید بخش");

                    sweetAlertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                        @Override
                        public void onShow(DialogInterface dialog) {
                            SweetAlertDialog alertDialog = (SweetAlertDialog) dialog;
                            TextView title = alertDialog.findViewById(R.id.title_text);
                            title.setTextColor(getContext().getResources().getColor(R.color.white));

                            Button btn = alertDialog.findViewById(R.id.confirm_button);
                            btn.setBackgroundColor(getContext().getResources().getColor(R.color.colorPrimaryDark));

                            TextView text = alertDialog.findViewById(R.id.content_text);
                            text.setTextColor(getContext().getResources().getColor(R.color.white));
                            text.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                            text.setSingleLine(false);
                            text.setMaxLines(6);
                            text.setLines(6);
                        }
                    });

                    if (MainSections.get(position).IsLocked()) {
                        if (MyApplication.Wallet >= MainSections.get(position).getPrice()) {

                            Status = 1;
                            sweetAlertDialog.setContentText("این بخش تا تاریخ " + MainSections.get(position).getShamsiOpeningDate() +
                                    " در دسترس نیست!" + "\n" + "همین حالا این بخش را با مبلغ " + MainSections.get(position).getEarlyPrice()
                                    + " تومان بخرید.");
                           /* cdd = new MyApplication.CustomDialogClass1(getContext(), ""
                                    , false, MainSections.get(position).getEarlyPrice(), 0, 0);*/
                        } else {

                            Status = 0;
                            sweetAlertDialog.setContentText("موجودی کیف پول شما کافی نیست؟آیا میخواهید آن را افزایش دهید؟");

                        }
                    } else {
                        if (MyApplication.Wallet >= MainSections.get(position).getPrice()) {

                            Status = 1;

                            sweetAlertDialog.setContentText("این بخش را میبایست با مبلغ" + MainSections.get(position).getPrice() +
                                    " تومان کیف پول اصلی و " + MainSections.get(position).getGiftPrice() + " تومان کیف پول هدیه تهیه کنید.");

                        } else {
                            Status = 0;
                            sweetAlertDialog.setContentText("موجودی کیف پول شما کافی نیست؟آیا میخواهید آن را افزایش دهید؟");

                        }
                    }

                    sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {

                            Log.e("CLicked" , Status + "");

                            if (Status == 0) {
                                //go to profile

                                MyApplication.increase_wallet(getContext());

                            } else {
                                AndroidNetworking.post(MyApplication.Domain + "buy_section")
                                        .setPriority(Priority.HIGH)
                                        .addHeaders("Accept", "application/json")
                                        .addHeaders("Authorization", "Bearer " + MyApplication.token)
                                        .addBodyParameter("section_id", MainSections.get(position).getID() + "")
                                        .build()
                                        .getAsJSONObject(new JSONObjectRequestListener() {
                                            @Override
                                            public void onResponse(JSONObject response) {

                                                if (response.has("error")) {

                                                    final SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.NORMAL_TYPE);
                                                    sweetAlertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                                                        @Override
                                                        public void onShow(DialogInterface dialog) {
                                                            SweetAlertDialog alertDialog = (SweetAlertDialog) dialog;
                                                            TextView title = alertDialog.findViewById(R.id.title_text);
                                                            title.setTextColor(getContext().getResources().getColor(R.color.white));

                                                            Button btn = alertDialog.findViewById(R.id.confirm_button);
                                                            btn.setBackgroundColor(getContext().getResources().getColor(R.color.colorPrimaryDark));

                                                            TextView text = alertDialog.findViewById(R.id.content_text);
                                                            text.setTextColor(getContext().getResources().getColor(R.color.white));
                                                            text.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                                            text.setSingleLine(false);
                                                            text.setMaxLines(3);
                                                            text.setLines(3);
                                                        }
                                                    });
                                                    sweetAlertDialog.setConfirmText("باشه");
                                                    sweetAlertDialog.setCancelText("افزایش اعتبار");

                                                    try {
                                                        sweetAlertDialog.setContentText(response.getString("error"));
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                    sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                        @Override
                                                        public void onClick(SweetAlertDialog sDialog) {
                                                            sDialog.dismissWithAnimation();
                                                        }
                                                    });

                                                    sweetAlertDialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                        @Override
                                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                            MyApplication.increase_wallet(getContext());
                                                            sweetAlertDialog.dismiss();
                                                        }
                                                    });

                                                    sweetAlertDialog.show();

                                                } else {
                                                    final SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.NORMAL_TYPE);
                                                    sweetAlertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                                                        @Override
                                                        public void onShow(DialogInterface dialog) {
                                                            SweetAlertDialog alertDialog = (SweetAlertDialog) dialog;
                                                            TextView title = alertDialog.findViewById(R.id.title_text);
                                                            title.setTextColor(getContext().getResources().getColor(R.color.white));

                                                            Button btn = alertDialog.findViewById(R.id.confirm_button);
                                                            btn.setBackgroundColor(getContext().getResources().getColor(R.color.colorPrimaryDark));

                                                            TextView text = alertDialog.findViewById(R.id.content_text);
                                                            text.setTextColor(getContext().getResources().getColor(R.color.white));
                                                            text.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                                            text.setSingleLine(false);
                                                            text.setMaxLines(3);
                                                            text.setLines(3);
                                                        }
                                                    });
                                                    MainSections.get(position).setHas_paid(true);
                                                    sectionAdapter.notifyDataSetChanged();
                                                    sweetAlertDialog.setConfirmText("باشه");
                                                    try {
                                                        sweetAlertDialog.setContentText(response.getString("data"));
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                    sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                        @Override
                                                        public void onClick(SweetAlertDialog sDialog) {

                                                            sDialog.dismissWithAnimation();
                                                            BaseActivity.of(getActivity()).AddFragments(new ShowVideo(), "ShowVideo", false);

                                                        }
                                                    });

                                                    sweetAlertDialog.show();
                                                }
                                            }

                                            @Override
                                            public void onError(ANError anError) {
                                                Log.e("error", anError.getErrorDetail() + " / " + anError.getErrorCode());
                                            }

                                        });
                            }

                            sDialog.dismissWithAnimation();
                        }
                    });

                    sweetAlertDialog.show();

                }

            }
        });

        sectionRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        sectionRecyclerView.setAdapter(sectionAdapter);

        TextView title = view.findViewById(R.id.title);
        TextView teacher_name = view.findViewById(R.id.teacher_name);


        title.setTypeface(MyApplication.getMyTypeFaceMedium(Objects.requireNonNull(getContext())));
        teacher_name.setTypeface(MyApplication.getMyTypeFaceMedium(Objects.requireNonNull(getContext())));

        title.setText(BaseActivity.of(getActivity()).SubjectTitle);
        teacher_name.setText(BaseActivity.of(getActivity()).TeacherName);

        Log.e("Data", BaseActivity.of(getActivity()).SubjectID + " / section");
        Log.e("Data", BaseActivity.of(getActivity()).TeacherID + " / teacher");

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);

        BaseActivity.getInstace().loading.setVisibility(View.VISIBLE);

        new Handler().post(new Runnable() {
            @Override
            public void run() {

                SectionViewModel sectionViewModel = ViewModelProviders.of(instance).get(SectionViewModel.class);
                sectionViewModel.init(MyApplication.Domain + "user_search_section",
                        BaseActivity.of(getActivity()).TeacherID,
                        BaseActivity.of(getActivity()).SubjectID);

                sectionViewModel.getModels().observe(instance, new Observer<List<Section>>() {
                    @Override
                    public void onChanged(List<Section> sections) {

                        MainSections.addAll(sections);
                        if (sectionAdapter != null)
                            sectionAdapter.notifyDataSetChanged();

                        BaseActivity.getInstace().loading.setVisibility(View.GONE);
                    }
                });
            }
        });

    }

    @Override
    public boolean onBeforeBack() {
        /* BaseActivity activity = BaseActivity.of(getActivity());
         *//*if (!activity.animateHomeIcon(MaterialMenuDrawable.IconState.BURGER)) {
            activity.drawerLayout.openDrawer(Gravity.START);
        }*/
        return super.onBeforeBack();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("Fragments", "ShowSection Destroyed!");
    }

    public Context getContext() {
        return BaseActivity.of(getActivity());
    }

}

