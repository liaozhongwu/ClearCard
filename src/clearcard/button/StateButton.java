package clearcard.button;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;

import com.example.clearcard.R;

public class StateButton extends Button{
	
	private int pause = R.drawable.pause;
	private int start = R.drawable.start;

	public StateButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		operation = pause;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		Resources res = getContext().getResources();
		BitmapDrawable drawable = (BitmapDrawable)res.getDrawable(operation);
		Bitmap image = drawable.getBitmap();
		canvas.drawBitmap(image, null, operation_rect, null);
	}
	
	public void pause(){
		operation = start;
		invalidate();
	}
	public void goon(){
		operation = pause;
		invalidate();
	}
}
