package com.example.asus.onlinecanteen.fragment;


import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.asus.onlinecanteen.R;
import com.example.asus.onlinecanteen.adapter.TransactionDetailAdapter;
import com.example.asus.onlinecanteen.model.Transaction;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

/**
 * A simple {@link Fragment} subclass.
 */
public class TransactionDetailFragment extends Fragment {

    // Transaction which is detailed
    private Transaction transaction;
    private String transactionId;

    // UI Variables
    private TextView transactionDateTextView;
    private TextView storeNameTextView;
    private RecyclerView itemsRecyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private TextView totalAmountTextView;
    private ImageButton QR;

    public TransactionDetailFragment() {}

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_transaction_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        transactionDateTextView = view.findViewById(R.id.transaction_detail_transaction_date);
        storeNameTextView = view.findViewById(R.id.transaction_detail_store_name);
        itemsRecyclerView = view.findViewById(R.id.transaction_detail_items);
        totalAmountTextView = view.findViewById(R.id.transaction_detail_amount);

        transactionDateTextView.setText(Transaction.getPurchasedDateString(transaction.getPurchaseDate()));
        FirebaseDatabase.getInstance().getReference().child("store").child(transaction.getSid()).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                storeNameTextView.setText(dataSnapshot.child("storeName").getValue().toString());
                // Log.i(MerchantOrderListFragment.class.getSimpleName(),"IF+ "+merchant.getDisplayName() +" "+ trans.getSid());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        totalAmountTextView.setText("Rp " + String.valueOf(transaction.getTotalPrice()));

        TransactionDetailAdapter detailAdapter = new TransactionDetailAdapter(transaction.getItems());
        layoutManager = new LinearLayoutManager(getContext());

        itemsRecyclerView.setLayoutManager(layoutManager);
        itemsRecyclerView.setAdapter(detailAdapter);

        QR = (ImageButton) view.findViewById(R.id.qr_button);
        QR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            showQR();
            }
        });
    }

    private void showQR() {
        Log.d("Transaction", "id: " + transactionId);
        if(transactionId == null) {
            retrieveTransactionId();
            return;
        }

        Bitmap bitmap;
        // TODO Auto-generated method stub
        if(getActivity() == null) return;
        AlertDialog.Builder alertadd = new AlertDialog.Builder(getActivity());
        alertadd.setTitle("QR Code");

        LayoutInflater factory = LayoutInflater.from(getActivity());
        final View view = factory.inflate(R.layout.qr_layout, null);
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(transactionId, BarcodeFormat.QR_CODE,300,300);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            bitmap = barcodeEncoder.createBitmap(bitMatrix);
            ImageView image= (ImageView) view.findViewById(R.id.imageView);
            image.setImageBitmap(bitmap);
        }  catch (WriterException e) {
            e.printStackTrace();
        }

        alertadd.setView(view);
        alertadd.setNeutralButton("Close", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dlg, int sumthin) {
                dlg.cancel();
            }

        });
        alertadd.show();
    }

    private void retrieveTransactionId() {
        FirebaseDatabase.getInstance().getReference().child("transactions").orderByChild("uid").equalTo(transaction.getUid()).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i(TransactionDetailFragment.class.getSimpleName() , " ADD12 "+ dataSnapshot.getValue() );
                for(DataSnapshot productSnapshot : dataSnapshot.getChildren())
                {
                    if(productSnapshot.child("sid").getValue().equals(transaction.getSid()) && productSnapshot.child("purchaseDate").getValue().equals(transaction.getPurchaseDate()))
                    {
                        transactionId = productSnapshot.getKey();
                        Log.d("Transaction retrieve", "id: " + transactionId);
                        showQR();
                        return;
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
