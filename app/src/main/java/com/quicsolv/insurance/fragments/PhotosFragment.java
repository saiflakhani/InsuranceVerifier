package com.quicsolv.insurance.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.quicsolv.insurance.CheckListActivity;
import com.quicsolv.insurance.MainActivity;
import com.quicsolv.insurance.R;
import com.quicsolv.insurance.ShowPhotoActivity;
import com.quicsolv.insurance.adapters.PhotosAdapter;
import com.quicsolv.insurance.apiCalls.ApiService;
import com.quicsolv.insurance.apiCalls.RetrofitClient;
import com.quicsolv.insurance.pojo.ApplicantDataVO;
import com.quicsolv.insurance.pojo.PhotoVO;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static com.quicsolv.insurance.MainActivity.pendingTasksList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PhotosFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PhotosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PhotosFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private Uri fileUri;
    static String picturePath;
    static String pictureName;
    PhotosAdapter adapter;
    LayoutInflater inflater;
    String returnString = "";
    List<PhotoVO> listOfPhotos;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public PhotosFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PhotosFragment.
     */
    // TODO: Rename and change types and number of parameters
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
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_photos, container, false);
        this.inflater = inflater;
        FloatingActionButton fabAddPhotos = rootView.findViewById(R.id.fabAddPhoto);
        fabAddPhotos.setOnClickListener(clickPicListener);
        listOfPhotos = CheckListActivity.applicantDataVO.getPhotoList();

        if(listOfPhotos==null)
            listOfPhotos = new ArrayList<PhotoVO>();

        Log.d("Photos", "This applicant has "+listOfPhotos.size()+" photos");
        adapter = new PhotosAdapter((ArrayList<PhotoVO>)listOfPhotos,getContext());
        ListView lVAddPhotos = rootView.findViewById(R.id.lVAddPhotos);
        lVAddPhotos.setAdapter(adapter);
        lVAddPhotos.setOnItemClickListener(photoClick);
        //getSharedPreferencesIfExist();
        //TODO SYNC PHOTO BITMAPS FROM SERVER
        return rootView;
    }

    private void getSharedPreferencesIfExist()
    {
        SharedPreferences pref = getContext().getSharedPreferences(CheckListActivity.applicantDataVO.getUniqueID(), 0);
        String jsonString = pref.getString("photosList","[]");
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<PhotoVO>>(){}.getType();
        ArrayList<PhotoVO> photoVOArrayList = gson.fromJson(jsonString,type);
        Log.d("PHOTOVO ARRAY LIST",""+photoVOArrayList.size());
        if(photoVOArrayList.size()>listOfPhotos.size()) listOfPhotos = photoVOArrayList;
        Log.d("PHOTOVO Array List",jsonString);
        adapter.notifyDataSetChanged();
    }

    private View.OnClickListener clickPicListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                // Create the File where the photo should go
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException ex) {
                    // Error occurred while creating the File
                }
                // Continue only if the File was successfully created
                if (photoFile != null) {

                    Uri photoURI = FileProvider.getUriForFile(getActivity(),
                            "com.quicsolv.insurance.fileprovider",
                            photoFile);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(takePictureIntent, 1);
                }
            }
        }
    };

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Bitmap bitmap = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(picturePath),100,100);
            showAlertDialog();
            CheckListActivity.photoTaken = true;
            //imageView.setImageBitmap(thumbImage);
            PhotoVO curPhoto = new PhotoVO();
            curPhoto.setDescription(returnString);
            curPhoto.setPhoto(BitMapToString(bitmap));
            curPhoto.setLocalPath(picturePath);
            curPhoto.setFileName(pictureName);
            curPhoto.setAssociatedUniqueID(CheckListActivity.applicantDataVO.getUniqueID());
            UUID uuid = UUID.randomUUID();
            String randomUUIDString = uuid.toString();
            curPhoto.setPhotoID(randomUUIDString);
            //curPhoto.setDescription("A new Photo");
            listOfPhotos.add(curPhoto);
            adapter.notifyDataSetChanged();
            CheckListActivity.applicantDataVO.setPhotoList(listOfPhotos);

            //savePhotoToSharedPrefs();
            //uploadPhotoToServer();
        }
    }

    private void showAlertDialog()
    {
        returnString = "";
        final View view = inflater.inflate(R.layout.alert_add_description, null);
        final AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.setTitle("Enter a Description");
        alertDialog.setCancelable(false);
        alertDialog.setMessage("This photo needs a description");


        final EditText etComments = (EditText) view.findViewById(R.id.etComments);

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    returnString = etComments.getText().toString();
                    listOfPhotos.get(listOfPhotos.size() - 1).setDescription(returnString);
                    adapter.notifyDataSetChanged();
                    CheckListActivity activity = (CheckListActivity)getActivity();
                    activity.saveStuffToServer(true,true);

                }catch (IndexOutOfBoundsException e)
                {
                    //THIS HAPPENS WHEN PHOTO IS REJECTED
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
                    listOfPhotos.remove(listOfPhotos.size()-1);
                    adapter.notifyDataSetChanged();
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
        SharedPreferences pref = getContext().getSharedPreferences(CheckListActivity.applicantDataVO.getUniqueID(), 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        Gson gson = new Gson();
        String jsonPendingList = gson.toJson(listOfPhotos);
        editor.putString("photosList",jsonPendingList);
        editor.apply();
        CheckListActivity.applicantDataVO.setPhotoList(listOfPhotos);
    }

    AdapterView.OnItemClickListener photoClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent i = new Intent(getActivity(), ShowPhotoActivity.class);
            i.putExtra("imageBitmap",listOfPhotos.get(position).getPhoto());
            i.putExtra("imagePath",listOfPhotos.get(position).getLocalPath());
            startActivity(i);
        }
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
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
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


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
