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
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatSeekBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import app.pharma.com.pharma.Support.EndlessScroll;
import app.pharma.com.pharma.Support.RecyclerItemClickListener;
import app.pharma.com.pharma.activity.Detail.Detail;
import io.realm.RealmList;

import static io.realm.internal.SyncObjectServerFacade.getApplicationContext;


/**
 * A simple {@link Fragment} subclass.
 */
public class Pill_Fragment extends Fragment {
    RecyclerView lv;

    List_Pill_Adapter adapter;
    Spinner spiner;
    ArrayList<String> arrMedicine,arrTP;
    ArrayList<CataloModel> arrTpAll,arrMedicineAll;
    TextView tvnull;
    String ingredient = "";
    String key = "";
    ArrayList<Pill_Constructor> arr;
    ArrayList<CataloModel> arrCata;
    Context context;
    SwipeRefreshLayout swip;
    Context ct;
    int step = 10;
    int stepMax = 2000000;
    int progressMin = 0;
    int progressMax = 0;
    String idFillCat = "";
    int stepMin = 10000;
    int minPrice = 0;
    int maxPrice = 0;
    BroadcastReceiver broadcastSearch,broadcastCloseSearch;
    View v;
    String idingredient = "";
    View footer;
    public  String idPill = "";
    boolean isFillter = false;
    boolean isSearch = false;
    boolean isNomar = true;
    FloatingActionButton fillter;
    int Mainpage = 1;
    DatabaseHandle db;
    boolean isLoading = false;
    int lastVisibleItem = 0;
    boolean mIsLoading;
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
        registerBroadcastSearch();
       // registerBroadcastCloseSearch();
        return v;
    }

    private void registerBroadcastSearch() {
        broadcastSearch = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(intent.getAction().equals(Constant.SEARCH_ACTION)){
                    key = intent.getStringExtra("key");
                    Mainpage = 1;
                    isNomar = false;
                    isFillter = false;
                    isSearch = true;

                    loadManager(1,isFillter,isNomar,isSearch,key);

                 //   loadPageSearch(Mainpage,idPill,key);
                }
            }
        };
        IntentFilter it = new IntentFilter();
        it.addAction(Constant.SEARCH_ACTION);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(broadcastSearch,
                it);
    }
    public void setRecycle(View v){
        lv = v.findViewById(R.id.lv_pill);
        lv.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        lv.setLayoutManager(layoutManager);
        adapter = new List_Pill_Adapter(getActivity(), arr);
        lv.setAdapter(adapter);
        EndlessScroll endlessScroll = new EndlessScroll(layoutManager,getApplicationContext()) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                Mainpage++;
               loadManager(Mainpage,isFillter,isNomar,isSearch,key);
            }
        };
        lv.addOnScrollListener(endlessScroll);
        lv.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Utils.setAlphalAnimation(view);
                        Intent it = new Intent(getActivity(), Detail.class);
                        it.putExtra("key","pill");
                        it.putExtra("id", arr.get(position).getId());

                        startActivity(it);

                        // TODO Handle item click
                    }
                })
        );
    }


    private void unRegister(){
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(broadcastSearch);
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(broadcastCloseSearch);
    }

    private void init() {
        db = new DatabaseHandle();
        footer = ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.footer_view, null, false);

        swip = v.findViewById(R.id.swip);
        swip.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                minPrice=-1;
//                maxPrice=-1;
                idingredient="";
                Mainpage = 1;
                isFillter = false;
                isSearch = false;
                isNomar = true;
                key = "";
                loadManager(Mainpage,isFillter,isNomar,isSearch,key);
            }
        });
        ct = getContext();
        context = getActivity();
        arrCata = new ArrayList<>();
        tvnull = v.findViewById(R.id.txt_null);
        arr = new ArrayList<>();

        LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

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


        spiner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String text = categories.get(i);

                for (int j =0;j<arrCata.size();j++){

                    if(arrCata.get(j).getName().equals(text)){
                        if(!idPill.equals(arrCata.get(j).getId())){
                            idPill = arrCata.get(j).getId();
                            isNomar = true;
                            Mainpage = 1;
                            isFillter = false;
                            isSearch = false;
                            key = "";
                            loadManager(Mainpage,isFillter,isNomar,isSearch,key);
                            break;
                        }


                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        setRecycle(v);
        spiner.setSelection(0);
    }


    public void loadManager(int page,boolean isFillter, boolean isNomar, boolean isSearch,String key){
        if(isFillter){

            loadPageFliter(page);
        }
        if (isNomar) {
            loadPage(page);
        }
        if(isSearch){
            loadPageSearch(page,idPill,key);
        }
    }

    private void getData(Map map){

        Log.d("MAP_PILL",map.toString());
        final boolean[] isEmpty = {false};
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
                                            if(ja.length()>0){
                                                for (int i = 0; i<ja.length();i++){
                                                    JSONObject jo = ja.getJSONObject(i);
                                                    JSONObject product = jo.getJSONObject(JsonConstant.PRODUCT);
                                                    Pill_Constructor pill = new Pill_Constructor();
                                                    pill.setName(product.getString(JsonConstant.NAME));
                                                    pill.setHtuse(product.getString(JsonConstant.DESCRI));
                                                    pill.setId(product.getString(JsonConstant.ID));
                                                    JSONObject price = product.getJSONObject(JsonConstant.PRICE);
                                                    if(price.getString(JsonConstant.MONEY).equals("")){
                                                        pill.setPrice(0);
                                                    }else{
                                                        pill.setPrice(price.getInt(JsonConstant.MONEY));
                                                    }

                                                    pill.setCmt(product.getInt(JsonConstant.COMMENT));
                                                    pill.setLike(product.getInt(JsonConstant.LIKE));
                                                    pill.setStar(product.getDouble(JsonConstant.STAR));
                                                    JSONArray Image = product.getJSONArray(JsonConstant.IMAGE);
                                                    pill.setImage(Image.toString());
                                                    pill.setOthername(product.getString(JsonConstant.COMPANY));
                                                    arr.add(pill);

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
                    if(getActivity()!=null){
                        Utils.dialogNotif(getActivity().getResources().getString(R.string.server_err));
                    }

                    e.printStackTrace();
                }

            }
        };
        Utils.PostServer(context, ServerPath.LIST_PILL,map,response);

    }

    private void loadPage(int page) {
        Log.d("PAGE_PILL",page+"");
        if(!Utils.isNetworkEnable(getActivity())){
            swip.setRefreshing(false);
            Utils.dialogNotif(getActivity().getResources().getString(R.string.network_err));
        }else{
            if(arr==null){
                arr = new ArrayList<>();
            }
            if(page==1){
                arr.clear();
                adapter.notifyDataSetChanged();
            }

            Map<String, String> map = new HashMap<>();
            map.put("page",page+"");
            map.put("type",idPill);


                getData(map);
        }


    }
    private void loadPageFliter(int page) {

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
        Log.d("PAGE_PILL",page+"");
            Map<String, String> map = new HashMap<>();
            map.put("page",page+"");
            map.put("type",idPill);
                map.put("priceFrom",minPrice+"");
                map.put("priceTo",maxPrice+"");
                map.put("idCat",idFillCat);
                map.put("ingredient",idingredient);
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
                adapter.notifyDataSetChanged();
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
//        seek.setMax(stepMax);
//
//        seekMax.setMax(stepMax);


        if(progressMin>-1){
            seek.setProgress(progressMin);
        }
        if(progressMax>-1){
            seekMax.setProgress(progressMax);
        }

        Button apply = dialog.findViewById(R.id.btn_apply);
        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.setAlphalAnimation(view);
//                if(maxPrice>0){
                    isFillter = true;
                    isNomar = false;
                    key = "";
                    isSearch = false;
                    dialog.dismiss();
                  loadManager(Mainpage,isFillter,isNomar,isSearch,key);
             //   }

            }
        });
        tv_price.setText(getActivity().getResources().
                getString(R.string.price,Constant.format.format(minPrice)+"VND ",
                        Constant.format.format(maxPrice)+"VND "));
        seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressMin = progress;
                minPrice = progressMin*(stepMax/100);
                tv_price.setText(getActivity().getResources().
                        getString(R.string.price,Constant.format.format(minPrice)+"VND ",
                                Constant.format.format(maxPrice)+"VND "));
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
                progressMax = progress;
                maxPrice = progressMax*(stepMax/100);
                tv_price.setText(getActivity().getResources().
                        getString(R.string.price,Constant.format.format(minPrice)+"VND ",
                                Constant.format.format(maxPrice)+"VND "));
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
               // strTPId[0] = arrTpAll.get(i).getId();
                idingredient = arrTpAll.get(i).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spMedicine.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                idFillCat = arrMedicineAll.get(i).getId();


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

            registerBroadcastSearch();
        }
//        if(broadcastCloseSearch==null){
//
//            registerBroadcastCloseSearch();
//        }

        super.onResume();

    }

    private void registerBroadcastCloseSearch() {
//        broadcastCloseSearch = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                if(intent.getAction().equals(Constant.CLOSE_SEARCH_ACTION)){
//                    Utils.hideKeyboard(getActivity());
//                    //   loadPageSearch(Mainpage,idPill,key);
//                }
//            }
//        };
//        IntentFilter it = new IntentFilter();
//        it.addAction(Constant.CLOSE_SEARCH_ACTION);
//        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(broadcastCloseSearch,
//                it);

    }

    @Override
    public void onStop() {
        unRegister();
        broadcastSearch=null;
      //  broadcastCloseSearch = null;
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
