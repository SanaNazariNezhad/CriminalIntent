package org.maktab.criminalintent.controller.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.maktab.criminalintent.R;
import org.maktab.criminalintent.controller.activity.CrimeDetailActivity;
import org.maktab.criminalintent.controller.activity.CrimePagerActivity;
import org.maktab.criminalintent.model.Crime;
import org.maktab.criminalintent.repository.CrimeRepository;
import org.maktab.criminalintent.repository.IRepository;

import java.util.List;

public class CrimeListFragment extends Fragment {

    public static final String TAG = "CLF";

    private RecyclerView mRecyclerView;

    private IRepository mRepository;
    private CrimeAdapter mCrimeAdapter;
    private int mPosition;

    public static CrimeListFragment newInstance() {

        Bundle args = new Bundle();

        CrimeListFragment fragment = new CrimeListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public CrimeListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mRepository = CrimeRepository.getInstance();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_crime_list, container, false);

        findViews(view);
        initViews();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        updateUI();
    }

    private void initViews() {

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateUI();
    }

    private void updateUI() {
        List<Crime> crimes = mRepository.getCrimes();

        if (mCrimeAdapter == null) {
           mCrimeAdapter = new CrimeAdapter(crimes);
           mRecyclerView.setAdapter(mCrimeAdapter);
        }else {
            mCrimeAdapter.notifyItemChanged(mPosition);
            /*mCrimeAdapter.notifyDataSetChanged();*/
        }
    }

    private void findViews(View view) {
        mRecyclerView = view.findViewById(R.id.recycler_view_crime_list);
    }

    private class CrimeHolder extends RecyclerView.ViewHolder {

        private TextView mTextViewTitle;
        private TextView mTextViewDate;
        private Crime mCrime;
        private ImageView mImageViewSolved;


        public CrimeHolder(@NonNull View itemView) {
            super(itemView);

            mTextViewTitle = itemView.findViewById(R.id.row_item_crime_title);
            mTextViewDate = itemView.findViewById(R.id.row_item_cime_date);
            mImageViewSolved = itemView.findViewById(R.id.imgview_solved);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Intent intent = CrimeDetailActivity.newIntent(getActivity(),mCrime.getId());
                    Intent intent = CrimePagerActivity.newIntent(getActivity(), mCrime.getId());
                    startActivity(intent);

                    /*Intent intent = new Intent(getActivity(), CrimeDetailActivity.class);
                    intent.putExtra(EXTRA_CRIME_ID,mCrime.getId());*/

//                    Toast.makeText(getActivity(),mCrime.getTitle() + " is Clicked",Toast.LENGTH_SHORT).show();
                }
            });
        }

        public void bindCrime(Crime crime){
            mCrime = crime;
            mTextViewTitle.setText(crime.getTitle());
            mTextViewDate.setText(crime.getDate().toString());
            mImageViewSolved.setVisibility(crime.isSolved() ? View.VISIBLE : View.GONE);


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

            mPosition = position;
            Crime crime = mCrimes.get(position);

            holder.bindCrime(crime);
        }

    }
}