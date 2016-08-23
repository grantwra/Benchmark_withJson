package com.example.benchmark_withjson;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Queries {

    JSONObject workloadJsonObject;
    Context context;
    Utils utils;

    public Queries(Context inContext){
        utils = new Utils();
        workloadJsonObject = Utils.workloadJsonObject;
        context = inContext;
    }

    public int startQueries(){

        utils.putMarker("{\"EVENT\":\"TESTBENCHMARK\"}", "trace_marker");

        utils.putMarker("START: App started\n", "trace_marker");
        utils.putMarker("{\"EVENT\":\"SQL_START\"}", "trace_marker");
        int tester = sqlQueries();
        if(tester != 0){
            return 1;
        }
        utils.putMarker("{\"EVENT\":\"SQL_END\"}", "trace_marker");

        utils.putMarker("{\"EVENT\":\"BDB_START\"}", "trace_marker");
        tester = bdbQueries();
        if (tester != 0){
            return 1;
        }
        utils.putMarker("{\"EVENT\":\"BDB_END\"}", "trace_marker");
        utils.putMarker("END: app finished\n", "trace_marker");
        return 0;
    }

    private int sqlQueries(){

        SQLiteDatabase db = context.openOrCreateDatabase("SQLBenchmark",0,null);
        int sqlException = 0;

        try {
            JSONArray benchmarkArray = workloadJsonObject.getJSONArray("benchmark");
            for(int i = 0; i < benchmarkArray.length(); i ++){
                JSONObject operationJson = benchmarkArray.getJSONObject(i);
                Object operationObject = operationJson.get("op");
                String operation = operationObject.toString();
                switch (operation) {
                    case "query": {
                        sqlException = 0;
                        Object queryObject = operationJson.get("sql");
                        String query = queryObject.toString();

                        try {

                            db.execSQL(query);

                            File file = new File(context.getFilesDir().getPath() + "/testSQL");
                            FileOutputStream fos = context.openFileOutput(file.getName(), Context.MODE_APPEND);
                            fos.write((query + "\n").getBytes());
                            fos.close();

                        }
                        catch (SQLiteException e){
                            sqlException = 1;
                            continue;
                        }
                        break;
                    }
                    case "break": {

                        if(sqlException == 0) {
                            //Object breakObject = operationJson.get("delta");
                            //int breakTime = Integer.parseInt(breakObject.toString());
                            int tester = utils.sleepThread(/*breakTime*/1);
                            if(tester != 0){
                                return 1;
                            }

                        }
                        sqlException = 0;
                        break;
                    }
                    default:
                        db.close();
                        return 1;
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
            db.close();
            return 1;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            db.close();
            return 1;
        } catch (IOException e) {
            e.printStackTrace();
            db.close();
            return 1;
        }
        db.close();
        return 0;
    }

    private int bdbQueries(){

        Connection con = utils.jdbcConnection("BDBBenchmark");
        Statement stmt;
        int sqlException = 0;

        try {
            JSONArray benchmarkArray = workloadJsonObject.getJSONArray("benchmark");
            for(int i = 0; i < benchmarkArray.length(); i ++){
                JSONObject operationJson = benchmarkArray.getJSONObject(i);
                Object operationObject = operationJson.get("op");
                String operation = operationObject.toString();
                switch (operation) {
                    case "query": {
                        sqlException = 0;
                        Object queryObject = operationJson.get("sql");
                        String query = queryObject.toString();

                        try {

                            stmt = con.createStatement();
                            stmt.execute(query);
                            stmt.close();

                            File file = new File(context.getFilesDir().getPath() + "/testBDB");
                            FileOutputStream fos = context.openFileOutput(file.getName(), Context.MODE_APPEND);
                            fos.write((query + "\n").getBytes());
                            fos.close();

                        }
                        catch (SQLiteException e){
                            sqlException = 1;
                            continue;
                        } catch (SQLException e) {
                            sqlException = 1;
                            e.printStackTrace();
                            continue;
                        }
                        break;
                    }
                    case "break": {

                        if(sqlException == 0) {
                            //Object breakObject = operationJson.get("delta");
                            //int breakTime = Integer.parseInt(breakObject.toString());
                            int tester = utils.sleepThread(/*breakTime*/1);
                            if(tester != 0){
                                return 1;
                            }

                        }
                        sqlException = 0;
                        break;
                    }
                    default:
                        con.close();
                        return 1;
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();

            try {
                con.close();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }

            return 1;
        } catch (FileNotFoundException e) {
            e.printStackTrace();

            try {
                con.close();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }

            return 1;
        } catch (IOException e) {
            e.printStackTrace();

            try {
                con.close();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }

            return 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return 1;
        }

        try {
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return 1;
        }

        return 0;
    }

}
