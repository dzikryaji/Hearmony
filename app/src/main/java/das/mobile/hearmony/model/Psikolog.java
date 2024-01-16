package das.mobile.hearmony.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Psikolog extends User implements Parcelable {
    private String roles;

    private String officeName;
    private String officeLocation;
    private String profileDescription;
    private String treatment;
    private String experience;
    private String education;

    public String getOfficeName() {
        return officeName;
    }

    public void setOfficeName(String officeName) {
        this.officeName = officeName;
    }

    public String getTreatment() {
        return treatment;
    }

    public void setTreatment(String treatment) {
        this.treatment = treatment;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

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

    public Psikolog(String id, String name, String email, String profilePict, String phoneNum, String roles, String officeName, String officeLocation, String profileDescription, String treatment, String experience, String education) {
        super(id, name, email, profilePict, phoneNum);
        this.roles = roles;
        this.officeName = officeName;
        this.officeLocation = officeLocation;
        this.profileDescription = profileDescription;
        this.treatment = treatment;
        this.experience = experience;
        this.education = education;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getId());
        dest.writeString(getName());
        dest.writeString(getEmail());
        dest.writeString(getPhoneNum());
        dest.writeString(getProfilePict());
        dest.writeString(roles);
        dest.writeString(officeName);
        dest.writeString(officeLocation);
        dest.writeString(profileDescription);
        dest.writeString(treatment);
        dest.writeString(experience);
        dest.writeString(education);
    }

    protected Psikolog(Parcel in) {
        super(in.readString(), in.readString(), in.readString(), in.readString(), in.readString());
        roles = in.readString();
        officeName = in.readString();
        officeLocation = in.readString();
        profileDescription = in.readString();
        treatment = in.readString();
        experience = in.readString();
        education = in.readString();
    }

    public static final Creator<Psikolog> CREATOR = new Creator<Psikolog>() {
        @Override
        public Psikolog createFromParcel(Parcel in) {
            return new Psikolog(in);
        }

        @Override
        public Psikolog[] newArray(int size) {
            return new Psikolog[size];
        }
    };
}
