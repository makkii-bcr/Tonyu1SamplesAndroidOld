package tonyu.kernel;

import tonyu.android.TonyuActivity;
import android.app.Activity;


/** Tonyu Systemではありません！！ Tonyu Systemにある$Systemと同じようなものです */
public class TonyuSystem extends TonyuObject {

	public void exit() {
		mesExit mes = new mesExit();
		TGL.activityHandler.post(mes);
	}
	
	private class mesExit implements Runnable {
		@Override
		public synchronized void run() {
			((TonyuActivity)TGL.context).exitSW = 1;
			((Activity)TGL.context).finish();
		}
	}
}
