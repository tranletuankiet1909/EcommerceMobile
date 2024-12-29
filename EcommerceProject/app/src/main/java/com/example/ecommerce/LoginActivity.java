package com.example.ecommerce;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerce.entity.Role;
import com.example.ecommerce.entity.User;

import androidx.appcompat.app.AppCompatActivity;


import com.example.ecommerce.dao.UserDAO;

public class LoginActivity extends AppCompatActivity {
    private EditText edtUsername, edtPassword;
    private Button btnLogin;
    private TextView txtViewLinkToRegister;
    private UserDAO userDAO;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        edtUsername = (EditText) findViewById(R.id.editText_username);
        edtPassword = (EditText) findViewById(R.id.editText_password);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        txtViewLinkToRegister = (TextView) findViewById(R.id.tvRegisterLink);

        //Hứng intent từ phía Register
        Intent i = getIntent();
        String usernameFromRegister = i.getStringExtra("username");
        if (usernameFromRegister != null && !usernameFromRegister.isEmpty()) {
            edtUsername.setText(usernameFromRegister);
        }

        //Khởi tạo userDAO
        userDAO = new UserDAO(this);
        userDAO.open();

        btnLogin.setOnClickListener(view -> {
            String username = edtUsername.getText().toString().trim();
            String password = edtPassword.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            } else {
                User user = userDAO.getUserByUsername(username, password);
                if (user != null) {
                    UserManager.getInstance().setCurrentUser(user);
                    // Phân quyền màn hình tại đây
                    if (user.getRole().equals(Role.BUYER.getDisplayName())) {
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else if (user.getRole().equals(Role.SELLER.getDisplayName())) {
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else if (user.getRole().equals(Role.ADMIN.getDisplayName())) {
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Sai tên mật khẩu hoặc đăng nhập!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        txtViewLinkToRegister.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        userDAO.close();
    }
}