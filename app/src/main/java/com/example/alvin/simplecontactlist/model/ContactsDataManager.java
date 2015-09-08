package com.example.alvin.simplecontactlist.model;

import android.content.Context;
import android.util.SparseArray;

import com.example.alvin.simplecontactlist.utils.NetUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by maruilin on 15/9/7.
 */
public class ContactsDataManager {
    public final static int SORT_NONE = 0;
    public final static int SORT_ASC = 1;
    public final static int SORT_DESC = 2;
    private Context mAppContext = null;
    SparseArray<PerContactInfo> mContactsMap = null;
    List<PerContactInfo> mContactsList = null;
    private static ContactsDataManager sInstance = null;
    private List<IDataLoadCallback> mLoadListeners = new ArrayList<IDataLoadCallback>(1);
    private volatile boolean loading = false;

    public static ContactsDataManager getInstance() {
        if (sInstance != null) {
            return sInstance;
        } else {
            synchronized (ContactsDataManager.class) {
                if (sInstance == null) {
                    sInstance = new ContactsDataManager();
                }
            }
            return sInstance;
        }
    }

    public void setAppContext(Context context) {
        mAppContext = context;
    }

    public PerContactInfo getContactInfo(int id) {
        return mContactsMap.get(id);
    }

    public void loadContacts(IDataLoadCallback callback, boolean forceReload) {
        if (mContactsMap != null && mContactsMap.size() != 0
                && !forceReload) {
            callback.onSuccess(mContactsList);
            return;
        }
        synchronized (mLoadListeners) {
            mLoadListeners.add(callback);
            if (!loading) {
                loading = true;
                NetUtils.asyncFetchContacts(mAppContext, new IDataLoadCallback() {
                    @Override
                    public void onSuccess(List<PerContactInfo> fetchedRawList) {
                        mContactsList = fetchedRawList;
                        if (fetchedRawList != null && fetchedRawList.size() != 0
                                && mContactsMap == null) {
                            mContactsMap = new SparseArray<PerContactInfo>(10);
                        } else if (mContactsMap != null) {
                            mContactsMap.clear();
                        }
                        for (int i = 0; i < fetchedRawList.size(); i ++) {
                            mContactsMap.append(fetchedRawList.get(i).id, fetchedRawList.get(i));
                        }
                        synchronized (mLoadListeners) {
                            loading = false;
                            for (int i = 0; i < mLoadListeners.size(); i ++) {
                                mLoadListeners.get(i).onSuccess(fetchedRawList);
                            }
                            mLoadListeners.clear();
                        }
                    }

                    @Override
                    public void onFail(String errMsg) {
                        // cached data will not be cleared
                        synchronized (mLoadListeners) {
                            loading = false;
                            for (int i = 0; i < mLoadListeners.size(); i ++) {
                                mLoadListeners.get(i).onFail(errMsg);
                            }
                            mLoadListeners.clear();
                        }
                    }
                });
            }
        }
    }

    public List<PerContactInfo> getContactsList(int sortWay) {
        switch (sortWay) {
            case SORT_NONE: {
                return mContactsList;
            }
            case SORT_ASC: {
                Collections.sort(mContactsList, new Comparator<PerContactInfo>() {
                    @Override
                    public int compare(PerContactInfo p0, PerContactInfo p1) {
                        return p0.name.compareToIgnoreCase(p1.name);
                    }
                });
                return mContactsList;
            }
            case SORT_DESC: {
                Collections.sort(mContactsList, new Comparator<PerContactInfo>() {
                    @Override
                    public int compare(PerContactInfo p0, PerContactInfo p1) {
                        return p1.name.compareToIgnoreCase(p0.name);
                    }
                });
                return mContactsList;
            }
            default: {
                return mContactsList;
            }
        }
    }
}
