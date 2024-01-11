package das.mobile.hearmony.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Article implements Parcelable {
    private String id;
    private String title;
    private String thumbnailPath;
    private String timestamp;
    private String category;
    private String content;
    private String comment;
    private String author;
    private String editor;

    public Article(String id, String title, String thumbnailPath, String timestamp, String category, String content, String comment) {
        this.id = id;
        this.title = title;
        this.thumbnailPath = thumbnailPath;
        this.timestamp = timestamp;
        this.category = category;
        this.content = content;
        this.comment = comment;
    }

    public Article(String id, String title, String thumbnailPath, String timestamp, String category, String content, String comment, String author, String editor) {
        this.id = id;
        this.title = title;
        this.thumbnailPath = thumbnailPath;
        this.timestamp = timestamp;
        this.category = category;
        this.content = content;
        this.comment = comment;
        this.author = author;
        this.editor = editor;
    }

    public Article() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumbnailPath() {
        return thumbnailPath;
    }

    public void setThumbnailPath(String thumbnailPath) {
        this.thumbnailPath = thumbnailPath;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getEditor() {
        return editor;
    }

    public void setEditor(String editor) {
        this.editor = editor;
    }

    // Parcelable implementation
    protected Article(Parcel in) {
        id = in.readString();
        title = in.readString();
        thumbnailPath = in.readString();
        timestamp = in.readString();
        category = in.readString();
        content = in.readString();
        comment = in.readString();
        author = in.readString();
        editor = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(thumbnailPath);
        dest.writeString(timestamp);
        dest.writeString(category);
        dest.writeString(content);
        dest.writeString(comment);
        dest.writeString(author);
        dest.writeString(editor);
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
