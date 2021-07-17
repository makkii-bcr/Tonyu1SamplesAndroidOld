LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := TonyuSampleAndroid
LOCAL_SRC_FILES := TonyuSampleAndroid.cpp

LOCAL_LDLIBS := -lGLESv1_CM
LOCAL_LDLIBS := -llog


include $(BUILD_SHARED_LIBRARY)
