package com.chunlangjiu.app.appraise.activity;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chunlangjiu.app.R;
import com.chunlangjiu.app.abase.BaseActivity;
import com.chunlangjiu.app.amain.fragment.CartFragment;
import com.chunlangjiu.app.appraise.adapter.AppraiserAdapter;
import com.chunlangjiu.app.appraise.bean.AppraiseBean;
import com.chunlangjiu.app.util.MyStatusBarUtils;

import java.util.List;

import butterknife.BindView;


/**
 * @CreatedbBy: 吴申飞 on 2018/6/16.
 * @Describe: 鉴定师主页
 */
public class AppraiserMainActivity extends BaseActivity {

    @BindView(R.id.tvAppraiseNum)
    TextView tvAppraiseNum;
    @BindView(R.id.rlAppraiseNovice)
    RelativeLayout rlAppraiseNovice;

    private List<AppraiseBean> appraiseList ;
    private AppraiserAdapter appraiserAdapter;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.appraise_activity_main);
    }

    @Override
    public void setTitleView() {

    }

}
