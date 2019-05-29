package skippie.tutionhelper;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class EditAccountActivity extends AppCompatActivity {
    
    //Firebase
    private FirebaseAuth Auth;
    private FirebaseUser CurrentUser;
    private FirebaseFirestore DB;
    
    
    //Variables
    private String CurrentUID;
    private String CurrentDisplayName;
    private String CurrentAvatar;
    private String CurrentAInfo;
    
    
    //Views
    private ImageView IV_Avatar;
    
    private TextView TV_Error;
    
    private EditText ET_Name;
    private EditText ET_AdditionalInfo;
    
    
    public void initialize() {
    
        //Firebase
        Auth = FirebaseAuth.getInstance();
        CurrentUser = Auth.getCurrentUser();
        CurrentUID = CurrentUser.getUid();
        DB = FirebaseFirestore.getInstance();
    
        DB.collection("users")
                .document(CurrentUID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        //Variables
                        CurrentDisplayName = CurrentUser.getDisplayName();
                        CurrentAvatar = task.getResult().get("Avatar").toString();
                        try {
                            CurrentAInfo = task.getResult().get("AInfo").toString();
                        }catch(Exception ignored) {
                            CurrentAInfo = "";
                        }
    
                        //Views
                        IV_Avatar = findViewById(R.id.EditAccount_AvatarImageView);
                        IV_Avatar.setImageBitmap(TuitionHelper.decodeStringToBitmap(CurrentAvatar));
    
                        TV_Error = findViewById(R.id.EditAccount_ErrorTextView);
    
                        ET_Name = findViewById(R.id.EditAccount_NameEditText);
                        ET_Name.setText(CurrentDisplayName);
                        ET_AdditionalInfo = findViewById(R.id.EditAccount_AInfoEditText);
                        ET_AdditionalInfo.setText(CurrentAInfo);
    
                    }
                });
        
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_account);
    
        initialize();
        
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 0 && resultCode == RESULT_OK) {
            try {
                CurrentAvatar = TuitionHelper.BitMapToString((Bitmap) data.getExtras().get("data"));
            }catch(Exception ignored) {}finally {
                IV_Avatar.setImageBitmap(TuitionHelper.decodeStringToBitmap(CurrentAvatar));
            }
        }else if(requestCode == 1 && resultCode == RESULT_OK) {
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
    
                CurrentAvatar = TuitionHelper.BitMapToString(selectedImage);
                
            }catch(IOException e) {
                e.printStackTrace();
            }finally {
                IV_Avatar.setImageBitmap(TuitionHelper.decodeStringToBitmap(CurrentAvatar));
            }
        }
    }
    
    public void CameraButtonListener(View view){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
            
            }
        }
        Intent CameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(CameraIntent, 0);
    }
    
    public void StorageButtonListener(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 1);
    }
    
    public void SaveButtonListener(View view) {
        UserProfileChangeRequest request = new UserProfileChangeRequest.Builder().setDisplayName(ET_Name.getText().toString()).build();
        CurrentUser.updateProfile(request);
    
        Map<String, Object> document = new HashMap<>();
        
        document.put("Name",  ET_Name.getText().toString());
        document.put("Avatar", CurrentAvatar);
        document.put("AInfo", ET_AdditionalInfo.getText().toString());
    
        DB.collection("users").document(CurrentUID).update(document);
    }
}
