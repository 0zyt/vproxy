package io.vproxy.msquic;

import io.vproxy.pni.*;
import io.vproxy.pni.hack.*;
import io.vproxy.pni.array.*;
import java.lang.foreign.*;
import java.lang.invoke.*;
import java.nio.ByteBuffer;

public class CxPlatExecutionState extends AbstractNativeObject implements NativeObject {
    private static final MethodHandle __getLayoutByteSizeMH = PanamaUtils.lookupPNICriticalFunction(new PNILinkOptions().setCritical(true), long.class, "JavaCritical_io_vproxy_msquic_CxPlatExecutionState___getLayoutByteSize");

    private static long __getLayoutByteSize() {
        long RESULT;
        try {
            RESULT = (long) __getLayoutByteSizeMH.invokeExact();
        } catch (Throwable THROWABLE) {
            throw PanamaUtils.convertInvokeExactException(THROWABLE);
        }
        return RESULT;
    }

    public static final MemoryLayout LAYOUT = PanamaUtils.padLayout(__getLayoutByteSize(), MemoryLayout::structLayout

    );
    public final MemorySegment MEMORY;

    @Override
    public MemorySegment MEMORY() {
        return MEMORY;
    }

    public CxPlatExecutionState(MemorySegment MEMORY) {
        MEMORY = MEMORY.reinterpret(LAYOUT.byteSize());
        this.MEMORY = MEMORY;
        long OFFSET = 0;
    }

    public CxPlatExecutionState(Allocator ALLOCATOR) {
        this(ALLOCATOR.allocate(LAYOUT));
    }

    @Override
    public void toString(StringBuilder SB, int INDENT, java.util.Set<NativeObjectTuple> VISITED, boolean CORRUPTED_MEMORY) {
        if (!VISITED.add(new NativeObjectTuple(this))) {
            SB.append("<...>@").append(Long.toString(MEMORY.address(), 16));
            return;
        }
        SB.append("CxPlatExecutionState{\n");
        SB.append(" ".repeat(INDENT)).append("}@").append(Long.toString(MEMORY.address(), 16));
    }

    public static class Array extends RefArray<CxPlatExecutionState> {
        public Array(MemorySegment buf) {
            super(buf, CxPlatExecutionState.LAYOUT);
        }

        public Array(Allocator allocator, long len) {
            super(allocator, CxPlatExecutionState.LAYOUT, len);
        }

        public Array(PNIBuf buf) {
            super(buf, CxPlatExecutionState.LAYOUT);
        }

        @Override
        protected void elementToString(io.vproxy.msquic.CxPlatExecutionState ELEM, StringBuilder SB, int INDENT, java.util.Set<NativeObjectTuple> VISITED, boolean CORRUPTED_MEMORY) {
            ELEM.toString(SB, INDENT, VISITED, CORRUPTED_MEMORY);
        }

        @Override
        protected String toStringTypeName() {
            return "CxPlatExecutionState.Array";
        }

        @Override
        protected CxPlatExecutionState construct(MemorySegment seg) {
            return new CxPlatExecutionState(seg);
        }

        @Override
        protected MemorySegment getSegment(CxPlatExecutionState value) {
            return value.MEMORY;
        }
    }

    public static class Func extends PNIFunc<CxPlatExecutionState> {
        private Func(io.vproxy.pni.CallSite<CxPlatExecutionState> func) {
            super(func);
        }

        private Func(io.vproxy.pni.CallSite<CxPlatExecutionState> func, Options opts) {
            super(func, opts);
        }

        private Func(MemorySegment MEMORY) {
            super(MEMORY);
        }

        public static Func of(io.vproxy.pni.CallSite<CxPlatExecutionState> func) {
            return new Func(func);
        }

        public static Func of(io.vproxy.pni.CallSite<CxPlatExecutionState> func, Options opts) {
            return new Func(func, opts);
        }

        public static Func of(MemorySegment MEMORY) {
            return new Func(MEMORY);
        }

        @Override
        protected String toStringTypeName() {
            return "CxPlatExecutionState.Func";
        }

        @Override
        protected CxPlatExecutionState construct(MemorySegment seg) {
            return new CxPlatExecutionState(seg);
        }
    }
}
// metadata.generator-version: pni 21.0.0.20
// sha256:1c0b38eb3170b9a9f05a2b1d26816779f7cab73e23054ded3c56b79aa30f7263
