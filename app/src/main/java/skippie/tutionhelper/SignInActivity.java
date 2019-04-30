package skippie.tutionhelper;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignInActivity extends AppCompatActivity{

    private String TAG_SYSTEM_DEBUG;
    private String TAG_FIREBASE_DEBUG;

    private FirebaseAuth FB_Auth;
    private FirebaseUser FB_User;

    private TextView TV_error;

    private EditText ET_email;
    private EditText ET_password;

    private Button BT_signIn;
    private Button BT_signUp;

    private ImageButton BT_googleSignIn;
    private ImageButton BT_facebookSignIn;
    private ImageButton BT_twitterSignIn;

    private Intent SignUpActivityIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState){

        TAG_SYSTEM_DEBUG = getText(R.string.TAG_SYSTEM_DEBUG).toString();
        TAG_FIREBASE_DEBUG = getText(R.string.TAG_FIREBASE_DEBUG).toString();

        Log.d(TAG_SYSTEM_DEBUG, "Creating SignInActivity...");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        Log.d(TAG_SYSTEM_DEBUG, "Initializing Buttons and EditTexts...");

        TV_error = findViewById(R.id.SignInActivity_errorTextView);

        ET_email = findViewById(R.id.SignInActivity_usernameEmailEditText);
        ET_password = findViewById(R.id.SignInActivity_passwordEditText);

        BT_signIn = findViewById(R.id.SignInActivity_signInButton);
        BT_signUp = findViewById(R.id.SignInActivity_signUpButton);

        BT_googleSignIn = findViewById(R.id.SignInActivity_googleSignInButton);
        BT_facebookSignIn = findViewById(R.id.SignInActivity_facebookSignInButton);
        BT_twitterSignIn = findViewById(R.id.SignInActivity_twitterSignInButton);

        SignUpActivityIntent = new Intent(this, SignUpActivity.class);

        Log.d(TAG_SYSTEM_DEBUG, "Completed initializing Buttons and EditTexts...");

        Log.d(TAG_SYSTEM_DEBUG, "Initializing Firebase and it's plugins");

        FB_Auth = FirebaseAuth.getInstance();

        Log.d(TAG_SYSTEM_DEBUG, "Completed initializing Firebase");

        Log.d(TAG_SYSTEM_DEBUG, "Setting Buttons on click listeners...");

        setOnListeners();

        Log.d(TAG_SYSTEM_DEBUG, "Completed setting Buttons on click listeners");

        Log.d(TAG_SYSTEM_DEBUG, "SignInActivity created successfully");
    }

    //TODO: create onClick logic for buttons

    private void setOnListeners(){

        //Sign In button click listener
        BT_signIn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){

                TV_error.setVisibility(View.INVISIBLE); //Resetting visibility of TextView on each click

                String email = ET_email.getText().toString();
                String password = ET_password.getText().toString();

                Log.d(TAG_SYSTEM_DEBUG, "Checking fields");

                if(!email.isEmpty() && !password.isEmpty()){ //Checking if both of fields aren't empty

                    Log.d(TAG_FIREBASE_DEBUG, "Trying to sign in Firebase using email and password...");

                    FB_Auth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(SignInActivity.this, new OnCompleteListener<AuthResult>(){
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task){

                                    if(task.isSuccessful()){

                                        Log.d(TAG_FIREBASE_DEBUG, "Sign in Firebase successfully");

                                        FB_User = FB_Auth.getCurrentUser();

                                        Log.d(TAG_FIREBASE_DEBUG, String.format("\nUser\nEmail: %s\nUID: %s", FB_User.getEmail(), FB_User.getUid()));

                                    }else{

                                        TV_error.setText(R.string.sign_in_error_incorrect_credentials);
                                        TV_error.setVisibility(View.VISIBLE);

                                        Log.d(TAG_FIREBASE_DEBUG, "Sign in failed");

                                    }

                                }
                            });

                }
                else {

                    TV_error.setText(R.string.sign_in_error_fields_empty);
                    TV_error.setVisibility(View.VISIBLE);

                    Log.d(TAG_SYSTEM_DEBUG, "Field is empty, sign in attempt rejected");

                }

            }

        });

        //Sign up button click listener
        BT_signUp.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){

                startActivity(SignUpActivityIntent);

            }

        });

        //Google sign in button listener
        BT_googleSignIn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){

            }

        });

        //Facebook sign in button listener
        BT_facebookSignIn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){

            }

        });

        //Twitter sign in button listener
        BT_twitterSignIn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){

            }

        });

    }

}
