package com.votingmachine.votingmachine20;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class MyTabAdapter extends FragmentStateAdapter {

    private int totalTabs;
    private Context context;


    public MyTabAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, int totalTabs,Context context) {
        super(fragmentManager, lifecycle);
        this.totalTabs = totalTabs;
        this.context = context;
    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {

        switch (position) {
            case 0:
                Login login_object = new Login((LoginSucessClickListener) context);
                return login_object;
            case 1:
                Register register_object = new Register();
                return register_object;

            default:
                return null;
        }
    }

    @Override
    public int getItemCount() {
        return totalTabs;
    }
}
