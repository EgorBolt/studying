package Socks5Proxy;

import org.xbill.DNS.*;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Map;

class DNS {
    static void isChannelDNS(SelectionKey key, ByteBuffer buf, int bufSize, DatagramChannel dns, Map<String, ProxyInfo> dnsNames) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(bufSize);
        Message message = new Message(buffer.array());
        Record[] response = message.getSectionArray(Section.ANSWER);
        byte[] answer = new byte[] {5, 0, 0, 1, 0, 0, 0, 0, 0, 0};
        ByteBuffer from = ByteBuffer.allocate(response.length);
        byte[] address;
        String name = null;
        String aString = null;
        String cname;
        InetAddress inetAddress = null;
        int i;

        dns.read(buf);

        for (i = 0; i < response.length; i++) {
            if (response[i].getType() == Type.CNAME) {
                cname = response[i].getName().toString();
                if(name == null) {
                    name = cname.substring(0, cname.length() - 1);
                }
                aString = ((CNAMERecord)response[i]).getAlias().toString();
            }
        }
        for (i = 0; i < response.length; i++) {
            if(response[i].getType() == Type.A) {
                String alias = response[i].getName().toString();

                if(alias.equals(aString)) {
                    inetAddress = ((ARecord) response[i]).getAddress();
                    break;
                }
                else if (aString == null) {
                    name = response[i].getName().toString();
                    name = name.substring(0, name.length() - 1);
                    inetAddress = ((ARecord) response[i]).getAddress();
                    break;
                }
            }
        }

        if (inetAddress == null) {
            dnsNames.remove(name);
            return;
        }

        address = inetAddress.getAddress();

        Iterator<Map.Entry<String, ProxyInfo>> dnsIterator = dnsNames.entrySet().iterator();
        while(dnsIterator.hasNext()) {
            Map.Entry<String, ProxyInfo> dnsMap = dnsIterator.next();

            if (dnsMap.getKey().equals(name)) {
                SocketChannel to = SocketChannel.open();
                to.configureBlocking(false);

                ProxyInfo proxyInfo = dnsMap.getValue();
                int port = proxyInfo.getPort();

                if (!to.connect(new InetSocketAddress(InetAddress.getByAddress(address), port))) {
                    ProxyInfo newInfo = new ProxyInfo(proxyInfo.getFrom(), to, null);
                    to.register(key.selector(), SelectionKey.OP_CONNECT, newInfo);
                }
                proxyInfo.setTo(to);

                from.put(answer);
                from.flip();
                proxyInfo.getFrom().write(from);

                dnsIterator.remove();
            }
        }
    }
}
