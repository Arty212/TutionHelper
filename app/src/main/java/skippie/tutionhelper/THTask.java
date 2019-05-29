package skippie.tutionhelper;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class THTask {
    private String from;
    private String to;
    private String text;
    private String files;
    
    public THTask(String fromUID, String toUID, String text, String files) {
        this.from = fromUID;
        this.to = toUID;
        this.text = text;
        this.files = files;
    }
    
    public THTask(DocumentSnapshot doc) {
        this.from = doc.get("from").toString();
        this.to =  doc.get("to").toString();
        this.text = doc.get("text").toString();
        this.files =  doc.get("files").toString();
    }
    
    public String getFrom() {
        return from;
    }
    
    public String getTo() {
        return to;
    }
    
    public String getText() {
        return text;
    }
    
    public String getFiles() {
        return files;
    }
}
