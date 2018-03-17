package com.example.asus.onlinecanteen.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.asus.onlinecanteen.R;

/**
 * Created by ASUS on 3/17/2018.
 */

public class AdminMenuAdapter extends BaseAdapter {

        Context context;
        private final String [] values;
        private final String [] numbers;
        private final int [] images;
        private final String [] TAG;

        public AdminMenuAdapter(Context context, String[] values, String[] numbers, int[] images, String[] tag){
            //super(context, R.layout.single_list_app_item, utilsArrayList);
            this.context = context;
            this.values = values;
            this.numbers = numbers;
            this.images = images;
            this.TAG = tag;
        }

        @Override
        public int getCount() {
            return values.length;
        }

        @Override
        public Object getItem(int i) {
            return i;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }


        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


            ViewHolder viewHolder;

            final View result;


            if (convertView == null) {

                viewHolder = new ViewHolder();
                LayoutInflater inflater = LayoutInflater.from(context);
                convertView = inflater.inflate(R.layout.admin_menu, parent, false);
                viewHolder.txtName = (TextView) convertView.findViewById(R.id.aNametxt);
                viewHolder.txtVersion = (TextView) convertView.findViewById(R.id.aVersiontxt);
                viewHolder.icon = (ImageView) convertView.findViewById(R.id.menuIcon);

                result=convertView;

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
                result=convertView;
            }

            viewHolder.txtName.setText(values[position]);
            viewHolder.txtVersion.setText(numbers[position]);
            viewHolder.icon.setImageResource(images[position]);

            return convertView;
        }

        private static class ViewHolder {

            TextView txtName;
            TextView txtVersion;
            ImageView icon;

        }

}
