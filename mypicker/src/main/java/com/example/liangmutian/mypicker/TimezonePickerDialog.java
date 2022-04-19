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

public class TimezonePickerDialog extends Dialog {

    public interface OnTimeSelectedListener {
        void onTimeSelected(String value);
    }


    public TimezonePickerDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    private void setParams(TimezonePickerDialog.Params params) {
        Params params1 = params;
    }


    private static final class Params {
        private boolean shadow = true;
        private boolean canCancel = true;
        private LoopView loopZone;
        private OnTimeSelectedListener callback;
    }

    public static class Builder {
        private final Context context;
        private String selectValue;
        private final TimezonePickerDialog.Params params;

        public Builder(Context context) {
            this.context = context;
            params = new TimezonePickerDialog.Params();
        }

        public Builder setsSlectValue(String selectValue) {
            this.selectValue = selectValue;
            return this;
        }

        private String getCurrDateValues() {
            return params.loopZone.getCurrentItemValue();
        }

        public TimezonePickerDialog create() {
            final TimezonePickerDialog dialog = new TimezonePickerDialog(context, params.shadow ? R.style.Theme_Light_NoTitle_Dialog : R.style.Theme_Light_NoTitle_NoShadow_Dialog);
            View view = LayoutInflater.from(context).inflate(R.layout.layout_picker_timezone, null);

            view.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
            final LoopView loopZone = (LoopView) view.findViewById(R.id.loop_zone);

            //修改优化边界值 by lmt 16/ 9 /12.禁用循环滑动,循环滑动有bug
            loopZone.setCyclic(false);
//            List<String> data = d();
            List<String> data = Arrays.asList(context.getResources().getStringArray(R.array.timezone_array));

            loopZone.setArrayList(data);
            if (selectValue != null) {
                int position = 0;
                for (int i = 0; i < data.size(); i++) {
                    if (data.get(i).contains(selectValue)) {
                        position = i;
                    }
                }
//                int position = data.indexOf(selectValue);
                loopZone.setCurrentItem(position);
            } else {
                loopZone.setCurrentItem(0);
            }

            view.findViewById(R.id.tx_finish).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    params.callback.onTimeSelected(getCurrDateValues());
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

            params.loopZone = loopZone;
            dialog.setParams(params);

            return dialog;
        }


        public Builder setOnTimeSelectedListener(OnTimeSelectedListener onTimeSelectedListener) {
            params.callback = onTimeSelectedListener;
            return this;
        }


        /**
         */
        private static List<String> d() {
            List<String> list = new ArrayList<>();
            list.add("GMT-11:00");
            list.add("GMT-10:00");
            list.add("GMT-09:00");
            list.add("GMT-08:00");
            list.add("GMT-07:00");
            list.add("GMT-06:00");
            list.add("GMT-05:00");
            list.add("GMT-04:00");
            list.add("GMT-03:00");
            list.add("GMT-02:00");
            list.add("GMT-01:00");
            list.add("GMT+00:00");
            list.add("GMT+01:00");
            list.add("GMT+02:00");
            list.add("GMT+03:00");
            list.add("GMT+04:00");
            list.add("GMT+05:00");
            list.add("GMT+06:00");
            list.add("GMT+07:00");
            list.add("GMT+08:00");
            list.add("GMT+09:00");
            list.add("GMT+10:00");
            list.add("GMT+11:00");
            list.add("GMT+12:00");
            return list;
        }
    }
}
