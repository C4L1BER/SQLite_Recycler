package com.example.chinmay.sqlitecustomlistview;

import android.widget.EditText;

import java.util.regex.Pattern;


public class Validations
{

    /**
     * @param string
     * @return return true if this is null otherwise false
     */
    public boolean isBlank(EditText string)
    {
        return string.getText().toString().trim().equals("");
    }

    /**
     * Verify Full Name
     */
    public boolean isValidFullName(EditText fullName)
    {
        /*return Pattern.compile("^[\\p{L} .'-]+$", Pattern.CASE_INSENSITIVE).matcher(fullName.getText().toString().trim()).matches();*/
        return Pattern.compile("^[A-Z][a-zA-Z]{1,}(?: [A-Z][a-zA-Z]*){0,1}$", Pattern.CASE_INSENSITIVE).matcher(fullName.getText().toString().trim()).matches();
    }

    /**
     * Email Validations
     */
    public boolean isValidEmail(EditText email)
    {
        String PATTERN_EMAIL = "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";
        return Pattern.compile(PATTERN_EMAIL, Pattern.CASE_INSENSITIVE).matcher(email.getText().toString().trim()).matches();
    }

    /**
     * check valid mobile no
     *
     * @param editText
     * @return
     */
    public boolean isValidPhone(EditText editText)
    {
        return Pattern.compile("\\d{10}").matcher(editText.getText().toString().trim()).matches();
    }
}