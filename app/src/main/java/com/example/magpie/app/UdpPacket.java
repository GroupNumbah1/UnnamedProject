package com.example.magpie.app;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

/**
 * Created by mattpatera on 4/12/17.
 */

public class UdpPacket {

    public static final int UDP_HEADER_BYTE_SIZE = 8;
    public static final int IP4_HEADER_BYTE_SIZE = 20;

    public static int UDP_PROTOCOL_NUM = 17;
    public static int EVERYTHING_ELSE = 0xFF;

    public IP4Header ip4Header;
    public UDPHeader udpHeader;

    public ByteBuffer backingBuffer;

    public UdpPacket(ByteBuffer buff) {

        this.ip4Header = new IP4Header(buff);

        if (this.ip4Header.protocol == UDP_PROTOCOL_NUM) {
            this.udpHeader = new UDPHeader(buff);
        }
        this.backingBuffer = buff;
    }

    public static class IP4Header {

        public byte version;
        public byte internetHeaderLen;
        public int headerLen;
        public short DSCP; // differentiated services code point
        public int totalLen;
        public int whyAreFlags3Bits;
        public short timeToLive;
        public short protocolNum;
        public int protocol;
        public int checksum;
        public InetAddress source;
        public InetAddress target;
        public int optionsAndPadding;

        public IP4Header(ByteBuffer buff) {
            byte versionAndIHL = buff.get();
            this.version = (byte) (versionAndIHL >> 4); // TODO See if we need to do this. ( / 16 ?)
            this.internetHeaderLen = (byte) (versionAndIHL & 0x0F);
            this.headerLen = this.internetHeaderLen * 4; // TODO was bitshift << 2
            this.DSCP = BitUtility.getUnsignedByte(buff.get());
            this.totalLen = BitUtility.getUnsignedShort(buff.getShort());
            this.whyAreFlags3Bits = buff.getInt();
            this.timeToLive = BitUtility.getUnsignedByte(buff.get());
            this.protocolNum = BitUtility.getUnsignedByte(buff.get());
            this.protocol = (protocolNum == UDP_PROTOCOL_NUM) ? UDP_PROTOCOL_NUM : EVERYTHING_ELSE;
            this.checksum = BitUtility.getUnsignedShort(buff.getShort());

            byte[] sourceAddressArray = new byte[4];
            byte[] targetAddressArray = new byte[4];
            buff.get(sourceAddressArray, 0, 4);
            buff.get(targetAddressArray, 0, 4);
            try {
                this.source = InetAddress.getByAddress(sourceAddressArray);
                this.target = InetAddress.getByAddress(targetAddressArray);
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }

        }
    }

    public static class UDPHeader {

        private UDPHeader(ByteBuffer buff) {

        }
    }


    private static class BitUtility
    {
        private static short getUnsignedByte(byte value)
        {
            return (short)(value & 0xFF);
        }

        private static int getUnsignedShort(short value)
        {
            return value & 0xFFFF;
        }

        private static long getUnsignedInt(int value)
        {
            return value & 0xFFFFFFFFL;
        }
    }
}
