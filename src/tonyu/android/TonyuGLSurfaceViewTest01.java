package tonyu.android;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.opengl.GLES10;
import android.opengl.GLES11;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.util.Log;
import android.view.SurfaceHolder;
// テスト０１
public class TonyuGLSurfaceViewTest01 extends GLSurfaceView implements SurfaceHolder.Callback, Runnable, GLSurfaceView.Renderer {
	private SurfaceHolder holder;
	public TonyuGLSurfaceViewTest01(Context context) {
		super(context);
        setRenderer(this);

        holder = getHolder();
        holder.addCallback(this); // callbackメソッド（下の３つ）を登録
	}

	private int cnt = 0;
	private int fpsCnt = 0;
	private int fps = 0;
	private long startTime = 0;
	private float xx = 0;
	private int yySW = 0;

	private int displayWidth  = 800;
	private int displayHeight = 480;
	private int screenWidth  = 640;
	private int screenHeight = 480;
	
	@Override
	public void onDrawFrame(GL10 gl) {
        // 描画内容をクリアする
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

        int cw = 16, ch = 16;
        int xs = 10, ys = 10;
        float[] vertices = new float[4 * 2 * xs*ys];
        int vertexIdx = 0;
        
	    for (int j=0; j<ys; j++) {
	    	for (int i=0; i<xs; i++) {
		        // 3点の座標を取ります
		        
		        // 左上
		        vertices[vertexIdx++] = xx+i*(cw+1);    // x
		        vertices[vertexIdx++] = j*(ch+1);       // y

		        // 右上
		        vertices[vertexIdx++] = xx+i*(cw+1)+cw;
		        vertices[vertexIdx++] = j*(ch+1);
		        
		        // 左下
		        vertices[vertexIdx++] = xx+i*(cw+1);
		        vertices[vertexIdx++] = j*(ch+1)+ch;
		        
		        // 右下
		        vertices[vertexIdx++] = xx+i*(cw+1)+cw;
		        vertices[vertexIdx++] = j*(ch+1)+ch;
		        
	    	}
	    }
         
        // JavaVMの外側のシステムリソースを直接取りに行きます。4はfloatのサイズです
        ByteBuffer bb = ByteBuffer.allocateDirect(vertices.length * 4);
        // CPUのアーキテクチャによって、エンディアンが異なります(値の格納の仕方！)。これを呼ぶことで自動的に合わせてくれます
        bb.order(ByteOrder.nativeOrder());
 
        // 取得したByteBufferをFloatBufferとして使います。Floatの値をぶち込める入れ物みたいなものかと。
        FloatBuffer fb = bb.asFloatBuffer();
        fb.put(vertices);
 
        // Bufferの先頭を指すように
        fb.position(0);
        // 頂点座標を有効にしてくださーい
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);

        // 3=(x, y, z)のこと。2次元図形で(x, y)しか定義していない場合は、2
        gl.glVertexPointer(2, GL10.GL_FLOAT, 0, fb);
        
        // 「頂点座標の3つの要素を使って、三角形を塗りつぶして描画してください」
        //gl.glDrawArrays(GL10.GL_TRIANGLES, 0, 3);

	    for (int j=0; j<ys; j++) {
	    	for (int i=0; i<xs; i++) {
	    		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 4 * (i + j*xs), 4);
	    	}
	    }
		//gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 6 * (xs*ys));
	    
        xx += 8;
        if (xx + (cw+1)*xs >= displayWidth) xx = 0;
        
		cnt ++;
		if (System.currentTimeMillis() - startTime >= 1000) {
			fps = fpsCnt;
	        Log.v("fps : ", Integer.toString(fpsCnt));
			fpsCnt = 0;
			startTime = System.currentTimeMillis();
		} else {
			fpsCnt ++;
		}
		
	}
	
	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		displayWidth  = width;
		displayHeight = height;
        // 描画範囲を指定する
		
        gl.glViewport(0, 0, displayWidth, displayHeight);
        gl.glMatrixMode(GL10.GL_PROJECTION);
	    gl.glLoadIdentity();
        GLU.gluOrtho2D(gl, 0, displayWidth, displayHeight, 0);
		
        

	    // ビューポートの設定
	    //gl.glViewport(0, 0, width, height);
	    // プロジェクションモードに設定
	    //gl.glMatrixMode(GL10.GL_PROJECTION);
	    // スクリーン座標を初期化
	    //gl.glLoadIdentity();
	    // 2Dの投影
        //GLU.gluOrtho2D(gl, 0, screenWidth, screenHeight, 0);
	    //GLU.gluOrtho2D( gl, -1.0f, 1.0f, 1.0f, -1.0f );
	    // 頂点の配列の利用を有効にする
	    //gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
	    // 色の配列の利用を有効にする
	    //gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
	    
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		// TODO 自動生成されたメソッド・スタブ
		
	}

	@Override
	public void run() {
		// TODO 自動生成されたメソッド・スタブ
		
	}

}
