package com.example.abyandafa.uploadimage;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.abyandafa.uploadimage.Model.APIResponse;
import com.example.abyandafa.uploadimage.Service.APIService;
import com.example.abyandafa.uploadimage.Service.BaseRestService;

import java.io.File;
import java.util.Calendar;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Multipart;

public class MainActivity extends AppCompatActivity {



    // your authority, must be the same as in your manifest file
    private static final String CAPTURE_IMAGE_FILE_PROVIDER = "com.example.abyandafa.uploadimage.fileprovider";

    private Button upload;
    private TextView textView;
    private static final int CAMERA_REQUEST_CODE = 200;
    private Boolean cameraPermission;
    private ImageView foto;

    File image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView textView = (TextView) findViewById(R.id.textView);
        foto = (ImageView) findViewById(R.id.foto);
        cameraPermission = false;
        getCameraPermission();

        upload = (Button) findViewById(R.id.upload);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cameraPermission == true)
                {
                    File path = new File(MainActivity.this.getFilesDir(), "hasilupload");
                    Log.d("1byan", path.getPath());
                    if (!path.exists()) path.mkdirs();
                    Log.d("2byan", path.getPath());
                    image = new File(path, Calendar.getInstance().getTimeInMillis() + ".jpg");
                    Uri imageUri = FileProvider.getUriForFile(MainActivity.this, CAPTURE_IMAGE_FILE_PROVIDER, image);

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    Log.d("MASUK", "onClick: ");
                    startActivityForResult(intent, CAMERA_REQUEST_CODE);
                }
                else getCameraPermission();
                Log.d("Keluar", "onClick: ");
            }
        });
    }

    private void getCameraPermission()
    {
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED ) {
            cameraPermission = true;


        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE}, CAMERA_REQUEST_CODE);

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode)
        {
            case 200:
                if(grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    cameraPermission = true;
                    Log.d("Masuk nih", "onRequestPermissionsResult: ");
                }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Log.d(image.getPath(), "onActivityResult: ");
                Glide.with(this).load(image).into(foto);

                RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), image);
                MultipartBody.Part body = MultipartBody.Part.createFormData("file", image.getName(), reqFile);
                RequestBody email = RequestBody.create(MediaType.parse("text/plain"), "email");
                RequestBody longitude = RequestBody.create(MediaType.parse("text/plain"), "longitude");
                RequestBody latitude = RequestBody.create(MediaType.parse("text/plain"), "latitude");
                RequestBody lokasi = RequestBody.create(MediaType.parse("text/plain"), "lokasi");
                RequestBody foto = RequestBody.create(MediaType.parse("text/plain"), "foto");
                RequestBody ambulan = RequestBody.create(MediaType.parse("text/plain"), "ambulan12");

                APIService apiService = new BaseRestService().initializeRetrofit(1).create(APIService.class);
                Call<APIResponse> result = apiService.postImage(body, email, longitude, latitude, lokasi, foto, ambulan);

                result.enqueue(new Callback<APIResponse>() {
                    @Override
                    public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                        if(response.isSuccessful())
                        {
                            Log.d("BERHASIL", "onResponse: ");
                        }
                        else
                        {
                            Log.d("hehe", "onResponse: ");
                        }
                    }

                    @Override
                    public void onFailure(Call<APIResponse> call, Throwable t) {
                        Log.d("Gagal", "onFailure: " + t.getMessage());
                        t.printStackTrace();

                    }
                });

            }
        }

    }
}
