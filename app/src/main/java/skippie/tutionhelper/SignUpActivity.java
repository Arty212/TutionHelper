package skippie.tutionhelper;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpActivity extends AppCompatActivity{

    private String TAG_SYSTEM_DEBUG;
    private String TAG_FIREBASE_DEBUG;

    private FirebaseAuth FB_Auth;
    private FirebaseUser FB_User;

    private TextView TV_error;

    private EditText ET_name;
    private EditText ET_email;
    private EditText ET_password;

    private Button BT_signUp;

    @Override
    protected void onCreate(Bundle savedInstanceState){

        TAG_SYSTEM_DEBUG = getText(R.string.TAG_SYSTEM_DEBUG).toString();
        TAG_FIREBASE_DEBUG = getText(R.string.TAG_FIREBASE_DEBUG).toString();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        FB_Auth = FirebaseAuth.getInstance();
        FB_User = FB_Auth.getCurrentUser();

        TV_error = findViewById(R.id.SignUp_errorTextView);

        ET_name = findViewById(R.id.SignUp_usernameEditText);
        ET_email = findViewById(R.id.SignUp_emailEditText);
        ET_password = findViewById(R.id.SignUp_passwordEditText);

        BT_signUp = findViewById(R.id.SignUp_signUpButton);

        BT_signUp.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){

                TV_error.setVisibility(View.INVISIBLE);

                String name = ET_name.getText().toString();
                String email = ET_email.getText().toString();
                String password = ET_password.getText().toString();

                if(!name.isEmpty() && !email.isEmpty() && !password.isEmpty()){
                    if(password.length() >= 6 && password.length() <= 12){

                        FB_Auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>(){
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task){
                                if(task.isSuccessful()){

                                    FB_User = FB_Auth.getCurrentUser();
                                    FirebaseAuth.getInstance().signOut();


                                }else{

                                    TV_error.setText(R.string.sign_up_error_unable_to_complete);
                                    TV_error.setVisibility(View.VISIBLE);

                                }
                            }
                        });

                    }else{

                        TV_error.setText(R.string.sign_up_error_password_illegal_size);
                        TV_error.setVisibility(View.VISIBLE);

                        Log.d(TAG_SYSTEM_DEBUG, "Illegal password length, sign up attempt rejected");

                    }
                }else{

                    TV_error.setText(R.string.sign_in_error_fields_empty);
                    TV_error.setVisibility(View.VISIBLE);

                    Log.d(TAG_SYSTEM_DEBUG, "Field is empty, sign up attempt rejected");

                }

            }

        });

    }

}
