package app.pharma.com.pharma.Adapter;

import android.content.Context;
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

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import app.pharma.com.pharma.Model.Common;
import app.pharma.com.pharma.Model.Constant;
import app.pharma.com.pharma.Model.Sick_Construct;
import app.pharma.com.pharma.Model.ServerPath;
import app.pharma.com.pharma.Model.Sick_Construct;
import app.pharma.com.pharma.Model.Utils;
import app.pharma.com.pharma.R;

/**
 * Created by Vi on 3/19/2018.
 */

public class List_Sick_Adapter extends
        RecyclerView.Adapter<List_Sick_Adapter.RecyclerViewHolder> {

    List<Sick_Construct> list;

    private List<Sick_Construct> listData = new ArrayList<>();
    Context context;

    public List_Sick_Adapter(Context context, List<Sick_Construct> listData) {
        this.context = context;
        this.listData = listData;
    }

    public void updateData(ArrayList<Sick_Construct> arr) {
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
        View itemView = inflater.inflate(R.layout.item_sick, viewGroup, false);
        //     new ScaleInAnimation(itemView).animate();
        return new RecyclerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(List_Sick_Adapter.RecyclerViewHolder viewHolder, int position) {
        Sick_Construct sick = listData.get(position);

        viewHolder.name_sick.setText(sick.getName());
        viewHolder.descri_sick.setText(sick.getDescri());
        viewHolder.like.setText(sick.getLike()+"");
        viewHolder.date.setText(Utils.convertTimestampToDate(sick.getDate()));
        viewHolder.catalo_txt.setText(sick.getCatalo());


        String firstImage = ServerPath.ROOT_URL+sick.getImage();
        Picasso.get().load(firstImage).into(viewHolder.img_sick);


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

        public TextView name_sick, descri_sick, like, comment, date, catalo_txt;
        public ImageView img_sick;
        public LinearLayout insertPoint;
        public RecyclerViewHolder(View v) {
            super(v);

             name_sick = v.findViewById(R.id.name_sick);
             descri_sick = v.findViewById(R.id.decrip_sick);
             like = v.findViewById(R.id.txt_like);
             comment = v.findViewById(R.id.txt_comment);
             date = v.findViewById(R.id.date_sick);
             catalo_txt = v.findViewById(R.id.cata_sick_text);
             img_sick = v.findViewById(R.id.img_sick);
        }
    }
}