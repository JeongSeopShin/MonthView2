package com.example.monthview;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Calendar;


public class MonthCalendarFragment extends Fragment {
    Calendar mCal; // 캘린더 선언
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    int year;
    int month;
    // TODO: Rename and change types of parameters
    private int mParam1;
    private int mParam2;
    public MonthCalendarFragment() {
        // Required empty public constructor
    }

    public static MonthCalendarFragment newInstance(int year, int month) {
        MonthCalendarFragment fragment = new MonthCalendarFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, year);
        args.putInt(ARG_PARAM2, month);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getInt(ARG_PARAM1);
            mParam2 = getArguments().getInt(ARG_PARAM2);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_month_calendar, container, false);
        ArrayList<String> dayList = new ArrayList<String>();
        mCal = Calendar.getInstance();
        // 각 파라미터에 저장된 값을 넣어 Calendar 설정
        mCal.set(Integer.parseInt(String.valueOf(mParam1)), Integer.parseInt(String.valueOf(mParam2))-1, 1);
        // 해당 월의 1일이 될때까지 그리드뷰에 공백을 삽입함
        int dayNum = mCal.get(Calendar.DAY_OF_WEEK);
        for (int i = 1; i < dayNum; i++) {
            dayList.add("");
        }
        int dayMax = mCal.getActualMaximum(Calendar.DAY_OF_MONTH);
        // 해당 월의 최대 일 수를 구하기 위해서 .getActualMaximum(Calendar.DAY_OF_MONTH) 함수를 사용함
        // .getActualMaximum(Calendar.DAY_OF_MONTH) 함수 -> 날짜가 셋팅 된 Calendar가 가질 수 있는 최댓 값

        for (int i = 1; i < dayMax+1; i++){ // 최대 일 수만큼 dayList에 요소를 추가함
            dayList.add(String.valueOf(i));
        }
        // 월간 달력은 총 42칸으로 구성되었고, 날짜를 표현하고 남은칸은 공백 삽입
        for (int i = dayList.size(); i < 42; i++){
            dayList.add("");
        }
        // id를 바탕으로 화면 레이아웃에 정의된 GridView 객체 로딩
        GridView gridview = rootView.findViewById(R.id.gridview);
        ArrayAdapter<String> adapt
                = new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                dayList){
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                // 각 칸의 색은 흰색으로 표현하였으며, 각 칸의 텍스트를 가운데로 정렬함
                TextView tv_cell = (TextView) super.getView(position,convertView,parent);
                tv_cell.setBackgroundColor(Color.WHITE);
                tv_cell.setGravity(Gravity.CENTER_HORIZONTAL);

                // 그리드뷰로 화면을 꽉 채우기 위해, 날짜를 출력할 TextView의 높이를 그리드뷰의 높이 / 6으로 설정함
                int gridviewH = gridview.getHeight() / 6;
                // 그리드뷰가 커져서 화면을 넘어가 스크롤바가 생기지 않도록 하기 위해 -2를 해줌
                tv_cell.setHeight(gridviewH-2);

                // Return the modified item
                return tv_cell;
            }
        };

        // 어댑터를 GridView 객체에 연결
        gridview.setAdapter(adapt);
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                if(position >= dayNum-1 && position < dayMax+dayNum-1) {
                    // 그리드뷰의 n번째 요소의 내용이 1일 이상이고, 해당 월의 최대일수 이하일 때 메시지를 출력
                    Toast.makeText(getActivity(),
                            mParam1 + "." + mParam2 + "." + (position -(dayNum-2)),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        return rootView;
    }
}