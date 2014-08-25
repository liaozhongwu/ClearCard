package clearcard.button;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public abstract class ButtonTouchListener implements OnTouchListener{
	
	Button button;
	public ButtonTouchListener(Button button){
		this.button = button;
	}
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if(event.getAction() == MotionEvent.ACTION_DOWN){
			button.touchDown();
		}else if(event.getAction() == MotionEvent.ACTION_UP){
			button.touchUp();
			action();
			return false;
		}
		return true;
	}
	public abstract void action();
}
