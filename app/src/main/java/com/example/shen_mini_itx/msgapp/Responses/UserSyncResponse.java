package com.example.shen_mini_itx.msgapp.Responses;

/**
 * Created by shen-mini-itx on 22-Jul-16.
 */
public class UserSyncResponse extends CommonResponse{

    private boolean newUser;
    private String api;
    private String id;

    public boolean isNewUser() {
        return newUser;
    }

    public void setNewUser(boolean newUser) {
        this.newUser = newUser;
    }

    public String getApi() {
        return api;
    }

    public void setApi(String api) {
        this.api = api;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
