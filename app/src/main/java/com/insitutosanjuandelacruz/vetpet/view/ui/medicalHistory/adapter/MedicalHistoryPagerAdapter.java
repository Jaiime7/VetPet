package com.insitutosanjuandelacruz.vetpet.view.ui.medicalHistory.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class MedicalHistoryPagerAdapter extends FragmentPagerAdapter {
    private int numOfTabs;

    public MedicalHistoryPagerAdapter(@NonNull FragmentManager fm, int numOfTabs) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.numOfTabs = numOfTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new UpcomingAppointmentsFragment();
            case 1:
                return new PastAppointmentsFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}
