#ifndef VFD_POSIX_H
    #define VFD_POSIX_H 1

    #ifndef _WIN32
        #include "ae.h"
    #endif

    #include <inttypes.h>
    #if defined(__linux__) || defined(__APPLE__)
    #define byte int8_t
    #endif

    #ifdef __linux__
        #include <sys/eventfd.h>
    #endif

    #ifdef _WIN32
        #include <winsock2.h>
        #include <mswsock.h>
    #else
        #include <sys/socket.h>
    #endif
    #ifndef _WIN32
        #include <netinet/ip.h>
    #endif

    #define V_AF_INET      AF_INET
    #define V_AF_INET6     AF_INET6
    #define V_AF_UNIX      AF_UNIX
    #define V_SOCK_STREAM  SOCK_STREAM
    #define V_SOCK_DGRAM   SOCK_DGRAM
    #define V_SOL_SOCKET   SOL_SOCKET
    #define V_SO_LINGER    SO_LINGER
    #ifdef _WIN32
      #define V_SO_REUSEPORT -1
    #else
      #define V_SO_REUSEPORT SO_REUSEPORT
    #endif
    #define V_SO_REUSEADDR SO_REUSEADDR
    #define V_SO_RCVBUF    SO_RCVBUF
    #define V_SO_BROADCAST SO_BROADCAST
    #define V_IPPROTO_TCP  IPPROTO_TCP
    #ifdef SOL_IP
        #define V_SOL_IP   SOL_IP
    #else
        #define V_SOL_IP   IPPROTO_IP
    #endif
    #define V_SHUT_WR      SHUT_WR
    typedef struct linger v_linger;

        #ifdef IP_TRANSPARENT
            #define V_IP_TRANSPARENT IP_TRANSPARENT
        #else
            // unsupported, let it fail
            #define V_IP_TRANSPARENT -1
        #endif

        #define v_socket      socket
        #define v_bind        bind
        #define v_listen      listen
        #define v_accept      accept
        #define v_setsockopt  setsockopt
        #define v_connect     connect
        #define v_shutdown    shutdown
        #define v_getsockname getsockname
        #define v_getpeername getpeername
        #define v_sendto      sendto
        #define v_recvfrom    recvfrom
        typedef struct sockaddr v_sockaddr;


    #ifndef _WIN32
        #include <netinet/tcp.h>
    #endif
    #define V_TCP_NODELAY  TCP_NODELAY



    #include <unistd.h>
    #ifdef _WIN32
        #define v_close      _close
        #define v_read       _read
        #define v_write      _write
        #define v_pipe       _pipe
    #else
        #define v_close       close
        #define v_read        read
        #define v_write       write
        #define v_pipe        pipe
    #endif

    typedef struct sockaddr_in  v_sockaddr_in;
    #ifdef _WIN32
        #include <ws2ipdef.h>
    #endif
    typedef struct sockaddr_in6 v_sockaddr_in6;
    #ifndef _WIN32
        #include <sys/un.h>
    #endif
    typedef struct sockaddr_un  v_sockaddr_un;
    #ifndef UNIX_PATH_MAX
        #define UNIX_PATH_MAX 100
    #endif



    #include <sys/time.h>
        #define v_gettimeofday gettimeofday
    typedef struct timeval v_timeval;



    #ifndef _WIN32
        #include <sys/ioctl.h>
    #endif
    #ifndef _WIN32
        #include <net/if.h>
    #endif
    #define V_FIONBIO FIONBIO
    #define V_SIOCGIFFLAGS SIOCGIFFLAGS
    #define V_SIOCSIFFLAGS SIOCSIFFLAGS
    #ifndef _WIN32
        #define v_ioctl ioctl
    #endif


    #ifndef _WIN32
        #include <arpa/inet.h>
    #endif

    #define v_htons htons
    #define v_htonl htonl
    #define v_ntohl ntohl
    #define v_ntohs ntohs

    #ifndef _WIN32
        #define v_inet_pton inet_pton
        #define v_inet_ntop inet_ntop
    #endif

    void j2cSockAddrIPv4(v_sockaddr_in* name, int32_t addrHostOrder, uint16_t port);
    int j2cSockAddrIPv6(v_sockaddr_in6* name, char* fullAddrCharArray, uint16_t port);

    #include <errno.h>
    #ifndef EWOULDBLOCK
        #define V_EWOULDBLOCK EAGAIN
    #else
        #define V_EWOULDBLOCK EWOULDBLOCK
    #endif
    #define V_EAGAIN EAGAIN
    #define V_EINPROGRESS EINPROGRESS



    #include <strings.h>
    #include <stdio.h>
    #include <stdlib.h>
    #define v_memset memset
    #define v_memcpy memcpy

    #include <fcntl.h>

    // for tap support
    #ifdef __linux__
      #include <string.h>
      #include <linux/if.h>
      #include <linux/if_tun.h>
    #endif
    #ifdef __APPLE__
      #include <sys/kern_control.h>
      #include <net/if_utun.h>
      #include <sys/sys_domain.h>
    #endif
    #ifndef IFNAMSIZ
      #define IFNAMSIZ 16
    #endif

    // pthread
    #include <pthread.h>

    // util functions
    static inline int v_str_starts_with(const char* str, const char* prefix) {
      int prelen = strlen(prefix);
      int len = strlen(str);
      return len < prelen ? 0 : memcmp(prefix, str, prelen) == 0;
    }

#endif
