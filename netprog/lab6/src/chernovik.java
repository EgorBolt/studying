//import java.io.IOException;
//import java.net.InetAddress;
//import java.net.InetSocketAddress;
//import java.net.SocketAddress;
//import java.nio.ByteBuffer;
//import java.nio.channels.*;
//import java.util.Iterator;
//import java.nio.charset.Charset;
//
//public class Forwarder {
//    private int lport;
//    private String rhost;
//    private int rport;
//
//    public Forwarder(int lport, String rhost, int rport) {
//        this.lport = lport;
//        this.rhost = rhost;
//        this.rport = rport;
//    }
//
//
//    public void doJob() {
//        int id = 0;
//        final int BUFFER_SIZE = 65536;
//
//        try (
//                Selector selector = Selector.open();
//                ServerSocketChannel serverSocket = ServerSocketChannel.open();
//        ) {
//
//            SocketAddress listenAddress = new InetSocketAddress("localhost", lport);
//            SocketAddress target = new InetSocketAddress(InetAddress.getByName(rhost), rport);
//
//            serverSocket.configureBlocking(false);
//            serverSocket.bind(listenAddress);
//            serverSocket.register(selector, SelectionKey.OP_ACCEPT);
//            System.out.println("Forwarder started...");
//
//            TODO: Сделать чтение ТОЛЬКО ПОСЛЕ ТОГО, как я приконнектился к серверу
//            TODO: Сделать для каждого соединения свой персональный буффер
//            TODO: Сделать дополнительный класс для хранения соответствия сооединений, буффера для каждого процесса и так далее
//
//            while (true) {
//                selector.select();
//                Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
//                int size = selector.selectedKeys().size();
//
//                while(keys.hasNext()) {
//
//                    SelectionKey key = keys.next();
//
//                    if (key.isAcceptable()) {
//                        SocketChannel from = serverSocket.accept();
//                        SocketChannel to = SocketChannel.open();
//                        from.configureBlocking(false);
//                        to.configureBlocking(false);
//                        ByteBuffer buffer = ByteBuffer.allocate(65536);
//
//                        ForwarderInfo fi = new ForwarderInfo(from, to, buffer, id);
//
//                        id++;
//
//                        to.register(selector, SelectionKey.OP_CONNECT, fi);
//
//                    } else if (key.isConnectable()) {
//                        ForwarderInfo attachment = (ForwarderInfo)key.attachment();
//                        int channelId = attachment.getId();
//                        SocketChannel channel = (SocketChannel)key.channel();
//
//                        channel.connect(target);
//                        if (channel.finishConnect()) {
//                            System.out.println("connected");
//                            attachment.getFrom().register(selector, SelectionKey.OP_READ, attachment);
//                        } else {
//                            System.err.println("cant connect, terminating");
//                            System.exit(-100);
//                        }
//
//                    } else if (key.isReadable()) {
//                        SocketChannel channel = (SocketChannel)key.channel();
//                        ForwarderInfo attachment = (ForwarderInfo)key.attachment();
//
//                        SocketChannel from = attachment.getFrom();
//                        SocketChannel to = attachment.getTo();
//                        ByteBuffer buffer = attachment.getInfo();
//                        int channelId = attachment.getId();
//                        buffer.clear();
//
//                        int haveRead = channel.read(buffer);
//                        String v = new String(buffer.array(), Charset.forName("UTF-8"));
//
//                        if (haveRead != -1) {
//                            buffer.flip();
//                            ForwarderInfo newInfo = new ForwarderInfo(from, to, buffer, channelId);
//                            if (channel.equals(attachment.getFrom())) {
//                                to.register(selector, SelectionKey.OP_WRITE, newInfo);
//                            } else {
//                                from.register(selector, SelectionKey.OP_WRITE, newInfo);
//                            }
//                        }
//                        else {
//                            channel.close();
//                        }
//
//                    } else if (key.isWritable()) {
//                        ForwarderInfo info = (ForwarderInfo)key.attachment();
//                        SocketChannel channel = (SocketChannel)key.channel();
//                        boolean isConnected = channel.isConnected();
//                        ByteBuffer buffer = info.getInfo();
//                        int channelId = info.getId();
//                        String v = new String(buffer.array(), Charset.forName("UTF-8"));
//
//                        if (isConnected) {
//                            channel.write(buffer);
//                        }
//
//                        if (channel.equals(info.getTo())) {
//                            channel.register(selector, SelectionKey.OP_READ, info);
//                        }
//                    }
//
//                    keys.remove();
//                }
//            }
//        } catch (IOException eIO) {
//            System.err.println("ERROR: Something wrong in doJob.");
//            eIO.printStackTrace();
//            System.exit(-3);
//        }
//    }
//}

//import java.nio.ByteBuffer;
//import java.nio.channels.SocketChannel;
//
//public class ForwarderInfo {
//    private SocketChannel from;
//    private SocketChannel to;
//    private ByteBuffer info;
//
//    public ForwarderInfo(SocketChannel from, SocketChannel to, ByteBuffer info) {
//        this.from = from;
//        this.to = to;
//        this.info = info;
//    }
//
//    public void setFrom(SocketChannel channel) {
//        this.from = channel;
//    }
//
//    public void setTo(SocketChannel channel) {
//        this.to = channel;
//    }
//
//    public void setInfo(ByteBuffer buffer) {
//        this.info = buffer;
//    }
//
//    public SocketChannel getFrom() {
//        return this.from;
//    }
//
//    public SocketChannel getTo() {
//        return this.to;
//    }
//
//    public ByteBuffer getInfo() {
//        return this.info;
//    }
//
//    public void flipInfo() {
//        this.info.flip();
//    }
//}