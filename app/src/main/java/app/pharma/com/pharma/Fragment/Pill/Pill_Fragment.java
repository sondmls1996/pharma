package app.pharma.com.pharma.Fragment.Pill;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatSeekBar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

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
import app.pharma.com.pharma.Model.Constructor.Pill_Constructor;
import app.pharma.com.pharma.Model.Database.Catalo;
import app.pharma.com.pharma.Model.Database.DatabaseHandle;
import app.pharma.com.pharma.Model.JsonConstant;
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
    ArrayList<String> arrString,arr_tp;
    ArrayList<CataloModel> arrTp;
    TextView tvnull;
    String ingredient = "";
    ArrayList<Pill_Constructor> arr;
    ArrayList<CataloModel> arrCata;
    Context context;
    SwipeRefreshLayout swip;
    Context ct;
    int minPrice = 0;
    int maxPrice = 0;
    View v;
    String idingredient = "";
    String idPill = "";
    FloatingActionButton fillter;
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
         v = inflater.inflate(R.layout.fragment_pill, container, false);
        init();

        return v;
    }

    private void init() {
        db = new DatabaseHandle();
        arrString = new ArrayList<>();
        arr_tp = new ArrayList<>();
        swip = v.findViewById(R.id.swip);
        swip.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadPage(1);
            }
        });
        ct = getContext();
        context = getActivity();
        arrCata = new ArrayList<>();
        tvnull = v.findViewById(R.id.txt_null);
        lv = (ListView)v.findViewById(R.id.lv_pill);
        spiner = (Spinner) v.findViewById(R.id.spin_pill);
        fillter = v.findViewById(R.id.fb_fill);
        fillter.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.blue)));
        fillter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogFillter();
            }
        });
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

        spiner.setSelection(0);
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

                //Check if the last view is visible
                if (++firstVisibleItem + i1 > i2) {
                    if(arr.size()>=15){

                        page++;
                        Log.d("PAGE_PILL",page+"");
                        loadPage(page);
                    }

                    //load more content
                }

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
    }

    public void fillter(Context ct,int minPrice,int maxPrice,String ingredient){
        page = 1;
        this.context = ct;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.idingredient = ingredient;
        loadPage(page);

    }

    private void selectItem() {
        page = 1;
        loadPage(page);
    }

    private void loadPage(int page) {
        if(arr==null){
            arr = new ArrayList<>();
        }
        if(page==1){
            arr.clear();

        }

        Map<String, String> map = new HashMap<>();
        map.put("page",page+"");
        map.put("type",idPill);
        if(minPrice>-1){
            map.put("minPrice",minPrice+"");
        }
        if(maxPrice>-1){
            map.put("maxPrice",maxPrice+"");
        }
        if(!idingredient.equals("")){
            map.put("ingredient",idingredient);

        }

        Response.Listener<String> response = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("RESPONSE_PILL",response);
                swip.setRefreshing(false);
                try {
                    JSONObject root = new JSONObject(response);
                    if(root.has(JsonConstant.CODE)){
                        String code = root.getString(JsonConstant.CODE);
                        switch (code){
                            case "0":
                                new AsyncTask<Void,Void,Void>(){

                                    @Override
                                    protected Void doInBackground(Void... voids) {
                                        JSONArray ja = null;
                                        try {

                                            ja = root.getJSONArray(JsonConstant.LIST_PRODUCT);
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
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }


                                        return null;
                                    }

                                    @Override
                                    protected void onPostExecute(Void aVoid) {
                                        if(arr.size()>0){
                                            tvnull.setVisibility(View.GONE);
                                        }else{
                                            tvnull.setVisibility(View.VISIBLE);
                                        }
                                        adapter.notifyDataSetChanged();
                                        super.onPostExecute(aVoid);
                                    }
                                }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);


                                break;
                            case "1":
                                Utils.dialogNotif(getResources().getString(R.string.error));
                                break;
                                default:
                                    break;
                        }


                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };
        Utils.PostServer(context, ServerPath.LIST_PILL,map,response);

    }
    private void showDialogFillter() {
        arr.clear();
        arr_tp.clear();
        arrTp = new ArrayList<>();
        Dialog dialog = new Dialog(Common.context);
        Window view=((Dialog)dialog).getWindow();
        view.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
// to get rounded corners and border for dialog window

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_fillter);
        dialog.setCanceledOnTouchOutside(true);
        Spinner sp_sick = dialog.findViewById(R.id.spin_sick);
        Spinner sp_tp = dialog.findViewById(R.id.spin_tp);
        TextView tv_price = dialog.findViewById(R.id.tv_price);

        AppCompatSeekBar seek = dialog.findViewById(R.id.seek_bar_min);
        AppCompatSeekBar seekMax = dialog.findViewById(R.id.seek_bar_max);
        if(minPrice>-1){
            seek.setProgress(minPrice);
        }
        if(maxPrice>-1){
            seekMax.setProgress(maxPrice);
        }
        Button apply = dialog.findViewById(R.id.btn_apply);
        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(maxPrice>0){
                    dialog.dismiss();
                    loadPage(1);

                }

            }
        });
        tv_price.setText(getActivity().getResources().
                getString(R.string.price,minPrice+"",maxPrice+""));
        seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                minPrice = progress;
                tv_price.setText(getActivity().getResources().
                        getString(R.string.price,minPrice+"",maxPrice+""));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekMax.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                maxPrice = progress;
                tv_price.setText(getActivity().getResources().
                        getString(R.string.price,minPrice+"",maxPrice+""));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        if(!db.isCataloPillEmpty()){
            Catalo cata = db.getListCataloById(Constant.LIST_CATALO_PILL);
            RealmList<CataloModel> list = cata.getListCatalo();
            for (int i =0; i <list.size();i++){
                arrString.add(list.get(i).getName());
            }

        }
        if(!db.isCataloPillEmpty()){
            Catalo cata = db.getListCataloById(Constant.LIST_CATALO_PILL_INTRO);
            RealmList<CataloModel> list = cata.getListCatalo();
            for (int i =0; i <list.size();i++){
                arr_tp.add(list.get(i).getName());
                arrTp.add(list.get(i));
            }

        }



        ArrayAdapter<String> dataAdapter =
                new ArrayAdapter<String>
                        (Common.context, R.layout.custom_text_spiner,R.id.txt_spin, arrString);
        dataAdapter.setDropDownViewResource(R.layout.custom_text_spiner);
        sp_sick.setAdapter(dataAdapter);

        sp_sick.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayAdapter<String> dataAdapter2 =
                new ArrayAdapter<String>
                        (Common.context, R.layout.custom_text_spiner,R.id.txt_spin, arr_tp);
        dataAdapter2.setDropDownViewResource(R.layout.custom_text_spiner);
        sp_tp.setAdapter(dataAdapter2);
        sp_tp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String text = arr_tp.get(i).toString();
                for (int j =0; j<arrTp.size();j++){
                    if(text.equals(arrTp.get(j).getName())){
                        idingredient = text;
                        break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        dialog.show();
    }
    @Override
    public void onResume() {
        Intent it = new Intent(Constant.SCROLL_LV);
        it.putExtra("action",Constant.ACTION_UP);
        ct.sendBroadcast(it);


        super.onResume();

    }
}
