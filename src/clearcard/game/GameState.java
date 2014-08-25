package clearcard.game;

import java.util.Timer;
import java.util.TimerTask;

import clearcard.GameActivity;
import clearcard.game.handler.TimerHandler;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

import com.example.clearcard.R;

public class GameState extends View{
	
	private final int WIDTH = 300;
	private final int HEIGHT = 40;
	private final int GAMETIME = 600;
	private final int RADIO = 5;
	private final int NUMBERSIZE = 24;
	private int time;
	private Timer timer;
	private TimerTask timerTask;
	private Handler timerHandler;
	private int state;
	
	private RectF backgroundRect;
	private RectF processRect;
	private Paint backgroundPaint;
	private Paint numberPaint;
	private Paint processPaint;
	
	public GameState(Context context, AttributeSet attrs) {
		super(context, attrs);
		timerHandler = new TimerHandler(this);
		setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		
		backgroundRect = new RectF(0, 0, WIDTH, HEIGHT);
		processRect = new RectF(0, 0, WIDTH, HEIGHT);
		backgroundPaint = new Paint();
		processPaint = new Paint();
		numberPaint = new Paint();
		backgroundPaint.setColor(getResources().getColor(R.color.time_background));
		numberPaint.setColor(getResources().getColor(R.color.time_number));
		numberPaint.setTextSize(NUMBERSIZE);
		numberPaint.setTextAlign(Paint.Align.CENTER);
		processPaint.setColor(getResources().getColor(R.color.time_process));

		time = GAMETIME;
	}
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		setMeasuredDimension(WIDTH, HEIGHT);
	}
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.drawRoundRect(backgroundRect, RADIO, RADIO, backgroundPaint);
		processRect.right = WIDTH * time / GAMETIME;
		canvas.drawRoundRect(processRect, RADIO, RADIO, processPaint);
		canvas.drawText(Integer.toString(time / 10), WIDTH / 2, (HEIGHT+NUMBERSIZE) / 2 - 2, numberPaint);
	}
	public void start(){
		state = 1;//normal
		if(timer == null && timerTask == null){
			timer = new Timer();
			timerTask = new TimerTask() {
				@Override
				public void run() {
					if(time >= 0){
						timerHandler.sendMessage(new Message());
					}else{
						over();
					}
				}
			};
			timer.schedule(timerTask, 0, 100);
		}
	}
	public void goon(){
		state = 1;//normal
		if(timer == null && timerTask == null){
			timer = new Timer();
			timerTask = new TimerTask() {
				@Override
				public void run() {
					if(time >= 0){
						timerHandler.sendMessage(new Message());
					}else{
						over();
					}
				}
			};
			timer.schedule(timerTask, 0, 100);
		}
	}
	public void pause(){
		state = 2;//pause
		if(timer != null && timerTask != null){
			timer.cancel();
			timerTask.cancel();
			timer = null;
			timerTask = null;
		}
	}
	public void over(){
		state = 0;//over;
		if(timer != null && timerTask != null){
			timer.cancel();
			timerTask.cancel();
			timer = null;
			timerTask = null;
		}
		if(getContext() instanceof GameActivity)
			((GameActivity)getContext()).over();
	}
	public void refresh(){
		time = GAMETIME;
		invalidate();
	}
	public void handleTimer(){
		time--;
		invalidate();
	}
	public int gameTime(){
		return GAMETIME / 10;
	}
	public int currentTime(){
		return time / 10;
	}
	public boolean isNormal(){
		return state == 1;
	}
	public boolean isPause(){
		return state == 2;
	}
	public boolean isTimeOver(){
		return state == 0;
	}
}
