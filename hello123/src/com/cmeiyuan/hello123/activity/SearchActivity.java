package com.cmeiyuan.hello123.activity;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.cmeiyuan.hello123.R;
import com.cmeiyuan.hello123.adapter.FundAdapter1;
import com.cmeiyuan.hello123.api.BaseApi;
import com.cmeiyuan.hello123.api.v2.FundsApi;
import com.cmeiyuan.hello123.bean.v2.Fund;
import com.cmeiyuan.hello123.bean.v2.Funds;
import com.cmeiyuan.hello123.data.FundManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class SearchActivity extends AnalysisActivity {

    private EditText et_key;
    private ListView lv_fund;
    private FundAdapter1 adapter = new FundAdapter1();
    private List<Fund> list;
    private List<Fund> result = new ArrayList<Fund>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        lv_fund = (ListView) findViewById(R.id.lv_fund);
        et_key = (EditText) findViewById(R.id.et_serach_key);

        lv_fund.setAdapter(adapter);

        et_key.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (s.length() == 0) {
                    adapter.setList(list);
                } else {
                    adapter.setList(getItem(String.valueOf(s)));
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        list = FundManager.getList();

        if (list == null || list.size() == 0) {
            FundsApi fundApi = new FundsApi();
            fundApi.setAsyncCallBack(new BaseApi.AsyncCallBack<Funds>() {
                @Override
                public void onSuccess(Funds funds) {
                    FundManager.setList(funds.getItems());
                    list = FundManager.getList();
                    adapter.setList(list);
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onFailed(BaseApi.Error error) {

                }
            });
            fundApi.get();
        }

        if (list == null || list.size() == 0) {
            try {
                InputStream in = getAssets().open("fund.txt");
                Gson gson = new GsonBuilder()
                        .excludeFieldsWithoutExposeAnnotation().create();
                list = gson.fromJson(new InputStreamReader(in),
                        new TypeToken<List<Fund>>() {
                        }.getType());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (list != null) {
            adapter.setList(list);
            adapter.notifyDataSetChanged();
        }

        lv_fund.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent = new Intent();
                Fund fund = adapter.getList().get(position);
                intent.putExtra(AddActivity.FUND, fund);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    public List<Fund> getItem(String key) {
        result.clear();
        for (Fund item : list) {
            if (item.getFundName().contains(key)) {
                result.add(item);
                continue;
            }

            if (item.getFundCode().contains(key)) {
                result.add(item);
                continue;
            }
        }
        return result;
    }

    @Override
    protected void onInitTopBar(TextView left, TextView right, TextView center) {
        left.setText("返回");
        right.setVisibility(View.GONE);
    }

    @Override
    protected void onTopBarSelected(View v) {
        if (v == getLeftTextView()) {
            finish();
        }
    }

}
