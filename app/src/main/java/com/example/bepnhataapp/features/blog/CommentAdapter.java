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
import com.example.bepnhataapp.common.model.BlogComment;
import com.example.bepnhataapp.common.dao.CustomerDao;
import com.example.bepnhataapp.common.model.Customer;
import android.graphics.BitmapFactory;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    private List<BlogComment> commentList;

    public CommentAdapter(List<BlogComment> commentList) {
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
        BlogComment comment = commentList.get(position);
        // Lấy thông tin user
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
        holder.imageViewLike.setOnClickListener(v -> {
            // TODO: Xử lý like bình luận
        });
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public void updateData(List<BlogComment> newList) {
        this.commentList.clear();
        this.commentList.addAll(newList);
        notifyDataSetChanged();
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