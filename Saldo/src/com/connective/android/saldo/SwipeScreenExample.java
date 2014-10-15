package com.connective.android.saldo;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.animation.AnimationUtils;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.connective.android.saldo.SimpleGestureFilter.SimpleGestureListener;

public class SwipeScreenExample extends Activity implements
		SimpleGestureListener {
	private SimpleGestureFilter detector;
	private int contador = 0;
	ViewFlipper vf;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final int currentapiVersion = android.os.Build.VERSION.SDK_INT;
		if (currentapiVersion < 14) {
			setContentView(R.layout.swipe_screen);
		} else {
			setContentView(R.layout.swipe_screen_jelly);
		}

		// Detect touched area
		detector = new SimpleGestureFilter(this, this);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent me) {
		// Call onTouchEvent of SimpleGestureFilter class
		this.detector.onTouchEvent(me);
		return super.dispatchTouchEvent(me);
	}

	@Override
	public void onSwipe(int direction) {
		String str = "";
		vf = (ViewFlipper) findViewById(R.id.details);
		switch (direction) {
		case SimpleGestureFilter.SWIPE_RIGHT:
			contador--;
			if (contador < 0) {
				contador = 0;
			} else {
				vf.setAnimation(AnimationUtils.loadAnimation(this,
						R.anim.slide_right));
				vf.showPrevious();
			}

			str = "Swipe Right";
			break;
		case SimpleGestureFilter.SWIPE_LEFT:
			str = "Swipe Left";
			contador++;
			if (contador == 3) {
				finish();
			} else {
				vf.setAnimation(AnimationUtils.loadAnimation(this,
						R.anim.slide_left));
				vf.showNext();
			}
			break;
		case SimpleGestureFilter.SWIPE_DOWN:
			str = "Swipe Down";
			break;
		case SimpleGestureFilter.SWIPE_UP:
			str = "Swipe Up";
			break;

		}
		// Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onDoubleTap() {
		Toast.makeText(this, "Double Tap", Toast.LENGTH_SHORT).show();
	}

}