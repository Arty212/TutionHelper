package skippie.tutionhelper;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

public class TuitionHelper{
    
    public static Bitmap decodeStringToBitmap(String encodedImage){
        try {
            byte[] encodedImageByteArray = Base64.decode(encodedImage, Base64.DEFAULT);
        
            return BitmapFactory.decodeByteArray(encodedImageByteArray, 0, encodedImageByteArray.length);
        }catch(Exception e) {
            e.getMessage();
        
            return null;
        }
    }
    
    public static String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
    
    
        bitmap = Bitmap.createScaledBitmap(bitmap, 300, 400, true);
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        
        byte[] b = baos.toByteArray();
        
        String temp = Base64.encodeToString(b, Base64.DEFAULT);
        
        return temp;
    }
    
}
