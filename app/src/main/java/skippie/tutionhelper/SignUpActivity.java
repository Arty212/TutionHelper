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

        Log.d(TuitionHelper.TAG_SIGN_UP_ACTIVITY, "Started creating SignUpActivity...");


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        initializeComponents();

        Log.d(TuitionHelper.TAG_SIGN_UP_ACTIVITY, "Completed creating SignUpActivity");
    }


    private void initializeComponents(){
        Log.d(TuitionHelper.TAG_SIGN_UP_ACTIVITY, "Initializing components...");


        MainActivityIntent = new Intent(this, MainActivity.class);
        MainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

        TV_error = findViewById(R.id.SignUpActivity_ErrorTextView);

        ET_name = findViewById(R.id.SignUpActivity_UsernameEditText);
        ET_email = findViewById(R.id.SignUpActivity_EmailEditText);
        ET_password = findViewById(R.id.SignUpActivity_PasswordEditText);

        BT_signUp = findViewById(R.id.SignUpActivity_SignUpButton);

        RB_student = findViewById(R.id.SignUpActivity_StudentRadioButton);
        RB_tutor = findViewById(R.id.SignUpActivity_TutorRadioButton);

        FB_Auth = FirebaseAuth.getInstance();
        FB_User = FB_Auth.getCurrentUser();
        FB_Firestore = FirebaseFirestore.getInstance();
    }


    private void tryCreateUserWithEmailAndPassword(final String email, final String password, final String displayName){
        FB_Auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>(){
            @Override
            public void onComplete(@NonNull Task<AuthResult> task){

                Log.d(TuitionHelper.TAG_SIGN_UP_ACTIVITY, "Trying to create a new Firebase user...");


                if(task.isSuccessful()){

                    FB_User = FirebaseAuth.getInstance().getCurrentUser();

                    FB_User.updateProfile(new UserProfileChangeRequest.Builder().setDisplayName(displayName).build())
                            .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<Void>(){
                                @Override
                                public void onComplete(@NonNull Task<Void> task){

                                    Map<String, Object> user = new HashMap<>();
                                    user.put("name",  displayName);
                                    user.put("email", email);

                                    FB_Firestore.collection("users")
                                            .document(FB_User.getUid())
                                            .set(user);


                                    Log.d(TuitionHelper.TAG_SIGN_UP_ACTIVITY, "New Firebase user successfully created");
                                    Log.i(TuitionHelper.TAG_SIGN_UP_ACTIVITY, String.format(" \nUser\nName:\t\t%s\nEmail:\t\t%s\nUID:\t\t%s\n", FB_User.getDisplayName(), FB_User.getEmail(), FB_User.getUid()));

                                    startActivity(MainActivityIntent);

                                    ET_name.setClickable(true);
                                    ET_email.setClickable(true);
                                    ET_password.setClickable(true);

                                    BT_signUp.setClickable(true);

                                    RB_tutor.setClickable(true);
                                    RB_student.setClickable(true);

                                    finish();
                                }
                            });



                }
                else{

                    Log.e(TuitionHelper.TAG_SIGN_UP_ACTIVITY, "Failed to create new Firebase user");


                    TV_error.setText(R.string.sign_in_error_incorrect_credentials);
                    TV_error.setVisibility(View.VISIBLE);

                    ET_name.setClickable(true);
                    ET_email.setClickable(true);
                    ET_password.setClickable(true);

                    BT_signUp.setClickable(true);

                    RB_tutor.setClickable(true);
                    RB_student.setClickable(true);

                }

            }

        });

    }


    public void SignUpButton_ClickListener(View view){

        Log.d(TuitionHelper.TAG_SIGN_UP_ACTIVITY, "SignUpActivity_SignUpButton button has been clicked");


        TV_error.setVisibility(View.INVISIBLE);

        ET_name.setClickable(true);
        ET_email.setClickable(true);
        ET_password.setClickable(true);

        BT_signUp.setClickable(true);

        RB_tutor.setClickable(true);
        RB_student.setClickable(true);


        String name = ET_name.getText().toString();
        String email = ET_email.getText().toString();
        String password = ET_password.getText().toString();

        if(!name.isEmpty() && !email.isEmpty() && !password.isEmpty() && password.length() >= 6 && password.length() <= 12 && (RB_student.isChecked() || RB_tutor.isChecked())){

            ET_name.setClickable(false);
            ET_email.setClickable(false);
            ET_password.setClickable(false);

            BT_signUp.setClickable(false);

            RB_tutor.setClickable(false);
            RB_student.setClickable(false);


            tryCreateUserWithEmailAndPassword(email, password, name);

        }
        else if(name.isEmpty() || email.isEmpty() || password.isEmpty()){

            Log.e(TuitionHelper.TAG_SIGN_UP_ACTIVITY, "One or more fields are empty, sign up attempt rejected");


            TV_error.setText(R.string.sign_in_error_fields_empty);
            TV_error.setVisibility(View.VISIBLE);

        }//"Empty fields" error handler
        else if(!(RB_student.isChecked() || RB_tutor.isChecked())){

            Log.e(TuitionHelper.TAG_SIGN_UP_ACTIVITY, "User type is not chosen, sign up attempt rejected");

            TV_error.setText(R.string.sign_up_error_type_is_not_chosen);
            TV_error.setVisibility(View.VISIBLE);

        }//"User's type is not chosen" error handler
        else{

            Log.e(TuitionHelper.TAG_SIGN_UP_ACTIVITY, "Illegal password length, sign up attempt rejected");


            TV_error.setText(R.string.sign_up_error_password_illegal_size);
            TV_error.setVisibility(View.VISIBLE);

        }//"Illegal password length" error handler

    }

}