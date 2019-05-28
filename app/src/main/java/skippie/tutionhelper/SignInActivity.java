package skippie.tutionhelper;

import android.content.Intent;
import android.content.SharedPreferences;
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

public class SignInActivity extends AppCompatActivity{
    
    //Constants
    private static final String TAG = "TH/:SignInActivity";
    public static final String SHARED_PREFERENCES_NAME = "sPreferences";
    public static final String EMAIL = "TUITION_HELPER_EMAIL";
    public static final String PASSWORD = "TUITION_HELPER_PASSWORD";
    
    
    //Firebase
    private FirebaseAuth FB_Auth;

    
    //Views
    private TextView TV_error;
    
    private EditText ET_email;
    private EditText ET_password;
    
    private Button BT_signIn;
    private Button BT_signUp;
    
    private ProgressBar progressBar;
    
    
    //Variables
    private SharedPreferences sPref;
    
    private Intent SignUpActivityIntent;
    private Intent MainActivityIntent;

    private String email;
    private String password;
    

    @Override
    protected void onCreate(Bundle savedInstanceState){

        Log.d(TAG, "Started creating SignInActivity...");


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        initializeComponents();


        FB_Auth = FirebaseAuth.getInstance();


        Log.d(TAG, "SignInActivity created successfully");

    }

    @Override
    protected void onResume(){
        super.onResume();

        if(!email.isEmpty() && !password.isEmpty()){
    
            ET_email.setText(email);
            ET_password.setText(password);
            
        }

    }

    private void initializeComponents(){

        Log.d(TAG, "Initializing components...");


        sPref = getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE);

        TV_error = findViewById(R.id.SignInActivity_errorTextView);

        ET_email = findViewById(R.id.SignInActivity_UsernameEmailEditText);
        ET_password = findViewById(R.id.SignInActivity_passwordEditText);

        BT_signIn = findViewById(R.id.SignInActivity_signInButton);
        BT_signUp = findViewById(R.id.SignInActivity_signUpButton);

        progressBar = findViewById(R.id.SignInActivity_ProgressBar);

        SignUpActivityIntent = new Intent(this, SignUpActivity.class);

        MainActivityIntent = new Intent(this, MainActivity.class);
        MainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

        email = sPref.getString(EMAIL, "");
        password = sPref.getString(PASSWORD, "");

    }


    public void SignInButton_ClickListener(View view){
        Log.d(TAG, "SignInActivity_SignInButton has been clicked");


        TV_error.setVisibility(View.INVISIBLE);

        email = ET_email.getText().toString();
        password = ET_password.getText().toString();

        if(!email.isEmpty() && !password.isEmpty()){
            Log.d(TAG, "Attempting to sign in Firebase...");


            BT_signIn.setClickable(false);
            BT_signUp.setClickable(false);

            ET_email.setClickable(false);
            ET_password.setClickable(false);

            progressBar.setVisibility(View.VISIBLE);


            FB_Auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(SignInActivity.this, new OnCompleteListener<AuthResult>(){
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task){
                            if(task.isSuccessful()){
                                Log.d(TAG, "Signed in Firebase successfully");


                                sPref.edit()
                                        .putString(EMAIL, email)
                                        .putString(PASSWORD, password)
                                        .commit();

                                startActivity(MainActivityIntent);


                                BT_signIn.setClickable(true);
                                BT_signUp.setClickable(true);

                                ET_email.setClickable(true);
                                ET_password.setClickable(true);

                                progressBar.setVisibility(View.INVISIBLE);


                                finish();
                            }
                            else{
                                Log.e(TAG, "Sign in attempt failed");


                                TV_error.setText(R.string.sign_in_error_incorrect_credentials);
                                TV_error.setVisibility(View.VISIBLE);

                                BT_signIn.setClickable(true);
                                BT_signUp.setClickable(true);

                                ET_email.setClickable(true);
                                ET_password.setClickable(true);

                                progressBar.setVisibility(View.INVISIBLE);

                            }
                        }
                    });
        }else{
            Log.e(TAG, "Field is empty, sign in attempt rejected");


            TV_error.setText(R.string.sign_in_error_fields_empty);
            TV_error.setVisibility(View.VISIBLE);

        }
    }


    public void SignUpButton_ClickListener(View view){
        Log.d(TAG, "Sign up button clicked");


        startActivity(SignUpActivityIntent);

    }

}
