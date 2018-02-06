package com.example.subbook;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Date;

import static android.text.TextUtils.isDigitsOnly;

public class EditSubscription extends AppCompatActivity {

    private int position = -1;
    private String nameSpecify = "Name: ";
    private String dateSpecify = "Date Started: ";
    private String chargeSpecify = "Monthly Charge: ";
    private String commentSpecify = "Comment: ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        int i, index, totalTimes;
        String s;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_subscription);

        // Set the default date as today
        EditText setDate = (EditText) findViewById( R.id.EditDate );
        SimpleDateFormat dateFormat = new SimpleDateFormat( "yyyy-MM-dd" );
        setDate.setHint(dateFormat.format(new Date()));

        // Get the Intent that started this activity and extract the string
        Intent fromMain = getIntent();
        String editAgain = fromMain.getStringExtra("needEdit");
        if (editAgain != null) {
            // Put name into name
            i = nameSpecify.length();
            s = "";
            while (editAgain.charAt(i) != '\n' && i < editAgain.length()) {
                s += editAgain.charAt(i);
                i++;
            }
            EditText nameAgain = (EditText) findViewById(R.id.EditName);
            nameAgain.setText(s);
            // Put date into date
            i += (dateSpecify.length() + 1);
            s = "";
            while (editAgain.charAt(i) != '\n' && i < editAgain.length()) {
                s += editAgain.charAt(i);
                i++;
            }
            EditText dateAgain = (EditText) findViewById(R.id.EditDate);
            dateAgain.setText(s);
            // Put charge into charge
            i += (chargeSpecify.length() + 1);
            s = "";
            while (editAgain.charAt(i) != '\n' && i < editAgain.length()) {
                s += editAgain.charAt(i);
                i++;
            }
            EditText chargeAgain = (EditText) findViewById(R.id.EditCharge);
            chargeAgain.setText(s);
            // Calculate total number of \n
            totalTimes = 0;
            for (int x=0; x < editAgain.length(); x++){
                if (editAgain.charAt(x) == '\n'){
                    totalTimes += 1;
                }
            }
            // Check if it has comment
            if (totalTimes == 4){
                // Put comment into comment
                i += (commentSpecify.length() + 1);
                s = "";
                while (editAgain.charAt(i) != '\n' && i < editAgain.length()) {
                    s += editAgain.charAt(i);
                    i++;
                }
                EditText commentAgain = (EditText) findViewById(R.id.EditComment);
                commentAgain.setText(s);
            }
            // Get the element's index
            i = editAgain.lastIndexOf('\n') + 1;
            s = "";
            while (i < editAgain.length()){
                s += editAgain.charAt(i);
                i++;
            }
            position = Integer.parseInt(s);
        }
    }
    public void SaveStatus(View view){
        int lenName, lenDate, lenComment;
        boolean defaultDate;

        // Assume user will give date
        defaultDate = false;

        // Get the name which is entered by user
        EditText name = (EditText) findViewById(R.id.EditName);
        String strName = name.getText().toString();
        lenName = strName.length();

        // Get the date which is entered by user
        EditText date = (EditText) findViewById(R.id.EditDate);
        String strDate = date.getText().toString();
        lenDate = strDate.length();

        // Get the charge which is entered by user
        EditText charge = (EditText) findViewById(R.id.EditCharge);
        String strCharge = charge.getText().toString();

        // Get the comment which is entered by user
        EditText comment = (EditText) findViewById(R.id.EditComment);
        String strComment = comment.getText().toString();
        lenComment = strComment.length();

        // If user did not enter a date, then set the date as today
        if (lenDate == 0){
            strDate = date.getHint().toString();
            defaultDate = true;
        }

        /** Check section */
        // Check if the name has more than 20 characters
        if (lenName > 20 || lenName == 0){
            new AlertDialog.Builder(this)
                    .setTitle("Alert")
                    .setMessage("Please enter a book name which is up to 20 characters.")
                    .setCancelable(false)
                    .setPositiveButton("OK", null)
                    .create()
                    .show();
        }

        // Check if the user given date satisfy the format
        else if (!defaultDate && !strDate.matches("([0-9]{4})-([0][1-9]||[1][0-2])-([0][1-9]||[1][0-9]||[2][0-9]||[3][0-1])")){
            new AlertDialog.Builder(this)
                    .setTitle("Alert")
                    .setMessage("Please enter a valid date following the format \"yyyy-mm-dd\".")
                    .setCancelable(false)
                    .setPositiveButton("OK", null)
                    .create()
                    .show();
        }

        // Check if the user given charge is OK
        //else if (!isDigitsOnly(strCharge) || Integer.parseInt(strCharge) < 0){
        else if (!strCharge.matches("^\\d+\\.\\d*$")){
            new AlertDialog.Builder(this)
                    .setTitle("Alert")
                    .setMessage("Please enter a non-negative currency value.")
                    .setCancelable(false)
                    .setPositiveButton("OK", null)
                    .create()
                    .show();
        }

        // Check if the comment has more than 30 characters
        else if (lenComment > 30){
            new AlertDialog.Builder(this)
                    .setTitle("Alert")
                    .setMessage("Please enter a comment which is up to 30 characters.")
                    .setCancelable(false)
                    .setPositiveButton("OK", null)
                    .create()
                    .show();
        }

        // If all conditions are satisfied
        else{
            String aSubscription = "";
            // Check if the user entered comment
            if (lenComment == 0) {
                // Form a return message
                aSubscription = aSubscription + nameSpecify + strName + "\n" + dateSpecify + strDate + "\n" + chargeSpecify + strCharge + "\n";
            }
            else{
                // Form a return message
                aSubscription = aSubscription + nameSpecify + strName + "\n" + dateSpecify + strDate + "\n" + chargeSpecify
                        + strCharge + "\n" + commentSpecify + strComment + "\n";
            }
            if (position != -1){
                aSubscription += position;
                position = -1;
            }
            // Extra those strings and go back to main activity
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("newSubscription", aSubscription);
            intent.putExtra("charge", strCharge);
            startActivity(intent);
        }
    }
}
