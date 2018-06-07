package app.pharma.com.pharma.activity.Detail;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import app.pharma.com.pharma.Adapter.Slide_Image_Adapter;
import app.pharma.com.pharma.Model.Common;
import app.pharma.com.pharma.Model.Constant;
import app.pharma.com.pharma.Model.Constructor.Object.Pill_obj;
import app.pharma.com.pharma.Model.Database.DatabaseHandle;
import app.pharma.com.pharma.Model.Database.User;
import app.pharma.com.pharma.Model.ServerPath;
import app.pharma.com.pharma.Model.Utils;
import app.pharma.com.pharma.R;
import me.relex.circleindicator.CircleIndicator;

public class Order extends AppCompatActivity {
    EditText ed_adr,ed_fullname,ed_email,ed_phone;
    RelativeLayout img_back;
    TextView title;
    private ViewPager mPager;
    EditText quality;
    TextView price;
    Pill_obj pillObj;
    TextView name_pill;
    LinearLayout ln_order;
    Slide_Image_Adapter adapter;
    DatabaseHandle db;
    int countPrice = 0;
    User user;
    Utils utils;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;

    private ArrayList<String> ImagesArray = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        Common.context = this;
        Intent it = getIntent();
        try{
            if(it.getExtras()!=null){
                pillObj = (Pill_obj)it.getSerializableExtra("obj");
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        init();
    }

    private void init() {
        if(Utils.isLogin()){
            db = new DatabaseHandle();
            user = db.getAllUserInfor();
        }
        utils = new Utils();
        ImagesArray = Detail.imagesArray;
        adapter = new Slide_Image_Adapter(Common.context,ImagesArray);
        mPager = (ViewPager) findViewById(R.id.slide_image);
        CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);
        mPager.setAdapter(adapter);
        indicator.setViewPager(mPager);
        adapter.registerDataSetObserver(indicator.getDataSetObserver());

        ln_order = findViewById(R.id.ln_order);
        name_pill = findViewById(R.id.name_pill);
        name_pill.setText(pillObj.getName());
        ln_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.setAlphalAnimation(v);
                if(Utils.isLogin()){

                    if(TextUtils.isEmpty(quality.getText().toString())){
                        Toast.makeText(getApplicationContext(),"Hãy điền số lượng muốn mua",Toast.LENGTH_SHORT).show();
                    }else{
                        pillObj.setQuality(Integer.parseInt(quality.getText().toString()));
                        dialogPreview();
                    }

                }else{
                    Utils.dialogNotif(getResources().getString(R.string.you_not_login));
                }
            }
        });

        title = findViewById(R.id.title);
        img_back = findViewById(R.id.img_back);
        title.setText(getResources().getString(R.string.order));
        quality = findViewById(R.id.ed_quality);

        price = findViewById(R.id.tv_price);
        quality.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    if(s.equals("1")){
                        countPrice = Integer.parseInt(s.toString())*pillObj.getPrice();
                        price.setText(Constant.format.format((countPrice))+" VND");
                    }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                    if(!s.toString().equals("")&&!s.toString().equals("0")){
                        countPrice = Integer.parseInt(s.toString())*pillObj.getPrice();
                        price.setText(Constant.format.format((countPrice)));
                    }
                    if(s.toString().equals("0")){
                        quality.setText("1");
                        countPrice = Integer.parseInt("1")*pillObj.getPrice();
                        price.setText(Constant.format.format((countPrice)));
                    }
            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });
        quality.setText("1");
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        ed_adr = findViewById(R.id.ed_adr);
        ed_fullname = findViewById(R.id.ed_fullname);
        ed_email = findViewById(R.id.ed_email);
        ed_phone = findViewById(R.id.ed_phone);
        if(Utils.isLogin()){
            ed_adr.setText(user.getAdr());
            ed_fullname.setText(user.getName());
            ed_email.setText(user.getEmail());
            ed_phone.setText(user.getPhone());
        }
    }

    public void dialogPreview(){
        Dialog dialog = new Dialog(Common.context);
        Window view=((Dialog)dialog).getWindow();
        view.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_order);
        dialog.setCanceledOnTouchOutside(true);
        TextView tv_prd = dialog.findViewById(R.id.tv_prd_info);
        TextView tv_cus = dialog.findViewById(R.id.tv_custom_info);
        TextView tv_access = dialog.findViewById(R.id.tv_access);
        TextView tv_cancel = dialog.findViewById(R.id.tv_cancel);
        int numqualy = pillObj.getQuality();
        int price = pillObj.getPrice();

        int tong = numqualy*price;
        if(tong>0){
            tv_prd.setText(Html.fromHtml(Order.this.getResources().getString(R.string.order_dialog_product,
                    pillObj.getName(),numqualy+"",
                    Constant.format.format(price)+"VND",Constant.format.format(tong)+"VND")));
        }
        String adr = ed_adr.getText().toString();
        String name = ed_fullname.getText().toString();
        String mail = ed_email.getText().toString();
        String phone = ed_phone.getText().toString();
        tv_cus.setText(Html.fromHtml(Order.this.getResources().getString(R.string.order_dialog_customer,
                name,adr,phone,mail)));

        tv_access.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                order();
            }
        });

        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }



    private void order() {
        String adr = ed_adr.getText().toString();
        String name = ed_fullname.getText().toString();
        String mail = ed_email.getText().toString();
        String phone = ed_phone.getText().toString();


        if(adr.equals("")){
            Toast.makeText(getApplicationContext(),"Hãy thêm địa chỉ",Toast.LENGTH_SHORT).show();
        }else if(phone.equals("")){
            Toast.makeText(getApplicationContext(),"Hãy thêm số điện thoại",Toast.LENGTH_SHORT).show();

        }else if(name.equals("")){
            Toast.makeText(getApplicationContext(),"Hãy thêm tên người nhận",Toast.LENGTH_SHORT).show();
        }else if(phone.length()<10){
            Toast.makeText(getApplicationContext(),"Số điện thoại không đúng",Toast.LENGTH_SHORT).show();
        }else{
            Map<String, String> map = new HashMap<>();
            map.put("accessToken",user.getToken());
            map.put("idProduct",pillObj.getId());
            map.put("quality",quality.getText().toString());
            map.put("fullName",name);
            map.put("email",mail);
            map.put("phone",phone);
            map.put("address",adr);

            Response.Listener<String> response = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("RESPONSE_ORDER",response);
                }
            };
            Utils.PostServer(this, ServerPath.BUY_NOW,map,response);

        }
     //   utils.showLoading(Order.this,20000,true);



    }

    @Override
    protected void onResume() {
        Common.context = this;
        super.onResume();
    }
}
