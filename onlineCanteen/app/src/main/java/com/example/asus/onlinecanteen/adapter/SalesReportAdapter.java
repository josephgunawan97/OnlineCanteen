package com.example.asus.onlinecanteen.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.asus.onlinecanteen.R;
import com.example.asus.onlinecanteen.model.SalesReport;
import com.example.asus.onlinecanteen.model.Transaction;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Month;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import javax.security.auth.callback.Callback;

/**
 * Created by ASUS on 3/27/2018.
 */

public class SalesReportAdapter extends RecyclerView.Adapter<SalesReportAdapter.ViewHolder> {
    // Sales Report History Items
    private ArrayList<SalesReport> salesReportHistory;
    ArrayList<Transaction> transactions = new ArrayList<>();
    String previousMonth;
    int earnings = 0;
    HashMap<String, HashMap<String, Integer>> items = new HashMap<>();
    HashMap<String, HashMap<String, Integer>> totalItems = new HashMap<>();
    String listItems = "";
    int totalQuantity;

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
        holder.date = salesReport.getRequestdate();
        holder.uid = salesReport.getUid();
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
        public String uid;
        public Long date;

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
                    updateStatus(date);

                    //Get month
                    SimpleDateFormat format = new SimpleDateFormat("MMMM yyyy");
                    Calendar c = Calendar.getInstance();
                    c.add(Calendar.MONTH, -1);
                    String monthYear = format.format(c.getTime());

                    //Get previous month
                    SimpleDateFormat format2 = new SimpleDateFormat("MM");
                    Calendar cal = Calendar.getInstance();
                    cal.add(Calendar.MONTH, -1);
                    previousMonth = format2.format(cal.getTime());

                    makeCSV(context, uid, monthYear, storeName.getText().toString(), storeEmail);
                }
            });
        }
    }

    public void updateStatus(Long date){
        //Update status
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("salesreportrequest");

        Query query = ref.orderByChild("requestdate").equalTo(date);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot child : snapshot.getChildren()) {
                    child.getRef().child("requeststatus").setValue(1);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void makeCSV(final Context context, String sid, final String monthYear, final String storeName, final String storeEmail){
        //Get data
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("transactions");

        Query query = ref.orderByChild("sid").equalTo(sid);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot child : snapshot.getChildren()) {
                    Transaction newTransaction = child.getValue(Transaction.class);
                    if (newTransaction.getDeliveryStatus() == 3) {
                        SimpleDateFormat sdf = new SimpleDateFormat("MM");
                        String monthTransaction = sdf.format(newTransaction.getPurchaseDate());
                        if (monthTransaction.equals(previousMonth)) {
                            transactions.add(newTransaction);
                            earnings += newTransaction.getTotalPrice();

                            //Get items
                            HashMap<String, Integer> desc;
                            items = newTransaction.getItems();
                            Object[] keys = items.keySet().toArray();
                            for(Object key : keys) {
                                desc = items.get(key);
                                if(totalItems.containsKey(key)){
                                    desc.put("quantity", desc.get("quantity") + totalItems.get(key).get("quantity"));
                                    totalItems.put(key.toString(), desc);
                                }else{
                                    totalItems.put(key.toString(), desc);
                                }
                            }
                        }
                    }
                }

                //Get items' description
                Object[] keys = totalItems.keySet().toArray();
                for(Object key : keys){
                    String itemName = key.toString();
                    int quantity = totalItems.get(key).get("quantity");
                    int unitPrice = totalItems.get(key).get("unit_price");
                    int totalPrice = quantity*unitPrice;
                    listItems = listItems.concat(itemName).concat(",").concat(String.valueOf(quantity)).concat(",").concat("Rp ")
                            .concat(String.valueOf(unitPrice)).concat(",").concat("Rp ").concat(String.valueOf(totalPrice)).concat("\n");
                    totalQuantity += quantity;
                }

                //Construct the csv
                String sitnshop =   "\"Sit 'n Shop\"";
                String store   =   "\"Store Name : \",\"" + storeName +"\"";
                String month = "\"Sales report for : \",\"" + monthYear +"\"";
                String successfulTransaction = "\"Successful transactions : \",\"" + transactions.size() + " transaction(s)" +"\"";
                String totalTypeItems = "\"Total type of items sold : \",\"" + totalItems.size() + " type of item(s)" +"\"";
                String totalSoldItems = "\"Total items sold : \",\"" + totalQuantity + " item(s)" +"\"";
                String soldItems = "\"Sold item(s) and quantity : \"";
                String itemsTitles = "\"Item Name\",\"Quantity Sold\",\"Unit Price\",\"Quantity Sold x Unit Price\"";
                String totalEarnings = "\"Total earnings : \",\"" + "Rp " + earnings +",-"+"\"";
                String thankyou = "\"Thank you for using Sit 'n Shop!\"";
                String combinedString = sitnshop + "\n" + store + "\n" + month + "\n" + successfulTransaction + "\n"+ totalTypeItems + "\n"+ totalSoldItems + "\n\n" + soldItems
                        + "\n" + itemsTitles  + "\n" + listItems  + "\n" + totalEarnings + "\n\n\n" + thankyou;

                //Make the csv file
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
                    Uri u1 = null;
                    u1 = Uri.fromFile(file);
                    String [] to = {storeEmail};

                    //Bring admin to Gmail/Yahoo to send the email
                    Intent sendIntent = new Intent(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Sit 'n Shop Sales Report : " + monthYear);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, "Hello, "+ storeName + "!\nThis email is a response to your request for a sales report."+
                            "\nIn this email, we have attached the sales report for " + monthYear
                            + ".\nThank you for using Sit 'n Shop!\n\n\nBest regards,\nSit 'n Shop\nsitnshop@gmail.com");
                    sendIntent.putExtra(Intent.EXTRA_EMAIL, to);
                    sendIntent.putExtra(Intent.EXTRA_STREAM, u1);
                    sendIntent.setType("text/html");
                    context.startActivity(sendIntent);
                    ((Activity)context).finish();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}