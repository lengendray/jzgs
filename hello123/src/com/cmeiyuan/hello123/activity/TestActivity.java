package com.cmeiyuan.hello123.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;

import com.cmeiyuan.hello123.R;

/**
 * Created by Administrator on 2015/2/26.
 */
public class TestActivity extends AnalysisActivity {

    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeLayout);

        // swipeRefreshLayout.setRefreshing(true);

        swipeRefreshLayout.setColorSchemeColors(Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                int a = R.drawable.shape_progress_bg;
                int b = com.cmeiyuan.widget.R.drawable.shape_progress_bg;
            }
        });
    }
}
