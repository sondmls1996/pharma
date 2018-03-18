package app.pharma.com.pharma.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import app.pharma.com.pharma.Adapter.List_Pill_Adapter;
import app.pharma.com.pharma.Model.Pill_Constructor;
import app.pharma.com.pharma.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class Pill_Fragment extends Fragment {
    ListView lv;
    List_Pill_Adapter adapter;
    ArrayList<Pill_Constructor> arr;

    public Pill_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_pill, container, false);
        lv = (ListView)v.findViewById(R.id.lv_pill);
        arr = new ArrayList<>();
        adapter = new List_Pill_Adapter(getContext(),0,arr);
        lv.setAdapter(adapter);
        arr.add(new Pill_Constructor());
        arr.add(new Pill_Constructor());
        arr.add(new Pill_Constructor());
        arr.add(new Pill_Constructor());
        arr.add(new Pill_Constructor());
        adapter.notifyDataSetChanged();
        return v;
    }

}
