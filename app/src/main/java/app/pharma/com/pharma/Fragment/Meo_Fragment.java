package app.pharma.com.pharma.Fragment;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import app.pharma.com.pharma.Support.EndlessScroll;
import app.pharma.com.pharma.Support.RecyclerItemClickListener;
import app.pharma.com.pharma.activity.Detail.Infor_Meo;

import static io.realm.internal.SyncObjectServerFacade.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 */
public class Meo_Fragment extends Fragment implements View.OnClickListener {
    RecyclerView lv;
    List_Meo_Adapter adapter;
    ArrayList<Meo_Constructor> arr;
    SwipeRefreshLayout swip;
    int lastVisibleItem = 0;
    private int lastY = 0;
    int Mainpage = 1;
    TextView tvNull;

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
        swip = v.findViewById(R.id.swip);
        swip.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Mainpage = 1;
             getData(Mainpage);
            }
        });
        tvNull = v.findViewById(R.id.txt_null);
        tv_focus.setOnClickListener(this);
        tv_hearth.setOnClickListener(this);
        tv_meo.setOnClickListener(this);

        arr = new ArrayList<>();
        setRecycle(v);

        tv_focus.performClick();
        getData(Mainpage);
    }
    public void setRecycle(View v){
        lv = v.findViewById(R.id.lv_meo);
        lv.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        lv.setLayoutManager(layoutManager);
        adapter = new List_Meo_Adapter(getActivity(), arr);
        lv.setAdapter(adapter);
        EndlessScroll endlessScroll = new EndlessScroll(layoutManager,getApplicationContext()) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                Mainpage ++;
                getData(Mainpage);
            }
        };
        lv.addOnScrollListener(endlessScroll);
        lv.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Utils.setAlphalAnimation(view);
                        Intent it = new Intent(Common.context, Infor_Meo.class);
                        it.putExtra("link",arr.get(position).getLink());
                        startActivity(it);
                        // TODO Handle item click
                    }
                })
        );
    }
    private void getData(int page) {
        Log.d("PAGE_MEO",page+"");
        if(page==1){
            arr.clear();
        }
        final boolean[] isEmpty = {false};
        Map<String,String> map = new HashMap<>();
        map.put("page",page+"");
        Response.Listener<String> response = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    swip.setRefreshing(false);
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
                                        if(tipnote.length()>0){
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
                                            isEmpty[0] = false;
                                        }else{
                                            isEmpty[0] = true;
                                        }


                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        return null;
                                    }

                                    return null;
                                }

                                @Override
                                protected void onPostExecute(Void aVoid) {
                                    if(isEmpty[0]&&Mainpage>1){
                                        Mainpage = Mainpage -1;
                                    }

                                    if(arr.size()>0){
                                        tvNull.setVisibility(View.GONE);
                                        lv.setVisibility(View.VISIBLE);
                                    }else{
                                        tvNull.setVisibility(View.VISIBLE);
                                        lv.setVisibility(View.GONE);
                                    }
                                    adapter.notifyDataSetChanged();
                                    super.onPostExecute(aVoid);
                                }
                            }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);


                            break;
                    }
                } catch (JSONException e) {
                    swip.setRefreshing(false);
                    Utils.dialogNotif(getActivity().getResources().getString(R.string.server_err));
                    tvNull.setVisibility(View.VISIBLE);
                    lv.setVisibility(View.GONE);
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
