package das.mobile.hearmony.model;

public class User {
    private String id;
    private String name;
    private String email;
    private String profileUrl;
    private String phoneNum;


    public User() {
    }

    public User(String id, String name, String email, String profileUrl, String phoneNum) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.profileUrl = profileUrl;
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

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }
}

