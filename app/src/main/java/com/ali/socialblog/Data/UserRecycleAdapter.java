package com.ali.socialblog.Data;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ali.socialblog.Model.User;
import com.ali.socialblog.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class UserRecycleAdapter extends RecyclerView.Adapter<UserRecycleAdapter.ViewHolder> {

    private Context context;
    private List<User> userList;

    public UserRecycleAdapter(Context context, List<User> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.user_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String url = null;
        User user = userList.get(position);
        holder.fullname.setText(user.getFirstName() + " " + user.getLastName());
        url = user.getImage();
        Picasso.get().load(url).fit().into(holder.img);


    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView fullname;
        ImageView img;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            fullname = itemView.findViewById(R.id.tvNameUserListID);
            img = itemView.findViewById(R.id.imgUserListID);
        }
    }
}
