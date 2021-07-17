package tonyu.kernel;

import android.os.Message;
import android.widget.Toast;

public class TonyuOutput implements Runnable {
	private Message msg;
	public TonyuOutput() {
		msg = Message.obtain();
	}


	// トーストメッセージ(短い時間表示)
	public void printTS(String s) {
		msg = Message.obtain();
	    //msg.what = TonyuActivityHandler.TOASTMESSAGE;
	    msg.obj = s;
	    msg.arg1 = Toast.LENGTH_SHORT;
	    TGL.activityHandler.post(this);
	}
	// トーストメッセージ(長い時間表示)
	public void printT(String s) {
		msg = Message.obtain();
	    //msg.what = TonyuActivityHandler.TOASTMESSAGE;
	    msg.obj = s;
	    msg.arg1 = Toast.LENGTH_LONG;
	    TGL.activityHandler.post(this);
	}

	@Override
	public void run() {
		Toast toast = Toast.makeText(TGL.context, (String)msg.obj, msg.arg1);
		toast.show();
	}
	
	
	
	
}
