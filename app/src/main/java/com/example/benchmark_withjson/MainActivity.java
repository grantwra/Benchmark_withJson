package com.example.benchmark_withjson;


import android.annotation.SuppressLint;
import android.content.Context;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        long start = System.currentTimeMillis();

        @SuppressLint("SdCardPath") String path = "/data/data/com.example.benchmark_withjson/files/workload_A_timing_A.json";

        CreateDB createDB = new CreateDB(this);
        int tester = createDB.create(path);
        if(tester != 0){
            this.finishAffinity();
        }
        Queries queries = new Queries(this);
        tester = queries.startQueries();
        if (tester != 0){
            this.finishAffinity();
        }

        //below method to find what queries are missing from SQL
        Utils utils = new Utils();
        int tester2 = utils.findMissingQueries(this);
        if(tester2 != 0){
            this.finishAffinity();
        }

        long end = System.currentTimeMillis();
        long delta = end - start;
        double elapsedSeconds = delta / 1000.0;

        File file = new File("/data/data/com.example.benchmark_withjson/files/time");
        FileOutputStream fos = null;
        try {
            fos = this.openFileOutput(file.getName(), Context.MODE_APPEND);
            fos.write((elapsedSeconds + "\n").getBytes());
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
