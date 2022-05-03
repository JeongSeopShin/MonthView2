package com.example.monthview;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import java.util.Calendar;

public class WeekViewFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public WeekViewFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static MonthViewFragment newInstance(String param1, String param2) {
        MonthViewFragment fragment = new MonthViewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // 뷰페이저를 선언하여 프래그먼트에 연결시킴
        View rootView = inflater.inflate(R.layout.fragment_week_view, container, false);
        ViewPager2 vpPager = rootView.findViewById(R.id.vpPager2);
        FragmentStateAdapter adapter = new WeekCalendarAdapter(this);
        vpPager.setAdapter(adapter);
        vpPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                // 뷰페이저의 각 페이지별로 년도, 월을 달리하여 액션바에 표시함
                // 모듈러를 사용하여 각각 year와 month를 표현하였음
                int year = Calendar.getInstance().get(Calendar.YEAR);
                int month = Calendar.getInstance().get(Calendar.MONTH)%12;
                int dm = position*7/42;
                int realMonth = (month+dm)%12+1;
                int ym = (month+dm)/12;
                ActionBar ab = ((MainActivity)getActivity()).getSupportActionBar();
                ab.setTitle((year+ym) + "년 " + realMonth + "월");
            }
        });
        return rootView;
    }
}