package app.pharma.com.pharma.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ViewUtils;
import android.text.Html;
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
import java.util.List;

import app.pharma.com.pharma.Model.Common;
import app.pharma.com.pharma.Model.Constant;
import app.pharma.com.pharma.Model.Constructor.Pill_Constructor;
import app.pharma.com.pharma.Model.ServerPath;
import app.pharma.com.pharma.Model.Utils;
import app.pharma.com.pharma.R;

/**
 * Created by Vi on 3/18/2018.
 */

public class List_Pill_Adapter extends
        RecyclerView.Adapter<List_Pill_Adapter.RecyclerViewHolder> {

        List<Pill_Constructor> list;

private List<Pill_Constructor> listData = new ArrayList<>();
        Context context;

public List_Pill_Adapter(Context context, List<Pill_Constructor> listData) {
        this.context = context;
        this.listData = listData;
        }

public void updateData(ArrayList<Pill_Constructor> arr) {
        this.listData = arr;
        notifyDataSetChanged();
        }

@Override
public int getItemCount() {
        return listData.size();
        }

@Override
public RecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup,
        int position) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.item_pill, viewGroup, false);
        //     new ScaleInAnimation(itemView).animate();
        return new RecyclerViewHolder(itemView);
        }

@Override
public void onBindViewHolder(RecyclerViewHolder viewHolder, int position) {
        Pill_Constructor pill = listData.get(position);

    viewHolder.tv_title.setText(pill.getName());
    viewHolder.decr_pill.setText(Html.fromHtml(context.getResources().getString(R.string.decripe,pill.getHtuse())));
    viewHolder.price.setText(Constant.format.format((pill.getPrice()))+"VND"
    );
    viewHolder.tv_like.setText(pill.getLike()+"");
    viewHolder.tv_comment.setText(pill.getCmt()+"");
    Double D = pill.getStar();

    viewHolder.insertPoint.removeAllViews();
    try {
        JSONArray arrImage = new JSONArray(pill.getImage());
        String firstImage = ServerPath.ROOT_URL+arrImage.get(0).toString();
        Utils.loadImagePicasso(firstImage,viewHolder.imgPill);

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

            viewHolder.insertPoint.addView(star, 0, new ViewGroup.LayoutParams(30, 30));
        }

    }


    viewHolder.compa_pill.setText(pill.getOthername());
        }


public void removeItem(int position) {
        listData.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, listData.size());
        notifyDataSetChanged();
        }

/**
 * ViewHolder for item view of list
 */
public class RecyclerViewHolder extends RecyclerView.ViewHolder {

    public TextView tv_title, decr_pill, compa_pill, price, tv_like, tv_comment;
    public ImageView imgPill;
    public   LinearLayout insertPoint;
    public RecyclerViewHolder(View v) {
        super(v);

         tv_title = v.findViewById(R.id.title_pill);
         decr_pill = v.findViewById(R.id.decrip_pill);
         compa_pill = v.findViewById(R.id.comppill);
         price = v.findViewById(R.id.tv_price);
         tv_like = v.findViewById(R.id.txt_like);
         imgPill = v.findViewById(R.id.img_pill);
         tv_comment = v.findViewById(R.id.txt_comment);
         insertPoint = v.findViewById(R.id.ln_star_pill);
    }
}
}