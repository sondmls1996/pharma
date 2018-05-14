package app.pharma.com.pharma.Fragment.Sick;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.Html;
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
import app.pharma.com.pharma.Model.Constructor.Sick_LQ_Construct;
import app.pharma.com.pharma.Model.JsonConstant;
import app.pharma.com.pharma.Model.ServerPath;
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
    Double star;
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
    private static final Integer[] IMAGES= {R.drawable.pharma_img,R.drawable.img_dr,R.drawable.img_sick};
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
        ln = (LinearLayout)v.findViewById(R.id.ln_lq_pill);
        hearth = (ImageView)v.findViewById(R.id.img_hearth);
        tv_title = v.findViewById(R.id.tv_title_sick);
        content = v.findViewById(R.id.tv_content_sick);
        tv_like = v.findViewById(R.id.txt_like);
        ln_star = v.findViewById(R.id.ln_star_sick);
        ln_star.removeAllViews();
        comment = v.findViewById(R.id.txt_comment);
        arrSickLq = new ArrayList<>();
        share = v.findViewById(R.id.img_share);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!share.equals("")){
                    Intent shareit = new Intent(Intent.ACTION_SEND);
                    shareit.setType("text/plain");
                    String shareBody = link_share;

                    shareit.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                    startActivity(Intent.createChooser(shareit, "Chia sẻ qua"));
                }
            }
        });
        hearth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkHearth();
            }
        });
//        for(int i=0;i<IMAGES.length;i++)
//            ImagesArray.add(IMAGES[i]);
        adapter = new Slide_Image_Adapter(Common.context,ImagesArray);

        mPager = (ViewPager) v.findViewById(R.id.slide_image);
        CircleIndicator indicator = (CircleIndicator) v.findViewById(R.id.indicator);
        mPager.setAdapter(adapter);
        indicator.setViewPager(mPager);
        adapter.registerDataSetObserver(indicator.getDataSetObserver());
        getData();



    }

    private void getData() {
        ImagesArray.clear();
        Map<String,String> map = new HashMap<>();
        map.put("id", Detail.id);
        Response.Listener<String> response = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jo = new JSONObject(response);
                    JSONObject Dise = jo.getJSONObject(JsonConstant.DISEASE);
                    tv_title.setText(Dise.getString(JsonConstant.NAME));
                    tv_like.setText(Dise.getString(JsonConstant.LIKE));
                    star = Dise.getDouble(JsonConstant.STAR);
//                    comment.setText(Dise.getString(JsonConstant.COMMENT));
                    link_share = Dise.getString(JsonConstant.LINK_SHARE);
                    likestt = Dise.getInt(JsonConstant.LIKE_STT);
                    JSONArray images = Dise.getJSONArray(JsonConstant.IMAGE);
                    for (int j = 0; j<images.length();j++){
                        ImagesArray.add(images.getString(j));
                    }
                    if(likestt==0){
                        like = false;
                        checkHearth();

                    }else{
                        like = true;
                        checkHearth();

                    }
                    int s = Integer.valueOf(star.intValue());
                    LayoutInflater vi = (LayoutInflater) Common.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

// insert into main view
                    for(int i = 0; i<s;i++){
                        View star = vi.inflate(R.layout.star, null);

                        ln_star.addView(star, 0, new ViewGroup.LayoutParams(30, 30));
                    }
                    content.setText(Html.fromHtml(getResources().getString(R.string.how_to_use_sick,Dise.getString(JsonConstant.DESCRI),"")));
                    adapter.notifyDataSetChanged();
                    getSickOther(jo);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };
        Utils.PostServer(getActivity(), ServerPath.DETAIL_SICK,map,response);
    }

    private void getSickOther(JSONObject jo) {
        try {
            arrSickLq.clear();
            JSONArray other = jo.getJSONArray(JsonConstant.OTHER_DISEA);
            for (int i = 0; i<other.length();i++){
                JSONObject index = other.getJSONObject(i);
                JSONObject product = index.getJSONObject(JsonConstant.DISEASE);
                Sick_LQ_Construct otherPrd = new Sick_LQ_Construct();
                otherPrd.setTitle(product.getString(JsonConstant.NAME));
                otherPrd.setId(product.getString(JsonConstant.ID));
                otherPrd.setImage(product.getString(JsonConstant.IMAGE));
                otherPrd.setDecri(product.getString(JsonConstant.DESCRI));
                JSONObject catalo = product.getJSONObject(JsonConstant.CATEGORY_LOW);
                otherPrd.setCatalo(catalo.getString(JsonConstant.CATEGORY_LOW));
                arrSickLq.add(otherPrd);
            }

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
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void checkHearth(){
        if(like){
            hearth.setImageDrawable(Common.context.getResources().getDrawable(R.drawable.red_heart));
            like = false;
        }else{
            hearth.setImageDrawable(Common.context.getResources().getDrawable(R.drawable.gray_hearth));
            like = true;
        }
    }




}
