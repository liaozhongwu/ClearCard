package clearcard;


import clearcard.button.ButtonTouchListener;
import clearcard.button.RankButton;
import clearcard.button.SetupButton;
import clearcard.button.StartButton;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Toast;

import com.example.clearcard.R;
import com.umeng.analytics.MobclickAgent;

public class MenuActivity extends Activity{
	
	private StartButton button_start;
	private RankButton button_rank;
	private SetupButton button_setup;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MobclickAgent.updateOnlineConfig(this);
		setContentView(R.layout.menu);
		
		button_start = (StartButton)findViewById(R.id.Button_start);
		button_rank = (RankButton)findViewById(R.id.Button_rank);
		button_setup = (SetupButton)findViewById(R.id.Button_setup);

		button_start.setOnTouchListener(new ButtonTouchListener(button_start) {
			
			@Override
			public void action() {
				Intent intent = new Intent();
				intent.setClass(MenuActivity.this, GameActivity.class);
				startActivity(intent);
				MenuActivity.this.finish();
			}
		});
		button_rank.setOnTouchListener(new ButtonTouchListener(button_rank) {
			
			@Override
			public void action() {
				Intent intent = new Intent();
				intent.setClass(MenuActivity.this, RankActivity.class);
				startActivity(intent);
				MenuActivity.this.finish();
			}
		});
		button_setup.setOnTouchListener(new ButtonTouchListener(button_setup) {
			
			@Override
			public void action() {
				Toast.makeText(MenuActivity.this, "抱歉，暂不支持游戏设置", Toast.LENGTH_SHORT).show();
			}
		});
	}
	@Override
	protected void onPause() {
		MobclickAgent.onResume(this);
		super.onPause();
	}
	@Override
	protected void onResume() {
		MobclickAgent.onPause(this);
		super.onResume();
	}
	private long lastExitTime;
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			long currtime = System.currentTimeMillis();
			if(currtime - lastExitTime > 3000){
				lastExitTime = currtime;
				Toast.makeText(MenuActivity.this, "再按一次返回退出游戏", Toast.LENGTH_SHORT).show();
				return false;
			}	
		}
		return super.onKeyDown(keyCode, event);
	}
}
