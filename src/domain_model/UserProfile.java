package domain_model;

public class UserProfile {
    String username;
    String password;

    public UserProfile(String username, String password){
        this.username = username;
        this.password = password;
    }

    public String getUsername(){ return this.username; }
    public String getPassword(){ return this.password; }

    public void setUsername(String new_username){ this.username = new_username; }
    public void setPassword(String new_password){ this.password = new_password; }
}
