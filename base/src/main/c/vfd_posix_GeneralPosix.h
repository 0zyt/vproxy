/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class vfd_posix_GeneralPosix */

#ifndef _Included_vfd_posix_GeneralPosix
#define _Included_vfd_posix_GeneralPosix
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     vfd_posix_GeneralPosix
 * Method:    pipeFDSupported
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL Java_vfd_posix_GeneralPosix_pipeFDSupported
  (JNIEnv *, jobject);

/*
 * Class:     vfd_posix_GeneralPosix
 * Method:    onlySelectNow
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL Java_vfd_posix_GeneralPosix_onlySelectNow
  (JNIEnv *, jobject);

/*
 * Class:     vfd_posix_GeneralPosix
 * Method:    aeReadable
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_vfd_posix_GeneralPosix_aeReadable
  (JNIEnv *, jobject);

/*
 * Class:     vfd_posix_GeneralPosix
 * Method:    aeWritable
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_vfd_posix_GeneralPosix_aeWritable
  (JNIEnv *, jobject);

/*
 * Class:     vfd_posix_GeneralPosix
 * Method:    openPipe
 * Signature: ()[I
 */
JNIEXPORT jintArray JNICALL Java_vfd_posix_GeneralPosix_openPipe
  (JNIEnv *, jobject);

/*
 * Class:     vfd_posix_GeneralPosix
 * Method:    aeCreateEventLoop
 * Signature: (I)J
 */
JNIEXPORT jlong JNICALL Java_vfd_posix_GeneralPosix_aeCreateEventLoop
  (JNIEnv *, jobject, jint);

/*
 * Class:     vfd_posix_GeneralPosix
 * Method:    aeApiPoll
 * Signature: (JJ)[Lvfd/posix/FDInfo;
 */
JNIEXPORT jobjectArray JNICALL Java_vfd_posix_GeneralPosix_aeApiPoll
  (JNIEnv *, jobject, jlong, jlong);

/*
 * Class:     vfd_posix_GeneralPosix
 * Method:    aeAllFDs
 * Signature: (J)[Lvfd/posix/FDInfo;
 */
JNIEXPORT jobjectArray JNICALL Java_vfd_posix_GeneralPosix_aeAllFDs
  (JNIEnv *, jobject, jlong);

/*
 * Class:     vfd_posix_GeneralPosix
 * Method:    aeCreateFileEvent
 * Signature: (JIILjava/lang/Object;)V
 */
JNIEXPORT void JNICALL Java_vfd_posix_GeneralPosix_aeCreateFileEvent
  (JNIEnv *, jobject, jlong, jint, jint, jobject);

/*
 * Class:     vfd_posix_GeneralPosix
 * Method:    aeUpdateFileEvent
 * Signature: (JII)V
 */
JNIEXPORT void JNICALL Java_vfd_posix_GeneralPosix_aeUpdateFileEvent
  (JNIEnv *, jobject, jlong, jint, jint);

/*
 * Class:     vfd_posix_GeneralPosix
 * Method:    aeDeleteFileEvent
 * Signature: (JI)V
 */
JNIEXPORT void JNICALL Java_vfd_posix_GeneralPosix_aeDeleteFileEvent
  (JNIEnv *, jobject, jlong, jint);

/*
 * Class:     vfd_posix_GeneralPosix
 * Method:    aeGetFileEvents
 * Signature: (JI)I
 */
JNIEXPORT jint JNICALL Java_vfd_posix_GeneralPosix_aeGetFileEvents
  (JNIEnv *, jobject, jlong, jint);

/*
 * Class:     vfd_posix_GeneralPosix
 * Method:    aeGetClientData
 * Signature: (JI)Ljava/lang/Object;
 */
JNIEXPORT jobject JNICALL Java_vfd_posix_GeneralPosix_aeGetClientData
  (JNIEnv *, jobject, jlong, jint);

/*
 * Class:     vfd_posix_GeneralPosix
 * Method:    aeDeleteEventLoop
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_vfd_posix_GeneralPosix_aeDeleteEventLoop
  (JNIEnv *, jobject, jlong);

/*
 * Class:     vfd_posix_GeneralPosix
 * Method:    setBlocking
 * Signature: (IZ)V
 */
JNIEXPORT void JNICALL Java_vfd_posix_GeneralPosix_setBlocking
  (JNIEnv *, jobject, jint, jboolean);

/*
 * Class:     vfd_posix_GeneralPosix
 * Method:    setSoLinger
 * Signature: (II)V
 */
JNIEXPORT void JNICALL Java_vfd_posix_GeneralPosix_setSoLinger
  (JNIEnv *, jobject, jint, jint);

/*
 * Class:     vfd_posix_GeneralPosix
 * Method:    setReusePort
 * Signature: (IZ)V
 */
JNIEXPORT void JNICALL Java_vfd_posix_GeneralPosix_setReusePort
  (JNIEnv *, jobject, jint, jboolean);

/*
 * Class:     vfd_posix_GeneralPosix
 * Method:    setRcvBuf
 * Signature: (II)V
 */
JNIEXPORT void JNICALL Java_vfd_posix_GeneralPosix_setRcvBuf
  (JNIEnv *, jobject, jint, jint);

/*
 * Class:     vfd_posix_GeneralPosix
 * Method:    setTcpNoDelay
 * Signature: (IZ)V
 */
JNIEXPORT void JNICALL Java_vfd_posix_GeneralPosix_setTcpNoDelay
  (JNIEnv *, jobject, jint, jboolean);

/*
 * Class:     vfd_posix_GeneralPosix
 * Method:    setBroadcast
 * Signature: (IZ)V
 */
JNIEXPORT void JNICALL Java_vfd_posix_GeneralPosix_setBroadcast
  (JNIEnv *, jobject, jint, jboolean);

/*
 * Class:     vfd_posix_GeneralPosix
 * Method:    setIpTransparent
 * Signature: (IZ)V
 */
JNIEXPORT void JNICALL Java_vfd_posix_GeneralPosix_setIpTransparent
  (JNIEnv *, jobject, jint, jboolean);

/*
 * Class:     vfd_posix_GeneralPosix
 * Method:    close
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_vfd_posix_GeneralPosix_close
  (JNIEnv *, jobject, jint);

/*
 * Class:     vfd_posix_GeneralPosix
 * Method:    createIPv4TcpFD
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_vfd_posix_GeneralPosix_createIPv4TcpFD
  (JNIEnv *, jobject);

/*
 * Class:     vfd_posix_GeneralPosix
 * Method:    createIPv6TcpFD
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_vfd_posix_GeneralPosix_createIPv6TcpFD
  (JNIEnv *, jobject);

/*
 * Class:     vfd_posix_GeneralPosix
 * Method:    createIPv4UdpFD
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_vfd_posix_GeneralPosix_createIPv4UdpFD
  (JNIEnv *, jobject);

/*
 * Class:     vfd_posix_GeneralPosix
 * Method:    createIPv6UdpFD
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_vfd_posix_GeneralPosix_createIPv6UdpFD
  (JNIEnv *, jobject);

/*
 * Class:     vfd_posix_GeneralPosix
 * Method:    createUnixDomainSocketFD
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_vfd_posix_GeneralPosix_createUnixDomainSocketFD
  (JNIEnv *, jobject);

/*
 * Class:     vfd_posix_GeneralPosix
 * Method:    bindIPv4
 * Signature: (III)V
 */
JNIEXPORT void JNICALL Java_vfd_posix_GeneralPosix_bindIPv4
  (JNIEnv *, jobject, jint, jint, jint);

/*
 * Class:     vfd_posix_GeneralPosix
 * Method:    bindIPv6
 * Signature: (ILjava/lang/String;I)V
 */
JNIEXPORT void JNICALL Java_vfd_posix_GeneralPosix_bindIPv6
  (JNIEnv *, jobject, jint, jstring, jint);

/*
 * Class:     vfd_posix_GeneralPosix
 * Method:    bindUnixDomainSocket
 * Signature: (ILjava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_vfd_posix_GeneralPosix_bindUnixDomainSocket
  (JNIEnv *, jobject, jint, jstring);

/*
 * Class:     vfd_posix_GeneralPosix
 * Method:    accept
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_vfd_posix_GeneralPosix_accept
  (JNIEnv *, jobject, jint);

/*
 * Class:     vfd_posix_GeneralPosix
 * Method:    connectIPv4
 * Signature: (III)V
 */
JNIEXPORT void JNICALL Java_vfd_posix_GeneralPosix_connectIPv4
  (JNIEnv *, jobject, jint, jint, jint);

/*
 * Class:     vfd_posix_GeneralPosix
 * Method:    connectIPv6
 * Signature: (ILjava/lang/String;I)V
 */
JNIEXPORT void JNICALL Java_vfd_posix_GeneralPosix_connectIPv6
  (JNIEnv *, jobject, jint, jstring, jint);

/*
 * Class:     vfd_posix_GeneralPosix
 * Method:    finishConnect
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_vfd_posix_GeneralPosix_finishConnect
  (JNIEnv *, jobject, jint);

/*
 * Class:     vfd_posix_GeneralPosix
 * Method:    shutdownOutput
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_vfd_posix_GeneralPosix_shutdownOutput
  (JNIEnv *, jobject, jint);

/*
 * Class:     vfd_posix_GeneralPosix
 * Method:    getIPv4Local
 * Signature: (I)Lvfd/posix/VSocketAddress;
 */
JNIEXPORT jobject JNICALL Java_vfd_posix_GeneralPosix_getIPv4Local
  (JNIEnv *, jobject, jint);

/*
 * Class:     vfd_posix_GeneralPosix
 * Method:    getIPv6Local
 * Signature: (I)Lvfd/posix/VSocketAddress;
 */
JNIEXPORT jobject JNICALL Java_vfd_posix_GeneralPosix_getIPv6Local
  (JNIEnv *, jobject, jint);

/*
 * Class:     vfd_posix_GeneralPosix
 * Method:    getIPv4Remote
 * Signature: (I)Lvfd/posix/VSocketAddress;
 */
JNIEXPORT jobject JNICALL Java_vfd_posix_GeneralPosix_getIPv4Remote
  (JNIEnv *, jobject, jint);

/*
 * Class:     vfd_posix_GeneralPosix
 * Method:    getIPv6Remote
 * Signature: (I)Lvfd/posix/VSocketAddress;
 */
JNIEXPORT jobject JNICALL Java_vfd_posix_GeneralPosix_getIPv6Remote
  (JNIEnv *, jobject, jint);

/*
 * Class:     vfd_posix_GeneralPosix
 * Method:    getUDSLocal
 * Signature: (I)Lvfd/posix/VSocketAddress;
 */
JNIEXPORT jobject JNICALL Java_vfd_posix_GeneralPosix_getUDSLocal
  (JNIEnv *, jobject, jint);

/*
 * Class:     vfd_posix_GeneralPosix
 * Method:    getUDSRemote
 * Signature: (I)Lvfd/posix/VSocketAddress;
 */
JNIEXPORT jobject JNICALL Java_vfd_posix_GeneralPosix_getUDSRemote
  (JNIEnv *, jobject, jint);

/*
 * Class:     vfd_posix_GeneralPosix
 * Method:    read
 * Signature: (ILjava/nio/ByteBuffer;II)I
 */
JNIEXPORT jint JNICALL Java_vfd_posix_GeneralPosix_read
  (JNIEnv *, jobject, jint, jobject, jint, jint);

/*
 * Class:     vfd_posix_GeneralPosix
 * Method:    write
 * Signature: (ILjava/nio/ByteBuffer;II)I
 */
JNIEXPORT jint JNICALL Java_vfd_posix_GeneralPosix_write
  (JNIEnv *, jobject, jint, jobject, jint, jint);

/*
 * Class:     vfd_posix_GeneralPosix
 * Method:    sendtoIPv4
 * Signature: (ILjava/nio/ByteBuffer;IIII)I
 */
JNIEXPORT jint JNICALL Java_vfd_posix_GeneralPosix_sendtoIPv4
  (JNIEnv *, jobject, jint, jobject, jint, jint, jint, jint);

/*
 * Class:     vfd_posix_GeneralPosix
 * Method:    sendtoIPv6
 * Signature: (ILjava/nio/ByteBuffer;IILjava/lang/String;I)I
 */
JNIEXPORT jint JNICALL Java_vfd_posix_GeneralPosix_sendtoIPv6
  (JNIEnv *, jobject, jint, jobject, jint, jint, jstring, jint);

/*
 * Class:     vfd_posix_GeneralPosix
 * Method:    recvfromIPv4
 * Signature: (ILjava/nio/ByteBuffer;II)Lvfd/posix/UDPRecvResult;
 */
JNIEXPORT jobject JNICALL Java_vfd_posix_GeneralPosix_recvfromIPv4
  (JNIEnv *, jobject, jint, jobject, jint, jint);

/*
 * Class:     vfd_posix_GeneralPosix
 * Method:    recvfromIPv6
 * Signature: (ILjava/nio/ByteBuffer;II)Lvfd/posix/UDPRecvResult;
 */
JNIEXPORT jobject JNICALL Java_vfd_posix_GeneralPosix_recvfromIPv6
  (JNIEnv *, jobject, jint, jobject, jint, jint);

/*
 * Class:     vfd_posix_GeneralPosix
 * Method:    currentTimeMillis
 * Signature: ()J
 */
JNIEXPORT jlong JNICALL Java_vfd_posix_GeneralPosix_currentTimeMillis
  (JNIEnv *, jobject);

/*
 * Class:     vfd_posix_GeneralPosix
 * Method:    tapNonBlockingSupported
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL Java_vfd_posix_GeneralPosix_tapNonBlockingSupported
  (JNIEnv *, jobject);

/*
 * Class:     vfd_posix_GeneralPosix
 * Method:    createTapFD
 * Signature: (Ljava/lang/String;)Lvfd/TapInfo;
 */
JNIEXPORT jobject JNICALL Java_vfd_posix_GeneralPosix_createTapFD
  (JNIEnv *, jobject, jstring);

#ifdef __cplusplus
}
#endif
#endif
