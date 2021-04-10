package com.example.queu_e;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.queu_e.utility.LoadingDialog;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 1;
    private Button googleSignIn, phoneSignIn;
    private EditText editText, otpEdit, nameEdit;
    private Context context;
    private GoogleSignInClient mGoogleSignInClient;
    private LoadingDialog dialog;
    private String verID;
    private String name = "";

    PhoneAuthProvider.OnVerificationStateChangedCallbacks phoneAuthCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            dialog.stopDialog();
            FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential).addOnCompleteListener(authResultOnCompleteListener);
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            dialog.stopDialog();
            Toast.makeText(context, "Something went wrong!", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            dialog.stopDialog();
            editText.setVisibility(View.GONE);
            nameEdit.setVisibility(View.GONE);
            phoneSignIn.setEnabled(false);
            otpEdit.setVisibility(View.VISIBLE);
            otpEdit.requestFocus();
            verID = s;
        }
    };

    OnCompleteListener<AuthResult> authResultOnCompleteListener = new OnCompleteListener<AuthResult>() {
        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
            dialog.stopDialog();
            if (task.isSuccessful()) {
                addUserToDb();
            } else {
                Toast.makeText(context, "Something went wrong!", Toast.LENGTH_LONG).show();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        createRequest();
        setReferences();
        setListeners();
    }

    private void setReferences() {
        context = this;
        editText = findViewById(R.id.editText);
        otpEdit = findViewById(R.id.otpEdit);
        nameEdit = findViewById(R.id.name);
        googleSignIn = findViewById(R.id.buttonGoogle);
        phoneSignIn = findViewById(R.id.buttonPhone);
        dialog = new LoadingDialog(context);
    }

    private void setListeners() {
        googleSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
        phoneSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInPhone();
            }
        });
        otpEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (otpEdit.getText().toString().length() == 6) {
                    hideKeyboard();
                    dialog.showDialog();
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verID, otpEdit.getText().toString().trim());
                    FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener(authResultOnCompleteListener);
                }
            }
        });
    }

    //PHONE SIGN IN
    private void signInPhone() {
        if (nameEdit.getText() == null) {
            Toast.makeText(context, "Enter name!", Toast.LENGTH_LONG).show();
            return;
        } else {
            if (nameEdit.getText().toString().trim().equals("")) {
                Toast.makeText(context, "Enter name!", Toast.LENGTH_LONG).show();
                return;
            } else {
                name = nameEdit.getText().toString().trim();
            }
        }
        if (editText.getText() == null) {
            Toast.makeText(context, "Enter number!", Toast.LENGTH_LONG).show();
        } else {
            if (editText.getText().toString().trim().equals("")) {
                Toast.makeText(context, "Enter number!", Toast.LENGTH_LONG).show();
            } else {
                String phone = editText.getText().toString().trim();
                dialog.showDialog();
                PhoneAuthOptions options = PhoneAuthOptions.newBuilder()
                        .setPhoneNumber(phone)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(phoneAuthCallback)
                        .build();
                PhoneAuthProvider.verifyPhoneNumber(options);
                hideKeyboard();
            }
        }
    }

    private void hideKeyboard() {
        InputMethodManager manager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(editText.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }


    //GOOGLE SIGN IN
    private void createRequest() {
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            dialog.showDialog();
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                name = account.getDisplayName();
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                dialog.stopDialog();
                Toast.makeText(context, "Something went Wrong!", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        dialog.stopDialog();
                        if (task.isSuccessful()) {
                            addUserToDb();
                        } else {
                            Toast.makeText(context, "Something went Wrong!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void addUserToDb() {
        FirebaseDatabase.getInstance().getReference()
                .child("USERS")
                .child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()))
                .child("Name")
                .setValue(name)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(context, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(context, "Something went Wrong!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}