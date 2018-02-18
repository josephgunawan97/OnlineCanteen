package com.example.asus.onlinecanteen.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.asus.onlinecanteen.R;
import com.example.asus.onlinecanteen.adapter.TransactionHistoryAdapter;
import com.example.asus.onlinecanteen.model.Product;
import com.example.asus.onlinecanteen.model.PurchasedItem;
import com.example.asus.onlinecanteen.model.Transaction;

import java.util.ArrayList;

/**
 * Created by Steven Albert on 10/02/2018.
 */

public class HistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        setTitle(R.string.history_title_item);

        ArrayList<Transaction> transactions = getDummyTransactions();
        TransactionHistoryAdapter adapter = new TransactionHistoryAdapter(transactions);

        recyclerView = findViewById(R.id.history_recycler_view);
        layoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    private ArrayList<Transaction> getDummyTransactions() {
        ArrayList<PurchasedItem> items = new ArrayList<>();
        items.add(new PurchasedItem(new Product("A", "Aqua", 10, 3000, null), 5));
        items.add(new PurchasedItem(new Product("A", "Oreo", 20, 2000, null), 10));
        ArrayList<Transaction> transactions = new ArrayList<>();
        for(int i=0; i<10; i++) {
            transactions.add(new Transaction("Toko " + ((char) (i + 'A')), "User X", items));
        }

        return transactions;
    }
}
