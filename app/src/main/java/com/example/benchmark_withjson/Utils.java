package com.example.benchmark_withjson;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Utils {

    static JSONObject workloadJsonObject;

    public String jsonToString(String filename){

        String line;
        String finalString = "";

        try {
            BufferedReader br = new BufferedReader(new FileReader(filename));
            while((line = br.readLine()) != null){
                if(!line.contains("sql")) {
                    line = line.replaceAll("\\s+", "");
                    finalString = finalString + line;
                }
                else {
                    finalString = finalString + line;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return finalString;
    }

    public JSONObject jsonStringToObject(String jsonString){
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(jsonString);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        workloadJsonObject = jsonObject;
        return jsonObject;
    }

    public int sleepThread(int interval) {
        try {
            Thread.sleep(interval);
        } catch (Exception e) {
            e.printStackTrace();
            return 1;
        }
        return 0;
    }


    public Connection jdbcConnection(String dbName) {
        if(dbName == null){
            return null;
        }
        String url =
                "jdbc:sqlite://data/data/com.example.benchmark_withjson/databases/" + dbName;
        Connection con;
        try {
            Class.forName("SQLite.JDBCDriver");


        } catch (java.lang.ClassNotFoundException e) {
            System.err.print("ClassNotFoundException: ");
            System.err.println(e.getMessage());
            return null;
        }

        try {
            con = DriverManager.getConnection(url);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        return con;
    }

    public int findMissingQueries(Context context){
        try {
            BufferedReader br1;
            BufferedReader br2;
            String sCurrentLine;
            List<String> list1 = new ArrayList<>();
            List<String> list2 = new ArrayList<>();
            br1 = new BufferedReader(new FileReader(context.getFilesDir().getPath() + "/testSQL"));
            br2 = new BufferedReader(new FileReader(context.getFilesDir().getPath() + "/testBDB"));
            while ((sCurrentLine = br1.readLine()) != null) {
                list1.add(sCurrentLine);
            }
            while ((sCurrentLine = br2.readLine()) != null) {
                list2.add(sCurrentLine);
            }
            List<String> tmpList = new ArrayList<>(list1);
            tmpList.removeAll(list2);

            tmpList = list2;
            tmpList.removeAll(list1);

            String dbMissingQueries;
            if(list1.size() < list2.size()){
                dbMissingQueries = "notInBDBQueries";
            }
            else if(list1.size() > list2.size()){
                dbMissingQueries = "notInSQLQueries";
            }
            else {
                dbMissingQueries = "bothRanSameQueries";
            }

            for (int i = 0; i < tmpList.size(); i++) {
                File file = new File(context.getFilesDir().getPath() + "/" + dbMissingQueries);
                FileOutputStream fos = context.openFileOutput(file.getName(), Context.MODE_APPEND);
                String temp = tmpList.get(i) + "\n";
                fos.write(temp.getBytes());
                fos.close();
            }


        } catch (IOException e) {
            e.printStackTrace();
            return 1;
        }

        return 0;
    }

    public int putMarker(String mark,String filename) {
        PrintWriter outStream = null;
        try{
            FileOutputStream fos = new FileOutputStream("/sys/kernel/debug/tracing/" + filename);
            outStream = new PrintWriter(new OutputStreamWriter(fos));
            outStream.println(mark);
            outStream.flush();
        }
        catch(Exception e) {
            return 1;
        }
        finally {
            if (outStream != null) {
                outStream.close();
            }
            return 0;
        }
    }

}