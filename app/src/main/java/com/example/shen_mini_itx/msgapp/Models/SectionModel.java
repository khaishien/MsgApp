package com.example.shen_mini_itx.msgapp.Models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shen-mini-itx on 02-Sep-16.
 */
public class SectionModel {


    private String headerTitle;
    private List<UserModel> userModels;

    public SectionModel() {
        userModels = new ArrayList<>();
    }

    public SectionModel(String headerTitle, List<UserModel> userModels) {
        this.headerTitle = headerTitle;
        this.userModels = userModels;
    }


    public String getHeaderTitle() {
        return headerTitle;
    }

    public void setHeaderTitle(String headerTitle) {
        this.headerTitle = headerTitle;
    }

    public List<UserModel> getUserModels() {
        return userModels;
    }

    public void setUserModels(List<UserModel> userModels) {
        this.userModels = userModels;
    }
}
