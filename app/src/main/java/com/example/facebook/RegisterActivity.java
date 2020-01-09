package com.example.facebook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.example.facebook.model.Login;
import com.example.facebook.model.Register;
import com.example.facebook.model.Response;
import com.example.facebook.model.User;
import com.example.facebook.network.RetrofitApi;
import com.example.facebook.utils.Constants;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener,RadioGroup.OnCheckedChangeListener{
    private LinearLayout layoutForWelcom;
    private LinearLayout layoutForFullName;
    private LinearLayout layoutForDob;
    private LinearLayout layoutForGender;
    private LinearLayout layoutForPhone,getLayoutForEmail,getLayoutForPassword;
    private TextView toolbarhead;
    private RelativeLayout layoutForFinish;
    TextInputEditText fname,lname,phone,email,password;
    DatePicker dob;
    SharedPreferences sharedPreferences;
    RadioGroup gender;

    Retrofit retrofit;
    private static final String TAG = "RegisterActivity";

    List<Response> regres = new ArrayList<>();


    String udob,firstname,lastname,ugender,uphone,uemail,upassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Button btnStart = findViewById(R.id.startsignup);
        Button btnTakeName = findViewById(R.id.btnTakeName);
        Button btnTakeDob = findViewById(R.id.btnTakeDob);
        Button btnTakeGender = findViewById(R.id.btnTakeGender);
        Button btnTakePhone = findViewById(R.id.btnTakePhone);
        Button btnTakeEmail = findViewById(R.id.btnTakeEmail);
        Button btnTakePassword = findViewById(R.id.btnTakePassword);
        Button btnSignupBack = findViewById(R.id.signupback);
        Button btnSignup = findViewById(R.id.btnTakeSignup);
        //for User Name
        fname = findViewById(R.id.fname);
        lname = findViewById(R.id.lname);

        //For Data of birth
        dob = findViewById(R.id.datePicker);
        Calendar c = Calendar.getInstance();
        c.set(2000, 11, 31);//Year,Mounth -1,Day
        dob.setMaxDate(c.getTimeInMillis());

        //for gender
        gender = findViewById(R.id.rgGender);

        //for Phone
        phone = findViewById(R.id.etPhone);

        //for Email
        email = findViewById(R.id.etEmail);

        //for Phone
        password = findViewById(R.id.etPassword);


        layoutForFullName = findViewById(R.id.layoutForFullName);
        layoutForDob = findViewById(R.id.layoutForDob);
        layoutForGender = findViewById(R.id.layoutForGender);
        layoutForPhone = findViewById(R.id.layoutForPhone);
        layoutForWelcom = findViewById(R.id.layoutForWelcome);
        toolbarhead = findViewById(R.id.toolbarhead);
        getLayoutForEmail = findViewById(R.id.layoutForEmail);
        getLayoutForPassword = findViewById(R.id.layoutForPassword);
        layoutForFinish = findViewById(R.id.layoutForSignUp);


        btnStart.setOnClickListener(this);
        btnTakeName.setOnClickListener(this);
        btnTakeDob.setOnClickListener(this);
        btnTakeGender.setOnClickListener(this);
        btnTakePhone.setOnClickListener(this);
        btnTakeEmail.setOnClickListener(this);
        btnTakePassword.setOnClickListener(this);
        btnSignupBack.setOnClickListener(this);
        gender.setOnCheckedChangeListener(this);
        btnSignup.setOnClickListener(this);

        sharedPreferences  = getSharedPreferences( "login", MODE_PRIVATE);

    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){
            case R.id.startsignup:

                layoutForFullName.setVisibility(View.VISIBLE);
                layoutForWelcom.setVisibility(View.GONE);
                toolbarhead.setText("Name");
                break;

            case R.id.btnTakeName:
                String ffname = fname.getText().toString();
                String llname = lname.getText().toString();
                if (TextUtils.isEmpty(ffname) || TextUtils.isEmpty(llname) ) {

                    fname.setError("Enter First Name");
                    lname.setError("Enter Last Name");
                }
                else {
                    layoutForDob.setVisibility(View.VISIBLE);
                    layoutForFullName.setVisibility(View.GONE);
                    toolbarhead.setText("Birthday");
                    firstname = fname.getText().toString();
                    lastname = lname.getText().toString();
                }
                break;

            case R.id.btnTakeDob:
                layoutForDob.setVisibility(View.GONE);
                layoutForGender.setVisibility(View.VISIBLE);
                toolbarhead.setText("Gender");
                udob= dob.getDayOfMonth()+"/"+ (dob.getMonth() + 1)+"/"+dob.getYear();

                break;
            case R.id.btnTakeGender:
                if (TextUtils.isEmpty(ugender)) {
                    Toast.makeText(this, "Select Your Gender", Toast.LENGTH_SHORT).show();
                }
                else{
                    layoutForGender.setVisibility(View.GONE);
                    layoutForPhone.setVisibility(View.VISIBLE);
                    toolbarhead.setText("Mobile Number");
                }
                break;
            case R.id.btnTakePhone:
                String ephone = phone.getText().toString();
                if (TextUtils.isEmpty(ephone)) {
                    phone.setError("Enter Your Phone");
                }
                else {
                    layoutForPhone.setVisibility(View.GONE);
                    getLayoutForEmail.setVisibility(View.VISIBLE);
                    toolbarhead.setText("Email");
                    uphone = phone.getText().toString();
                }
                break;
            case R.id.btnTakeEmail:
                String eemail = email.getText().toString();
                if (TextUtils.isEmpty(eemail)) {
                    email.setError("Enter Your Email");
                }
                else {
                    getLayoutForEmail.setVisibility(View.GONE);
                    getLayoutForPassword.setVisibility(View.VISIBLE);
                    toolbarhead.setText("Password");
                    uemail = email.getText().toString();

                }
                break;
            case R.id.btnTakePassword:
                String epassword = password.getText().toString();
                if (TextUtils.isEmpty(epassword)) {
                    password.setError("Enter Your Password");
                }
                else {
                    getLayoutForPassword.setVisibility(View.GONE);
                    layoutForFinish.setVisibility(View.VISIBLE);
                    toolbarhead.setText("Terms & Privacy");
                    upassword = password.getText().toString();

                }
                break;

            case R.id.signupback:
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.btnTakeSignup:

                register();

                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        if (i == R.id.rbMale) {
            ugender = "Male";
            Toast.makeText(this, "Male", Toast.LENGTH_SHORT).show();
        }
        if (i == R.id.rbFemale) {
            ugender = "Female";
            Toast.makeText(this, "Female", Toast.LENGTH_SHORT).show();
        }
        if (i == R.id.rbOther) {
            ugender = "Other";
            Toast.makeText(this, "Others", Toast.LENGTH_SHORT).show();
        }
    }


    private void register(){

        RetrofitApi api = com.example.facebook.network.Retrofit.retrofitInit().create(RetrofitApi.class);
        Call<List<Response>> registerresponse = api.register(new Register(firstname, lastname, udob, uphone , uemail, upassword));

     final   SharedPreferences.Editor edit = sharedPreferences.edit();

        registerresponse.enqueue(new Callback<List<Response>>() {
            @Override
            public void onResponse(Call<List<Response>> call, retrofit2.Response<List<Response>> response) {
                MainActivity.userlist = new ArrayList<>();
                if(response.isSuccessful()){
                    regres = response.body();
                   MainActivity.userlist.add(regres.get(0).getUser());
//                    Log.d(TAG, "onResponse: " + userlist.get(0).getEmail());
                    edit.putString(Constants.TOKEN, response.body().get(0).getToken());
                    edit.putString(Constants.EMAIL, MainActivity.userlist.get(0).getEmail());
                    edit.commit();

                    Intent intent = new Intent(RegisterActivity.this, DashboardActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(RegisterActivity.this, "Error While Registering", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Response>> call, Throwable t) {

            }
        });

    }

}