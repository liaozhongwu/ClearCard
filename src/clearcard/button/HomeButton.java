package clearcard.button;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;

import com.example.clearcard.R;

public class HomeButton extends Button{
	
	private int home = R.drawable.home;

	public HomeButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		operation = home;
	}	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		Resources res = getContext().getResources();
		BitmapDrawable drawable = (BitmapDrawable)res.getDrawable(operation);
		Bitmap image = drawable.getBitmap();
		canvas.drawBitmap(image, null, operation_rect, null);
	}
}
