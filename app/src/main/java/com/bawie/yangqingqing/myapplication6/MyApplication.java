package com.bawie.yangqingqing.myapplication6;

import android.app.Application;
import android.os.Environment;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.io.File;

/**
 * Created by Bare on 2017/10/24.
 */

public class MyApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        String path = Environment.getExternalStorageDirectory()+"/1507D";

        File cacheDir = new File(path);

        //希望有缓存，指明缓存的路径
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                //指明内存缓存要缓存多少*多少像素图片  480 * 800
                .memoryCacheExtraOptions(480,800)
                //指明线程优先级
                .threadPriority(100)
                //配置多少个线程在后台加载图片
                .threadPoolSize(3)
                //指明图片sdcard缓存，缓存到什么地方
                .diskCache(new UnlimitedDiskCache(cacheDir))
                //限定一下内存缓存的大小  在内存缓存当中缓存2MB的大小图片
                .memoryCacheSize(2 * 1024 * 1024)
                //在sdcard缓存多少MB的图片
                .diskCacheSize(50 * 1024 * 1024)
                //指明图片文件名(MD5) 为了避免有重复的图片
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .build();
        ImageLoader.getInstance().init(config);
    }

    public static DisplayImageOptions options(){
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                //对图片进行sdcard缓存
                .cacheOnDisk(true)
                //是否对图片进行内存缓存
                .cacheInMemory(true)
                .showImageOnFail(R.mipmap.ic_launcher)
                .showImageForEmptyUri(R.mipmap.ic_launcher)
                .showImageOnLoading(R.mipmap.ic_launcher)
                //.displayer(new CircleBitmapDisplayer())
                .displayer(new RoundedBitmapDisplayer(20))
                .build();
        return options;
    }
}
