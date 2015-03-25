package com.lntu.online.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ListView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.reflect.TypeToken;
import com.lntu.online.R;
import com.lntu.online.adapter.ExamPlanAdapter;
import com.lntu.online.http.HttpUtil;
import com.lntu.online.http.RetryAuthListener;
import com.lntu.online.info.NetworkConfig;
import com.lntu.online.model.ExamPlan;
import com.lntu.online.util.JsonUtil;

import org.apache.http.Header;

import java.util.Collections;
import java.util.List;

import butterknife.ButterKnife;

public class ExamPlanActivity extends ActionBarActivity {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_plan);
        ButterKnife.inject(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        startNetwork();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
            finish();
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    private void startNetwork() {
        HttpUtil.get(this, NetworkConfig.serverUrl + "examPlan/info", new RetryAuthListener(this) {

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    List<ExamPlan> ceps = JsonUtil.fromJson(responseString, new TypeToken<List<ExamPlan>>(){}.getType());
                    Collections.sort(ceps);
                    ListView lvRoot = (ListView) findViewById(R.id.exam_plan_lv_root);
                    lvRoot.setAdapter(new ExamPlanAdapter(getContext(), ceps));
                } catch(Exception e) {
                    String[] msgs = responseString.split("\n");
                    if (msgs[0].equals("0x01040003")) {
                        showNothingDialog();
                    } else {
                        showErrorDialog("提示", msgs[0], msgs[1]);
                    }
                }
            }

            @Override
            public void onBtnRetry() {
                startNetwork();
            }

        });
    }

    private void showNothingDialog() {
        new MaterialDialog.Builder(this)
                .title("提示")
                .content("暂时没有考试信息，过一个月再看吧")
                .cancelable(false)
                .positiveText("确定")
                .positiveColorRes(R.color.colorPrimary)
                .callback(new MaterialDialog.ButtonCallback() {

                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        finish();
                    }

                })
                .show();
    }

}