package org.maktab.criminalintent.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import org.maktab.criminalintent.R;
import org.maktab.criminalintent.databinding.CrimeRowListBinding;
import org.maktab.criminalintent.model.Crime;
import org.maktab.criminalintent.view.fragment.CrimeListFragment;
import org.maktab.criminalintent.viewmodel.CrimeListViewModel;
import org.maktab.criminalintent.viewmodel.CrimeViewModel;

import java.util.List;

public class CrimeAdapter extends RecyclerView.Adapter<CrimeAdapter.CrimeHolder> {

    private List<Crime> mCrimes;
    private Context mContext;
    private CrimeListViewModel mCrimeListViewModel;

    public List<Crime> getCrimes() {
        return mCrimes;
    }

    public void setCrimes(List<Crime> crimes) {
        mCrimes = crimes;
    }

    public CrimeAdapter(Context context,List<Crime> crimes,CrimeListFragment.Callbacks callbacks) {
        mContext = context;
        mCrimes = crimes;
        mCrimeListViewModel = new CrimeListViewModel(context);
    }

    @Override
    public int getItemCount() {
        return mCrimes.size();
    }

    @NonNull
    @Override
    public CrimeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

//        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        CrimeRowListBinding crimeRowListBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext),
                R.layout.crime_row_list, parent, false  );
//        View view = layoutInflater.inflate(R.layout.crime_row_list, parent, false);
        CrimeHolder crimeHolder = new CrimeHolder(crimeRowListBinding);
        return crimeHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CrimeHolder holder, int position) {

        Crime crime = mCrimes.get(position);
        holder.bindCrime(crime);
    }

    class CrimeHolder extends RecyclerView.ViewHolder {
/*
        private TextView mTextViewTitle;
        private TextView mTextViewDate;
        private Crime mCrime;
        private ImageView mImageViewSolved;*/
        private CrimeRowListBinding mRowListBinding;
      /*  private CheckBox mCheckBoxSelect;*/

        public CrimeHolder(CrimeRowListBinding crimeRowListBinding) {
            super(crimeRowListBinding.getRoot());

            mRowListBinding = crimeRowListBinding;
            mRowListBinding.setCrimeViewModel(new CrimeViewModel());

       /*     mTextViewTitle = itemView.findViewById(R.id.row_item_crime_title);
            mTextViewDate = itemView.findViewById(R.id.row_item_crime_date);
            mImageViewSolved = itemView.findViewById(R.id.imgview_solved);*/
//            mCheckBoxSelect = itemView.findViewById(R.id.row_item_crime_checkBox);

/*
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mCallbacks.onCrimeSelected(mCrime);

                }
            });
            mCheckBoxSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    Crime crime = mCrimeListViewModel.getCrime(mCrime.getId());
                    crime.setCheck_Select(isChecked ? 1 : 0);
                    mCrimeListViewModel.updateCrime(crime);
                }
            });*/
        }

        public void bindCrime(Crime crime) {
            mRowListBinding.getCrimeViewModel().setCrime(crime,mContext);
            mRowListBinding.executePendingBindings();
           /* mCrime = crime;
            mTextViewTitle.setText(crime.getTitle());
            mTextViewDate.setText(crime.getDate().toString());
            mImageViewSolved.setVisibility(crime.isSolved() ? View.VISIBLE : View.GONE);*/
            mRowListBinding.imgviewSolved.setVisibility(crime.isSolved() ? View.VISIBLE : View.GONE);
            mRowListBinding.rowItemCrimeCheckBox.setChecked(crime.getCheck_Select() == 1);

        }
    }
}
