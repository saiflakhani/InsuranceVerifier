package com.quicsolv.insurance.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.quicsolv.insurance.CheckListActivity;
import com.quicsolv.insurance.ImagePickerActivity;
import com.quicsolv.insurance.MainActivity;
import com.quicsolv.insurance.R;
import com.quicsolv.insurance.ShowPhotoActivity;
import com.quicsolv.insurance.adapters.PhotosAdapter;
import com.quicsolv.insurance.apiCalls.ApiService;
import com.quicsolv.insurance.apiCalls.RetrofitClient;
import com.quicsolv.insurance.pojo.PhotoList;
import com.quicsolv.insurance.pojo.PhotoUploadResponse;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.quicsolv.insurance.CheckListActivity.photos;

public class PhotosFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    static String picturePath;
    static String pictureName;
    private Context mContext;
    PhotosAdapter adapter;
    LayoutInflater inflater;
    String returnString = "";

    public static final int REQUEST_IMAGE = 100;

    @Override
    public void onAttach(Context context) {
        mContext = context;
        super.onAttach(context);
    }

    public PhotosFragment() {
        // Required empty public constructor
    }

    public static PhotosFragment newInstance(String param1, String param2) {
        PhotosFragment fragment = new PhotosFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_photos, container, false);
        this.inflater = inflater;
        FloatingActionButton fabAddPhotos = rootView.findViewById(R.id.fabAddPhoto);
        fabAddPhotos.setOnClickListener(clickPicListener);
        photos = (ArrayList<PhotoList>) CheckListActivity.applicantDataVO.getPhotoList();
//        listOfPhotos = CheckListActivity.applicantDataVO.getPhotoList();

//        if(listOfPhotos==null)
//            listOfPhotos = new ArrayList<PhotoVO>();

//        Log.d("Photos", "This applicant has "+listOfPhotos.size()+" photos");
        adapter = new PhotosAdapter(photos, getContext());
        ListView lVAddPhotos = rootView.findViewById(R.id.lVAddPhotos);
        lVAddPhotos.setAdapter(adapter);
        lVAddPhotos.setOnItemClickListener(photoClick);
        //getSharedPreferencesIfExist();
        return rootView;
    }

    private View.OnClickListener clickPicListener = v -> onProfileImageClick();

    void onProfileImageClick() {
        Dexter.withActivity(getActivity())
                .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            showImagePickerOptions();
                        }

                        if (report.isAnyPermissionPermanentlyDenied()) {
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Grant Permissions");
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("Go to settings", (dialog, which) -> {
            dialog.cancel();
            openSettings();
        });
        builder.setNegativeButton(getString(android.R.string.cancel), (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", mContext.getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }


    private void showImagePickerOptions() {
        ImagePickerActivity.showImagePickerOptions(mContext, new ImagePickerActivity.PickerOptionListener() {
            @Override
            public void onTakeCameraSelected() {
                launchCameraIntent();
            }

            @Override
            public void onChooseGallerySelected() {
                launchGalleryIntent();
            }
        });
    }

    private void launchCameraIntent() {
        Intent intent = new Intent(mContext, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_IMAGE_CAPTURE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);

        // setting maximum bitmap width and height
        intent.putExtra(ImagePickerActivity.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, true);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_WIDTH, 1000);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_HEIGHT, 1000);

        startActivityForResult(intent, REQUEST_IMAGE);
    }

    private void launchGalleryIntent() {
        Intent intent = new Intent(mContext, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_GALLERY_IMAGE);

        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);
        startActivityForResult(intent, REQUEST_IMAGE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                Uri uri = data.getParcelableExtra("path");
                try {
                    // You can update this bitmap to your server
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), uri);

                    String fileName = CheckListActivity.applicantDataVO.getUniqueID() + Calendar.getInstance().getTime().toString().replace(" ", "-");
                    File f = new File(mContext.getCacheDir(), fileName + ".png");
                    f.createNewFile();

                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 0, bos);
                    byte[] bitmapData = bos.toByteArray();

                    FileOutputStream fos = new FileOutputStream(f);
                    fos.write(bitmapData);
                    fos.flush();
                    fos.close();

                    RequestBody fileReqBody = RequestBody.create(MediaType.parse("image/*"), f);
                    MultipartBody.Part part = MultipartBody.Part.createFormData("photo", f.getName(), fileReqBody);

//                    RequestBody description = RequestBody.create(MediaType.parse("text/plain"), "image-type");

                    showAlertDialog(part);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public static Bitmap rotateBitmap(Bitmap source, float angle)
    {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }
//
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//
//    }

    private void showAlertDialog(MultipartBody.Part part)
    {
        returnString = "";
        final View view = inflater.inflate(R.layout.alert_add_description, null);
        final AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.setTitle("Enter a Description");
        alertDialog.setCancelable(false);
        alertDialog.setMessage("This photo needs a description");


        final EditText etComments = view.findViewById(R.id.etComments);

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    returnString = etComments.getText().toString();
                    ApiService getProfileDetailsInterface = RetrofitClient.getClient(MainActivity.BASE_URL).create(ApiService.class);

                    getProfileDetailsInterface.postPhoto(CheckListActivity.applicantDataVO.getUniqueID(), etComments.getText().toString(), part).enqueue(new Callback<PhotoUploadResponse>() {
                        @Override
                        public void onResponse(@NonNull Call<PhotoUploadResponse> call, @NonNull Response<PhotoUploadResponse> response) {
                            if (response.code() == 200) {
                                photos.add(new PhotoList(response.body().getMsg(), etComments.getText().toString()));
                                adapter.notifyDataSetChanged();
                                Toast.makeText(mContext, "Photo uploaded successfully", Toast.LENGTH_SHORT).show();
                                Log.d("EditProfile", "Image captured");
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<PhotoUploadResponse> call, @NonNull Throwable t) {
                            Log.d("EditProfile", t.getMessage());
                            Toast.makeText(mContext, "Something went wrong. Please try again.", Toast.LENGTH_SHORT).show();
                        }
                    });

//                    listOfPhotos.get(listOfPhotos.size() - 1).setDescription(returnString);
//                    adapter.notifyDataSetChanged();
//                    if (getActivity() != null) {
//                        CheckListActivity activity = (CheckListActivity) getActivity();
//                        activity.saveStuffToServer(true, true);
//                    }
                }catch (IndexOutOfBoundsException e)
                {
                    Log.d("Photo","Rejected");
                }
            }
        });

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel Image", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
//                    returnString = etComments.getText().toString();
//                    listOfPhotos.get(listOfPhotos.size() - 1).setDescription(returnString);
//                    listOfPhotos.remove(listOfPhotos.size()-1);
//                    adapter.notifyDataSetChanged();
                }catch (IndexOutOfBoundsException e)
                {
                    //THIS HAPPENS WHEN PHOTO IS REJECTED
                    Log.d("Photo","Rejected");
                }
            }
        });
        alertDialog.setView(view);
        alertDialog.show();
    }

    private void savePhotoToSharedPrefs()
    {
//        SharedPreferences pref = getContext().getSharedPreferences(CheckListActivity.applicantDataVO.getUniqueID(), 0); // 0 - for private mode
//        SharedPreferences.Editor editor = pref.edit();
//        Gson gson = new Gson();
//        String jsonPendingList = gson.toJson(listOfPhotos);
//        editor.putString("photosList",jsonPendingList);
//        editor.apply();
//        CheckListActivity.applicantDataVO.setPhotoList(listOfPhotos);
    }

    AdapterView.OnItemClickListener photoClick = (parent, view, position, id) -> {
        Intent i = new Intent(getActivity(), ShowPhotoActivity.class);
//            i.putExtra("imageBitmap",listOfPhotos.get(position).getPhoto());
        i.putExtra("imagePath",photos.get(position).getPath());
        startActivity(i);
    };

    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp= Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "PNG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".png",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        picturePath = image.getAbsolutePath();
        pictureName = image.getName();

        return image;
    }
}
