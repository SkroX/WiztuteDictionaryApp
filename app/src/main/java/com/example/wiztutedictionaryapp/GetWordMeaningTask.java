package com.example.wiztutedictionaryapp;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class GetWordMeaningTask extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... params) {


        final String app_id = "10742428";
        final String app_key = "ada344f3a7a7c7de0315fb78c5c9d6f9";
        try {
            URL url = new URL(params[0]);
            HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setRequestProperty("app_id", app_id);
            urlConnection.setRequestProperty("app_key", app_key);

            // read the output from the server
            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder stringBuilder = new StringBuilder();

            String line ;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line + "\n");
            }

            return stringBuilder.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return "not found!";
        }
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        Log.d("This will be my result", result);
    }
}
