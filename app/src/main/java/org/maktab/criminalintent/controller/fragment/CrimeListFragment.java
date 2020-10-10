package org.maktab.criminalintent.controller.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import org.maktab.criminalintent.R;
import org.maktab.criminalintent.controller.activity.CrimePagerActivity;
import org.maktab.criminalintent.model.Crime;
import org.maktab.criminalintent.repository.CrimeDBRepository;
import org.maktab.criminalintent.repository.IRepository;
import java.util.List;

public class CrimeListFragment extends Fragment {

    private static final String ARG_Username = "username";
    public static final String BUNDLE_ARG_IS_SUBTITLE_VISIBLE = "isSubtitleVisible";

    private RecyclerView mRecyclerView;
    private IRepository mRepository;
    private List<Crime> mCrimes;
    private CrimeAdapter mCrimeAdapter;
    private boolean mIsSubtitleVisible = true;
    private LinearLayout mLinearLayoutEmpty;
    private LinearLayout mLinearLayoutRecycler;
    private Button mButtonNewCrime;
    private String mUsername;

    private Callbacks mCallbacks;

    public static CrimeListFragment newInstance(String username) {
        Bundle args = new Bundle();
        args.putString(ARG_Username,username);
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
        mRepository = CrimeDBRepository.getInstance(getActivity());
        setHasOptionsMenu(true);
        if (savedInstanceState != null)
            mIsSubtitleVisible =
                    savedInstanceState.getBoolean(BUNDLE_ARG_IS_SUBTITLE_VISIBLE, true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_crime_list, container, false);
        findViews(view);
        initViews();
        listeners();
        return view;
    }

    private void listeners() {
        mButtonNewCrime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Crime crime = new Crime();
                mRepository.insertCrime(crime);
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
                mRepository.insertCrime(crime);
                mCallbacks.onCrimeSelected(crime);
                updateUI();
                return true;

            case R.id.menu_item_subtitle:
                mIsSubtitleVisible = !mIsSubtitleVisible;
                updateSubtitle();
                setMenuItemSubtitle(item);
                return true;

            case R.id.menu_item_remove_crime:
                for (int i = 0; i <mCrimes.size() ; i++) {
                    if (mCrimes.get(i).isCheck_Select()) {
                        mRepository.deleteCrime(mCrimes.get(i));
                        i-=1;
                    }
                }
                updateUI();
                return true;

            case R.id.menu_item_selectAll_crime:
                mRepository.setCrimesSelected();
                updateUI();
                return true;

            case R.id.menu_item_unSelectAll_crime:
                mRepository.setCrimesUnSelected();
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

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateUI();
    }

    public void updateUI() {
        mCrimes = mRepository.getCrimes();
        if (mCrimes.size() != 0){
            mLinearLayoutEmpty.setVisibility(View.GONE);
            mLinearLayoutRecycler.setVisibility(View.VISIBLE);
            if (mCrimeAdapter == null) {
                mCrimeAdapter = new CrimeAdapter(mCrimes);
                mRecyclerView.setAdapter(mCrimeAdapter);
            }else {
                mCrimeAdapter.setCrimes(mCrimes);
                mCrimeAdapter.notifyDataSetChanged();
            }
        }
        else {
            mLinearLayoutEmpty.setVisibility(View.VISIBLE);
            mLinearLayoutRecycler.setVisibility(View.GONE);

        }

    }

    private void findViews(View view) {
        mRecyclerView = view.findViewById(R.id.recycler_view_crime_list);
        mLinearLayoutEmpty = view.findViewById(R.id.empty_layout);
        mLinearLayoutRecycler = view.findViewById(R.id.recycler_layout);
        mButtonNewCrime = view.findViewById(R.id.btn_newCrime);
    }

    private class CrimeHolder extends RecyclerView.ViewHolder {

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
                    /*Intent intent = CrimePagerActivity.newIntent(getActivity(), mCrime.getId(),mUsername);
                    startActivityForResult(intent,0);*/
                    mCallbacks.onCrimeSelected(mCrime);

                }
            });
            mCheckBoxSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    Crime crime = mRepository.getCrime(mCrime.getId());
                    crime.setCheck_Select(isChecked);
                    mRepository.updateCrime(crime);
                }
            });
        }

        public void bindCrime(Crime crime){
            mCrime = crime;
            mTextViewTitle.setText(crime.getTitle());
            mTextViewDate.setText(crime.getDate().toString());
            mImageViewSolved.setVisibility(crime.isSolved() ? View.VISIBLE : View.GONE);
            mCheckBoxSelect.setChecked(crime.isCheck_Select());


        }
    }

    private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder>{

        private List<Crime> mCrimes;

        public List<Crime> getCrimes(){
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
            View view = layoutInflater.inflate(R.layout.crime_row_list,parent,false);
            CrimeHolder crimeHolder = new CrimeHolder(view);
            return crimeHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull CrimeHolder holder, int position) {

            Crime crime = mCrimes.get(position);
            holder.bindCrime(crime);

        }

    }

    public interface Callbacks {
        void onCrimeSelected(Crime crime);
    }
}