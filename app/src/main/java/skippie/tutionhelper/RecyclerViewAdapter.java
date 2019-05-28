package skippie.tutionhelper;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {
    
    private Context mContext;
    private List<User> mData;
    
    public RecyclerViewAdapter(Context mContext, List<User> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }
    
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
    
        View view;
        LayoutInflater mInflayer = LayoutInflater.from(mContext);
        view = mInflayer.inflate(R.layout.fragment_user, viewGroup, false);
    
    
        return new MyViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
    
        myViewHolder.TV_displayName.setText(mData.get(i).getDisplayName());
        myViewHolder.IV_avatar.setImageBitmap(mData.get(i).getAvatar());
        
    }
    
    @Override
    public int getItemCount() {
        return mData.size();
    }
    
    
    
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView TV_displayName;
        ImageView IV_avatar;
        
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
    
            TV_displayName = itemView.findViewById(R.id.FragmentUser_displayName);
            IV_avatar = itemView.findViewById(R.id.FragmentUser_avatar);
        }
    }
    
}
