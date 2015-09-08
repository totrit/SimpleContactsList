package com.example.alvin.simplecontactlist.view;

import android.app.Fragment;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.alvin.simplecontactlist.R;
import com.example.alvin.simplecontactlist.control.ITopLevelDelegate;
import com.example.alvin.simplecontactlist.databinding.FragContactDetailBinding;
import com.example.alvin.simplecontactlist.model.ContactsDataManager;
import com.example.alvin.simplecontactlist.model.IDataLoadCallback;
import com.example.alvin.simplecontactlist.model.PerContactInfo;
import com.example.alvin.simplecontactlist.utils.LogUtils;

import java.util.List;

/**
 * Created by maruilin on 15/9/7.
 */
public class ContactDetailFrag extends Fragment{
    private final static String LOG_TAG = "ContactDetailFrag";
    private ITopLevelDelegate mTopLevelDelegate;
    public ContactDetailFrag() {
    }

    public static ContactDetailFrag createInstance(int id, ITopLevelDelegate topLevelDelegate) {
        ContactDetailFrag newFrag = new ContactDetailFrag();
        newFrag.mTopLevelDelegate = topLevelDelegate;

        Bundle args = new Bundle();
        args.putInt("id", id);
        newFrag.setArguments(args);

        return newFrag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View fragRoot = inflater.inflate(R.layout.frag_contact_detail, container, false);
        this.setHasOptionsMenu(false);
        ((ITopLevelDelegate)getActivity()).showBackButton(true);
        final int id = getArguments().getInt("id");
        ContactsDataManager.getInstance().loadContacts(new IDataLoadCallback() {
            @Override
            public void onSuccess(List<PerContactInfo> fetchedRawList) {
                PerContactInfo info = ContactsDataManager.getInstance().getContactInfo(id);
                LogUtils.d(LOG_TAG, "displaying contact, id=" + id + ", info=" + info);
                mTopLevelDelegate.changeTitle(info.name);
                FragContactDetailBinding binding = DataBindingUtil.bind(fragRoot);
                binding.setDetail(info);
            }

            @Override
            public void onFail(String errMsg) {
                //TODO: tips
            }
        }, false);

        return fragRoot;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        super.onCreateOptionsMenu(menu, inflater);
    }

}
