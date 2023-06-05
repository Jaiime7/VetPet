package com.insitutosanjuandelacruz.vetpet.view.ui.medicalHistory;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.insitutosanjuandelacruz.vetpet.databinding.FragmentHistoryMedicalBinding;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.insitutosanjuandelacruz.vetpet.view.ui.medicalHistory.adapter.MedicalHistoryPagerAdapter;

public class MedicalHistoryFragment extends Fragment {

    private FragmentHistoryMedicalBinding binding;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private MedicalHistoryPagerAdapter adapter;
    private FirebaseFirestore db;
    private CollectionReference appointmentRef;
    private CollectionReference medicalHistoryRef;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        MedicalHistoryViewModel medicalHistoryViewModel = new ViewModelProvider(this).get(MedicalHistoryViewModel.class);

        binding = FragmentHistoryMedicalBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        tabLayout = binding.tabLayout;
        viewPager = binding.viewPager;

        // Inicializar Firestore
        db = FirebaseFirestore.getInstance();
        appointmentRef = db.collection("appointment");
        medicalHistoryRef = db.collection("medicalHistory");

        // Configurar el ViewPager
        setupViewPager();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void setupViewPager() {
        adapter = new MedicalHistoryPagerAdapter(getChildFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }
}
