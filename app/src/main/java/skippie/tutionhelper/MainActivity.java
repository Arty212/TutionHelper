package skippie.tutionhelper;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends FragmentActivity{

    private LinearLayout layout;

    private TextView usernameTV;
    private TextView userTypeTV;
    private TextView additionalInfoTV;

    private FirebaseAuth FB_Auth;
    private FirebaseUser FB_User;
    private FirebaseFirestore FB_Firestore;

    private Intent intent;

    private String username;
    private String additionalInfo;
    private String userType;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FB_Auth = FirebaseAuth.getInstance();
        FB_User = FB_Auth.getCurrentUser();
        FB_Firestore = FirebaseFirestore.getInstance();

        usernameTV = findViewById(R.id.MainActivity_usernameTV);
        userTypeTV = findViewById(R.id.MainActivity_userTypeTV);

        layout = findViewById(R.id.MainActivity_userInfoContainer);


        username = FB_User.getDisplayName();
        FB_Firestore.collection("users")
                .document(FB_User.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>(){
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task){
                        try{
                            if(task.getResult().getData().get("isTeacher").toString().equals("true"))
                                userType = "Tutor";
                            else
                                userType = "Student";
                        }catch(Exception e){}
                    }
                });

        usernameTV.setText(username);
        userTypeTV.setText(userType);

        layout.setVisibility(View.VISIBLE);
    }


}
