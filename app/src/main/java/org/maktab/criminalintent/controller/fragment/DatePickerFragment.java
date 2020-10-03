package org.maktab.criminalintent.controller.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import org.maktab.criminalintent.R;
import java.util.Calendar;
import java.util.Date;

public class DatePickerFragment extends DialogFragment {

    public static final String ARGS_CRIME_DATE = "crimeDate";
    public static final String EXTRA_USER_SELECTED_DATE = "com.example.criminalintent.userSelectedDate";
    public static final String BUNDLE_KEY_CRIME_DATE = "bundle_key_CrimeDate";
    private Date mCrimeDate;

    private DatePicker mDatePicker;
    private Button mButtonOk, mButtonCancel;
    private Calendar mCalendar;

    public DatePickerFragment() {
        // Required empty public constructor
    }

    public static DatePickerFragment newInstance(Date crimeDate) {
        DatePickerFragment fragment = new DatePickerFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARGS_CRIME_DATE, crimeDate);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            Calendar calendar = (Calendar) savedInstanceState.getSerializable(BUNDLE_KEY_CRIME_DATE);
            mCrimeDate = calendar.getTime();

        } else {
            mCrimeDate = (Date) getArguments().getSerializable(ARGS_CRIME_DATE);
        }
        mCalendar = Calendar.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.costume_date_picker_fragment, container, false);

        findViews(view);
        initViews();

        listeners();
        return view;
    }

    private void listeners() {
        mButtonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                extractDateFromDatePicker();
                sendResult(mCalendar);
                dismiss();
            }
        });
        mButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        extractDateFromDatePicker();
        outState.putSerializable(BUNDLE_KEY_CRIME_DATE, mCalendar);
    }

    private void findViews(View view) {
        mDatePicker = view.findViewById(R.id.date_picker_crime);
        mButtonOk = view.findViewById(R.id.btn_ok_date);
        mButtonCancel = view.findViewById(R.id.btn_cancel_date);
    }

    private void initViews() {
        initDatePicker();
    }

    private void initDatePicker() {
        // i have a date and i want to set it in date picker.
        mCalendar.setTime(mCrimeDate);
        int year = mCalendar.get(Calendar.YEAR);
        int monthOfYear = mCalendar.get(Calendar.MONTH);
        int dayOfMonth = mCalendar.get(Calendar.DAY_OF_MONTH);
        mDatePicker.init(year, monthOfYear, dayOfMonth, null);
    }

    private void extractDateFromDatePicker() {
        int year = mDatePicker.getYear();
        int month = mDatePicker.getMonth();
        int dayOfMonth = mDatePicker.getDayOfMonth();

        mCalendar.set(Calendar.YEAR,year);
        mCalendar.set(Calendar.MONTH,month);
        mCalendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
    }

    private void sendResult(Calendar userSelectedDate) {
        Fragment fragment = getTargetFragment();

        int requestCode = getTargetRequestCode();
        int resultCode = Activity.RESULT_OK;
        Intent intent = new Intent();
        intent.putExtra(EXTRA_USER_SELECTED_DATE, userSelectedDate);

        fragment.onActivityResult(requestCode, resultCode, intent);
    }
}