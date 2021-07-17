package tonyu.kernel;

import javax.microedition.khronos.opengles.GL11;

import android.graphics.Canvas;
import android.graphics.Color;
import android.opengl.GLES20;

public class TonyuSprite extends TonyuObject {

	public void drawMethod(TonyuSprite sprite) {
		TGL.tonyuDrawer.drawMethod(sprite, 0);
	}
	public void drawMethod(TonyuSprite sprite, int zOrder) {
		TGL.tonyuDrawer.drawMethod(sprite, zOrder);
	}
	
	public void drawSprite(float x, float y, int p) {
		TGL.tonyuDrawer.drawSprite(x, y, p, 0, 0);
	}
	public void drawSprite(float x, float y, int p, int f) {
		TGL.tonyuDrawer.drawSprite(x, y, p, f, 0);
	}
	public void drawSprite(float x, float y, int p, int f, int zOrder) {
		TGL.tonyuDrawer.drawSprite(x, y, p, f, zOrder);
	}
	
	public void drawSpriteLT(float x, float y, int p) {
		TGL.tonyuDrawer.drawSpriteLT(x, y, p, 0, 0);
	}
	public void drawSpriteLT(float x, float y, int p, int f) {
		TGL.tonyuDrawer.drawSpriteLT(x, y, p, f, 0);
	}
	public void drawSpriteLT(float x, float y, int p, int f, int zOrder) {
		TGL.tonyuDrawer.drawSpriteLT(x, y, p, f, zOrder);
	}
	

	public void drawDxSprite(float x, float y, int p) {
		TGL.tonyuDrawer.drawDxSprite(x, y, p, 0, 0, 0, 255, 1, 0);
	}
	public void drawDxSprite(float x, float y, int p, int f) {
		TGL.tonyuDrawer.drawDxSprite(x, y, p, f, 0, 0, 255, 1, 0);
	}
	public void drawDxSprite(float x, float y, int p, int f, int zOrder) {
		TGL.tonyuDrawer.drawDxSprite(x, y, p, f, zOrder, 0, 255, 1, 0);
	}
	public void drawDxSprite(float x, float y, int p, int f, int zOrder, float angle) {
		TGL.tonyuDrawer.drawDxSprite(x, y, p, f, zOrder, angle, 255, 1, 0);
	}
	public void drawDxSprite(float x, float y, int p, int f, int zOrder, float angle, float alpha) {
		TGL.tonyuDrawer.drawDxSprite(x, y, p, f, zOrder, angle, alpha, 1, 0);
	}
	public void drawDxSprite(float x, float y, int p, int f, int zOrder, float angle, float alpha, float scaleX) {
		TGL.tonyuDrawer.drawDxSprite(x, y, p, f, zOrder, angle, alpha, scaleX, 0);
	}
	public void drawDxSprite(float x, float y, int p, int f, int zOrder, float angle, float alpha, float scaleX, float scaleY) {
		TGL.tonyuDrawer.drawDxSprite(x, y, p, f, zOrder, angle, alpha, scaleX, scaleY);
	}


	public void drawText(float x, float y, String text) {
		TGL.tonyuDrawer.drawText(x, y, text, Color.WHITE, 12, 0);
	}
	public void drawText(float x, float y, String text, int color) {
		TGL.tonyuDrawer.drawText(x, y, text, color, 12, 0);
	}
	public void drawText(float x, float y, String text, int color, float size) {
		TGL.tonyuDrawer.drawText(x, y, text, color, size, 0);
	}
	public void drawText(float x, float y, String text, int color, float size, int zOrder) {
		TGL.tonyuDrawer.drawText(x, y, text, color, size, zOrder);
	}
	
	
	public void drawTextCC(float x, float y, String text) {
		TGL.tonyuDrawer.drawTextCC(x, y, text, Color.WHITE, 12, 0);
	}
	public void drawTextCC(float x, float y, String text, int color) {
		TGL.tonyuDrawer.drawTextCC(x, y, text, color, 12, 0);
	}
	public void drawTextCC(float x, float y, String text, int color, float size) {
		TGL.tonyuDrawer.drawTextCC(x, y, text, color, size, 0);
	}
	public void drawTextCC(float x, float y, String text, int color, float size, int zOrder) {
		TGL.tonyuDrawer.drawTextCC(x, y, text, color, size, zOrder);
	}

	public void drawNumber(float x, float y, int zeroP, int num) {
		drawNumber(x, y, zeroP, num, Color.WHITE, 0);
	}
	public void drawNumber(float x, float y, int zeroP, int num, int color) {
		drawNumber(x, y, zeroP, num, color, 0);
	}
	public void drawNumber(float x, float y, int zeroP, int num, int color, int zOrder) {
		int s = 1, tmp, point = 1;
		float xx = 0;
		for (tmp = num; ; s++) { // 何桁か調べる
			tmp /= 10; // 作業中の数字(10で割っていく)
			if (tmp == 0) break;
			point *= 10;
		}
		for (int i=0; i<s; i++) {
			tmp = num / point % 10; // 取り出した１桁分の数字
			TGL.tonyuDrawer.drawSpriteLT(x + xx, y, zeroP + tmp, color, 0, zOrder);
			xx += getPatWidth(zeroP + tmp); // 10進数のシフト(<<, >> は2進数のシフトになってしまう)
			point /= 10;
		}
	}

	public void drawDxNumber(float x, float y, int zeroP, int num) {
		drawNumber(x, y, zeroP, num, Color.WHITE, 0);
	}
	public void drawDxNumber(float x, float y, int zeroP, int num, int color) {
		drawNumber(x, y, zeroP, num, color, 0);
	}
	public void drawDxNumber(float x, float y, int zeroP, int num, int color, int zOrder) {
		drawNumber(x, y, zeroP, num, color, zOrder);
	}
	public void drawDxNumber(float x, float y, int zeroP, int num, int color, int zOrder, float alpha) {
		drawDxNumber(x, y, zeroP, num, color, zOrder, alpha, 1);
	}
	public void drawDxNumber(float x, float y, int zeroP, int num, int color, int zOrder, float alpha, float scale) {
		int s = 1, tmp, point = 1;
		float xx = 0;
		for (tmp = num; ; s++) { // 何桁か調べる
			tmp /= 10; // 作業中の数字(10で割っていく)
			if (tmp == 0) break;
			point *= 10;
		}
		xx += getPatWidth(tmp) * scale / 2;
		for (int i=0; i<s; i++) {
			tmp = num / point % 10; // 取り出した１桁分の数字
			TGL.tonyuDrawer.drawDxSprite(x + xx, y + getPatHeight(tmp), zeroP + tmp, color, 0, zOrder, 0, alpha, scale, scale);
			xx += getPatWidth(zeroP + tmp) * scale; // 10進数のシフト(<<, >> は2進数のシフトになってしまう)
			point /= 10;
		}
	}
	

	public void drawLine(float sx, float sy, float dx, float dy, int color) {
		TGL.tonyuDrawer.drawLine(sx, sy, dx, dy, color, 0);
	}
	public void drawLine(float sx, float sy, float dx, float dy, int color, int zOrder) {
		TGL.tonyuDrawer.drawLine(sx, sy, dx, dy, color, zOrder);
	}
	

	public void fillRect(float sx, float sy, float dx, float dy, int color) {
		TGL.tonyuDrawer.fillRect(sx, sy, dx, dy, color, 0);
	}
	public void fillRect(float sx, float sy, float dx, float dy, int color, int zOrder) {
		TGL.tonyuDrawer.fillRect(sx, sy, dx, dy, color, zOrder);
	}
	
	
	// 画像の開始番号を取得
	public int getPatNo(String bitmapName) {
		return TGL.grManager.getPatNo(bitmapName);
	}
	// 画像のチップ数を取得
	public int getPatChipSize(String bitmapName) {
		return TGL.grManager.getPatChipSize(bitmapName);
	}
	// 全ての画像チップの数を取得
	public int getChipSize() {
		return TGL.grManager.getChipSize();
	}
	

	// 画像の幅
	public int getPatWidth(int p) {
		TonyuGraphicsChip gChip = TGL.grManager.getGraphicsChip(p);
		if (gChip != null) return gChip.width;
		else               return 0;
	}
	// 画像の高さ
	public int getPatHeight(int p) {
		TonyuGraphicsChip gChip = TGL.grManager.getGraphicsChip(p);
		if (gChip != null) return gChip.height;
		else               return 0;
	}
	
	// 描画のタイミングで呼ばれる　引数のcanvas,glを使って描画できる
	// このメソッドはオーバーライドして使う
	protected void directDrawCanvas(Canvas canvas) {} // 描画用のキャンバスをそのまま渡す
	protected void directDrawGL11(GL11 gl) {}         // 描画用のGLES11をそのまま渡す
	protected void directDrawGL20(GLES20 gl) {}       // 描画用のGLES20をそのまま渡す
}
