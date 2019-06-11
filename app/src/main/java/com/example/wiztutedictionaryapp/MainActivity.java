package com.example.wiztutedictionaryapp;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    private static final int REQUEST_CODE = 100;
    private TextView textOutput;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textOutput = findViewById(R.id.textOutput);
        textOutput.setText("Speak to enter text!");
        if (savedInstanceState != null)
            makeTagLinks(savedInstanceState.getString("speech"), textOutput);
        // ImageView imageView = findViewById(R.id.imageView);
        //imageView.setImageResource(R.drawable.background);



    }

//This method is called with the button is pressed//

    public void start(View v)

//Create an Intent with “RecognizerIntent.ACTION_RECOGNIZE_SPEECH” action//

    {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

        try {

//Start the Activity and wait for the response//

            startActivityForResult(intent, REQUEST_CODE);
        } catch (ActivityNotFoundException a) {
            a.printStackTrace();
        }
    }

    @Override

//Handle the results//

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_CODE: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    textOutput.setText(result.get(0));
                    makeTagLinks(result.get(0), textOutput);
                }
                break;
            }

        }
    }

    private void makeTagLinks(final String text, final TextView tv) {
        if (text == null || tv == null) {
            return;
        }
        final SpannableString ss = new SpannableString(text);
        final String[] items = text.split("\\s+");
        int start = 0, end;
        for (final String item : items) {
            end = start + item.length();
            if (start < end) {
                ss.setSpan(new MyClickableSpan(item, this), start, end, 0);
            }
            start += item.length() + 1;//comma and space in the original text ;)
        }
        tv.setMovementMethod(LinkMovementMethod.getInstance());
        tv.setText(ss, TextView.BufferType.SPANNABLE);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.i("speech", textOutput.getText().toString());
        outState.putString("speech", textOutput.getText().toString());
        super.onSaveInstanceState(outState);

    }


}