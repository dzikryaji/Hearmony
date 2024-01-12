package das.mobile.hearmony.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Comment implements Parcelable {
    private String key;
    private String userId;
    private String comment;
    private String timestamp;

    public Comment() {
    }

    public Comment(String key, String userId, String comment, String timestamp) {
        this.key = key;
        this.userId = userId;
        this.comment = comment;
        this.timestamp = timestamp;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    protected Comment(Parcel in) {
        key = in.readString();
        userId = in.readString();
        timestamp = in.readString();
        comment = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(key);
        dest.writeString(userId);
        dest.writeString(timestamp);
        dest.writeString(comment);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Comment> CREATOR = new Creator<Comment>() {
        @Override
        public Comment createFromParcel(Parcel in) {
            return new Comment(in);
        }

        @Override
        public Comment[] newArray(int size) {
            return new Comment[size];
        }
    };
}
