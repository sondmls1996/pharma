package app.pharma.com.pharma.Adapter;

import android.content.Context;
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

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import app.pharma.com.pharma.Model.Common;
import app.pharma.com.pharma.Model.Constant;
import app.pharma.com.pharma.Model.Constructor.Pill_Constructor;
import app.pharma.com.pharma.Model.ServerPath;
import app.pharma.com.pharma.Model.Utils;
import app.pharma.com.pharma.R;

/**
 * Created by Vi on 3/18/2018.
 */

public class List_Pill_Adapter extends ArrayAdapter<Pill_Constructor> {
    Context ct;
    ArrayList<Pill_Constructor> array;

    public List_Pill_Adapter(Context context, int resource, ArrayList<Pill_Constructor> items) {

        super(context, resource, items);
        this.ct = context;
        this.array = items;

    }

    public void addListItem(ArrayList<Pill_Constructor> itemplust){
        array.addAll(itemplust);
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return array.size();
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
//        v.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent it = new Intent(getContext(), Detail.class);
//                it.putExtra("key","pill");
//                it.putExtra("id", pill.getId());
//                getContext().startActivity(it);
//            }
//        });

        TextView tv_title = v.findViewById(R.id.title_pill);
        TextView decr_pill = v.findViewById(R.id.decrip_pill);
        TextView compa_pill = v.findViewById(R.id.comppill);
        TextView price = v.findViewById(R.id.tv_price);
        TextView tv_like = v.findViewById(R.id.txt_like);
        ImageView imgPill = v.findViewById(R.id.img_pill);
        TextView tv_comment = v.findViewById(R.id.txt_comment);
        tv_title.setText(pill.getName());
        decr_pill.setText(pill.getHtuse());
        price.setText(Constant.format.format((pill.getPrice()))+"VND"
        );
        tv_like.setText(pill.getLike()+"");
        tv_comment.setText(pill.getCmt()+"");
        Double D = pill.getStar();
        LinearLayout insertPoint = (LinearLayout) v.findViewById(R.id.ln_star_pill);
        insertPoint.removeAllViews();
        try {
            JSONArray arrImage = new JSONArray(pill.getImage());
            String firstImage = ServerPath.ROOT_URL+arrImage.get(0).toString();
            Utils.loadImagePicasso(firstImage,imgPill);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(D!=null){

            int s = Integer.valueOf(D.intValue());
            LayoutInflater vi = (LayoutInflater) Common.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            Log.d("STAR",s+"");

// insert into main view
            for(int i = 0; i<s;i++){
                View star = vi.inflate(R.layout.star, null);

                insertPoint.addView(star, 0, new ViewGroup.LayoutParams(30, 30));
            }

        }


        compa_pill.setText(pill.getOthername());


        return v;
    }

    private void init(View v) {

      //  TextView t
    }
}
