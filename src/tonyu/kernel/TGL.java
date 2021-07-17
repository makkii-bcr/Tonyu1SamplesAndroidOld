package tonyu.kernel;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL11;

import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;

/** グローバル変数一覧。Tonyuのグローバル変数の先頭に付く$の変わり */
public class TGL {

	public static int drawMode;
	public static int activityState;
	
	public static boolean _doDraw;
	
	public static int frameCount;
	public static float padX;
	public static float padY;
	public static int padPush;
	public static int padPushCnt;
	public static int screenWidth;
	public static int screenHeight;
	public static int displayWidth;
	public static int displayHeight;
	public static int marginWidth;
	public static int marginHeight;
	public static int marginHalfWidth;
	public static int marginHalfHeight;
	public static float widthR;
	public static float heightR;
	public static float viewX;
	public static float viewY;

	public static Context context;
	static Resources res;
	static TonyuBoot tonyuBoot;
	static TonyuDrawer tonyuDrawer;
	public static Handler activityHandler;
	public static int whiteTexNo;
	
	public static TonyuGraphicsManager grManager;
	public static TonyuMediaPlayer mplayer;
	public static TonyuMap map;
	public static TonyuProjectManager projectManager;
	public static TonyuScreenManager screenManager;
	public static TonyuKeyboardManager keyManager;
	public static TonyuOutput output;
	public static TonyuSelectBox selectBox;
	public static TonyuSystem system;
	
	public static ArrayList<TonyuPlainChar> chars;
	public static GL11 gl11;
}
