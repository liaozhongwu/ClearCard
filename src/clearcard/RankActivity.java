package clearcard;

import clearcard.button.ButtonTouchListener;
import clearcard.button.HomeButton;
import clearcard.button.ShareButton;
import clearcard.button.StartButton;
import clearcard.game.AppRenRen;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.TextView;
import android.widget.Toast;

import com.example.clearcard.R;
import com.umeng.analytics.MobclickAgent;

public class RankActivity extends Activity{
	private TextView textView_rank_first;
	private TextView textView_rank_second;
	private TextView textView_rank_third;
	private TextView textView_rank_fourth;
	private int[] rankScores;
	private int rankFirstScore;
	private int rankSecondScore;
	private int rankThirdScore;
	private int rankFourthScore;
	private SharedPreferences sharedPreferences;
	private ShareButton Button_share;
	private StartButton Button_start;
	private HomeButton Button_home;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rank);
		textView_rank_first = (TextView)findViewById(R.id.TextView_rank_first);
		textView_rank_second = (TextView)findViewById(R.id.TextView_rank_second);
		textView_rank_third = (TextView)findViewById(R.id.TextView_rank_third);
		textView_rank_fourth = (TextView)findViewById(R.id.TextView_rank_fourth);
		
		sharedPreferences = getSharedPreferences("rank", Context.MODE_PRIVATE);
		rankFirstScore = sharedPreferences.getInt("rankFirst", 0);
		rankSecondScore = sharedPreferences.getInt("rankSecond", 0);
		rankThirdScore = sharedPreferences.getInt("rankThird", 0);
		rankFourthScore = sharedPreferences.getInt("rankFourth", 0);
		rankScores = new int[]{rankFirstScore,rankSecondScore,rankThirdScore,rankFourthScore};
		
		textView_rank_first.setText(Integer.toString(rankFirstScore));
		textView_rank_second.setText(Integer.toString(rankSecondScore));
		textView_rank_third.setText(Integer.toString(rankThirdScore));
		textView_rank_fourth.setText(Integer.toString(rankFourthScore));
		
		Button_share = (ShareButton)findViewById(R.id.Button_share);
		Button_share.setOnTouchListener(new ButtonTouchListener(Button_share) {
			
			@Override
			public void action() {
				AppRenRen.sendStatusOfRank(RankActivity.this, rankScores);
			}
		});
		Button_start = (StartButton)findViewById(R.id.Button_start);
		Button_start.setOnTouchListener(new ButtonTouchListener(Button_start) {
			
			@Override
			public void action() {
				Intent intent = new Intent();
				intent.setClass(RankActivity.this, GameActivity.class);
				startActivity(intent);
				RankActivity.this.finish();
			}
		});
		Button_home = (HomeButton)findViewById(R.id.Button_home);
		Button_home.setOnTouchListener(new ButtonTouchListener(Button_home) {
			
			@Override
			public void action() {
				Intent intent = new Intent();
				intent.setClass(RankActivity.this, MenuActivity.class);
				startActivity(intent);
				RankActivity.this.finish();
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
				Toast.makeText(RankActivity.this, "再按一次返回退出游戏", Toast.LENGTH_SHORT).show();
				return false;
			}	
		}
		return super.onKeyDown(keyCode, event);
	}
}
