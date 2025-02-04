/* DO NOT EDIT THIS FILE - it is machine generated */
/* Header for class io_vproxy_vfd_windows_IOCP */
#ifndef _Included_io_vproxy_vfd_windows_IOCP
#define _Included_io_vproxy_vfd_windows_IOCP
#ifdef __cplusplus
extern "C" {
#endif

#ifdef __cplusplus
}
#endif

#include <jni.h>
#include <pni.h>
#include "ioapiset.h"
#include "exception.h"
#include "io_vproxy_vfd_windows_HANDLE.h"
#include "io_vproxy_vfd_windows_OverlappedEntry.h"
#include "io_vproxy_vfd_windows_SOCKET.h"
#include "io_vproxy_vfd_windows_Overlapped.h"

#ifdef __cplusplus
extern "C" {
#endif

JNIEXPORT int JNICALL Java_io_vproxy_vfd_windows_IOCP_getQueuedCompletionStatusEx(PNIEnv_int * env, HANDLE handle, OVERLAPPED_ENTRY * completionPortEntries, uint32_t count, int32_t milliseconds, uint8_t alertable);
JNIEXPORT int JNICALL Java_io_vproxy_vfd_windows_IOCP_createIoCompletionPort(PNIEnv_dummyHANDLE * env, HANDLE fileHandle, HANDLE existingCompletionPort, void * completionKey, int32_t numberOfConcurrentThreads);
JNIEXPORT int JNICALL Java_io_vproxy_vfd_windows_IOCP_postQueuedCompletionStatus(PNIEnv_void * env, HANDLE completionPort, int32_t numberOfBytesTransferred, void * completionKey, OVERLAPPED * overlapped);

#ifdef __cplusplus
}
#endif
#endif // _Included_io_vproxy_vfd_windows_IOCP
// metadata.generator-version: pni 22.0.0.17
// sha256:2840a12c7829fe33cf2187e211c4d01b08b6bd44857bca9f05b0e9c63b8082f4
