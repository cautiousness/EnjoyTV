#ifndef JIANXIFFMPEG_FFMPEG_RUN_H
#define JIANXIFFMPEG_FFMPEG_RUN_H

#include <jni.h>

JNIEXPORT jint JNICALL
Java_com_fuj_enjoytv_camera_FFmpegBridge_runCMD(JNIEnv *env, jclass type, jobjectArray commands);

JNIEXPORT void JNICALL
Java_com_fuj_enjoytv_camera_FFmpegBridge_initFFmpeg(JNIEnv *env, jclass type, jboolean debug, jstring logUrl_);

void log_callback(void* ptr, int level, const char* fmt, va_list vl);

int ffmpeg_cmd_run(int argc, char **argv);
#endif
