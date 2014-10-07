package com.example.bloghome_shaodf;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class TouchListener implements OnTouchListener {
	float oldDist,newDist;
	float oldY,nowY;
	Context context;
	List<Float> list = new ArrayList<Float>();
	boolean on_off;

	public TouchListener(Context context,boolean bl) {
		this.context = context;
		this.on_off = bl;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		float f = event.getRawX();
		list.add(f);
		oldDist = list.get(0);
		switch (event.getAction()&MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:
			oldY = event.getY();
			return on_off;
		case MotionEvent.ACTION_UP:
			newDist = event.getRawX();
			Log.v("tag", oldDist+"    "+newDist);
			if(oldDist<100&&newDist-oldDist>200){
				Intent intent = new Intent();
				intent.setAction("open");
				context.sendBroadcast(intent);
				//HomeAct.isopen = true;
			}if(newDist-oldDist<-200||(newDist-oldDist>0&&newDist-oldDist<200&&oldDist<100)){
				Intent intent = new Intent();
				intent.setAction("back");
				context.sendBroadcast(intent);
			}
//			if(newDist-oldDist<0&&newDist-oldDist>-200&& MainActivity.isopen){
//				Intent intent = new Intent();
//				intent.setAction("open");
//				context.sendBroadcast(intent);
//				MainActivity.isopen = true;
//			}
			list = new ArrayList<Float>();
			nowY = event.getY();
			Log.v("tag", oldY+"   "+nowY);
			if(Math.abs(nowY-oldY)<40&&Math.abs(newDist-oldDist)>40){
				return true;
			}else{
				return false;
			}
		case MotionEvent.ACTION_MOVE:
			newDist = event.getRawX();
			float distance = newDist-oldDist;
			Intent intent = new Intent();
			intent.putExtra("distance", distance);
			intent.putExtra("old", oldDist);
			intent.setAction("move");
			context.sendBroadcast(intent);
			return false;
		}
		return true;
	}
}
