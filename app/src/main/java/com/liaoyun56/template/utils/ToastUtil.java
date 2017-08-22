package com.liaoyun56.template.utils;

import android.content.Context;
import android.widget.Toast;

public class ToastUtil {

    // 当测试阶段时true
    private static final boolean isShow = true;

    public static void showToastInt(Context context, int msgResId) {
        Toast.makeText(context, msgResId, Toast.LENGTH_LONG).show();
    }

    /**
     * 测试用 在正式投入市场：删
     */
    public static void showToastTest(Context context, String msg) {
        if (isShow) {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
        }
    }

    public static void showShortToast(Context context, int resId) {
        Toast.makeText(context, context.getString(resId), Toast.LENGTH_SHORT).show();
    }

    public static void showShortToast(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }


//    /**
//     * 优化Toast提示
//     *
//     * @param context
//     * @param title
//     * @param msg
//     */
//    public static void showCustomDialog(Context context, String title, String msg) {
//        new com.education.view.MCAlertDialog(context).builder().setTitle(title).setMsg(msg).show();
//    }

//    /***
//     * 自定义带有样式的Toast
//     *
//     * @param context
//     * @param title
//     * @param msg
//     * @param dialogCallBack
//     */
//    public static void showCustomDialog(Context context, String title, String msg, final DialogCallBack dialogCallBack) {
//        com.education.view.MCAlertDialog dialog = new com.education.view.MCAlertDialog(context).builder()
//                .setTitle(title)
//                .setMsg(msg)
//                .setNegativeButton("取消", new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        dialogCallBack.onNegativeButtonClick();
//                    }
//                });
//        dialog.setPositiveButton("确定", new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialogCallBack.onPositiveButtonClick();
//            }
//        });
//        dialog.show();
//    }


//    /***
//     * 自定义带有样式的Toast
//     *
//     * @param context
//     * @param title
//     * @param msg
//     * @param dialogCallBack
//     */
//    public static void showCustomDialog(Context context, String firstButton, String SecondButton, String title, String msg, final DialogCallBack dialogCallBack) {
//        com.education.view.MCAlertDialog dialog = new com.education.view.MCAlertDialog(context).builder()
//                .setTitle(title)
//                .setMsg(msg)
//                .setNegativeButton(firstButton, new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        dialogCallBack.onNegativeButtonClick();
//                    }
//                });
//        dialog.setPositiveButton(SecondButton, new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialogCallBack.onPositiveButtonClick();
//            }
//        });
//        dialog.show();
//    }


//    public static void showCustomDialog(Context context, String singleButton, String title, String msg, final DialogCallBack dialogCallBack) {
//        com.education.view.MCAlertDialog dialog = new com.education.view.MCAlertDialog(context).builder()
//                .setTitle(title)
//                .setMsg(msg)
//                .setPositiveButton(singleButton, new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if (dialogCallBack != null)
//                            dialogCallBack.onPositiveButtonClick();
//                    }
//                });
//        dialog.show();
//    }

//
//    /**
//     * 保留对话框
//     *
//     * @param context
//     * @param singleButton
//     * @param title
//     * @param msg
//     * @param dialogCallBack
//     */
//    public static void showCustomDialogNoDismiss(Context context, String singleButton, String title, String msg, final DialogCallBack dialogCallBack) {
//        com.education.view.MCAlertDialog dialog = new com.education.view.MCAlertDialog(context).builder()
//                .setTitle(title)
//                .setMsg(msg)
//                .setPositiveButtonNoDismiss(singleButton, new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        dialogCallBack.onPositiveButtonClick();
//                    }
//                });
//        dialog.show();
//    }


    /**
     * 自定义对话框的事件接口回调
     */
    public interface DialogCallBack {

        /**
         * 点击取消接口回调
         */
        void onNegativeButtonClick();

        /**
         * 点击确认接口回调
         */
        void onPositiveButtonClick();

    }

}