package app.pharma.com.pharma.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.ArrayList;

import app.pharma.com.pharma.Model.Common;
import app.pharma.com.pharma.Model.Pill_Constructor;
import app.pharma.com.pharma.R;

/**
 * Created by Vi on 3/18/2018.
 */

public class List_Pill_Adapter extends ArrayAdapter<Pill_Constructor> {
    Context ct;
    ArrayAdapter<Pill_Constructor> array;

    public List_Pill_Adapter(Context context, int resource, ArrayList<Pill_Constructor> items) {

        super(context, resource, items);
        this.ct = context;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;
        Pill_Constructor pill = getItem(position);
        if(v==null){
            LayoutInflater inflater = LayoutInflater.from(getContext());
            v =  inflater.inflate(R.layout.item_pill, null);
        }
        NumberFormat format = NumberFormat.getCurrencyInstance();
        TextView tv_title = v.findViewById(R.id.title_pill);
        TextView decr_pill = v.findViewById(R.id.decrip_pill);
        TextView compa_pill = v.findViewById(R.id.comppill);
        TextView price = v.findViewById(R.id.tv_price);
        TextView tv_like = v.findViewById(R.id.txt_like);

        TextView tv_comment = v.findViewById(R.id.txt_comment);
        tv_title.setText(pill.getName());
        decr_pill.setText(pill.getHtuse());
        price.setText(format.format((pill.getPrice())));
        tv_like.setText(pill.getLike()+"");
        tv_comment.setText(pill.getCmt()+"");
        Double D = pill.getStar();
        if(D!=null){

            int s = Integer.valueOf(D.intValue());
            LayoutInflater vi = (LayoutInflater) Common.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            Log.d("STAR",s+"");

// insert into main view
            for(int i = 0; i<s;i++){
                View star = vi.inflate(R.layout.star, null);
                LinearLayout insertPoint = (LinearLayout) v.findViewById(R.id.ln_star_pill);
                insertPoint.addView(star, 0, new ViewGroup.LayoutParams(15, 15));
            }

        }




        compa_pill.setText(pill.getOthername());


        return v;
    }

    private void init(View v) {

      //  TextView t
    }
}
