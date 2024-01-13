package das.mobile.hearmony.model;

public class User {
    private String id;
    private String name;
    private String email;
    private String profilePict;
    private String phoneNum;


    public User() {
    }

    public User(String id, String name, String email, String profilePict, String phoneNum) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.profilePict = profilePict;
        this.phoneNum = phoneNum;
    }

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

    public String getProfilePict() {
        return profilePict;
    }

    public void setProfilePict(String profilePict) {
        this.profilePict = profilePict;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoaneNum) {
        this.phoneNum = phoneNum;
    }
}

