package skippie.tutionhelper;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.MyViewHolder> {

    private List<RecyclerViewItem> itemsList;
    private Context context;

    public SearchAdapter(List<RecyclerViewItem> itemsList, Context context) {
        this.itemsList = itemsList;
        this.context = context;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView IV_userAvatar;
        
        private TextView TV_userName;
        private TextView TV_userAInfo;

        public MyViewHolder(View view){
            super(view);
            
            IV_userAvatar = view.findViewById(R.id.AdapterListItem_UserAvatarImageView);
    
            TV_userName = view.findViewById(R.id.AdapterListItem_UserNameTextView);
            TV_userAInfo = view.findViewById(R.id.AdapterListItem_UserAddInfo);
        }
        
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_recycler_view_item, viewGroup, false);
        
        return new MyViewHolder(view);
        
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        RecyclerViewItem item = itemsList.get(i);

        myViewHolder.IV_userAvatar.setImageBitmap(RecyclerViewItem.getImageBitmap());
        myViewHolder.TV_userName.setText(RecyclerViewItem.getUserName());
        myViewHolder.TV_userAInfo.setText(RecyclerViewItem.getUserAInfo());

    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }

}
