package com.example.bepnhataapp.features.recipes;

import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bepnhataapp.R;
import com.example.bepnhataapp.common.dao.CustomerDao;
import com.example.bepnhataapp.common.model.Customer;
import com.example.bepnhataapp.common.model.RecipeComment;

import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecipeCommentAdapter extends RecyclerView.Adapter<RecipeCommentAdapter.CommentViewHolder> {

    private List<RecipeComment> commentList;

    public RecipeCommentAdapter(List<RecipeComment> commentList) {
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
        RecipeComment comment = commentList.get(position);
        // Get user info
        CustomerDao customerDao = new CustomerDao(holder.itemView.getContext());
        Customer customer = customerDao.findById(comment.getCustomerID());
        if (customer != null) {
            holder.textViewUserName.setText(customer.getFullName());
            byte[] avatar = customer.getAvatar();
            if (avatar != null && avatar.length > 0) {
                holder.imageViewProfile.setImageBitmap(BitmapFactory.decodeByteArray(avatar, 0, avatar.length));
            } else {
                holder.imageViewProfile.setImageResource(R.drawable.profile_placeholder);
            }
        } else {
            holder.textViewUserName.setText("Ẩn danh");
            holder.imageViewProfile.setImageResource(R.drawable.profile_placeholder);
        }
        holder.textViewTime.setText(comment.getCreatedAt());
        holder.textViewCommentText.setText(comment.getContent());
        holder.textViewLikes.setText(String.format(Locale.getDefault(), "Hữu ích (%d)", comment.getUsefulness()));
        holder.imageViewLike.setImageResource(R.drawable.ic_like_outline);
        // TODO: like click if needed
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public void updateData(List<RecipeComment> newList) {
        this.commentList.clear();
        this.commentList.addAll(newList);
        notifyDataSetChanged();
    }

    static class CommentViewHolder extends RecyclerView.ViewHolder {
        CircleImageView imageViewProfile;
        TextView textViewUserName;
        TextView textViewTime;
        TextView textViewCommentText;
        TextView textViewLikes;
        ImageView imageViewLike;
        TextView textViewReply;
        CommentViewHolder(@NonNull View itemView) {
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