package com.quicsolv.insurance;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;

public class ShowPhotoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_photo);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarshowphoto);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //Bitmap b = getIntent().getParcelableExtra("data");
        String file_path = getIntent().getStringExtra("imagePath");
        setPic(file_path);


        //iVPhoto.setImageBitmap(b);

    }

    private void setPic(String file_path) {
        ImageView iVPhoto = findViewById(R.id.iVshowImage);
        File file = new File(file_path);
        if(file.exists()){
                Bitmap bitmap = BitmapFactory.decodeFile(file_path);
                iVPhoto.setImageBitmap(bitmap);
        }else{
            Toast.makeText(this, "File does not exist", Toast.LENGTH_SHORT).show();
        };
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
