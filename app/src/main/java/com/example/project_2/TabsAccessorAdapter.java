package com.example.project_2;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class TabsAccessorAdapter extends FragmentPagerAdapter
{
    Context mContext;


    public TabsAccessorAdapter(@NonNull FragmentManager fm, Context mContext) {
        super(fm);
        this.mContext=mContext;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0:
                ChatsFragment chatsFragment = new ChatsFragment();
                return chatsFragment;

            case 1:
                GroupsFragment groupsFragment = new GroupsFragment();
                return groupsFragment;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public String getPageTitle(int position) {
        switch (position)
        {
            case 0:
                return mContext.getResources().getString(R.string.chats);

            case 1:
                return mContext.getResources().getString(R.string.herbalists);

            default:
                return null;
        }
    }
}
