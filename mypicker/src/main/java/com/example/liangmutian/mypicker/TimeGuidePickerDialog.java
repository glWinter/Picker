package com.example.liangmutian.mypicker;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.util.Arrays;
import java.util.List;

public class TimeGuidePickerDialog extends Dialog {

    public interface OnTimeSelectedListener {
        void onTimeSelected(int[] times);
    }

    public interface OnPrevSelectedListener {
        void onPrevSelected();
    }


    private Params params;

    public TimeGuidePickerDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    private void setParams(TimeGuidePickerDialog.Params params) {
        this.params = params;
    }


    private static final class Params {
        private boolean shadow = true;
        private boolean canCancel = false;
        private LoopView loopHour, loopMin;
        private OnTimeSelectedListener callback;
        private OnPrevSelectedListener prevCallback;


    }

    public static class Builder {
        private final Context context;
        private Integer selectHour;
        private Integer selectMinute;
        private final TimeGuidePickerDialog.Params params;

        public Builder(Context context) {
            this.context = context;
            params = new TimeGuidePickerDialog.Params();
        }

        public Builder setSelectHour(int hour) {
            this.selectHour = hour;
            return this;
        }

        public Builder setSelectMinute(int minute) {
            this.selectMinute = minute;
            return this;
        }

        /**
         * 获取当前选择的时间
         *
         * @return int[]数组形式返回。例[12,30]
         */
        private final int[] getCurrDateValues() {
            int currHour = Integer.parseInt(params.loopHour.getCurrentItemValue());
            int currMin = Integer.parseInt(params.loopMin.getCurrentItemValue());
            return new int[]{currHour, currMin};
        }

        public TimeGuidePickerDialog create() {
            final TimeGuidePickerDialog dialog = new TimeGuidePickerDialog(context, params.shadow ? R.style.Theme_Light_NoTitle_Dialog : R.style.Theme_Light_NoTitle_NoShadow_Dialog);
            View view = LayoutInflater.from(context).inflate(R.layout.layout_picker_time_guide, null);

            final LoopView loopHour = (LoopView) view.findViewById(R.id.loop_hour);

            //修改优化边界值 by lmt 16/ 9 /12.禁用循环滑动,循环滑动有bug
            loopHour.setCyclic(false);
            loopHour.setArrayList(d(0, 24));
            if (selectHour != null) {
                loopHour.setCurrentItem(selectHour);
            } else {
                loopHour.setCurrentItem(0);
            }

            final LoopView loopMin = (LoopView) view.findViewById(R.id.loop_min);
            loopMin.setCyclic(false);
            loopMin.setArrayList(d(0, 60));
            if (selectMinute != null) {
                loopMin.setCurrentItem(selectMinute);
            } else {
                loopMin.setCurrentItem(0);
            }

            view.findViewById(R.id.tx_finish).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    params.callback.onTimeSelected(getCurrDateValues());
                }
            });
            view.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    params.prevCallback.onPrevSelected();
                }
            });
            Window win = dialog.getWindow();
            win.getDecorView().setPadding(0, 0, 0, 0);
            WindowManager.LayoutParams lp = win.getAttributes();
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            win.setAttributes(lp);
            win.setGravity(Gravity.BOTTOM);
            win.setWindowAnimations(R.style.Animation_Bottom_Rising);

            dialog.setContentView(view);
            dialog.setCanceledOnTouchOutside(params.canCancel);
            dialog.setCancelable(params.canCancel);

            params.loopHour = loopHour;
            params.loopMin = loopMin;
            dialog.setParams(params);

            return dialog;
        }


        public Builder setOnTimeSelectedListener(OnTimeSelectedListener onTimeSelectedListener) {
            params.callback = onTimeSelectedListener;
            return this;
        }

        public Builder setPrevSelectedListener(OnPrevSelectedListener prevCallback) {
            params.prevCallback = prevCallback;
            return this;
        }

        /**
         * 将数字传化为集合，并且补充0
         *
         * @param startNum 数字起点
         * @param count    数字个数
         * @return
         */
        private static List<String> d(int startNum, int count) {
            String[] values = new String[count];
            for (int i = startNum; i < startNum + count; i++) {
                String tempValue = (i < 10 ? "0" : "") + i;
                values[i - startNum] = tempValue;
            }
            return Arrays.asList(values);
        }
    }
}
