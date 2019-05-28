package skippie.tutionhelper;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
    
    //Constants
    private static final String TAG = "TH/:SignUpActivity";
    
    
    //Firebase
    private FirebaseAuth FB_Auth;
    private FirebaseUser FB_User;
    private FirebaseFirestore FB_Firestore;
    
    
    //Views
    private TextView TV_error;
    
    private EditText ET_name;
    private EditText ET_email;
    private EditText ET_password;
    
    private Button BT_signUp;
    
    
    //Variables
    private Intent MainActivityIntent;
    private String encodedAvatar;


    @Override
    protected void onCreate(Bundle savedInstanceState){

        Log.d(TAG, "Started creating SignUpActivity...");


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        initializeComponents();

        Log.d(TAG, "Completed creating SignUpActivity");
    }


    private void initializeComponents(){
        Log.d(TAG, "Initializing components...");


        MainActivityIntent = new Intent(this, MainActivity.class);
        MainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

        TV_error = findViewById(R.id.SignUpActivity_ErrorTextView);

        ET_name = findViewById(R.id.SignUpActivity_UsernameEditText);
        ET_email = findViewById(R.id.SignUpActivity_EmailEditText);
        ET_password = findViewById(R.id.SignUpActivity_PasswordEditText);

        BT_signUp = findViewById(R.id.SignUpActivity_SignUpButton);

        FB_Auth = FirebaseAuth.getInstance();
        FB_User = FB_Auth.getCurrentUser();
        FB_Firestore = FirebaseFirestore.getInstance();
    
        encodedAvatar = TuitionHelper.BitMapToString(BitmapFactory.decodeResource(this.getResources(), R.drawable.avatar_drawable));
    }


    private void tryCreateUserWithEmailAndPassword(final String email, final String password, final String displayName){
        FB_Auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>(){
            @Override
            public void onComplete(@NonNull Task<AuthResult> task){

                Log.d(TAG, "Trying to create a new Firebase user...");


                if(task.isSuccessful()){

                    FB_User = FirebaseAuth.getInstance().getCurrentUser();
                    
                    FB_User.updateProfile(new UserProfileChangeRequest.Builder().setDisplayName(displayName).build())
                            .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<Void>(){
                                @Override
                                public void onComplete(@NonNull Task<Void> task){

                                    Map<String, Object> user = new HashMap<>();
                                    user.put("Name",  displayName);
                                    user.put("Email", email);
                                    user.put("Avatar", encodedAvatar);

                                    FB_Firestore.collection("users")
                                            .document(FB_User.getUid())
                                            .set(user);


                                    Log.d(TAG, "New Firebase user successfully created");
                                    Log.i(TAG, String.format(" \nUser\nName:\t\t%s\nEmail:\t\t%s\nUID:\t\t%s\n", FB_User.getDisplayName(), FB_User.getEmail(), FB_User.getUid()));

                                    startActivity(MainActivityIntent);

                                    ET_name.setClickable(true);
                                    ET_email.setClickable(true);
                                    ET_password.setClickable(true);

                                    BT_signUp.setClickable(true);
                                    finish();
                                }
                            });



                }
                else{

                    Log.e(TAG, "Failed to create new Firebase user");


                    TV_error.setText("User with this email already exists");
                    TV_error.setVisibility(View.VISIBLE);

                    ET_name.setClickable(true);
                    ET_email.setClickable(true);
                    ET_password.setClickable(true);

                    BT_signUp.setClickable(true);

                }

            }

        });

    }


    public void SignUpButton_ClickListener(View view){

        Log.d(TAG, "SignUpActivity_SignUpButton button has been clicked");

        TV_error.setVisibility(View.INVISIBLE);

        ET_name.setClickable(true);
        ET_email.setClickable(true);
        ET_password.setClickable(true);

        BT_signUp.setClickable(true);



        String name = ET_name.getText().toString();
        String email = ET_email.getText().toString();
        String password = ET_password.getText().toString();

        if(!name.isEmpty() && !email.isEmpty() && !password.isEmpty() && password.length() >= 6 && password.length() <= 12){

            ET_name.setClickable(false);
            ET_email.setClickable(false);
            ET_password.setClickable(false);

            BT_signUp.setClickable(false);



            tryCreateUserWithEmailAndPassword(email, password, name);

        }
        else if(name.isEmpty() || email.isEmpty() || password.isEmpty()){

            Log.e(TAG, "One or more fields are empty, sign up attempt rejected");


            TV_error.setText(R.string.sign_in_error_fields_empty);
            TV_error.setVisibility(View.VISIBLE);

        }//"Empty fields" error handler
        else{

            Log.e(TAG, "Illegal password length, sign up attempt rejected");


            TV_error.setText(R.string.sign_up_error_password_illegal_size);
            TV_error.setVisibility(View.VISIBLE);

        }//"Illegal password length" error handler

    }

}