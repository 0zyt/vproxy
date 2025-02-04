package io.vproxy.test.cases;

import io.vproxy.base.http.HttpReqParser;
import io.vproxy.base.http.HttpRespParser;
import io.vproxy.base.processor.Processor;
import io.vproxy.base.processor.http1.HttpContext;
import io.vproxy.base.processor.http1.HttpProcessor;
import io.vproxy.base.processor.http1.HttpSubContext;
import io.vproxy.base.processor.http1.entity.Header;
import io.vproxy.base.processor.http1.entity.Request;
import io.vproxy.base.processor.http1.entity.Response;
import io.vproxy.base.util.ByteArray;
import io.vproxy.vfd.IP;
import io.vproxy.vfd.IPPort;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Objects;

import static org.junit.Assert.*;

public class TestHttp1Processor {
    private static final String forwardedFor = "1.2.3.4";
    private static final String clientPort = "1122";
    private static final IPPort address = new IPPort(
        IP.from(Objects.requireNonNull(IP.parseIpv4String(forwardedFor))),
        Integer.parseInt(clientPort)
    );
    private static final Processor.ContextInitParams ctxInitParams = new Processor.ContextInitParams(
        address
    );

    @Test
    public void simpleRequest() throws Exception {
        Processor<HttpContext, HttpSubContext> p = new HttpProcessor();
        HttpContext ctx = p.init(ctxInitParams);
        HttpSubContext front = p.initSub(new Processor.SubContextInitParams<>(
            ctx, 0, null
        ));

        String reqHead =
            "GET /hello/url HTTP/1.1\r\n" +
            "Host: www.example.com\r\n" +
            "Hello: World\r\n" +
            "\r\n";
        byte[] reqHeadBytes = reqHead.getBytes();

        for (int i = 0; i < reqHeadBytes.length; i++) {
            byte b = reqHeadBytes[i];
            int len = front.unittest_len();
            assertEquals(-1, len);
            ByteArray a = ByteArray.from(b);
            ByteArray r = front.unittest_feed(a);
            if (i != reqHeadBytes.length - 1) {
                assertNull(r);
            } else {
                assertEquals(ByteArray.from(
                    "GET /hello/url HTTP/1.1\r\n" +
                    "Host: www.example.com\r\n" +
                    "Hello: World\r\n" +
                    "X-Forwarded-For: " + forwardedFor + "\r\n" +
                    "X-Client-Port: " + clientPort + "\r\n" +
                    "\r\n"), r);
            }
        }
        Request req = front.unittest_getReq();
        {
            assertEquals("GET", req.method);
            assertEquals("/hello/url", req.uri);
            assertEquals("HTTP/1.1", req.version);

            assertEquals("Host", req.headers.get(0).key);
            assertEquals("www.example.com", req.headers.get(0).value);

            assertEquals("Hello", req.headers.get(1).key);
            assertEquals("World", req.headers.get(1).value);
        }
        assertTrue(front.isIdle());
    }

    @Test
    public void simpleResponse() throws Exception {
        Processor<HttpContext, HttpSubContext> p = new HttpProcessor();
        HttpContext ctx = p.init(new Processor.ContextInitParams(null));
        HttpSubContext backend = p.initSub(new Processor.SubContextInitParams<>(
            ctx, 1, null
        ));

        String respHead =
            "HTTP/1.1 200 OK\r\n" +
            "Content-Type: application/json\r\n" +
            "\r\n";
        byte[] respHeadBytes = respHead.getBytes();

        for (int i = 0; i < respHeadBytes.length; i++) {
            byte b = respHeadBytes[i];
            int len = backend.unittest_len();
            assertEquals(-1, len);
            ByteArray a = ByteArray.from(b);
            if (i != respHeadBytes.length - 1) {
                assertNull(backend.unittest_feed(a));
            } else {
                assertEquals(ByteArray.from(respHeadBytes), backend.unittest_feed(a));
            }
        }
        Response resp = backend.unittest_getResp();
        {
            assertEquals("HTTP/1.1", resp.version);
            assertEquals(200, resp.statusCode);
            assertEquals("OK", resp.reason);

            assertEquals("Content-Type", resp.headers.get(0).key);
            assertEquals("application/json", resp.headers.get(0).value);
        }
        assertTrue(backend.isIdle());
    }

    @Test
    public void noHeaderRequest() throws Exception {
        Processor<HttpContext, HttpSubContext> p = new HttpProcessor();
        HttpContext ctx = p.init(ctxInitParams);
        HttpSubContext front = p.initSub(new Processor.SubContextInitParams<>(
            ctx, 0, null
        ));

        String reqHead =
            "GET /hello/url HTTP/1.1\r\n" +
            "\r\n";
        byte[] reqHeadBytes = reqHead.getBytes();

        for (int i = 0; i < reqHeadBytes.length; i++) {
            byte b = reqHeadBytes[i];
            int len = front.unittest_len();
            assertEquals(-1, len);
            ByteArray a = ByteArray.from(b);
            if (i != reqHeadBytes.length - 1) {
                assertNull(front.unittest_feed(a));
            } else {
                assertEquals(ByteArray.from(
                    "GET /hello/url HTTP/1.1\r\n" +
                    "X-Forwarded-For: " + forwardedFor + "\r\n" +
                    "X-Client-Port: " + clientPort + "\r\n" +
                    "\r\n"
                ), front.unittest_feed(a));
            }
        }
        Request req = front.unittest_getReq();
        {
            assertEquals("GET", req.method);
            assertEquals("/hello/url", req.uri);
            assertEquals("HTTP/1.1", req.version);
        }
        assertTrue(front.isIdle());
    }

    @Test
    public void noHeaderResponse() throws Exception {
        Processor<HttpContext, HttpSubContext> p = new HttpProcessor();
        HttpContext ctx = p.init(new Processor.ContextInitParams(null));
        HttpSubContext backend = p.initSub(new Processor.SubContextInitParams<>(
            ctx, 1, null
        ));

        String respHead =
            "HTTP/1.1 200 OK\r\n" +
            "\r\n";
        byte[] respHeadBytes = respHead.getBytes();

        for (int i = 0; i < respHeadBytes.length; i++) {
            byte b = respHeadBytes[i];
            int len = backend.unittest_len();
            assertEquals(-1, len);
            ByteArray a = ByteArray.from(b);
            if (i != respHeadBytes.length - 1) {
                assertNull(backend.unittest_feed(a));
            } else {
                assertEquals(ByteArray.from(respHeadBytes), backend.unittest_feed(a));
            }
        }
        Response resp = backend.unittest_getResp();
        {
            assertEquals("HTTP/1.1", resp.version);
            assertEquals(200, resp.statusCode);
            assertEquals("OK", resp.reason);

            assertNull(resp.headers);
        }
        assertTrue(backend.isIdle());
    }

    @Test
    public void normalRequest() throws Exception {
        Processor<HttpContext, HttpSubContext> p = new HttpProcessor();
        HttpContext ctx = p.init(ctxInitParams);
        HttpSubContext front = p.initSub(new Processor.SubContextInitParams<>(
            ctx, 0, null
        ));

        String reqHead =
            "PUT /hello/url HTTP/1.1\r\n" +
            "Host: www.example.com\r\n" +
            "Hello: World\r\n" +
            "Content-Length: 10\r\n" +
            "\r\n" +
            "";
        byte[] reqHeadBytes = reqHead.getBytes();

        for (int i = 0; i < reqHeadBytes.length; i++) {
            byte b = reqHeadBytes[i];
            int len = front.unittest_len();
            assertEquals(-1, len);
            ByteArray a = ByteArray.from(b);
            ByteArray r = front.unittest_feed(a);
            if (i != reqHeadBytes.length - 1) {
                assertNull(r);
            } else {
                assertEquals(ByteArray.from(
                    "PUT /hello/url HTTP/1.1\r\n" +
                    "Host: www.example.com\r\n" +
                    "Hello: World\r\n" +
                    "Content-Length: 10\r\n" +
                    "X-Forwarded-For: " + forwardedFor + "\r\n" +
                    "X-Client-Port: " + clientPort + "\r\n" +
                    "\r\n"), r);
            }
        }
        Request req = front.unittest_getReq();
        {
            assertEquals("PUT", req.method);
            assertEquals("/hello/url", req.uri);
            assertEquals("HTTP/1.1", req.version);

            assertEquals("Host", req.headers.get(0).key);
            assertEquals("www.example.com", req.headers.get(0).value);

            assertEquals("Hello", req.headers.get(1).key);
            assertEquals("World", req.headers.get(1).value);

            assertEquals("Content-Length", req.headers.get(2).key);
            assertEquals("10", req.headers.get(2).value);
        }
        assertEquals(10, front.unittest_len());
        assertEquals(Processor.Mode.proxy, front.unittest_mode());
    }

    @Test
    public void normalResponse() throws Exception {
        Processor<HttpContext, HttpSubContext> p = new HttpProcessor();
        HttpContext ctx = p.init(new Processor.ContextInitParams(null));
        HttpSubContext backend = p.initSub(new Processor.SubContextInitParams<>(
            ctx, 1, null
        ));

        String respHead =
            "HTTP/1.1 200 OK\r\n" +
            "Content-Type: application/json\r\n" +
            "Content-Length: 20\r\n" +
            "\r\n";
        byte[] respHeadBytes = respHead.getBytes();

        for (int i = 0; i < respHeadBytes.length; i++) {
            byte b = respHeadBytes[i];
            int len = backend.unittest_len();
            assertEquals(-1, len);
            ByteArray a = ByteArray.from(b);
            if (i != respHeadBytes.length - 1) {
                assertNull(backend.unittest_feed(a));
            } else {
                assertEquals(ByteArray.from(respHeadBytes), backend.unittest_feed(a));
            }
        }
        Response resp = backend.unittest_getResp();
        {
            assertEquals("HTTP/1.1", resp.version);
            assertEquals(200, resp.statusCode);
            assertEquals("OK", resp.reason);

            assertEquals("Content-Type", resp.headers.get(0).key);
            assertEquals("application/json", resp.headers.get(0).value);

            assertEquals("Content-Length", resp.headers.get(1).key);
            assertEquals("20", resp.headers.get(1).value);
        }
        assertEquals(20, backend.unittest_len());
        assertEquals(Processor.Mode.proxy, backend.unittest_mode());

        // resp = backend.unittest_getResp();
        // data not feed: assertEquals(ByteArray.from("01234567890123456789".getBytes()), resp.body);
    }

    private HttpSubContext chunkRequestNoEnd() throws Exception {
        Processor<HttpContext, HttpSubContext> p = new HttpProcessor();
        HttpContext ctx = p.init(ctxInitParams);
        HttpSubContext front = p.initSub(new Processor.SubContextInitParams<>(
            ctx, 0, null
        ));

        String reqHead =
            "POST /hello/url HTTP/1.1\r\n" +
            "Host: www.example.com\r\n" +
            "Hello: World\r\n" +
            "Transfer-Encoding: chunked\r\n" +
            "\r\n";
        byte[] reqHeadBytes = reqHead.getBytes();

        for (int i = 0; i < reqHeadBytes.length; i++) {
            byte b = reqHeadBytes[i];
            int len = front.unittest_len();
            assertEquals(-1, len);
            ByteArray a = ByteArray.from(b);
            ByteArray r = front.unittest_feed(a);
            if (i != reqHeadBytes.length - 1) {
                assertNull(r);
            } else {
                assertEquals(ByteArray.from(
                    "POST /hello/url HTTP/1.1\r\n" +
                    "Host: www.example.com\r\n" +
                    "Hello: World\r\n" +
                    "Transfer-Encoding: chunked\r\n" +
                    "X-Forwarded-For: " + forwardedFor + "\r\n" +
                    "X-Client-Port: " + clientPort + "\r\n" +
                    "\r\n"), r);
            }
        }
        Request req = front.unittest_getReq();
        {
            assertEquals("POST", req.method);
            assertEquals("/hello/url", req.uri);
            assertEquals("HTTP/1.1", req.version);

            assertEquals("Host", req.headers.get(0).key);
            assertEquals("www.example.com", req.headers.get(0).value);

            assertEquals("Hello", req.headers.get(1).key);
            assertEquals("World", req.headers.get(1).value);

            assertEquals("Transfer-Encoding", req.headers.get(2).key);
            assertEquals("chunked", req.headers.get(2).value);
        }
        LinkedHashMap<byte[], ByteArray> chunks = new LinkedHashMap<>() {{
            put("1a  \r\n".getBytes(), ByteArray.from("01234567890123456789012345".getBytes()));
            put("3 ; some-extension\r\n".getBytes(), ByteArray.from("012".getBytes()));
        }};
        for (byte[] chunk : chunks.keySet()) {
            ByteArray content = chunks.get(chunk);
            for (int i = 0; i < chunk.length; i++) {
                byte b = chunk[i];
                int len = front.unittest_len();
                assertEquals(-1, len);
                ByteArray a = ByteArray.from(b);
                if (i != chunk.length - 1) {
                    assertNull(front.unittest_feed(a));
                } else {
                    assertEquals(ByteArray.from(chunk), front.unittest_feed(a));
                }
            }
            int len = front.unittest_len();
            assertEquals(content.length(), len);
            assertNull(front.unittest_feed(ByteArray.from('\r')));
            assertEquals(ByteArray.from("\r\n"), front.unittest_feed(ByteArray.from('\n')));
        }

        byte[] lastChunk = "0\r\n".getBytes();
        for (int i = 0; i < lastChunk.length; i++) {
            byte b = lastChunk[i];
            int len = front.unittest_len();
            assertEquals(-1, len);
            ByteArray a = ByteArray.from(b);
            if (i != lastChunk.length - 1) {
                assertNull(front.unittest_feed(a));
            } else {
                assertEquals(ByteArray.from(lastChunk), front.unittest_feed(a));
            }
        }
        req = front.unittest_getReq();
        {
            assertEquals(3, req.chunks.size());

            assertEquals(26, req.chunks.get(0).size);
            assertNull(req.chunks.get(0).extension);
            // data is not feed: assertEquals(ByteArray.from("01234567890123456789012345".getBytes()), req.chunks.get(0).content);

            assertEquals(3, req.chunks.get(1).size);
            assertEquals("some-extension", req.chunks.get(1).extension);
            // data is not feed: assertEquals(ByteArray.from("012".getBytes()), req.chunks.get(1).content);

            assertEquals(0, req.chunks.get(2).size);
            assertNull(req.chunks.get(2).extension);
            assertNull(req.chunks.get(2).content);
        }

        return front;
    }

    private HttpSubContext chunkResponseNoEnd() throws Exception {
        Processor<HttpContext, HttpSubContext> p = new HttpProcessor();
        HttpContext ctx = p.init(new Processor.ContextInitParams(null));
        HttpSubContext backend = p.initSub(new Processor.SubContextInitParams<>(
            ctx, 1, null
        ));

        String respHead =
            "HTTP/1.1 200 OK\r\n" +
            "Content-Type: application/json\r\n" +
            "Transfer-Encoding: chunked\r\n" +
            "\r\n";
        byte[] respHeadBytes = respHead.getBytes();

        for (int i = 0; i < respHeadBytes.length; i++) {
            byte b = respHeadBytes[i];
            int len = backend.unittest_len();
            assertEquals(-1, len);
            ByteArray a = ByteArray.from(b);
            if (i != respHeadBytes.length - 1) {
                assertNull(backend.unittest_feed(a));
            } else {
                assertEquals(ByteArray.from(respHeadBytes), backend.unittest_feed(a));
            }
        }
        Response resp = backend.unittest_getResp();
        {
            assertEquals("HTTP/1.1", resp.version);
            assertEquals(200, resp.statusCode);
            assertEquals("OK", resp.reason);

            assertEquals("Content-Type", resp.headers.get(0).key);
            assertEquals("application/json", resp.headers.get(0).value);

            assertEquals("Transfer-Encoding", resp.headers.get(1).key);
            assertEquals("chunked", resp.headers.get(1).value);
        }
        LinkedHashMap<byte[], ByteArray> chunks = new LinkedHashMap<>() {{
            put("1a  \r\n".getBytes(), ByteArray.from("01234567890123456789012345".getBytes()));
            put("3 ; some-extension\r\n".getBytes(), ByteArray.from("012".getBytes()));
        }};
        for (byte[] chunk : chunks.keySet()) {
            ByteArray content = chunks.get(chunk);
            for (int i = 0; i < chunk.length; i++) {
                byte b = chunk[i];
                int len = backend.unittest_len();
                assertEquals(-1, len);
                ByteArray a = ByteArray.from(b);
                if (i != chunk.length - 1) {
                    assertNull(backend.unittest_feed(a));
                } else {
                    assertEquals(ByteArray.from(chunk), backend.unittest_feed(a));
                }
            }
            int len = backend.unittest_len();
            assertEquals(content.length(), len);
            assertNull(backend.unittest_feed(ByteArray.from('\r')));
            assertEquals(ByteArray.from("\r\n"), backend.unittest_feed(ByteArray.from('\n')));
        }
        byte[] lastChunkAndEnd = "0\r\n".getBytes();
        for (int i = 0; i < lastChunkAndEnd.length; i++) {
            byte b = lastChunkAndEnd[i];
            int len = backend.unittest_len();
            assertEquals(-1, len);
            ByteArray a = ByteArray.from(b);
            if (i != lastChunkAndEnd.length - 1) {
                assertNull(backend.unittest_feed(a));
            } else {
                assertEquals(ByteArray.from("0\r\n"), backend.unittest_feed(a));
            }
        }
        resp = backend.unittest_getResp();
        {
            assertEquals(3, resp.chunks.size());

            assertEquals(26, resp.chunks.get(0).size);
            assertNull(resp.chunks.get(0).extension);
            // data not feed: assertEquals(ByteArray.from("01234567890123456789012345".getBytes()), resp.chunks.get(0).content);

            assertEquals(3, resp.chunks.get(1).size);
            assertEquals("some-extension", resp.chunks.get(1).extension);
            // data not feed: assertEquals(ByteArray.from("012".getBytes()), resp.chunks.get(1).content);

            assertEquals(0, resp.chunks.get(2).size);
            assertNull(resp.chunks.get(2).extension);
            assertNull(resp.chunks.get(2).content);
        }

        return backend;
    }

    @Test
    public void chunkRequest() throws Exception {
        HttpSubContext front = chunkRequestNoEnd();
        byte[] end = "\r\n".getBytes();
        for (int i = 0; i < end.length; i++) {
            byte b = end[i];
            int len = front.unittest_len();
            assertEquals(-1, len);
            ByteArray a = ByteArray.from(b);
            if (i != end.length - 1) {
                assertNull(front.unittest_feed(a));
            } else {
                assertEquals(ByteArray.from(end), front.unittest_feed(a));
            }
        }
        assertTrue(front.isIdle());
    }

    @Test
    public void chunkRequestTrailers() throws Exception {
        HttpSubContext front = chunkRequestNoEnd();
        byte[] trailersAndEnd = (
            "A-Trail: value1\r\n" +
            "B-Trail: value2\r\n" +
            "\r\n"
        ).getBytes();
        for (int i = 0; i < trailersAndEnd.length; i++) {
            byte b = trailersAndEnd[i];
            int len = front.unittest_len();
            assertEquals(-1, len);
            ByteArray a = ByteArray.from(b);
            if (i != trailersAndEnd.length - 1) {
                assertNull(front.unittest_feed(a));
            } else {
                assertEquals(ByteArray.from(trailersAndEnd), front.unittest_feed(a));
            }
        }
        assertTrue(front.isIdle());
        Request req = front.unittest_getReq();
        assertEquals(2, req.trailers.size());
        assertEquals("A-Trail", req.trailers.get(0).key);
        assertEquals("value1", req.trailers.get(0).value);
        assertEquals("B-Trail", req.trailers.get(1).key);
        assertEquals("value2", req.trailers.get(1).value);
    }

    @Test
    public void chunkResponse() throws Exception {
        HttpSubContext backend = chunkResponseNoEnd();
        byte[] lastChunkAndEnd = "\r\n".getBytes();
        for (int i = 0; i < lastChunkAndEnd.length; i++) {
            byte b = lastChunkAndEnd[i];
            int len = backend.unittest_len();
            assertEquals(-1, len);
            ByteArray a = ByteArray.from(b);
            if (i != lastChunkAndEnd.length - 1) {
                assertNull(backend.unittest_feed(a));
            } else {
                assertEquals(ByteArray.from(lastChunkAndEnd), backend.unittest_feed(a));
            }
        }
        assertTrue(backend.isIdle());
    }

    @Test
    public void chunkResponseTrailers() throws Exception {
        HttpSubContext backend = chunkResponseNoEnd();
        byte[] trailersAndEnd = (
            "A-Trail: value1\r\n" +
            "B-Trail: value2\r\n" +
            "\r\n"
        ).getBytes();
        for (int i = 0; i < trailersAndEnd.length; i++) {
            byte b = trailersAndEnd[i];
            int len = backend.unittest_len();
            assertEquals(-1, len);
            ByteArray a = ByteArray.from(b);
            if (i != trailersAndEnd.length - 1) {
                assertNull(backend.unittest_feed(a));
            } else {
                assertEquals(ByteArray.from(trailersAndEnd), backend.unittest_feed(a));
            }
        }
        assertTrue(backend.isIdle());
        Response resp = backend.unittest_getResp();
        assertEquals(2, resp.trailers.size());
        assertEquals("A-Trail", resp.trailers.get(0).key);
        assertEquals("value1", resp.trailers.get(0).value);
        assertEquals("B-Trail", resp.trailers.get(1).key);
        assertEquals("value2", resp.trailers.get(1).value);
    }

    @Test
    public void feedHalfReqBody() throws Exception {
        Processor<HttpContext, HttpSubContext> p = new HttpProcessor();
        HttpContext ctx = p.init(ctxInitParams);
        HttpSubContext front = p.initSub(new Processor.SubContextInitParams<>(
            ctx, 0, null
        ));
        var data = ByteArray.from(
            "GET /index.html HTTP/1.1\r\n" +
            "Host: vproxy.io\r\n" +
            "Content-Length: 10\r\n" +
            "\r\n" +
            "123"
        );
        var res = front.unittest_feed(data);
        assertEquals(ByteArray.from(
            "GET /index.html HTTP/1.1\r\n" +
            "Host: vproxy.io\r\n" +
            "Content-Length: 10\r\n" +
            "X-Forwarded-For: " + forwardedFor + "\r\n" +
            "X-Client-Port: " + clientPort + "\r\n" +
            "\r\n" +
            "123"
        ), res);
        assertEquals(7, front.unittest_len());
        assertEquals(Processor.Mode.proxy, front.unittest_mode());
    }

    @Test
    public void feedHalfRespBody() throws Exception {
        Processor<HttpContext, HttpSubContext> p = new HttpProcessor();
        HttpContext ctx = p.init(new Processor.ContextInitParams(null));
        HttpSubContext backend = p.initSub(new Processor.SubContextInitParams<>(
            ctx, 1, null
        ));
        var data = ByteArray.from(
            "HTTP/1.1 200 OK\r\n" +
            "Host: vproxy.io\r\n" +
            "Content-Length: 10\r\n" +
            "\r\n" +
            "123456"
        );
        var res = backend.unittest_feed(data);
        assertEquals(data, res);
        assertEquals(4, backend.unittest_len());
        assertEquals(Processor.Mode.proxy, backend.unittest_mode());
    }

    @Test
    public void feedHalfReqChunk() throws Exception {
        Processor<HttpContext, HttpSubContext> p = new HttpProcessor();
        HttpContext ctx = p.init(ctxInitParams);
        HttpSubContext front = p.initSub(new Processor.SubContextInitParams<>(
            ctx, 0, null
        ));
        var data = ByteArray.from(
            "GET /index.html HTTP/1.1\r\n" +
            "Host: vproxy.io\r\n" +
            "Transfer-Encoding: chunked\r\n" +
            "\r\n" +
            "a\r\n" +
            "123"
        );
        var res = front.unittest_feed(data);
        assertEquals(ByteArray.from(
            "GET /index.html HTTP/1.1\r\n" +
            "Host: vproxy.io\r\n" +
            "Transfer-Encoding: chunked\r\n" +
            "X-Forwarded-For: " + forwardedFor + "\r\n" +
            "X-Client-Port: " + clientPort + "\r\n" +
            "\r\n" +
            "a\r\n" +
            "123"
        ), res);
        assertEquals(7, front.unittest_len());
        assertEquals(Processor.Mode.proxy, front.unittest_mode());
    }

    @Test
    public void feedHalfRespChunk() throws Exception {
        Processor<HttpContext, HttpSubContext> p = new HttpProcessor();
        HttpContext ctx = p.init(new Processor.ContextInitParams(null));
        HttpSubContext backend = p.initSub(new Processor.SubContextInitParams<>(
            ctx, 1, null
        ));
        var data = ByteArray.from(
            "HTTP/1.1 200 OK\r\n" +
            "Host: vproxy.io\r\n" +
            "Transfer-Encoding: chunked\r\n" +
            "\r\n" +
            "a\r\n" +
            "123456"
        );
        var res = backend.unittest_feed(data);
        assertEquals(data, res);
        assertEquals(4, backend.unittest_len());
        assertEquals(Processor.Mode.proxy, backend.unittest_mode());
    }

    @Test
    public void gZipResponse() {
        Response originResponse = new Response();
        originResponse.version = "HTTP/1.1";
        originResponse.statusCode = 200;
        originResponse.reason = "OK";
        originResponse.isPlain = true;
        LinkedList<Header> headers = new LinkedList<>();
        headers.add(new Header("Content-Type", "text/plain; charset=UTF-8"));
        headers.add(new Header("Content-Encoding", "gzip"));
        originResponse.headers = headers;
        String s = "Hello, world!";
        originResponse.body = ByteArray.from(s.getBytes(StandardCharsets.UTF_8));
        ByteArray originResponseBytes = originResponse.toByteArray();

        var parser = new HttpRespParser();
        int res = parser.feed(originResponseBytes.toFullChannel());
        assertEquals(0, res);
        Response newResponse = parser.getResult();

        assertEquals("HTTP/1.1", newResponse.version);
        assertEquals(200, newResponse.statusCode);
        assertEquals("OK", newResponse.reason);
        assertEquals("Content-Type", newResponse.headers.get(0).key);
        assertEquals("text/plain; charset=UTF-8", newResponse.headers.get(0).value);
        assertEquals("Content-Encoding", newResponse.headers.get(1).key);
        assertEquals("gzip", newResponse.headers.get(1).value);
        assertEquals("Content-Length", newResponse.headers.get(2).key);
        assertEquals("33", newResponse.headers.get(2).value);

        assertArrayEquals(originResponse.body.toGZipJavaByteArray(), newResponse.body.toJavaArray());
        assertArrayEquals(originResponseBytes.toJavaArray(), newResponse.toByteArray().toJavaArray());
    }

    @Test
    public void gZipRequest() {
        Request originRequest = new Request();
        originRequest.method = "POST";
        originRequest.uri = "/user/1";
        originRequest.version = "HTTP/1.1";
        String s = "{\"username\":\"admin\"}";
        originRequest.body = ByteArray.from(s.getBytes(StandardCharsets.UTF_8));
        LinkedList<Header> headers = new LinkedList<>();
        headers.add(new Header("Host", "www.example.com"));
        headers.add(new Header("Content-Encoding", "gzip"));
        headers.add(new Header("Content-Type", "application/json"));
        originRequest.headers = headers;
        originRequest.isPlain = true;
        ByteArray originRequestBytes = originRequest.toByteArray();

        var parser = new HttpReqParser();
        var res = parser.feed(originRequestBytes.toFullChannel());
        assertEquals(0, res);
        Request newRequest = parser.getResult();
        assertArrayEquals(newRequest.body.toJavaArray(), originRequest.body.toGZipJavaByteArray());
        assertArrayEquals(newRequest.toByteArray().toJavaArray(), originRequest.toByteArray().toJavaArray());
    }
}
