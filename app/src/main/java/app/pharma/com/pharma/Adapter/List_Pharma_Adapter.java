package app.pharma.com.pharma.Adapter;

import android.content.Context;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
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
import app.pharma.com.pharma.Model.Constructor.Pharma_Constructor;
import app.pharma.com.pharma.Model.Constructor.Pharma_Constructor;
import app.pharma.com.pharma.Model.ServerPath;
import app.pharma.com.pharma.Model.Utils;
import app.pharma.com.pharma.R;

/**
 * Created by Vi on 3/19/2018.
 */

public class List_Pharma_Adapter extends
        RecyclerView.Adapter<List_Pharma_Adapter.RecyclerViewHolder> {

    List<Pharma_Constructor> list;

    private List<Pharma_Constructor> listData = new ArrayList<>();
    Context context;

    public List_Pharma_Adapter(Context context, List<Pharma_Constructor> listData) {
        this.context = context;
        this.listData = listData;
    }

    public void updateData(ArrayList<Pharma_Constructor> arr) {
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
        View itemView = inflater.inflate(R.layout.item_pharma, viewGroup, false);
        //     new ScaleInAnimation(itemView).animate();
        return new RecyclerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(List_Pharma_Adapter.RecyclerViewHolder viewHolder, int position) {
        Pharma_Constructor pharma = listData.get(position);

        Utils.loadImagePicasso(ServerPath.ROOT_URL+pharma.getAvatar(),viewHolder.img_pharma);
        viewHolder.tv_name.setText(pharma.getName());
        viewHolder.tv_adr.setText(pharma.getAdr());
        viewHolder.like.setText(pharma.getLike());
        viewHolder.comment.setText(pharma.getComment());
        viewHolder.ln.removeAllViews();
        if(Common.lat!=0&&Common.lng!=0){
            Location location = new Location("");
            location.setLatitude(Common.lat);
            location.setLongitude(Common.lng);
            Location finishLocation = new Location("");
            finishLocation.setLatitude(pharma.getX());
            finishLocation.setLongitude( pharma.getY());

            float distance = location.distanceTo(finishLocation);
            if(distance>=1000){
                distance = distance/1000;
                if(distance>0){
                    int d = (int) Math.ceil(distance);

                    String around = d+"";

                    viewHolder.tv_distance.setText("Cách "+around+" km");
                }else{

                }
            }else{
                if(distance>0){
                    int d = (int) Math.ceil(distance);
                    String around = d+"";

                    viewHolder.tv_distance.setText("Cách "+around+" m");
                }
            }
        }
        Double d = pharma.getRate();
        if(d!=null){

            int s = Integer.valueOf(d.intValue());
            LayoutInflater vi = (LayoutInflater) Common.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            for(int i = 0; i<s;i++){
                View star = vi.inflate(R.layout.star, null);
                viewHolder.ln.addView(star, 0, new ViewGroup.LayoutParams(30, 30));
            }

        }
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

        public TextView tv_name, tv_adr, tv_distance, like, comment;
        public ImageView img_pharma;
        public   LinearLayout ln;
        public RecyclerViewHolder(View v) {
            super(v);

             img_pharma = v.findViewById(R.id.img_pharma);
             tv_name = v.findViewById(R.id.name_pharma);
             tv_adr = v.findViewById(R.id.adr_pharma);
             tv_distance = v.findViewById(R.id.distance_pharma);
             ln = v.findViewById(R.id.ln_star_pharma);
             ln.removeAllViews();
             like = v.findViewById(R.id.txt_like);
             comment = v.findViewById(R.id.txt_comment);

        }
    }
}