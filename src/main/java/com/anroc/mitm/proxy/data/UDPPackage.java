package com.anroc.mitm.proxy.data;

import lombok.Data;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.Arrays;

@Data
public class UDPPackage {

    private final byte[] data;
    private final int length;
    private final InetAddress inetAddress;
    private final int port;

    private boolean resetConnection = false;

    public UDPPackage(byte[] data, int length, InetAddress inetAddress, int port) {
        this.data = data;
        this.length = length;
        this.inetAddress = inetAddress;
        this.port = port;
    }

    @Override
    public UDPPackage clone() {
        byte[] newArray = new byte[length];
        System.arraycopy(getData(), 0, newArray, 0, length);

        return new UDPPackage(
                newArray,
                getLength(),
                getInetAddress(),
                getPort()
        );
    }

    public final DatagramPacket toDatagramPacket(InetAddress inetAddress, int port) {
        return new DatagramPacket(getData(), getLength(), inetAddress, port);
    }

    public DatagramPacket toDatagramPacket() {
        return new DatagramPacket(getData(), getLength(), getInetAddress(), getPort());
    }

    public static UDPPackage fromDatagramPacket(DatagramPacket datagramPacket, InetAddress inetAddress, int port) {
        return new UDPPackage(
                createNewArray(datagramPacket.getData(), datagramPacket.getLength()),
                datagramPacket.getLength(),
                inetAddress,
                port
        );
    }

    public static UDPPackage fromDatagramPacket(DatagramPacket datagramPacket) {
        return fromDatagramPacket(datagramPacket, datagramPacket.getAddress(), datagramPacket.getPort());
    }

    public static byte[] createNewArray(byte[] input, int length) {
        byte[] newArray = new byte[length];
        System.arraycopy(input, 0, newArray, 0, length);
        return newArray;
    }

    public UDPPackage resetConnection() {
        this.resetConnection = true;
        return this;
    }

    public boolean shouldResetConnection() {
        return this.resetConnection;
    }

    @Override
    public String toString() {
        return Arrays.toString(getData());
    }
}
