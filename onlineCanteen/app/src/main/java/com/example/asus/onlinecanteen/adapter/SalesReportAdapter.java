package com.example.asus.onlinecanteen.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.time.Month;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import javax.security.auth.callback.Callback;

import static com.itextpdf.text.Annotation.FILE;
import static com.itextpdf.text.Element.ALIGN_CENTER;

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
    private static Font Font18 = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.NORMAL);
    private static Font Font16 = new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.NORMAL);
    private static Font Font12 = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL);

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
                    cal.add(Calendar.MONTH,-1);
                    previousMonth = format2.format(cal.getTime());

                    makePDF(context, uid, monthYear, storeName.getText().toString(), storeEmail);
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

    public void makePDF(final Context context, String sid, final String monthYear, final String storeName, final String storeEmail){
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

                //Make the pdf file
                File file = null;
                File root = Environment.getExternalStorageDirectory();
                if (root.canWrite()) {
                    File dir = new File(root.getAbsolutePath() + "/SalesReport");
                    dir.mkdirs();
                    file = new File(dir, "Sales Report (" + monthYear + ").pdf");
                    FileOutputStream out = null;
                    try {
                        out = new FileOutputStream(file);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    catch(Exception e)
                    {
                        return;
                    }
                    try {
                        Document document = new Document(PageSize.A4);
                        PdfWriter.getInstance(document, new FileOutputStream(file));
                        document.open();
                        addContent(document, context, storeName, monthYear);
                        document.close();
                    } catch (Exception e) {
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
                    sendIntent.setType("application/pdf");
                    context.startActivity(sendIntent);
                    ((Activity)context).finish();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void addContent(Document document, Context context, String storeName, String monthYear)
            throws DocumentException {

        //Make table
        PdfPTable table = new PdfPTable(4);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell("Item Name");
        table.addCell("Quantity Sold");
        table.addCell("Unit Price");
        table.addCell("Total");
        table.setHeaderRows(1);

        //Get items' description
        Object[] keys = totalItems.keySet().toArray();
        for(Object key : keys){
            String itemName = key.toString();
            int quantity = totalItems.get(key).get("quantity");
            int unitPrice = totalItems.get(key).get("unit_price");
            int totalPrice = quantity*unitPrice;
            totalQuantity += quantity;

            //Add to table
            table.addCell(itemName);
            table.addCell(String.valueOf(quantity));
            table.addCell("Rp " + String.valueOf(unitPrice) + ",-");
            table.addCell("Rp " + String.valueOf(totalPrice) + ",-");
        }

        //Add logo to the top of document
        try{
            Drawable d = context.getResources().getDrawable(R.drawable.logo2);
            BitmapDrawable bitDw = ((BitmapDrawable) d);
            Bitmap bmp = bitDw.getBitmap();
            Bitmap resized = Bitmap.createScaledBitmap(bmp,(int)(bmp.getWidth()*0.2), (int)(bmp.getHeight()*0.2), true);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            resized.compress(Bitmap.CompressFormat.PNG, 100, stream);
            Image image = Image.getInstance(stream.toByteArray());
            image.setAlignment(ALIGN_CENTER);
            document.add(image);
        } catch(Exception e){
            e.printStackTrace();
        }

        //Set content
        Paragraph preface = new Paragraph();
        addEmptyLine(preface, 2);
        preface.add(new Paragraph(storeName + "'s Sales Report for " + monthYear, Font18));
        addEmptyLine(preface, 1);
        preface.add(new Paragraph("Successful transactions : " + transactions.size() + " transaction(s)", Font16));
        preface.add(new Paragraph("Total type of items sold : " + totalItems.size() + " type of items(s)", Font16));
        preface.add(new Paragraph("Total items sold : " + totalQuantity + " item(s)", Font16));
        preface.add(new Paragraph("Total earnings : Rp " + earnings + ",-" , Font16));
        addEmptyLine(preface, 1);
        preface.add(new Paragraph("Sold item(s) and quantity :", Font16));
        addEmptyLine(preface, 1);

        //Footer document
        Paragraph footer = new Paragraph("\n\n\nThis is a computer generated document. No signature is required.", Font12);
        footer.setAlignment(Element.ALIGN_CENTER);

        //Add to document
        document.add(preface);
        document.add(table);
        document.add(footer);
    }

    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }
}