package com.example.asus.onlinecanteen.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.asus.onlinecanteen.R;
import com.example.asus.onlinecanteen.activity.MerchantOrderDetailActivity;
import com.example.asus.onlinecanteen.model.Product;
import com.example.asus.onlinecanteen.model.TopUp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class TopUpAdapter extends RecyclerView.Adapter<TopUpAdapter.ViewHolder> {

    // topup History Items
    private ArrayList<TopUp> topUpHistory;

    /**
     * Construct {@link TopUpAdapter} instance
     *
     * @param topup list of topup history
     */
    public TopUpAdapter(@NonNull List<TopUp> topup) {
        this.topUpHistory = new ArrayList<>(topup);
    }

    /**
     * Create {@link ViewHolder} instance of the views
     * @param parent ViewGroup instance in which the View is added to
     * @param viewType the view type of the new View
     * @return new ViewHolder instance
     */
    @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.top_up_adapter, parent, false);

        return new ViewHolder(layoutView);
    }

    /**
     * Bind the view with data at the specified position
     * @param holder ViewHolder which should be updated
     * @param position position of items in the adapter
     */
    @Override public void onBindViewHolder(final ViewHolder holder, int position) {
        // Get topup Item
        final TopUp topup = topUpHistory.get(position);
        // Set Information on View
        //topup.setName("TEST");
        Log.i(TopUpAdapter.class.getSimpleName(),"IF+ "+" "+ topup.getUid());

        FirebaseDatabase.getInstance().getReference("users").child(topup.getUid()).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                // Log.i(MerchantOrderListFragment.class.getSimpleName(),"IF+ "+merchant.getDisplayName() +" "+ trans.getSid());

                    holder.NameTextView.setText(dataSnapshot.child("name").getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        holder.topupDateTextView.setText(topup.getRequestDateString(topup.getRequestdate()));
        holder.AmountTextView.setText("Rp " + String.valueOf(topup.getAmount()));

    }
    /**
     * Retrieved the amount of items in adapter
     * @return amount of items in adapter
     */
    @Override public int getItemCount() {
        return topUpHistory.size();
    }

    public void settopupHistory(ArrayList<TopUp> topupHistory) {
        this.topUpHistory = topupHistory;
    }

    public void addTopUp(TopUp topUp) {
        if(topUp == null) return;
        this.topUpHistory.add(topUp);
        notifyDataSetChanged();
    }

    /**
     * ViewHolder class of {@link TopUpAdapter}
     */
    public class ViewHolder extends RecyclerView.ViewHolder {

        // TextView of store name
        public TextView NameTextView;
        // TextView of topup date
        public TextView topupDateTextView;
        // TextView of payment amount
        public TextView AmountTextView;
        // TextView of LocationTextView

        /**
         * Construct {@link ViewHolder} instance
         * @param view layout view of topup items
         */
        public ViewHolder(View view) {
            super(view);
            final Context context = itemView.getContext();
            // Set the holder attributes
            NameTextView = view.findViewById(R.id.username);
            topupDateTextView = view.findViewById(R.id.date);
            AmountTextView = view.findViewById(R.id.amount);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                   /* Intent intent = new Intent(context, TopUpDetailActivity.class);
                    intent.putExtra("topup",topUpHistory);
                    intent.putExtra("Position", getAdapterPosition());
                    context.startActivity(intent);*/
                }
            });
        }
    }
}
