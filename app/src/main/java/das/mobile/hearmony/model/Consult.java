package das.mobile.hearmony.model;

public class Consult {
    private String consultId;
    private String userId;
    private String price;
    private String hour;
    private String date;

    public String getConsultId() {
        return consultId;
    }

    public void setConsultId(String consultId) {
        this.consultId = consultId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Consult() {
    }

    public Consult(String consultId, String userId, String price, String hour, String date) {
        this.consultId = consultId;
        this.userId = userId;
        this.price = price;
        this.hour = hour;
        this.date = date;
    }
}
