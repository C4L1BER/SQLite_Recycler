package com.example.chinmay.sqlitecustomlistview;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
{
    private Validations validations;

    EditText etName, etEmail, etPhone;

    Button btnAdd, btnView, btnUpdate, btnDelete;

    DatabaseHelper myDB;

    Context context = MainActivity.this;

    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setTitle(R.string.MainMenu);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);

        btnAdd = findViewById(R.id.btnAdd);
        btnView = findViewById(R.id.btnView);

        myDB = new DatabaseHelper(this);

        validations = new Validations();

        btnView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Cursor data = myDB.getListContents();
                int numRows = data.getCount();
                if(numRows == 0)
                {
                    Toast.makeText(MainActivity.this, "The database is empty!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Intent i = new Intent(getBaseContext(), ViewListContents.class);
                    startActivity(i);
                }
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                /* Check if the entered data is valid, if yes then add it in the database and then clear the EditTexts.*/
                if(isValid())
                {
                    String uName = etName.getText().toString();
                    String eMail = etEmail.getText().toString();
                    String phone = etPhone.getText().toString();

                    if(uName.length() != 0 && eMail.length() != 0 && phone.length() != 0)
                    {
                        AddData(uName, eMail, phone);
                        etName.setText("");
                        etEmail.setText("");
                        etPhone.setText("");
                        etName.requestFocus();
                    }
                }
            }
        });
    }

    /** Inserting data into the database */
    public void AddData(String uName, String eMail, String phone)
    {
        boolean insertData = myDB.addData(uName, eMail, phone);

        if(insertData)
        {
            Toast.makeText(this, "Successfully entered data!", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(this, "Something wrong with insertion!", Toast.LENGTH_SHORT).show();
        }
    }

    /** Conditions for checking if the entered data is valid */
    public  boolean isValid()
    {
        if (validations.isBlank(etName))
        {
            etName.setError("Please enter your Name");
            etName.requestFocus();
            return false;
        }
        else if (validations.isBlank(etEmail))
        {
            etEmail.setError("Please enter your E-mail");
            etEmail.requestFocus();
            return false;
        }
        else if (validations.isBlank(etPhone))
        {
            etPhone.setError("Please enter your phone number");
            etPhone.requestFocus();
            return false;
        }
        else if(!validations.isValidFullName(etName))
        {
            etName.setError("Invalid Name!");
            etName.requestFocus();
            return false;
        }
        else if(!validations.isValidEmail(etEmail))
        {
            etEmail.setError("Invalid E-mail!");
            etEmail.requestFocus();
            return false;
        }
        else if(!validations.isValidPhone(etPhone))
        {
            etPhone.setError("Invalid Phone number!");
            etPhone.requestFocus();
            return false;
        }
        return true;
    } // Method for validating the data entered by user.

    /** Method for "click BACK again to exit" function */
    @Override
    public void onBackPressed()
    {
        if(doubleBackToExitPressedOnce)
        {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(context, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }
}