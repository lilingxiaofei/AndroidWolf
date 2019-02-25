package com.chunlangjiu.app.goods.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chunlangjiu.app.R;
import com.chunlangjiu.app.abase.BaseActivity;
import com.chunlangjiu.app.goods.adapter.TagAdapter;
import com.chunlangjiu.app.goods.bean.TagBean;
import com.pkqup.commonlibrary.realm.RealmUtils;
import com.pkqup.commonlibrary.util.KeyBoardUtils;
import com.pkqup.commonlibrary.util.ToastUtils;
import com.pkqup.commonlibrary.view.tagview.FlowTagLayout;
import com.pkqup.commonlibrary.view.tagview.OnTagClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @CreatedbBy: liucun on 2018/6/24.
 * @Describe:
 */
public class SearchActivity extends BaseActivity {

    @BindView(R.id.rl_hot)
    RelativeLayout rlHot;
    @BindView(R.id.rl_history)
    RelativeLayout rlHistory;

    @BindView(R.id.tag_hot)
    FlowTagLayout tagHot;
    @BindView(R.id.tag_history)
    FlowTagLayout tagHistory;
    @BindView(R.id.imgDelete)
    ImageView imgDelete;

    private List<TagBean> hotLists;
    private List<TagBean> historyLists;

    private TagAdapter<TagBean> hotAdapter;
    private TagAdapter<TagBean> historyAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.goods_activity_search);
        initView();
        initData();
    }

    @Override
    public void setTitleView() {
        titleImgLeft.setOnClickListener(onClickListener);
        titleName.setVisibility(View.GONE);
        titleSearchView.setVisibility(View.VISIBLE);
        titleSearchEdit.setOnEditorActionListener(onEditorActionListener);
    }

    private TextView.OnEditorActionListener onEditorActionListener = new
            TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                        if (!TextUtils.isEmpty(titleSearchEdit.getText().toString().trim())) {
                            KeyBoardUtils.hideSoftInput(SearchActivity.this);
                            searchGoods();
                        }
                    }
                    return true;
                }
            };

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.img_title_left:
                    finish();
                    break;
                case R.id.imgDelete:
                    deleteHistory();
                    break;
            }
        }
    };

    private void initView() {
        imgDelete.setOnClickListener(onClickListener);

        hotLists = new ArrayList<>();
        hotAdapter = new TagAdapter<>(this, hotLists);
        tagHot.setAdapter(hotAdapter);
        tagHot.setOnTagClickListener(new OnTagClickListener() {
            @Override
            public void onItemClick(FlowTagLayout parent, View view, int position) {
                ToastUtils.showShort(hotLists.get(position).getName());
            }
        });

        historyLists = new ArrayList<>();
        historyAdapter = new TagAdapter<>(this, historyLists);
        tagHistory.setAdapter(historyAdapter);
        tagHistory.setOnTagClickListener(new OnTagClickListener() {
            @Override
            public void onItemClick(FlowTagLayout parent, View view, int position) {
                GoodsListNewActivity.startGoodsListNewActivity(SearchActivity.this, "","","", "", historyLists.get(position).getName());
            }
        });
    }

    private void initData() {
        hotLists.clear();
        for (int i = 0; i < 10; i++) {
            TagBean tagBean = new TagBean();
            tagBean.setName("热搜" + i);
            hotLists.add(tagBean);
        }
        hotAdapter.setLists(hotLists);

        findHistory();
    }

    private void findHistory() {
        List<TagBean> findLists = (List<TagBean>) RealmUtils.queryAllByDescending(TagBean.class, "index");
        if (null != findLists && findLists.size() > 0) {
            rlHistory.setVisibility(View.VISIBLE);
            historyLists = findLists;
            historyAdapter.setLists(historyLists);
        } else {
            rlHistory.setVisibility(View.GONE);
        }
    }

    private void searchGoods() {
        List<TagBean> findLists = (List<TagBean>) RealmUtils.queryAll(TagBean.class);
        TagBean tagBean = new TagBean();
        if (null != findLists && findLists.size() > 0) {
            tagBean.setIndex(findLists.get(findLists.size() - 1).getIndex() + 1);
            for (int i = 0; i < findLists.size(); i++) {
                if (findLists.get(i).getName().equals(titleSearchEdit.getText().toString().trim())) {
                    RealmUtils.deleteElement(TagBean.class, i);
                }
            }
        } else {
            tagBean.setIndex(0);
        }
        tagBean.setName(titleSearchEdit.getText().toString().trim());
        RealmUtils.add(tagBean);
        findHistory();

        GoodsListNewActivity.startGoodsListNewActivity(this, "","","", "", titleSearchEdit.getText().toString().trim());
    }

    private void deleteHistory() {
        RealmUtils.deleteAll(TagBean.class);
        findHistory();
    }
}
