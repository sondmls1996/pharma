package app.pharma.com.pharma.Fragment.Pharma;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import app.pharma.com.pharma.Adapter.List_Rate_Adapter;
import app.pharma.com.pharma.Model.Common;
import app.pharma.com.pharma.Model.Constant;
import app.pharma.com.pharma.Model.Constructor.Object.Pharma_Obj;
import app.pharma.com.pharma.Model.Constructor.Object.Pill_obj;
import app.pharma.com.pharma.Model.Constructor.Object.Rating_Obj;
import app.pharma.com.pharma.Model.Constructor.Object.Sick_Obj;
import app.pharma.com.pharma.Model.Database.DatabaseHandle;
import app.pharma.com.pharma.Model.Database.User;
import app.pharma.com.pharma.Model.JsonConstant;
import app.pharma.com.pharma.Model.ServerPath;
import app.pharma.com.pharma.Model.Utils;
import app.pharma.com.pharma.R;
import app.pharma.com.pharma.Support.EndlessScroll;
import app.pharma.com.pharma.activity.Detail.Detail;

import static io.realm.internal.SyncObjectServerFacade.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 */
public class Pharma_Detail_Rate extends Fragment {

    RecyclerView lv_rate;
    ArrayList<View> arrView;
    LinearLayout ln_rate_now;
    int Mainpage = 1;
    List_Rate_Adapter adapter;
    ArrayList<Rating_Obj> arr;
    Utils util;
    SwipeRefreshLayout swip;
    private boolean mIsLoading, isLoadFinish;
    String allCmt;
    View v;
    DatabaseHandle db;
    View footer;
    User user;
    String idProduct="";
    String type = "";
    String keyId = "";
    String numComment = "0";
    TextView tv_comment,tv_de_comment;
    LayoutInflater inflater2;
    RelativeLayout rl_top;
    public Pharma_Detail_Rate() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         v = inflater.inflate(R.layout.fragment_pharma__rate, container, false);
        
        init();

        return v;
    }

    private void init() {
        db = new DatabaseHandle();
        user = db.getAllUserInfor();
        arr = new ArrayList<>();
         footer = ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.footer_view, null, false);
        util = new Utils();
        setRecycle(v);
        swip = v.findViewById(R.id.swip);
        swip.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Mainpage = 1;
                getListRate(Mainpage,type,keyId);
            }
        });
        ln_rate_now = v.findViewById(R.id.ln_rate_now);
        rl_top = v.findViewById(R.id.rl_rate_top);
        arrView = new ArrayList<>();

        tv_de_comment = v.findViewById(R.id.txt_de_comment);
        ln_rate_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Utils.isLogin()){
                    showDialogRate();
                }else{
                    Utils.dialogNotif(getActivity().getResources().getString(R.string.you_not_login));
                }
            }
        });
        inflater2 = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        setRelativeTop(Detail.key);



    }
    public void setRecycle(View v){
        lv_rate = v.findViewById(R.id.lv_list_rate);
        lv_rate.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        lv_rate.setLayoutManager(layoutManager);
        adapter = new List_Rate_Adapter(getActivity(), arr);
        lv_rate.setAdapter(adapter);
        EndlessScroll endlessScroll = new EndlessScroll(layoutManager,getApplicationContext()) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                Mainpage++;
                Log.d("PAGE_RATE",Mainpage+"");
                getListRate(Mainpage,type,keyId);
            }
        };
        lv_rate.addOnScrollListener(endlessScroll);

    }
    private void setRelativeTop(String key) {

        if(key.equals("pharma")){
            setHeaderPharma();
            keyId = "idStore";
            getListRate(Mainpage,type,keyId);
        }else if(key.equals("pill")){
            setHeaderPill();
            keyId = "idProduct";
            getListRate(Mainpage,type,keyId);
        }else if(key.equals("sick")){
            setHeaderSick();
            keyId = "idDisease";
            getListRate(Mainpage,type,keyId);

        }


    }

    private void setHeaderSick() {
        View rowView = inflater2.inflate(R.layout.rate_sick_include, null);
        TextView tvName = rowView.findViewById(R.id.include_sick_name);
        TextView tvCk = rowView.findViewById(R.id.include_sick_catalo);
        TextView tvDate = rowView.findViewById(R.id.include_sick_day);
        TextView tvLike = rowView.findViewById(R.id.txt_like);
        TextView tvCommment = rowView.findViewById(R.id.txt_comment);
        ImageView img = rowView.findViewById(R.id.include_sick_img);
        LinearLayout ln = rowView.findViewById(R.id.include_sick_star);
        Sick_Obj sick = (Sick_Obj)Detail.headerObj;
        idProduct = sick.getId();
        allCmt = sick.getCmt()+"";
        type = "disease";
        tvName.setText(sick.getName());
        Utils.loadImagePicasso(ServerPath.ROOT_URL+sick.getImages().get(0),img);
        tvLike.setText(sick.getLike()+"");
        tvCommment.setText(sick.getCmt()+"");

        int s = Integer.valueOf(sick.getStar().intValue());
        LayoutInflater vi = (LayoutInflater) Common.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
// insert into main view
        if(s>0){
            for(int i = 0; i<s;i++){
                View star = vi.inflate(R.layout.star, null);

                ln.addView(star, 0, new ViewGroup.LayoutParams(40, 40));
            }
        }else{
            View nullTv = vi.inflate(R.layout.null_tv_smal, null);

            ln.addView(nullTv, 0);
        }

        rl_top.addView(rowView);
    }

    private void setHeaderPill() {
        View rowView = inflater2.inflate(R.layout.rate_pill_include, null);
        TextView tvName = rowView.findViewById(R.id.include_pill_name);
        TextView tvPrice = rowView.findViewById(R.id.include_pill_price);
        LinearLayout ln = rowView.findViewById(R.id.inclide_pill_star);
        ln.removeAllViews();
        ImageView img = rowView.findViewById(R.id.include_pill_image);
        TextView like = rowView.findViewById(R.id.txt_like);
        TextView cmt = rowView.findViewById(R.id.txt_comment);

        Pill_obj obj = (Pill_obj) Detail.headerObj;
        idProduct = obj.getId();
        type = "product";
        tvName.setText(obj.getName());
        tvPrice.setText(Constant.format.format((obj.getPrice()))+"VND");
        Utils.loadImagePicasso(ServerPath.ROOT_URL+obj.getImages().get(0),img);
        like.setText(obj.getLike()+"");
        cmt.setText(obj.getComment()+"");
        allCmt = obj.getComment()+"";
        if(obj.getStar()!=null){

            int s = Integer.valueOf(obj.getStar().intValue());
            LayoutInflater vi = (LayoutInflater) Common.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if(s>0){
                for(int i = 0; i<s;i++){
                    View star = vi.inflate(R.layout.star, null);

                    ln.addView(star, 0, new ViewGroup.LayoutParams(30, 30));
                }
            }else{
                View nullTv = vi.inflate(R.layout.null_tv_smal, null);

                ln.addView(nullTv, 0);
            }


        }
        rl_top.addView(rowView);
    }

    private void setHeaderPharma() {
        View rowView = inflater2.inflate(R.layout.rate_pharma_include, null);
        TextView tvName = rowView.findViewById(R.id.include_pharma_name);
        TextView tvAround = rowView.findViewById(R.id.include_pharma_around);
        ImageView img = rowView.findViewById(R.id.include_pharma_image);
        LinearLayout ln = rowView.findViewById(R.id.include_pharma_star);
        ln.removeAllViews();
        Pharma_Obj pharma = (Pharma_Obj) Detail.headerObj;
        idProduct = pharma.getId();
        type = "store";
        tvName.setText(pharma.getName());
        allCmt = pharma.getComment();
        int s = Integer.valueOf(pharma.getStar().intValue());
        LayoutInflater vi = (LayoutInflater) Common.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
// insert into main view
        if(s>0){
            for(int i = 0; i<s;i++){
                View star = vi.inflate(R.layout.star, null);

                ln.addView(star, 0, new ViewGroup.LayoutParams(40, 40));
            }
        }else{
            View nullTv = vi.inflate(R.layout.null_tv_smal, null);

            ln.addView(nullTv, 0);
        }

        if(Common.lat!=0&&Common.lng!=0){
            Location location = new Location("");
            location.setLatitude(Common.lat);
            location.setLongitude(Common.lng);
            Location finishLocation = new Location("");
            finishLocation.setLatitude(pharma.lat);
            finishLocation.setLongitude( pharma.lng);

            float distance = location.distanceTo(finishLocation);
            if(distance>=1000){
                distance = distance/1000;
                if(distance>0){
                    int d = (int) Math.ceil(distance);
                    String straround = d+"";
                    tvAround.setText("Cách "+straround+" km");
                }else{
                }
            }else{
                if(distance>0){
                    int d = (int) Math.ceil(distance);
                    String straround = d+"";

                    tvAround.setText("Cách "+straround+" m");

                }
            }

        }
        Picasso.with(getActivity()).load(ServerPath.ROOT_URL+pharma.getImage().get(0)).into(img);
        rl_top.addView(rowView);



    }

    private void getListRate(int page,String type,String keyId) {
        if(page==1){
            arr.clear();
        }

        mIsLoading = true;
        Map<String,String> map = new HashMap<>();
        map.put("page",page+"");
        map.put("type",type);
        map.put(keyId,Detail.id);
        final boolean[] isEmpty = {false};
        Response.Listener<String> response = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.d("RESPONSE_RATE_CMT",response);
                    swip.setRefreshing(false);
                    JSONObject jo = new JSONObject(response);
                    String code = jo.getString(JsonConstant.CODE);
                    switch (code){
                        case "0":
                            new AsyncTask<Void,Void,Void>(){

                                @Override
                                protected Void doInBackground(Void... voids) {
                                    JSONArray listRate = null;
                                    try {

                                        listRate = jo.getJSONArray(JsonConstant.LIST_RATING);
                                        numComment = jo.getString(JsonConstant.numberCmt);
                                        if(listRate.length()>0){
                                            for (int i = 0; i<listRate.length();i++){
                                                JSONObject idx = listRate.getJSONObject(i);
                                                JSONObject Rating = idx.getJSONObject(JsonConstant.RATING);
                                                Rating_Obj rate = new Rating_Obj();
                                                if(Rating.has(JsonConstant.SHORT_CMT)){
                                                    rate.setShortComment(Rating.getString(JsonConstant.SHORT_CMT));
                                                }
                                                if(Rating.has(JsonConstant.COMMENT)){
                                                    rate.setComment(Rating.getString(JsonConstant.COMMENT));
                                                }
                                                if(Rating.has(JsonConstant.STAR)){
                                                    rate.setStar(Rating.getDouble(JsonConstant.STAR));
                                                }
                                                if(Rating.has(JsonConstant.TIME)){
                                                    rate.setTime(Rating.getLong(JsonConstant.TIME));
                                                }
                                                if(Rating.has(JsonConstant.USERNAME)){
                                                    rate.setUserName(Rating.getString(JsonConstant.USERNAME));
                                                }
                                                arr.add(rate);

                                            }
                                            isEmpty[0] = false;
                                        }else{
                                            isEmpty[0] = true;
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    return null;
                                }

                                @Override
                                protected void onPostExecute(Void aVoid) {
                                     if(isEmpty[0]&&page>1){
                                         Mainpage = page-1;
                                     }
                                    mIsLoading = false;
                                    switch (type){
                                        case "store":
                                            tv_de_comment.setText(getActivity().getResources().getString(R.string.people_cmt,
                                                    allCmt,"nhà thuốc"));
                                            break;
                                        case "product":
                                            tv_de_comment.setText(getActivity().getResources().getString(R.string.people_cmt,
                                                    allCmt,"sản phẩm"));
                                            break;
                                        case "disease":
                                            tv_de_comment.setText(getActivity().getResources().getString(R.string.people_cmt,
                                                    allCmt,"bài viết"));
                                            break;
                                    }

                                    adapter.notifyDataSetChanged();
                                    super.onPostExecute(aVoid);
                                }
                            }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                            break;
                        case "1":

                            switch (type){
                                case "store":
                                    tv_de_comment.setText(getActivity().getResources().getString(R.string.people_cmt,
                                            allCmt,"nhà thuốc"));
                                    break;
                                case "product":
                                    tv_de_comment.setText(getActivity().getResources().getString(R.string.people_cmt,
                                            allCmt,"sản phẩm"));
                                    break;
                                case "disease":
                                    tv_de_comment.setText(getActivity().getResources().getString(R.string.people_cmt,
                                            allCmt,"bài viết"));
                                    break;
                            }

                            break;
                    }




                } catch (JSONException e) {

                    mIsLoading = false;
                    isLoadFinish = false;
                    e.printStackTrace();
                }
                Log.d("RESPONSE_RATE_LIST",response);
            }
        };
        Utils.PostServer(getActivity(), ServerPath.LIST_RATING,map,response);
    }

    private void showDialogRate() {
        Dialog dialog = new Dialog(Common.context);
        Window view=((Dialog)dialog).getWindow();
        view.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
// to get rounded corners and border for dialog window
        view.setBackgroundDrawableResource(R.drawable.border_white);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_rate);
        dialog.setCanceledOnTouchOutside(true);
        AppCompatRatingBar rating = dialog.findViewById(R.id.rating_bar);
        EditText short_cmt = dialog.findViewById(R.id.ed_shortcmt);
        EditText cmt = dialog.findViewById(R.id.ed_comment);
        Button btnrate = dialog.findViewById(R.id.btn_rate);
        TextView title = dialog.findViewById(R.id.txt_title_rate);
        if(Detail.key.equals("pill")){
            title.setText(getResources().getString(R.string.title_rate_pill));
        }else if(Detail.key.equals("pharma")){
            title.setText(getResources().getString(R.string.title_rate_pharma));
        }else if(Detail.key.equals("sick")){


            title.setText(getResources().getString(R.string.title_rate_sick));
        }

        btnrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                util.showLoading(getActivity(),10000,true);
            if(rating.getRating()==0){
                util.showLoading(getActivity(),10000,false);
                Toast.makeText(getActivity(),"Hãy đánh giá sao",Toast.LENGTH_SHORT).show();
            }else if(short_cmt.getText().toString().equals("")){
                util.showLoading(getActivity(),10000,false);
                Toast.makeText(getActivity(),"Hãy ghi bình luận ngắn",Toast.LENGTH_SHORT).show();
            }else if(cmt.getText().toString().equals("")){
                util.showLoading(getActivity(),10000,false);
                Toast.makeText(getActivity(),"Hãy ghi bình luận",Toast.LENGTH_SHORT).show();
            }else{
                Map<String,String> map = new HashMap<>();
                map.put("type",type);
                map.put("accessToken",user.getToken());
                map.put("shortComment",short_cmt.getText().toString());
                map.put("comment",cmt.getText().toString());
                map.put("star",rating.getRating()+"");
                if(type.equals("product")){
                    map.put("idProduct",idProduct);
                }
                if(type.equals("store")){
                    map.put("idStore",idProduct);
                }
                if(type.equals("disease")){
                    map.put("idDisease",idProduct);
                }


                Response.Listener<String> response = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("RESPONSE_COMMENT_PILL",response);
                        try {
                            JSONObject jo = new JSONObject(response);
                            String code = jo.getString(JsonConstant.CODE);
                            switch (code){
                                case "0":
                                    util.showLoading(getActivity(),10000,false);
                                    dialog.dismiss();
                                    Utils.dialogNotif("Đánh giá thành công");

                                    break;
                                case "-1":
                                    util.showLoading(getActivity(),10000,false);
                                    Utils.dialogNotif("Phiên đăng nhập hết hạn, vui lòng đăng nhập lại");
                                    break;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                };

                Utils.PostServer(getActivity(),ServerPath.COMMENT_PILL,map,response);
            }
            }
        });
        dialog.show();
    }

}
