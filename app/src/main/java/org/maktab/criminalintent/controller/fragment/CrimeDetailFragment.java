package org.maktab.criminalintent.controller.fragment;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ShareCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import org.maktab.criminalintent.R;
import org.maktab.criminalintent.controller.activity.CrimeListActivity;
import org.maktab.criminalintent.controller.activity.CrimePagerActivity;
import org.maktab.criminalintent.model.Crime;
import org.maktab.criminalintent.repository.CrimeDBRepository;
import org.maktab.criminalintent.repository.IRepository;
import com.example.criminalintent.utils.PictureUtils;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class CrimeDetailFragment extends Fragment {

    public static final String ARGUMENT_CRIME_ID = "crimeId";
    public static final String ARGUMENT_USERNAME = "username";
    public static final String FRAGMENT_TAG_DATE_PICKER = "DatePicker";
    public static final String FRAGMENT_TAG_TIME_PICKER = "TimePicker";
    public static final String AUTHORITY = "org.maktab.criminalintent.fileProvider";
    public static final int REQUEST_CODE_DATE_PICKER = 0;
    public static final int REQUEST_CODE_TIME_PICKER = 1;
    private static final int REQUEST_SELECT_PHONE_NUMBER = 2;
    private static final int REQUEST_CODE_IMAGE_CAPTURE = 3;
    public static final String TAG = "CDF";
    public static final String BUNDLE_KEY_DATE = "Date";
    public static final String BUNDLE_KEY_TIME = "Time";

    private EditText mEditTextTitle;
    private Button mButtonDate;
    private Button mButtonTime;
    private Button mButtonSuspect;
    private Button mButtonReport;
    private Button mButtonCall;
    private Button mButtonDial;
    private ImageButton mImageButtonTakePicture;
    private ImageView mImageViewPhoto;
    private ImageView mImageViewNext, mImageViewPerv, mImageViewFirst, mImageViewLast;
    private CheckBox mCheckBoxSolved;
    private IRepository mRepository;
    private Crime mCrime;
    private File mPhotoFile;
    private String mUsername;
    private String mDate, mTime;
    private boolean flag = false;
    private boolean mFlagRemove = false;
    private int mCurrentIndex;

    private Callbacks mCallbacks;


    public static CrimeDetailFragment newInstance(UUID crimeId, String username) {

        Bundle args = new Bundle();
        args.putSerializable(ARGUMENT_CRIME_ID, crimeId);
        args.putString(ARGUMENT_USERNAME, username);
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

        if (context instanceof Callbacks)
            mCallbacks = (Callbacks) context;
        else {
            throw new ClassCastException(context.toString()
                    + " must implement Callbacks");
        }
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
        mRepository = CrimeDBRepository.getInstance(getActivity());
        UUID crimeId = (UUID) getArguments().getSerializable(ARGUMENT_CRIME_ID);
        mUsername = getArguments().getString(ARGUMENT_USERNAME);
        mCrime = mRepository.getCrime(crimeId);
        mPhotoFile = mRepository.getPhotoFile(mCrime);
        setHasOptionsMenu(true);

    }

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
        updatePhotoView();
        setListeners();
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        updateCrime();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_crime_detail, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_delete_crime:
                mRepository.deleteCrime(mCrime);
                mFlagRemove = true;
                Intent intent = CrimeListActivity.newIntent(getActivity(), mUsername);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
        if (resultCode != Activity.RESULT_OK || intent == null)
            return;
        if (requestCode == REQUEST_CODE_DATE_PICKER) {
            Calendar userSelectedDate =
                    (Calendar) intent.getSerializableExtra(DatePickerFragment.EXTRA_USER_SELECTED_DATE);
            updateCrimeDate(userSelectedDate.getTime());
        } else if (requestCode == REQUEST_CODE_TIME_PICKER) {
            Calendar userSelectedTime =
                    (Calendar) intent.getSerializableExtra(TimePickerFragment.EXTRA_USER_SELECTED_TIME);
            updateCrimeTime(userSelectedTime.getTime());
        }
        else if (requestCode == REQUEST_SELECT_PHONE_NUMBER){
            // Get the URI and query the content provider for the phone number
            handleSelectedContactNumber(intent);
        }else if (requestCode == REQUEST_CODE_IMAGE_CAPTURE) {
            Uri photoUri = generateUriForPhotoFile();
            getActivity().revokeUriPermission(photoUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

            updatePhotoView();
        }
    }

    private void handleSelectedContactNumber(Intent intent) {
        Uri contactUri = intent.getData();
        String[] projection = new String[]{ContactsContract.Contacts.DISPLAY_NAME,ContactsContract.CommonDataKinds.Phone.NUMBER};
        Cursor cursor = getActivity().getContentResolver().query(contactUri, projection,
                null, null, null);
        // If the cursor returned is valid, get the phone number
        if (cursor == null || cursor.getCount() == 0)
            return;

        try {
            cursor.moveToFirst();
            int numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            String number = cursor.getString(numberIndex);
            mCrime.setSuspectPhoneNumber(number);
            String dial = getString(R.string.dial_to, mCrime.getSuspectPhoneNumber() + "");
            String call = getString(R.string.call_to, mCrime.getSuspectPhoneNumber() + "");
            mButtonCall.setText(call);
            mButtonDial.setText(dial);
            String suspect = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            mCrime.setSuspect(suspect);
            mButtonSuspect.setText(suspect);
        }finally {
            cursor.close();
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
        mButtonSuspect = view.findViewById(R.id.choose_suspect);
        mButtonReport = view.findViewById(R.id.send_report);
        mButtonCall = view.findViewById(R.id.call_btn);
        mButtonDial = view.findViewById(R.id.dial_btn);
        mImageViewPhoto = view.findViewById(R.id.imgview_photo);
        mImageButtonTakePicture = view.findViewById(R.id.imgbtn_take_picture);
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

        if (mCrime.getSuspect() != null) {
            mButtonSuspect.setText(mCrime.getSuspect());
            String dial = getString(R.string.dial_to, mCrime.getSuspectPhoneNumber() + "");
            String call = getString(R.string.call_to, mCrime.getSuspectPhoneNumber() + "");
            mButtonCall.setText(call);
            mButtonDial.setText(dial);
        }
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
                updateCrime();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mCheckBoxSolved.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                mCrime.setSolved(isChecked);
                updateCrime();
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
                Intent crimePagerActivityIntent = CrimePagerActivity.newIntent(getActivity(),mCrime.getId(),mUsername);
                startActivity(crimePagerActivityIntent);
//                initViews();
            }
        });
        mImageViewPerv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentIndex = mRepository.getIndexOfCrime(mCrime);
                mCurrentIndex = (mCurrentIndex - 1 + mRepository.repositorySize()) % mRepository.repositorySize();
                mCrime = mRepository.getCrimeWithIndex(mCurrentIndex);
                Intent crimePagerActivityIntent = CrimePagerActivity.newIntent(getActivity(),mCrime.getId(),mUsername);
                startActivity(crimePagerActivityIntent);
//                initViews();
            }
        });
        mImageViewFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentIndex = 0;
                mCrime = mRepository.getCrimeWithIndex(mCurrentIndex);
                Intent crimePagerActivityIntent = CrimePagerActivity.newIntent(getActivity(),mCrime.getId(),mUsername);
                startActivity(crimePagerActivityIntent);
//                initViews();
            }
        });
        mImageViewLast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentIndex = mRepository.repositorySize() - 1;
                mCrime = mRepository.getCrimeWithIndex(mCurrentIndex);
                Intent crimePagerActivityIntent = CrimePagerActivity.newIntent(getActivity(),mCrime.getId(),mUsername);
                startActivity(crimePagerActivityIntent);
//                initViews();
            }
        });
        mButtonSuspect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectContact();
            }
        });

        mButtonReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareReportIntent();
            }
        });
        mButtonCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:"+mCrime.getSuspectPhoneNumber()));
                startActivity(callIntent);
            }
        });
        mButtonDial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent dialIntent = new Intent(Intent.ACTION_DIAL);
                dialIntent.setData(Uri.parse("tel:"+mCrime.getSuspectPhoneNumber()));
                startActivity(dialIntent);
            }
        });

        mImageButtonTakePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePictureIntent();
            }
        });
    }

    private void updateCrime() {
        if (!mFlagRemove) {
            mRepository.updateCrime(mCrime);

            //todo: anti pattern
            mCallbacks.onCrimeUpdated(mCrime);
        }
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

    private DateFormat getDateFormat() {
        return new SimpleDateFormat("yyyy/MM/dd");
    }

    private DateFormat getTimeFormat() {
        return new SimpleDateFormat("HH:mm:ss");
    }

    private String getReport() {
        String title = mCrime.getTitle();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd-HH:mm:SS");
        String dateString = simpleDateFormat.format(mCrime.getDate());

        String solvedString = mCrime.isSolved() ?
                getString(R.string.crime_report_solved) :
                getString(R.string.crime_report_unsolved);

        String suspectString = mCrime.getSuspect() == null ?
                getString(R.string.crime_report_no_suspect) :
                getString(R.string.crime_report_suspect, mCrime.getSuspect());

        String report = getString(
                R.string.crime_report,
                title,
                dateString,
                solvedString,
                suspectString);

        return report;
    }

    private void shareReportIntent() {

        ShareCompat.IntentBuilder intentBuilder = ShareCompat.IntentBuilder.from(getActivity());
        Intent intent = intentBuilder
                .setType("text/plain")
                .setText(getReport())
                .setChooserTitle(getString(R.string.crime_report_subject))
                .createChooserIntent();

        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    private void takePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            if (mPhotoFile != null && takePictureIntent
                    .resolveActivity(getActivity().getPackageManager()) != null) {

                // file:///data/data/com.example.ci/files/234234234234.jpg
                Uri photoUri = generateUriForPhotoFile();

                grantWriteUriToAllResolvedActivities(takePictureIntent, photoUri);

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(takePictureIntent, REQUEST_CODE_IMAGE_CAPTURE);
            }
        } catch (ActivityNotFoundException e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    private void grantWriteUriToAllResolvedActivities(Intent takePictureIntent, Uri photoUri) {
        List<ResolveInfo> activities = getActivity().getPackageManager()
                .queryIntentActivities(
                        takePictureIntent,
                        PackageManager.MATCH_DEFAULT_ONLY);

        for (ResolveInfo activity: activities) {
            getActivity().grantUriPermission(
                    activity.activityInfo.packageName,
                    photoUri,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        }
    }

    private Uri generateUriForPhotoFile() {
        return FileProvider.getUriForFile(
                getContext(),
                AUTHORITY,
                mPhotoFile);
    }


    private void selectContact() {

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_SELECT_PHONE_NUMBER);
        }
    }

    private void updatePhotoView() {
        if (mPhotoFile == null || !mPhotoFile.exists())
            return;

//        Bitmap bitmap = BitmapFactory.decodeFile(mPhotoFile.getAbsolutePath());

        //this has a better memory management.
        Bitmap bitmap = com.example.criminalintent.utils.PictureUtils.getScaledBitmap(mPhotoFile.getAbsolutePath(), getActivity());
        mImageViewPhoto.setImageBitmap(bitmap);
    }

    public interface Callbacks {
        void onCrimeUpdated(Crime crime);
    }
}