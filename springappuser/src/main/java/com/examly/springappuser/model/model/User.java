package model;

public class User {

    private Long  userId;
    private String email;
    private String username;
    private String mobileNumber;
    private String userRole;

    public User() {}
    
    public User(Long userId, String email, String username, String mobileNumber, String userRole) {
        this.userId = userId;
        this.email = email;
        this.username = username;
        this.mobileNumber = mobileNumber;
        this.userRole = userRole;
    }
    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getMobileNumber() {
        return mobileNumber;
    }
    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }
    public String getUserRole() {
        return userRole;
    }
    public void setUserRole(String userRole) {
        this.userRole = userRole;
    } 

    

}
