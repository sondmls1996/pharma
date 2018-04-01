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
import android.widget.TextView;

import java.util.ArrayList;

import app.pharma.com.pharma.Model.Common;
import app.pharma.com.pharma.R;
import app.pharma.com.pharma.activity.Detail.Detail;

/**
 * A simple {@link Fragment} subclass.
 */
public class Pharma_Detail_Rate extends Fragment {

    LinearLayout ln_list;
    ArrayList<View> arrView;
    LinearLayout ln_rate_now;
    View v;
    TextView tv_comment,tv_de_comment;
    LayoutInflater inflater2;
    RelativeLayout rl_top;
    public Pharma_Detail_Rate() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         v = inflater.inflate(R.layout.fragment_pharma__rate, container, false);
        
        init();
      


        return v;
    }

    private void init() {

        ln_list = v.findViewById(R.id.ln_list_rate);
        ln_rate_now = v.findViewById(R.id.ln_rate_now);
        rl_top = v.findViewById(R.id.rl_rate_top);
        arrView = new ArrayList<>();

        tv_de_comment = v.findViewById(R.id.txt_de_comment);
        ln_rate_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogRate();
            }
        });
        inflater2 = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        setRelativeTop(Detail.key);

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
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void setRelativeTop(String key) {

        if(key.equals("pharma")){
            View rowView = inflater2.inflate(R.layout.rate_pharma_include, null);
            rl_top.addView(rowView);

        }else if(key.equals("pill")){
            View rowView = inflater2.inflate(R.layout.rate_pill_include, null);
            rl_top.addView(rowView);

        }else if(key.equals("sick")){
            View rowView = inflater2.inflate(R.layout.rate_sick_include, null);
            rl_top.addView(rowView);

        }


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
        TextView title = dialog.findViewById(R.id.txt_title_rate);
        if(Detail.key.equals("pill")){
            title.setText(getResources().getString(R.string.title_rate_pill));
        }else if(Detail.key.equals("pharma")){
            title.setText(getResources().getString(R.string.title_rate_pharma));
        }else if(Detail.key.equals("sick")){
            title.setText(getResources().getString(R.string.title_rate_sick));
        }
        dialog.show();
    }

}
