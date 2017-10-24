package com.bawie.yangqingqing.myapplication6;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.gson.Gson;
import com.liaoinstan.springview.container.AcFunFooter;
import com.liaoinstan.springview.container.DefaultFooter;
import com.liaoinstan.springview.widget.SpringView;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.main_rcycler)
    RecyclerView mainRcycler;
    @BindView(R.id.main_spr)
    SpringView mainSpr;

    private int page=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mainSpr.setType(SpringView.Type.FOLLOW);
        mainSpr.setHeader(new AcFunFooter(this,R.mipmap.ic_launcher));
        mainSpr.setFooter(new DefaultFooter(this));
        getData();

        mainSpr.setListener(new SpringView.OnFreshListener(){

            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mainSpr.onFinishFreshAndLoad();
                        page++;
                        getData();
                    }
                },2000);
            }

            @Override
            public void onLoadmore() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mainSpr.onFinishFreshAndLoad();
                        page++;
                        getData();
                    }
                },2000);

            }
        });
    }
    //解析数据
    private void getData(){
        OkHttpClient client=new OkHttpClient();
        Request request=new Request.Builder()
                .url("http://www.yulin520.com/a2a/impressApi/news/mergeList?sign=C7548DE604BCB8A17592EFB9006F9265&pageSize=20&gender=2&ts=1871746850&page="+page)
                .build();
        Call call=new OkHttpClient().newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String str=response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Gson gson=new Gson();
                        MyBean bean=gson.fromJson(str,MyBean.class);
                        List<MyBean.DataBean> list=bean.getData();
                        mainRcycler.setLayoutManager(new LinearLayoutManager(MainActivity.this,LinearLayoutManager.VERTICAL,false));
                        mainRcycler.setAdapter(new MyAdapter(MainActivity.this,list));
                    }
                });
            }
        });
    }
}
