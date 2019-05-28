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
    private final List<User> mData;

    public interface OnItemClickListener{
        void OnItemClick(User item);
    }

    private final OnItemClickListener listener;
    
    public RecyclerViewAdapter(Context mContext, List<User> mData, OnItemClickListener listener) {
        this.mContext = mContext;
        this.mData = mData;
        this.listener = listener;
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
        myViewHolder.bind(mData.get(i), listener);
    }
    
    @Override
    public int getItemCount() {
        if(mData != null)
            return mData.size();
        return 0;
    }
    
    
    
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView TV_displayName;
        ImageView IV_avatar;
        
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
    
            TV_displayName = itemView.findViewById(R.id.FragmentUser_displayName);
            IV_avatar = itemView.findViewById(R.id.FragmentUser_avatar);
        }

        public void bind(final User item, final OnItemClickListener listener) {
            TV_displayName.setText(item.getDisplayName());
            IV_avatar.setImageBitmap(item.getAvatar());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.OnItemClick(item);
                }
            });
        }
    }
    
}
