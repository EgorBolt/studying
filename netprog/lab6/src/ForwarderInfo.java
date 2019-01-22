import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

class ForwarderInfo {
    private SocketChannel from;
    private SocketChannel to;
    private ByteBuffer buffer;

    ForwarderInfo(SocketChannel whereToWrite, SocketChannel from, ByteBuffer buffer)
    {
        this.from = from;
        this.to = whereToWrite;
        this.buffer = buffer;
    }

    ByteBuffer getToWrite() {
        return buffer;
    }

    SocketChannel getTo() {
        return to;
    }

    SocketChannel getFrom() {
        return from;
    }

}
