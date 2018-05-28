package app.pharma.com.pharma.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import app.pharma.com.pharma.Model.Common;
import app.pharma.com.pharma.Model.Constructor.Object.Rating_Obj;
import app.pharma.com.pharma.Model.Utils;
import app.pharma.com.pharma.R;

public class List_Rate_Adapter extends ArrayAdapter<Rating_Obj> {
    Context ct;
    ArrayAdapter<Rating_Obj> array;

    public List_Rate_Adapter(Context context, int resource, ArrayList<Rating_Obj> items) {

        super(context, resource, items);
        this.ct = context;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;
        Rating_Obj rate = getItem(position);
        if(v==null){
            LayoutInflater inflater = LayoutInflater.from(getContext());
            v =  inflater.inflate(R.layout.item_rate, null);
        }
        TextView name_rate = v.findViewById(R.id.tv_name_rate);
        TextView short_cmt = v.findViewById(R.id.tv_shortCmt);
        TextView cmt = v.findViewById(R.id.tv_cmt_rate);
        TextView date = v.findViewById(R.id.tv_date);
        LinearLayout ln_star = v.findViewById(R.id.ln_star_rate);
        ln_star.removeAllViews();

        Double star = rate.getStar();
        if(star!=null){

            int s = Integer.valueOf(star.intValue());
            LayoutInflater vi = (LayoutInflater) Common.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


// insert into main view
            for(int i = 0; i<s;i++){
                View stars = vi.inflate(R.layout.star, null);

                ln_star.addView(stars, 0, new ViewGroup.LayoutParams(30, 30));
            }

        }

        short_cmt.setText(rate.getShortComment());
        cmt.setText(rate.getComment());
        date.setText(Utils.convertTimestampToDate(rate.getTime()));


        return v;
    }

    private void init() {

    }
}

