package app.pharma.com.pharma.Fragment.Pill;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.squareup.picasso.Downloader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import app.pharma.com.pharma.Adapter.Slide_Image_Adapter;
import app.pharma.com.pharma.Model.Common;
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
    boolean like = false;
    View v;
    String product_id;
    double star_count = 0;
    Slide_Image_Adapter adapter;
    TextView tv_title;
    LinearLayout ln_star;
    TextView tv_like,tv_comment;
    TextView tv_content;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private static Integer[] IMAGES;
    private ArrayList<String> ImagesArray = new ArrayList<>();
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

        ln = (LinearLayout)v.findViewById(R.id.ln_lq_pill);
        hearth = (ImageView)v.findViewById(R.id.img_hearth);
        ln_buy = v.findViewById(R.id.ln_buynow);
        tv_title = v.findViewById(R.id.title_detail_pill);
        tv_comment = v.findViewById(R.id.txt_comment);
        tv_like = v.findViewById(R.id.txt_like);
        tv_content = v.findViewById(R.id.tv_content);

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
                checkHearth();
            }
        });

        adapter = new Slide_Image_Adapter(Common.context,ImagesArray);

        mPager = (ViewPager) v.findViewById(R.id.slide_image);
        CircleIndicator indicator = (CircleIndicator) v.findViewById(R.id.indicator);
        mPager.setAdapter(adapter);
        indicator.setViewPager(mPager);
        adapter.registerDataSetObserver(indicator.getDataSetObserver());

        LayoutInflater inflater2 = (LayoutInflater) Common.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        for (int i = 0; i < 5; i++){
            final View rowView = inflater2.inflate(R.layout.item_pill_lq, null);
            ln.addView(rowView);

        }

        loadData();

    }

    private void loadData() {
        Map<String,String> map = new HashMap<>();
        map.put("id", Detail.id);
        Response.Listener<String> response = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jo = new JSONObject(response);
                    JSONObject product = jo.getJSONObject(JsonConstant.PRODUCT);
                    tv_title.setText(product.getString(JsonConstant.NAME));
                    product_id = product.getString(JsonConstant.ID);
                    JSONObject joPrice = product.getJSONObject(JsonConstant.PRICE);
                    tv_content.setText(getActivity().getResources().getString(R.string.how_to_use_pill,
                            product.getString(JsonConstant.USAGE),
                            product.getString(JsonConstant.RECOMENT),
                            product.getString(JsonConstant.INGREINFO),
                            product.getString(JsonConstant.STORAGE)
                    ));
                    JSONArray images = product.getJSONArray(JsonConstant.IMAGE);
                    for (int j = 0; j<images.length();j++){
                        ImagesArray.add(images.getString(j));
                    }
                    tv_like.setText(product.getString(JsonConstant.LIKE));
                    tv_comment.setText(product.getString(JsonConstant.COMMENT));
                    star_count = product.getDouble(JsonConstant.STAR);

                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        Utils.PostServer(getActivity(), ServerPath.DETAIL_PILL,map,response);
    }


    public void checkHearth(){
        if(like){
            hearth.setImageDrawable(Common.context.getResources().getDrawable(R.drawable.gray_hearth));
            like = false;
        }else{
            hearth.setImageDrawable(Common.context.getResources().getDrawable(R.drawable.red_heart));
            like = true;
        }
    }
}
