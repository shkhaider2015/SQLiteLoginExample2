package com.example.sqliteloginexample;

import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class LoginFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "LoginFragment";
    final FragmentManager fragmentManager = getFragmentManager();

    private EditText mEmail, mPassword;
    private Button mLogin, mSignup;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login, container, false);


        mLogin.setOnClickListener(this);
        mSignup.setOnClickListener(this);
        return view;
    }

    private void init(View view)
    {
        mEmail = view.findViewById(R.id.login_email);
        mPassword = view.findViewById(R.id.login_password);
        mLogin = view.findViewById(R.id.login_login);
        mSignup = view.findViewById(R.id.login_signup);
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.login_login:
                break;
            case R.id.login_signup:
                break;
        }
    }

    private void validateInfo()
    {
        String email, password;

        email = mEmail.getText().toString().trim();
        password = mPassword.getText().toString().trim();

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

        SQLiteHelper sqLiteHelper = new SQLiteHelper(getContext());

        if(sqLiteHelper.isEmailExists(email))
        {
            // goto next page
            nextPage();
        }
        else
        {
            // email not exist
            displayToast("Email is not exist");
            // goto signup page
        }

    }

    private void displayToast(String Message)
    {
        Toast.makeText(getContext(), Message, Toast.LENGTH_SHORT).show();
    }
    private void nextPage()
    {
        Fragment loginFragment = FragmentUtility.getFragmentByTagName(fragmentManager, "LoginFragment");

        if(loginFragment == null)
        {
            loginFragment = new LoginFragment();
        }
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout_fragment, loginFragment, "LoginFragment");
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

        FragmentUtility.printActivityFragmentList(fragmentManager);

    }
}
