package com.example.alvin.simplecontactlist.view;

import android.app.Fragment;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.DataBindingUtil;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.alvin.simplecontactlist.BR;
import com.example.alvin.simplecontactlist.R;
import com.example.alvin.simplecontactlist.control.ITopLevelDelegate;
import com.example.alvin.simplecontactlist.databinding.FragmentMainBinding;
import com.example.alvin.simplecontactlist.databinding.MainListItemBinding;
import com.example.alvin.simplecontactlist.model.ContactsDataManager;
import com.example.alvin.simplecontactlist.model.IDataLoadCallback;
import com.example.alvin.simplecontactlist.model.PerContactInfo;

import java.util.List;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainFrag extends Fragment {
    private MainListAdapter mListAdapter;
    private RecyclerView mRecyclerView;
    private ITopLevelDelegate mClickHandler;
    private DisplayController mDisplayController;

    public MainFrag() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragRoot = inflater.inflate(R.layout.fragment_main, container, false);
        mClickHandler = (ITopLevelDelegate)getActivity();
        setHasOptionsMenu(true);
        ((ITopLevelDelegate)getActivity()).showBackButton(false);
        mDisplayController = new DisplayController();
        FragmentMainBinding binding = FragmentMainBinding.bind(fragRoot);
        binding.setController(mDisplayController);
        mRecyclerView = (RecyclerView) fragRoot.findViewById(R.id.lv);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        mListAdapter = new MainListAdapter();
        mRecyclerView = (RecyclerView) fragRoot.findViewById(R.id.lv);
        mRecyclerView.setAdapter(mListAdapter);
        mRecyclerView.addItemDecoration(new Divider((int) getResources().getDimension(R.dimen.view_divider_height)));
        loadData();
        fragRoot.findViewById(R.id.hint).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mDisplayController.getState() == DisplayController.STAT_ERROR) {
                    mDisplayController.setState(DisplayController.STAT_IN_PROGRESS);
                    loadData();
                }
            }
        });
        return fragRoot;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((ITopLevelDelegate)getActivity()).changeTitle(getResources().getString(R.string.main_title));
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_sort_asc) {
            mListAdapter.setData(ContactsDataManager.getInstance().getContactsList(ContactsDataManager.SORT_ASC));
            return true;
        } else if (id == R.id.action_sort_desc) {
            mListAdapter.setData(ContactsDataManager.getInstance().getContactsList(ContactsDataManager.SORT_DESC));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void loadData() {
        ContactsDataManager.getInstance().loadContacts(new IDataLoadCallback() {
            @Override
            public void onSuccess(List<PerContactInfo> fetched) {
                mListAdapter.setData(ContactsDataManager.getInstance().getContactsList(ContactsDataManager.SORT_NONE));
                mDisplayController.setState(DisplayController.STAT_DISPLAY_LIST);
            }

            @Override
            public void onFail(String errMsg) {
                mDisplayController.setState(DisplayController.STAT_ERROR);
            }
        }, false);
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
                    mClickHandler.displayContact(mDataSet.get(this.getPosition()).id);
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

    public static class DisplayController extends BaseObservable {
        private static int enum_counter = 0;
        public static final int STAT_IN_PROGRESS = enum_counter++;
        public static final int STAT_DISPLAY_LIST = enum_counter++;
        public static final int STAT_ERROR = enum_counter++;
        private int state = STAT_IN_PROGRESS;

        public void setState(int newState) {
            state = newState;
            notifyPropertyChanged(BR.state);
        }

        @Bindable
        public int getState() {
            return state;
        }
    }

    static class Divider extends RecyclerView.ItemDecoration {

        private final int mSpace;

        public Divider(int space) {
            this.mSpace = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                                   RecyclerView.State state) {
            outRect.bottom = mSpace;
        }
    }

}
