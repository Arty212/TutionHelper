package skippie.tutionhelper;


import android.graphics.Bitmap;

public class User {
    private String displayName;
    private Bitmap avatar;
    
    public User(String displayName, Bitmap avatar) {
        this.displayName = displayName;
        this.avatar = avatar;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public Bitmap getAvatar() {
        return avatar;
    }
    
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
    
    public void setAvatar(Bitmap avatar) {
        this.avatar = avatar;
    }
}
