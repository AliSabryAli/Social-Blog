package com.ali.socialblog.Data;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ali.socialblog.Model.Blog;
import com.ali.socialblog.R;
import com.ali.socialblog.Util.Constant;
import com.squareup.picasso.Picasso;

import java.util.List;

public class BlogRecycleAdapter extends RecyclerView.Adapter<BlogRecycleAdapter.ViewHolder> {

    private Context context;
    private List<Blog> blogList;

    public BlogRecycleAdapter(Context context, List<Blog> blogList) {
        this.context = context;
        this.blogList = blogList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.post_row, viewGroup, false);
        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {

        Blog blog = blogList.get(position);
        String imageUrl = null;

        viewHolder.tvTitle.setText(blog.getpTitle());
        viewHolder.tvdescp.setText(blog.getDesc());
        viewHolder.tvTime.setText(Constant.getTime(blog.getTimeStamp()));

        imageUrl = blog.getpImage();
        Log.d("Adapter", "onBindViewHolder: " + imageUrl + " : " + position);
        Picasso.get().load(imageUrl).fit().into(viewHolder.img);

    }

    @Override
    public int getItemCount() {
        return blogList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvTitle;
        public TextView tvdescp;
        public TextView tvTime;
        public ImageView img;
        String userId;

        public ViewHolder(@NonNull View itemView, Context ctx) {
            super(itemView);
            context = ctx;
            tvTitle = itemView.findViewById(R.id.tvPostTitleID);
            tvdescp = itemView.findViewById(R.id.tvPosttextListID);
            tvTime = itemView.findViewById(R.id.tvPostTimeListID);
            img = itemView.findViewById(R.id.imgPostListID);

            userId = null;

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

        }
    }
}
