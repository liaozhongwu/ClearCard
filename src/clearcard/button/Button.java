package clearcard.button;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.View;

import com.example.clearcard.R;

public abstract class Button extends View {

	private int background_normal = R.drawable.btn_bg;
	private int background_touch = R.drawable.btn_bg_touch;
	protected int background;
	protected int operation;
	protected Rect background_rect;
	protected Rect operation_rect;

	public Button(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	protected void init(){
		background = background_normal;
		background_rect = new Rect(0, 0, 80, 80);
		operation_rect = new Rect(20, 20, 60, 60);
	}
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		setMeasuredDimension(80, 80);
	}
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		Resources res = getContext().getResources();
		BitmapDrawable drawable = (BitmapDrawable)res.getDrawable(background);
		Bitmap image = drawable.getBitmap();
		canvas.drawBitmap(image, null, background_rect, null);
	}

	public void touchDown(){
		background = background_touch;
		invalidate();
	}
	public void touchUp(){
		background = background_normal;
		invalidate();
	}
}
