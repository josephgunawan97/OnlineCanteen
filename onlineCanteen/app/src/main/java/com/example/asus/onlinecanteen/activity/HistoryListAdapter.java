package com.example.asus.onlinecanteen.activity;

import android.app.Activity;
import android.widget.ArrayAdapter;

import com.example.asus.onlinecanteen.R;
import com.example.asus.onlinecanteen.model.Product;

import java.util.List;

/**
 * Created by Rickhen Hermawan on 14/02/2018.
 */

public class HistoryListAdapter extends ArrayAdapter<Product> {

    public HistoryListAdapter(Activity context, List<Product> products) {
        super(context, R.layout.history_adapter_list, products);
    }
}

