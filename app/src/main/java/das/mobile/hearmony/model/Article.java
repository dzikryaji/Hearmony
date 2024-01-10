package das.mobile.hearmony.model;

public class Article {
    private String id;
    private String title;
    private String thumbnailPath;
    private String timestamp;
    private String category;
    private String comment;

    public Article(String id, String title, String thumbnailPath, String category, String timestamp) {
        this.id = id;
        this.title = title;
        this.thumbnailPath = thumbnailPath;
        this.category = category;
        this.timestamp = timestamp;
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
}
