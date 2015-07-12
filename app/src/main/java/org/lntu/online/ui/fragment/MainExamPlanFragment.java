package org.lntu.online.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.lntu.online.R;
import org.lntu.online.ui.activity.MainActivity;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MainExamPlanFragment extends MainActivity.BaseFragment {

    @InjectView(R.id.main_exam_plan_toolbar)
    protected Toolbar toolbar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_main_exam_plan, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.inject(this, view);

        toolbar.setNavigationOnClickListener(getOpenNavigationClickListener());
    }

}