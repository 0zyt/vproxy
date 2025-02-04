/* DO NOT EDIT THIS FILE - it is machine generated */
/* Header for class io_vproxy_vfd_posix_PosixNative */
#ifndef _Included_io_vproxy_vfd_posix_PosixNative
#define _Included_io_vproxy_vfd_posix_PosixNative
#ifdef __cplusplus
extern "C" {
#endif

#ifdef __cplusplus
}
#endif

#include <jni.h>
#include <pni.h>
#include "io_vproxy_vfd_posix_SocketAddressIPv4ST.h"
#include "io_vproxy_vfd_posix_SocketAddressIPv6ST.h"
#include "io_vproxy_vfd_posix_SocketAddressUDSST.h"
#include "io_vproxy_vfd_posix_UDPRecvResultIPv4ST.h"
#include "io_vproxy_vfd_posix_UDPRecvResultIPv6ST.h"
#include "io_vproxy_vfd_posix_TapInfoST.h"

#ifdef __cplusplus
extern "C" {
#endif

JNIEXPORT int JNICALL Java_io_vproxy_vfd_posix_PosixNative_aeReadable(PNIEnv_int * env);
JNIEXPORT int JNICALL Java_io_vproxy_vfd_posix_PosixNative_aeWritable(PNIEnv_int * env);
JNIEXPORT int JNICALL Java_io_vproxy_vfd_posix_PosixNative_openPipe(PNIEnv_void * env, PNIBuf_int * fds);
JNIEXPORT int JNICALL Java_io_vproxy_vfd_posix_PosixNative_aeCreateEventLoop(PNIEnv_long * env, int32_t setsize, int32_t epfd, uint8_t preferPoll);
JNIEXPORT int JNICALL Java_io_vproxy_vfd_posix_PosixNative_aeGetFired(PNIEnv_pointer * env, int64_t ae);
JNIEXPORT int JNICALL Java_io_vproxy_vfd_posix_PosixNative_aeGetFiredExtra(PNIEnv_pointer * env, int64_t ae);
JNIEXPORT int JNICALL Java_io_vproxy_vfd_posix_PosixNative_aeApiPoll(PNIEnv_int * env, int64_t ae, int64_t wait);
JNIEXPORT int JNICALL Java_io_vproxy_vfd_posix_PosixNative_aeApiPollNow(PNIEnv_int * env, int64_t ae);
JNIEXPORT int JNICALL Java_io_vproxy_vfd_posix_PosixNative_aeGetFiredExtraNum(PNIEnv_int * env, int64_t ae);
JNIEXPORT int JNICALL Java_io_vproxy_vfd_posix_PosixNative_aeCreateFileEvent(PNIEnv_void * env, int64_t ae, int32_t fd, int32_t mask);
JNIEXPORT int JNICALL Java_io_vproxy_vfd_posix_PosixNative_aeUpdateFileEvent(PNIEnv_void * env, int64_t ae, int32_t fd, int32_t mask);
JNIEXPORT int JNICALL Java_io_vproxy_vfd_posix_PosixNative_aeDeleteFileEvent(PNIEnv_void * env, int64_t ae, int32_t fd);
JNIEXPORT int JNICALL Java_io_vproxy_vfd_posix_PosixNative_aeDeleteEventLoop(PNIEnv_void * env, int64_t ae);
JNIEXPORT int JNICALL Java_io_vproxy_vfd_posix_PosixNative_setBlocking(PNIEnv_void * env, int32_t fd, uint8_t v);
JNIEXPORT int JNICALL Java_io_vproxy_vfd_posix_PosixNative_setSoLinger(PNIEnv_void * env, int32_t fd, int32_t v);
JNIEXPORT int JNICALL Java_io_vproxy_vfd_posix_PosixNative_setReusePort(PNIEnv_void * env, int32_t fd, uint8_t v);
JNIEXPORT int JNICALL Java_io_vproxy_vfd_posix_PosixNative_setRcvBuf(PNIEnv_void * env, int32_t fd, int32_t buflen);
JNIEXPORT int JNICALL Java_io_vproxy_vfd_posix_PosixNative_setTcpNoDelay(PNIEnv_void * env, int32_t fd, uint8_t v);
JNIEXPORT int JNICALL Java_io_vproxy_vfd_posix_PosixNative_setBroadcast(PNIEnv_void * env, int32_t fd, uint8_t v);
JNIEXPORT int JNICALL Java_io_vproxy_vfd_posix_PosixNative_setIpTransparent(PNIEnv_void * env, int32_t fd, uint8_t v);
JNIEXPORT int JNICALL Java_io_vproxy_vfd_posix_PosixNative_close(PNIEnv_void * env, int32_t fd);
JNIEXPORT int JNICALL Java_io_vproxy_vfd_posix_PosixNative_createIPv4TcpFD(PNIEnv_int * env);
JNIEXPORT int JNICALL Java_io_vproxy_vfd_posix_PosixNative_createIPv6TcpFD(PNIEnv_int * env);
JNIEXPORT int JNICALL Java_io_vproxy_vfd_posix_PosixNative_createIPv4UdpFD(PNIEnv_int * env);
JNIEXPORT int JNICALL Java_io_vproxy_vfd_posix_PosixNative_createIPv6UdpFD(PNIEnv_int * env);
JNIEXPORT int JNICALL Java_io_vproxy_vfd_posix_PosixNative_createUnixDomainSocketFD(PNIEnv_int * env);
JNIEXPORT int JNICALL Java_io_vproxy_vfd_posix_PosixNative_bindIPv4(PNIEnv_void * env, int32_t fd, int32_t addrHostOrder, int32_t port);
JNIEXPORT int JNICALL Java_io_vproxy_vfd_posix_PosixNative_bindIPv6(PNIEnv_void * env, int32_t fd, char * fullAddr, int32_t port);
JNIEXPORT int JNICALL Java_io_vproxy_vfd_posix_PosixNative_bindUnixDomainSocket(PNIEnv_void * env, int32_t fd, char * path);
JNIEXPORT int JNICALL Java_io_vproxy_vfd_posix_PosixNative_accept(PNIEnv_int * env, int32_t fd);
JNIEXPORT int JNICALL Java_io_vproxy_vfd_posix_PosixNative_connectIPv4(PNIEnv_void * env, int32_t fd, int32_t addrHostOrder, int32_t port);
JNIEXPORT int JNICALL Java_io_vproxy_vfd_posix_PosixNative_connectIPv6(PNIEnv_void * env, int32_t fd, char * fullAddr, int32_t port);
JNIEXPORT int JNICALL Java_io_vproxy_vfd_posix_PosixNative_connectUDS(PNIEnv_void * env, int32_t fd, char * sock);
JNIEXPORT int JNICALL Java_io_vproxy_vfd_posix_PosixNative_finishConnect(PNIEnv_void * env, int32_t fd);
JNIEXPORT int JNICALL Java_io_vproxy_vfd_posix_PosixNative_shutdownOutput(PNIEnv_void * env, int32_t fd);
JNIEXPORT int JNICALL Java_io_vproxy_vfd_posix_PosixNative_getIPv4Local(PNIEnv_SocketAddressIPv4_st * env, int32_t fd, SocketAddressIPv4_st * return_);
JNIEXPORT int JNICALL Java_io_vproxy_vfd_posix_PosixNative_getIPv6Local(PNIEnv_SocketAddressIPv6_st * env, int32_t fd, SocketAddressIPv6_st * return_);
JNIEXPORT int JNICALL Java_io_vproxy_vfd_posix_PosixNative_getIPv4Remote(PNIEnv_SocketAddressIPv4_st * env, int32_t fd, SocketAddressIPv4_st * return_);
JNIEXPORT int JNICALL Java_io_vproxy_vfd_posix_PosixNative_getIPv6Remote(PNIEnv_SocketAddressIPv6_st * env, int32_t fd, SocketAddressIPv6_st * return_);
JNIEXPORT int JNICALL Java_io_vproxy_vfd_posix_PosixNative_getUDSLocal(PNIEnv_SocketAddressUDS_st * env, int32_t fd, SocketAddressUDS_st * return_);
JNIEXPORT int JNICALL Java_io_vproxy_vfd_posix_PosixNative_getUDSRemote(PNIEnv_SocketAddressUDS_st * env, int32_t fd, SocketAddressUDS_st * return_);
JNIEXPORT int JNICALL Java_io_vproxy_vfd_posix_PosixNative_read(PNIEnv_int * env, int32_t fd, void * directBuffer, int32_t off, int32_t len);
JNIEXPORT int JNICALL Java_io_vproxy_vfd_posix_PosixNative_readBlocking(PNIEnv_int * env, int32_t fd, void * directBuffer, int32_t off, int32_t len);
JNIEXPORT int JNICALL Java_io_vproxy_vfd_posix_PosixNative_write(PNIEnv_int * env, int32_t fd, void * directBuffer, int32_t off, int32_t len);
JNIEXPORT int JNICALL Java_io_vproxy_vfd_posix_PosixNative_sendtoIPv4(PNIEnv_int * env, int32_t fd, void * directBuffer, int32_t off, int32_t len, int32_t addrHostOrder, int32_t port);
JNIEXPORT int JNICALL Java_io_vproxy_vfd_posix_PosixNative_sendtoIPv6(PNIEnv_int * env, int32_t fd, void * directBuffer, int32_t off, int32_t len, char * fullAddr, int32_t port);
JNIEXPORT int JNICALL Java_io_vproxy_vfd_posix_PosixNative_recvfromIPv4(PNIEnv_UDPRecvResultIPv4_st * env, int32_t fd, void * directBuffer, int32_t off, int32_t len, UDPRecvResultIPv4_st * return_);
JNIEXPORT int JNICALL Java_io_vproxy_vfd_posix_PosixNative_recvfromIPv6(PNIEnv_UDPRecvResultIPv6_st * env, int32_t fd, void * directBuffer, int32_t off, int32_t len, UDPRecvResultIPv6_st * return_);
JNIEXPORT int JNICALL Java_io_vproxy_vfd_posix_PosixNative_currentTimeMillis(PNIEnv_long * env);
JNIEXPORT int JNICALL Java_io_vproxy_vfd_posix_PosixNative_tapNonBlockingSupported(PNIEnv_bool * env);
JNIEXPORT int JNICALL Java_io_vproxy_vfd_posix_PosixNative_tunNonBlockingSupported(PNIEnv_bool * env);
JNIEXPORT int JNICALL Java_io_vproxy_vfd_posix_PosixNative_createTapFD(PNIEnv_TapInfo_st * env, char * dev, uint8_t isTun, TapInfo_st * return_);
JNIEXPORT int JNICALL Java_io_vproxy_vfd_posix_PosixNative_setCoreAffinityForCurrentThread(PNIEnv_void * env, int64_t mask);

#ifdef __cplusplus
}
#endif
#endif // _Included_io_vproxy_vfd_posix_PosixNative
// metadata.generator-version: pni 22.0.0.17
// sha256:1e84eb47f36e590566f1bbbf692bdba8e228a6ba2a5900b4e62f016ecfb9a38b
