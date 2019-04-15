package skippie.tutionhelper;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class SignInActivity extends AppCompatActivity{

    EditText usernameEmailET;
    EditText passwordET;

    Button signInBT;
    Button signUpBT;

    ImageButton googleSignInBT;
    ImageButton facebookSignInBT;
    ImageButton twitterSignInBT;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);


        usernameEmailET = findViewById(R.id.SignInActivity_usernameEmailEditText);
        passwordET = findViewById(R.id.SignInActivity_passwordEditText);

        signInBT = findViewById(R.id.SignInActivity_signInButton);
        signUpBT = findViewById(R.id.SignInActivity_signUpButton);

        googleSignInBT = findViewById(R.id.SignInActivity_googleSignInButton);
        facebookSignInBT = findViewById(R.id.SignInActivity_facebookSignInButton);
        twitterSignInBT = findViewById(R.id.SignInActivity_twitterSignInButton);


    }

    //TODO: create onClick logic for buttons

    private void setOnListeners(){

        signInBT.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

            }
        });

        signUpBT.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

            }
        });

        googleSignInBT.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

            }
        });

        facebookSignInBT.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

            }
        });

        twitterSignInBT.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

            }
        });
    }


}
