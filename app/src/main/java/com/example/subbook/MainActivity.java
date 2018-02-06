package com.example.subbook;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.lang.*;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView subView;
    ArrayAdapter<String> adapter;
    static ArrayList<String> subList = new ArrayList<String>();
    static float sumCharge = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        int i, targetPosition;
        String s;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        String aSubscription = intent.getStringExtra("newSubscription");
        // Get the charge
        String strCharge = intent.getStringExtra("charge");
        if (strCharge != null) {
            float charge = Float.parseFloat(strCharge);
            sumCharge += charge;
        }

        // Get ListView object from xml
        subView = (ListView) findViewById(R.id.SubList);

        // Capture the layout's TextView and set the string as its text
        TextView textView = findViewById(R.id.textView);
        DecimalFormat df = new DecimalFormat("#.00");
        String displayStr = "The total monthly charge is: " + df.format(sumCharge);
        textView.setText(displayStr);

        // Check if return string exist
        if (aSubscription != null) {
            // Check it is a new one or a edited one
            if (aSubscription.endsWith("\n")) {
                // It is new
                // Add valid subscription to the list
                subList.add(aSubscription);
            }
            else{
                // It is an edited one
                // Get the element's index
                i = aSubscription.lastIndexOf('\n') + 1;
                s = "";
                while (i < aSubscription.length()){
                    s += aSubscription.charAt(i);
                    i++;
                }
                targetPosition = Integer.parseInt(s);
                // Delete the index in string
                i = aSubscription.lastIndexOf('\n') + 1;
                StringBuilder builder = new StringBuilder(aSubscription);
                while (i < aSubscription.length()){
                    builder.deleteCharAt(i);
                    i++;
                }
                aSubscription = builder.toString();
                subList.set(targetPosition, aSubscription);
            }
        }

        // Handle click
        subView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // ListView Clicked item index
                final int index = position;

                // Ask user's option
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Attention")
                        .setMessage("What do you want to do next?")
                        .setCancelable(false)
                        .setPositiveButton("Edit It", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Extra the subscription and go to editor
                                Intent goEditor = new Intent(MainActivity.this, EditSubscription.class);
                                String send = subList.get(index);
                                // Get the charge
                                int aimPos = send.indexOf('\n', send.indexOf('\n', send.indexOf('\n')+1)+1);
                                while (send.charAt(aimPos) != ' '){
                                    aimPos--;
                                }
                                aimPos++;
                                String sCharge = "";
                                while (send.charAt(aimPos) != '\n'){
                                    sCharge += send.charAt(aimPos);
                                    aimPos++;
                                }
                                float floChar = Float.parseFloat(sCharge);
                                sumCharge -= floChar;
                                // Form the send string
                                send += index;
                                goEditor.putExtra("needEdit", send);
                                startActivity(goEditor);
                            }
                        })
                        .setNegativeButton("Delete It",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String element = subList.get(index);
                                // Get the charge
                                int aPos = element.indexOf('\n', element.indexOf('\n', element.indexOf('\n')+1)+1);
                                while (element.charAt(aPos) != ' '){
                                    aPos--;
                                }
                                aPos++;
                                String sCharge = "";
                                while (element.charAt(aPos) != '\n'){
                                    sCharge += element.charAt(aPos);
                                    aPos++;
                                }
                                float floChar = Float.parseFloat(sCharge);
                                sumCharge -= floChar;
                                TextView textView = findViewById(R.id.textView);
                                DecimalFormat df = new DecimalFormat("#.00");
                                String displayStr = "The total monthly charge is: " + df.format(sumCharge);
                                textView.setText(displayStr);
                                // Delete the element
                                subList.remove(index);
                                adapter.notifyDataSetChanged();
                            }
                        })
                        .create()
                        .show();
            }
        });
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        // Define a new Adapter
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, subList);

        // Assign adapter to ListView
        subView.setAdapter(adapter);

        // Load
        if (subList.isEmpty()) {
            SharedPreferences settings = getSharedPreferences("PREFS", 0);
            String wordsString = settings.getString("words", "");
            String[] itemWords = wordsString.split("\r");
            for (int i = 0; i < itemWords.length; i++) {
                subList.add(itemWords[i]);
            }
            if (subList.size() > 1) {
                String StrsumCharge = subList.get(subList.size() - 1);
                sumCharge = Float.parseFloat(StrsumCharge);
                subList.remove(subList.size() - 1);
                // Capture the layout's TextView and set the string as its text
                TextView textView = findViewById(R.id.textView);
                DecimalFormat df = new DecimalFormat("#.00");
                String displayStr = "The total monthly charge is: " + df.format(sumCharge);
                textView.setText(displayStr);
            }
        }
    }

    @Override
    protected void onStop(){
        // TODO Auto-generated method stub
        super.onStop();
        StringBuilder stringBuilder = new StringBuilder();
        for (String aElement : subList){
            stringBuilder.append(aElement);
            stringBuilder.append("\r");
        }
        stringBuilder.append(sumCharge);
        stringBuilder.append("\r");

        SharedPreferences settings = getSharedPreferences("PREFS", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("words", stringBuilder.toString());
        editor.commit();
    }

    /** Open Subscription Editor*/
    public void SubEditor(View view) {
        Intent EditSub = new Intent(this, EditSubscription.class);
        startActivity(EditSub);
    }

    public void QuiButton(View view) {
        // TODO Auto-generated method stub
        ActivityCompat.finishAffinity(this);
    }
}
