package skippie.tutionhelper;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignInActivity extends AppCompatActivity{

    private FirebaseAuth FB_Auth;
    private FirebaseUser FB_User;

    private TextView TV_error;

    private EditText ET_email;
    private EditText ET_password;

    private Button BT_signIn;
    private Button BT_signUp;

    private ProgressBar progressBar;

    private Intent SignUpActivityIntent;
    private Intent MainActivityIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState){

        Log.d(TuitionHelper.TAG_DEBUG, "Started creating SignInActivity...");


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        initializeComponents();


        Log.d(TuitionHelper.TAG_DEBUG, "Started initializing Firebase and it's components");


        FB_Auth = FirebaseAuth.getInstance();


        Log.d(TuitionHelper.TAG_DEBUG, "Completed initializing Firebase");


        setOnListeners();


        Log.d(TuitionHelper.TAG_DEBUG, "SignInActivity created successfully");

    }

    private void initializeComponents(){

        Log.d(TuitionHelper.TAG_DEBUG, "Started initializing Buttons and EditTexts...");

////////////////////////////////////////////////////////////////////////////////////////////////////

        TV_error = findViewById(R.id.SignInActivity_errorTextView);

        ET_email = findViewById(R.id.SignInActivity_usernameEmailEditText);
        ET_password = findViewById(R.id.SignInActivity_passwordEditText);

        BT_signIn = findViewById(R.id.SignInActivity_signInButton);
        BT_signUp = findViewById(R.id.SignInActivity_signUpButton);

        progressBar = findViewById(R.id.SignInActivity_progressBar);

        SignUpActivityIntent = new Intent(this, SignUpActivity.class);
        MainActivityIntent = new Intent(this, MainActivity.class);

////////////////////////////////////////////////////////////////////////////////////////////////////

        Log.d(TuitionHelper.TAG_DEBUG, "Completed initializing Buttons and EditTexts");

    }

    //TODO: create onClick logic for buttons

    private void setOnListeners(){

        Log.d(TuitionHelper.TAG_DEBUG, "Started setting Buttons on click listeners...");

////////////////////////////////////////////////////////////////////////////////////////////////////

        //Sign In button click listener
        BT_signIn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){

                Log.d(TuitionHelper.TAG_DEBUG, "Sign in button clicked");


                TV_error.setVisibility(View.INVISIBLE); //Resetting visibility of TextView on each click

                String email = ET_email.getText().toString();
                String password = ET_password.getText().toString();

                Log.d(TuitionHelper.TAG_DEBUG, "Checking fields...");

                if(!email.isEmpty() && !password.isEmpty()){

                    Log.d(TuitionHelper.TAG_FIREBASE, "Trying to sign in Firebase using email and password...");

                    BT_signIn.setClickable(false);
                    BT_signUp.setClickable(false);
                    progressBar.setVisibility(View.VISIBLE);

                    FB_Auth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(SignInActivity.this, new OnCompleteListener<AuthResult>(){
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task){

                                    if(task.isSuccessful()){

                                        Log.d(TuitionHelper.TAG_FIREBASE, "Signed in Firebase successfully");


                                        FB_User = FB_Auth.getCurrentUser();
                                        MainActivityIntent.putExtra("Display name", FB_User.getDisplayName());
                                        FirebaseFirestore.getInstance().collection("users").document(FB_User.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>(){
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task){

                                                MainActivityIntent.putExtra("Is teacher", Boolean.parseBoolean(task.getResult().getData().get("isTeacher").toString()));

                                            }
                                        });

                                        startActivity(MainActivityIntent);

                                        BT_signIn.setClickable(true);
                                        BT_signUp.setClickable(true);
                                        progressBar.setVisibility(View.INVISIBLE);


                                        Log.i(TuitionHelper.TAG_FIREBASE, String.format("\nUser\nEmail: %s\nUID: %s", FB_User.getEmail(), FB_User.getUid()));

                                    }else{

                                        TV_error.setText(R.string.sign_in_error_incorrect_credentials);
                                        TV_error.setVisibility(View.VISIBLE);

                                        BT_signIn.setClickable(true);
                                        BT_signUp.setClickable(true);
                                        progressBar.setVisibility(View.INVISIBLE);

                                        Log.e(TuitionHelper.TAG_ERROR, "Sign in attempt failed");

                                    }

                                }

                            });

                }else{

                    TV_error.setText(R.string.sign_in_error_fields_empty);
                    TV_error.setVisibility(View.VISIBLE);


                    Log.e(TuitionHelper.TAG_ERROR, "Field is empty, sign in attempt rejected");

                }

            }

        });

        //Sign up button click listener
        BT_signUp.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){

                Log.d(TuitionHelper.TAG_DEBUG, "Sign up button clicked");

                startActivity(SignUpActivityIntent);

            }

        });

////////////////////////////////////////////////////////////////////////////////////////////////////

        Log.d(TuitionHelper.TAG_DEBUG, "Completed setting Buttons on click listeners");

    }

}
