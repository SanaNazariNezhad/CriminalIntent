package org.maktab.criminalintent.controller.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;

import org.maktab.criminalintent.R;
import org.maktab.criminalintent.controller.activity.CrimeDetailActivity;
import org.maktab.criminalintent.controller.activity.CrimePagerActivity;
import org.maktab.criminalintent.model.Crime;
import org.maktab.criminalintent.repository.CrimeRepository;
import org.maktab.criminalintent.repository.IRepository;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class CrimeDetailFragment extends Fragment {

    public static final String ARGUMENT_CRIME_ID = "crimeId";
    public static final String FRAGMENT_TAG_DATE_PICKER = "DatePicker";
    public static final String FRAGMENT_TAG_TIME_PICKER = "TimePicker";
    public static final int REQUEST_CODE_DATE_PICKER = 0;
    public static final int REQUEST_CODE_TIME_PICKER = 1;
    public static final String TAG = "CDF";
    public static final String BUNDLE_KEY_DATE = "Date";
    public static final String BUNDLE_KEY_TIME = "Time";

    private EditText mEditTextTitle;
    private Button mButtonDate;
    private Button mButtonTime;
    private CheckBox mCheckBoxSolved;
    private IRepository mRepository;
    private Crime mCrime;

    private String mDate, mTime;
    private boolean flag = false;

    private ImageView mImageViewNext, mImageViewPerv, mImageViewFirst, mImageViewLast;
    private int mCurrentIndex;


    public static CrimeDetailFragment newInstance(UUID crimeId) {

        Bundle args = new Bundle();

        args.putSerializable(ARGUMENT_CRIME_ID, crimeId);

        CrimeDetailFragment fragment = new CrimeDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public CrimeDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        Log.d(TAG, "onAttach");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mDate = savedInstanceState.getString(BUNDLE_KEY_DATE);
            mTime = savedInstanceState.getString(BUNDLE_KEY_TIME);
            flag = true;
        }

        Log.d(TAG, "onCreate");

        mRepository = CrimeRepository.getInstance();

        UUID crimeId = (UUID) getArguments().getSerializable(ARGUMENT_CRIME_ID);
        mCrime = mRepository.getCrime(crimeId);

    }

    /**
     * 1. Inflate the layout (or create layout in code)
     * 2. find all views
     * 3. logic for all views (like setListeners)
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_crime_detail, container, false);

        findViews(view);
        if (flag) {
            mButtonDate.setText(mDate);
            mButtonTime.setText(mTime);
        }
        initViews();
        setListeners();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        Log.d(TAG, "onStart");
    }

    @Override
    public void onResume() {
        super.onResume();

        Log.d(TAG, "onResume");
    }

    @Override
    public void onPause() {
        super.onPause();

        updateCrime();

        Log.d(TAG, "onPause");
    }

    @Override
    public void onStop() {
        super.onStop();

        Log.d(TAG, "onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        Log.d(TAG, "onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.d(TAG, "onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();

        Log.d(TAG, "onDetach");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode != Activity.RESULT_OK || data == null)
            return;

        if (requestCode == REQUEST_CODE_DATE_PICKER) {
            Date userSelectedDate =
                    (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_USER_SELECTED_DATE);

            updateCrimeDate(userSelectedDate);
        } else if (requestCode == REQUEST_CODE_TIME_PICKER) {
            Calendar userSelectedTime =
                    (Calendar) data.getSerializableExtra(TimePickerFragment.EXTRA_USER_SELECTED_TIME);
            updateCrimeTime(userSelectedTime.getTime());
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(BUNDLE_KEY_DATE, mButtonDate.getText().toString());
        outState.putString(BUNDLE_KEY_TIME, mButtonTime.getText().toString());
    }

    private void findViews(View view) {
        mEditTextTitle = view.findViewById(R.id.crime_title);
        mButtonDate = view.findViewById(R.id.crime_date);
        mButtonTime = view.findViewById(R.id.crime_time);
        mCheckBoxSolved = view.findViewById(R.id.crime_solved);
        mImageViewNext = view.findViewById(R.id.imgBtn_next);
        mImageViewPerv = view.findViewById(R.id.imgBtn_Prev);
        mImageViewFirst = view.findViewById(R.id.imgBtn_first);
        mImageViewLast = view.findViewById(R.id.imgBtn_last);
    }

    private void initViews() {
        mEditTextTitle.setText(mCrime.getTitle());
        mCheckBoxSolved.setChecked(mCrime.isSolved());

        if (!flag) {
            DateFormat dateFormat = getDateFormat();
            mButtonDate.setText(dateFormat.format(mCrime.getDate()));
            DateFormat timeFormat = getTimeFormat();
            mButtonTime.setText(timeFormat.format(mCrime.getDate()));
        }
    }

    private DateFormat getDateFormat() {
        return new SimpleDateFormat("yyyy/MM/dd");
    }

    private DateFormat getTimeFormat() {
        return new SimpleDateFormat("HH:mm:ss");
    }

    private void setListeners() {
        mEditTextTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d(TAG, "onTextChanged: " + s + ", " + start + ", " + before + ", " + count);

                mCrime.setTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mCheckBoxSolved.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                mCrime.setSolved(isChecked);
            }
        });

        mButtonDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment datePickerFragment =
                        DatePickerFragment.newInstance(mCrime.getDate());

                //create parent-child relations between CDF and DPF
                datePickerFragment.setTargetFragment(
                        CrimeDetailFragment.this,
                        REQUEST_CODE_DATE_PICKER);

                datePickerFragment.show(
                        getActivity().getSupportFragmentManager(),
                        FRAGMENT_TAG_DATE_PICKER);
            }
        });
        mButtonTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerFragment timePickerFragment = TimePickerFragment.newInstance(mCrime.getDate());

                timePickerFragment.setTargetFragment(CrimeDetailFragment.this,
                        REQUEST_CODE_TIME_PICKER);

                timePickerFragment.show(
                        getActivity().getSupportFragmentManager(),
                        FRAGMENT_TAG_TIME_PICKER);
            }
        });

        mImageViewNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentIndex = mRepository.getIndexOfCrime(mCrime);
                mCurrentIndex = (mCurrentIndex + 1) % mRepository.repositorySize();
                mCrime = mRepository.getCrimeWithIndex(mCurrentIndex);
                initViews();

                /*UUID id = mRepository.nextPosition(mCrime.getId());
                Intent intent = CrimePagerActivity.newIntent(getActivity(), id);
                startActivity(intent);*/
            }
        });
        mImageViewPerv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentIndex = mRepository.getIndexOfCrime(mCrime);
                mCurrentIndex = (mCurrentIndex - 1 + mRepository.repositorySize()) % mRepository.repositorySize();
                mCrime = mRepository.getCrimeWithIndex(mCurrentIndex);
                initViews();

                /*UUID id = mRepository.pervPosition(mCrime.getId());
                Intent intent = CrimePagerActivity.newIntent(getActivity(), id);
                startActivity(intent);*/
            }
        });
        mImageViewFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentIndex = 0;
                mCrime = mRepository.getCrimeWithIndex(mCurrentIndex);
                initViews();
                /*UUID id = mRepository.pervPosition(mCrime.getId());
                Intent intent = CrimePagerActivity.newIntent(getActivity(), id);
                startActivity(intent);*/
            }
        });
        mImageViewLast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentIndex = mRepository.repositorySize() - 1;
                mCrime = mRepository.getCrimeWithIndex(mCurrentIndex);
                initViews();
                /*UUID id = mRepository.pervPosition(mCrime.getId());
                Intent intent = CrimePagerActivity.newIntent(getActivity(), id);
                startActivity(intent);*/
            }
        });
    }

    private void updateCrime() {
        mRepository.updateCrime(mCrime);
    }

    private void updateCrimeDate(Date userSelectedDate) {
        mCrime.setDate(userSelectedDate);
        updateCrime();

        DateFormat dateFormat = getDateFormat();
        mButtonDate.setText(dateFormat.format(mCrime.getDate()));

    }

    private void updateCrimeTime(Date userSelectedTime) {
        mCrime.setDate(userSelectedTime);
        updateCrime();

        DateFormat timeFormat = getTimeFormat();
        mButtonTime.setText(timeFormat.format(mCrime.getDate()));
    }

}