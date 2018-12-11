package com.example.chinmay.sqlitecustomlistview;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class ViewListContents extends AppCompatActivity
{
    DatabaseHelper myDB;
    ArrayList<User> userList;
    User user;
    Context context = ViewListContents.this;
    private Validations validations;
    private Dialog dialog;
    private EditText userId, userName, userEmail, userPhone, delUserId;
    private Button btnUpdate, btnDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_list_contents);

        assert getSupportActionBar() != null;

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        myDB = new DatabaseHelper(this);

        validations = new Validations();

        userList = new ArrayList<>();
        Cursor data = myDB.getListContents();
        int numRows = data.getCount();
        if(numRows == 0)
        {
            Intent i = new Intent(ViewListContents.this, MainActivity.class);
            startActivity(i);
            Toast.makeText(this, "Database Empty!", Toast.LENGTH_SHORT).show();
        }// If Database is empty go back to main activity else populate the RecyclerView.
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
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.view_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.menuUpdate:
                update();
                return true;

            case R.id.menuDelete:
                delete();
                return true;
            default: return super.onOptionsItemSelected(item);
        }
    }

    public boolean onSupportNavigateUp()
    {
        finish();
        return true;
    } // Method for BackButton

    private void update()
    {
        validations = new Validations();

        dialog = new Dialog(ViewListContents.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.update_layout);
        dialog.setCancelable(true);

        dialog.show();

        btnUpdate = dialog.findViewById(R.id.btnUpdate);

        userName = dialog.findViewById(R.id.etName);
        userId = dialog.findViewById(R.id.etId);
        userEmail = dialog.findViewById(R.id.etEmail);
        userPhone = dialog.findViewById(R.id.etPhone);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                if(isValid())
                {
                    int temp = userId.getText().toString().length();
                    if(temp > 0)
                    {
                        Boolean update = myDB.updateData(userId.getText().toString(), userName.getText().toString(), userEmail.getText().toString(), userPhone.getText().toString());
                        if(update)
                        {
                            Toast.makeText(context, "Successfully updated the data!", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            finish();
                            startActivity(getIntent());
                        }
                        else
                        {
                            userId.setError("The ID does not exist!");
                        }
                    }
                }
            }
        });
    }// Create a dialog to update the ID which user wants to update.

    private void delete()
    {
        validations = new Validations();

        dialog = new Dialog(ViewListContents.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.delete_layout);
        dialog.setCancelable(true);

        dialog.show();

        btnDelete = dialog.findViewById(R.id.btnDelete);
        delUserId = dialog.findViewById(R.id.delEtId);

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(validations.isBlank(delUserId))
                {
                    delUserId.setError("Please enter an ID");
                    delUserId.requestFocus();
                }
                else
                {
                    int temp = delUserId.getText().toString().length();
                    if(temp > 0)
                    {
                        Integer deleteRow = myDB.deleteData(delUserId.getText().toString());
                        if(deleteRow > 0)
                        {
                            Toast.makeText(context, "Successfully deleted the data!", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            finish();
                            startActivity(getIntent());
                        }
                        else
                        {
                            delUserId.setError("The ID does not exist!");
                        }
                    }
                }
            }
        });
    }// Create a dialog to accept and delete the ID which the user wants to delete.

    public  boolean isValid()
    {
        if (validations.isBlank(userId))
        {
            userId.setError("Please enter an ID");
            userId.requestFocus();
            return false;
        }
        else if (validations.isBlank(userName))
        {
            userName.setError("Please enter your Name");
            userName.requestFocus();
            return false;
        }
        else if (validations.isBlank(userEmail))
        {
            userEmail.setError("Please enter your E-mail");
            userEmail.requestFocus();
            return false;
        }
        else if (validations.isBlank(userPhone))
        {
            userPhone.setError("Please enter your phone number");
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
    } // Method for validating the data entered by user.
}