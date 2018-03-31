package com.example.asus.onlinecanteen.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.asus.onlinecanteen.R;
import com.example.asus.onlinecanteen.model.Store;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;


public class AdminVerifyStoreAdapter extends RecyclerView.Adapter<AdminVerifyStoreAdapter.ViewHolder> {

    DatabaseReference storeDatabase;
    // topup History Items
    private ArrayList<Store> storeList;

    /**
     * Construct {@link AdminVerifyStoreAdapter} instance
     *
     * @param stores list of topup history
     */
    public AdminVerifyStoreAdapter(@NonNull List<Store> stores) {
        this.storeList = new ArrayList<>(stores);
    }

    /**
     * Create {@link ViewHolder} instance of the views
     * @param parent ViewGroup instance in which the View is added to
     * @param viewType the view type of the new View
     * @return new ViewHolder instance
     */
    @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_verify_store_adapter, parent, false);

        return new ViewHolder(layoutView);
    }

    /**
     * Bind the view with data at the specified position
     * @param holder ViewHolder which should be updated
     * @param position position of items in the adapter
     */
    @Override public void onBindViewHolder(final ViewHolder holder, final int position) {
        // Get topup Item
        final Store store = storeList.get(position);

        holder.NameTextView.setText(store.getStoreName());
        holder.emailET.setText(store.getEmail());
        holder.locationET.setText(store.getLocation());

        holder.verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = store.getEmail().replaceAll(Pattern.quote("."),",");

                storeDatabase = FirebaseDatabase.getInstance().getReference();

                storeDatabase.child("emailtouid").child(email).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            String id = dataSnapshot.getValue().toString();
                            storeDatabase.child("role").child(id).setValue("STORE");
                            storeList.remove(position);

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

        });



    }
    /**
     * Retrieved the amount of items in adapter
     * @return amount of items in adapter
     */
    @Override public int getItemCount() {
        return storeList.size();
    }

    public void settopupHistory(ArrayList<Store> storeList) {
        this.storeList = storeList;
    }

    public void addTopUp(String storeUid) {
        FirebaseDatabase.getInstance().getReference("store").child(storeUid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Store store = dataSnapshot.getValue(Store.class);
                storeList.add(store);
                notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**
     * ViewHolder class of {@link AdminVerifyStoreAdapter}
     */
    public class ViewHolder extends RecyclerView.ViewHolder {

        // TextView of store name
        public TextView NameTextView, emailET, locationET;
        public Button verifyButton;


        LinearLayout bg;

        /**
         * Construct {@link ViewHolder} instance
         * @param view layout view of topup items
         */
        public ViewHolder(View view) {
            super(view);
            final Context context = itemView.getContext();
            // Set the holder attributes
            NameTextView = view.findViewById(R.id.adminverifystore_store_name);
            emailET = view.findViewById(R.id.adminverifystore_store_email);
            locationET = view.findViewById(R.id.adminverifystore_location);
            bg = view.findViewById(R.id.bg_admin_verify_store);

            verifyButton = view.findViewById(R.id.adminverifystore_button);




            /*itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    Intent intent = new Intent(context, TopUpDetailActivity.class);
                    intent.putExtra("store",storeList);
                    intent.putExtra("Position", getAdapterPosition());
                    context.startActivity(intent);
                }
            });
            */
        }
    }
}
