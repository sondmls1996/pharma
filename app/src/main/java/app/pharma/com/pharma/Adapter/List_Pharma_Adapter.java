package app.pharma.com.pharma.Adapter;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import app.pharma.com.pharma.Model.Common;
import app.pharma.com.pharma.Model.Constructor.Pharma_Constructor;
import app.pharma.com.pharma.Model.Constructor.Pill_Constructor;
import app.pharma.com.pharma.Model.ServerPath;
import app.pharma.com.pharma.Model.Utils;
import app.pharma.com.pharma.R;

/**
 * Created by Vi on 3/19/2018.
 */

public class List_Pharma_Adapter extends ArrayAdapter<Pharma_Constructor> {
    Context ct;
    ArrayAdapter<Pill_Constructor> array;

    public List_Pharma_Adapter(Context context, int resource, ArrayList<Pharma_Constructor> items) {

        super(context, resource, items);
        this.ct = context;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;
        Pharma_Constructor pharma = getItem(position);
        if(v==null){
            LayoutInflater inflater = LayoutInflater.from(getContext());
            v =  inflater.inflate(R.layout.item_pharma, null);
        }
        ImageView img_pharma = v.findViewById(R.id.img_pharma);
        TextView tv_name = v.findViewById(R.id.name_pharma);
        TextView tv_adr = v.findViewById(R.id.adr_pharma);
        TextView tv_distance = v.findViewById(R.id.distance_pharma);
        LinearLayout ln = v.findViewById(R.id.ln_star_pharma);
        ln.removeAllViews();
        TextView like = v.findViewById(R.id.txt_like);
        TextView comment = v.findViewById(R.id.txt_comment);

        Utils.loadImagePicasso(ServerPath.ROOT_URL+pharma.getAvatar(),img_pharma);
        tv_name.setText(pharma.getName());
        tv_adr.setText(pharma.getAdr());
        like.setText(pharma.getLike());
        comment.setText(pharma.getComment());
        if(Common.lat!=0&&Common.lng!=0){
            double x1 = Common.lat;
            double y1 = Common.lng;
            double x2 = pharma.getX();
            double y2 = pharma.getY();
            Double x = Math.sqrt(Math.pow(x2 - x1, 2) - Math.pow(y2 - y1, 2));
            int a = Integer.valueOf(x.intValue());
            if(a>0){
                tv_distance.setText("Cách "+Integer.valueOf(x.intValue())+"km");
            }else{
                if(x>0){
                    String distance = x+"";
                    if(distance.length()>4){
                        distance = distance.substring(0,3);
                    }
                    tv_distance.setText("Cách "+distance+"km");
                }

            }


        }
        Double d = pharma.getRate();
        if(d!=null){

            int s = Integer.valueOf(d.intValue());
            LayoutInflater vi = (LayoutInflater) Common.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            for(int i = 0; i<s;i++){
                View star = vi.inflate(R.layout.star, null);
                ln.addView(star, 0, new ViewGroup.LayoutParams(30, 30));
            }

        }



        return v;
    }

    private void init() {

    }
}
