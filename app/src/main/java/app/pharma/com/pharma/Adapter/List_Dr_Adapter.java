package app.pharma.com.pharma.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.ads.formats.NativeAd;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import app.pharma.com.pharma.Model.Constructor.Dr_Constructor;
import app.pharma.com.pharma.Model.ServerPath;
import app.pharma.com.pharma.Model.Utils;
import app.pharma.com.pharma.R;

/**
 * Created by Vi on 3/19/2018.
 */

public class List_Dr_Adapter extends
        RecyclerView.Adapter<List_Dr_Adapter.RecyclerViewHolder> {

        List<Dr_Constructor> list;

private List<Dr_Constructor> listData = new ArrayList<>();
        Context context;

public List_Dr_Adapter(Context context, List<Dr_Constructor> listData) {
        this.context = context;
        this.listData = listData;
        }

public void updateData(ArrayList<Dr_Constructor> arr) {
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
        View itemView = inflater.inflate(R.layout.item_dr, viewGroup, false);
        //     new ScaleInAnimation(itemView).animate();
        return new RecyclerViewHolder(itemView);
        }

@Override
public void onBindViewHolder(RecyclerViewHolder viewHolder, int position) {
        Dr_Constructor dr = listData.get(position);
    Utils.loadImagePicasso(ServerPath.ROOT_URL+dr.getAvatar(),viewHolder.img_dr);
    //Picasso.with(getContext()).load(ServerPath.ROOT_URL+dr.getAvatar()).into(img_dr);
    viewHolder.name_dr.setText(dr.getName());
    viewHolder.age_dr.setText(dr.getAge()+" tuổi");
    viewHolder.work_year.setText(dr.getWork_year() +" công tác");


    viewHolder.ck.setText(dr.getHospital());
 //   viewHolder.hospital.setText(main[1]);

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

    public TextView name_dr, age_dr, work_year, ck, hospital;
    public ImageView img_dr;
    public LinearLayout insertPoint;
    public RecyclerViewHolder(View v) {
        super(v);
         img_dr = v.findViewById(R.id.img_dr);
         name_dr = v.findViewById(R.id.text_name_dr);
         age_dr = v.findViewById(R.id.tv_age_dr);
         work_year = v.findViewById(R.id.tv_work_dr_age);
         ck = v.findViewById(R.id.tv_dr_ck);
        // hospital = v.findViewById(R.id.tv_dr_hospital);
    }
}
}