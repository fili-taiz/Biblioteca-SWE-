package domain_model;

public abstract class SoftwareUser {
    String user_code;
    String username;
    String name;
    String surname;
    String e_mail;
    String telephone_number;
    UserProfile user_profile;

    public SoftwareUser(String user_code, String username, String name, String surname, String e_mail, String telephone_number, UserProfile user_profile){
        this.user_code = user_code;
        this.username = username;
        this.name = name;
        this.surname = surname;
        this.e_mail = e_mail;
        this.telephone_number = telephone_number;
        this.user_profile = user_profile;
    }

    public String getUserCode() { return this.user_code; }
    public String getUsername() { return this.username; }
    public String getName() { return this.name; }
    public String getSurname() { return this.surname; }
    public String getEMail() { return this.e_mail; }
    public String getTelephoneNumber() { return this.telephone_number; }
    public UserProfile getUserProfile() { return this.user_profile; }

    public void setUserCode(String userCode) { this.user_code = userCode; }
    public void setUsername(String username) { this.username = username; }
    public void setName(String name) { this.name = name; }
    public void setSurname(String surname) { this.surname = surname; }
    public void setEMail(String eMail) { this.e_mail = eMail; }
    public void setTelephoneNumber(String telephoneNumber) { this.telephone_number = telephoneNumber; }
    public void setUserProfile(UserProfile new_user_profile){ this.user_profile = new_user_profile; }
}
