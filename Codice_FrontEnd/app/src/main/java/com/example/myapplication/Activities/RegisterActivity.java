package com.example.myapplication.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.API.UserAPI;
import com.example.myapplication.Classes.User;
import com.example.myapplication.R;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Pattern;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterActivity extends AppCompatActivity {

    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^" +
            "(?=.*[0-9])" +         //at least 1 digit
            "(?=.*[a-z])" +         //at least 1 lower case letter
            "(?=.*[A-Z])" +         //at least 1 upper case letter
            "(?=.*[a-zA-Z])" +      //any letter
            "(?=.*[@#$%^&+=!])" +    //at least 1 special character
            "(?=\\S+$)" +           //no white spaces
            ".{8,}" +               //at least 8 characters
            "$");

    EditText rgstrName, rgstrSurname, rgstrCF, rgstrEmail, rgstrDob, rgstrPwd, rgstrRepeatPwd;
    Button btnRegister;
    String inputCF, inputName, inputSurname, inputEmail, inputDOB, inputPwd;
    MaterialDatePicker<Long> materialDatePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initViews();

        rgstrDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                materialDatePicker.show(getSupportFragmentManager(),"DATE_PICKER");
                materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
                    @Override
                    public void onPositiveButtonClick(Long selection) {
                        // Get the offset from our timezone and UTC.
                        TimeZone timeZoneUTC = TimeZone.getDefault();
                        // It will be negative, so that's the -1
                        int offsetFromUTC = timeZoneUTC.getOffset(new Date().getTime()) * -1;
                        // Create a date format, then a date object with our offset
                        SimpleDateFormat simpleFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ITALY);
                        Date date = new Date(selection + offsetFromUTC);

                        rgstrDob.setText(simpleFormat.format(date));
                    }
                });
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (validateName() && validateSurname() && validateCF()
                        && validateEmail() && validateDOB() && validatePassword()) {

                    Retrofit retrofit = new Retrofit.Builder().baseUrl(getString(R.string.baseUrl))
                            .addConverterFactory(GsonConverterFactory.create()).build();

                    UserAPI userAPI = retrofit.create(UserAPI.class);

                    inputCF = rgstrCF.getText().toString();
                    inputName = rgstrName.getText().toString();
                    inputSurname = rgstrSurname.getText().toString();
                    inputEmail = rgstrEmail.getText().toString();
                    inputDOB = rgstrDob.getText().toString();
                    inputPwd = rgstrPwd.getText().toString();

                    User newUser = new User(inputCF, inputName, inputSurname, inputDOB, inputEmail, inputPwd);

                    Call<ResponseBody> call = userAPI.registerUser(newUser);

                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                            if (!response.isSuccessful()) {
                                Toast.makeText(RegisterActivity.this, "User already registered.", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(RegisterActivity.this, "Registration complete successfully.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                                startActivity(intent);
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Toast.makeText(RegisterActivity.this, "Error during registration, please make another attempt.", Toast.LENGTH_LONG).show();

                        }
                    });

                }

            }
        });
    }

    public void initViews() {
        rgstrName = findViewById(R.id.rgstrName);
        rgstrSurname = findViewById(R.id.rgstrSurname);
        rgstrCF = findViewById(R.id.rgstrCF);
        rgstrEmail = findViewById(R.id.rgstrEmail);
        rgstrDob = findViewById(R.id.rgstrDOB);
        rgstrPwd = findViewById(R.id.rgstrPwd);
        rgstrRepeatPwd = findViewById(R.id.rgstrRepeatPwd);
        btnRegister = findViewById(R.id.btnRgstr);

        MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker();
        materialDatePicker = builder.build();
    }

    public boolean validateName() {
        String pwdRgstr = rgstrName.getText().toString().trim();

        if (pwdRgstr.isEmpty()) {
            rgstrName.setError("Field can't be empty!");
            return false;
        } else {
            rgstrName.setError(null);
            return true;
        }
    }

    public boolean validateSurname() {
        String surnameRgstr = rgstrSurname.getText().toString().trim();

        if (surnameRgstr.isEmpty()) {
            rgstrSurname.setError("Field can't be empty!");
            return false;
        } else {
            rgstrSurname.setError(null);
            return true;
        }
    }

    public boolean validateCF() {
        String cfRgstr = rgstrCF.getText().toString().trim();

        if (cfRgstr.isEmpty()) {
            rgstrCF.setError("Field can't be empty!");
            return false;
        } else if (rgstrCF.getText().toString().length() < 16) {
            rgstrCF.setError("CF value is not valid");
            return false;
        } else {
            rgstrCF.setError(null);
            return true;
        }
    }

    public boolean validateEmail() {
        String emailRgstr = rgstrEmail.getText().toString().trim();

        if (emailRgstr.isEmpty()) {
            rgstrEmail.setError("Field can't be empty!");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailRgstr).matches()) {
            rgstrEmail.setError("Please enter a valid email address.");
            return false;
        } else {
            rgstrEmail.setError(null);
            return true;
        }
    }

    public boolean validateDOB() {
        String dobRgstr = rgstrDob.getText().toString().trim();

        if (dobRgstr.isEmpty()) {
            rgstrDob.setError("Field can't be empty!");
            return false;
        } else {
            rgstrDob.setError(null);
            return true;
        }
    }

    public boolean validatePassword() {
        String pwdRgstr = rgstrPwd.getText().toString().trim();
        Boolean flag = false;

        if (pwdRgstr.isEmpty()) {
            rgstrPwd.setError("Field can't be empty!");
            flag = false;
        } else if (!PASSWORD_PATTERN.matcher(pwdRgstr).matches()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
            builder.setMessage("The password must respect the following constraints:\n"
                    + " - At least 1 digit\n"
                    + " - At least 1 lower case letter\n"
                    + " - At least 1 upper case letter\n"
                    + " - At least 1 special character (@#$%^&+=!)\n"
                    + " - At least 8 characters\n"
                    + " - No white spaces\n");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }

            });

            builder.create().show();

        } else if (!rgstrPwd.getText().toString().equals(rgstrRepeatPwd.getText().toString())) {
            rgstrRepeatPwd.setError("The password must match!");
            flag = false;
        } else {
            rgstrRepeatPwd.setError(null);
            flag = true;
        }

        return flag;

    }


}