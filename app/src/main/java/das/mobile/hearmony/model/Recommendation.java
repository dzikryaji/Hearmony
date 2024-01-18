package das.mobile.hearmony.model;

public class Recommendation {
    private String id;
    private String category;
    private String price;
    private String subcategory;
    private String thumbnail;
    private String timestamp;
    private String title;

    public Recommendation(String id, String category, String price, String subcategory, String thumbnail, String timestamp, String title) {
        this.id = id;
        this.category = category;
        this.price = price;
        this.subcategory = subcategory;
        this.thumbnail = thumbnail;
        this.timestamp = timestamp;
        this.title = title;
    }

    public Recommendation() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(String subcategory) {
        this.subcategory = subcategory;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
