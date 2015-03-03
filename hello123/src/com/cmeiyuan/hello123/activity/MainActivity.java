package com.cmeiyuan.hello123.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.cmeiyuan.hello123.Constants;
import com.cmeiyuan.hello123.R;
import com.cmeiyuan.hello123.adapter.FundAdapter;
import com.cmeiyuan.hello123.api.BaseApi.AsyncCallBack;
import com.cmeiyuan.hello123.api.BaseApi.Error;
import com.cmeiyuan.hello123.api.v2.EstimatesApi;
import com.cmeiyuan.hello123.api.v2.FundsApi;
import com.cmeiyuan.hello123.bean.FundHold;
import com.cmeiyuan.hello123.bean.v2.Estimate;
import com.cmeiyuan.hello123.bean.v2.Estimates;
import com.cmeiyuan.hello123.bean.v2.Funds;
import com.cmeiyuan.hello123.data.FundManager;
import com.cmeiyuan.hello123.listener.SimpleAnimatorListener;
import com.cmeiyuan.hello123.swipe.SwipeMenu;
import com.cmeiyuan.hello123.swipe.SwipeMenuCreator;
import com.cmeiyuan.hello123.swipe.SwipeMenuItem;
import com.cmeiyuan.hello123.swipe.SwipeMenuListView;
import com.cmeiyuan.hello123.swipe.SwipeMenuListView.OnMenuItemClickListener;
import com.cmeiyuan.hello123.swipe.Test;
import com.cmeiyuan.hello123.util.FundUtil;
import com.cmeiyuan.hello123.util.UnitUtil;
import com.cmeiyuan.hello123.widget.MySwipeRefreshLayout;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ValueAnimator;
import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CountDownLatch;

public class MainActivity extends AnalysisActivity implements AsyncCallBack<Estimates> {

    private static final long MIN_REFRESH_TIME = 1200;

    private long start_refresh_time = 0;
    private long last_refresh_time = 0;
    private boolean isStartRefresh = false;
    private boolean isFirstRefresh = true;

    private EstimatesApi api = new EstimatesApi();

    private TextView tv_time;
    private TextView tv_income;
    private TextView tv_income_unit;
    // private ProgressBar progressBar;
    private MySwipeRefreshLayout swipeRefreshLayout;
    private SwipeMenuListView swipeMenuListView;
    private View headerView;
    private FundAdapter adapter = new FundAdapter();
    private DecimalFormat format = new DecimalFormat("#0.00");
    private SimpleDateFormat sd1 = new SimpleDateFormat("HH:mm", Locale.CHINA);
    private SimpleDateFormat sd2 = new SimpleDateFormat("MM月dd日", Locale.CHINA);
    private Handler handler = new Handler();
    private Date nowDate = new Date();

    private CountDownLatch countDownLatch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MobclickAgent.setDebugMode(true);
        MobclickAgent.updateOnlineConfig(this);
        UmengUpdateAgent.setUpdateOnlyWifi(false);
        UmengUpdateAgent.update(this);

        setContentView(R.layout.activity_main);

        api.setAsyncCallBack(this);

        headerView = LayoutInflater.from(this).inflate(
                R.layout.list_header_income, null);

        // progressBar = (ProgressBar) headerView.findViewById(R.id.progress);
        // progressBar.setVisibility(View.INVISIBLE);

        tv_time = (TextView) headerView.findViewById(R.id.tv_update_time);
        tv_income = (TextView) headerView.findViewById(R.id.tv_income_value);
        tv_income.setText("0.00");
        tv_income_unit = (TextView) headerView.findViewById(R.id.tv_total_income_unit);
        tv_income_unit.setText("");
        tv_time.setVisibility(View.INVISIBLE);


        swipeRefreshLayout = (MySwipeRefreshLayout) findViewById(R.id.swipeLayout);
        int a = R.color.progress;
        swipeRefreshLayout.setProgressBackgroundColor(a);
        swipeRefreshLayout.setColorSchemeColors(Color.WHITE);
        // swipeRefreshLayout.setColorSchemeColors(Constants.COLOR_POSITIVE, Constants.COLOR_NEGATIVE);
        swipeRefreshLayout.setOnRefreshListener(new MySwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Do work to refresh the list here.
                refreshData();
            }
        });

        swipeRefreshLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                autoRefreshData();
            }
        });

        swipeMenuListView = (SwipeMenuListView) findViewById(R.id.lv_swipeMenu);
        swipeMenuListView.setAdapter(adapter);
        swipeMenuListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (position < 1) {
                    return;
                }
                Intent intent = new Intent(MainActivity.this,
                        HistoryActivity.class);
                FundHold fund = adapter.getList().get(position - 1);
                intent.putExtra(HistoryActivity.FUND, fund);
                startActivity(intent);
            }
        });

        setMenuListView();
        setTotalIncome();
        refreshList();
        tv_time.setVisibility(View.INVISIBLE);

        loadFundList();
    }

    private void loadFundList() {
        FundsApi fundApi = new FundsApi();
        fundApi.setAsyncCallBack(new AsyncCallBack<Funds>() {
            @Override
            public void onSuccess(Funds funds) {
                FundManager.setList(funds.getItems());
            }

            @Override
            public void onFailed(Error error) {

            }
        });
        fundApi.get();
    }

    private void setMenuListView() {
        SwipeMenuListView lv_swipe = swipeMenuListView;
        lv_swipe.setDividerHeight(0);
        lv_swipe.addHeaderView(headerView);
        lv_swipe.setHeaderDividersEnabled(false);

        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
                SwipeMenuItem openItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                openItem.setBackground(new ColorDrawable(Color
                        .parseColor("#4169E1")));
                // set item width
                openItem.setWidth(UnitUtil.dp2px(MainActivity.this, 90));
                // set item title
                openItem.setTitle("编辑");
                // set item title fontsize
                openItem.setTitleSize(18);
                // set item title font color
                openItem.setTitleColor(Color.WHITE);
                // set a icon
                // openItem.setIcon(R.drawable.ic_menu_edit);
                // add to menu
                menu.addMenuItem(openItem);

                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color
                        .parseColor("#EE7600")));
                // set item width
                deleteItem.setWidth(UnitUtil.dp2px(MainActivity.this, 90));
                // set item title
                deleteItem.setTitle("删除");
                // set item title fontsize
                deleteItem.setTitleSize(18);
                // set item title font color
                deleteItem.setTitleColor(Color.WHITE);
                // set a icon
                // deleteItem.setIcon(R.drawable.ic_delete);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };

        // set creator
        lv_swipe.setMenuCreator(creator);

        // step 2. listener item click event
        lv_swipe.setOnMenuItemClickListener(new OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu,
                                           int index) {
                switch (index) {
                    case 0:
                        Intent intent = new Intent(MainActivity.this,
                                AddActivity.class);
                        intent.putExtra(AddActivity.MODE, AddActivity.MODE_EDIT);
                        FundHold fund1 = adapter.getList().get(position);
                        intent.putExtra(AddActivity.FUND, fund1);
                        startActivityForResult(intent, 2);
                        break;
                    case 1:
                        final FundHold fund = adapter.getList().get(position);

                        new AlertDialog.Builder(MainActivity.this).setTitle("删除提示")
                                .setMessage("确定要删除" + fund.fundName + "吗？")
                                .setPositiveButton("确定", new OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        FundUtil.deleteFund(MainActivity.this, fund);
                                        refreshList();
                                    }
                                }).setNegativeButton("取消", null).show();

                        break;
                }
                return false;
            }
        });

        lv_swipe.setCloseInterpolator(new DecelerateInterpolator());
    }

    private void refreshList() {
        adapter.setList(FundUtil.getFunds(this));
        adapter.notifyDataSetChanged();
    }

    private void autoRefreshData() {
        if (!isStartRefresh) {
            stopRefreshData();
            isStartRefresh = true;
            handler.post(r);
        }
    }

    private void stopRefreshData() {
        isStartRefresh = false;
        handler.removeCallbacks(r);
    }

    private Runnable r = new Runnable() {

        @Override
        public void run() {
            long interval = FundUtil.getInterval(MainActivity.this);
            if ((System.currentTimeMillis() - last_refresh_time) / 1000 > interval) {
                swipeRefreshLayout.setRefreshing(true);
                refreshData();
            }
            handler.postDelayed(r, interval);
        }
    };

    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_add_fund) {
            startActivityForResult(new Intent(this, AddActivity.class), 1);
        }
        return true;
    }

    @Override
    public void onSuccess(Estimates estimates) {
        setUpdateTime(estimates);
        updateData(estimates);
        setTotalIncome();
        adapter.notifyDataSetChanged();
        stopRefresh();
    }

    @Override
    public void onFailed(Error error) {
        showToast(error.getMessage());
        setTotalIncome();
        stopRefresh();
    }

    private void refreshData() {
        List<FundHold> list = adapter.getList();
        if (list != null && list.size() > 0) {
            start_refresh_time = System.currentTimeMillis();
            countDownLatch = new CountDownLatch(list.size());
            String[] fundCodes = new String[list.size()];
            for (int i = 0; i < list.size(); i++) {
                fundCodes[i] = list.get(i).fundCode;
            }
            api.get(fundCodes);
        } else {
            stopRefresh();
        }
    }

    protected void stopRefresh() {
        long time = MIN_REFRESH_TIME - (System.currentTimeMillis() - start_refresh_time) / 1000;
        if (time > 0 && !isFirstRefresh) {
            isFirstRefresh = false;
            //handler.postDelayed(new Runnable() {
            //    @Override
            //    public void run() {
            //        swipeRefreshLayout.setRefreshing(false);
            //    }
            //}, time);
            swipeRefreshLayout.setRefreshing(false);
        } else {
            swipeRefreshLayout.setRefreshing(false);
        }
        last_refresh_time = System.currentTimeMillis();
    }

    private void updateData(Estimates value) {
        List<FundHold> list = adapter.getList();
        if (list == null) {
            return;
        }
        List<Estimate> items = value.getItems();
        for (int i = 0; i < items.size(); i++) {
            Estimate estimate = items.get(i);
            for (int j = 0; j < list.size(); j++) {
                FundHold hold = list.get(j);
                if (hold.fundCode.equalsIgnoreCase(estimate.getFundCode())) {
                    hold.estimate = estimate;
                    break;
                }
            }
        }
    }

    private void setUpdateTime(Estimates value) {
        if (value != null) {
            Date date = new Date(value.getItems().get(0).getDateTime());

            if (date != null) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                int day1 = calendar.get(Calendar.DAY_OF_MONTH);
                calendar.setTime(nowDate);
                int day2 = calendar.get(Calendar.DAY_OF_MONTH);
                String time;
                if (day1 == day2) time = sd1.format(date);
                else if (day1 == day2 - 1) {
                    time = "昨天";
                } else {
                    time = sd2.format(date);
                }
                tv_time.setVisibility(View.VISIBLE);
                tv_time.setText("更新于：" + time);
            }
        } else {
            tv_time.setVisibility(View.INVISIBLE);
        }
    }

    private void setTotalIncome() {
        List<FundHold> list = adapter.getList();
        if (list != null) {
            float value = 0;
            boolean hasValue = false;
            for (int i = 0; i < list.size(); i++) {
                FundHold fund = list.get(i);
                if (fund.estimate != null) if (!TextUtils.isEmpty(fund.fundShare)) {
                    try {
                        value += fund.estimate.getGszzl()
                                * fund.getFundShare() / 100;
                    } catch (Exception ignored) {
                    }
                    hasValue = true;
                }
            }

            final float incomeValue = value;

            if (hasValue) {
                ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, incomeValue);
                valueAnimator.setDuration(200);
                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        tv_income.setText(format.format(valueAnimator.getAnimatedValue()));
                        tv_income_unit.setText("元");
                    }
                });
                valueAnimator.addListener(new SimpleAnimatorListener() {

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        tv_income.setText(format.format(incomeValue));
                        tv_income_unit.setText("元");
                    }

                });
                valueAnimator.start();
            } else {
                tv_income.setText("--");
                tv_income_unit.setText("");
            }

            if (value > 0) {
                headerView.setBackgroundColor(Constants.COLOR_POSITIVE);
            } else if (value < 0) {
                headerView.setBackgroundColor(Constants.COLOR_NEGATIVE);
            } else {
                headerView.setBackgroundColor(Color.parseColor("#55000000"));
            }
        } else {
            tv_income.setText("--");
            headerView.setBackgroundColor(Color.parseColor("#55000000"));
        }

    }

    @Override
    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
        super.onActivityResult(arg0, arg1, arg2);
        if (arg1 == RESULT_OK) {
            refreshList();
            refreshData();
        }
    }

    @Override
    protected void onInitTopBar(TextView left, TextView right, TextView center) {
        left.setVisibility(View.GONE);
        right.setText("添加");
    }

    @Override
    protected void onTopBarSelected(View v) {
        if (v == getRightTextView()) {
            Intent intent = new Intent(this, AddActivity.class);
            intent.putExtra(AddActivity.MODE, AddActivity.MODE_ADD);
            startActivityForResult(intent, 1);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopRefreshData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                autoRefreshData();
            }
        }, 1500);
    }

}
