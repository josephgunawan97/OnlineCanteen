package com.example.asus.onlinecanteen.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.asus.onlinecanteen.model.SalesReport;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class AdminSendsSalesReportActivity extends AppCompatActivity {

    SalesReport report;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void send(ArrayList<SalesReport> salesReport, int position, String storeName){
        report = salesReport.get(position);

        String columnString =   "\"Store Name\"";
        String dataString   =   "\"" + storeName +"\"";
        String combinedString = columnString + "\n" + dataString;

        File file   = null;
        File root   = Environment.getExternalStorageDirectory();
        if (root.canWrite()){
            File dir    =   new File (root.getAbsolutePath() + "/SalesReport");
            dir.mkdirs();
            file   =   new File(dir, "SalesReport.csv");
            FileOutputStream out   =   null;
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
        Uri u1  =   null;
        u1  =   Uri.fromFile(file);

        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Sit 'n Shop Sales Report");
        sendIntent.putExtra(Intent.EXTRA_TEXT, "TEST");
        sendIntent.putExtra(Intent.EXTRA_STREAM, u1);
        sendIntent.setType("text/html");
        startActivity(sendIntent);
    }
}

