package app.pharma.com.pharma.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.formats.NativeAd;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import app.pharma.com.pharma.Model.Constructor.Dr_Constructor;
import app.pharma.com.pharma.Model.ServerPath;
import app.pharma.com.pharma.Model.Utils;
import app.pharma.com.pharma.R;

/**
 * Created by Vi on 3/19/2018.
 */

public class List_Dr_Adapter extends ArrayAdapter<Dr_Constructor> {
    Context ct;
    ArrayAdapter<Dr_Constructor> array;

    public List_Dr_Adapter(Context context, int resource, ArrayList<Dr_Constructor> items) {

        super(context, resource, items);
        this.ct = context;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;
        Dr_Constructor dr = getItem(position);
        if(v==null){
            LayoutInflater inflater = LayoutInflater.from(getContext());
            v =  inflater.inflate(R.layout.item_dr, null);
        }
        ImageView img_dr = v.findViewById(R.id.img_dr);
        TextView name_dr = v.findViewById(R.id.text_name_dr);
        TextView age_dr = v.findViewById(R.id.tv_age_dr);
        TextView work_year = v.findViewById(R.id.tv_work_dr_age);
        TextView ck = v.findViewById(R.id.tv_dr_ck);
        TextView hospital = v.findViewById(R.id.tv_dr_hospital);
        Utils.loadImagePicasso(ServerPath.ROOT_URL+dr.getAvatar(),img_dr);
        //Picasso.with(getContext()).load(ServerPath.ROOT_URL+dr.getAvatar()).into(img_dr);
        name_dr.setText(dr.getName());
        age_dr.setText(dr.getAge()+" tuổi");
        work_year.setText(dr.getWork_year() +" công tác");
        String[] main = dr.getHospital().split("-");

        ck.setText(main[0]);
        hospital.setText(main[1]);
        return v;
    }

    private void init() {

    }
}

