package das.mobile.hearmony.model;

public class Psikolog extends User {
    private String officeLocation;
    private String profileDescription;

    public String getOfficeLocation() {
        return officeLocation;
    }

    public void setOfficeLocation(String officeLocation) {
        this.officeLocation = officeLocation;
    }

    public String getProfileDescription() {
        return profileDescription;
    }

    public void setProfileDescription(String profileDescription) {
        this.profileDescription = profileDescription;
    }

    public Psikolog() {
    }

    public Psikolog(String id, String name, String email, String profilePict, String phoneNum, String officeLocation, String profileDescription) {
        super(id, name, email, profilePict, phoneNum);
        this.officeLocation = officeLocation;
        this.profileDescription = profileDescription;
    }
}
