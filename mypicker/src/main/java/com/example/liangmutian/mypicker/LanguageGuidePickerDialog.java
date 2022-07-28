package com.example.liangmutian.mypicker;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LanguageGuidePickerDialog extends Dialog {

    public interface OnLanguageSelectedListener {
        void onLanguageSelected(int index, String value);
    }


    public LanguageGuidePickerDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    private void setParams(LanguageGuidePickerDialog.Params params) {
        Params params1 = params;
    }


    private static final class Params {
        private boolean shadow = true;
        private boolean canCancel = false;
        private LoopView loopZone;
        private OnLanguageSelectedListener callback;
    }

    public static class Builder {
        private final Context context;
        private String selectValue;
        private final LanguageGuidePickerDialog.Params params;
        private float textSize = 20f;
        private int animResId = 0;
        private boolean needDismiss = true;

        public Builder setAnimResId(int resId) {
            this.animResId = resId;
            return this;
        }

        public Builder setNeedDismiss(boolean needDismiss) {
            this.needDismiss = needDismiss;
            return this;
        }

        public Builder setTextSize(float textSize) {
            this.textSize = textSize;
            return this;
        }

        public Builder(Context context) {
            this.context = context;
            params = new LanguageGuidePickerDialog.Params();
        }

        public Builder setsSlectValue(String selectValue) {
            this.selectValue = selectValue;
            return this;
        }

        private String getCurrDateValues() {
            return params.loopZone.getCurrentItemValue();
        }

        private int getCurrDateIndex() {
            return params.loopZone.getCurrentItem();
        }

        public LanguageGuidePickerDialog create() {
            final LanguageGuidePickerDialog dialog = new LanguageGuidePickerDialog(context, params.shadow ? R.style.Theme_Light_NoTitle_Dialog : R.style.Theme_Light_NoTitle_NoShadow_Dialog);
            View view = LayoutInflater.from(context).inflate(R.layout.layout_picker_language_guide, null);

            final LoopView loopZone = (LoopView) view.findViewById(R.id.loop_language);
            loopZone.setTextSize(textSize);
            //修改优化边界值 by lmt 16/ 9 /12.禁用循环滑动,循环滑动有bug
            loopZone.setCyclic(false);
            List<String> data = Arrays.asList(context.getResources().getStringArray(R.array.language_array));

            loopZone.setArrayList(data);
            if (selectValue != null) {
                int position = 0;
                for (int i = 0; i < data.size(); i++) {
                    if (data.get(i).contains(selectValue)) {
                        position = i;
                    }
                }
                loopZone.setCurrentItem(position);
            } else {
                loopZone.setCurrentItem(0);
            }

            view.findViewById(R.id.tx_finish).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (needDismiss) {
                        dialog.dismiss();
                    }
                    params.callback.onLanguageSelected(getCurrDateIndex(), getCurrDateValues());
                }
            });

            Window win = dialog.getWindow();
            win.getDecorView().setPadding(0, 0, 0, 0);
            WindowManager.LayoutParams lp = win.getAttributes();
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.MATCH_PARENT;
            win.setAttributes(lp);
            win.setGravity(Gravity.CENTER);
            if (animResId != 0) {
                win.setWindowAnimations(R.style.Animation_Bottom_Rising);
            }

            dialog.setContentView(view);
            dialog.setCanceledOnTouchOutside(params.canCancel);
            dialog.setCancelable(params.canCancel);

            params.loopZone = loopZone;
            dialog.setParams(params);

            return dialog;
        }


        public Builder setOnLanguageSelectedListener(OnLanguageSelectedListener onLanguageSelectedListener) {
            params.callback = onLanguageSelectedListener;
            return this;
        }


    }
}
