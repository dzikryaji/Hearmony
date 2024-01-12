package das.mobile.hearmony.model;

public class User {
    private String id;
    private String name;
    private String email;
    private int avatar;
    private String phoneNum;


    public User() {
    }

    public User(String id, String name, String email, int avatar, String phoneNum) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.avatar = avatar;
        this.phoneNum = phoneNum;
    }

    // Add getters and setters as needed

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getAvatar() {
        return avatar;
    }

    public void setAvatar(int avatar) {
        this.avatar = avatar;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoaneNum) {
        this.phoneNum = phoneNum;
    }
}

