package app.pharma.com.pharma.Fragment.Pill;


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
import app.pharma.com.pharma.Model.Constructor.Other_Product_Constuctor;
import app.pharma.com.pharma.Model.Constructor.Object.Pill_obj;
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
    ImageView hearth;
    int page = 1;
    boolean like = false;
    View v;
    Pill_obj objPill;
    String product_id;
    User user;
    DatabaseHandle db;
    String link_share = "";
    Double star_count;
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

        init();

        return v;
    }

    private void init() {
        if(Utils.isLogin()){
            db = new DatabaseHandle();
            user = db.getAllUserInfor();
        }

        ln = (LinearLayout)v.findViewById(R.id.ln_lq_pill);
        ln.removeAllViews();
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
                Intent it = new Intent(Common.context, Order.class);
                startActivity(it);
            }
        });
        hearth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.setAlphalAnimation(v);
                onClickHeart();
            }
        });



        loadData();

    }

    private void onClickHeart() {
        if(Utils.isLogin()){
            int likestt = objPill.getLikeStt();

            Map<String, String> map = new HashMap<>();
            map.put("type","product");
            map.put("id",product_id);
            map.put("accessToken",user.getToken());
            if(likestt==0){
                map.put("likeStatus","1");
            }else{
                map.put("likeStatus","0");
            }

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

                            }else{
                                objPill.setLikeStt(0);
                            }

                            checkHearth(objPill.getLikeStt());
                        }else{

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };
            Utils.PostServer(getActivity(),ServerPath.LIKE_PILL,map,response);
        }else{
            Utils.dialogNotif(getActivity().getResources().getString(R.string.you_not_login));
        }

    }

    private void loadData() {
        Map<String,String> map = new HashMap<>();
        map.put("id", Detail.id);
        Response.Listener<String> response = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("RESPONSE_DETAIL",response);
                    new AsyncTask<Void,Void,JSONObject>(){

                        @Override
                        protected JSONObject doInBackground(Void... voids) {

                            Detail.headerJson = response;
                            JSONObject jo = null;
                            try {
                                jo = new JSONObject(response);
                                String code = jo.getString(JsonConstant.CODE);
                                switch (code){
                                    case "0":
                                        JSONObject data = jo.getJSONObject(JsonConstant.DATA);
                                        JSONObject product = data.getJSONObject(JsonConstant.PRODUCT);
                                        JSONObject joPrice = product.getJSONObject(JsonConstant.PRICE);
                                        JSONArray images = product.getJSONArray(JsonConstant.IMAGE);

                                        objPill = new Pill_obj();
                                        objPill.setId(product.getString(JsonConstant.ID));
                                        objPill.setName(product.getString(JsonConstant.NAME));
                                        objPill.setLinkShare(product.getString(JsonConstant.LINK_SHARE));
                                        objPill.setPrice(joPrice.getInt(JsonConstant.MONEY));
                                        objPill.setUsage(product.getString(JsonConstant.USAGE));
                                        objPill.setRecoment(product.getString(JsonConstant.RECOMENT));
                                        objPill.setInteractIn(product.getString(JsonConstant.INGREINFO));
                                        objPill.setStorage( product.getString(JsonConstant.STORAGE));
                                        for (int j = 0; j<images.length();j++){
                                            ImagesArray.add(images.getString(j));
                                        }
                                        objPill.setImages(ImagesArray);
                                        objPill.setLike(product.getInt(JsonConstant.LIKE));
                                        objPill.setComment(product.getInt(JsonConstant.COMMENT));
                                        objPill.setStar(product.getDouble(JsonConstant.STAR));
                                        objPill.setLikeStt(product.getInt(JsonConstant.LIKE_STT));

                                        break;
                                    default:
                                        break;
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            return jo;
                        }

                        @Override
                        protected void onPostExecute(JSONObject jo) {
                            Detail.headerObj = objPill;
                            tv_title.setText(objPill.getName());
                            tv_like.setText(objPill.getLike()+"");
                            tv_comment.setText(objPill.getComment()+"");
                            tv_content.setText(Html.fromHtml(Common.context.getResources().getString(R.string.how_to_use_pill,
                                    objPill.getUsage(),
                                    objPill.getRecoment(),
                                    objPill.getInteractIn(),
                                    objPill.getStorage())
                            ));
                            product_id = objPill.getId();
                            link_share = objPill.getLinkShare();


                            adapter = new Slide_Image_Adapter(Common.context,ImagesArray);
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
                            for(int i = 0; i<s;i++){
                                View star = vi.inflate(R.layout.star, null);
                                insertPoint.addView(star, 0, new ViewGroup.LayoutParams(40, 40));
                            }
                            getOtherPrd(jo);
                            super.onPostExecute(jo);
                        }
                    }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        };

        Utils.PostServer(getActivity(), ServerPath.DETAIL_PILL,map,response);
    }

    private void getOtherPrd(JSONObject jo) {

            new AsyncTask<Void,Void,Void>(){

                @Override
                protected Void doInBackground(Void... voids) {
                    arrOther.clear();
                    JSONArray  other = null;
                    try {
                        other = jo.getJSONArray(JsonConstant.OTHER_PRD);
                        for (int i = 0; i<other.length();i++){
                            JSONObject index = other.getJSONObject(i);
                            JSONObject product = index.getJSONObject(JsonConstant.PRODUCT);
                            Other_Product_Constuctor otherPrd = new Other_Product_Constuctor();
                            otherPrd.setName(product.getString(JsonConstant.NAME));
                            otherPrd.setCompany(product.getString(JsonConstant.COMPANY));
                            otherPrd.setId(product.getString(JsonConstant.ID));
                            otherPrd.setImage(product.getString(JsonConstant.AVATAR));
                            otherPrd.setStar(product.getDouble(JsonConstant.STAR));
                            JSONObject price = product.getJSONObject(JsonConstant.PRICE);
                            otherPrd.setPrice(price.getInt(JsonConstant.MONEY));
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
                        Picasso.with(getActivity()).load(ServerPath.ROOT_URL+product.getImage()).into(img_pill_lq);
                        price_lq.setText(Constant.format.format(product.getPrice()));
                        tv_name_lq.setText(product.getName());
                        tv_company_lq.setText(product.getCompany());
                        ln.addView(rowView);
                    }
                    super.onPostExecute(aVoid);
                }
            }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);


    }


    public void checkHearth(int likeStt){
        if(likeStt==0){
            hearth.setImageDrawable(Common.context.getResources().getDrawable(R.drawable.gray_hearth));
            like = false;
        }else{
            hearth.setImageDrawable(Common.context.getResources().getDrawable(R.drawable.red_heart));
            like = true;
        }
    }
}
