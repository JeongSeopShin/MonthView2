package com.example.monthview;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

;import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class WeekCalendarFragment extends Fragment {
    // 캘린더 선언
    Calendar mCal;
    int cellnum;

    //실제 사용자가 터치한 뷰를 가르키는 변수
    private int mTouchStartView = 0;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";

    // TODO: Rename and change types of parameters
    private int mParam1;
    private int mParam2;
    private int mParam3;
    public WeekCalendarFragment() {
        // Required empty public constructor
    }

    public static WeekCalendarFragment newInstance(int year, int month, int day) {
        WeekCalendarFragment fragment = new WeekCalendarFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, year);
        args.putInt(ARG_PARAM2, month);
        args.putInt(ARG_PARAM3, day);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getInt(ARG_PARAM1);
            mParam2 = getArguments().getInt(ARG_PARAM2);
            mParam3 = getArguments().getInt(ARG_PARAM3);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        int year = mParam1;
        int month = mParam2;
        View rootView = inflater.inflate(R.layout.fragment_week_calendar, container, false);
        ArrayList<String> dayList = new ArrayList<String>();
        ArrayList<String> voidcell = new ArrayList<String>();
        // 파라미터로 전달받은 year과 month를 캘린더에 세팅하여 해당 월의 첫번째 요일과 최대 일 수를 구함
        mCal = Calendar.getInstance();
        mCal.set(Integer.parseInt(String.valueOf(year)), Integer.parseInt(String.valueOf(month)) - 1, 1);
        int dayNum = mCal.get(Calendar.DAY_OF_WEEK);
        int dayMax = mCal.getActualMaximum(Calendar.DAY_OF_MONTH);

        // 한 달을 6주로 구성하기 위해 42로 모듈러 연산을 함
        int day = mParam3*7%42;

        // 첫 주에는 시작일이 될 때까지 공백을 채워야 하기 때문에 무조건 day를 0으로 설정함
        if(day<7)
            day=0;
        int count=0;

        // 첫 주면 시작일이 될 때까지 공백을 채우고 이후에 날짜를 채움
        if(day<dayNum) {
            for (int i = 1; i < dayNum; i++) {
                dayList.add("");
                count++;
            }
            for (int i = 1; i < 8 - count; i++){
                dayList.add(String.valueOf(i));
            }
        }
        // 첫주가 아니라면 정해진대로 날짜를 채움. 단, 최대일까지 달력을 채웠을 경우 날짜를 채우는 것을 멈춤
        else{
                for (int i = dayNum; i > dayNum-7; i--) { // 최대 일 수만큼 dayList에 요소를 추가
                    if((day-i+2)>dayMax)
                        break;
                    dayList.add(String.valueOf(day-i+2));

                }
        }

        ArrayAdapter<String> adapt
                = new ArrayAdapter<String>(
                getActivity(),
                R.layout.item_week,R.id.item_gridview2,
                dayList);
        // 기존에 simple_list_item_1 리소스를 사용하였으나 텍스트 정렬을 위해
        // item_week 레이아웃을 만들어 그 내부에 만든 item_gridview2를 사용하였음

        // id를 바탕으로 화면 레이아웃에 정의된 GridView 객체 로딩
        GridView gridview = rootView.findViewById(R.id.gridview2);
        // 어댑터를 GridView 객체에 연결
        gridview.setAdapter(adapt);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                // 기존에는 밑에 그리드뷰의 칸을 클릭해도 토스트 메시지가 출력되지 않으나,
                // 날짜를 클릭한 뒤에 밑의 그리드뷰의 칸을 클릭하면 토스트 메시지가 출력되도록 변수를 지정한다.
                cellnum = position+1;
            }
        });
        // 시간을 표현할 데이터 원본 준비
        String[] items = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23"};

        //어댑터 준비 (배열 객체 이용, simple_list_item_1 리소스 사용
        ArrayAdapter<String> adapt2
                = new ArrayAdapter<String>(
                getActivity(),
                R.layout.item_datetime,R.id.datetime,
                items);
        // 주간 달력의 시간을 나타내기 위해 리스트뷰를 연결함
        // 어댑터 연결
        ListView listview = rootView.findViewById(R.id.listView);
        listview.setAdapter(adapt2);

        // 공백의 그리드뷰를 표시하기 위해 각 칸에 공백을 추가함
        for(int i=0; i<7*24; i++)
            voidcell.add("");
        ArrayAdapter<String> adapt3
                = new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                voidcell){
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                TextView tv_cell = (TextView) super.getView(position,convertView,parent);
                tv_cell.setBackgroundColor(Color.WHITE);
                // 각 칸을 흰색으로 설정
                return tv_cell;
            }
        };
        // id를 바탕으로 화면 레이아웃에 정의된 GridView 객체 로딩
        GridView gridview2 = rootView.findViewById(R.id.gridview3);
        // 어댑터를 GridView 객체에 연결
        gridview2.setAdapter(adapt3);
        // 그리드뷰 스크롤시 리스트뷰도 같이 스크롤됨
        gridview2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int id =  v.getId();			    // 이벤트 들어온 뷰의 아이값
                int action = event.getAction(); 	// 이벤트 동작(다운, 무브, 업 등.)

                // 터치 다운이벤트가 들어오고, 기존에 터치된 뷰가 없으면
                // 즉, 현재 이벤트가 들어온 뷰가 사용자가 직접 터치한 뷰이면
                if(action == MotionEvent.ACTION_DOWN && mTouchStartView == 0)
                    mTouchStartView = id;	// 뷰의 id값 저장.

                // 사용자가 터치한 뷰가 스크롤뷰 리스트뷰 (2번에 이벤트를 전달하기위해 구분)
                // 사용자가 직접 터치한 뷰이면 이벤트를 넘겨준다.
                // 사용자가 직접 터치 하지 않고 다른 뷰가 이벤트를 넘겨줬을 경우는 패스
                if(mTouchStartView == R.id.listView && mTouchStartView == id)
                    gridview2.dispatchTouchEvent(event);

                // 그리드뷰이면 리스트뷰에 이벤트 넘겨줌
                else if(mTouchStartView == R.id.gridview3 && mTouchStartView == id)
                    listview.dispatchTouchEvent(event);

                // 터치가 끝나면 변수 값 초기화.
                // 플링시 그 이벤트도 같이 전달하기 위해서 마지막에 검사.
                // 플링은 무시하려면 위에 있는 터치 다운 이벤트 검사 바로 다음으로 옮기면 플링은 무시한다.
                if(action ==MotionEvent.ACTION_UP)
                    mTouchStartView = 0;

                return false;
            }
        });

        gridview2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                if(cellnum > 0)
                    // 날짜를 클릭했을 때에 cellnum이라는 변수가 1 이상이 되어
                    // 빈 그리드뷰의 칸을하면 해당 칸의 날짜와 시간이 출력됨
                    //Toast.makeText(getActivity(), mParam1 + "." + mParam2 + "." + cellnum + "." + (position-1)*7/24/2 + "시", Toast.LENGTH_SHORT).show();

                    // 빈 그리드뷰의 칸을하면 해당 칸의 position이 토스트메시지로 출력된다.
                    Toast.makeText(getActivity(),"position=" + position, Toast.LENGTH_SHORT).show();
            }
        });
        return rootView;
    }
}