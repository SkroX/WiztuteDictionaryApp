package com.example.wiztutedictionaryapp;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.text.Selection;
import android.text.SpannableString;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MyClickableSpan extends ClickableSpan  {


    private final String mText;
    private Context context1;

    MyClickableSpan(final String text, Context context) {
        mText = text;
        context1 = context;


    }

    @Override
    public void onClick(final View widget) {
        try {
            String result = new GetWordMeaningTask().execute(dictionaryEntries()).get();
            String def = getDefinition(result);
            //  if(def.equals("")){ def="no relevant meaning found";}


            showPopUp(widget, def);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void showPopUp(View widget, String def) {
        LayoutInflater inflater = (LayoutInflater)
                context1.getSystemService(context1.LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popuplayout, null);
        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        popupWindow.setAnimationStyle(R.style.Animation);
        TextView meaning = popupWindow.getContentView().findViewById(R.id.textView6);
        meaning.setText(def);

        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken

        Rect location = locateView(widget);
        popupWindow.showAtLocation(widget, Gravity.TOP | Gravity.CENTER, 0, location.bottom + 35);
        // dismiss the popup window when touched
        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();

                return true;
            }
        });
    }

    private static Rect locateView(View v) {
        int[] loc_int = new int[2];
        if (v == null) return null;
        try {
            v.getLocationOnScreen(loc_int);
        } catch (NullPointerException npe) {
            //Happens when the view doesn't exist on screen anymore.
            return null;
        }
        Rect location = new Rect();
        location.left = loc_int[0];
        location.top = loc_int[1];
        location.right = location.left + v.getWidth();
        location.bottom = location.top + v.getHeight();
        return location;
    }

    private String dictionaryEntries() {
        final String language = "en";
        final String word_id = mText.toLowerCase(); //word id is case sensitive and lowercase is required
        return "https://od-api.oxforddictionaries.com:443/api/v1/entries/" + language + "/" + word_id;
    }

    private String getDefinition(String result) {
        String def = "";
        try {
            JSONObject js = new JSONObject(result);
            JSONArray results = js.getJSONArray("results");
            for (int i = 0; i < results.length(); i++) {
                JSONObject lentries = results.getJSONObject(i);
                JSONArray la = lentries.getJSONArray("lexicalEntries");
                for (int j = 0; j < la.length(); j++) {
                    JSONObject entries = la.getJSONObject(j);
                    JSONArray e = entries.getJSONArray("entries");
                    for (int k = 0; k < e.length(); k++) {
                        JSONObject senses = e.getJSONObject(k);
                        JSONArray s = senses.getJSONArray("senses");
                        JSONObject d = s.getJSONObject(0);
                        JSONArray de = d.getJSONArray("definitions");
                        def = de.getString(0);
                    }
                }
            }
            Log.e("def", def);
            return def;

        } catch (JSONException e) {
            e.printStackTrace();
            return "not found!";
        }
    }
}



