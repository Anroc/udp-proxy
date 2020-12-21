package com.anroc.mitm.proxy.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

import java.net.DatagramPacket;
import java.net.InetAddress;

@Data
@AllArgsConstructor
public class UDPPackage {

    private byte[] data;
    private int length;
    private InetAddress inetAddress;
    private int port;

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

    public DatagramPacket toDatagramPacket(InetAddress inetAddress, int port) {
        return new DatagramPacket(getData(), getLength(), inetAddress, port);
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
}
