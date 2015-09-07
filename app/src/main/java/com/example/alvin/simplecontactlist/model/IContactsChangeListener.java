package com.example.alvin.simplecontactlist.model;

import java.util.List;

/**
 * Created by maruilin on 15/9/7.
 */
public interface IContactsChangeListener {
    public void onChanged(List<PerContactInfo> newContacts);
}
