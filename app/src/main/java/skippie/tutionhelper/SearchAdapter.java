package skippie.tutionhelper;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.MyViewHolder> {

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView name;
        public TextView aInfo;
        public ImageView userImage;

        public MyViewHolder(View view){
            super(view);
    
            name = view.findViewById(R.id.AdapterListItem_UserNameTextView);
            aInfo = view.findViewById(R.id.AdapterListItem_UserAddInfo);
            userImage = view.findViewById(R.id.AdapterListItem_UserAvatarImageView);
        }
    }
    
    public SearchAdapter() {
    
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_recycler_view_item, viewGroup, false);
        
        MyViewHolder myViewHolder = new MyViewHolder(view);
        
        return myViewHolder;
        
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

}
