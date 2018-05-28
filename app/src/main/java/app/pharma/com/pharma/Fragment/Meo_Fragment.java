package app.pharma.com.pharma.Fragment;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import app.pharma.com.pharma.Adapter.List_Meo_Adapter;
import app.pharma.com.pharma.Model.Common;
import app.pharma.com.pharma.Model.Constant;
import app.pharma.com.pharma.Model.Constructor.Meo_Constructor;
import app.pharma.com.pharma.Model.JsonConstant;
import app.pharma.com.pharma.Model.ServerPath;
import app.pharma.com.pharma.Model.Utils;
import app.pharma.com.pharma.R;
import app.pharma.com.pharma.activity.Detail.Infor_Meo;

/**
 * A simple {@link Fragment} subclass.
 */
public class Meo_Fragment extends Fragment implements View.OnClickListener {
    ListView lv;
    List_Meo_Adapter adapter;
    ArrayList<Meo_Constructor> arr;
    int lastVisibleItem = 0;
    private int lastY = 0;
    int page = 1;
    TextView tv_focus;
    TextView tv_hearth;
    TextView tv_meo;
    View v;
    public Meo_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

         v = inflater.inflate(R.layout.fragment_meo, container, false);
        initView();

        return v;
    }

    private void initView() {
        tv_focus = v.findViewById(R.id.tv_focus);
        tv_hearth = v.findViewById(R.id.tv_hearth);
        tv_meo = v.findViewById(R.id.tv_meo);

        tv_focus.setOnClickListener(this);
        tv_hearth.setOnClickListener(this);
        tv_meo.setOnClickListener(this);

        lv = (ListView)v.findViewById(R.id.lv_meo);
        arr = new ArrayList<>();
        adapter = new List_Meo_Adapter(getContext(),0,arr);
        lv.setAdapter(adapter);




        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent it = new Intent(Common.context, Infor_Meo.class);
                startActivity(it);
            }
        });
        tv_focus.performClick();
        getData(page);
    }

    private void getData(int page) {
        if(page==1){
            arr.clear();
        }
        Map<String,String> map = new HashMap<>();
        map.put("page",page+"");
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

                                    try {
                                        JSONObject jo = new JSONObject(response);
                                        JSONArray tipnote = jo.getJSONArray(JsonConstant.TIPNOTE);
                                        for (int i =0; i<tipnote.length();i++){
                                            JSONObject idx =tipnote.getJSONObject(i);
                                            JSONObject notice = idx.getJSONObject(JsonConstant.NOTICE);

                                            Meo_Constructor meo = new Meo_Constructor();
                                            meo.setTitle(notice.getString(JsonConstant.TITLE));
                                            meo.setImage(notice.getString(JsonConstant.IMAGE));
                                            meo.setId(notice.getString(JsonConstant.ID));
                                         //   meo.setComment(notice.getString(JsonConstant.COMMENT));
                                            meo.setDate(notice.getLong(JsonConstant.TIME));
                                            meo.setDescrep(notice.getString(JsonConstant.INTRODUCT));
                                            meo.setLink(notice.getString(JsonConstant.LINKVIEW));
                                            arr.add(meo);
                                        }



                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    return null;
                                }

                                @Override
                                protected void onPostExecute(Void aVoid) {
                                    adapter.notifyDataSetChanged();
                                    super.onPostExecute(aVoid);
                                }
                            }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);


                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d("RESPONSE_MEO",response);
            }
        };
        Utils.PostServer(getActivity(), ServerPath.LIST_MEO,map,response);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_focus:
                changeColor(tv_focus);
                break;
            case R.id.tv_hearth:
                changeColor(tv_hearth);
                break;
            case R.id.tv_meo:
                changeColor(tv_meo);
                break;
        }
    }

    private void changeColor(TextView tv) {
        tv_focus.setTextColor(Constant.resources.getColor(R.color.gray));
        tv_hearth.setTextColor(Constant.resources.getColor(R.color.gray));
        tv_meo.setTextColor(Constant.resources.getColor(R.color.gray));

        tv_focus.setBackgroundColor(Constant.resources.getColor(R.color.light_gray));
        tv_hearth.setBackgroundColor(Constant.resources.getColor(R.color.light_gray));
        tv_meo.setBackgroundColor(Constant.resources.getColor(R.color.light_gray));

        if (tv==tv_focus){
            tv_focus.setTextColor(Constant.resources.getColor(R.color.blue));
            tv_focus.setBackgroundColor(Constant.resources.getColor(R.color.white));
         //   lv.setVisibility(View.GONE);
        }else if(tv==tv_hearth){
            tv_hearth.setTextColor(Constant.resources.getColor(R.color.blue));
            tv_hearth.setBackgroundColor(Constant.resources.getColor(R.color.white));
        //    lv.setVisibility(View.VISIBLE);
        }else{
            tv_meo.setTextColor(Constant.resources.getColor(R.color.blue));
            tv_meo.setBackgroundColor(Constant.resources.getColor(R.color.white));
        }
    }
}
