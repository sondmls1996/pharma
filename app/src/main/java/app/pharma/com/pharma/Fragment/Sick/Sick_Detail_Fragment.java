package app.pharma.com.pharma.Fragment.Sick;

import android.app.Dialog;
import android.content.Context;
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
import app.pharma.com.pharma.Model.Constructor.Object.Sick_Obj;
import app.pharma.com.pharma.Model.Constructor.Sick_LQ_Construct;
import app.pharma.com.pharma.Model.Database.DatabaseHandle;
import app.pharma.com.pharma.Model.Database.User;
import app.pharma.com.pharma.Model.JsonConstant;
import app.pharma.com.pharma.Model.ServerPath;
import app.pharma.com.pharma.Model.Sick_Construct;
import app.pharma.com.pharma.Model.Utils;
import app.pharma.com.pharma.R;
import app.pharma.com.pharma.activity.Detail.Detail;
import me.relex.circleindicator.CircleIndicator;


public class Sick_Detail_Fragment extends Fragment {
    View v;
    LinearLayout ln;
    private  ViewPager mPager;
    ImageView hearth;
    boolean like = false;
    TextView tv_title;
    TextView tv_like;
    Sick_Obj sickObj;
    Double star;
    DatabaseHandle db;
    Utils utils;
    User user;
    LinearLayout ln_star;
    TextView comment;
    ArrayList<Sick_LQ_Construct> arrSickLq;
    int likestt = 0;
    String link_share = "";
    TextView content;
    ImageView share;
    Slide_Image_Adapter adapter;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;

    private ArrayList<String> ImagesArray = new ArrayList<String>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_sick__detail_, container, false);
        init();
        return v;
    }

    private void init() {
        if(Utils.isLogin()){
            db = new DatabaseHandle();
            user = db.getAllUserInfor();
        }
        utils = new Utils();
        ln = (LinearLayout)v.findViewById(R.id.ln_lq_pill);
        hearth = (ImageView)v.findViewById(R.id.img_hearth);
        tv_title = v.findViewById(R.id.tv_title_sick);
        content = v.findViewById(R.id.tv_content_sick);
        tv_like = v.findViewById(R.id.txt_like);

        comment = v.findViewById(R.id.txt_comment);
        arrSickLq = new ArrayList<>();
        share = v.findViewById(R.id.img_share);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.setAlphalAnimation(view);
                if(!share.equals("")){
                    Utils.shareLink(link_share);
                }
            }
        });
        hearth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.setAlphalAnimation(v);
                onClickHeart();
//                checkHearth();
            }
        });
//        for(int i=0;i<IMAGES.length;i++)
//            ImagesArray.add(IMAGES[i]);

        getData(Detail.id);

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
                Detail.likestt = sickObj.getLike_stt();

                Map<String, String> map = new HashMap<>();
                map.put("type","disease");
                map.put("id",sickObj.getId());
                map.put("accessToken",user.getToken());
                if(Detail.likestt==0){
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
                                if(Detail.likestt==0){
                                    Detail.likestt = 1;
                                    sickObj.setLike_stt(1);
                                    sickObj.setLike(sickObj.getLike()+1);
                                 //   tv_like.setText(sickObj.getLike());
                                }else{
                                    Detail.likestt = 0;
                                    sickObj.setLike_stt(0);
                                    sickObj.setLike(sickObj.getLike()-1);
                               //+     tv_like.setText(sickObj.getLike()+"");
                                }

                                checkHearth(sickObj.getLike_stt());
                                Detail.headerObj = sickObj;
                            }else{

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                Utils.PostServer(Common.context,ServerPath.LIKE_PILL,map,response);
            }else{
                Utils.dialogNotif(getActivity().getResources().getString(R.string.you_not_login));
            }

        }


    }

    public void loadAgaint(String id){
        getData(id);
    }

    private void getData(String id) {
        ImagesArray.clear();
        if(Detail.headerObj !=null){
            sickObj = (Sick_Obj)Detail.headerObj;
            updateUi(sickObj);
        }else{
            if(!Utils.isNetworkEnable(getActivity())){
                Utils.ShowNotifString(getActivity().getResources().getString(R.string.no_internet),
                        new Utils.ShowDialogNotif.OnCloseDialogNotif() {
                            @Override
                            public void onClose(Dialog dialog) {
                                dialog.dismiss();
                                getActivity().finish();
                            }
                        });
            }else{
                Map<String,String> map = new HashMap<>();
                map.put("id", id);
                if(Utils.isLogin()){
                    map.put("accessToken",user.getToken());
                }
                Response.Listener<String> response = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("RESPONSE_SICK_DETAIL",response);
                        utils.showLoading(Common.context,10000,true);
                        Detail.headerJson = response;
                        initJson(response);

                    }
                };
                Utils.PostServer(Common.context, ServerPath.DETAIL_SICK,map,response);
            }
        }


    }

    private void initJson(String response) {
        try {

            JSONObject jo = new JSONObject(response);
            String code = jo.getString(JsonConstant.CODE);
            switch (code){
                case "0":
                    new AsyncTask<Void,Void,JSONObject>(){

                        @Override
                        protected JSONObject doInBackground(Void... voids) {

                            try {
                                JSONObject data = jo.getJSONObject(JsonConstant.DATA);

                                JSONObject Dise = data.getJSONObject(JsonConstant.DISEASE);
                                JSONArray images = Dise.getJSONArray(JsonConstant.IMAGE);
                                sickObj = new Sick_Obj();
                                sickObj.setName(Dise.getString(JsonConstant.NAME));
                                if(Dise.has(JsonConstant.DESCRI)){
                                    sickObj.setDescri(Dise.getString(JsonConstant.DESCRI));
                                }
                                if(Dise.has(JsonConstant.DEFINE)){
                                    sickObj.setDefine(Dise.getString(JsonConstant.DEFINE));
                                }

                                sickObj.setLike(Dise.getInt(JsonConstant.LIKE));
                                sickObj.setCmt(Dise.getInt(JsonConstant.COMMENT));
                                if(Dise.has(JsonConstant.STAR)){
                                    sickObj.setStar(Dise.getDouble(JsonConstant.STAR));
                                }else{
                                    sickObj.setStar(0.0);
                                }

                                sickObj.setLink_share(Dise.getString(JsonConstant.LINK_SHARE));
                                sickObj.setLike_stt(Dise.getInt(JsonConstant.LIKE_STT));
                                sickObj.setId(Dise.getString(JsonConstant.ID));

                                for (int j = 0; j<images.length();j++){
                                    if(!images.getString(j).equals("")){
                                        ImagesArray.add(images.getString(j));
                                    }

                                }
                                sickObj.setImages(ImagesArray);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                            return jo;
                        }

                        @Override
                        protected void onPostExecute(JSONObject jo) {
                            Detail.headerJson = jo.toString();
                            updateUi(sickObj);



                            super.onPostExecute(jo);
                        }
                    }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                    break;

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void updateUi(Sick_Obj sickObj) {
        ln_star = v.findViewById(R.id.ln_star_sick);
        ln_star.removeAllViews();

        Detail.headerObj = sickObj;
        Detail.id = sickObj.getId();
        tv_title.setText(sickObj.getName());
        tv_like.setText(sickObj.getLike()+"");
        comment.setText(sickObj.getCmt()+"");
        link_share = sickObj.getLink_share();
        checkHearth(sickObj.getLike_stt());

        adapter = new Slide_Image_Adapter(Common.context,sickObj.getImages());

        mPager = (ViewPager) v.findViewById(R.id.slide_image);
        CircleIndicator indicator = (CircleIndicator) v.findViewById(R.id.indicator);
        mPager.setAdapter(adapter);
        indicator.setViewPager(mPager);
        adapter.registerDataSetObserver(indicator.getDataSetObserver());
        adapter.notifyDataSetChanged();

        int s = Integer.valueOf(sickObj.getStar().intValue());
        LayoutInflater vi = (LayoutInflater) Common.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
// insert into main view
        if(s!=0){
            for(int i = 0; i<s;i++){
                View star = vi.inflate(R.layout.star, null);

                ln_star.addView(star, 0,
                        new ViewGroup.LayoutParams(40, 40));
            }
        }else{
            View null_text = vi.inflate(R.layout.null_textview, null);

            ln_star.addView(null_text, 0);
        }
        if(sickObj.getDescri()==null||sickObj.getDescri().length()<0){
            sickObj.setDescri("Không có mô tả");
        }
        if(sickObj.getDefine()==null||sickObj.getDefine().length()<0){
            sickObj.setDefine("Không có mô tả");
        }
        content.setText(Html.fromHtml(getResources().getString(R.string.how_to_use_sick,
                sickObj.getDescri(),
                sickObj.getDefine())));
        utils.showLoading(Common.context,10000,false);
        getSickOther(Detail.headerJson);

    }

    private void getSickOther(String jo) {

            arrSickLq.clear();
            ln.removeAllViews();

            new AsyncTask<Void,Void,Void>(){

                @Override
                protected Void doInBackground(Void... voids) {
                    JSONArray other = null;
                    try {
                        JSONObject obj = new JSONObject(jo);
                        other = obj.getJSONArray(JsonConstant.OTHER_DISEA);
                        for (int i = 0; i<other.length();i++){
                            JSONObject index = other.getJSONObject(i);
                            JSONObject product = index.getJSONObject(JsonConstant.DISEASE);
                            JSONArray images = product.getJSONArray(JsonConstant.IMAGE);
                            Sick_LQ_Construct otherPrd = new Sick_LQ_Construct();
                            otherPrd.setTitle(product.getString(JsonConstant.NAME));
                            otherPrd.setId(product.getString(JsonConstant.ID));

                            otherPrd.setImage(images.getString(0));
                            otherPrd.setDecri(product.getString(JsonConstant.DEFINE));
                            JSONObject catalo = product.getJSONObject(JsonConstant.CATEGORY_LOW);
                            otherPrd.setCatalo(catalo.getString(JsonConstant.CATEGORY_LOW));
                            arrSickLq.add(otherPrd);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    LayoutInflater inflater2 = (LayoutInflater) Common.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                    for (int i = 0; i < arrSickLq.size(); i++){
                        Sick_LQ_Construct product = arrSickLq.get(i);
                        View rowView = inflater2.inflate(R.layout.item_sick_lq, null);
                        ImageView img_pill_lq = rowView.findViewById(R.id.img_sick_lq);
                        TextView tv_name_lq  = rowView.findViewById(R.id.tv_sick_lq);
                        TextView tv_descri = rowView.findViewById(R.id.tv_content_lq);
                        TextView cata_lq= rowView.findViewById(R.id.tv_cata_sick_lq);
                        Picasso.with(getActivity()).load(ServerPath.ROOT_URL+arrSickLq.get(i).getImage()).into(img_pill_lq);
                        tv_name_lq.setText(arrSickLq.get(i).getTitle());
                        tv_descri.setText(arrSickLq.get(i).getDecri());
                        cata_lq.setText(arrSickLq.get(i).getCatalo());
                        ln.addView(rowView);

                        int finalI = i;
                        rowView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Utils.setAlphalAnimation(v);
                                //           scroll.fullScroll(ScrollView.FOCUS_UP);
                                Detail.headerObj =null;
                                loadAgaint(arrSickLq.get(finalI).getId());
                            }
                        });
                    }
                    super.onPostExecute(aVoid);
                }
            }.execute();

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
