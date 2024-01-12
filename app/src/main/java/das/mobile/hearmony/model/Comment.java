package das.mobile.hearmony.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Comment implements Parcelable {
    private String key;
    private String userID;
    private String comment;
    private String timestamp;

    public Comment() {
    }

    public Comment(String key, String userID, String comment, String timestamp) {
        this.key = key;
        this.userID = userID;
        this.comment = comment;
        this.timestamp = timestamp;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
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
        userID = in.readString();
        timestamp = in.readString();
        comment = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(key);
        dest.writeString(userID);
        dest.writeString(timestamp);
        dest.writeString(comment);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Article> CREATOR = new Creator<Article>() {
        @Override
        public Article createFromParcel(Parcel in) {
            return new Article(in);
        }

        @Override
        public Article[] newArray(int size) {
            return new Article[size];
        }
    };
}
