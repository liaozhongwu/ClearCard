package clearcard.button;

import com.example.clearcard.R;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;

public class ShareButton extends Button{
	
	private int share = R.drawable.share;

	public ShareButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		operation = share;
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
