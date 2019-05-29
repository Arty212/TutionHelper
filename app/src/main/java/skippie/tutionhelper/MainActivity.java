package skippie.tutionhelper;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends FragmentActivity {
    
    //Constants
    private String TAG = "TH/:MainActivity";
    
    //Firebase
    private FirebaseAuth FB_Auth;
    private FirebaseUser FB_User;
    private FirebaseFirestore FB_DB;
    private CollectionReference FB_Users;
    private DocumentReference FB_CurrUser;
    
    //Views
    private TextView TV_username;
    private TextView TV_additionalInfo;
    private ImageView IV_avatar;
    private BottomNavigationView bottomNavigationView;
    
    //Variables
    private String uid;
    
    private void initializeComponents() {
        
        //Firebase
        FB_DB = FirebaseFirestore.getInstance();
        FB_Auth = FirebaseAuth.getInstance();
        FB_User = FB_Auth.getCurrentUser();
        FB_Users = FB_DB.collection("users");
        FB_CurrUser = FB_Users.document(FB_User.getUid());
        
        //Views
        TV_username = findViewById(R.id.MainActivity_usernameTV);
        TV_additionalInfo = findViewById(R.id.MainActivity_userAdditionalInfoTV);
        IV_avatar = findViewById(R.id.MainActivity_userAvatarIV);
        bottomNavigationView = findViewById(R.id.MainActivity_BottomNavigationView);
        
        //Variables
        uid = FB_User.getUid();
        
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    
        initializeComponents();
        
        TV_username.setText(FB_User.getDisplayName());
        FB_CurrUser.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                try {
                    String encodedString = task.getResult().get("Avatar").toString();
                    IV_avatar.setImageBitmap(TuitionHelper.decodeStringToBitmap(encodedString));
                }catch(Exception ignored){
                
                }
                
            }
        });
        
        bottomNavigationView.setSelectedItemId(R.id.action__tasks);
        
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment selectedFragment = null;
    
                switch(menuItem.getItemId()) {
                    case R.id.action__contacts:
                        selectedFragment = new ContactsFragment();
                        break;
                    case R.id.action__search:
                        selectedFragment = new SearchFragment();
                        break;
                    case R.id.action__tasks:
                        selectedFragment = new TasksFragment();
                        break;
                }
    
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.MainActivity_FragmentContainer, selectedFragment)
                        .commit();
                
                return true;
            }
        });
        
    }
    
    @Override
    public void onBackPressed() {
        
        int count = getSupportFragmentManager().getBackStackEntryCount();
        
        if (count == 0) {
            super.onBackPressed();
        } else {
            getSupportFragmentManager().popBackStack();
        }
        
    }
    
    public void ContactsButton_ClickListener(View view) {
    
    }
    
    public void SearchButton_ClickListener(View view) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.MainActivity_FragmentContainer, new SearchFragment())
                .commit();
    }
    
    public void TasksButton_ClickListeners(View view) {
        
    }
    
    public void EditAccount_ClickListener(View view) {
        Intent EditAccountIntent = new Intent(this, EditAccountActivity.class);
        
        startActivity(EditAccountIntent);
    }
    
}
