package com.example.ecommerce;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.example.ecommerce.dao.UserDAO;
import com.example.ecommerce.entity.Role;
import com.example.ecommerce.entity.User;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "Upload ###";
    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView imageViewAvatar;
    private Button btnAvatar, btnRegister;
    private EditText edtUsername, edtPassword, edtFullname, edtEmail;
    private Spinner spinnerRole;
    private Uri selectedImageUri;
    private String cloudinaryPath;
    private TextView tvErrorUsername, tvErrorPassword, tvErrorFullname, tvErrorEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();

        spinnerRole = (Spinner) findViewById(R.id.spinner_role);
        imageViewAvatar = (ImageView) findViewById(R.id.imgView_Avatar);
        btnAvatar = (Button) findViewById(R.id.btnAvatar);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        edtUsername = (EditText) findViewById(R.id.edtUsername);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        edtFullname = (EditText) findViewById(R.id.edtFullname);
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        tvErrorUsername = (TextView) findViewById(R.id.tvErrorUsername);
        tvErrorPassword = (TextView) findViewById(R.id.tvErrorPassword);
        tvErrorFullname = (TextView) findViewById(R.id.tvErrorFullname);
        tvErrorEmail = (TextView) findViewById(R.id.tvErrorEmail);


        initConfig();

        // Set Adapter for Spinner Role
        List<String> roleDisplayNames = new ArrayList<>();
        for (Role role: Role.values()) {
            roleDisplayNames.add(role.getDisplayName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                roleDisplayNames
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerRole.setAdapter(adapter);

        // Set OnClick event for button Avatar
        btnAvatar.setOnClickListener(this);
        // Set OnClick event for button Register
        btnRegister.setOnClickListener(this);

    }

    private void initConfig() {
//        Map<String, Object> config = new HashMap<>();
//        config.put("cloud_name", "dku2oqsgy");
//        config.put("api_key", "881488226939423");
//        config.put("api_secret", "XVTKFf5IFYzFv6GajJXeUpRuI70");
        CloudinaryManager.init(this);
    }

    @Override
    public void onClick(View v) {
        if (v == findViewById(R.id.btnAvatar)){
            requestPermissions();
        } else if (v == findViewById(R.id.btnRegister)) {
            CloudinaryManager.getInstance().upload(selectedImageUri).callback(new UploadCallback() {
                @Override
                public void onStart(String requestId) {
                    Log.d(TAG, "onStart: " + "started");
                }

                @Override
                public void onProgress(String requestId, long bytes, long totalBytes) {
                    Log.d(TAG, "onStart: " + "uploading");
                }

                @Override
                public void onSuccess(String requestId, Map resultData) {
                    cloudinaryPath = (String) resultData.get("url");
                    Log.d(TAG, "onStart: " + "success");
                }

                @Override
                public void onError(String requestId, ErrorInfo error) {
                    Log.d(TAG, "onStart: " + error);
                }

                @Override
                public void onReschedule(String requestId, ErrorInfo error) {
                    Log.d(TAG, "onStart: " + error);
                }
            }).dispatch();
            String username = edtUsername.getText().toString().trim();
            String password = edtPassword.getText().toString().trim();
            String fullname = edtFullname.getText().toString().trim();
            String email = edtEmail.getText().toString().trim();
            String role = spinnerRole.getSelectedItem().toString();

            String avatar = cloudinaryPath != null ? cloudinaryPath : null;

            boolean isValid = true;

            if (username.isEmpty()){
                tvErrorUsername.setText("Tên đăng nhập không được để trống");
                tvErrorUsername.setVisibility(View.VISIBLE);
                isValid = false;
            } else {
                tvErrorUsername.setVisibility(View.GONE);
            }

            if (password.isEmpty()) {
                tvErrorPassword.setText("Mật khẩu không được để trống");
                tvErrorPassword.setVisibility(View.VISIBLE);
                isValid = false;
            } else if (password.length() < 6) {
                tvErrorPassword.setText("Mật khẩu phải có ít nhất 6 ký tự");
                tvErrorPassword.setVisibility(View.VISIBLE);
                isValid = false;
            } else {
                tvErrorPassword.setVisibility(View.GONE);
            }

            if (fullname.isEmpty()) {
                tvErrorFullname.setText("Họ và tên không được để trống");
                tvErrorFullname.setVisibility(View.VISIBLE);
                isValid = false;
            } else {
                tvErrorFullname.setVisibility(View.GONE);
            }

            if (email.isEmpty()) {
                tvErrorEmail.setText("Email không được để trống");
                tvErrorEmail.setVisibility(View.VISIBLE);
                isValid = false;
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                tvErrorEmail.setText("Email không hợp lệ");
                tvErrorEmail.setVisibility(View.VISIBLE);
                isValid = false;
            } else {
                tvErrorEmail.setVisibility(View.GONE);
            }

            if (avatar == null) {
                isValid = false;
                Toast.makeText(RegisterActivity.this, "Bắt buộc phải chọn Avatar!", Toast.LENGTH_SHORT).show();
            }

            if (isValid) {
                UserDAO userDAO = new UserDAO(this);
                userDAO.open();

                if (userDAO.isUsernameExists(username)) {
                    tvErrorUsername.setText("Tên đăng nhập đã tồn tại!");
                    tvErrorUsername.setVisibility(View.VISIBLE);
                } else {
                    User newUser = new User(username, password, fullname, email, role, avatar);
                    boolean success = userDAO.registerUser(newUser);

                    if (success) {
                        Toast.makeText(this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        intent.putExtra("username", username);
                        startActivity(intent);
                    } else {
                        Toast.makeText(this, "Đăng ký thất bại", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

    private void requestPermissions() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){ //Android 13+
            if (ContextCompat.checkSelfPermission(RegisterActivity.this, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED){
                selectImage();
            } else {
                ActivityCompat.requestPermissions(RegisterActivity.this, new String[]{
                        Manifest.permission.READ_MEDIA_IMAGES
                }, PICK_IMAGE_REQUEST);
            }
        } else { //Android 12 và thấp hơn
            if (ContextCompat.checkSelfPermission(RegisterActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                selectImage();
            } else {
                ActivityCompat.requestPermissions(RegisterActivity.this, new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE
                }, PICK_IMAGE_REQUEST);
            }
        }
    }

    // select the image from the gallery
    private void selectImage() {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        mActivityResultLauncher.launch(i);
    }

    ActivityResultLauncher<Intent> mActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult o) {
                    if (o.getResultCode() == Activity.RESULT_OK) {
                        Intent data = o.getData();
                        selectedImageUri=data.getData();
                        Picasso.get().load(selectedImageUri).into(imageViewAvatar);
                        imageViewAvatar.setVisibility(View.VISIBLE);
                    }
                }
            }

    );

}