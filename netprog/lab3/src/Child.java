import java.net.InetAddress;

public class Child {
    private String nodeName;
    private InetAddress ipAddress;
    private int port;

    public Child(String nodeName, InetAddress address, int port) {
        this.nodeName = nodeName;
        this.ipAddress = address;
        this.port = port;
    }

    @Override
    public boolean equals(Object v) {
        boolean retVal = false;

        if (v instanceof Child) {
            Child ptr = (Child) v;

            return this.ipAddress.equals(ptr.ipAddress);
        }

        return retVal;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + (this.ipAddress != null ? this.ipAddress.hashCode() : 0);
        return hash;
    }

    public InetAddress getIpAddress() {
        return ipAddress;
    }

    public int getPort() {
        return port;
    }
}
