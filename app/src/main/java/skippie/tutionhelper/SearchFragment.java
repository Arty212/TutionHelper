package skippie.tutionhelper;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class SearchFragment extends Fragment {
    
    //Firebase
    FirebaseFirestore FB_DB;
    Query query;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
    
        FB_DB = FirebaseFirestore.getInstance();
        
    
        final EditText ET_SearchField = view.findViewById(R.id.SearchFragment_nameEditText);
        ET_SearchField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        
            }
    
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                query = FB_DB.collection("users").whereEqualTo("name", ET_SearchField.getText().toString());
            }
    
            @Override
            public void afterTextChanged(Editable s) {
        
            }
        });
        
        return view;
    }
}
