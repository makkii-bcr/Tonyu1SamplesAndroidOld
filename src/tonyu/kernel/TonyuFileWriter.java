package tonyu.kernel;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import android.content.Context;

/**
 * ファイル書き込みを担当するクラス。<br>
 * TonyuのFileWriterに相当。<br>
 */
public class TonyuFileWriter {

	/**
	 * テキストファイルを書き込む
	 * @param fileName ファイル名
	 * @param strAry 書き込むテキストデータ(１要素１行分のデータが入ったString配列)
	 * @return 書き込みに成功したか(true:成功)
	 */
	public boolean writeTextFile(String fileName, String[] strAry) {
		BufferedWriter bw = null;
		boolean writeOK = true;
		try {
			FileOutputStream fos = TGL.context.openFileOutput(fileName, Context.MODE_PRIVATE);
			OutputStreamWriter ops = new OutputStreamWriter(fos);
			bw = new BufferedWriter(ops);
			
			for (String str : strAry) {
				bw.write(str);
				bw.newLine();
			}

    		bw.flush();
		} catch (FileNotFoundException e) {
			writeOK = false;
            e.printStackTrace();
        } catch (IOException e) {
        	writeOK = false;
            e.printStackTrace();
		} finally {
			if (bw != null) {
	        	try {
	        		bw.close();
		        } catch (IOException e) {
		        	writeOK = false;
		            e.printStackTrace();
		        }
			}
        }
		return writeOK;
	}
	
	
	/**
	 * バイナリファイルを書き込む
	 * @param fileName ファイル名
	 * @param byteAry 書き込むバイトデータ(１要素１バイト分のデータが入ったbyte配列)
	 * @return 書き込みに成功したか(true:成功)
	 */
	public boolean writeBinaryFile(String fileName, byte[] byteAry) {
		BufferedOutputStream bos = null;
		boolean writeOK = true;
		try {
			FileOutputStream fos = TGL.context.openFileOutput(fileName, Context.MODE_PRIVATE);
			bos = new BufferedOutputStream(fos);
			bos.write(byteAry);

    		bos.flush();
		} catch (FileNotFoundException e) {
			writeOK = false;
            e.printStackTrace();
        } catch (IOException e) {
        	writeOK = false;
            e.printStackTrace();
		} finally {
			if (bos != null) {
	        	try {
	        		bos.close();
		        } catch (IOException e) {
		        	writeOK = false;
		            e.printStackTrace();
		        }
			}
        }
		return writeOK;
	}
}
