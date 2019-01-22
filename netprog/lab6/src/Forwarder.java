import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

class Forwarder {
    private int lport;
    private String rhost;
    private int rport;

    Forwarder(int lport, String rhost, int rport) {
        this.lport = lport;
        this.rhost = rhost;
        this.rport = rport;
    }

    void doJob() {
        try (Selector selector = Selector.open();
             ServerSocketChannel serverSocket = ServerSocketChannel.open()
        ) {
            SocketAddress listenAddress = new InetSocketAddress("localhost", lport);
            SocketAddress target = new InetSocketAddress(InetAddress.getByName(rhost), rport);

            serverSocket.bind(listenAddress);
            serverSocket.configureBlocking(false);
            serverSocket.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("Forwarder started...");

//            TODO: Сделать чтение ТОЛЬКО ПОСЛЕ ТОГО, как я приконнектился к серверу
//            TODO: Сделать для каждого соединения свой персональный буффер
//            TODO: Сделать дополнительный класс для хранения соответствия сооединений, буффера для каждого процесса и так далее

            //noinspection InfiniteLoopStatement
            while (true) {
                selector.select();
                Iterator<SelectionKey> keys = selector.selectedKeys().iterator();

                while (keys.hasNext()) {
                    SelectionKey key = keys.next();

                    if (key.isAcceptable()) {
                        SocketChannel from = serverSocket.accept();
                        SocketChannel to = SocketChannel.open();
                        from.configureBlocking(false);
                        to.configureBlocking(false);

                        to.connect(target);
                        to.register(key.selector(), SelectionKey.OP_CONNECT, new ForwarderInfo(to, from, null));
                        from.register(key.selector(), SelectionKey.OP_READ, new ForwarderInfo(to, from, null));
                    }
                    else if (key.isConnectable()) {
                        SocketChannel channel = (SocketChannel) key.channel();
                        ForwarderInfo attachment = (ForwarderInfo) key.attachment();

                        if (channel.finishConnect()) {
                            if (attachment.getToWrite() != null) {
                                key.interestOps(SelectionKey.OP_WRITE);
                            } else {
                                key.cancel();
                            }
                        }
                    }

                    else if (key.isReadable()) {
                        SocketChannel channel = (SocketChannel) key.channel();
                        ByteBuffer buffer = ByteBuffer.allocate(4096);
                        ForwarderInfo newInfo;

                        int haveRead = channel.read(buffer);
                        if (haveRead == -1) {
                            continue;
                        }

                        buffer.flip();
                        ForwarderInfo attachment = (ForwarderInfo) key.attachment();
                        SocketChannel from = attachment.getFrom();
                        SocketChannel to = attachment.getTo();

                        if (to.isConnected() && haveRead != 0) {
                            newInfo = new ForwarderInfo(from, to, null);
                            int haveWritten = to.write(buffer);

                            if(haveWritten != haveRead) {
                                to.register(key.selector(), SelectionKey.OP_WRITE, newInfo);
                            }
                            else {
                                buffer.clear();
                                to.register(key.selector(), SelectionKey.OP_READ, newInfo);
                            }
                        } else {
                            newInfo = new ForwarderInfo(from, to, buffer);
                            to.register(key.selector(), SelectionKey.OP_CONNECT, newInfo);
                        }
                    }

                    else if (key.isWritable()) {
                        ForwarderInfo info = (ForwarderInfo) key.attachment();
                        ByteBuffer buffer = info.getToWrite();
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
//10080 fit.ippolitov.me 80




