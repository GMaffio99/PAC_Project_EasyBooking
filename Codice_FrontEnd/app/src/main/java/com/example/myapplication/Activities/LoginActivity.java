package com.example.myapplication.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.API.UserAPI;
import com.example.myapplication.Classes.User;
import com.example.myapplication.R;
import com.example.myapplication.SupportViews.ResetPWDDialog;
import com.example.myapplication.SessionManager.SessionManager;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    TextView btnSignUp, btnForgotPWD;
    EditText inputEmail, inputPassword;
    Button btnLogin;
    Boolean flag = false;
    UserAPI userAPI;

    SessionManager currentSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initViews();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (validateEmail() && validatePassword()) {

                    Retrofit retrofit = new Retrofit.Builder().baseUrl(getString(R.string.baseUrl)).addConverterFactory(GsonConverterFactory.create()).build();

                    userAPI = retrofit.create(UserAPI.class);

                    RequestBody requestBody = new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("username", inputEmail.getText().toString())
                            .addFormDataPart("password", inputPassword.getText().toString())
                            .build();

                    userAPI.loginUser(requestBody).enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if(!response.isSuccessful()) {
                                Toast.makeText(LoginActivity.this,"WRONG CREDENTIALS.", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(LoginActivity.this,"LOGIN COMPLETED SUCCESSFULLY.", Toast.LENGTH_LONG).show();
                                userAPI.getUser(inputEmail.getText().toString()).enqueue(new Callback<User>() {
                                    @Override
                                    public void onResponse(Call<User> call, Response<User> response) {
                                        if(!response.isSuccessful()) {
                                            Toast.makeText(LoginActivity.this,"Something gone wrong, please make another attempt.", Toast.LENGTH_LONG).show();
                                        } else {

                                            User loggedUser = response.body();

                                            currentSession = new SessionManager(LoginActivity.this);
                                            currentSession.createLoginSession(loggedUser.id, loggedUser.name, loggedUser.surname, loggedUser.email, loggedUser.cf, loggedUser.dob);

                                            flag = false;
                                            startActivity(new Intent(LoginActivity.this,HomePageActivity.class));

                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<User> call, Throwable t) {
                                        Toast.makeText(LoginActivity.this,"Error during retrieving data.", Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Toast.makeText(LoginActivity.this,"Error during login, please make another attempt.", Toast.LENGTH_LONG).show();

                        }
                    });

                }

            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        btnForgotPWD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog();
            }
        });
    }

    public void initViews() {

        btnSignUp = findViewById(R.id.textSignUp);
        btnForgotPWD = findViewById(R.id.textForgotPWD);
        inputEmail = findViewById(R.id.inputEmail);
        inputPassword = findViewById(R.id.inputPwd);
        btnLogin = findViewById(R.id.btnLogin);

    }

    public boolean validateEmail() {
        String emailInput = inputEmail.getText().toString().trim();

        if (emailInput.isEmpty()) {
            inputEmail.setError("Field can't be empty!");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            inputEmail.setError("Please enter a valid email address.");
            return false;
        } else {
            inputEmail.setError(null);
            return true;
        }
    }

    public boolean validatePassword() {
        String pwdInput = inputPassword.getText().toString().trim();

        if (pwdInput.isEmpty()) {
            inputPassword.setError("Field can't be empty!");
            return false;
        } else {
            inputPassword.setError(null);
            return true;
        }
    }

    public void login(){
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("username", inputEmail.getText().toString())
                .addFormDataPart("password", inputPassword.getText().toString())
                .build();


    }

    private void openDialog() {

        ResetPWDDialog dialog = new ResetPWDDialog();
        dialog.show(getSupportFragmentManager(),"ResetPWDDialog");

    }
}