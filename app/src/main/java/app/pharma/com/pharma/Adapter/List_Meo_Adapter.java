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

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import app.pharma.com.pharma.Model.Constructor.Meo_Constructor;
import app.pharma.com.pharma.Model.ServerPath;
import app.pharma.com.pharma.Model.Utils;
import app.pharma.com.pharma.R;

/**
 * Created by Vi on 3/19/2018.
 */

public class List_Meo_Adapter extends ArrayAdapter<Meo_Constructor> {
    Context ct;
    ArrayAdapter<Meo_Constructor> array;
    SimpleDateFormat format,formatHours;
    public List_Meo_Adapter(Context context, int resource, ArrayList<Meo_Constructor> items) {

        super(context, resource, items);
        this.ct = context;
        format = new SimpleDateFormat("dd/MM/yyyy");
        formatHours = new SimpleDateFormat("hh:mm");
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;
        Meo_Constructor pill = getItem(position);
        if(v==null){
            LayoutInflater inflater = LayoutInflater.from(getContext());
            v =  inflater.inflate(R.layout.item_meo, null);
        }
        ImageView img = v.findViewById(R.id.img_meo);
        TextView tv_title = v.findViewById(R.id.title_meo);
        TextView descr = v.findViewById(R.id.descr_meo);
        TextView date = v.findViewById(R.id.date_meo);
        TextView time_meo = v.findViewById(R.id.time_meo);

        Utils.loadImagePicasso(ServerPath.ROOT_URL+pill.getImage(),img);
        tv_title.setText(pill.getTitle());
        descr.setText(pill.getDescrep());
        date.setText(format.format(pill.getDate()));
        time_meo.setText(formatHours.format(pill.getDate()));


        init();

        return v;
    }

    private void init() {

    }
}

