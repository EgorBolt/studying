package Socks5Proxy;

import org.xbill.DNS.*;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Map;

class ProxyInfo {
    private SocketChannel from;
    private SocketChannel to;
    private ByteBuffer buffer;
    private short port;

    ProxyInfo(SocketChannel from, SocketChannel to, ByteBuffer buffer) {
        this.from = from;
        this.to = to;
        this.buffer = buffer;
    }

    SocketChannel getFrom() {
        return from;
    }

    SocketChannel getTo() {
        return to;
    }

    void setTo(SocketChannel to) {
        this.to = to;
    }

    ByteBuffer getBuffer() {
        return buffer;
    }

    void setBuffer(ByteBuffer buffer)
    {
        this.buffer = buffer;
    }

    int getPort() {
        return port;
    }

    private void setPort(short port) {
        this.port = port;
    }

    static void parseHeader(ByteBuffer buf, SelectionKey key, InetSocketAddress dnsAddress, DatagramChannel dns, Map<String, ProxyInfo> dnsNames) throws IOException {
        ProxyInfo proxyInfo = (ProxyInfo) key.attachment();
        SocketChannel keyChannel = (SocketChannel) key.channel();
        byte[] header = buf.array();
        int byteCount = buf.position();

        if(header[0] != 5) {
            System.out.println("This is not SOCKS5 connection.");
            return;
        }

        if(header[1] == byteCount - 2) {
            ByteBuffer b = ByteBuffer.allocate(2);
            byte[] twoBytes = new byte[] {5, 0};
            b.put(twoBytes);
            b.flip();
            keyChannel.write(b);
        }
        else {
            if(header[1] != 1) {
                System.out.println("This proxy supports only TCP connections!");
                return;
            }

            if(header[3] == 1) {
                SocketChannel remoteServer = SocketChannel.open();
                remoteServer.configureBlocking(false);

                byte[] address = new byte[4];
                short port = ByteBuffer.wrap(new byte[]{header[8], header[9]}).getShort();
                System.arraycopy(header, 4, address, 0, 4);
                if (!remoteServer.connect(new InetSocketAddress(InetAddress.getByAddress(address), port))) {
                    remoteServer.register(key.selector(), SelectionKey.OP_CONNECT, new ProxyInfo(keyChannel, remoteServer, null));
                }
                proxyInfo.setTo(remoteServer);

                byte[] response = new byte[] {5, 0, 0, 1, 0, 0, 0, 0, 0, 0};
                ByteBuffer b = ByteBuffer.allocate(response.length);
                b.put(response);
                b.flip();
                keyChannel.write(b);
            }
            else if(header[3] == 3) {
                Message message = new Message();
                byte len = header[4];
                byte[] domainBytes = new byte[len];
                System.arraycopy(header, 5, domainBytes, 0, len);
                String domainName = new String(domainBytes, StandardCharsets.UTF_8);
                short port = ByteBuffer.wrap(new byte[]{header[5 + len], header[5 + len + 1]}).getShort();

                Record record = Record.newRecord(new Name(domainName + "."), Type.A, DClass.IN);
                message.addRecord(record, Section.QUESTION);
                message.getHeader().setFlag(Flags.RD);

                byte[] messageBytes = message.toWire();
                ByteBuffer b = ByteBuffer.allocate(messageBytes.length);
                b.put(messageBytes);
                b.flip();
                dns.send(b, dnsAddress);

                proxyInfo.setPort(port);
                dnsNames.put(domainName, proxyInfo);
            }
        }
    }

}
