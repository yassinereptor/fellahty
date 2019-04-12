package com.fil.fellahty;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fil.fellahty.classes.LineView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class chart extends Fragment {


    public chart() {
        // Required empty public constructor
    }
    int randomint = 9;
    LineView lineView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_chart, container, false);
        lineView = (LineView)v.findViewById(R.id.line_view);

//must*
        ArrayList<String> test = new ArrayList<String>();
        for (int i=0; i<randomint; i++){
            test.add(String.valueOf(i+1));
        }
        lineView.setBottomTextList(test);
        lineView.setDrawDotLine(true);
        lineView.setShowPopup(LineView.SHOW_POPUPS_NONE);
        randomSet(lineView);
        return v;
    }
    private void randomSet(LineView lineView){
        ArrayList<Integer> dataList = new ArrayList<Integer>();
        int random = (int)(Math.random()*9+1);
        for (int i=0; i<randomint; i++){
            dataList.add((int)(Math.random()*random));
        }

        ArrayList<Integer> dataList2 = new ArrayList<Integer>();
        random = (int)(Math.random()*9+1);
        for (int i=0; i<randomint; i++){
            dataList2.add((int)(Math.random()*random));
        }

        ArrayList<Integer> dataList3 = new ArrayList<Integer>();
        random = (int)(Math.random()*9+1);
        for (int i=0; i<randomint; i++){
            dataList3.add((int)(Math.random()*random));
        }

        ArrayList<ArrayList<Integer>> dataLists = new ArrayList<ArrayList<Integer>>();
        dataLists.add(dataList);
        dataLists.add(dataList2);


        lineView.setDataList(dataLists);
    }
}
