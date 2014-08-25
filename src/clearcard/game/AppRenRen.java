package clearcard.game;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.renn.rennsdk.RennClient;
import com.renn.rennsdk.RennClient.LoginListener;
import com.renn.rennsdk.RennExecutor.CallBack;
import com.renn.rennsdk.RennResponse;
import com.renn.rennsdk.exception.RennException;
import com.renn.rennsdk.param.PutStatusParam;

public class AppRenRen{
	private final static String APP_ID = "267559";
	private final static String API_KEY = "f3b40ae6ac7341c697b4a262c25d12c5"; 
	private final static String SECRET_KEY = "0211dbb2ebaf404cb60f9ef31b8e91bd";

	private static RennClient rennClient;

	public static void sendStatusOfScore(final Activity activity,final int score){
		String content = "我在纸牌消除游戏中获得了"+score+"分";
		prepareStatus(activity, content);
	}
	public static void sendStatusOfRank(final Activity activity,final int[] ranks){
		StringBuilder content = new StringBuilder();
		content.append("我的纸牌消除游戏排行榜:");
		for(int i = 0;i < ranks.length;i++){
			content.append('(');
			content.append(i);
			content.append(')');
			content.append(ranks[i]);
		}
		content.append("你能超越吗?");
		prepareStatus(activity, content.toString());
	}
	private static boolean isNetworkConnected(Context context) { 
		if (context != null) { 
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context 
			.getSystemService(Context.CONNECTIVITY_SERVICE); 
			NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo(); 
			if (mNetworkInfo != null) { 
				return mNetworkInfo.isAvailable(); 
			} 
		} 
		return false; 
	}
	private static void init(final Activity activity){
		rennClient = RennClient.getInstance(activity);
		rennClient.init(APP_ID, API_KEY, SECRET_KEY);
		rennClient.setScope("status_update");
		rennClient.setTokenType("bearer");
	}
	private static void prepareStatus(final Activity activity,final String content){
		if(isNetworkConnected(activity)){
			if(rennClient == null) init(activity);
			if(rennClient.isLogin()){
				sendStatus(activity, content);
			}else{
				rennClient.setLoginListener(new LoginListener() {
					@Override
					public void onLoginSuccess() {
						Toast.makeText(activity, "登录成功", Toast.LENGTH_SHORT).show();
						sendStatus(activity, content);
					}
		
					@Override
					public void onLoginCanceled() {
						
					}
				});
				rennClient.login(activity);
			}	
		}
		else{
			Toast.makeText(activity, "网络连接失败,请检查您的网络连接", Toast.LENGTH_SHORT).show();
		}
	}
	private static void sendStatus(final Activity activity,final String content){
		PutStatusParam putStatusParam = new PutStatusParam();
        putStatusParam.setContent(content);
        try {
            rennClient.getRennService().sendAsynRequest(putStatusParam, new CallBack() {    
                
                @Override
                public void onSuccess(RennResponse response) {
                    Toast.makeText(activity, "状态发布成功", Toast.LENGTH_SHORT).show();            
                }
                
                @Override
                public void onFailed(String errorCode, String errorMessage) {
                    Toast.makeText(activity, "状态发布失败", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (RennException e1) {
            e1.printStackTrace();
        }
	}
}
