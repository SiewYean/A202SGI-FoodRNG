package choo.edeline.foodrng;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterFragment extends Fragment {

    DatabaseHelper mDatabaseHelper;

    private TextInputLayout mName, mPassword, mReenterPassword;
    private Button mRegister;
    private TextView mLogin;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_register, container, false);

        mName = (TextInputLayout)mView.findViewById(R.id.etNewUsername);
        mPassword = (TextInputLayout)mView.findViewById(R.id.etNewPassword);
        mReenterPassword = (TextInputLayout)mView.findViewById(R.id.etReenterNewPassword);
        mRegister = (Button)mView.findViewById(R.id.btnRegister);

        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateUser(mName.getEditText().getText().toString(), mPassword.getEditText().getText().toString(),
                        mReenterPassword.getEditText().getText().toString());
            }
        });

        mLogin = (TextView)mView.findViewById(R.id.tvLogin);
        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginFragment loginFragment = new LoginFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.loginContainer, loginFragment);
                transaction.commit();
            }
        });
        return mView;
    }

    private boolean validateUsername(String userName) {
        mDatabaseHelper = new DatabaseHelper(getActivity()
        );
        if (userName.isEmpty()) {
            mName.setError("Field can't be empty.");
            return false;
        } else {
            if(mDatabaseHelper.checkIfUserExist(userName)){
                mName.setError("This username is used.");
                return false;
            }
            else {
                mName.setError(null);
                return true;
            }
        }
    }

    private boolean validatePassword(String userPassword) {
        if (userPassword.isEmpty()) {
            mPassword.setError("Field can't be empty.");
            return false;
        } else {
            mPassword.setError(null);
            return true;
        }
    }

    private boolean validateReenterPassword(String userReenterPassword, String userPassword) {
        if (userReenterPassword.isEmpty()) {
            mReenterPassword.setError("Field can't be empty.");
            return false;
        } else {
            if (userPassword.equals(userReenterPassword)) {
                mReenterPassword.setError(null);
                return true;
            }
            else {
                mReenterPassword.setError("Password entered does not match");
                return false;
            }
        }
    }

    private void validateUser(String userName, String userPassword, String userReenterPassword) {
        if (validateUsername(userName) && validatePassword(userPassword)
                && validateReenterPassword(userReenterPassword, userPassword)) {
            mDatabaseHelper = new DatabaseHelper(getActivity());
            mDatabaseHelper.addUser(userName, userPassword);
            Toast.makeText(getActivity(), "Registration Successful!", Toast.LENGTH_SHORT).show();

            LoginFragment loginFragment = new LoginFragment();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.loginContainer, loginFragment);
            transaction.commit();
        } else {
            if (mDatabaseHelper.checkIfUserExist(userName)) {
                Toast.makeText(getActivity(), "This username is already registered.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "Registration Failed!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
