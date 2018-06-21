package app.pharma.com.pharma.activity.Detail;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import app.pharma.com.pharma.Model.Common;
import app.pharma.com.pharma.Model.Constant;
import app.pharma.com.pharma.Model.Constructor.Object.Order_Detail_Obj;
import app.pharma.com.pharma.Model.Database.DatabaseHandle;
import app.pharma.com.pharma.Model.Database.User;
import app.pharma.com.pharma.Model.JsonConstant;
import app.pharma.com.pharma.Model.ServerPath;
import app.pharma.com.pharma.Model.Utils;
import app.pharma.com.pharma.R;

public class Order_Detail extends AppCompatActivity {
    String id = "";
    DatabaseHandle db;
    User user;
    Button btnCancel;
    TextView tvproduct;
    TextView tvStt;
    Utils utils;
    TextView tvUserInfo;
    Order_Detail_Obj detail;

    ImageView img_order;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order__detail);
        Intent it = getIntent();
        Common.context = this;
        if(it.getExtras()!=null){
            id = it.getStringExtra("id");
        }
        utils = new Utils();
        db = new DatabaseHandle();
        TextView tvTitle = (TextView)findViewById(R.id.title);
        RelativeLayout imgBack = findViewById(R.id.img_back);
        img_order = findViewById(R.id.img_order_img);
            tvTitle.setText(getResources().getString(R.string.detail_order));
        tvUserInfo = findViewById(R.id.txt_custom);
        btnCancel = findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Utils.isLogin()){
                   showDialogCancel();
                }
            }


        });
        tvStt = findViewById(R.id.tv_stt);



        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        tvproduct = findViewById(R.id.txt_product);
        if(Utils.isLogin()){
            user = db.getAllUserInfor();
        }
        getData();
    }

    private void setStatus(String status) {
         int sdk = android.os.Build.VERSION.SDK_INT;
        if(status.equals("new")){

            if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                tvStt.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.soild_green) );
            } else {
                tvStt.setBackground(ContextCompat.getDrawable(this, R.drawable.soild_green));
            }
            tvStt.setText("Mới");
            tvStt.setTextColor(getResources().getColor(R.color.white));

        }

        if(status.equals("paid")){
            if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                tvStt.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.soild_gray) );
            } else {
                tvStt.setBackground(ContextCompat.getDrawable(this, R.drawable.soild_gray));
            }
            tvStt.setText("Đã thanh toán");
            tvStt.setTextColor(getResources().getColor(R.color.white));
        }
        if(status.equals("disable")){
            if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                tvStt.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.soild_red) );
            } else {
                tvStt.setBackground(ContextCompat.getDrawable(this, R.drawable.soild_red));
            }
            tvStt.setText("Đã hủy");
            tvStt.setTextColor(getResources().getColor(R.color.white));
        }
    }

    private void showDialogCancel() {
        Dialog dialog = new Dialog(Order_Detail.this);
        Window view=((Dialog)dialog).getWindow();
        view.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
// to get rounded corners and border for dialog window
        view.setBackgroundDrawableResource(R.drawable.border_white);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_cancel_order);
        dialog.setCanceledOnTouchOutside(true);
        TextView tv_ok = dialog.findViewById(R.id.tv_ok);
        TextView tv_cancel = dialog.findViewById(R.id.tv_close);
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                cancelOrder();
            }
        });
//        TextView title = dialog.findViewById(R.id.txt_title_rate);
//        if(Detail.key.equals("pill")){
//            title.setText(getResources().getString(R.string.title_rate_pill));
//        }else if(Detail.key.equals("pharma")){
//            title.setText(getResources().getString(R.string.title_rate_pharma));
//        }else if(Detail.key.equals("sick")){
//            title.setText(getResources().getString(R.string.title_rate_sick));
//        }
        dialog.show();
    }

    private void cancelOrder() {
        utils.showLoading(this,20000,true);
        Map<String,String> map = new HashMap<>();
        map.put("accessToken",user.getToken());
        map.put("idOrder",detail.getIdOrder());
        Response.Listener<String> response = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                utils.showLoading(Order_Detail.this,20000,false);
                try {
                    JSONObject jo = new JSONObject(response);
                    String code = jo.getString(JsonConstant.CODE);
                    switch (code){
                        case "0":
                            btnCancel.setVisibility(View.GONE);
                       Utils.ShowNotifString(getResources().getString(R.string.cancel_success), new Utils.ShowDialogNotif.OnCloseDialogNotif() {
                           @Override
                           public void onClose(Dialog dialog) {
                               dialog.dismiss();
                               finish();
                           }
                       });
                            break;
                        case "1":
                            Utils.ShowNotifString(getResources().getString(R.string.error), new Utils.ShowDialogNotif.OnCloseDialogNotif() {
                                @Override
                                public void onClose(Dialog dialog) {
                                    dialog.dismiss();
                                   // finish();
                                }
                            });
                            break;
                        case "-1":
                            Utils.ShowNotifString(getResources().getString(R.string.session_out), new Utils.ShowDialogNotif.OnCloseDialogNotif() {
                                @Override
                                public void onClose(Dialog dialog) {
                                    dialog.dismiss();
                                   // finish();
                                }
                            });
                            break;

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        Utils.PostServer(this,ServerPath.CANCEL_ORDER,map,response);
    }

    private void getData() {
        utils.showLoading(this,20000,true);
        Map<String,String> map = new HashMap();
        map.put("idOrder",id);
        map.put("accessToken",user.getToken());
        Response.Listener<String> response = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("RESPONSE_DETAIL_ORDER",response);
                try {
                    utils.showLoading(Order_Detail.this,20000,false);
                    JSONObject jo = new JSONObject(response);
                    JSONArray ja = jo.getJSONArray(JsonConstant.DATA);
                    for (int i =0; i<ja.length();i++){
                        JSONObject idx = ja.getJSONObject(i);
                        JSONObject product = idx.getJSONObject(JsonConstant.PRODUCT_ORDER);


                        detail = new Order_Detail_Obj();
                        detail.setIdOrder(idx.getString(JsonConstant.ID));
                        detail.setTime(idx.getLong(JsonConstant.TIME));
                        detail.setPhone(idx.getString(JsonConstant.PHONE));
                        detail.setAdr(idx.getString(JsonConstant.ADRESS));
                        detail.setNameUser(idx.getString(JsonConstant.FULLNAME));
                        detail.setStatus(idx.getString(JsonConstant.STATUS));
                        detail.setEmail(idx.getString(JsonConstant.EMAIL));
                        detail.setTotalMoney(idx.getInt(JsonConstant.TOTAL_MONEY));
                        detail.setIdUser(idx.getString(JsonConstant.ID_ACC));
                        detail.setIdPrd(product.getString(JsonConstant.ID));
                        detail.setNamePrd(product.getString(JsonConstant.NAME));
                        detail.setQuality(product.getString(JsonConstant.QUALITY));
                        detail.setImgPrd(product.getString(JsonConstant.IMAGE));
                        if(product.getString(JsonConstant.PRICE).equals("")){
                            detail.setPricePrd(0);
                        }else{
                            detail.setPricePrd(product.getInt(JsonConstant.PRICE));
                        }


                        if(detail.getStatus().equals("paid")||detail.getStatus().equals("disable")){
                            btnCancel.setVisibility(View.GONE);
                        }else{
                            btnCancel.setVisibility(View.VISIBLE);
                        }

                        tvproduct.setText(Html.fromHtml(getResources().getString(R.string.order_dialog_product,
                                detail.getNamePrd(),detail.getQuality(),
                                Constant.format.format(detail.getPricePrd()),Constant.format.format(detail.getTotalMoney()))
                                ));
                        tvUserInfo.setText(Html.fromHtml(getResources().getString(R.string.order_dialog_customer,
                                detail.getNameUser(),detail.getAdr(),
                                detail.getPhone(),detail.getEmail())
                        ));

                        Picasso.with(getApplicationContext()).load
                                (ServerPath.ROOT_URL+detail.getImgPrd()).into(img_order);
                        setStatus(detail.getStatus());
//                        JSONObject order = idx.getJSONObject(JsonConstant.ORDER);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        Utils.PostServer(this, ServerPath.DETAIL_ORDER,map,response);
    }

    @Override
    protected void onResume() {
        Common.context = this;
        super.onResume();
    }
}
