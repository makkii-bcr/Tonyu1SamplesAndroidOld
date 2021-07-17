package tonyu.kernel;

public interface TonyuDrawer {
	public void drawMethod(TonyuSprite sprite, int zOrder);
	public void drawSprite(float x, float y, int p, int f, int zOrder);
	public void drawSpriteLT(float x, float y, int p, int f, int zOrder);
	public void drawSpriteLT(float x, float y, TonyuGraphicsChip gChip, int color, int f, int zOrder);
	public void drawDxSprite(float x, float y, int p, int f, int zOrder, float angle, float alpha, float scaleX, float scaleY);
	public void drawText(float x, float y, String text, int color, float size, int zOrder);
	public void drawTextCC(float x, float y, String text, int color, float size, int zOrder);
	public void drawLine(float sx, float sy, float dx, float dy, int color, int zOrder);
	public void fillRect(float sx, float sy, float dx, float dy, int color, int zOrder);
	
	public void allDraw(Object drawObj, boolean updateFrame);
	void drawSpriteLT(float x, float y, int p, int color, int f, int zOrder);
	void drawDxSprite(float x, float y, int p, int color, int f, int zOrder, float angle, float alpha, float scaleX, float scaleY);
}
