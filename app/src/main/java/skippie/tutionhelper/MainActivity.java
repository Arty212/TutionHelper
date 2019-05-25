package skippie.tutionhelper;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class MainActivity extends FragmentActivity{

    //Firebase
    private FirebaseAuth FB_Auth;
    private FirebaseUser FB_User;
    private FirebaseFirestore FB_DB;

    //Views
    private RecyclerView RV_SearchList;
    private RecyclerView.Adapter RV_Adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.MainAcitivity_fragmentContainer, new MainMenuFragment())
                .commit();
        
    }

    private void initializeComponents(){

        //Firebase
        FB_DB = FirebaseFirestore.getInstance();
        FB_Auth = FirebaseAuth.getInstance();
        FB_User = FB_Auth.getCurrentUser();

        //Views
        RV_SearchList = findViewById(R.id.SearchFragment_UsersList);
        RV_SearchList.setHasFixedSize(true);
        RV_SearchList.setLayoutManager(new LinearLayoutManager(this));
    }

    public void FriendsListButton_ClickListener(View view){

    }

    public void SearchButton_ClickListener(View view){
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.MainAcitivity_fragmentContainer, new SearchFragment())
                .commit();
        
    }

    public void TasksButton_ClickListeners(View view){
        
    }

}
