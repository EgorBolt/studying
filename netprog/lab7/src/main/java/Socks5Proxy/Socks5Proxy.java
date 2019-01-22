package Socks5Proxy;

import org.xbill.DNS.*;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

class Socks5Proxy {
    private short port;
    private int bufSize;

    Socks5Proxy(short port) {
        this.port = port;
        this.bufSize = 4096;
    }

    void doJob() {
        try (Selector selector = Selector.open();
             ServerSocketChannel serverSocket = ServerSocketChannel.open();
             DatagramChannel dns = DatagramChannel.open())
        {
            SocketAddress listenAddress = new InetSocketAddress("localhost", port);
            String dnsServers[] = ResolverConfig.getCurrentConfig().servers();
            InetSocketAddress dnsAddress = new InetSocketAddress(InetAddress.getByName(dnsServers[0]), 100);
            Map<String, ProxyInfo> dnsNames = new HashMap<>();

            serverSocket.bind(listenAddress);
            serverSocket.configureBlocking(false);
            serverSocket.register(selector, SelectionKey.OP_ACCEPT);

            dns.connect(dnsAddress);
            dns.configureBlocking(false);
            dns.register(selector, SelectionKey.OP_READ);
            System.out.println("Proxy started...");

            //noinspection InfiniteLoopStatement
            while (true) {
                selector.select();
                Iterator<SelectionKey> keys = selector.selectedKeys().iterator();

                while (keys.hasNext()) {
                    SelectionKey key = keys.next();

                    if (key.isAcceptable()) {
                        SocketChannel client = serverSocket.accept();

                        client.configureBlocking(false);
                        client.register(key.selector(), SelectionKey.OP_READ);
                    } else if (key.isConnectable()) {
                        SocketChannel channel = (SocketChannel) key.channel();
                        ProxyInfo attachment = (ProxyInfo) key.attachment();

                        if (channel.finishConnect()) {
                            if (attachment.getBuffer() != null) {
                                key.interestOps(SelectionKey.OP_WRITE);
                            } else {
                                key.cancel();
                            }
                        }

                    } else if (key.isReadable()) {
                        ByteBuffer buf = ByteBuffer.allocate(bufSize);
                        Channel channel = key.channel();

                        if (channel == dns) {
                            DNS.isChannelDNS(key, buf, bufSize, dns, dnsNames);
                        }
                        else {
                            SocketChannel keyChannel = (SocketChannel) key.channel();
                            ProxyInfo proxyInfo = (ProxyInfo) key.attachment();
                            ProxyInfo newInfo;
                            int haveRead;
                            int haveWritten;

                            if (proxyInfo == null) {
                                proxyInfo = new ProxyInfo(keyChannel, null,null);
                                key.attach(proxyInfo);
                            }
                            try {
                                haveRead = keyChannel.read(buf);
                                if (haveRead == -1) {
                                    key.cancel();
                                    continue;
                                }
                            } catch (IOException e) {
                                key.cancel();
                                continue;
                            }

                            if (proxyInfo.getTo() == null) {
                                ProxyInfo.parseHeader(buf, key, dnsAddress, dns, dnsNames);
                            } else {
                                SocketChannel remote = proxyInfo.getTo();
                                buf.flip();
                                if (remote.isConnected() && haveRead != 0) {
                                    try {
                                        haveWritten = remote.write(buf);
                                    }
                                    catch(IOException error) {
                                        key.cancel();
                                        continue;
                                    }
                                    if (haveWritten != haveRead) {
                                        proxyInfo.setBuffer(buf);
                                        remote.register(key.selector(), SelectionKey.OP_WRITE, proxyInfo);
                                    } else {
                                        buf.clear();
                                        newInfo = new ProxyInfo(proxyInfo.getTo(), proxyInfo.getFrom(), null);
                                        remote.register(key.selector(), SelectionKey.OP_READ, newInfo);
                                    }
                                } else {
                                    newInfo = new ProxyInfo(proxyInfo.getTo(), proxyInfo.getFrom(), buf);
                                    remote.register(key.selector(), SelectionKey.OP_CONNECT, newInfo);
                                }
                            }
                        }
                    } else if (key.isWritable()) {
                        ProxyInfo info = (ProxyInfo) key.attachment();
                        ByteBuffer buffer = info.getBuffer();
                        SocketChannel channel = (SocketChannel) key.channel();

                        if (buffer != null) {
                            channel.write(buffer);
                            buffer.clear();
                        }
                        key.interestOps(SelectionKey.OP_READ);

                    }
                    keys.remove();
                }
            }
        }
        catch (IOException eIO) {
            System.err.println("ERROR: Something wrong in doJob.");
            eIO.printStackTrace();
            System.exit(-3);
        }
    }
}

