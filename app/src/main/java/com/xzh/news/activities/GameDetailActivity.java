package com.xzh.news.activities;

import android.app.ProgressDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xzh.news.R;
import com.xzh.news.cache.ImageLoader;
import com.xzh.news.entity.Detail;
import com.xzh.news.utils.HttpAdress;
import com.xzh.news.utils.HttpUtils;
import com.xzh.news.utils.SystemBarTintManager;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 游戏详情界面
 */
public class GameDetailActivity extends ActionBarActivity implements View.OnClickListener {


    @Bind(R.id.tv_game_name)
    TextView tvGameName;
    @Bind(R.id.tv_game_type)
    TextView tvGameType;
    @Bind(R.id.tv_game_product)
    TextView tvGameProduct;
    @Bind(R.id.tv_game_time)
    TextView tvGameTime;
    @Bind(R.id.tv_release_company)
    TextView tvReleaseCompany;
    @Bind(R.id.tv_game_url)
    TextView tvGameUrl;
    @Bind(R.id.tv_game_terrace)
    TextView tvGameTerrace;
    @Bind(R.id.tv_game_language)
    TextView tvGameLanguage;
    @Bind(R.id.tv_game_detail)
    TextView tvGameDetail;
    @Bind(R.id.iv_game)
    ImageView ivGame;
    private Toolbar toolbar;
    private String title;
    private String litpic;
    private String imageURl;
    private String description;
    private String typename;
    private String release_company;
    private String release_date;
    private String made_company;
    private String terrace;
    private String language;
    private String websit;
    private ProgressDialog pd;//进度对话框

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_detail);
        initWindow();
        ButterKnife.bind(this);
        initView();
        //初始化数据
        initData();
        //设置监听
        setListener();

    }

    //初始化控件，获取控件
    private void initView() {
        //获取控件
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        //2.替代
        setSupportActionBar(toolbar);
        //加载背景颜色
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorBackground)));
        //设置显示返回上一级的图标
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //设置标题
        getSupportActionBar().setTitle("游戏详情");
        //设置标题栏字体颜色
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.backup));
    }

    //初始化窗体布局
    private void initWindow() {
        SystemBarTintManager tintManager;
        //由于沉浸式状态栏需要在Android4.4.4以上才能使用
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintColor(getResources().getColor(R.color.colorBackground));
            tintManager.setStatusBarTintEnabled(true);
        }
    }

    //初始化数据
    private void initData() {
        //获取到从游戏列表中传递过来的参数
        String id = getIntent().getStringExtra("id");
        String typeid = getIntent().getStringExtra("typeid");
        String url = String.format(HttpAdress.ChapterContent_URL, id, typeid);//游戏详情请求地址
        //下载网络数据
        pd = new ProgressDialog(this);
        pd.setMessage("游戏详情加载中。。。");
        pd.show();
        HttpUtils.downLoadData(url, new HttpUtils.OnFetchDataListener() {
            @Override
            public void OnFetch(String url, byte[] data) {
                Log.i("------>ju", "我来了，，，，");
                String json = new String(data);
                //json解析
                Detail detail = new Gson().fromJson(json, Detail.class);
                pd.dismiss();//隐藏进度对话框
                title = detail.getTitle();
                description = detail.getDescription();
                typename = detail.getTypename();
                //游戏封面链接
                litpic = detail.getLitpic();
                release_company = detail.getRelease_company();
                //发行时间
                release_date = detail.getRelease_date();
                made_company = detail.getMade_company();
                terrace = detail.getTerrace();
                language = detail.getLanguage();
                websit = detail.getWebsit();

                tvGameName.setText(title);
                //游戏封面链接
                imageURl = HttpAdress.DMGEAME_URL + litpic;
                //下载图片，优先使用缓存图片
                ImageLoader.getInstance().disPlay(ivGame, imageURl);
                tvGameDetail.setText(description);
                tvGameType.setText(typename);
                tvReleaseCompany.setText(release_company);
                //发行时间
                tvGameTime.setText(release_date);
                tvGameProduct.setText(made_company);
                tvGameTerrace.setText(terrace);
                tvGameLanguage.setText(language);
                //设置官网链接
                tvGameUrl.setText(
                        Html.fromHtml("<a href=" + websit + ">点击进入</a> "));
                tvGameUrl.setMovementMethod(LinkMovementMethod.getInstance());

            }
        });

    }

    //设置监听
    private void setListener() {
        //设置toolbar返回上一级的事件监听
        toolbar.setNavigationOnClickListener(this);
    }

    //toolbar的事件监听
    @Override
    public void onClick(View v) {
        //返回上一级
        finish();
    }

}
