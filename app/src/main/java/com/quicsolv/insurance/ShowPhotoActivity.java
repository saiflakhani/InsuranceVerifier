package com.quicsolv.insurance;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;
import java.io.FileNotFoundException;

import static com.quicsolv.insurance.fragments.PhotosFragment.rotateBitmap;

public class ShowPhotoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_show_photo);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarshowphoto);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //Bitmap b = getIntent().getParcelableExtra("data");
        String file_path = getIntent().getStringExtra("imagePath");
//        setPic(file_path);
        ImageView iVPhoto = findViewById(R.id.iVshowImage);
        Glide.with(getBaseContext()).load(MainActivity.BASE_URL + file_path).apply(new RequestOptions().centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL)).thumbnail(0.5f).into(iVPhoto);

        //iVPhoto.setImageBitmap(b);

    }

//    private void setPic(String file_path) {
//        ImageView iVPhoto = findViewById(R.id.iVshowImage);
//        File file = new File(file_path);
//        if(file.exists()){
//            Bitmap bitmap = BitmapFactory.decodeFile(file_path);
//            bitmap = rotateBitmap(bitmap, 90);
//            iVPhoto.setImageBitmap(bitmap);
//        }else{
//            Toast.makeText(this, "File does not exist", Toast.LENGTH_SHORT).show();
//        };
//    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
