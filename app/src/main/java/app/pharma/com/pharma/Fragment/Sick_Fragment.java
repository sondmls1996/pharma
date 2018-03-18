package app.pharma.com.pharma.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import app.pharma.com.pharma.Adapter.List_Sick_Adapter;
import app.pharma.com.pharma.Model.Sick_Construct;
import app.pharma.com.pharma.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Sick_Fragment extends Fragment {
    ListView lv;
    List_Sick_Adapter adapter;
    ArrayList<Sick_Construct> arr;

    public Sick_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_sick_, container, false);
        lv = (ListView)v.findViewById(R.id.lv_sick);
        arr = new ArrayList<>();
        adapter = new List_Sick_Adapter(getContext(),0,arr);
        lv.setAdapter(adapter);
        arr.add(new Sick_Construct());
        arr.add(new Sick_Construct());
        arr.add(new Sick_Construct());
        arr.add(new Sick_Construct());
        arr.add(new Sick_Construct());
        adapter.notifyDataSetChanged();
        return v;
    }

}
