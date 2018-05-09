package app.pharma.com.pharma.Fragment.Dr;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.android.volley.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.pharma.com.pharma.Adapter.List_Dr_Adapter;
import app.pharma.com.pharma.Model.CataloModel;
import app.pharma.com.pharma.Model.Common;
import app.pharma.com.pharma.Model.Constant;
import app.pharma.com.pharma.Model.Database.Catalo;
import app.pharma.com.pharma.Model.Database.DatabaseHandle;
import app.pharma.com.pharma.Model.Constructor.Dr_Constructor;
import app.pharma.com.pharma.Model.JsonConstant;
import app.pharma.com.pharma.Model.ServerPath;
import app.pharma.com.pharma.Model.Utils;
import app.pharma.com.pharma.R;
import app.pharma.com.pharma.activity.Detail.Infor_Dr;
import io.realm.RealmList;


public class Dr_Fragment extends Fragment {
    ListView lv;
    List_Dr_Adapter adapter;
    ArrayList<Dr_Constructor> arr;
    ArrayList<CataloModel> arrCata;
    Context ct;
    int lastVisibleItem = 0;
    Spinner spiner;
    String idDr;
    DatabaseHandle db;
    private int lastY = 0;
    public Dr_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_dr_, container, false);
        lv = (ListView)v.findViewById(R.id.lv_dr);
        arr = new ArrayList<>();
        adapter = new List_Dr_Adapter(getContext(),0,arr);
        lv.setAdapter(adapter);
        db = new DatabaseHandle();
        ct = getContext();
        arrCata = new ArrayList<>();

        adapter.notifyDataSetChanged();

        spiner = (Spinner) v.findViewById(R.id.spin_dr);
        List<String> categories = new ArrayList<String>();
        if(!db.isCataloDrEmpty()){
            Catalo cata = db.getListCataloById(Constant.LIST_CATALO_DR);
            RealmList<CataloModel> list = cata.getListCatalo();

            for (int i =0; i <list.size();i++){
                CataloModel model = new CataloModel();
                model.setName(list.get(i).getName());
                model.setId(list.get(i).getId());
                arrCata.add(model);
                categories.add(list.get(i).getName());
            }

        }

        ArrayAdapter<String> dataAdapter =
                new ArrayAdapter<String>
                        (Common.context, R.layout.custom_text_spiner,R.id.txt_spin, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(R.layout.custom_text_spiner);

        // attaching data adapter to spinner
        spiner.setAdapter(dataAdapter);
        spiner.setSelection(0);
        spiner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String text = categories.get(i);
                for (int j =0;j<arrCata.size();j++){
                    if(arrCata.get(j).getName().equals(text)){

                        idDr = arrCata.get(j).getId();
                         loadPage(1);
                        break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        lv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int i1, int i2) {
                int top = 0;
                if(lv.getChildAt(0) != null){
                    top = lv.getChildAt(0).getTop();
                }

                if(firstVisibleItem > lastVisibleItem){
                    Intent it = new Intent(Constant.SCROLL_LV);
                    it.putExtra("action",Constant.ACTION_DOWN);
                    ct.sendBroadcast(it);
                }else if(firstVisibleItem < lastVisibleItem){
                    Intent it = new Intent(Constant.SCROLL_LV);
                    it.putExtra("action",Constant.ACTION_UP);
                    ct.sendBroadcast(it);
                }else{
                    if(top < lastY){
                        Intent it = new Intent(Constant.SCROLL_LV);
                        it.putExtra("action",Constant.ACTION_DOWN);
                        ct.sendBroadcast(it);
                    }else if(top > lastY){
                        Intent it = new Intent(Constant.SCROLL_LV);
                        it.putExtra("action",Constant.ACTION_UP);
                        ct.sendBroadcast(it);
                    }
                }

                lastVisibleItem = firstVisibleItem;
                lastY = top;

            }
        });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent it = new Intent(getContext(), Infor_Dr.class);
                it.putExtra("key","pharma");
                getContext().startActivity(it);
            }
        });
        return v;
    }

    private void loadPage(int page) {
        if(page==1){
            arr.clear();
        }
        adapter.notifyDataSetChanged();
        Map<String, String> map = new HashMap<>();
        map.put("page",page+"");
        map.put("type",idDr);
        Response.Listener<String> response = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("RESPONSE_DR",response);
                try {

                    JSONArray ja = new JSONArray(response);

                    for (int i = 0; i<ja.length();i++){
                        JSONObject jo = ja.getJSONObject(i);
                        JSONObject pharma = jo.getJSONObject(JsonConstant.PHARMACIS);
                        Dr_Constructor dr = new Dr_Constructor();
                        dr.setName(pharma.getString(JsonConstant.NAME));
                        dr.setLink(pharma.getString(JsonConstant.AVATAR));
                        dr.setRate(pharma.getDouble(JsonConstant.STAR));
                        dr.setId(pharma.getString(JsonConstant.ID));
                        dr.setHospital(pharma.getString(JsonConstant.HOSPITAL));
                         arr.add(dr);

                    }
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };
        Utils.PostServer(getActivity(), ServerPath.LIST_DR,map,response);

    }


    @Override
    public void onResume() {
        Intent it = new Intent(Constant.SCROLL_LV);
        it.putExtra("action",Constant.ACTION_UP);
        ct.sendBroadcast(it);
        super.onResume();

    }

}
