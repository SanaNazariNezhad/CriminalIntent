package org.maktab.criminalintent.view.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import org.maktab.criminalintent.R;
import org.maktab.criminalintent.adapter.CrimeAdapter;
import org.maktab.criminalintent.databinding.FragmentCrimeListBinding;
import org.maktab.criminalintent.model.Crime;
import org.maktab.criminalintent.repository.CrimeDBRepository;
import org.maktab.criminalintent.repository.IRepository;
import org.maktab.criminalintent.viewmodel.CrimeListViewModel;

import java.util.List;

public class CrimeListFragment extends Fragment {

    private static final String ARG_Username = "username";
    public static final String BUNDLE_ARG_IS_SUBTITLE_VISIBLE = "isSubtitleVisible";

    private FragmentCrimeListBinding mListBinding;
    /*private IRepository mRepository;*/
    private CrimeListViewModel mCrimeListViewModel;
    private List<Crime> mCrimes;
    private CrimeAdapter mCrimeAdapter;
    private boolean mIsSubtitleVisible = true;
    private String mUsername;
    private Callbacks mCallbacks;
    private Crime mCrimeUndo;

    public static CrimeListFragment newInstance(String username) {
        Bundle args = new Bundle();
        args.putString(ARG_Username, username);
        CrimeListFragment fragment = new CrimeListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public CrimeListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof Callbacks)
            mCallbacks = (Callbacks) context;
        else {
            throw new ClassCastException(context.toString()
                    + " must implement Callbacks");
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mUsername = getArguments().getString(ARG_Username);
//        mRepository = CrimeDBRepository.getInstance(getActivity());
        mCrimeListViewModel = new CrimeListViewModel(getContext());
        mCrimeListViewModel.setCrimesUnSelected();
        setHasOptionsMenu(true);
        if (savedInstanceState != null)
            mIsSubtitleVisible =
                    savedInstanceState.getBoolean(BUNDLE_ARG_IS_SUBTITLE_VISIBLE, true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mListBinding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_crime_list,
                container,
                false);
        initViews();
        listeners();
        return mListBinding.getRoot();
    }

    private void listeners() {
        mListBinding.btnNewCrime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Crime crime = new Crime();
                mCrimeListViewModel.insertCrime(crime);
                mCallbacks.onCrimeSelected(crime);
                updateUI();
            }
        });
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_crime_list, menu);
        MenuItem item = menu.findItem(R.id.menu_item_subtitle);
        setMenuItemSubtitle(item);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_add_crime:
                Crime crime = new Crime();
                mCrimeListViewModel.insertCrime(crime);
                mCallbacks.onCrimeSelected(crime);
                updateUI();
                return true;

            case R.id.menu_item_subtitle:
                mIsSubtitleVisible = !mIsSubtitleVisible;
                updateSubtitle();
                setMenuItemSubtitle(item);
                return true;

            case R.id.menu_item_remove_selected_crime:
                mCrimeListViewModel.deleteSelectedCrime();
                updateUI();
                return true;

            case R.id.menu_item_selectAll_crime:
                mCrimeListViewModel.setCrimesSelected();
                updateUI();
                return true;

            case R.id.menu_item_unSelectAll_crime:
                mCrimeListViewModel.setCrimesUnSelected();
                updateUI();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setMenuItemSubtitle(MenuItem item) {
        item.setTitle(
                mIsSubtitleVisible ?
                        R.string.menu_item_hide_subtitle :
                        R.string.menu_item_show_subtitle);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBoolean(BUNDLE_ARG_IS_SUBTITLE_VISIBLE, mIsSubtitleVisible);
    }

    private void updateSubtitle() {
        String crimesText = mIsSubtitleVisible ? mUsername : null;
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setSubtitle(crimesText);
    }

    @Override
    public void onResume() {
        super.onResume();

        updateUI();
        updateSubtitle();
    }

    private void initViews() {
        mListBinding.recyclerViewCrimeList.setLayoutManager(new LinearLayoutManager(getActivity()));
        swipeRecycler();
        updateUI();
    }

    private void swipeRecycler() {
        /*  set swipe touch listener */
        SwipeableRecyclerView swipeTouchListener = new
                SwipeableRecyclerView(mListBinding.recyclerViewCrimeList,
                new SwipeableRecyclerView.SwipeListener() {

                    @Override
                    public boolean canSwipeRight(int position) {
                        //enable/disable right swipe on checkbox base else use true/false
                        return true;
                    }

                    @Override
                    public void onDismissedBySwipeRight(RecyclerView recyclerView, int[] reverseSortedPositions) {
                        //on recycler view swipe right dismiss update adapter
                        onRecyclerViewDismiss(reverseSortedPositions, mCrimes);
                    }
                });

        //add item touch listener to recycler view
        mListBinding.recyclerViewCrimeList.addOnItemTouchListener(swipeTouchListener);
    }

    private void onRecyclerViewDismiss(int[] reverseSortedPositions, List<Crime> crimes) {
        for (int position : reverseSortedPositions) {
            mCrimeUndo = crimes.get(position);
            mCrimeListViewModel.deleteCrime(crimes.get(position));
        }
        updateUI();
        showSnackBar();
    }

    private void showSnackBar() {
        Snackbar snackbar = Snackbar.make(mListBinding.crimeListLayout, R.string.crime_dismiss_success, Snackbar.LENGTH_SHORT);
        snackbar.setAction(R.string.crime_dismiss_undo, new MyUndoListener());
        snackbar.show();
    }

    public void updateUI() {
        mCrimes = mCrimeListViewModel.getCrimes();
        if (mCrimes.size() != 0) {
            mListBinding.emptyLayout.setVisibility(View.GONE);
            mListBinding.recyclerLayout.setVisibility(View.VISIBLE);
            if (mCrimeAdapter == null) {
                mCrimeAdapter = new CrimeAdapter(getContext(),mCrimes,mCallbacks);
                mListBinding.recyclerViewCrimeList.setAdapter(mCrimeAdapter);
            } else {
                mCrimeAdapter.setCrimes(mCrimes);
                mCrimeAdapter.notifyDataSetChanged();
            }
        } else {
            mListBinding.emptyLayout.setVisibility(View.VISIBLE);
            mListBinding.recyclerLayout.setVisibility(View.GONE);

        }

    }

   /* private class CrimeHolder extends RecyclerView.ViewHolder {

        private TextView mTextViewTitle;
        private TextView mTextViewDate;
        private Crime mCrime;
        private ImageView mImageViewSolved;
        private CheckBox mCheckBoxSelect;

        public CrimeHolder(@NonNull View itemView) {
            super(itemView);

            mTextViewTitle = itemView.findViewById(R.id.row_item_crime_title);
            mTextViewDate = itemView.findViewById(R.id.row_item_crime_date);
            mImageViewSolved = itemView.findViewById(R.id.imgview_solved);
            mCheckBoxSelect = itemView.findViewById(R.id.row_item_crime_checkBox);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mCallbacks.onCrimeSelected(mCrime);

                }
            });
            mCheckBoxSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    Crime crime = mRepository.getCrime(mCrime.getId());
                    crime.setCheck_Select(isChecked ? 1 : 0);
                    mRepository.updateCrime(crime);
                }
            });
        }

        public void bindCrime(Crime crime) {
            mCrime = crime;
            mTextViewTitle.setText(crime.getTitle());
            mTextViewDate.setText(crime.getDate().toString());
            mImageViewSolved.setVisibility(crime.isSolved() ? View.VISIBLE : View.GONE);
            mCheckBoxSelect.setChecked(crime.getCheck_Select() == 1);

        }
    }*/

    /*private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder> {

        private List<Crime> mCrimes;

        public List<Crime> getCrimes() {
            return mCrimes;
        }

        public void setCrimes(List<Crime> crimes) {
            mCrimes = crimes;
        }

        public CrimeAdapter(List<Crime> crimes) {
            mCrimes = crimes;
        }

        @Override
        public int getItemCount() {
            return mCrimes.size();
        }

        @NonNull
        @Override
        public CrimeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.crime_row_list, parent, false);
            CrimeHolder crimeHolder = new CrimeHolder(view);
            return crimeHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull CrimeHolder holder, int position) {

            Crime crime = mCrimes.get(position);
            holder.bindCrime(crime);
        }
    }*/

    public interface Callbacks {
        void onCrimeSelected(Crime crime);
    }

    public class MyUndoListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            mCrimeListViewModel.insertCrime(mCrimeUndo);
            updateUI();
        }
    }
}