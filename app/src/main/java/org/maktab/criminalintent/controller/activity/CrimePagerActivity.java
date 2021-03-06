package org.maktab.criminalintent.controller.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import org.maktab.criminalintent.R;
import org.maktab.criminalintent.controller.fragment.CrimeDetailFragment;
import org.maktab.criminalintent.model.Crime;
import org.maktab.criminalintent.repository.CrimeDBRepository;
import org.maktab.criminalintent.repository.IRepository;

import java.util.List;
import java.util.UUID;

public class CrimePagerActivity extends AppCompatActivity implements
        CrimeDetailFragment.Callbacks{

    public static final String EXTRA_CRIME_ID = "com.example.criminalintent.crimeId";
    public static final String TAG = "CPA";
    public static int CURRENT_INDEX;
    private static final String EXTRA_Username = "username";
    private IRepository mRepository;
    private UUID mCrimeId;
    private String mUsername;
    private ViewPager2 mViewPagerCrimes;

    public static Intent newIntent(Context context, UUID crimeId, String username) {
        Intent intent = new Intent(context, CrimePagerActivity.class);
        intent.putExtra(EXTRA_CRIME_ID, crimeId);
        intent.putExtra(EXTRA_Username, username);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime_pager);
        mRepository = CrimeDBRepository.getInstance(this);
        mCrimeId = (UUID) getIntent().getSerializableExtra(EXTRA_CRIME_ID);
        mUsername = getIntent().getStringExtra(EXTRA_Username);
        findViews();
        initViews();
//        circleViewPager();

    }

    private void circleViewPager() {
        mViewPagerCrimes.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            int currentPage;

            @Override
            public void onPageSelected(int position) {
                currentPage = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == ViewPager2.SCROLL_STATE_IDLE) {
                    List<Crime> crimes = mRepository.getCrimes();
                    int pageCount = crimes.size();

                    if (currentPage == 0) {
                        mViewPagerCrimes.setCurrentItem(pageCount - 2, false);
                    } else if (currentPage == pageCount - 1) {
                        mViewPagerCrimes.setCurrentItem(1, false);
                    }
                }
            }
        });
    }

    private void findViews() {
        mViewPagerCrimes = findViewById(R.id.view_pager_crimes);
    }

    private void initViews() {
        List<Crime> crimes = mRepository.getCrimes();
        CrimePagerAdapter adapter = new CrimePagerAdapter(this, crimes);
        mViewPagerCrimes.setAdapter(adapter);
        CURRENT_INDEX = mRepository.getPosition(mCrimeId);
        mViewPagerCrimes.setCurrentItem(CURRENT_INDEX);
        mViewPagerCrimes.setPageTransformer(new ZoomOutPageTransformer());
    }

    @Override
    public void onCrimeUpdated(Crime crime) {
        //nothing
    }

    public class ZoomOutPageTransformer implements ViewPager2.PageTransformer {
        private static final float MIN_SCALE = 0.85f;
        private static final float MIN_ALPHA = 0.5f;

        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();
            int pageHeight = view.getHeight();

            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setAlpha(0f);

            } else if (position <= 1) { // [-1,1]
                // Modify the default slide transition to shrink the page as well
                float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
                float vertMargin = pageHeight * (1 - scaleFactor) / 2;
                float horzMargin = pageWidth * (1 - scaleFactor) / 2;
                if (position < 0) {
                    view.setTranslationX(horzMargin - vertMargin / 2);
                } else {
                    view.setTranslationX(-horzMargin + vertMargin / 2);
                }

                // Scale the page down (between MIN_SCALE and 1)
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);

                // Fade the page relative to its size.
                view.setAlpha(MIN_ALPHA +
                        (scaleFactor - MIN_SCALE) /
                                (1 - MIN_SCALE) * (1 - MIN_ALPHA));

            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setAlpha(0f);
            }
        }
    }


    private class CrimePagerAdapter extends FragmentStateAdapter {

        private List<Crime> mCrimes;

        public List<Crime> getCrimes() {
            return mCrimes;
        }

        public void setCrimes(List<Crime> crimes) {
            mCrimes = crimes;
        }

        public CrimePagerAdapter(@NonNull FragmentActivity fragmentActivity, List<Crime> crimes) {
            super(fragmentActivity);
            mCrimes = crimes;

        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            Log.d(TAG, "position: " + (position + 1));
            Crime crime = mCrimes.get(position);
            CrimeDetailFragment crimeDetailFragment =
                    CrimeDetailFragment.newInstance(crime.getId(), mUsername);

            return crimeDetailFragment;
        }

        @Override
        public int getItemCount() {
            return mCrimes.size();
        }
    }

}