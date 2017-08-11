package com.ashutosh.internship.project;

import java.util.ArrayList;

/**
 * Created by ashutosh on 8/3/2017.
 */

public class DataModel {
    String errorString;
    Integer errorCode;
    String result;
    ArrayList<Data> data = null;

    public DataModel(String errorString, Integer errorCode, String result, ArrayList<Data> data) {
        this.errorString = errorString;
        this.errorCode = errorCode;
        this.result = result;
        this.data = data;
    }

    public String getErrorString() {
        return errorString;
    }

    public void setErrorString(String errorString) {
        this.errorString = errorString;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public ArrayList<Data> getData() {
        return data;
    }

    public void setData(ArrayList<Data> data) {
        this.data = data;
    }
}
