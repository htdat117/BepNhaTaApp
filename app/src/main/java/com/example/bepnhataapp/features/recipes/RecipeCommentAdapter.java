package com.example.bepnhataapp.features.recipes;

import android.graphics.BitmapFactory;
import com.bumptech.glide.Glide;
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
    private java.util.Set<Long> likedSet = new java.util.HashSet<>();

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
                android.graphics.Bitmap bmp = BitmapFactory.decodeByteArray(avatar,0,avatar.length);
                if(bmp!=null){
                    holder.imageViewProfile.setImageBitmap(bmp);
                }else{
                    holder.imageViewProfile.setImageResource(R.drawable.ic_avatar);
                }
            } else if(customer instanceof com.example.bepnhataapp.common.model.Customer && ((com.example.bepnhataapp.common.model.Customer)customer).getAvatarLink()!=null){
                String link = ((com.example.bepnhataapp.common.model.Customer)customer).getAvatarLink();
                Object src = link;
                if(!link.startsWith("http")){
                    String resName = link;
                    int dot = resName.lastIndexOf('.');
                    if(dot>0) resName = resName.substring(0,dot);
                    int resId = holder.itemView.getContext().getResources().getIdentifier(resName, "drawable", holder.itemView.getContext().getPackageName());
                    if(resId!=0) src = resId;
                }
                Glide.with(holder.itemView.getContext())
                        .load(src)
                        .placeholder(R.drawable.ic_avatar)
                        .into(holder.imageViewProfile);
            } else {
                holder.imageViewProfile.setImageResource(R.drawable.ic_avatar);
            }
        } else {
            holder.textViewUserName.setText("Ẩn danh");
            holder.imageViewProfile.setImageResource(R.drawable.ic_avatar);
        }
        holder.textViewTime.setText(comment.getCreatedAt());
        holder.textViewCommentText.setText(comment.getContent());
        holder.textViewLikes.setText(String.format(Locale.getDefault(), "Hữu ích (%d)", comment.getUsefulness()));
        boolean isLiked = likedSet.contains(comment.getRecipeCommentID());
        holder.imageViewLike.setImageResource(isLiked ? R.drawable.ic_heart_filled : R.drawable.ic_like_outline);

        View.OnClickListener likeListener = v -> {
            android.content.Context ctx = holder.itemView.getContext();
            if(!com.example.bepnhataapp.common.utils.SessionManager.isLoggedIn(ctx)){
                android.widget.Toast.makeText(ctx, "Vui lòng đăng nhập để đánh giá hữu ích", android.widget.Toast.LENGTH_SHORT).show();
                return;
            }
            boolean curLiked = likedSet.contains(comment.getRecipeCommentID());
            if(curLiked){
                if(comment.getUsefulness()>0) comment.setUsefulness(comment.getUsefulness()-1);
                likedSet.remove(comment.getRecipeCommentID());
            }else{
                comment.setUsefulness(comment.getUsefulness()+1);
                likedSet.add(comment.getRecipeCommentID());
            }
            new com.example.bepnhataapp.common.dao.RecipeCommentDao(ctx).update(comment);
            holder.textViewLikes.setText(String.format(Locale.getDefault(), "Hữu ích (%d)", comment.getUsefulness()));
            holder.imageViewLike.setImageResource(likedSet.contains(comment.getRecipeCommentID()) ? R.drawable.ic_heart_filled : R.drawable.ic_like_outline);
        };
        holder.textViewLikes.setOnClickListener(likeListener);
        holder.imageViewLike.setOnClickListener(likeListener);
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
