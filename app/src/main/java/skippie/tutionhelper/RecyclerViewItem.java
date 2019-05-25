package skippie.tutionhelper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

public class RecyclerViewItem {
    
    private static final String TAG = "RECYCLER_VIEW_ITEM";
    
    private String encodedImage;
    private String userName;
    private String userAInfo;
    
    public RecyclerViewItem(String encodedImage, String userName, String userAInfo) {
        this.encodedImage = encodedImage;
        this.userName = userName;
        this.userAInfo = userAInfo;
    }
    
    public Bitmap getImageBitmap() {
        try {
            byte[] encodedImageByteArray = Base64.decode(encodedImage, Base64.DEFAULT);
            
            return BitmapFactory.decodeByteArray(encodedImageByteArray, 0, encodedImageByteArray.length);
        }catch(Exception e) {
            Log.e(TAG, e.getMessage());
            
            return null;
        }
    }
    
    public String getUserName() {
        return userName;
    }
    
    public String getUserAInfo() {
        return userAInfo;
    }
}
