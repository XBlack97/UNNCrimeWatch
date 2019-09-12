package com.x.unncrimewatch.views;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.x.unncrimewatch.R;
import com.x.unncrimewatch.adapter.CWRecyclerViewAdapter;
import com.x.unncrimewatch.model.CWViewModel;
import com.x.unncrimewatch.roomDB.CW;

import java.util.ArrayList;
import java.util.List;

public class CWListFragment extends Fragment {

    private ArrayList<CW> Updates = new ArrayList<CW>();

    private CWRecyclerViewAdapter mUpdateAdapter = new CWRecyclerViewAdapter(Updates);

    private RecyclerView mRecyclerView;

    protected CWViewModel cwViewModel;

    private SwipeRefreshLayout mSwipeToRefreshView;

    private OnListFragmentInteractionListener mListener;

    public interface OnListFragmentInteractionListener {
        void onListFragmentRefreshRequested();
    }

    public CWListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActivity().setTitle("News");
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_update_list,
                container, false);
        mRecyclerView = view.findViewById(R.id.list);
        mSwipeToRefreshView = view.findViewById(R.id.swiperefresh);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Set the Recycler View adapter
        Context context = view.getContext();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mUpdateAdapter);

        // Setup the Swipe to Refresh view
        mSwipeToRefreshView.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        refreshUpdates();
                    }
                });
    }

    protected void refreshUpdates() {
        if (mListener != null)
            mListener.onListFragmentRefreshRequested();

        mSwipeToRefreshView.setRefreshing(false);
    }


    public void setUpdates(List<CW> updates) {

        Updates.clear();
        mUpdateAdapter.notifyDataSetChanged();

        for (CW update : updates) {
            if (!Updates.contains(update)) {
                Updates.add(update);
                mUpdateAdapter.notifyItemInserted(Updates.indexOf(update));
            }
        }

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Retrieve the Earthquake View Model for the parent Activity.
        cwViewModel = ViewModelProviders.of(getActivity())
                .get(CWViewModel.class);

        // Get the data from the View Model, and observe any changes.
        cwViewModel.getUpdates()
                .observe(this, new Observer<List<CW>>() {
                    @Override
                    public void onChanged(@Nullable List<CW> updates) {
                        // When the View Model changes, update the List
                        if (updates != null)
                            setUpdates(updates);
                    }
                });

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mListener = (OnListFragmentInteractionListener) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

}
