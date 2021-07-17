package tonyu.kernel;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * ファイル読み込みを担当するクラス。<br>
 * TonyuのFileReaderに相当。<br>
 */
public class TonyuFileReader {
	
	// テキストファイル版 ///////////////////////////////////////////
	
	/**
	 * リソースからテキストファイルを読み込む
	 * @param resId リソースID
	 * @return 読み込まれたテキストデータ(１要素１行分のデータが入ったString配列)<br>
	 * 読み込みエラー:null
	 */
	public String[] readTextRes(int resId) {
		return readTextMethod(TGL.res.openRawResource(resId));
	}

	/**
	 * テキストファイルを読み込む
	 * @param fileName ファイル名
	 * @return 読み込まれたテキストデータ(１要素１行分のデータが入ったString配列)<br>
	 * 読み込みエラー:null
	 */
	public String[] readTextFile(String fileName) {
		FileInputStream fis;
		try {
			fis = TGL.context.openFileInput(fileName);
			return readTextMethod(fis);
		} catch (FileNotFoundException e) { // ファイルなかったらnull
			e.printStackTrace();
			return null;
		}
	}
	

	// バイナリファイル版 ///////////////////////////////////////////

	/**
	 * リソースからバイナリファイルを読み込む
	 * @param resId リソースID
	 * @return 読み込まれたバイナリデータ(１要素１バイト分のデータが入ったbyte配列)<br>
	 * 読み込みエラー:null
	 */
	public byte[] readBinaryRes(int resId) {
		return readBinaryMethod(TGL.res.openRawResource(resId));
	}
	
	/**
	 * バイナリファイルを読み込む
	 * @param fileName ファイル名
	 * @return 読み込まれたバイナリデータ(１要素１バイト分のデータが入ったbyte配列)<br>
	 * 読み込みエラー:null
	 */
	public byte[] readBinaryFile(String fileName) {
		FileInputStream fis;
		try {
			fis = TGL.context.openFileInput(fileName);
			return readBinaryMethod(fis);
		} catch (FileNotFoundException e) { // ファイルなかったらnull
			e.printStackTrace();
			return null;
		}
	}
	
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * テキストファイルを読み込む(汎用性を持った隠しメソッド)
	 * @param is InputStreamのインスタンス
	 * @return 読み込まれたテキストデータ(１要素１行分のデータが入ったString配列)<br>
	 * 読み込みエラー:null
	 */
	private String[] readTextMethod(InputStream fis) {
		ArrayList<String> strAry = null;
		BufferedReader br = null;
		boolean readOK = true;
		try {
			InputStreamReader isr = new InputStreamReader(fis);
			br = new BufferedReader(isr);
			strAry = new ArrayList<String>();
			
			String str;
			while ((str = br.readLine()) != null) {
				strAry.add(str);
			}
			
		} catch (FileNotFoundException e) {
			readOK = false;
            e.printStackTrace();
        } catch (IOException e) {
        	readOK = false;
            e.printStackTrace();
		} finally {
			if (br != null) {
	        	try {
	        		br.close();
		        } catch (IOException e) {
		            e.printStackTrace();
		        }
			}
        }
		if (readOK) return (String[])strAry.toArray();
		else        return null;
	}
	
	
	
	/**
	 * バイナリファイルを読み込む(汎用性を持った隠しメソッド)
	 * @param is InputStreamのインスタンス
	 * @return 読み込まれたバイナリデータ(１要素１バイト分のデータが入ったbyte配列)<br>
	 * 読み込みエラー:null
	 */
	private byte[] readBinaryMethod(InputStream is) {
		BufferedInputStream bis = null;
		ByteArrayOutputStream byteAry = null;
		boolean readOK = true;
		try {
			bis = new BufferedInputStream(is);
			
            int size;
            byteAry = new ByteArrayOutputStream();
            byte[] byteTmp = new byte[is.available()];
            while ((size = bis.read(byteTmp)) != -1) {
            	byteAry.write(byteTmp, 0, size);   
            }
            
		} catch (FileNotFoundException e) {
			readOK = false;
            e.printStackTrace();
        } catch (IOException e) {
        	readOK = false;
            e.printStackTrace();
		} finally {
        	try {
        		if (is  != null) is.close();
        		if (bis != null) bis.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
        }
		if (readOK) return byteAry.toByteArray();
		else        return null;
	}
}
