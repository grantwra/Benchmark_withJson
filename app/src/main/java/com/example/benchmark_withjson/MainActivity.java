package com.example.benchmark_withjson;


import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;



public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

    }
}
