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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.security.spec.ECField;
import java.util.ArrayList;
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

        userList = new ArrayList<>();
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

        initialize();
        
        RV_searchResults = view.findViewById(R.id.SearchFragment_recyclerView);
        RV_searchResults.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayout.VERTICAL, false));
        
        ET_searchField = view.findViewById(R.id.SearchFragment_nameEditText);


        //Text changed listener
        ET_searchField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        
            }
    
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!ET_searchField.getText().toString().isEmpty()) {
                    try {
                        searchQuery = FB_UsersRef
                                .whereGreaterThanOrEqualTo("Name", ET_searchField.getText().toString())
                                .whereLessThanOrEqualTo("Name", ET_searchField.getText().toString().concat("zzzzzzzzzzzz"));
                    } catch (Exception ignored) {

                    }
                }
            }
    
            @Override
            public void afterTextChanged(Editable s) {
                if(!ET_searchField.getText().toString().isEmpty()) {
                    try {
                        searchQuery
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        userList.clear();
                                        for (DocumentSnapshot doc : task.getResult().getDocuments()) {
                                            String displayName = doc.get("Name").toString();
                                            String uid = doc.get("UID").toString();
                                            Bitmap avatar = TuitionHelper.decodeStringToBitmap(doc.get("Avatar").toString());

                                            userList.add(new User(displayName, uid, avatar));

                                            mAdapter = new RecyclerViewAdapter(v.getContext(), userList, listener);
                                            RV_searchResults.setAdapter(mAdapter);

                                        }
                                    }
                                });

                    } catch (Exception ignored) { }
                }else {
                    userList.clear();

                    mAdapter = new RecyclerViewAdapter(v.getContext(), userList, listener);
                    RV_searchResults.setAdapter(mAdapter);
                }
            }
        });
        //Text changed listener


    }

    RecyclerViewAdapter.OnItemClickListener listener = new RecyclerViewAdapter.OnItemClickListener() {
        @Override
        public void OnItemClick(User item) {

        }
    };
}
