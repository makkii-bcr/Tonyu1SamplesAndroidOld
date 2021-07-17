package tonyu.android;

import tonyu.kernel.TonyuBoot;
import android.content.Context;
import android.view.KeyEvent;

public interface TonyuView {
	public abstract void onActivityPause();
	public abstract void onActivityStop();
	public abstract void onActivityResume();
	public abstract void onActivityDestroy(int exitSW);
	public abstract boolean dispatchKeyEvent(KeyEvent event);
	
	public abstract int getActivityState();
	public abstract TonyuViewDisplay getScreen();
	public abstract Context getContext();
	public abstract TonyuBoot getTonyuBoot();
}
