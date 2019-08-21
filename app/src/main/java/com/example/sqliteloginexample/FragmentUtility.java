package com.example.sqliteloginexample;

import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.util.List;

public class FragmentUtility {

    private static final String TAG = "FragmentUtility";

    public static Fragment getFragmentByTagName(FragmentManager manager, String fragmentTagName)
    {
        Fragment returnFragment = null;

        List<Fragment> fragmentList = manager.getFragments();

        if(fragmentList != null)
        {
            int size = fragmentList.size();
            for(int i=0; i<size; i++)
            {
                Fragment fragment = fragmentList.get(i);
                if(fragment != null)
                {
                    String fragmentTag = fragment.getTag();

                    if(fragmentTag.equals(fragmentTagName))
                    {
                        returnFragment = fragment;
                    }
                }
            }
        }

        return returnFragment;
    }

    public static void printActivityFragmentList(FragmentManager manager)
    {
        List<Fragment> fragmentList = manager.getFragments();

        if(fragmentList != null)
        {
            int size = fragmentList.size();

            for(int i=0; i<size; i++)
            {
                Fragment fragment = fragmentList.get(i);
                if(fragment != null)
                {
                    String fragmentTag = fragment.getTag();
                    Log.d(TAG, fragmentTag);
                }
            }
            Log.d(TAG, "****************************");
        }

    }
}
