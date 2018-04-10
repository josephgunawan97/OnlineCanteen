package com.example.asus.onlinecanteen.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asus.onlinecanteen.R;
import com.example.asus.onlinecanteen.model.Store;
import com.example.asus.onlinecanteen.model.Withdraw;
import com.example.asus.onlinecanteen.utils.AccountUtil;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Rickhen Hermawan on 10/04/2018.
 */

public class ChangeDeliveryFee extends AppCompatActivity {
    private DatabaseReference database;
    private Button changefeeButton;
    private Store currentStore;
    private TextView deliveryfeeedittext;
    private String prevFee;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delivery_fee_change);
        deliveryfeeedittext = findViewById(R.id.deliveryFee);
        changefeeButton = findViewById(R.id.changeButton);
        currentStore = AccountUtil.getCurrentStore();
        populateFields();
        changefeeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!deliveryfeeedittext.getText().toString().equals("")){
                    if(!prevFee.equals(deliveryfeeedittext.getText().toString())){
                        changefee();
                        finish();
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Not Changed", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(), "Not Fill", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
    private void populateFields() {
        if(currentStore != null) {
            deliveryfeeedittext.setText(Integer.toString(currentStore.getDeliveryfee()));
            prevFee = deliveryfeeedittext.getText().toString();
        }
    }
    public void changefee(){
        Integer deliveryfee = Integer.parseInt (deliveryfeeedittext.getText().toString());
        currentStore.setDeliveryfee(deliveryfee);
        Log.d("Change Delivery Fee", "Current store: " + currentStore.getStoreId());
        FirebaseDatabase.getInstance().getReference("store").child(currentStore.getStoreId()).setValue(currentStore);
        finish();
        Toast.makeText(this, "Changed", Toast.LENGTH_SHORT).show();
        return;
    }


}
