package com.example.asus.onlinecanteen;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MenuListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] itemname;
    private final Integer[] imgid;

    public MenuListAdapter(Activity context, String[] itemname, Integer[] imgid) {
        super(context, R.layout.list, itemname);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.itemname=itemname;
        this.imgid=imgid;
    }

    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.list, null,true);

        TextView txtTitle = rowView.findViewById(R.id.itemName);
        ImageView imageView = rowView.findViewById(R.id.icon);
        TextView extratxt = rowView.findViewById(R.id.price);
        TextView seller = rowView.findViewById(R.id.seller);

        txtTitle.setText(itemname[position]);
        //imageView.setImageResource(imgid[position]);
        extratxt.setText("Rp ");
        seller.setText("Seller");
        return rowView;

    };
}