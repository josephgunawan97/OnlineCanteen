package com.example.asus.onlinecanteen.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.asus.onlinecanteen.R;
import com.example.asus.onlinecanteen.activity.WithdrawDetailActivity;
import com.example.asus.onlinecanteen.model.Withdraw;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rickhen Hermawan on 03/04/2018.
 */

public class WithdrawAdapter extends RecyclerView.Adapter<WithdrawAdapter.ViewHolder> {

    // Withdraw History Items
    private ArrayList<Withdraw> withdrawHistory;

    /**
     * Construct {@link WithdrawAdapter} instance
     *
     * @param withdraw list of withdraw history
     */
    public WithdrawAdapter(@NonNull List<Withdraw> withdraw) {
        this.withdrawHistory = new ArrayList<>(withdraw);
    }

    /**
     * Create {@link ViewHolder} instance of the views
     * @param parent ViewGroup instance in which the View is added to
     * @param viewType the view type of the new View
     * @return new ViewHolder instance
     */
    @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.withdraw_adapter, parent, false);

        return new ViewHolder(layoutView);
    }

    /**
     * Bind the view with data at the specified position
     * @param holder ViewHolder which should be updated
     * @param position position of items in the adapter
     */
    @Override public void onBindViewHolder(final ViewHolder holder, int position) {
        // Get withdraw Item
        final Withdraw withdraw = withdrawHistory.get(position);
        // Set Information on View
        //withdraw.setName("TEST");
        Log.i(WithdrawAdapter.class.getSimpleName(),"IF+ "+" "+ withdraw.getUid());

        FirebaseDatabase.getInstance().getReference("store").child(withdraw.getUid())
                .addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                // Log.i(MerchantOrderListFragment.class.getSimpleName(),"IF+ "+merchant.getDisplayName() +" "+ trans.getSid());

                if(dataSnapshot.child("storeName").getValue() != null) {
                    holder.NameTextView.setText(dataSnapshot.child("storeName").getValue().toString());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        holder.withdrawDateTextView.setText(withdraw.getRequestDateString(withdraw.getRequestdate()));
        holder.AmountTextView.setText("Rp " + String.valueOf(withdraw.getAmount()));

        if(withdraw.getRequeststatus()==1)
        {
            holder.bg.setBackgroundColor(Color.LTGRAY);
        }
        else
            holder.bg.setBackgroundColor(Color.WHITE);

    }
    /**
     * Retrieved the amount of items in adapter
     * @return amount of items in adapter
     */
    @Override public int getItemCount() {
        return withdrawHistory.size();
    }

    public void setwithdrawHistory(ArrayList<Withdraw> withdrawHistory) {
        this.withdrawHistory = withdrawHistory;
    }

    public void addWithdraw(Withdraw withdraw) {
        if(withdraw == null) return;
        this.withdrawHistory.add(withdraw);
        notifyDataSetChanged();
    }

    /**
     * ViewHolder class of {@link WithdrawAdapter}
     */
    public class ViewHolder extends RecyclerView.ViewHolder {

        // TextView of store name
        public TextView NameTextView;
        // TextView of withdraw date
        public TextView withdrawDateTextView;
        // TextView of payment amount
        public TextView AmountTextView;
        // TextView of LocationTextView

        LinearLayout bg;

        /**
         * Construct {@link ViewHolder} instance
         * @param view layout view of withdraw items
         */
        public ViewHolder(View view) {
            super(view);
            final Context context = itemView.getContext();
            // Set the holder attributes
            NameTextView = view.findViewById(R.id.username);
            withdrawDateTextView = view.findViewById(R.id.date);
            AmountTextView = view.findViewById(R.id.amount);
            bg = view.findViewById(R.id.bg_withdraw);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    Intent intent = new Intent(context, WithdrawDetailActivity.class);
                    intent.putExtra("withdraw",withdrawHistory);
                    intent.putExtra("Position", getAdapterPosition());
                    context.startActivity(intent);
                }
            });
        }
    }
}
