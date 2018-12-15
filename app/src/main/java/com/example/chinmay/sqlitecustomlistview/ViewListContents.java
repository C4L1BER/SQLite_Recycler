package com.example.chinmay.sqlitecustomlistview;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class ViewListContents extends AppCompatActivity
{
    private static final String TAG = ViewListContents.class.getSimpleName();

    private Validations validations;

    private Dialog dialog;

    private EditText userName, userEmail, userPhone;

    DatabaseHelper myDB;

    ArrayList<User> userList;

    User user;

    Context context = ViewListContents.this;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_list_contents);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        assert getSupportActionBar() != null;

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        myDB = new DatabaseHelper(this);

        validations = new Validations();

        final Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        userList = new ArrayList<>();
        Cursor data = myDB.getListContents();
        int numRows = data.getCount();

        /* If Database is empty go back to main activity else populate the RecyclerView. */
        if(numRows == 0)
        {
            Toast.makeText(this, "Database Empty!", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(ViewListContents.this, MainActivity.class);
            startActivity(i);
            finish();
        }
        else
        {
            int i = 0;
            while(data.moveToNext())
            {
                user = new User(data.getString(0), data.getString(1), data.getString(2), data.getString(3));
                userList.add(i, user);
                System.out.println(data.getString(0)+" "+data.getString(1)+" "+data.getString(2)+" "+data.getString(3));
                System.out.println(userList.get(i).getuName());
                i++;
            }

            RecyclerView recyclerView = findViewById(R.id.recyclerView);
            RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, userList);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));

            recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this, recyclerView, new RecyclerTouchListener.ClickListener()
            {
                @Override
                public void onClick(View view, int position)
                {

                }

                @Override
                public void onLongClick(View view, int position)
                {
                    String id = userList.get(position).getId();
                    Log.i(TAG, "RecyclerLongClick: "+id);
                    vibe.vibrate(25);
                    update(id);
                }

            }));
        }
    }

    /** Method for BackButton */
    public boolean onSupportNavigateUp()
    {
        finish();
        return true;
    }

    /** Method for updateButton, the button in the OptionsMenu. */
    private void update(final String position)
    {
        validations = new Validations();

        dialog = new Dialog(ViewListContents.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.update_layout);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        dialog.setCancelable(true);

        dialog.show();

        Button btnUpdate = dialog.findViewById(R.id.btnUpdate);
        Button btnDelete = dialog.findViewById(R.id.btnDelete);

        userName = dialog.findViewById(R.id.etName);
        userEmail = dialog.findViewById(R.id.etEmail);
        userPhone = dialog.findViewById(R.id.etPhone);

        /* Get data of the corresponding ID from the database . */
        myDB = new DatabaseHelper(ViewListContents.this);
        Cursor cursor = myDB.getContents(position);
        Log.i(TAG, "CursorCount: "+cursor.getCount());

        /* If cursor moveToFirst is true, then set data to the EditTexts*/
        if (cursor.moveToFirst())
        {
            do{
                String uName = cursor.getString(cursor.getColumnIndex("UNAME"));
                String Email = cursor.getString(cursor.getColumnIndex("EMAIL"));
                String Phone = cursor.getString(cursor.getColumnIndex("PHONE"));
                userName.setText(uName);
                userEmail.setText(Email);
                userPhone.setText(Phone);
                userName.requestFocus();

                Log.i(TAG, "ViewData UserName: "+uName+" Email: "+Email+" Phone: "+Phone);
            }
            while(cursor.moveToNext());
        }
        cursor.close();

        /* Update button click event in the dialog. */
        btnUpdate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                /* Check if the entered data is valid. If it's invalid display a toast. Else update the row corresponding to that ID. */
                if(isValid())
                {
                    myDB = new DatabaseHelper(ViewListContents.this);
                    Cursor cursor = myDB.getContents(position);
                    Log.i(TAG, "CursorCount: "+cursor.getCount());
                    if(cursor.getCount() == 0)
                    {
                        Toast.makeText(ViewListContents.this, "ID doesn't exist!", Toast.LENGTH_SHORT).show();
                    }
                    if (cursor.moveToFirst())
                    {
                        do{
                            String uName = cursor.getString(cursor.getColumnIndex("UNAME"));
                            String Email = cursor.getString(cursor.getColumnIndex("EMAIL"));
                            String Phone = cursor.getString(cursor.getColumnIndex("PHONE"));

                            /* Check if the entered data exists in the database */
                            if (uName.equals(userName.getText().toString().trim())&&Email.equals(userEmail.getText().toString().trim())&&Phone.equals(userPhone.getText().toString().trim()))
                            {
                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                builder.setTitle("Alert!");
                                builder.setMessage("You have not made any changes, cannot update.");
                                builder.setPositiveButton("OK", null);
                                AlertDialog dialog = builder.create();
                                dialog.show();
                            }
                            else
                            {
                                int temp = position.length();
                                if(temp > 0)
                                {
                                    Boolean update = myDB.updateData(position, userName.getText().toString().trim(), userEmail.getText().toString().trim(), userPhone.getText().toString().trim());
                                    if(update)
                                    {
                                        Toast.makeText(context, "Successfully updated the data!", Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                        finish();
                                        startActivity(getIntent());
                                    }
                                }
                            }
                            Log.i(TAG, "ViewData UserName: "+uName+" Email: "+Email+" Phone: "+Phone);
                        }
                        while(cursor.moveToNext());
                    }
                    cursor.close();
                }
            }
        });

        /* Delete button click event in the dialog */
        btnDelete.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Alert!");
                builder.setMessage("Are you sure you want to delete this record?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        int temp = position.length();
                        if(temp > 0)
                        {
                            Integer deleteRow = myDB.deleteData(position);
                            if(deleteRow > 0)
                            {
                                Toast.makeText(context, "Successfully deleted the data!", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                                finish();
                                startActivity(getIntent());
                            }
                        }
                    }
                });

                builder.setNegativeButton("No", null);

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    /** Conditions for checking if the entered data is valid */
    public  boolean isValid()
    {
        if (validations.isBlank(userName))
        {
            userName.setError("Please enter new UserName");
            userName.requestFocus();
            return false;
        }
        else if (validations.isBlank(userEmail))
        {
            userEmail.setError("Please enter new E-mail");
            userEmail.requestFocus();
            return false;
        }
        else if (validations.isBlank(userPhone))
        {
            userPhone.setError("Please enter new phone number");
            userPhone.requestFocus();
            return false;
        }
        else if(!validations.isValidFullName(userName))
        {
            userName.setError("Invalid Name!");
            userEmail.requestFocus();
            return false;
        }
        else if(!validations.isValidEmail(userEmail))
        {
            userEmail.setError("Invalid E-mail!");
            userEmail.requestFocus();
            return false;
        }
        else if(!validations.isValidPhone(userPhone))
        {
            userPhone.setError("Invalid Phone number!");
            userPhone.requestFocus();
            return false;
        }

        return true;
    }
}