package com.example.designapp;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class SignUpScreen extends AppCompatActivity {
    private int PICK_IMAGE = 1;
    private String currentPhotoPath;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE) {
            //TODO: action
            Log.d("dsfjkadsljf__", "Uri :" + data.getData());
            Toast.makeText(this, "" + data, Toast.LENGTH_SHORT).show();
            circleImageView.setImageURI(data.getData());

        }
        else if (requestCode==2)
        {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            circleImageView.setImageBitmap(bitmap);
        }

    }

    private ImageView back_to_home;
    private TextView change_Mode;
    private CircleImageView circleImageView;
    private EditText username, email, phone, password, confirmpassword;
    private Button signUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signupscreen);
        init();
        change_Mode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpScreen.this, LoginScreen.class));
            }
        });

        back_to_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpScreen.this, LoginScreen.class));
            }
        });
        circleImageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder alertdialog = new AlertDialog.Builder(SignUpScreen.this);
                String[] list = {"Camera", "Gallery"};
                alertdialog.setItems(list, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0: {
                                   Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                   startActivityForResult(camera,2);
                                break;
                            }
                            case 1: {
                                Intent intent = new Intent();
                                intent.setType("image/*");
                                intent.setAction(Intent.ACTION_GET_CONTENT);
                                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
                                break;
                            }

                        }
                    }
                });
                alertdialog.create().show();
                return true;
            }
        });
        signUp.setOnClickListener(v -> {

            if (!username.getText().toString().isEmpty()) {
                if (username.getText().toString().length() > 20) {
                    username.setError("invalid username length");
                }

            }
            if (!email.getText().toString().isEmpty()) {
                if (email.getText().toString().length() > 40) {
                    email.setError("Invalid Email Length");
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
                    email.setError("Invalid Email Form");
                }
            }

            if (!phone.getText().toString().isEmpty()) {
                if (phone.getText().toString().length() > 11) {
                    phone.setError("invalid phone number");
                }
            }
            if (!(password.getText().toString().isEmpty() && confirmpassword.getText().toString().isEmpty())) {
                if (password.getText().toString().toLowerCase().equalsIgnoreCase(confirmpassword.getText().toString().toLowerCase())) {
                    Toast.makeText(getApplicationContext(), "Password match Successfully", Toast.LENGTH_SHORT).show();
                } else {
                    password.setError("Invalid password");
                    confirmpassword.setError("Invalid password");
                }
            }
            startActivity(new Intent(this,PhoneActivity.class));
            finish();

        });
    }
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {

            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.designapp.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, 2);
            }
        }
    }

    private void checkPermission() {
        ArrayList<String> permissionList = new ArrayList<>();
        int camera_permission = ActivityCompat.checkSelfPermission(SignUpScreen.this, Manifest.permission.CAMERA);
        int read_permission = ActivityCompat.checkSelfPermission(SignUpScreen.this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int write_permission = ActivityCompat.checkSelfPermission(SignUpScreen.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

//     if (camera_permission== PackageManager.PERMISSION_DENIED)
//     {
//         permissionList.add(Manifest.permission.CAMERA);
//     }
        if (read_permission == PackageManager.PERMISSION_DENIED) {
            permissionList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (write_permission == PackageManager.PERMISSION_DENIED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (permissionList.size() > 0) {
            String[] permission = new String[permissionList.size()];
            for (int i = 0; i < permission.length; i++) {
                permission[i] = permissionList.get(i);
            }
            ActivityCompat.requestPermissions(SignUpScreen.this, permission, 3);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 3) {
            for (int i = 0; i < permissions.length; i++) {
                if (permissions.length > 0 && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), permissions[i] + " Granted SuccessFully", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void init() {
        back_to_home = findViewById(R.id.sign_up_back_btn);
        change_Mode = findViewById(R.id.change_to_login);
        username = findViewById(R.id.sign_username);
        email = findViewById(R.id.sign_email);
        phone = findViewById(R.id.sign_phone);
        password = findViewById(R.id.sign_password);
        confirmpassword = findViewById(R.id.sin_confirmpassword);
        signUp = findViewById(R.id.btn_sign);
        circleImageView = findViewById(R.id.user_image);
    }
}
