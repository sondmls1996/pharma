package app.pharma.com.pharma.Fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import app.pharma.com.pharma.Model.Common;
import app.pharma.com.pharma.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Pill_Fragment_Detail extends Fragment {
    LinearLayout ln;

    public Pill_Fragment_Detail() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_pill__fragment__detail, container, false);
        ln = (LinearLayout)v.findViewById(R.id.ln_lq_pill);
        LayoutInflater inflater2 = (LayoutInflater) Common.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        for (int i = 0; i < 5; i++){
            final View rowView = inflater2.inflate(R.layout.item_pill_lq, null);
            ln.addView(rowView);

        }
        return v;
    }

}
