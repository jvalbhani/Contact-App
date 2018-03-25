package com.example.jayesh.jumpcontatcts;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
public class SignUpFragment extends Fragment {

    View currentActivity;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        currentActivity=inflater.inflate(R.layout.fragment_sign_up,container,false);
        container.removeAllViewsInLayout();
        mAuth = FirebaseAuth.getInstance();

        ((Button)currentActivity.findViewById(R.id.signUp)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validateForm())
                    createAccount(((EditText)currentActivity.findViewById(R.id.emailField)).getText().toString(),((EditText)currentActivity.findViewById(R.id.passwordField)).getText().toString());
            }
        });

        return currentActivity;
    }

    private FirebaseAuth mAuth;
    private void createAccount(String email, String password) {
        Log.d("Create Account Selcted", "createAccount:" + email);
        if (!validateForm()) {
            return;
        }
        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener( getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            Log.d("Account created", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            user.sendEmailVerification();
                            Toast.makeText(getActivity(),"Verification email has been sent please verify",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(currentActivity.getContext(),MainActivity.class));
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("Account Failure", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(getActivity(), "Authentication failed.", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
        // [END create_user_with_email]
    }
    private boolean validateForm() {
        boolean valid = true;
        EditText mEmailField=(EditText)currentActivity.findViewById(R.id.emailField);
        EditText mPasswordField=(EditText)currentActivity.findViewById(R.id.passwordField);
        EditText verifyPasswordField=(EditText)currentActivity.findViewById(R.id.verifyPassword);
        String email = mEmailField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmailField.setError("Required.");
            valid = false;
        } else {
            mEmailField.setError(null);
        }

        String password = mPasswordField.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mPasswordField.setError("Required.");
            valid = false;
        } else {
            if(!verifyPasswordField.getText().toString().equals(password))
            {
                mPasswordField.setError("Input Password Mismatch");
                verifyPasswordField.setError("Input Password Mismatch");
                valid = false;
            }
            else
                mPasswordField.setError(null);
        }
        return valid;
    }
}
