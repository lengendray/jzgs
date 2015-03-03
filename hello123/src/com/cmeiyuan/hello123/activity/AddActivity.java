package com.cmeiyuan.hello123.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.cmeiyuan.hello123.R;
import com.cmeiyuan.hello123.bean.FundHold;
import com.cmeiyuan.hello123.bean.v2.Fund;
import com.cmeiyuan.hello123.util.FundUtil;

public class AddActivity extends AnalysisActivity {

    public static final String MODE_ADD = "add";
    public static final String MODE_EDIT = "edit";
    public static final String MODE = "mode";
    public static final String FUND = "fund";

    private EditText et_fund_code;
    private EditText et_fund_name;
    private EditText et_fund_share;
    private Button btn_add_fund;

    private FundHold fund;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        et_fund_code = (EditText) findViewById(R.id.et_fund_code);
        et_fund_name = (EditText) findViewById(R.id.et_fund_name);
        et_fund_share = (EditText) findViewById(R.id.et_fund_share);
        btn_add_fund = (Button) findViewById(R.id.btn_add_fund);

        try {
            fund = (FundHold) getIntent().getSerializableExtra(FUND);
            updateContentView(fund);
        } catch (Exception e) {
        }

        String mode = getIntent().getStringExtra(MODE);
        if (mode != null && mode.equalsIgnoreCase(MODE_EDIT)) {
            setTitle("修改基金");
            btn_add_fund.setText("保存修改");
        } else {
            setTitle("添加基金");
            btn_add_fund.setText("立即添加");
            gotoSearchActivity1();
        }

        btn_add_fund.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if (fund == null) {
                    fund = new FundHold();
                } else {
                    FundUtil.deleteFund(AddActivity.this, fund);
                }

                fund.fundCode = et_fund_code.getText().toString().trim();
                fund.fundName = et_fund_name.getText().toString().trim();
                fund.fundShare = et_fund_share.getText().toString().trim();

                if (addFund(fund)) {
                    setResult(RESULT_OK);
                    finish();
                }
            }
        });

        TextWatcher watcher = new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                updateSubmitButton();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        et_fund_name.addTextChangedListener(watcher);
        et_fund_code.addTextChangedListener(watcher);

        updateSubmitButton();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            try {
                Fund fund = (Fund) data.getSerializableExtra(FUND);
                FundHold hold = new FundHold();
                hold.fundCode = fund.getFundCode();
                hold.fundName = fund.getFundName();
                hold.fundType = fund.getFundType();
                updateContentView(hold);
            } catch (Exception e) {

            }
        } else {
            if (requestCode == 1) {
                finish();
            }
        }

    }

    private void updateSubmitButton() {
        if (btn_add_fund != null) {
            String fundName = et_fund_name.getText().toString().trim();
            String fundCode = et_fund_code.getText().toString().trim();
            if (TextUtils.isEmpty(fundName) || TextUtils.isEmpty(fundCode)) {
                btn_add_fund.setEnabled(false);
            } else {
                btn_add_fund.setEnabled(true);
            }
        }
    }

    private void updateContentView(FundHold fund) {
        if (fund != null) {
            if (fund.fundCode != null) {
                et_fund_code.setText(fund.fundCode);
            }
            if (fund.fundName != null) {
                et_fund_name.setText(fund.fundName);
                et_fund_name.setSelection(fund.fundName.length());
            }
            if (fund.fundShare != null) {
                et_fund_share.setText(String.valueOf(fund.fundShare));
            }
            et_fund_share.requestFocus();
        }
    }

    private boolean addFund(FundHold fund) {
        if (TextUtils.isEmpty(fund.fundCode)) {
            showToast("基金代码不能为空");
            return false;
        }

        if (TextUtils.isEmpty(fund.fundName)) {
            showToast("基金名称不能为空");
            return false;
        }
        FundUtil.saveFund(this, fund);
        return true;
    }

    @Override
    protected void onInitTopBar(TextView left, TextView right, TextView center) {
        left.setText("返回");
        right.setText("搜索");
    }

    @Override
    protected void onTopBarSelected(View v) {
        if (v == getLeftTextView()) {
            finish();
        } else if (v == getRightTextView()) {
            gotoSearchActivity2();
        }
    }

    private void gotoSearchActivity1() {
        startActivityForResult(new Intent(this, SearchActivity.class), 1);
    }

    private void gotoSearchActivity2() {
        startActivityForResult(new Intent(this, SearchActivity.class), 2);
    }

}
