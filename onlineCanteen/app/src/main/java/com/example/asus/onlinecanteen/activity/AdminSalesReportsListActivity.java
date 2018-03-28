package com.example.asus.onlinecanteen.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.asus.onlinecanteen.R;
import com.example.asus.onlinecanteen.adapter.SalesReportAdapter;
import com.example.asus.onlinecanteen.model.SalesReport;
import com.example.asus.onlinecanteen.utils.SalesReportUtil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class AdminSalesReportsListActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    DatabaseReference emailDatabase;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<SalesReport> reportReqs;
    private SalesReportAdapter adapter;
    private ChildEventListener EventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_sales_reports_list);

        getSupportActionBar().setTitle("Send Sales Report");
        reportReqs = new ArrayList<SalesReport>();
        adapter = new SalesReportAdapter(reportReqs);
        recyclerView = findViewById(R.id.list);
        layoutManager = new LinearLayoutManager(this);

        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onResume() {
        super.onResume();
        attachDatabaseReadListener();
    }

    @Override
    public void onPause() {
        super.onPause();
        detachDatabaseReadListener();
    }
    private void attachDatabaseReadListener() {
        if (EventListener == null) {
            EventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    SalesReport salesReport = dataSnapshot.getValue(SalesReport.class);
                    adapter.addSalesReport(salesReport);
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {}

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {}

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {}

                @Override
                public void onCancelled(DatabaseError databaseError) {}
            };

            SalesReportUtil.query().addChildEventListener(EventListener);
        }
    }

    private void detachDatabaseReadListener() {
        if(reportReqs != null) {
            reportReqs = null;
        }
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this,MainActivityAdmin.class);
        startActivity(intent);
        finish();
    }
}
