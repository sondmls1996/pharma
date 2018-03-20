package app.pharma.com.pharma.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import app.pharma.com.pharma.Adapter.List_Meo_Adapter;
import app.pharma.com.pharma.Model.Meo_Constructor;
import app.pharma.com.pharma.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Meo_Fragment extends Fragment {
    ListView lv;
    List_Meo_Adapter adapter;
    ArrayList<Meo_Constructor> arr;

    public Meo_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_meo, container, false);
        lv = (ListView)v.findViewById(R.id.lv_meo);
        arr = new ArrayList<>();
        adapter = new List_Meo_Adapter(getContext(),0,arr);
        lv.setAdapter(adapter);
        arr.add(new Meo_Constructor());
        arr.add(new Meo_Constructor());
        arr.add(new Meo_Constructor());
        arr.add(new Meo_Constructor());
        arr.add(new Meo_Constructor());
        adapter.notifyDataSetChanged();
        return v;
    }

}
