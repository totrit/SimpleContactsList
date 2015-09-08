package com.example.alvin.simplecontactlist.model;

import java.util.List;

/**
 * Created by maruilin on 15/9/7.
 */
public interface IDataLoadCallback {
    void onSuccess(List<PerContactInfo> fetchedRawList);
    void onFail(String errMsg);
}
