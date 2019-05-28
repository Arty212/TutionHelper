package skippie.tutionhelper;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.security.spec.ECField;
import java.util.List;

public class SearchFragment extends Fragment {
    
    //Firebase
    FirebaseAuth FB_Auth;
    FirebaseFirestore FB_DB;
    CollectionReference FB_UsersRef;
    Query searchQuery;
    
    
    //Views
    EditText ET_searchField;
    RecyclerView RV_searchResults;
    
    
    //Variables
    RecyclerViewAdapter mAdapter;
    List<User> userList;
    
    void initialize() {
        FB_Auth = FirebaseAuth.getInstance();
        FB_DB = FirebaseFirestore.getInstance();
        FB_UsersRef = FB_DB.collection("users");
    }
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    
        final View v = view;
        
        RV_searchResults = view.findViewById(R.id.SearchFragment_recyclerView);
        RV_searchResults.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayout.VERTICAL, false));
        
        ET_searchField = view.findViewById(R.id.SearchFragment_nameEditText);
        ET_searchField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        
            }
    
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    searchQuery = FB_UsersRef.whereEqualTo("Name", s.toString());
                }catch(Exception ignored) {
                
                }
            }
    
            @Override
            public void afterTextChanged(Editable s) {
                searchQuery
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                try {
                                    for(DocumentSnapshot doc : task.getResult().getDocuments()) {
                                        String displayName = doc.get("Name").toString();
                                        Bitmap avatar = TuitionHelper.decodeStringToBitmap(doc.get("Avatar").toString());
            
                                        userList.add(new User(displayName, avatar));
                                    }
                                }catch(Exception ignored) {
                                
                                }
                                
                            }
                        });
    
    
                mAdapter = new RecyclerViewAdapter(v.getContext(), userList);
            }
        });
    }
}
