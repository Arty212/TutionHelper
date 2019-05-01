package skippie.tutionhelper;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity{

    private String TAG_DEBUG;
    private String TAG_FIREBASE;
    private String TAG_ERROR;
    private String TAG_INFO;

    private FirebaseAuth FB_Auth;
    private FirebaseUser FB_User;
    private FirebaseFirestore FB_Firestore;

    private TextView TV_error;

    private EditText ET_name;
    private EditText ET_email;
    private EditText ET_password;

    private Button BT_signUp;

    private ImageButton BT_googleSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState){

        TAG_DEBUG = getText(R.string.TAG_DEBUG).toString();
        TAG_FIREBASE = getText(R.string.TAG_FIREBASE).toString();
        TAG_ERROR = getText(R.string.TAG_ERROR).toString();
        TAG_INFO = getText(R.string.TAG_INFO).toString();

        Log.d(TAG_DEBUG, "Started creating SignUpActivity...");


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        initializeFirebase();

        initializeComponents();

        setOnListeners();

        Log.d(TAG_DEBUG, "Completed creating SignUpActivity");
    }

    private void initializeFirebase(){

        Log.d(TAG_FIREBASE, "Started initializing Firebase and it's components");


        FB_Auth = FirebaseAuth.getInstance();
        FB_User = FB_Auth.getCurrentUser();
        FB_Firestore = FirebaseFirestore.getInstance();


        Log.d(TAG_FIREBASE, "Completed initializing Firebase and it's components");
    }

    private void initializeComponents(){

        Log.d(TAG_DEBUG, "Started initializing components..");


        TV_error = findViewById(R.id.SignUp_errorTextView);

        ET_name = findViewById(R.id.SignUp_usernameEditText);
        ET_email = findViewById(R.id.SignUp_emailEditText);
        ET_password = findViewById(R.id.SignUp_passwordEditText);

        BT_signUp = findViewById(R.id.SignUp_signUpButton);


        Log.d(TAG_DEBUG, "Completed initializing components");
    }

    private void setOnListeners(){


        Log.d(TAG_DEBUG, "Started setting buttons on listeners...");

        BT_signUp.setOnClickListener(new View.OnClickListener(){


            @Override
            public void onClick(View v){

                Log.d(TAG_DEBUG, "Sign up button clicked");


                TV_error.setVisibility(View.INVISIBLE);

                String name = ET_name.getText().toString();
                String email = ET_email.getText().toString();
                String password = ET_password.getText().toString();

                if(!name.isEmpty() && !email.isEmpty() && !password.isEmpty() && password.length() >= 6 && password.length() <= 12){

                    tryCreateUserWithEmailAndPassword(email, password, name);

                }
                else if(name.isEmpty() || email.isEmpty() || password.isEmpty()){

                    TV_error.setText(R.string.sign_in_error_fields_empty);
                    TV_error.setVisibility(View.VISIBLE);


                    Log.e(TAG_ERROR, "One or more fields are empty, sign up attempt rejected");

                }
                else{

                    TV_error.setText(R.string.sign_up_error_password_illegal_size);
                    TV_error.setVisibility(View.VISIBLE);


                    Log.e(TAG_ERROR, "Illegal password length, sign up attempt rejected");

                }

            }

        });

    }


    private void tryCreateUserWithEmailAndPassword(final String email, String password, final String displayName){
        FB_Auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>(){
            @Override
            public void onComplete(@NonNull Task<AuthResult> task){

                Log.d(TAG_DEBUG, "Trying to create a new Firebase user...");


                if(task.isSuccessful()){

                    FB_User = FirebaseAuth.getInstance().getCurrentUser();

                    FB_User.updateProfile(new UserProfileChangeRequest.Builder().setDisplayName(ET_name.getText().toString()).build())
                            .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<Void>(){
                                @Override
                                public void onComplete(@NonNull Task<Void> task){

                                    Map<String, Object> user = new HashMap<>();
                                    user.put("name",  FB_User.getDisplayName());
                                    user.put("email", FB_User.getEmail());
                                    user.put("isTeacher", false);

                                    FB_Firestore.collection("users").document(FB_User.getUid())
                                            .set(user);


                                    Log.d(TAG_DEBUG, "New Firebase user successfully created");
                                    Log.i(TAG_INFO, String.format("\nUser\nName:\t%s\nEmail:\t%s\nUID:\t%s", FB_User.getDisplayName(), FB_User.getEmail(), FB_User.getUid()));
                                }
                            });



                }
                else{

                    TV_error.setText(R.string.sign_up_error_unable_to_complete);
                    TV_error.setVisibility(View.VISIBLE);


                    Log.e(TAG_ERROR, "Failed to create new Firebase user");

                }

            }

        });

    }

}