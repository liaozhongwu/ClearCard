package clearcard.game.handler;

import clearcard.game.GameState;
import android.os.Handler;
import android.os.Message;

public class TimerHandler extends Handler{
	GameState game;
	
	public TimerHandler(GameState game){
		this.game = game;
	}
	@Override
	public void handleMessage(Message msg){
		super.handleMessage(msg);
		game.handleTimer();
	}
}