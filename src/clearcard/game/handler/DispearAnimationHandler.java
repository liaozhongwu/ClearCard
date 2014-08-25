package clearcard.game.handler;

import android.os.Handler;
import android.os.Message;
import clearcard.game.Board;

public class DispearAnimationHandler extends Handler{
    	Board board;
    	public DispearAnimationHandler(Board board){
    		this.board = board;
    	}
    	@Override
		public void handleMessage(Message msg){
			super.handleMessage(msg);
			board.handleDispearAnimation();
		}
    }