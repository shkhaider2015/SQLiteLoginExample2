package com.example.sqliteloginexample;

import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class SignupFragment extends Fragment {

    private EditText
            mFullName,
            mEmail,
            mPassword,
            mConfirmPassword,
            mCellNumber;
    private ImageButton mProfilePicture;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.signup, container, false);
        init(view);

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


    }
}
