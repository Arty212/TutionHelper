package skippie.tutionhelper;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

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

    private Intent MainActivityIntent;

    private boolean isTeacher;

    private FirebaseAuth FB_Auth;
    private FirebaseUser FB_User;
    private FirebaseFirestore FB_Firestore;

    private TextView TV_error;

    private EditText ET_name;
    private EditText ET_email;
    private EditText ET_password;

    private Button BT_signUp;

    private RadioButton RB_student;
    private RadioButton RB_tutor;

    @Override
    protected void onCreate(Bundle savedInstanceState){

        Log.d(TuitionHelper.TAG_DEBUG, "Started creating SignUpActivity...");


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        initializeFirebase();

        initializeComponents();

        setOnListeners();

        Log.d(TuitionHelper.TAG_DEBUG, "Completed creating SignUpActivity");
    }

    private void initializeFirebase(){

        Log.d(TuitionHelper.TAG_FIREBASE, "Started initializing Firebase and it's components");


        FB_Auth = FirebaseAuth.getInstance();
        FB_User = FB_Auth.getCurrentUser();
        FB_Firestore = FirebaseFirestore.getInstance();


        Log.d(TuitionHelper.TAG_FIREBASE, "Completed initializing Firebase and it's components");
    }

    private void initializeComponents(){

        Log.d(TuitionHelper.TAG_DEBUG, "Started initializing components..");

        MainActivityIntent = new Intent(this, MainActivity.class);

        TV_error = findViewById(R.id.SignUpActivity_errorTextView);

        ET_name = findViewById(R.id.SignUpActivity_usernameEditText);
        ET_email = findViewById(R.id.SignUpActivity_emailEditText);
        ET_password = findViewById(R.id.SignUpActivity_passwordEditText);

        BT_signUp = findViewById(R.id.SignUpActivity_signUpButton);

        RB_student = findViewById(R.id.SignUpActivity_studentRadioButton);
        RB_tutor = findViewById(R.id.SignUpActivity_tutorRadioButton);


        Log.d(TuitionHelper.TAG_DEBUG, "Completed initializing components");
    }

    private void setOnListeners(){

        Log.d(TuitionHelper.TAG_DEBUG, "Started setting buttons on listeners...");


        BT_signUp.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){

                Log.d(TuitionHelper.TAG_DEBUG, "Sign up button clicked");


                if(RB_student.isChecked())
                    isTeacher = false;
                if(RB_tutor.isChecked())
                    isTeacher = true;

                TV_error.setVisibility(View.INVISIBLE);

                String name = ET_name.getText().toString();
                String email = ET_email.getText().toString();
                String password = ET_password.getText().toString();

                if(!name.isEmpty() && !email.isEmpty() && !password.isEmpty() && password.length() >= 6 && password.length() <= 12 && (RB_student.isChecked() || RB_tutor.isChecked())){

                    BT_signUp.setClickable(false);

                    tryCreateUserWithEmailAndPassword(email, password, name);

                }
                else if(name.isEmpty() || email.isEmpty() || password.isEmpty()){

                    TV_error.setText(R.string.sign_in_error_fields_empty);
                    TV_error.setVisibility(View.VISIBLE);


                    Log.e(TuitionHelper.TAG_ERROR, "One or more fields are empty, sign up attempt rejected");

                }
                else if(!(RB_student.isChecked() || RB_tutor.isChecked())){

                    TV_error.setText(R.string.sign_up_error_type_is_not_chosen);
                    TV_error.setVisibility(View.VISIBLE);


                    Log.e(TuitionHelper.TAG_ERROR, "User type is not chosen, sign up attempt rejected");
                }
                else{

                    TV_error.setText(R.string.sign_up_error_password_illegal_size);
                    TV_error.setVisibility(View.VISIBLE);


                    Log.e(TuitionHelper.TAG_ERROR, "Illegal password length, sign up attempt rejected");

                }

            }

        });

        RB_tutor.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                isTeacher = true;
            }
        });

        RB_student.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                isTeacher = false;
            }
        });


    }

    private void tryCreateUserWithEmailAndPassword(final String email, final String password, final String displayName){
        FB_Auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>(){
            @Override
            public void onComplete(@NonNull Task<AuthResult> task){

                Log.d(TuitionHelper.TAG_DEBUG, "Trying to create a new Firebase user...");


                if(task.isSuccessful()){

                    FB_User = FirebaseAuth.getInstance().getCurrentUser();

                    FB_User.updateProfile(new UserProfileChangeRequest.Builder().setDisplayName(displayName).build())
                            .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<Void>(){
                                @Override
                                public void onComplete(@NonNull Task<Void> task){

                                    Map<String, Object> user = new HashMap<>();
                                    user.put("name",  FB_User.getDisplayName());
                                    user.put("email", FB_User.getEmail());
                                    user.put("isTeacher", false);

                                    FB_Firestore.collection("users").document(FB_User.getUid())
                                            .set(user);


                                    Log.d(TuitionHelper.TAG_DEBUG, "New Firebase user successfully created");
                                    Log.i(TuitionHelper.TAG_INFO, String.format(" \nUser\nName:\t%s\nEmail:\t%s\nUID:\t%s", FB_User.getDisplayName(), FB_User.getEmail(), FB_User.getUid()));


                                    BT_signUp.setClickable(true);

                                    MainActivityIntent.putExtra("Email", email);
                                    MainActivityIntent.putExtra("Password", password);

                                    startActivity(MainActivityIntent);
                                }
                            });



                }
                else{

                    TV_error.setText(R.string.sign_up_error_unable_to_complete);
                    TV_error.setVisibility(View.VISIBLE);

                    BT_signUp.setClickable(true);


                    Log.e(TuitionHelper.TAG_FIREBASE, "Failed to create new Firebase user");

                }

            }

        });

    }

}