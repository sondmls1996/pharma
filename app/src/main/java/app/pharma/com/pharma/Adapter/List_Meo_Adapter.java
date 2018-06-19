package app.pharma.com.pharma.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import app.pharma.com.pharma.Model.Constructor.Meo_Constructor;
import app.pharma.com.pharma.Model.ServerPath;
import app.pharma.com.pharma.Model.Utils;
import app.pharma.com.pharma.R;

/**
 * Created by Vi on 3/19/2018.
 */

public class List_Meo_Adapter 	extends
        RecyclerView.Adapter<List_Meo_Adapter.RecyclerViewHolder> {
    SimpleDateFormat format,formatHours;
    List<Meo_Constructor> list;

    private List<Meo_Constructor> listData = new ArrayList<>();
    Context context;

    public List_Meo_Adapter(Context context, List<Meo_Constructor> listData) {
        formatHours = new SimpleDateFormat();
        this.context = context;
        this.listData = listData;
    }

    public void updateData(ArrayList<Meo_Constructor> arr) {
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
        View itemView = inflater.inflate(R.layout.item_meo, viewGroup, false);
        //     new ScaleInAnimation(itemView).animate();
        return new RecyclerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(List_Meo_Adapter.RecyclerViewHolder viewHolder, int position) {
        Meo_Constructor pill = listData.get(position);

        Utils.loadImagePicasso(ServerPath.ROOT_URL+pill.getImage(),viewHolder.img);
        viewHolder.tv_title.setText(pill.getTitle());
        viewHolder.descr.setText(pill.getDescrep());
        viewHolder.date.setText(Utils.convertTimestampToDate(pill.getDate()));
        viewHolder.time_meo.setText(formatHours.format(pill.getDate()));
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

        public TextView tv_title, descr, date, time_meo;
        public ImageView img;
        public LinearLayout insertPoint;
        public RecyclerViewHolder(View v) {
            super(v);
            img = v.findViewById(R.id.img_meo);
            tv_title = v.findViewById(R.id.title_meo);
            descr = v.findViewById(R.id.descr_meo);
            date = v.findViewById(R.id.date_meo);
            time_meo = v.findViewById(R.id.time_meo);
        }
    }
}