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

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import app.pharma.com.pharma.Model.Common;
import app.pharma.com.pharma.Model.ServerPath;
import app.pharma.com.pharma.Model.Sick_Construct;
import app.pharma.com.pharma.Model.Utils;
import app.pharma.com.pharma.R;

/**
 * Created by Vi on 3/19/2018.
 */

public class List_Sick_Adapter extends ArrayAdapter<Sick_Construct> {
    Context ct;
    ArrayAdapter<Sick_Construct> array;

    public List_Sick_Adapter(Context context, int resource, ArrayList<Sick_Construct> items) {

        super(context, resource, items);
        this.ct = context;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;
        Sick_Construct sick = getItem(position);
        if(v==null){
            LayoutInflater inflater = LayoutInflater.from(getContext());
            v =  inflater.inflate(R.layout.item_sick, null);
        }
        TextView name_sick = v.findViewById(R.id.name_sick);
        TextView descri_sick = v.findViewById(R.id.decrip_sick);
        TextView like = v.findViewById(R.id.txt_like);
        TextView comment = v.findViewById(R.id.txt_comment);
        TextView date = v.findViewById(R.id.date_sick);
        TextView catalo_txt = v.findViewById(R.id.cata_sick_text);

        name_sick.setText(sick.getName());
        descri_sick.setText(sick.getDescri());
        like.setText(sick.getLike()+"");
        date.setText(Utils.convertTimestampToDate(sick.getDate()));
        catalo_txt.setText(sick.getCatalo());

       ImageView img_sick = v.findViewById(R.id.img_sick);
        String firstImage = ServerPath.ROOT_URL+sick.getImage();
        Picasso.with(Common.context).load(firstImage).into(img_sick);
//        try {
//            JSONArray ja = new JSONArray(sick.getImage());
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }



        return v;
    }

    private void init() {

    }
}

