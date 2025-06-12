package com.example.bepnhataapp.features.blog;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import java.util.Locale;

import com.example.bepnhataapp.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    private final List<Comment> commentList;

    public CommentAdapter(List<Comment> commentList) {
        this.commentList = commentList;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        Comment comment = commentList.get(position);
        holder.textViewUserName.setText(comment.getUserName());
        holder.textViewTime.setText(comment.getTime());
        holder.textViewCommentText.setText(comment.getCommentText());
        holder.textViewLikes.setText(String.format(Locale.getDefault(), "Hữu ích (%d)", comment.getLikes()));

        // Set profile image (using placeholder for now)
        holder.imageViewProfile.setImageResource(R.drawable.profile_placeholder);

        // Set like icon (using outline for now)
        holder.imageViewLike.setImageResource(R.drawable.ic_like_outline);

        // Handle like button click
        holder.imageViewLike.setOnClickListener(v -> {
            // TODO: Implement like logic here (e.g., update comment.likes and notifyDataSetChanged)
            // For now, just a placeholder action
            Toast.makeText(holder.itemView.getContext(), "Thích bình luận", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder {
        CircleImageView imageViewProfile;
        TextView textViewUserName;
        TextView textViewTime;
        TextView textViewCommentText;
        TextView textViewLikes;
        ImageView imageViewLike;
        TextView textViewReply;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewProfile = itemView.findViewById(R.id.imageViewProfile);
            textViewUserName = itemView.findViewById(R.id.textViewUserName);
            textViewTime = itemView.findViewById(R.id.textViewTime);
            textViewCommentText = itemView.findViewById(R.id.textViewCommentText);
            textViewLikes = itemView.findViewById(R.id.textViewLikes);
            imageViewLike = itemView.findViewById(R.id.imageViewLike);
            textViewReply = itemView.findViewById(R.id.textViewReply);
        }
    }
} 