package skippie.tutionhelper;


import android.graphics.Bitmap;

public class User {
    private String displayName;
    private String uid;
    private Bitmap avatar;

    public User(String displayName, String uid, Bitmap avatar) {
        this.displayName = displayName;
        this.uid = uid;
        this.avatar = avatar;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getUid() {
        return uid;
    }

    public Bitmap getAvatar() {
        return avatar;
    }
}
