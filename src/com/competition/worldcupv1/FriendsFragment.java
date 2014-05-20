package com.competition.worldcupv1;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.annotation.SuppressLint;
import android.app.Fragment;

@SuppressLint("NewApi")
public class FriendsFragment extends Fragment {
	public FriendsFragment() {
		
	}
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_friends, container, false);
         
        return rootView;
    }
}
