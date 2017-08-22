package com.liaoyun56.template.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.liaoyun56.template.R;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by liaoyun on 2017/8/22.
 */
public class PicUtil {
    /**
     * 简单加载图片
     * @param context
     * @param image
     * @param view
     */
    public static void setPic(Context context, int image, ImageView view){
        Glide.with(context).load(image).into(view);
    }

    /**
     * 设置图片（不使用缓存）, 加载过程中 和 加载错误后 使用默认的图片
     * @param context
     * @param url
     * @param view
     */
    public static void setPic(Context context, String url, ImageView view){
        Glide.with(context).load(url)
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(view);
    }

    /**
     * 跳过缓存加载图片
     * @param context
     * @param url
     * @param view
     */
    public static void setPicSkipCache(Context context, String url, ImageView view){
        Glide.with(context).load(url)
                .skipMemoryCache(true)                                              //路过缓存
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(view);
    }
    /**
     * 加载图片， 并使用缓存
     * @param context
     * @param url
     * @param view
     */
    public static void setPicWithCache(Context context, String url, ImageView view){
        Glide.with(context).load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)                           //缓存策略
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(view);
    }

    /**
     * 加载动画
     * @param context
     * @param url
     * @param animate        动画类型  如：R.anim.item_alpha_in
     * @param view
     */
    public static void setAnim(Context context, String url, int animate, ImageView view){
        Glide.with(context).load(url)
                .animate(animate)
                .into(view);
    }

    /**
     * 设置静态 gif
     * @param context
     * @param url
     * @param view
     */
    public static void setStatiGif(Context context, String url, ImageView view){
        Glide.with(context).load(url)
                .asBitmap()
                .into(view);
    }

    /**
     * 设置动态 gif
     * @param context
     * @param url
     * @param view
     */
    public static void setGif(Context context, String url, ImageView view){
        Glide.with(context).load(url)
                .asGif()
                .into(view);
    }

    /**
     * 清理磁盘缓存 需要在子线程中执行
     * @param context
     */
    public static void clearDiskCache(Context context){
        Glide.get(context).clearDiskCache();
    }

    /**
     * 清理内存缓存  可以在UI主线程中进行
     * @param context
     */
    public static void clearMemory(Context context){
        Glide.get(context).clearMemory();
    }

    /**
     * 设置圆形图
     * @param context
     * @param url
     * @param view
     */
    public static void setCirclePic(final Context context, String url, ImageView view){
        Glide.with(context).load(url)
                .asBitmap()
                .centerCrop()
                .into(new BitmapImageViewTarget(view){
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                        roundedBitmapDrawable.setCircular(true);
                        view.setImageDrawable(roundedBitmapDrawable);
                    }
                });
    }

    //======拍照获取图片================================================
    /**
     * 自定义文件名， 获取拍照的 File
     * @param context
     * @return
     */
    public static File createImgFile(Context context){
        String filename = "img_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".img";
        File dir;
        if(Environment.getExternalStorageDirectory().equals(Environment.MEDIA_MOUNTED)){
            dir = Environment.getExternalStorageDirectory();
        }else{
            dir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        }
        File tmpFile = new File(dir, filename);
        try{
            if(tmpFile.exists()){
                tmpFile.delete();
            }
            tmpFile.createNewFile();
        }catch (IOException e){
            LogUtil.error(e.getMessage());
            e.printStackTrace();
        }
        //String photoPath = tmpFile.getAbsolutePath();                               //获取文件路径
        return tmpFile;
    }

    /**
     * 压缩 图片
     * @param photoPath 文件路径 getAbsolutePath()
     * @return
     */
    public static Bitmap compressPic(String photoPath, int width, int height){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(photoPath, options);
        int photoWidth = options.outWidth;
        int photoHeight = options.outHeight;
        int inSampleSize = 1;                                                       //压缩比例
        if(photoWidth>width || photoHeight>height){
            int widthRatio = Math.round((float)photoWidth/width);
            int heightRatio = Math.round((float)photoHeight/height);
            inSampleSize = Math.min(widthRatio, heightRatio);
        }
        options.inSampleSize = inSampleSize;
        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeFile(photoPath, options);
        return bitmap;
    }

    /**
     * 解决一件经常发生的事情，就是可能拍照之后，
     * 照片没有及时出现在手机相册中，而是需要重新启动后才会有。
     * 发一个广播就好了
     */
    public static void gallaryAddPic(Context context, Uri photoUri){
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        mediaScanIntent.setData(photoUri);
        context.sendBroadcast(mediaScanIntent);
    }
}
