package com.winsafe.jiangmenzhibao.utils;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import java.util.Calendar;

public class DateYearMonthSelect {
	public DateYearMonthSelect() {
		// TODO Auto-generated constructor stub
	}

	public static DatePickerDialog showYearMonthDatePickerDialog(Context context, OnDateSetListener callBack) {
		Calendar mCalendar = Calendar.getInstance();
		int year = mCalendar.get(Calendar.YEAR);
		int monthOfYear = mCalendar.get(Calendar.MONTH);
		int dayOfMonth = mCalendar.get(Calendar.DAY_OF_MONTH);

		DatePickerDialog mDatePickerDialog = new DatePickerDialog(context, callBack, year, monthOfYear, dayOfMonth);
		mDatePickerDialog.show();
		return mDatePickerDialog;
	}

	private static DatePicker findDatePicker(ViewGroup group) {
		if (group != null) {
			for (int i = 0, j = group.getChildCount(); i < j; i++) {
				View child = group.getChildAt(i);
				if (child instanceof DatePicker) {
					return (DatePicker) child;
				} else if (child instanceof ViewGroup) {
					DatePicker result = findDatePicker((ViewGroup) child);
					if (result != null)
						return result;
				}
			}
		}
		return null;
	}
}
