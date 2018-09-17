package ru.nsu.fit.g16201.Boldyrev;

import java.net.InetAddress;

public class IPInfo {
    private InetAddress ipAddress;
    private int ipStatus;

    public IPInfo(InetAddress address, int status) {
        ipAddress = address;
        ipStatus = status;
    }

    @Override
    public boolean equals(Object v) {
        boolean retVal = false;

        if (v instanceof IPInfo) {
            IPInfo ptr = (IPInfo) v;
            retVal = ptr.ipAddress == this.ipAddress;
        }

        return retVal;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + (this.ipAddress != null ? this.ipAddress.hashCode() : 0);
        return hash;
    }

    public void decreaseStatus() {
        ipStatus--;
    }

    public int getIpStatus() {
        return ipStatus;
    }

    public String getIpAddress() {
        return ipAddress.getHostAddress();
    }
}
