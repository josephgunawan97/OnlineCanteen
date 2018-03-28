package com.example.asus.onlinecanteen.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
                holder.storeEmail = dataSnapshot.child("email").getValue().toString();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        holder.dateReq.setText(salesReport.getRequestDateString(salesReport.getRequestdate()));
        if (salesReport.getRequeststatus().equals(0)) {
            holder.status.setText("Pending");
        } else {
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
        if (salesReport == null) return;
        this.salesReportHistory.add(salesReport);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        //Initialize views
        public TextView storeName, dateReq, status;
        public Button sendButton;
        public String storeEmail;

        public ViewHolder(View view) {
            super(view);
            final Context context = itemView.getContext();

            // Set the holder attributes
            storeName = view.findViewById(R.id.storename);
            dateReq = view.findViewById(R.id.date);
            status = view.findViewById(R.id.status);
            sendButton = view.findViewById(R.id.sendButton);

            sendButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String columnString = "\"Store Name\"";
                    String dataString = "\"" + storeName.getText().toString() + "\"";
                    String combinedString = columnString + "\n" + dataString;

                    SimpleDateFormat format = new SimpleDateFormat("MMMM yyyy");
                    Calendar c = Calendar.getInstance();
                    c.add(Calendar.MONTH, -1);
                    String monthYear = format.format(c.getTime());

                    File file = null;
                    File root = Environment.getExternalStorageDirectory();
                    if (root.canWrite()) {
                        File dir = new File(root.getAbsolutePath() + "/SalesReport");
                        dir.mkdirs();
                        file = new File(dir, "Sales Report (" + monthYear + ").csv");
                        FileOutputStream out = null;
                        try {
                            out = new FileOutputStream(file);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        try {
                            out.write(combinedString.getBytes());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            out.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    Uri u1 = null;
                    u1 = Uri.fromFile(file);
                    String [] to = {storeEmail};

                    Intent sendIntent = new Intent(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Sit 'n Shop Sales Report : " + monthYear);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, "Hello, "+ storeName.getText().toString() + "!\nThis is your sales report for " + monthYear +
                            ".\nThank you for using Sit 'n Shop!\n\n\nBest regards,\nSit 'n Shop\nsitnshop@gmail.com");
                    sendIntent.putExtra(Intent.EXTRA_EMAIL, to);
                    sendIntent.putExtra(Intent.EXTRA_STREAM, u1);
                    sendIntent.setType("text/html");
                    context.startActivity(sendIntent);
                }
            });
        }
    }
}
