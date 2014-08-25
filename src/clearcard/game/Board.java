package clearcard.game;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import clearcard.GameActivity;
import clearcard.game.handler.DispearAnimationHandler;
import clearcard.game.handler.DropAnimationHandler;

import com.example.clearcard.R;


public class Board extends View implements OnTouchListener{

	public Board(Context context) {
		super(context);
	}
	public Board(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	public Board(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}
	public void setParentActivity(GameActivity activity){
		this.parentActivity = activity;
	}
	private GameActivity parentActivity;

	private int xCnt;
	private int yCnt;
	private int cellPadding;
	private int width;
	private int height;
	private int cellsize;
	private int xOffset;
	private int yOffset;
	private RectF backgroundRectF;
	private Paint backgroundPaint;
	private boolean isLocked;
	private boolean isInited = false;
	private List<Element> elements;
	private List<Element> touchedElements;
	private List<Element> bombElements;
	private Element touchElement;

	private int dispearMinPadding;
	private int dispearMaxPadding;
	private int dispearPadding;
	private Timer dispearAnimationTimer;
	private TimerTask dispearAnimationTimerTask;
	private DispearAnimationHandler dispearAnimationHandler;
	private Paint touchedPaint;
	private Paint bombPaint;
	private Timer dropAnimationTimer;
	private TimerTask dropAnimationTimerTask;
	private DropAnimationHandler dropAnimationHandler;

	public void init(){
		xCnt = 7;
		yCnt = 10;
		cellPadding = 4;
		width = this.getMeasuredWidth();
		height = this.getMeasuredHeight();
		cellsize = Math.min(width/xCnt,height/yCnt);
		xOffset = (width - cellsize*xCnt)/2;
		yOffset = (height - cellsize*yCnt)/2;
		backgroundRectF = new RectF(xOffset, yOffset, width - xOffset, height - yOffset);
		backgroundPaint = new Paint();
		isLocked = false;
		isInited = true;
		elements = new ArrayList<Element>();
		for(int i = 0 ; i < xCnt ; i++){
			for(int j = 0 ; j < yCnt ; j++){
				elements.add(Element.random(i,j));
			}
		}
		touchedElements = new ArrayList<Element>();
		bombElements = new ArrayList<Element>();

		dispearMinPadding = cellPadding;
		dispearMaxPadding = cellsize / 2;
		dispearPadding = dispearMinPadding;
		dispearAnimationHandler = new DispearAnimationHandler(this);
		touchedPaint = new Paint();
		bombPaint = new Paint();
		
		dropAnimationHandler = new DropAnimationHandler(this);
		this.setOnTouchListener(this);
	}
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if(!isInited) init();
		canvas.clipRect(backgroundRectF);
		Resources res = this.getContext().getResources();
		BitmapDrawable drawable;
		Bitmap image;

		touchedPaint.setColor(Color.WHITE);
		touchedPaint.setAntiAlias(true);
		backgroundPaint.setColor(getResources().getColor(R.color.board_background));
		backgroundPaint.setAlpha(150);;
		backgroundPaint.setAntiAlias(true);
		bombPaint.setColor(Color.BLACK);
		bombPaint.setAntiAlias(true);

		canvas.drawRoundRect(backgroundRectF, 15, 15, backgroundPaint);
		if(touchElement != null){
			canvas.drawRoundRect(elementShowRect(touchElement), 10, 10, touchedPaint);
		}
		for(Element temp : touchedElements){
			canvas.drawRoundRect(elementShowRect(temp), 10, 10, touchedPaint);
		}
		for(Element temp : bombElements){
			canvas.drawRoundRect(elementShowRect(temp), 10, 10, bombPaint);
		}
		for(Element temp : elements){
			for(int tempImage : temp.getType().images()){
				drawable = (BitmapDrawable)res.getDrawable(tempImage);
				image = drawable.getBitmap();
				canvas.drawBitmap(image, null, elementShowRect(temp), null);
			}
		}
	}
	public RectF elementShowRect(Element element){
		int x = element.getX();
		int y = element.getY();
		int dropOffset = element.getDropOffset();
		int padding = cellPadding;
		if(touchedElements.contains(element) || bombElements.contains(element))
			padding = dispearPadding;
		return new RectF(xOffset + x * cellsize + padding, yOffset+ y * cellsize + padding - dropOffset,
				xOffset + (x+1) * cellsize - padding, yOffset + (y+1) * cellsize - padding - dropOffset);
	}
	public int realXToVirtualX(int rX){
		int vX = (rX - xOffset) / cellsize;
		if(vX < 0) vX = 0;
		if(vX >= xCnt) vX = xCnt - 1;
		return vX;
	}
	public int realYToVirtualY(int rY){
		int vY = (rY - yOffset) / cellsize;
		if(vY < 0) vY = 0;
		if(vY >= yCnt) vY = yCnt - 1;
		return vY;
	}
	public Element realCoorElement(int rX, int rY){
		return virtualCoorElement(realXToVirtualX(rX), realYToVirtualY(rY));
	}
	public Element virtualCoorElement(int vX, int vY){
		for(Element temp : elements)
			if(temp.getX() == vX && temp.getY() == vY)
				return temp;
		return null;
	}
	public int virtualCoorElementIndex(int vX,int vY){
		for(int i = 0 ; i < elements.size() ; i++){
			Element temp = elements.get(i);
			if(temp.getX() == vX && temp.getY() == vY)
				return i;
		}
		return 0;
	}
	public ArrayList<Element> nearElements(Element element){
		ArrayList<Element> nearElements = new ArrayList<Element>();
		for(Element temp : elements){
			if(element.near(temp))
				nearElements.add(temp);
		}
		return nearElements;
	}
	public ArrayList<Element> preYElements(Element element){
		ArrayList<Element> preElements = new ArrayList<Element>();
		for(Element temp : elements){
			if(temp.getX() == element.getX() && temp.getY() < element.getY())
				preElements.add(temp);
		}
		return preElements;
	}
	public void lock(){
		isLocked = true;
	}
	public void unlock(){
		isLocked = false;
	}
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		setMeasuredDimension(measureWidth(widthMeasureSpec),
				measureHeight(heightMeasureSpec));
	}
	private int measureWidth(int measureSpec) {
		int result = 0;
		int specMode = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);

		if (specMode == MeasureSpec.EXACTLY) {
			result = specSize;
		} else {
			result = (int) 400 + getPaddingLeft()
					+ getPaddingRight();
			if (specMode == MeasureSpec.AT_MOST) {
				result = Math.min(result, specSize);
			}
		}

		return result;
	}
	
	private int measureHeight(int measureSpec) {
		int result = 0;
		int specMode = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);

		if (specMode == MeasureSpec.EXACTLY) {
			result = specSize;
		} else {
			result = (int) 400 + getPaddingTop()
					+ getPaddingBottom();
			if (specMode == MeasureSpec.AT_MOST) {
				result = Math.min(result, specSize);
			}
		}
		return result;
	}
	private long doubleTouchTime;
	private Element doubleTouchElement;
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if(!isLocked){
			int x = (int)event.getX();
			int y = (int)event.getY();
			Element element = realCoorElement(x, y);
			if(event.getAction() == MotionEvent.ACTION_DOWN){
				touchElement = element;
				if(doubleTouchElement == null || System.currentTimeMillis() - doubleTouchTime > 200 ||
						!touchElement.equals(element)){
					doubleTouchTime = System.currentTimeMillis();
					doubleTouchElement = element;
					addTouchElement(element);
				}
				else{
					doubleTouch(element);
				}
			}
			if(event.getAction() == MotionEvent.ACTION_MOVE){
				if(!element.equals(touchElement)){
					touchElement = element;
					addTouchElement(element);
				}
			}
			if(event.getAction() == MotionEvent.ACTION_UP){
				touchElement = null;
				handleTouch();
			}
			return true;
		}
		return false;
	}
	public boolean validateTouchedElement(Element element){
		if(touchedElements.isEmpty()){
			return true;
		}
		else{
			for(Element temp : touchedElements){
				if(element.equals(temp)) return false;
				else if(!element.varietyEquals(temp)) return false;
				else if(element.near(temp)) return true;
			}
			return false;
		}
	}
	public void addTouchElement(Element element){
		if(validateTouchedElement(element)){
			if(bombElements.contains(element)){
				bombElements.remove(element);
			}
			touchedElements.add(element);
			if(element.getType() instanceof Bomb)
				bomb(element);
		}
		invalidate();
	}
	public void doubleTouch(Element element){
		addTouchElement(element);
		for(Element temp : nearElements(element)){
			if(element.varietyEquals(temp) && !touchedElements.contains(temp)){
				doubleTouch(temp);
			}
		}
	}
	public void handleTouch(){
		if(touchedElements.size() > 1){
			dispearAnimate();
		}else{
			clearTouch();
			invalidate();
		}
	}
	public void dispearAnimate(){
		if(dispearAnimationTimer == null && dispearAnimationTimerTask == null){
			if(bombElements.isEmpty())
				parentActivity.touchMusic();
			else
				parentActivity.bombMusic();
			dispearAnimationTimer = new Timer();
			dispearAnimationTimerTask = new TimerTask() {

				@Override
				public void run() {
					dispearAnimationHandler.sendMessage(new Message());
				}
			};
			dispearAnimationTimer.schedule(dispearAnimationTimerTask, 0, 10);
		}
	}
	public void handleDispearAnimation(){
		if(dispearPadding < dispearMaxPadding) dispearPadding++;
		else{
			cancelTouchAnimate();
			handleDrop();
		}
		invalidate();
	}
	public void cancelTouchAnimate(){
		if(dispearAnimationTimer != null && dispearAnimationTimerTask != null){
			dispearPadding = dispearMinPadding;
			dispearAnimationTimer.cancel();
			dispearAnimationTimerTask.cancel();
			dispearAnimationTimer = null;
			dispearAnimationTimerTask = null;
		}
	}
	public void clearTouch(){
		bombElements.clear();
		touchedElements.clear();
	}
	public int touchScore(int elementNum){
		return elementNum * 10 + elementNum*elementNum - elementNum;
	}
	public void bomb(Element element){
		if(element.getType() instanceof Bomb){
			Bomb bomb = (Bomb)element.getType();
			BombType bombType = bomb.getType();
			Variety variety = bomb.getVariety();
			int oX = element.getX();
			int oY = element.getY();
			switch(bombType){
			case AREA:
				for(int i = oX - 1 ; i <= oX + 1 ; i++){
					for(int j = oY - 1 ; j <= oY + 1 ; j++){
						if(i >= 0 && i < xCnt && j >= 0 && j < yCnt){
							Element temp = virtualCoorElement(i, j);
							addBombElement(temp);
						}
					}
				}
				break;
			case L:
				for(int i = 0 ; i < xCnt ; i++){
					Element temp = virtualCoorElement(i, oY);
					addBombElement(temp);
				}
				break;
			case T:
				for(int j = 0 ; j < yCnt ; j++){
					Element temp = virtualCoorElement(oX, j);
					addBombElement(temp);
				}
				break;
			case CROSS:
				for(int i = 0 ; i < xCnt ; i++){
					Element temp = virtualCoorElement(i, oY);
					addBombElement(temp);
				}
				for(int j = 0 ; j < yCnt ; j++){
					Element temp = virtualCoorElement(oX, j);
					addBombElement(temp);
				}
				break;
			case LT:
				while(oX > 0 && oY > 0){
					oX--;oY--;
				}
				while(oX < xCnt && oY < yCnt){
					Element temp = virtualCoorElement(oX++, oY++);
					addBombElement(temp);
				}
				break;
			case LB:
				while(oX > 0 && oY < yCnt - 1){
					oX--;oY++;
				}
				while(oX < xCnt && oY >= 0){
					Element temp = virtualCoorElement(oX++, oY--);
					addBombElement(temp);
				}
				break;
			case X:
				int tX = oX;
				int tY = oY;
				while(oX > 0 && oY < yCnt - 1){
					oX--;oY++;
				}
				while(oX < xCnt && oY >= 0){
					Element temp = virtualCoorElement(oX++, oY--);
					addBombElement(temp);
				}
				while(tX > 0 && tY > 0){
					tX--;tY--;
				}
				while(tX < xCnt && tY < yCnt){
					Element temp = virtualCoorElement(tX++, tY++);
					addBombElement(temp);
				}
				break;
			case ALL:
				addBombElement(element);
				for(Element temp : elements){
					if(temp.getType().getVariety() == variety)
						addBombElement(temp);
				}
			}
		}
	}
	public void addBombElement(Element element){
		if(!bombElements.contains(element) && !touchedElements.contains(element)){
			bombElements.add(element);
			if(element.getType() instanceof Bomb)
				bomb(element);
		}
	}
	public int bombScore(int elementNum){
		return 2 * (elementNum * 10 + elementNum*elementNum - elementNum);
	}
	public void handleDrop(){
		handleTouchedElements();
		handleBombElement();
		ArrayList<Element> handleElements = new ArrayList<Element>();
		for(Element temp : touchedElements)
			handleElements.add(temp);
		for(Element temp : bombElements)
			handleElements.add(temp);
		parentActivity.addScore(touchScore(touchedElements.size()));
		parentActivity.addScore(bombScore(bombElements.size()));
		clearTouch();
		for(Element temp : handleElements){
			int maxDropOffset = 0;
			for(Element temp2 : preYElements(temp)){
				temp2.setY(temp2.getY() + 1);
				if(temp2.getDropOffset() > maxDropOffset)
					maxDropOffset = temp2.getDropOffset();
				temp2.setDropOffset(temp2.getDropOffset() + cellsize);
			}
			temp.setY(0);
			temp.setDropOffset(maxDropOffset + cellsize);
		}
		dropAnimate();
	}
	public void dropAnimate(){
		if(dropAnimationTimer == null && dropAnimationTimerTask == null){
			dropAnimationTimer = new Timer();
			dropAnimationTimerTask = new TimerTask() {

				@Override
				public void run() {
					dropAnimationHandler.sendMessage(new Message());
				}
			};
			dropAnimationTimer.schedule(dropAnimationTimerTask, 0, 10);
		}
	}
	public void handleDropAnimation(){
		boolean done = true;
		for(Element temp : elements){
			int n = temp.getDropOffset();
			if(n > 5){
				done = false;
				temp.setDropOffset((int) (n*0.95)-5);
			}
			else if(n > 0){
				temp.setDropOffset(0);
			}
		}
		invalidate();
		if(done){
			cancelDropAnimate();
		}
	}
	public void cancelDropAnimate(){
		if(dropAnimationTimer != null && dropAnimationTimerTask != null){
			dropAnimationTimer.cancel();
			dropAnimationTimerTask.cancel();
			dropAnimationTimer = null;
			dropAnimationTimerTask = null;
		}
	}
	public void handleTouchedElements(){
		Random random = new Random();
		Variety variety = touchedElements.get(0).getType().getVariety();
		int xmin=10, xmax=0;
		int ymin=10, ymax=0;
		for(int i = 0; i < touchedElements.size(); i++ ){
			Element temp = touchedElements.get(i);
			if(temp.getX()<xmin) xmin = temp.getX();
			if(temp.getX()>xmax) xmax = temp.getX();
			if(temp.getY()<ymin) ymin = temp.getY();
			if(temp.getY()>ymax) ymax = temp.getY();
			temp.setType(ElementType.random());
		}
		int xrange = xmax-xmin+1;
		int yrange = ymax-ymin+1;
		boolean map[][] = new boolean[xrange][yrange];
		for(Element temp : touchedElements)
			map[temp.getX()-xmin][temp.getY()-ymin]=true;
		
		boolean bombL = false, bombT = false, bombArea = false,bombLT = false, bombLB = false, bombAll = false;
		
		for(int i = 0; i<xrange;++i)
		for(int j = 0; j<yrange;++j){
			if(map[i][j]){
				if(i+3<xrange && map[i+1][j] && map[i+2][j] && map[i+3][j])
					bombT = true;
				if(j+3<yrange && map[i][j+1] && map[i][j+2] && map[i][j+3])
					bombL = true;
				if(i+3<xrange && j+3<yrange && map[i+1][j+1] && map[i+2][j+2] && map[i+3][j+3])
					bombLB = true;
				if(i-3>-1 && j+3<yrange && map[i-1][j+1] && map[i-2][j+2] && map[i-3][j+3])
					  bombLT = true;
				if(i+1<xrange && j+1<yrange && map[i+1][j] && map[i][j+1] && map[i+1][j+1])
					bombArea = true;
			}
		}
		
		int num = touchedElements.size();
		bombAll = num >= 10;
		
		boolean bombCross = bombL && bombT, bombX = bombLT && bombLB;
		bombL = bombL && !bombCross;
		bombT = bombT && !bombCross;
		bombLT = bombLT && !bombX;
		bombLB = bombLB && !bombX;

		if(bombL){
			int r = random.nextInt(num);
			Element e = touchedElements.get(r);;
			Bomb bomb = new Bomb(variety, BombType.L);
			e.setType(bomb);
		}
		if(bombT){
			int r = random.nextInt(num);
			Element e = touchedElements.get(r);
			Bomb bomb = new Bomb(variety, BombType.T);
			e.setType(bomb);
		}
		if(bombLT){
			int r = random.nextInt(num);
			Element e = touchedElements.get(r);
			Bomb bomb = new Bomb(variety, BombType.LT);
			e.setType(bomb);
		}
		if(bombLB){
			int r = random.nextInt(num);
			Element e = touchedElements.get(r);
			Bomb bomb = new Bomb(variety, BombType.LB);
			e.setType(bomb);
		}
		if(bombAll){
			int r = random.nextInt(num);
			Element e = touchedElements.get(r);
			Bomb bomb = new Bomb(variety, BombType.ALL);
			e.setType(bomb);
		}
		if(bombArea){
			int r = random.nextInt(num);
			Element e = touchedElements.get(r);
			Bomb bomb = new Bomb(variety, BombType.AREA);
			e.setType(bomb);
		}
		if(bombCross){
			int r = random.nextInt(num);
			Element e = touchedElements.get(r);
			Bomb bomb = new Bomb(variety, BombType.CROSS);
			e.setType(bomb);
		}
		if(bombX){
			int r = random.nextInt(num);
			Element e = touchedElements.get(r);
			Bomb bomb = new Bomb(variety, BombType.X);
			e.setType(bomb);
		}
		for(Element temp : touchedElements){
			System.out.println(temp.getX() + " " +temp.getY());
		}
	}
	public void handleBombElement(){
		for(Element element : bombElements){
			element.setType(ElementType.random());
		}
	}
	public void refresh(){
		elements.clear();
		for(int i = 0 ; i < xCnt ; i++){
			for(int j = 0 ; j < yCnt ; j++){
				elements.add(Element.random(i,j));
			}
		}
		invalidate();
	}

}