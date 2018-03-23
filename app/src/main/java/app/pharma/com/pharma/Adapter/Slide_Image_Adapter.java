package app.pharma.com.pharma.Adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import app.pharma.com.pharma.Model.Common;
import app.pharma.com.pharma.R;

/**
 * Created by Vi on 3/24/2018.
 */

public class Slide_Image_Adapter extends PagerAdapter {
    Context context;
   ArrayList<String> images;
    LayoutInflater layoutInflater;


    public Slide_Image_Adapter(Context context, ArrayList<String> images) {
        this.context = context;
        this.images = images;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return images.size();
    }



    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View itemView = layoutInflater.inflate(R.layout.image_slide, container, false);

        ImageView imageView = (ImageView) itemView.findViewById(R.id.image_lide);
        Picasso.with(Common.context).load(R.drawable.img_avt).into(imageView);
        Log.d("LINKK",images.get(position));
        container.addView(itemView);

        //listening to image click
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "you clicked image " + (position + 1), Toast.LENGTH_LONG).show();
            }
        });

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return false;
    }
}