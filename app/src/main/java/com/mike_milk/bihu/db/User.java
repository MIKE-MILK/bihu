package com.mike_milk.bihu.db;
import android.os.Parcel;
import android.os.Parcelable;
public class User implements Parcelable  {
    public User(){

    }
    private String userName;
    private String password;
    private String newPassword;
    private String token;
    private String avatar;

    protected User(Parcel in){
        userName=in.readString();
        password=in.readString();
        newPassword=in.readString();
        token=in.readString();
        avatar=in.readString();
    }
    public static final Creator<User>CREATOR=new Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public String getPassword() {
        return password;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getToken() {
        return token;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userName);
        dest.writeString(password);
        dest.writeString(newPassword);
        dest.writeString(token);
        dest.writeString(avatar);
    }
}
