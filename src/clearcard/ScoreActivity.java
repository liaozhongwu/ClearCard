package clearcard;

import clearcard.button.ButtonTouchListener;
import clearcard.button.HomeButton;
import clearcard.button.RefreshButton;
import clearcard.button.ShareButton;
import clearcard.game.AppRenRen;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.TextView;
import android.widget.Toast;

import com.example.clearcard.R;
import com.umeng.analytics.MobclickAgent;

public class ScoreActivity extends Activity{

	private int score;
	private TextView textView_score;
	private TextView textView_score_desc;
	private TextView textView_rank_first;
	private TextView textView_rank_second;
	private TextView textView_rank_third;
	private TextView textView_rank_fourth;
	private int rankFirstScore;
	private int rankSecondScore;
	private int rankThirdScore;
	private int rankFourthScore;
	private SharedPreferences sharedPreferences;
	private Editor editor;
	private ShareButton Button_share;
	private RefreshButton Button_refresh;
	private HomeButton Button_home;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.score);
		Intent intent = this.getIntent();
		score = intent.getIntExtra("score", 0);
		
		textView_score = (TextView)findViewById(R.id.TextView_score);
		textView_score.setText(Integer.toString(score));
		textView_score_desc = (TextView)findViewById(R.id.TextView_score_desc);
		textView_rank_first = (TextView)findViewById(R.id.TextView_rank_first);
		textView_rank_second = (TextView)findViewById(R.id.TextView_rank_second);
		textView_rank_third = (TextView)findViewById(R.id.TextView_rank_third);
		textView_rank_fourth = (TextView)findViewById(R.id.TextView_rank_fourth);

		Button_share = (ShareButton)findViewById(R.id.Button_share);
		Button_share.setOnTouchListener(new ButtonTouchListener(Button_share) {
			
			@Override
			public void action() {
				AppRenRen.sendStatusOfScore(ScoreActivity.this, score);
			}
		});
		Button_refresh = (RefreshButton)findViewById(R.id.Button_refresh);
		Button_refresh.setOnTouchListener(new ButtonTouchListener(Button_refresh) {
			
			@Override
			public void action() {
				Intent intent = new Intent();
				intent.setClass(ScoreActivity.this, GameActivity.class);
				startActivity(intent);
				ScoreActivity.this.finish();
			}
		});
		Button_home = (HomeButton)findViewById(R.id.Button_home);
		Button_home.setOnTouchListener(new ButtonTouchListener(Button_home) {
			
			@Override
			public void action() {
				Intent intent = new Intent();
				intent.setClass(ScoreActivity.this, MenuActivity.class);
				startActivity(intent);
				ScoreActivity.this.finish();
			}
		});
		handleScore();
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
	public void handleScore(){
		sharedPreferences = getSharedPreferences("rank", Context.MODE_PRIVATE);
		editor = sharedPreferences.edit();
		rankFirstScore = sharedPreferences.getInt("rankFirst", 0);
		rankSecondScore = sharedPreferences.getInt("rankSecond", 0);
		rankThirdScore = sharedPreferences.getInt("rankThird", 0);
		rankFourthScore = sharedPreferences.getInt("rankFourth", 0);
		
		Log.i("score", rankFirstScore+","+rankSecondScore+","+rankThirdScore+","+rankFourthScore);
		if(score < rankFourthScore){
			textView_score_desc.setText(R.string.score_desc_none);
		}
		else if(score < rankThirdScore){
			textView_score_desc.setText(R.string.score_desc_fourth);
			textView_rank_fourth.setTextColor(getResources().getColor(R.color.diamond_color));
			rankFourthScore = score;
		}
		else if(score < rankSecondScore){
			textView_score_desc.setText(R.string.score_desc_third);
			textView_rank_third.setTextColor(getResources().getColor(R.color.club_color));
			rankFourthScore = rankThirdScore;
			rankThirdScore = score;
		}
		else if(score < rankFirstScore){
			textView_score_desc.setText(R.string.score_desc_second);
			textView_rank_second.setTextColor(getResources().getColor(R.color.heart_color));
			rankFourthScore = rankThirdScore;
			rankThirdScore = rankSecondScore;
			rankSecondScore = score;
		}
		else if(score >= rankFirstScore){
			textView_score_desc.setText(R.string.score_desc_first);
			textView_rank_first.setTextColor(getResources().getColor(R.color.spade_color));
			rankFourthScore = rankThirdScore;
			rankThirdScore = rankSecondScore;
			rankSecondScore = rankFirstScore;
			rankFirstScore = score;
		}
		textView_rank_first.setText(Integer.toString(rankFirstScore));
		textView_rank_second.setText(Integer.toString(rankSecondScore));
		textView_rank_third.setText(Integer.toString(rankThirdScore));
		textView_rank_fourth.setText(Integer.toString(rankFourthScore));
		Log.i("score", rankFirstScore+","+rankSecondScore+","+rankThirdScore+","+rankFourthScore);
		editor.putInt("rankFourth", rankFourthScore);
		editor.putInt("rankThird", rankThirdScore);
		editor.putInt("rankSecond", rankSecondScore);
		editor.putInt("rankFirst", rankFirstScore);
		editor.commit();
	}
	private long lastExitTime;
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			long currtime = System.currentTimeMillis();
			if(currtime - lastExitTime > 3000){
				lastExitTime = currtime;
				Toast.makeText(ScoreActivity.this, "再按一次返回退出游戏", Toast.LENGTH_SHORT).show();
				return false;
			}	
		}
		return super.onKeyDown(keyCode, event);
	}
}
