package com.example.asus.onlinecanteen.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.asus.onlinecanteen.R;
import com.example.asus.onlinecanteen.activity.AdminSalesReportsListActivity;
import com.example.asus.onlinecanteen.activity.AdminSendsSalesReportActivity;
import com.example.asus.onlinecanteen.model.SalesReport;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ASUS on 3/27/2018.
 */

public class SalesReportAdapter extends RecyclerView.Adapter<SalesReportAdapter.ViewHolder> {
    // Sales Report History Items
    private ArrayList<SalesReport> salesReportHistory;

    public SalesReportAdapter(@NonNull List<SalesReport> salesReport) {
        this.salesReportHistory = new ArrayList<>(salesReport);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.sales_report_adapter, parent, false);

        return new ViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        // Get sales report
        final SalesReport salesReport = salesReportHistory.get(position);

        FirebaseDatabase.getInstance().getReference("store").child(salesReport.getUid()).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                holder.storeName.setText(dataSnapshot.child("storeName").getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        holder.dateReq.setText(salesReport.getRequestDateString(salesReport.getRequestdate()));
        if(salesReport.getRequeststatus().equals(0)) {
            holder.status.setText("Pending");
        }else{
            holder.status.setText("Completed");
            holder.sendButton.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return salesReportHistory.size();
    }

    public void setSalesReportHistory(ArrayList<SalesReport> salesReportHistory) {
        this.salesReportHistory = salesReportHistory;
    }

    public void addSalesReport(SalesReport salesReport) {
        if(salesReport == null) return;
        this.salesReportHistory.add(salesReport);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        //Initialize views
        public TextView storeName, dateReq, status;
        public Button sendButton;

        public ViewHolder(View view) {
            super(view);

            // Set the holder attributes
            storeName = view.findViewById(R.id.storename);
            dateReq = view.findViewById(R.id.date);
            status = view.findViewById(R.id.status);
            sendButton = view.findViewById(R.id.sendButton);

            sendButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AdminSendsSalesReportActivity sendReport = new AdminSendsSalesReportActivity();
                    sendReport.send();
                    notifyDataSetChanged();
                }
            });
        }
    }
}
