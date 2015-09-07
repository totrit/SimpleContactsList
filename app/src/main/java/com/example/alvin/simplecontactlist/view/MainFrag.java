package com.example.alvin.simplecontactlist.view;

import android.databinding.DataBindingUtil;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.alvin.simplecontactlist.R;
import com.example.alvin.simplecontactlist.control.IContactsItemClickDelegate;
import com.example.alvin.simplecontactlist.databinding.MainListItemBinding;
import com.example.alvin.simplecontactlist.model.PerContactInfo;
import com.example.alvin.simplecontactlist.utils.NetUtils;

import java.util.List;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainFrag extends Fragment {
    private MainListAdapter mListAdapter;
    private RecyclerView mRecyclerView;
    private IContactsItemClickDelegate mClickHandler;

    public MainFrag() {
    }

    public void setClickHandler(IContactsItemClickDelegate clickHandler) {
        mClickHandler = clickHandler;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragRoot = inflater.inflate(R.layout.fragment_main, container, false);
        mRecyclerView = (RecyclerView) fragRoot.findViewById(R.id.lv);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        mListAdapter = new MainListAdapter();
        mRecyclerView = (RecyclerView) fragRoot.findViewById(R.id.lv);
        mRecyclerView.setAdapter(mListAdapter);
        NetUtils.asyncFetchContacts(getActivity(), new NetUtils.IContactsFetchCallback() {
            @Override
            public void onSuccess(List<PerContactInfo> contacts) {
                mListAdapter.setData(contacts);
            }

            @Override
            public void onFail(String errMsg) {
                //TODO tap to refresh
            }
        });
        return fragRoot;
    }

    private class MainListAdapter extends RecyclerView.Adapter<MainListAdapter.ViewHolder> {
        private List<PerContactInfo> mDataSet;

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
            private MainListItemBinding mRowBinding;
            public ViewHolder(View v) {
                super(v);
                mRowBinding = DataBindingUtil.bind(v);
                v.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                if (mClickHandler != null) {
                    mClickHandler.onItemClicked(this.getPosition());
                }
            }
        }

        public void setData(List<PerContactInfo> dataset) {
            mDataSet = dataset;
            this.notifyDataSetChanged();
        }

        @Override
        public MainListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                             int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.main_list_item, parent, false);
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.mRowBinding.setContact(mDataSet.get(position));
        }

        @Override
        public int getItemCount() {
            if (mDataSet == null) {
                return 0;
            }
            return mDataSet.size();
        }

    }
}
