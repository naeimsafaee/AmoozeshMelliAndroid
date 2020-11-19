package Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amoozeshmelli.BaseActivity;
import com.amoozeshmelli.MyApplication;
import com.amoozeshmelli.R;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.DownloadListener;
import com.androidnetworking.interfaces.DownloadProgressListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import Data.Product;
import Tools.TransitionHelper;
import cn.pedant.SweetAlert.SweetAlertDialog;

import static android.content.Context.MODE_PRIVATE;

public class ShowProduct extends TransitionHelper.BaseFragment {

    public View view;
    Product product;

    public ShowProduct() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e("Fragments", "ShowProduct Created!");

        view = inflater.inflate(R.layout.dialog_2, container, false);

        product = BaseActivity.of(getActivity()).product;

        TextView title_1 = view.findViewById(R.id.title_1);
        TextView mode = view.findViewById(R.id.mode);

        TextView title = view.findViewById(R.id.title);

        TextView price = view.findViewById(R.id.price);
        TextView gift_price = view.findViewById(R.id.gift_price);
        ImageView product_image = view.findViewById(R.id.product_image);
        Button buy = view.findViewById(R.id.buy);

        title_1.setTypeface(MyApplication.getMyTypeFaceMedium(getContext()));
        title.setTypeface(MyApplication.getMyTypeFaceMedium(getContext()));

        title.setText("فروشگاه");

        view.findViewById(R.id.back_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseActivity.of(getActivity()).onBackPressed();
            }
        });

        mode.setTypeface(MyApplication.getMyTypeFaceMedium(getContext()));
        price.setTypeface(MyApplication.getMyTypeFaceMedium(getContext()));
        gift_price.setTypeface(MyApplication.getMyTypeFaceMedium(getContext()));
        buy.setTypeface(MyApplication.getMyTypeFaceBold(getContext()));

        mode.setText(product.isDownload_able() ? "محصول دانلودی" : "محصول فیزیکی");
        title_1.setText(" | " + product.getTitle() + " | ");
        gift_price.setText(product.getGift_price() + " تومان");
        price.setText(product.getPrice() + " تومان");

        if (product.getGift_price() == 0) {
            gift_price.setVisibility(View.GONE);
            view.findViewById(R.id.gifted).setVisibility(View.GONE);
        }

        Glide.with(getContext())
                .load(product.getImage_url())
                .into(product_image);

        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!product.isDownload_able())
                    return;

                AndroidNetworking.post(MyApplication.Domain + "buy_product")
                        .addHeaders("Accept", "application/json")
                        .addHeaders("Authorization", "Bearer " + MyApplication.token)
                        .addBodyParameter("product_id" , product.getID() + "")
                        .setPriority(Priority.HIGH)
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(final JSONObject response) {

                                if (response.has("error")) {


                                    SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.NORMAL_TYPE);

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

                                    sweetAlertDialog.setConfirmText("تایید");

//                                            AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
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

                                    sweetAlertDialog.show();

                                } else {
                                    SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.NORMAL_TYPE);

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
                                    sweetAlertDialog.setConfirmText("تایید");
//                                            AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                                    try {
                                        sweetAlertDialog.setContentText(response.getString("data"));

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            sDialog.dismissWithAnimation();
                                            try {
                                                startDownload(response.getString("file_path") , product.getTitle());
                                            } catch (JSONException e) {
                                                Log.e("Download" , e.getMessage());
                                                e.printStackTrace();
                                            }
                                        }
                                    });

                                    sweetAlertDialog .show();
                                }
                            }

                            @Override
                            public void onError(ANError anError) {
                                Log.e("error", anError.getErrorDetail());
                            }
                        });
            }
        });

        return view;
    }


    ProgressDialog mProgressDialog;

    private void startDownload(String file_path, String name) {

        File dir = new File(Environment.getExternalStorageDirectory(), "AmoozeshMelli");
        if (!dir.exists())
            dir.mkdirs();

        String ext = file_path.substring(file_path.lastIndexOf('.'));

        if (!file_path.contains("https"))
            file_path = file_path.replaceAll("http", "https");

        Log.e("Download" , file_path);

        mProgressDialog = new ProgressDialog(getContext());
        mProgressDialog.setMessage("در حال دانلود فایل");
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setCancelable(true);

        // execute this when the downloader must be fired
        /*final DownloadTask downloadTask = new DownloadTask(getContext());
        downloadTask.execute(file_path);
*/
        mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                AndroidNetworking.forceCancel("download");
                dialog.dismiss();
            }
        });

        mProgressDialog.show();

        AndroidNetworking.download(file_path, dir.getAbsolutePath(), name + ext)
                .setTag("download")
                .setPriority(Priority.MEDIUM)
                .build()
                .setDownloadProgressListener(new DownloadProgressListener() {
                    @Override
                    public void onProgress(long bytesDownloaded, long totalBytes) {

                        Log.e("Download" , "onProgress : " + bytesDownloaded + " / " + totalBytes);
                        mProgressDialog.setIndeterminate(false);
                        mProgressDialog.setMax(100);
                        mProgressDialog.setProgress((int) (bytesDownloaded / totalBytes * 100));

                    }
                })
                .startDownload(new DownloadListener() {
                    @Override
                    public void onDownloadComplete() {
                        Log.e("Download" , "onDownloadComplete");
                        mProgressDialog.dismiss();

                        Toast.makeText(getContext(), "فایل محصول شما در فولدر AmoozeshMelli ذخیره شد!", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(ANError error) {
                        Log.e("Download" , error.getErrorDetail() + error.getErrorCode());

                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
    }

    @Override
    public boolean onBeforeBack() {
//        BaseActivity activity = BaseActivity.of(getActivity());
        /*if (!activity.animateHomeIcon(MaterialMenuDrawable.IconState.BURGER)) {
            activity.drawerLayout.openDrawer(Gravity.START);
        }*/
        return super.onBeforeBack();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("Fragments", "ShowProduct Destroyed!");
    }

    public Context getContext() {
        return BaseActivity.of(getActivity());
    }

}

