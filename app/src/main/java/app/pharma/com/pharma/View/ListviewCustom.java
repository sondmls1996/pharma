package app.pharma.com.pharma.View;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;

import app.pharma.com.pharma.Adapter.List_Dr_Adapter;
import app.pharma.com.pharma.Adapter.List_Pharma_Adapter;
import app.pharma.com.pharma.Adapter.List_Pill_Adapter;
import app.pharma.com.pharma.Adapter.List_Sick_Adapter;
import app.pharma.com.pharma.Model.Constructor.Dr_Constructor;
import app.pharma.com.pharma.Model.Constructor.Pharma_Constructor;
import app.pharma.com.pharma.Model.Constructor.Pill_Constructor;
import app.pharma.com.pharma.Model.Sick_Construct;
import app.pharma.com.pharma.Model.Utils;
import app.pharma.com.pharma.R;
import app.pharma.com.pharma.activity.Detail.Detail;

public class ListviewCustom extends SwipeRefreshLayout {

    Object object;
    String type = "";
    View footer;


    public ListviewCustom(Context context, String type) {
        super(context);
        this.type = type;
        initView();
    }



    private void initView() {
        setOnRefreshListener(this);
    }

    private void setOnRefreshListener(ListviewCustom listviewCustom) {


    }



}
