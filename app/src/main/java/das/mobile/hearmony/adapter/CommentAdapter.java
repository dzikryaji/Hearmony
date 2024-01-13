package das.mobile.hearmony.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import das.mobile.hearmony.databinding.ItemCommentBinding;
import das.mobile.hearmony.model.Comment;
import das.mobile.hearmony.model.User;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    private List<Comment> commentList;
    private DatabaseReference userRef;

    public CommentAdapter(List<Comment> commentList) {
        this.commentList = commentList;
        userRef = FirebaseDatabase.getInstance().getReference().child("users");
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CommentViewHolder(ItemCommentBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        Comment comment = commentList.get(position);

        // Add a null check for comment.getUserId()
        if (comment.getUserId() != null) {
            userRef.child(comment.getUserId()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        User user = snapshot.getValue(User.class);
                        if (user != null) {
                            holder.binding.tvComment.setText(comment.getComment());
                            holder.binding.tvName.setText(user.getName());
                            holder.binding.tvTimestamp.setText(formatTimeStamp(comment.getTimestamp()));
                            setAvatar(holder, user.getProfilePict());
                        } else {
                            holder.binding.getRoot().setVisibility(View.GONE);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } else {
            // Handle the case where comment.getUserId() is null
            holder.binding.getRoot().setVisibility(View.GONE);
        }
    }



    @Override
    public int getItemCount() {
        if (commentList.isEmpty()){
            return 0;
        }
        return commentList.size();
    }

    private String formatTimeStamp(String TimeStamp) {
        if (TimeStamp != null && !TimeStamp.isEmpty()){
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                Date date = dateFormat.parse(TimeStamp);
                long currentTime = System.currentTimeMillis();
                long timeDifference = currentTime - date.getTime();
                long seconds = timeDifference / 1000;
                long minutes = seconds / 60;
                long hours = minutes / 60;
                long days = hours / 24;
                long weeks = days / 7;
                long months = days / 30;
                long years = days / 365;

                if (seconds < 60) {
                    if (seconds == 1) {
                        return seconds + " second ago";
                    } else {
                        return seconds + " seconds ago";
                    }
                } else if (minutes < 60) {
                    if (minutes == 1) {
                        return minutes + " minute ago";
                    } else {
                        return minutes + " minutes ago";
                    }
                } else if (hours < 24) {
                    if (hours == 1) {
                        return hours + " hour ago";
                    } else {
                        return hours + " hours ago";
                    }
                } else if (days < 7) {
                    if (days == 1) {
                        return days + " day ago";
                    } else {
                        return days + " days ago";
                    }
                } else if (weeks < 4) {
                    if (weeks == 1) {
                        return weeks + " week ago";
                    } else {
                        return weeks + " weeks ago";
                    }
                } else if (months < 12) {
                    if (months == 1) {
                        return months + " month ago";
                    } else {
                        return months + " months ago";
                    }
                } else {
                    if (years == 1) {
                        return years + " year ago";
                    } else {
                        return years + " years ago";
                    }
                }
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        } else {
            return "a moment ago";
        }

    }
    private void setAvatar(CommentViewHolder holder, String avatarValue) {
        StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
        final StorageReference imgRef = mStorageRef.child("/avatar/" + avatarValue + ".jpg");

        imgRef.getDownloadUrl().addOnSuccessListener(uri -> {
            Picasso.get().load(uri).memoryPolicy(MemoryPolicy.NO_CACHE).into(holder.binding.ivAvatar);
        }).addOnFailureListener(exception -> {
            Log.d("error===========", exception.getMessage());
        });
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder {
        ItemCommentBinding binding;
        public CommentViewHolder(@NonNull ItemCommentBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
