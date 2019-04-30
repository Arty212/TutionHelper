package skippie.tutionhelper;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.androidhuman.rxfirebase2.core.OnCompleteDisposable;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignInActivity extends AppCompatActivity{

    private String TAG;

    private FirebaseAuth FB_Auth;
    private FirebaseUser FB_User;

    private EditText ET_email;
    private EditText ET_password;

    private Button BT_signIn;
    private Button BT_signUp;

    private ImageButton BT_googleSignIn;
    private ImageButton BT_facebookSignIn;
    private ImageButton BT_twitterSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        TAG = getText(R.string.TAG).toString();
        Log.d(TAG, "Creating SignInActivity...");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        Log.d(TAG, "Initializing Buttons and EditTexts...");

        ET_email = findViewById(R.id.SignInActivity_usernameEmailEditText);
        ET_password = findViewById(R.id.SignInActivity_passwordEditText);

        BT_signIn = findViewById(R.id.SignInActivity_signInButton);
        BT_signUp = findViewById(R.id.SignInActivity_signUpButton);

        BT_googleSignIn = findViewById(R.id.SignInActivity_googleSignInButton);
        BT_facebookSignIn = findViewById(R.id.SignInActivity_facebookSignInButton);
        BT_twitterSignIn = findViewById(R.id.SignInActivity_twitterSignInButton);

        Log.d(TAG, "Completed initializing Buttons and EditTexts...\n\nInitializing Firebase and it's plugins");

        FB_Auth = FirebaseAuth.getInstance();

        Log.d(TAG, "Completed initializing Firebase\n\nSetting Buttons on click listeners...");

        setOnListeners();

        Log.d(TAG, "Completed setting Buttons on click listeners");

        Log.d(TAG, "SignInActivity created successfully");
    }

    //TODO: create onClick logic for buttons

    private void setOnListeners(){

        BT_signIn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                String email = ET_email.getText().toString();
                String password = ET_password.getText().toString();

                Log.d(TAG, "Trying to sign in Firebase using email and password...");

                FB_Auth.signInWithEmailAndPassword(email, password)
                       .addOnCompleteListener(SignInActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Log.d(TAG, "Sign in Firebase successfully");
                            FB_User = FB_Auth.getCurrentUser();

                            Log.d(TAG, "User\nEmail:" + FB_User.getEmail());
                        }else{
                            Log.d(TAG, "Sign in failed");
                        }
                    }
                });

            }
        });

        BT_signUp.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

            }
        });

        BT_googleSignIn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

            }
        });

        BT_facebookSignIn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

            }
        });

        BT_twitterSignIn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

            }
        });



    }


}
