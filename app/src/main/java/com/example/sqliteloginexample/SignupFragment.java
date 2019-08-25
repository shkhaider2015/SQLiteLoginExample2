package com.example.sqliteloginexample;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class SignupFragment extends Fragment implements View.OnClickListener {

    private static final int REQUEST_CAMERA = 0;
    private static final int SELECT_FILE = 1;
    private static final String TAG = "SignupFragment";
    final FragmentManager fragmentManager = getFragmentManager();

    private String userChoosenTask = "Nothing";
    //private boolean isImageSelected = false;

    private EditText
            mFullName,
            mEmail,
            mPassword,
            mConfirmPassword,
            mCellNumber;
    private ImageButton mProfilePicture;
    private Button mSignup;
    private byte[] profileImage = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.signup, container, false);
        init(view);

        mProfilePicture.setOnClickListener(this);
        mSignup.setOnClickListener(this);

        return view;
    }

    private void init(View view) {
        mFullName = view.findViewById(R.id.signup_name);
        mEmail = view.findViewById(R.id.signup_email);
        mPassword = view.findViewById(R.id.signup_password);
        mConfirmPassword = view.findViewById(R.id.signup_confirm_password);
        mCellNumber = view.findViewById(R.id.signup_phone);
        mProfilePicture = view.findViewById(R.id.signup_pic);
        mSignup = view.findViewById(R.id.signup_signup);
    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                boolean result = Utility.checkPermission(getActivity());

                if (items[i].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    if (result)
                        cameraIntent();
                } else if (items[i].equals("Choose From Library")) {
                    userChoosenTask = "Choose From Library";
                    if (result)
                        galleryIntent();
                } else if (items[i].equals("Cancel")) {
                    dialogInterface.dismiss();
                }
            }
        });

        builder.show();
    }

    private void validateInfo() {
        String name, email, password, cPassword, cellNumber;
        byte[] image;


        name = mFullName.getText().toString();
        email = mEmail.getText().toString().trim();
        password = mPassword.getText().toString().trim();
        cPassword = mConfirmPassword.getText().toString().trim();
        cellNumber = mCellNumber.getText().toString().trim();

        if (name.isEmpty()) {
            mFullName.requestFocus();
            return;
        }
        if (email.isEmpty()) {
            mEmail.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            mPassword.requestFocus();
            return;
        }
        if (cPassword.isEmpty()) {
            mConfirmPassword.requestFocus();
            return;
        }
        if (cellNumber.isEmpty()) {
            mCellNumber.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mEmail.requestFocus();
            return;
        }
        if (password.length() < 6) {
            mPassword.requestFocus();
            return;
        }
        if (!password.equals(cPassword)) {
            mConfirmPassword.requestFocus();
            return;
        }
        if (!Patterns.PHONE.matcher(cellNumber).matches()) {
            mCellNumber.requestFocus();
            return;
        }
        if (profileImage == null )
        {
            setDrawableResource();
        }

        loadToDatabase(name, email, password, cellNumber, profileImage);

        nextPage();

    }

    private void loadToDatabase(String name, String email, String password, String cellNumber, byte[] profileImage)
    {
        UserDataModel user = new UserDataModel();
        SQLiteHelper db = new SQLiteHelper(getContext());

        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);
        user.setCellNumber(cellNumber);
        user.setProfilePicture(profileImage);

        db.addUsers(user);

        SessionManager sessionManager = new SessionManager(getContext());
        sessionManager.setEmail(email);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.signup_pic:
                selectImage();
                break;
            case R.id.signup_signup:
                validateInfo();
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (userChoosenTask.equals("Take Photo")) {
                        cameraIntent();
                    } else if (userChoosenTask.equals("Choose From Library")) {
                        galleryIntent();
                    }
                } else {

                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE) {
                onSelectFromGalleryResult(data);
            } else if (requestCode == REQUEST_CAMERA) {
                onCaptureImageResult(data);
            }
        }
    }

    private void cameraIntent() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, REQUEST_CAMERA);
    }

    private void galleryIntent() {
        Intent galleryIntent = new Intent();
        galleryIntent.setType("image/*");
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(galleryIntent, "Select File"), SELECT_FILE);
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        Bitmap bm = null;

        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getActivity().getApplicationContext().getContentResolver(), data.getData());
            } catch (FileNotFoundException e) {
                Log.d(TAG, "onSelectFromGalleryResult: Error -->> " + e.getMessage());
                e.printStackTrace();

            } catch (IOException e) {
                Log.d(TAG, "onSelectFromGalleryResult: Error -->> " + e.getMessage());
                e.printStackTrace();
            } catch (Exception e) {
                Log.d(TAG, "onSelectFromGalleryResult: Error --> " + e.getMessage());
                e.printStackTrace();
            }
        }

        mProfilePicture.setImageBitmap(bm);
        //isImageSelected = true;
        setByteArray(bm);
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis() + ".jpg");
        FileOutputStream fo;

        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            //byte[] bytesArray= bytes.toByteArray();
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (IOException e) {
            Log.d(TAG, "onCaptureImageResult: IOException " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            Log.d(TAG, "onCaptureImageResult: Exception " + e.getMessage());
            e.printStackTrace();
        }


        mProfilePicture.setImageBitmap(thumbnail);
        //isImageSelected = true;
        setByteArray(thumbnail);

    }

    private void setByteArray(Bitmap bitmap)
    {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream);
        profileImage = byteArrayOutputStream.toByteArray();

    }
    private void setDrawableResource()
    {
        Bitmap tempData = BitmapFactory.decodeResource(getResources(), R.drawable.temp_user);
        setByteArray(tempData);
    }

    private void nextPage()
    {
        Fragment HomeFragment = FragmentUtility.getFragmentByTagName(fragmentManager, "HomeFragment");

        if(HomeFragment == null)
        {
            HomeFragment = new HomeFragment();
        }

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout_fragment, HomeFragment, "HomeFragment");
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

        FragmentUtility.printActivityFragmentList(fragmentManager);

    }

}
