package com.example.sqliteloginexample;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
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

public class SignupFragment extends Fragment implements View.OnClickListener {

    private static final int REQUEST_CAMERA = 121;

    private EditText
            mFullName,
            mEmail,
            mPassword,
            mConfirmPassword,
            mCellNumber;
    private ImageButton mProfilePicture;
    private Button mSignup;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.signup, container, false);
        init(view);

        mProfilePicture.setOnClickListener(this);
        mSignup.setOnClickListener(this);

        return view;
    }

    private void init(View view)
    {
        mFullName = view.findViewById(R.id.signup_name);
        mEmail = view.findViewById(R.id.signup_email);
        mPassword = view.findViewById(R.id.signup_password);
        mConfirmPassword = view.findViewById(R.id.signup_confirm_password);
        mCellNumber = view.findViewById(R.id.signup_phone);
        mProfilePicture = view.findViewById(R.id.signup_pic);
        mSignup = view.findViewById(R.id.signup_signup);
    }

    private void selectImage()
    {
        final CharSequence[] items = { "Take Photo", "Choose from Library", "Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                boolean result = Utility.checkPermission(getActivity());

                if(items[i].equals("Take Photo"))
                {
                    userChoosenTask = "Take Photo";
                    if(result)
                        cameraIntent();
                }
                else if(items[i].equals("Choose From Library"))
                {
                    userChoosenTask = "Choose From Library";
                    if(result)
                        galleryIntent();
                }
                else if(items[i].equals("Cancel"))
                {
                    dialogInterface.dismiss();
                }
            }
        });

        builder.show();
    }
    private void validateInfo()
    {
        String name, email, password, cPassword, cellNumber;

        name = mFullName.getText().toString();
        email =mEmail.getText().toString().trim();
        password =mPassword.getText().toString().trim();
        cPassword = mConfirmPassword.getText().toString().trim();
        cellNumber = mCellNumber.getText().toString().trim();

        if(name.isEmpty())
        {
            mFullName.requestFocus();
            return;
        }
        if(email.isEmpty())
        {
            mEmail.requestFocus();
            return;
        }
        if(password.isEmpty())
        {
            mPassword.requestFocus();
            return;
        }
        if(cPassword.isEmpty())
        {
            mConfirmPassword.requestFocus();
            return;
        }
        if(cellNumber.isEmpty())
        {
            mCellNumber.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            mEmail.requestFocus();
            return;
        }
        if(password.length() < 6)
        {
            mPassword.requestFocus();
            return;
        }
        if(!password.equals(cPassword))
        {
            mConfirmPassword.requestFocus();
            return;
        }
        if(!Patterns.PHONE.matcher(cellNumber).matches())
        {
            mCellNumber.requestFocus();
            return;
        }

        loadToDatabase(name, email, password, cellNumber);


    }

    private void loadToDatabase(String name, String email, String password, String cellNumber)
    {
        
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.signup_pic:
                break;
            case R.id.signup_signup:
                break;
        }
    }

    private void cameraIntent()
    {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, REQUEST_CAMERA);
    }
    private void galleryIntent()
    {
        
    }
}
