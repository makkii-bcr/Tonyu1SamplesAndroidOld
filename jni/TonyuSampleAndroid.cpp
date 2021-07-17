#include <jni.h>
#include <GLES/gl.h>
#include <GLES/glext.h>
#include <android/log.h>
#include <tonyu_kernel_TonyuOpenGL11Drawer.h>

#define i_printf(...) __android_log_print(ANDROID_LOG_INFO, "ndk", __VA_ARGS__)

/*
 * Class:     tonyu_kernel_TonyuOpenGL11Drawer
 * Method:    allDraw_NDK
 * Signature: (Ljava/lang/Object;Z)Ljava/lang/String;
 */
JNIEXPORT void JNICALL Java_tonyu_kernel_TonyuOpenGL11Drawer_allDraw_1NDK
	(JNIEnv *, jobject, jobject, jboolean) {

	static int cnt = 0;
    //i_printf("allDraw_ndk : %d", cnt);
    cnt++;

}
