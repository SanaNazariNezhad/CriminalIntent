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
import android.widget.TimePicker;

import org.maktab.criminalintent.R;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

public class TimePickerFragment extends DialogFragment {

    public static final String ARGS_CRIME_TIME = "crimeTime";
    public static final String EXTRA_USER_SELECTED_TIME = "com.example.criminalintent.userSelectedTime";
    public static final String BUNDLE_KEY_CRIME_DATE = "bundle_key_CrimeDate";
    private Date mCrimeDate;

    private TimePicker mTimePicker;
    private Button mButtonOk, mButtonCancel;
    private Calendar mCalendar;

    public TimePickerFragment() {
        // Required empty public constructor
    }

    public static TimePickerFragment newInstance(Date crimeDate) {
        TimePickerFragment fragment = new TimePickerFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARGS_CRIME_TIME, crimeDate);
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
            mCrimeDate = (Date) getArguments().getSerializable(ARGS_CRIME_TIME);
        }
        mCalendar = Calendar.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.costume_timer_picker_fragment, container, false);

        findViews(view);
        initViews();

        listeners();
        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        extractTimeFromTimePicker();
        outState.putSerializable(BUNDLE_KEY_CRIME_DATE, mCalendar);
    }

    private void listeners() {
        mButtonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                extractTimeFromTimePicker();
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

    /*@NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.fragment_time_picker, null);

        findViews(view);
        initViews();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.time_picker_title)
                .setIcon(R.drawable.ic_clock)
                .setView(view)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        extractTimeFromTimePicker();
                        sendResult(mCalendar);

                    }
                })
                .setNegativeButton(android.R.string.cancel, null);

        AlertDialog dialog = builder.create();
        return dialog;
    }*/

    private void findViews(View view) {
        mTimePicker = view.findViewById(R.id.time_picker_crime);
        mButtonOk = view.findViewById(R.id.btn_ok_time);
        mButtonCancel = view.findViewById(R.id.btn_cancel_time);
    }

    private void initViews() {
        initTimePicker();

    }

    private void initTimePicker() {
        // i have a date and i want to set it in date picker.

        mCalendar.setTime(mCrimeDate);
        int hour = mCalendar.get(Calendar.HOUR);
        int minute = mCalendar.get(Calendar.MINUTE);
        mTimePicker.setHour(hour);
        mTimePicker.setMinute(minute);


    }

    private void extractTimeFromTimePicker() {
        LocalDateTime now = LocalDateTime.now();
        int hour = mTimePicker.getHour();
        int minute = mTimePicker.getMinute();
        int second = now.getSecond();

        mCalendar.set(Calendar.HOUR, hour);
        mCalendar.set(Calendar.MINUTE, minute);
        mCalendar.set(Calendar.SECOND, second);
    }

    private void sendResult(Calendar userSelectedDate) {
        Fragment fragment = getTargetFragment();

        int requestCode = getTargetRequestCode();
        int resultCode = Activity.RESULT_OK;
        Intent intent = new Intent();
        intent.putExtra(EXTRA_USER_SELECTED_TIME, userSelectedDate);

        fragment.onActivityResult(requestCode, resultCode, intent);
    }
}