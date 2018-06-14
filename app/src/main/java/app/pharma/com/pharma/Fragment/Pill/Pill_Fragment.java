package app.pharma.com.pharma.Fragment.Pill;


import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
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
    ArrayList<String> arrMedicine,arrTP;
    ArrayList<CataloModel> arrTpAll,arrMedicineAll;
    TextView tvnull;
    String ingredient = "";
    ArrayList<Pill_Constructor> arr;
    ArrayList<CataloModel> arrCata;
    Context context;
    SwipeRefreshLayout swip;
    Context ct;
    int minPrice = 0;
    int maxPrice = 0;
    BroadcastReceiver broadcastSearch;
    View v;
    String idingredient = "";
    View footer;
    public  String idPill = "";
    boolean isFillter = false;
    FloatingActionButton fillter;
    int page = 1;
    DatabaseHandle db;
    boolean isLoading = false;
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
        Constant.inFragment = "pill";
        init();
        registerBroadcast();
        return v;
    }

    private void registerBroadcast() {
        broadcastSearch = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(intent.getAction().equals(Constant.SEARCH_ACTION)){
                    String key = intent.getStringExtra("key");
                    loadPageSearch(1,idPill,key);
                }
            }
        };
        IntentFilter it = new IntentFilter();
        it.addAction(Constant.SEARCH_ACTION);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(broadcastSearch,
                it);
    }

    private void unRegister(){
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(broadcastSearch);
    }

    private void init() {
        db = new DatabaseHandle();


        swip = v.findViewById(R.id.swip);
        swip.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                minPrice=-1;
//                maxPrice=-1;
//                idingredient="";
                isFillter = false;
                loadPage(1,isFillter);
            }
        });
        ct = getContext();
        context = getActivity();
        arrCata = new ArrayList<>();
        tvnull = v.findViewById(R.id.txt_null);
        lv = (ListView)v.findViewById(R.id.lv_pill);
        LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        footer = inflater.inflate(R.layout.footer_view,null);
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
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visiableItem, int total) {

                //Check if the last view is visible

//                if(absListView.getLastVisiblePosition()==total-1&&isLoading==false){
//                  //  lv.addFooterView(footer);
//                    page++;
//                    Log.d("PAGE_PILL",page+"");
//                    loadPage(page,isFillter);
//
//                }
//                if (++firstVisibleItem + i1 > i2) {
//
//
//
//
//                    //load more content
//                }

            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
              Utils.setAlphalAnimation(view);
                Intent it = new Intent(getActivity(), Detail.class);
                it.putExtra("key","pill");
                it.putExtra("id", arr.get(i).getId());

                startActivity(it);

            }
        });
    }

    public class mHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    //loading
                    lv.addFooterView(footer);
                    break;
                case 1:
                    //loaded
                    lv.removeFooterView(footer);
                    adapter.notifyDataSetChanged();
                    isLoading = false;
                    break;
            }
            super.handleMessage(msg);

        }
    }

    private void selectItem() {
        page = 1;

        loadPage(page,isFillter);
    }

    private void getData(Map map){



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

    private void loadPage(int page,boolean isFillter) {
        if(!Utils.isNetworkEnable(getActivity())){
            swip.setRefreshing(false);
            Utils.dialogNotif(getActivity().getResources().getString(R.string.network_err));
        }else{
            if(arr==null){
                arr = new ArrayList<>();
            }
            if(page==1){
                arr.clear();

            }

            Map<String, String> map = new HashMap<>();
            map.put("page",page+"");
            map.put("type",idPill);

            if(isFillter){
                map.put("minPrice",minPrice+"");
                map.put("maxPrice",maxPrice+"");
                map.put("ingredient",idingredient);
            }
                getData(map);
        }


    }
    private void loadPageSearch(int page,String idPill, String key) {
        if(!Utils.isNetworkEnable(getActivity())){
            swip.setRefreshing(false);
            Utils.dialogNotif(getActivity().getResources().getString(R.string.network_err));
        }else{
            if(arr==null){
                arr = new ArrayList<>();
            }
            if(page==1){
                arr.clear();

            }

            Map<String, String> map = new HashMap<>();
            map.put("page",page+"");
            map.put("type",idPill);
            map.put("key",key);
            getData(map);
        }


    }
    private void showDialogFillter() {
//        arr.clear();
        final String[] strTPId = {""};
        final String[] strMedicineId = {""};
        arrMedicine = new ArrayList<>();
        arrMedicineAll = new ArrayList<>();
        arrTP = new ArrayList<>();
        arrTpAll = new ArrayList<>();
        Dialog dialog = new Dialog(Common.context);
        Window view=((Dialog)dialog).getWindow();
        view.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
// to get rounded corners and border for dialog window

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_fillter);
        dialog.setCanceledOnTouchOutside(true);
        Spinner spMedicine = dialog.findViewById(R.id.spin_sick);
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
                Utils.setAlphalAnimation(view);
//                if(maxPrice>0){
                    isFillter = true;
                    dialog.dismiss();
                    loadPage(1,isFillter);

             //   }

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
                arrMedicine.add(list.get(i).getName());
                arrMedicineAll.add(list.get(i));
            }

        }
        if(!db.isCataloPillIntroEmpty()){
            Catalo cata = db.getListCataloById(Constant.PILL_INTRO_TYPE);
            RealmList<CataloModel> list = cata.getListCatalo();
            for (int i =0; i <list.size();i++){
                arrTP.add(list.get(i).getName());
                arrTpAll.add(list.get(i));
            }

        }

        ArrayAdapter<String> dataAdapter =
                new ArrayAdapter<String>
                        (Common.context, R.layout.custom_text_spiner,R.id.txt_spin, arrMedicine);
        dataAdapter.setDropDownViewResource(R.layout.custom_text_spiner);
        spMedicine.setAdapter(dataAdapter);



        ArrayAdapter<String> dataAdapter2 =
                new ArrayAdapter<String>
                        (Common.context, R.layout.custom_text_spiner,R.id.txt_spin, arrTP);
        dataAdapter2.setDropDownViewResource(R.layout.custom_text_spiner);
        sp_tp.setAdapter(dataAdapter2);
        sp_tp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                strTPId[0] = arrTpAll.get(i).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spMedicine.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                strMedicineId[0] = arrMedicineAll.get(i).getId();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        dialog.show();
    }
    @Override
    public void onResume() {
        if(broadcastSearch==null){

            registerBroadcast();
        }


        super.onResume();

    }

    @Override
    public void onStop() {
        unRegister();
        broadcastSearch=null;
        super.onStop();
    }

//    class BroadcastSearch extends BroadcastReceiver{
//
//        @Override
//        public void onReceive(Context context, Intent intent) {
//
//        }
//    }

}
