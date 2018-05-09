package app.pharma.com.pharma.Fragment.Pill;


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

import app.pharma.com.pharma.Adapter.List_Pill_Adapter;
import app.pharma.com.pharma.Model.CataloModel;
import app.pharma.com.pharma.Model.Common;
import app.pharma.com.pharma.Model.Constant;
import app.pharma.com.pharma.Model.Database.Catalo;
import app.pharma.com.pharma.Model.Database.DatabaseHandle;
import app.pharma.com.pharma.Model.JsonConstant;
import app.pharma.com.pharma.Model.Constructor.Pill_Constructor;
import app.pharma.com.pharma.Model.ServerPath;
import app.pharma.com.pharma.Model.Utils;
import app.pharma.com.pharma.R;
import app.pharma.com.pharma.activity.Detail.Detail;
import io.realm.RealmList;


/**
 * A simple {@link Fragment} subclass.
 */
public class Pill_Fragment extends Fragment {
    ListView lv;
    List_Pill_Adapter adapter;
    Spinner spiner;
    ArrayList<Pill_Constructor> arr;
    ArrayList<CataloModel> arrCata;
    Context ct;
    String idPill = "";
    int page = 1;
    DatabaseHandle db;
    int lastVisibleItem = 0;
    private int lastY = 0;
    public Pill_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_pill, container, false);
        db = new DatabaseHandle();
        ct = getContext();
        arrCata = new ArrayList<>();
        lv = (ListView)v.findViewById(R.id.lv_pill);
        spiner = (Spinner) v.findViewById(R.id.spin_pill);
        List<String> categories = new ArrayList<String>();
        if(!db.isCataloPillEmpty()){
            Catalo cata = db.getListCataloById(Constant.LIST_CATALO_PILL);
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

        spiner.setSelection(1);
        spiner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String text = categories.get(i);
                for (int j =0;j<arrCata.size();j++){
                    if(arrCata.get(j).getName().equals(text)){

                        idPill = arrCata.get(j).getId();
                        selectItem();
                        break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        arr = new ArrayList<>();
        adapter = new List_Pill_Adapter(getContext(),0,arr);
        lv.setAdapter(adapter);





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
                Intent it = new Intent(getActivity(), Detail.class);
                it.putExtra("key","pill");
                it.putExtra("id", arr.get(i).getId());
                it.putExtra("images",arr.get(i).getImage());
                startActivity(it);
            }
        });
        return v;
    }

    private void selectItem() {
        page = 1;
        loadPage(page);
    }

    private void loadPage(int page) {
        if(page==1){
            arr.clear();
        }
        adapter.notifyDataSetChanged();
        Map<String, String> map = new HashMap<>();
        map.put("page",page+"");
        map.put("type",idPill);
        Response.Listener<String> response = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("RESPONSE_PILL",response);
                try {

                    JSONArray ja = new JSONArray(response);

                    for (int i = 0; i<ja.length();i++){
                        JSONObject jo = ja.getJSONObject(i);
                        JSONObject product = jo.getJSONObject(JsonConstant.PRODUCT);
                        Pill_Constructor pill = new Pill_Constructor();
                        pill.setName(product.getString(JsonConstant.NAME));
                        pill.setHtuse(product.getString(JsonConstant.DESCRI));
                        pill.setId(product.getString(JsonConstant.ID));
                          JSONObject price = product.getJSONObject(JsonConstant.PRICE);
                        pill.setPrice(price.getInt(JsonConstant.MONEY));
                        pill.setCmt(product.getInt(JsonConstant.COMMENT));
                        pill.setLike(product.getInt(JsonConstant.LIKE));
                        pill.setStar(product.getDouble(JsonConstant.STAR));
                        JSONArray Image = product.getJSONArray(JsonConstant.IMAGE);
                        pill.setImage(Image.toString());
                        pill.setOthername(product.getString(JsonConstant.COMPANY));
                        arr.add(pill);

                    }
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };
        Utils.PostServer(getActivity(), ServerPath.LIST_PILL,map,response);

    }

    @Override
    public void onResume() {
        Intent it = new Intent(Constant.SCROLL_LV);
        it.putExtra("action",Constant.ACTION_UP);
        ct.sendBroadcast(it);


        super.onResume();

    }
}
