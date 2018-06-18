package app.pharma.com.pharma.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import app.pharma.com.pharma.Model.Common;
import app.pharma.com.pharma.Model.Constructor.Object.Rating_Obj;
import app.pharma.com.pharma.Model.Utils;
import app.pharma.com.pharma.R;

public class List_Rate_Adapter extends
        RecyclerView.Adapter<List_Rate_Adapter.RecyclerViewHolder> {

    List<Rating_Obj> list;

    private List<Rating_Obj> listData = new ArrayList<>();
    Context context;

    public List_Rate_Adapter(Context context, List<Rating_Obj> listData) {
        this.context = context;
        this.listData = listData;
    }

    public void updateData(ArrayList<Rating_Obj> arr) {
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
        View itemView = inflater.inflate(R.layout.item_rate, viewGroup, false);
        //     new ScaleInAnimation(itemView).animate();
        return new RecyclerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(List_Rate_Adapter.RecyclerViewHolder viewHolder, int position) {
        Rating_Obj rate = listData.get(position);
        viewHolder.ln_star.removeAllViews();
        Double star = rate.getStar();
        if(star!=null){

            int s = Integer.valueOf(star.intValue());
            LayoutInflater vi = (LayoutInflater) Common.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
// insert into main view
            for(int i = 0; i<s;i++){
                View stars = vi.inflate(R.layout.star, null);

                viewHolder.ln_star.addView(stars, 0, new ViewGroup.LayoutParams(30, 30));
            }

        }
        viewHolder.short_cmt.setText(rate.getShortComment());
        viewHolder.cmt.setText(rate.getComment());
        viewHolder.date.setText(Utils.convertTimestampToDate(rate.getTime()));
        viewHolder.name_rate.setText(rate.getUserName());

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

        public TextView name_rate, short_cmt, cmt, date;
        public ImageView img_dr;
        public LinearLayout ln_star;
        public RecyclerViewHolder(View v) {
            super(v);
             name_rate = v.findViewById(R.id.tv_name_rate);
             short_cmt = v.findViewById(R.id.tv_shortCmt);
             cmt = v.findViewById(R.id.tv_cmt_rate);
             date = v.findViewById(R.id.tv_date);
             ln_star = v.findViewById(R.id.ln_star_rate);
            ln_star.removeAllViews();
        }
    }
}