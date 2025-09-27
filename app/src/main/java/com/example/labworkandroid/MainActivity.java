package com.example.labworkandroid;

import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private EditText loginTxtBx;
    private EditText passwordTxtBx;
    private TextView statusTxtBx;
    private CheckBox showPasswordChckBx;
    private Button enterBtn;
    private Button clearBtn;
    private final String LOGIN_IS_EMPTY = "Введите логин!";
    private final String PASSWORD_IS_EMPTY = "Введите пароль!";
    private final String LOGIN_USER = "m.plugov@aues.kz";
    private final String PASSWORD_USER = "123MAX";
    private final String AUTHORIZATION_SUCCESS = "Авторизация прошла успешно!";
    private final String AUTHORIZATION_FAIL = "Авторизация не прошла!";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();
        clickEnterBtn();
        clickShowPasswordRdBtn();
        setupClearButton();
    }

    private void initViews(){
        loginTxtBx = findViewById(R.id.loginTxtBx);
        passwordTxtBx = findViewById(R.id.passwordTxtBx);
        statusTxtBx = findViewById(R.id.statusTxtBx);
        showPasswordChckBx = findViewById(R.id.showPasswordChckBx);
        enterBtn = findViewById(R.id.enterBtn);
        clearBtn = findViewById(R.id.clearBtn);
    }

    private void clickEnterBtn(){
        enterBtn.setOnClickListener(v -> {
            if (!checkFieldsOnEmpty()) {
                return;
            }

            String login = loginTxtBx.getText().toString();
            String password = passwordTxtBx.getText().toString();

            if(login.equals(LOGIN_USER) && password.equals(PASSWORD_USER)){
                statusTxtBx.setText(AUTHORIZATION_SUCCESS);
                statusTxtBx.setBackgroundColor(ContextCompat.getColor(this, R.color.auth_success));
                statusTxtBx.setVisibility(View.VISIBLE);
            } else {
                statusTxtBx.setText(AUTHORIZATION_FAIL);
                statusTxtBx.setBackgroundColor(ContextCompat.getColor(this, R.color.auth_fail));
                statusTxtBx.setVisibility(View.VISIBLE);
            }
        });
    }

    private void clickShowPasswordRdBtn(){
        showPasswordChckBx.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked){
                passwordTxtBx.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            } else {
                passwordTxtBx.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }
            passwordTxtBx.setSelection(passwordTxtBx.getText().length());
        });
    }

    private boolean checkFieldsOnEmpty(){
        if(loginTxtBx.getText().toString().isEmpty()){
            Toast.makeText(this, LOGIN_IS_EMPTY, Toast.LENGTH_SHORT).show();
            return false;
        }
        if(passwordTxtBx.getText().toString().isEmpty()){
            Toast.makeText(this, PASSWORD_IS_EMPTY, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void setupClearButton() {
        clearBtn.setOnClickListener(v -> {
            loginTxtBx.getText().clear();
            passwordTxtBx.getText().clear();
            showPasswordChckBx.setChecked(false);
            statusTxtBx.setVisibility(View.INVISIBLE);
        });
    }
}