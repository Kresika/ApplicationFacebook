package com.example.facebook.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.facebook.R;
import com.example.facebook.model.Post;
import com.example.facebook.utils.Constants;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.Holder> {

    Context context;
    private static final String TAG = "PostsAdapter";
    List<Post> postsList;

    public PostsAdapter(Context context, List<Post> postsList){
        this.context = context;
        this.postsList = postsList;
    }

    @NonNull
    @Override
    public PostsAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.post_layout, parent, false);
        Holder holder = new Holder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull PostsAdapter.Holder holder, int position) {
        Post post = postsList.get(position);

        holder.usernametxt.setText(post.getUser().getFirstname() + " " + post.getUser().getLastname());
        holder.captiontxt.setText(post.getCaption());
        String imgurl = Constants.IMAGE_URL + post.getImage();
        Log.d("Asdasd", "onResponse: " + imgurl);
        Picasso.get().load(imgurl).into(holder.postimg);



        String profileimg = Constants.IMAGE_URL + post.getUser().getImg();

        Picasso.get().load(profileimg).into(holder.profile);


    }

    @Override
    public int getItemCount() {
        return postsList.size();
    }




    public class Holder extends RecyclerView.ViewHolder{

        TextView usernametxt, captiontxt;
        ImageView profile, postimg;

        public Holder(View view){
            super(view);

            usernametxt = view.findViewById(R.id.username);
            captiontxt = view.findViewById(R.id.caption);
            profile = view.findViewById(R.id.postprofilepic);
            postimg = view.findViewById(R.id.postimg);

        }
    }
}
