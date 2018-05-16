package app.pharma.com.pharma.Fragment.Pharma;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import app.pharma.com.pharma.Adapter.List_Rate_Adapter;
import app.pharma.com.pharma.Model.Common;
import app.pharma.com.pharma.Model.Constructor.Rating_Obj;
import app.pharma.com.pharma.Model.JsonConstant;
import app.pharma.com.pharma.Model.ServerPath;
import app.pharma.com.pharma.Model.Utils;
import app.pharma.com.pharma.R;
import app.pharma.com.pharma.activity.Detail.Detail;

/**
 * A simple {@link Fragment} subclass.
 */
public class Pharma_Detail_Rate extends Fragment {

    ListView lv_rate;
    ArrayList<View> arrView;
    LinearLayout ln_rate_now;
    int page = 1;
    List_Rate_Adapter rateAdapter;
    ArrayList<Rating_Obj> arrRate;
    View v;
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
        arrRate = new ArrayList<>();

        lv_rate = v.findViewById(R.id.lv_list_rate);
        rateAdapter = new List_Rate_Adapter(getActivity(),R.layout.item_rate,arrRate);
        lv_rate.setAdapter(rateAdapter);

        ln_rate_now = v.findViewById(R.id.ln_rate_now);
        rl_top = v.findViewById(R.id.rl_rate_top);
        arrView = new ArrayList<>();

        tv_de_comment = v.findViewById(R.id.txt_de_comment);
        ln_rate_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogRate();
            }
        });
        inflater2 = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        setRelativeTop(Detail.key);

    }

    private void setRelativeTop(String key) {

        if(key.equals("pharma")){
            View rowView = inflater2.inflate(R.layout.rate_pharma_include, null);
            rl_top.addView(rowView);
            getListRate(page,"store","idStore");
        }else if(key.equals("pill")){
            View rowView = inflater2.inflate(R.layout.rate_pill_include, null);
            rl_top.addView(rowView);
            getListRate(page,"product","idProduct");
        }else if(key.equals("sick")){
            View rowView = inflater2.inflate(R.layout.rate_sick_include, null);
            rl_top.addView(rowView);
            getListRate(page,"disease","idDisease");

        }


    }

    private void getListRate(int page,String type,String keyId) {
        Map<String,String> map = new HashMap<>();
        map.put("page",page+"");
        map.put("type",type);
        map.put(keyId,Detail.id);
        Response.Listener<String> response = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
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
                                        if(listRate.length()>0){
                                            for (int i = 0; i<listRate.length();i++){
                                                JSONObject idx = listRate.getJSONObject(i);
                                                JSONObject Rating = idx.getJSONObject(JsonConstant.RATING);
                                                Rating_Obj rate = new Rating_Obj();
                                                rate.setShortComment(Rating.getString(JsonConstant.SHORT_CMT));
                                                rate.setComment(Rating.getString(JsonConstant.COMMENT));
                                                rate.setStar(Rating.getDouble(JsonConstant.STAR));
                                                rate.setTime(Rating.getLong(JsonConstant.TIME));
                                                arrRate.add(rate);

                                            }
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    return null;
                                }

                                @Override
                                protected void onPostExecute(Void aVoid) {
                                    switch (type){
                                        case "store":
                                            tv_de_comment.setText(getActivity().getResources().getString(R.string.people_cmt,
                                                    arrRate.size()+"","nhà thuốc"));
                                            break;
                                        case "product":
                                            tv_de_comment.setText(getActivity().getResources().getString(R.string.people_cmt,
                                                    arrRate.size()+"","sản phẩm"));
                                            break;
                                        case "disease":
                                            tv_de_comment.setText(getActivity().getResources().getString(R.string.people_cmt,
                                                    arrRate.size()+"","bài viết"));
                                            break;
                                    }

                                    rateAdapter.notifyDataSetChanged();
                                    super.onPostExecute(aVoid);
                                }
                            }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                            break;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d("RESPONSE_RATE_PILL",response);
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
        TextView title = dialog.findViewById(R.id.txt_title_rate);
        if(Detail.key.equals("pill")){
            title.setText(getResources().getString(R.string.title_rate_pill));
        }else if(Detail.key.equals("pharma")){
            title.setText(getResources().getString(R.string.title_rate_pharma));
        }else if(Detail.key.equals("sick")){
            title.setText(getResources().getString(R.string.title_rate_sick));
        }
        dialog.show();
    }

}
