package com.example.benchmark_withjson;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final int workload_a_timing_a = R.raw.workload_a_timing_a;
        final int workload_b_timing_a = R.raw.workload_b_timing_a;
        final int workload_c_timing_a = R.raw.workload_c_timing_a;
        final int workload_d_timing_a = R.raw.workload_d_timing_a;
        final int workload_e_timing_a = R.raw.workload_e_timing_a;
        final int workload_f_timing_a = R.raw.workload_f_timing_a;

        long start = System.currentTimeMillis();

        //Create the databases from the JSON
        CreateDB createDB = new CreateDB(this);
        int tester = createDB.create(workload_c_timing_a);
        if(tester != 0){
            this.finishAffinity();
        }

        //Run the queries specified in the JSON on the newly created databases
        Queries queries = new Queries(this);
        tester = queries.startQueries();
        if (tester != 0){
            this.finishAffinity();
        }

        //Find what queries were not executed successfully in the SQL or BDB traces
        Utils utils = new Utils();
        int tester2 = utils.findMissingQueries(this);
        if(tester2 != 0){
            this.finishAffinity();
        }

        //Calculate total time of the traces
        long end = System.currentTimeMillis();

        long delta = end - start;
        double elapsedSeconds = delta / 1000.0;


        File file = new File(this.getFilesDir().getPath() + "/time");
        FileOutputStream fos;
        try {
            fos = this.openFileOutput(file.getName(), Context.MODE_APPEND);
            fos.write((elapsedSeconds + "\n").getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //this.finishAffinity();

    }
}
