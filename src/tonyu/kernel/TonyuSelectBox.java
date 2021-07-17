package tonyu.kernel;

import android.app.AlertDialog;
import android.content.DialogInterface;

public class TonyuSelectBox {

	public SelectMsg open(String msg) {
		return open(msg, null, null, null, null);
	}
	public SelectMsg open(String msg, String title) {
		return open(msg, title, null, null, null);
	}
	public SelectMsg open(String msg, String title, String str1) {
		return open(msg, title, str1, null, null);
	}
	public SelectMsg open(String msg, String title, String str1, String str2) {	
		return open(msg, title, str1, str2, null);
	}
	public SelectMsg open(String msg, String title, String str1, String str2, String str3) {
		SelectMsg msgObj = new SelectMsg();
		msgObj.msg = msg;
		msgObj.title = title;
		msgObj.str1 = str1;
		msgObj.str2 = str2;
		msgObj.str3 = str3;
		TGL.activityHandler.post(msgObj);
		return msgObj;
	}
	
	public class SelectMsg implements Runnable {
		public String msg, title, str1, str2, str3, str4;
		public int msgStatus = -1;

		public int getStatus() {
			return msgStatus;
		}
		
		@Override
		public synchronized void run() {
			
			AlertDialog.Builder builder = new AlertDialog.Builder(TGL.context);
			if (title != null) builder.setTitle(title); else builder.setTitle("確認");
			builder.setMessage(msg);
	
			if (str1 == null && str2 == null && str3 == null) {
	
				builder.setPositiveButton("はい",
		                new DialogInterface.OnClickListener() {
		                    @Override
		                    public void onClick(DialogInterface dialog, int which) {
		                    	msgStatus = 1;
		                    }
		                });
				builder.setNegativeButton("いいえ",
		                new DialogInterface.OnClickListener() {
		                    @Override
		                    public void onClick(DialogInterface dialog, int which) {
		                    	msgStatus = 0;
		                    }
		                });
			}
			if (str1 != null) {
				builder.setPositiveButton(str1,
		                new DialogInterface.OnClickListener() {
		                    @Override
		                    public void onClick(DialogInterface dialog, int which) {
		                    	msgStatus = 1;
		                    }
		                });
			}
			if (str2 != null) {
				builder.setNegativeButton(str2,
		                new DialogInterface.OnClickListener() {
		                    @Override
		                    public void onClick(DialogInterface dialog, int which) {
		                    	msgStatus = 0;
		                    }
		                });
			}
			if (str3 != null) {
				builder.setNeutralButton(str3,
		                new DialogInterface.OnClickListener() {
		                    @Override
		                    public void onClick(DialogInterface dialog, int which) {
		                    	msgStatus = 2;
		                    }
		                });
			}
			builder.setCancelable(false); // キャンセル可能か？
	        AlertDialog alertDialog = builder.create();
	        alertDialog.show();
		}
	}
}
