package clearcard;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.TextView;
import android.widget.Toast;
import clearcard.button.ButtonTouchListener;
import clearcard.button.HomeButton;
import clearcard.button.RefreshButton;
import clearcard.button.StateButton;
import clearcard.game.Board;
import clearcard.game.GameState;

import com.example.clearcard.R;
import com.umeng.analytics.MobclickAgent;

public class GameActivity extends Activity{

	private int score;
	private GameState gameState;
	private Board board;
	private TextView textView_score;
	private HomeButton button_home;
	private RefreshButton button_refresh;
	private StateButton button_state;
	private SoundPool soundPool;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game);
		gameState = (GameState)findViewById(R.id.gameState1);
		board = (Board)findViewById(R.id.board);
		board.setParentActivity(GameActivity.this);
		textView_score = (TextView)findViewById(R.id.TextView_score);
		button_home = (HomeButton)findViewById(R.id.Button_home);
		button_refresh = (RefreshButton)findViewById(R.id.Button_refresh);
		button_state = (StateButton)findViewById(R.id.Button_state);
		soundPool= new SoundPool(3,AudioManager.STREAM_MUSIC,0);
		soundPool.load(this,R.raw.touch,1);
		soundPool.load(this,R.raw.bomb,1);
		setOperationListener();
		start();
	}
	@Override
	protected void onPause() {
		pause();
		MobclickAgent.onResume(this);
		super.onPause();
	}
	@Override
	protected void onResume() {
		MobclickAgent.onPause(this);
		super.onResume();
	}
	public void start(){
		gameState.start();
	}
	public void refresh(){
		score = 0;
		textView_score.setText(Integer.toString(score));
		gameState.refresh();
		board.refresh();
	}
	private boolean confirmPause;
	public void showConfirmDialog(){
		confirmPause = !gameState.isPause();
		if(confirmPause)
			gameState.pause();
	}
	public void hideConfirmDialog(){
		if(confirmPause)
			gameState.goon();
	}
	public void pause(){
		gameState.pause();
		button_state.pause();
		board.lock();
	}
	public void goon(){
		gameState.goon();
		button_state.goon();
		board.unlock();
	}
	public void over(){
		Intent intent = new Intent();
		intent.putExtra("score", score);
		intent.setClass(GameActivity.this, ScoreActivity.class);
		startActivity(intent);
		GameActivity.this.finish();
	}
	public void addScore(int addScore){
		score += addScore;
		textView_score.setText(""+score);
	}
	public void setOperationListener(){
		button_home.setOnTouchListener(new ButtonTouchListener(button_home) {
			
			@Override
			public void action() {
				showConfirmDialog();
				new AlertDialog.Builder(GameActivity.this)
				.setTitle("提示")
				.setMessage("当前进度将被删除，确定要回到主菜单吗？")
				.setPositiveButton("确定", new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						Intent intent = new Intent();
						intent.setClass(GameActivity.this, MenuActivity.class);
						startActivity(intent);
						GameActivity.this.finish();
					}
				})
				.setNegativeButton("取消", new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						hideConfirmDialog();
					}
				})
				.show();
			}
		});
		button_refresh.setOnTouchListener(new ButtonTouchListener(button_refresh) {
			
			@Override
			public void action() {
				showConfirmDialog();
				new AlertDialog.Builder(GameActivity.this)
				.setTitle("提示")
				.setMessage("当前进度将被删除，确定要重新开始吗？")
				.setPositiveButton("确定", new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						refresh();
						hideConfirmDialog();
					}
				})
				.setNegativeButton("取消", new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						hideConfirmDialog();
					}
				})
				.show();
			}
		});
		button_state.setOnTouchListener(new ButtonTouchListener(button_state) {
			
			@Override
			public void action() {
				if(gameState.isPause()){
					goon();
					button_state.goon();
				}else if(gameState.isNormal()){
					pause();
					button_state.pause();
				}
			}
		});
	}
	public void touchMusic(){
		soundPool.play(1, 1, 1, 0, 0, 1);
	}
	public void bombMusic(){
		soundPool.play(2, 1, 1, 0, 0, 1);
	}
	private long lastExitTime;
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			long currtime = System.currentTimeMillis();
			if(currtime - lastExitTime > 3000){
				lastExitTime = currtime;
				Toast.makeText(GameActivity.this, "再按一次返回退出游戏", Toast.LENGTH_SHORT).show();
				return false;
			}	
		}
		return super.onKeyDown(keyCode, event);
	}
}