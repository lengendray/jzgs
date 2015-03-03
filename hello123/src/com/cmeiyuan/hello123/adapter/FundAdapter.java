package com.cmeiyuan.hello123.adapter;

import java.text.DecimalFormat;
import java.util.List;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.cmeiyuan.hello123.Constants;
import com.cmeiyuan.hello123.R;
import com.cmeiyuan.hello123.bean.FundHold;
import com.cmeiyuan.hello123.listener.SimpleAnimatorListener;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;

public class FundAdapter extends BaseAdapter {
    private List<FundHold> list;

    public List<FundHold> getList() {
        return list;
    }

    public void setList(List<FundHold> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int arg0) {
        return list.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int arg0, View arg1, ViewGroup arg2) {
        if (arg1 == null) {
            arg1 = LayoutInflater.from(arg2.getContext()).inflate(
                    R.layout.list_item_fund, null);
            arg1.setTag(new ViewHolder(arg1));
        }
        ViewHolder viewHolder = (ViewHolder) arg1.getTag();
        viewHolder.fillView(getItem(arg0));
        return arg1;
    }

    public class ViewHolder {
        TextView tv_name;
        TextView tv_share;
        TextView tv_net_value;
        TextView tv_income_value;
        TextView tv_income_unit;
        // Button btn_udpate;
        // Button btn_delete;
        DecimalFormat format = new DecimalFormat("#0.00");

        public ViewHolder(View view) {
            tv_name = (TextView) view.findViewById(R.id.tv_name);
            tv_share = (TextView) view.findViewById(R.id.tv_share);
            tv_net_value = (TextView) view.findViewById(R.id.tv_net_value);
            tv_income_value = (TextView) view.findViewById(R.id.tv_income_value);
            tv_income_unit = (TextView) view.findViewById(R.id.tv_income_unit);
            // btn_udpate = (Button) view.findViewById(R.id.btn_update_fund);
            // btn_delete = (Button) view.findViewById(R.id.btn_delete_fund);
        }

        public void fillView(Object item) {
            if (item != null && item instanceof FundHold) {

                FundHold fund = (FundHold) item;
                tv_name.setText(fund.fundName);
                tv_share.setText(format.format(fund.getFundShare()) + "份");

                if (fund.estimate != null) {
                    final float netValuePercent = fund.estimate.getGszzl();
                    ValueAnimator valueAnimator1 = ValueAnimator.ofFloat(0, netValuePercent);
                    valueAnimator1.setDuration(200);
                    valueAnimator1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator valueAnimator) {
                            tv_net_value.setText(format.format(valueAnimator.getAnimatedValue()) + "%");
                        }
                    });

                    valueAnimator1.addListener(new SimpleAnimatorListener() {

                        @Override
                        public void onAnimationEnd(Animator animator) {
                            tv_net_value.setText(format.format(netValuePercent) + "%");
                        }

                    });

                    valueAnimator1.start();

                    if (netValuePercent > 0) {
                        tv_net_value.setTextColor(Constants.COLOR_POSITIVE);
                    } else if (netValuePercent < 0) {
                        tv_net_value.setTextColor(Constants.COLOR_NEGATIVE);
                    } else {
                        tv_net_value.setTextColor(Constants.COLOR_ZERO);
                    }

                    final float incomeValue = fund.estimate.getGszzl()
                            * fund.getFundShare() / 100;

                    ValueAnimator valueAnimator2 = ValueAnimator.ofFloat(0, incomeValue);
                    valueAnimator2.setDuration(200);
                    valueAnimator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator valueAnimator) {
                            tv_income_value.setText(format.format(valueAnimator.getAnimatedValue()));
                            tv_income_unit.setText("元");
                        }
                    });

                    valueAnimator2.addListener(new SimpleAnimatorListener() {

                        @Override
                        public void onAnimationEnd(Animator animator) {
                            tv_income_value.setText(format.format(incomeValue));
                            tv_income_unit.setText("元");
                        }

                    });

                    valueAnimator2.start();

                    if (incomeValue > 0) {
                        tv_income_value.setTextColor(Constants.COLOR_POSITIVE);
                        tv_income_unit.setTextColor(Constants.COLOR_POSITIVE);
                    } else if (incomeValue < 0) {
                        tv_income_value.setTextColor(Constants.COLOR_NEGATIVE);
                        tv_income_unit.setTextColor(Constants.COLOR_NEGATIVE);
                    } else {
                        tv_income_value.setTextColor(Constants.COLOR_ZERO);
                        tv_income_unit.setTextColor(Constants.COLOR_ZERO1);
                    }

                }
            } else {
                tv_net_value.setText("--");
                tv_income_value.setText("--");
                tv_net_value.setTextColor(Constants.COLOR_ZERO);
                tv_income_value.setTextColor(Constants.COLOR_ZERO);
                tv_income_unit.setText("");
            }
        }

    }


}
