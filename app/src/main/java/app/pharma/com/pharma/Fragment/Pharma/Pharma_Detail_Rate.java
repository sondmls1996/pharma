package app.pharma.com.pharma.Fragment.Pharma;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.lang.reflect.Array;
import java.util.ArrayList;

import app.pharma.com.pharma.Model.Common;
import app.pharma.com.pharma.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Pharma_Detail_Rate extends Fragment {

    LinearLayout ln_list;
    ArrayList<View> arrView;
    LinearLayout ln_rate_now;
    RelativeLayout rl_item;
    public Pharma_Detail_Rate() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_pharma__rate, container, false);
        ln_list = v.findViewById(R.id.ln_list_rate);
        ln_rate_now = v.findViewById(R.id.ln_rate_now);
        arrView = new ArrayList<>();
        ln_rate_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogRate();
            }
        });

        LayoutInflater inflater2 = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        new AsyncTask<Void, Void, ArrayList<View>>() {
            @Override
            protected ArrayList<View> doInBackground(Void... voids) {

                for (int i = 0; i < 10; i++){
                    final View rowView = inflater2.inflate(R.layout.item_rate, null);
                    arrView.add(rowView);
                }
                return arrView;
            }

            @Override
            protected void onPostExecute(ArrayList<View> views) {

                for ( int i =0;i<views.size();i++){
                    ln_list.addView(views.get(i));
                }
                super.onPostExecute(views);
            }
        }.execute();


        return v;
    }

    private void showDialogRate() {
        Dialog dialog = new Dialog(Common.context);
        Window view=((Dialog)dialog).getWindow();
        view.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
// to get rounded corners and border for dialog window
        view.setBackgroundDrawableResource(R.drawable.border_white);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_rate);

        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

}
