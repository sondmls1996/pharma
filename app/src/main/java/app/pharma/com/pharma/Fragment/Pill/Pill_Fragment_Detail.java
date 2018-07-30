package app.pharma.com.pharma.Fragment.Pill;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.Response;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import app.pharma.com.pharma.Adapter.Slide_Image_Adapter;
import app.pharma.com.pharma.Model.Common;
import app.pharma.com.pharma.Model.Constant;
import app.pharma.com.pharma.Model.Constructor.Object.Pharma_Obj;
import app.pharma.com.pharma.Model.Constructor.Object.Pill_obj;
import app.pharma.com.pharma.Model.Constructor.Other_Product_Constuctor;
import app.pharma.com.pharma.Model.Database.DatabaseHandle;
import app.pharma.com.pharma.Model.Database.User;
import app.pharma.com.pharma.Model.JsonConstant;
import app.pharma.com.pharma.Model.ServerPath;
import app.pharma.com.pharma.Model.Utils;
import app.pharma.com.pharma.R;
import app.pharma.com.pharma.activity.Detail.Detail;
import app.pharma.com.pharma.activity.Detail.Order;
import me.relex.circleindicator.CircleIndicator;


/**
 * A simple {@link Fragment} subclass.
 */
public class Pill_Fragment_Detail extends Fragment {
    LinearLayout ln,ln_buy;
    private  ViewPager mPager;
    ScrollView scroll;
    ImageView hearth;
    int page = 1;
    boolean like = false;
    View v;
    public Pill_obj objPill;
    Utils utils;
    String product_id;
    User user;
    DatabaseHandle db;
    String link_share = "";
    Double star_count;
    String token = "";
    Slide_Image_Adapter adapter;
    ImageView img_share;
    TextView tv_title;
    LinearLayout ln_star;
    TextView tv_like,tv_comment;
    TextView tv_content;
    ArrayList<Other_Product_Constuctor> arrOther = new ArrayList<>();
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private static Integer[] IMAGES;
    private ArrayList<String> ImagesArray;
    public Pill_Fragment_Detail() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         v = inflater.inflate(R.layout.fragment_pill__fragment__detail, container, false);
        if(Utils.isKeyboardShow(v,getActivity())){
            Utils.hideKeyboard(getActivity());
        }
        init();

        return v;
    }

    private void init() {
        if(Utils.isLogin()){
            db = new DatabaseHandle();
            user = db.getAllUserInfor();
        }
        utils = new Utils();
        scroll = v.findViewById(R.id.scroll_detail);

        ImagesArray  = new ArrayList<>();
        hearth = (ImageView)v.findViewById(R.id.img_hearth);
        ln_buy = v.findViewById(R.id.ln_buynow);
        tv_title = v.findViewById(R.id.title_detail_pill);
        tv_comment = v.findViewById(R.id.txt_comment);
        tv_like = v.findViewById(R.id.txt_like);
        tv_content = v.findViewById(R.id.tv_content);
        img_share = v.findViewById(R.id.img_share);
        img_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.setAlphalAnimation(view);
                if(!link_share.equals("")){
                    Utils.shareLink(link_share);
                }
            }
        });
        ln_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Utils.isNetworkEnable(getActivity())){
                        if(objPill!=null){
                            Intent it = new Intent(Common.context, Order.class);
                            it.putExtra("obj",objPill);

                            startActivity(it);
                        }


                }


            }
        });
        hearth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.setAlphalAnimation(v);
                onClickHeart();
            }
        });

        loadAgaint(Detail.id);

    }

    private void onClickHeart() {
        if(!Utils.isNetworkEnable(getActivity())){
            Utils.ShowNotifString(getActivity().getResources().getString(R.string.no_internet),
                    new Utils.ShowDialogNotif.OnCloseDialogNotif() {
                @Override
                public void onClose(Dialog dialog) {
                    dialog.dismiss();

                }
            });
        }else{
            if(Utils.isLogin()){
                if(objPill!=null){
                    final int likestt = objPill.getLikeStt();

                    Map<String, String> map = new HashMap<>();
                    map.put("type","product");
                    map.put("id",product_id);
                    map.put("accessToken",user.getToken());
                    if(Detail.likestt==0){
                        map.put("likeStatus","1");

                    }else{
                        map.put("likeStatus","0");

                    }
                    Log.d("MAP",map.toString());


                    Response.Listener<String> response  = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("LIKE_STT_PILL",response);
                            try {
                                JSONObject jo = new JSONObject(response);
                                String code = jo.getString(JsonConstant.CODE);
                                if(code.equals("0")){
                                    if(likestt==0){

                                        objPill.setLikeStt(1);
                                    objPill.setLike(objPill.getLike()+1);
                                    tv_like.setText(objPill.getLike()+"");
                                    }else{

                                        objPill.setLikeStt(0);
                                    objPill.setLike(objPill.getLike()-1);
                                    tv_like.setText(objPill.getLike()+"");

                                    }
                                    Detail.headerObj = objPill;
                                    checkHearth(objPill.getLikeStt());
                                    //         Detail.headerObj = objPill;
                                }else if(code.equals("-1")){
                                    Utils.dialogNotif(getResources().getString(R.string.session_out));
                                }else{
                                    Utils.dialogNotif(getResources().getString(R.string.error));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    Utils.PostServer(Common.context,ServerPath.LIKE_PILL,map,response);
                }

            }else{
                Utils.dialogNotif(getActivity().getResources().getString(R.string.you_not_login));
            }
        }


    }

    public void loadAgaint(String id){
        loadData(id);
    }

    private void loadData(String id) {
        utils.showLoading(Common.context,10000,true);
//        if(Detail.headerObj!=null){
//            objPill = (Pill_obj) Detail.headerObj;
//            updateUi(objPill);
//        }else{
            if(!Utils.isNetworkEnable(getActivity())){
                utils.showLoading(Common.context,10000,false);
                Utils.ShowNotifString(getActivity().getResources().getString(R.string.no_internet),
                        new Utils.ShowDialogNotif.OnCloseDialogNotif() {
                            @Override
                            public void onClose(Dialog dialog) {
                                dialog.dismiss();
                                getActivity().finish();
                            }
                        });
                //   Utils.dialogNotif(getActivity().getResources().getString(R.string.no_internet));
            }else{
                Map<String,String> map = new HashMap<>();
                map.put("id", id);
                if(Utils.isLogin()){
                    map.put("accessToken",user.getToken());
                }
                Response.Listener<String> response = new Response.Listener<String>() {
                    @SuppressLint("StaticFieldLeak")
                    @Override
                    public void onResponse(String response) {

                        ImagesArray.clear();
                        Log.d("RESPONSE_DETAIL",response);
                        Detail.headerJson = response;
                        initJson(response);
                    }
                };

                Utils.PostServer(Common.context, ServerPath.DETAIL_PILL,map,response);
            }
      //  }


    }

    private void initJson(String response) {
        new AsyncTask<Void,Void,JSONObject>(){

            @Override
            protected JSONObject doInBackground(Void... voids) {


                JSONObject jo = null;
                try {
                    jo = new JSONObject(response);
                    String code = jo.getString(JsonConstant.CODE);
                    switch (code){
                        case "0":
                            ImagesArray.clear();
                            JSONObject data = jo.getJSONObject(JsonConstant.DATA);
                            JSONObject product = data.getJSONObject(JsonConstant.PRODUCT);
                            JSONObject joPrice = product.getJSONObject(JsonConstant.PRICE);
                            JSONArray images = product.getJSONArray(JsonConstant.IMAGE);

                            objPill = new Pill_obj();
                            if(product.has(JsonConstant.ID)){
                                objPill.setId(product.getString(JsonConstant.ID));
                            }
                            if(product.has(JsonConstant.NAME)){
                                objPill.setName(product.getString(JsonConstant.NAME));
                            }

                            objPill.setLinkShare(product.getString(JsonConstant.LINK_SHARE));
                            if(joPrice.getString(JsonConstant.MONEY).equals("")){
                                objPill.setPrice(0);
                            }else{
                                objPill.setPrice(joPrice.getInt(JsonConstant.MONEY));
                            }
                            objPill.setUsage(product.getString(JsonConstant.USAGE));
                            objPill.setRecoment(product.getString(JsonConstant.RECOMENT));
                            objPill.setInteractIn(product.getString(JsonConstant.INGREINFO));
                            objPill.setInteraction(product.getString(JsonConstant.INTERAC));
                            objPill.setStorage( product.getString(JsonConstant.STORAGE));
                            for (int j = 0; j<images.length();j++){
                                if(!images.getString(j).equals("")){
                                    ImagesArray.add(images.getString(j));
                                }

                            }
                            objPill.setImages(ImagesArray);
                            if(product.has(JsonConstant.LIKE)){
                                objPill.setLike(product.getInt(JsonConstant.LIKE));
                            }else{
                                objPill.setLike(0);
                            }

                            if(product.has(JsonConstant.COMMENT)){
                                objPill.setComment(product.getInt(JsonConstant.COMMENT));
                            }else{
                                objPill.setComment(0);
                            }
                            if(product.has(JsonConstant.STAR)){
                                objPill.setStar(product.getDouble(JsonConstant.STAR));
                            }else{
                                objPill.setStar((double) 0);
                            }
                            objPill.setLikeStt(product.getInt(JsonConstant.LIKE_STT));
                            if(product.has(JsonConstant.BINDING)){
                                objPill.setBinding(product.getBoolean(JsonConstant.BINDING));
                            }


                            break;

                        default:
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    return null;
                }

                return jo;
            }

            @Override
            protected void onPostExecute(JSONObject jo) {
                Detail.headerJson = jo.toString();
                    updateUi(objPill);

                super.onPostExecute(jo);
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }

    private void updateUi(Pill_obj objPill) {
        Detail.headerObj = objPill;
        Detail.imagesArray = objPill.getImages();
        Detail.id = objPill.getId();

        tv_title.setText(objPill.getName());
        tv_like.setText(objPill.getLike()+"");
        tv_comment.setText(objPill.getComment()+"");
        Detail.likestt = objPill.getLikeStt();
        if(objPill.getUsage()==null||objPill.getUsage().length()<0){
            objPill.setUsage("Không có dữ liệu");
        }
        if(objPill.getRecoment()==null||objPill.getRecoment().length()<0){
            objPill.setRecoment("Không có dữ liệu");
        }
        if(objPill.getInteractIn()==null||objPill.getInteractIn().length()<0){
            objPill.setInteractIn("Không có dữ liệu");
        }
        if(objPill.getInteraction()==null||objPill.getInteraction().length()<0){
            objPill.setInteraction("Không có dữ liệu");
        }
        if(objPill.getStorage()==null||objPill.getStorage().length()<0){
            objPill.setStorage("Không có dữ liệu");
        }

        tv_content.setText(Html.fromHtml(Common.context.getResources().getString(R.string.how_to_use_pill,
                objPill.getUsage(),
                objPill.getRecoment(),
                objPill.getInteractIn(),
                objPill.getInteraction(),
                objPill.getStorage())
        ));
        product_id = objPill.getId();
        link_share = objPill.getLinkShare();

        adapter = new Slide_Image_Adapter(Common.context,objPill.getImages());
        mPager = (ViewPager) v.findViewById(R.id.slide_image);
        CircleIndicator indicator = (CircleIndicator) v.findViewById(R.id.indicator);
        mPager.setAdapter(adapter);
        indicator.setViewPager(mPager);
        adapter.registerDataSetObserver(indicator.getDataSetObserver());
        adapter.notifyDataSetChanged();

        checkHearth(objPill.getLikeStt());

        LinearLayout insertPoint = (LinearLayout) v.findViewById(R.id.ln_star_pill);
        insertPoint.removeAllViews();
        int s = Integer.valueOf(objPill.getStar().intValue());
        LayoutInflater vi = (LayoutInflater) Common.context.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
        Log.d("STAR",s+"");
        if(s>0){
            for(int i = 0; i<s;i++){
                View star = vi.inflate(R.layout.star, null);
                insertPoint.addView(star, 0, new ViewGroup.LayoutParams(40, 40));
            }
        }else{
            View null_text = vi.inflate(R.layout.null_textview, null);

            insertPoint.addView(null_text, 0);
        }

        utils.showLoading(Common.context,10000,false);
        getOtherPrd(Detail.headerJson);

    }

    private void getOtherPrd(String jo) {
        ln = (LinearLayout)v.findViewById(R.id.ln_lq_pill);
        ln.removeAllViews();

            new AsyncTask<Void,Void,Void>(){

                @Override
                protected Void doInBackground(Void... voids) {
                    arrOther.clear();

                    JSONArray  other = null;
                    try {
                        JSONObject obj = new JSONObject(jo);
                        other = obj.getJSONArray(JsonConstant.OTHER_PRD);
                        for (int i = 0; i<other.length();i++){
                            JSONObject index = other.getJSONObject(i);
                            JSONObject product = index.getJSONObject(JsonConstant.PRODUCT);
                            JSONArray images = product.getJSONArray(JsonConstant.IMAGE);
                            Other_Product_Constuctor otherPrd = new Other_Product_Constuctor();
                            otherPrd.setName(product.getString(JsonConstant.NAME));
                            otherPrd.setCompany(product.getString(JsonConstant.COMPANY));
                            otherPrd.setId(product.getString(JsonConstant.ID));
                            otherPrd.setImage(images.getString(0));
                            if(product.has(JsonConstant.STAR)){
                                otherPrd.setStar(product.getDouble(JsonConstant.STAR));
                            }else{
                                otherPrd.setStar(0);
                            }

                            JSONObject price = product.getJSONObject(JsonConstant.PRICE);
                            if(price.getString(JsonConstant.MONEY).equals("")){
                                otherPrd.setPrice(0);
                            }else{
                                otherPrd.setPrice(price.getInt(JsonConstant.MONEY));
                            }

                            arrOther.add(otherPrd);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    LayoutInflater inflater2 = (LayoutInflater) Common.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                    for (int i = 0; i < arrOther.size(); i++){
                        Other_Product_Constuctor product = arrOther.get(i);
                        View rowView = inflater2.inflate(R.layout.item_pill_lq, null);
                        ImageView img_pill_lq = rowView.findViewById(R.id.img_pill_lq);
                        TextView tv_name_lq  = rowView.findViewById(R.id.name_pill_lq);
                        TextView tv_company_lq = rowView.findViewById(R.id.company_lq);
                        TextView price_lq = rowView.findViewById(R.id.price_pill_lq);
                        Double d = product.getStar();
                        int s = Integer.valueOf(d.intValue());
                        Picasso.get().load(ServerPath.ROOT_URL+product.getImage()).into(img_pill_lq);
                        price_lq.setText(Constant.format.format(product.getPrice())+"VND");
                        tv_name_lq.setText(product.getName());
                        tv_company_lq.setText(product.getCompany());
                        ln.addView(rowView);

                        int finalI = i;
                        rowView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Utils.setAlphalAnimation(v);
                     //           scroll.fullScroll(ScrollView.FOCUS_UP);
                                Detail.headerObj = null;
                                loadAgaint(arrOther.get(finalI).getId());
                                    }
                                });
                            }
                    super.onPostExecute(aVoid);
                }
            }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);


    }


    public void checkHearth(int likeStt){
        Log.d("LIKE_STT",likeStt+"");
        if(likeStt==0){
            hearth.setImageDrawable(Common.context.getResources().getDrawable(R.drawable.gray_hearth));
            like = false;
        }else{
            hearth.setImageDrawable(Common.context.getResources().getDrawable(R.drawable.red_heart));
            like = true;
        }
    }
}
