package clearcard.game.handler;

import android.os.Handler;
import android.os.Message;
import clearcard.game.Board;

public class DropAnimationHandler extends Handler{
    	Board board;
    	public DropAnimationHandler(Board board){
    		this.board = board;
    	}
    	@Override
		public void handleMessage(Message msg){
			super.handleMessage(msg);
			board.handleDropAnimation();
		}
    }