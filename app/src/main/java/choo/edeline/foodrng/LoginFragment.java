package choo.edeline.foodrng;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class LoginFragment extends Fragment {

    DatabaseHelper mDatabaseHelper;

    private TextInputLayout mName, mPassword;
    private Button mLogin;
    private TextView mRegister;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_login, container, false);

        mName = (TextInputLayout)mView.findViewById(R.id.etUsername);
        mPassword = (TextInputLayout)mView.findViewById(R.id.etPassword);
        mLogin = (Button)mView.findViewById(R.id.btnLogin);

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateUser(mName.getEditText().getText().toString(), mPassword.getEditText().getText().toString());
            }
        });

        mRegister = (TextView)mView.findViewById(R.id.tvRegister);
        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               RegisterFragment registerFragment = new RegisterFragment();
               FragmentTransaction transaction = getFragmentManager().beginTransaction();
               transaction.replace(R.id.loginContainer, registerFragment);
               transaction.commit();
            }
        });

        return mView;
    }

    private boolean validateUsername(String userName) {
        if (userName.isEmpty()) {
            mName.setError("Field can't be empty.");
            return false;
        } else {
            mName.setError(null);
            return true;
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

    private void validateUser(String userName, String userPassword) {
        if (validateUsername(userName) && validatePassword(userPassword)) {
            mDatabaseHelper = new DatabaseHelper(getActivity());
            if (mDatabaseHelper.checkIfUserExist(userName)) {
                if ( mDatabaseHelper.validateUserCredentials(userName, userPassword)) {
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    intent.putExtra("username", userName);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(getActivity(), "Incorrect password.",
                            Toast.LENGTH_SHORT).show();
                }
            }
            else {
                Toast.makeText(getActivity(), "Login Failed!",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
}