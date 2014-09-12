package com.normal.ordering.orderfragment;

import com.normal.ordering.R;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

public class TimeTextWatcher{

	private EditText editText;
	public TimeTextWatcher(EditText editText){
		this.editText=editText;
	}
	static int txtYear=2014;
	static int txtMonth;
	public void checkEditText(){
		switch(editText.getId()){
		case R.id.datatimg_activity_year:
			editText.addTextChangedListener(textWatcherYear);
			break;
		case R.id.datatimg_activity_month:
			editText.addTextChangedListener(textWatcherMonth);
			break;
		case R.id.datatimg_activity_day:
			editText.addTextChangedListener(textWatcherDay);
			break;
		case R.id.datatimg_activity_hour:
			editText.addTextChangedListener(textWatcherHour);
			break;
		}
	}
	public TextWatcher textWatcherYear =new TextWatcher(){

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			String str=editText.getText().toString();
			if(str==null||"".equals(str)){
			}else{
				int year=Integer.parseInt(str);
				if(year<2014){
					year=2014;
					editText.setText("2014");
				}
				txtYear=year;
			}
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// TODO Auto-generated method stub
			
		}
		
	} ;
	public TextWatcher textWatcherMonth =new TextWatcher(){

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			String str=editText.getText().toString();
			if(str==null||"".equals(str)){
			}else{
				int month=Integer.parseInt(str);
				if(month<1){
					month=1;
					editText.setText("1");
				}else if(month>12){
					month=12;
					editText.setText("12");
				}
				txtMonth=month;
			}
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			
		}
		
	};
	public TextWatcher textWatcherDay =new TextWatcher(){

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			String str=editText.getText().toString();
			if(str==null||"".equals(str)){
			}else{
				int day=Integer.parseInt(str);
				if(day<1){
					editText.setText("1");
				}else if((txtYear%4==0&&txtYear%100!=0)||(txtYear%400==0)){
					Log.i("hello", txtMonth+"");
					if(txtMonth==2){
						if(day>29){
							editText.setText("29");
						}
					}else if(txtMonth==1||txtMonth==3||txtMonth==5||txtMonth==7||txtMonth==8||txtMonth==10||txtMonth==12){
						if(day>31){
							editText.setText("31");
						}
					}else if(txtMonth==4||txtMonth==6||txtMonth==9||txtMonth==11){
						if(day>30){
							editText.setText("30");
						}
					}
				}else{
					if(txtMonth==2){
						if(day>28){
							editText.setText("28");
						}
					}else if(txtMonth==1||txtMonth==3||txtMonth==5||txtMonth==7||txtMonth==8||txtMonth==10||txtMonth==12){
						if(day>31){
							editText.setText("31");
						}
					}else {
						if(day>30){
							editText.setText("30");
						}
					}
				}
			}
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// TODO Auto-generated method stub
		}
			
		};
	public TextWatcher textWatcherHour =new TextWatcher(){

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			String str=editText.getText().toString();
			if(str==null||"".equals(str)){
			}else{
				int hour=Integer.parseInt(str);
				if(hour<0){
					editText.setText("0");
				}else if(hour>23){
					editText.setText("23");
				}
			}
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// TODO Auto-generated method stub
			String str=editText.getText().toString();
			int hour=Integer.parseInt(str);
			if(hour<0){
				editText.setText("0");
			}else if(hour>23){
				editText.setText("23");
			}
		}
			
		};
}
