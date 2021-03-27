package fr.smato.gameproject.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;


import com.google.android.material.tabs.TabLayout;

import fr.smato.gameproject.activities.menu.MainActivity;
import fr.smato.gameproject.R;
import fr.smato.gameproject.adapter.ViewPagerAdapter;
import fr.smato.gameproject.fragments.social.ChatFragment;
import fr.smato.gameproject.fragments.social.UserFragment;
import fr.smato.gameproject.utils.Updater;

public class SocialMainFragment extends Fragment implements Updater {

    private MainActivity context;

    private UserFragment userFragment;
    private ChatFragment chatFragment;

    private ViewPagerAdapter viewPagerAdapter;

    public SocialMainFragment(MainActivity context) {
        this.context = context;
        this.userFragment = new UserFragment();
        this.chatFragment = new ChatFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager(), 1);
        viewPagerAdapter.addFragment(userFragment, getResources().getString(R.string.friends));
        viewPagerAdapter.addFragment(chatFragment, getResources().getString(R.string.clan));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_main_social, container, false);

        TabLayout tabLayout = view.findViewById(R.id.tab_layout);
        ViewPager viewPager = view.findViewById(R.id.view_pager);

        userFragment.refresh();
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        return view;
    }

    @Override
    public void update() {
        userFragment.refresh();
    }
}
