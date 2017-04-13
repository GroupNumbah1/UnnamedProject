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

    public boolean isUDP; // make getter

    public ByteBuffer backingBuffer;

    public UdpPacket(ByteBuffer buff) {

        this.ip4Header = new IP4Header(buff);

        if (this.ip4Header.protocol == UDP_PROTOCOL_NUM) {
            this.udpHeader = new UDPHeader(buff);
            this.isUDP = true;
        } else {
            this.isUDP = false;
        }
        this.backingBuffer = buff;
    }

    public void swapSourceAndDestination() {
        InetAddress newSourceAddress = ip4Header.target;
        ip4Header.target = ip4Header.source;
        ip4Header.source = newSourceAddress;

        if (isUDP) {
            int newSourcePort = udpHeader.destinationPort;
            udpHeader.destinationPort = udpHeader.sourcePort;
            udpHeader.sourcePort = newSourcePort;
        } 
    }

    public void updateUDPBuffer(ByteBuffer buffer, int payloadSize) {
        buffer.position(0);
        udpHeader.fillHeader(buffer);
        backingBuffer = buffer;

        int udpTotalLength = UDP_HEADER_BYTE_SIZE + payloadSize;
        backingBuffer.putShort(IP4_HEADER_BYTE_SIZE + 4, (short) udpTotalLength);
        udpHeader.length = udpTotalLength;

        // Disable UDP checksum validation
        backingBuffer.putShort(IP4_HEADER_BYTE_SIZE + 6, (short) 0);
        udpHeader.checksum = 0;

        int ip4TotalLength = IP4_HEADER_BYTE_SIZE + udpTotalLength;
        backingBuffer.putShort(2, (short) ip4TotalLength);
        ip4Header.totalLen = ip4TotalLength;

        updateIP4Checksum();
    }

    private void updateIP4Checksum() {
        ByteBuffer buffer = backingBuffer.duplicate();
        buffer.position(0);

        // Clear previous checksum
        buffer.putShort(10, (short) 0);

        int ipLength = ip4Header.headerLen;
        int sum = 0;
        while (ipLength > 0) {
            sum += BitUtility.getUnsignedShort(buffer.getShort());
            ipLength -= 2;
        }
        while (sum >> 16 > 0)
            sum = (sum & 0xFFFF) + (sum >> 16);

        sum = ~sum;
        ip4Header.checksum = sum;
        backingBuffer.putShort(10, (short) sum);
    }

    private void fillHeader(ByteBuffer buffer) {
        ip4Header.fillHeader(buffer);
        if (isUDP) {
            udpHeader.fillHeader(buffer);
        }
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

        public void fillHeader(ByteBuffer buffer) {
            //each break seperates 32 bits of the header
            buffer.put((byte) (this.version << 4 | this.internetHeaderLen));
            buffer.put((byte) this.DSCP);
            buffer.putShort((short) this.headerLen);

            buffer.putInt(this.whyAreFlags3Bits);

            buffer.put((byte) this.timeToLive);
            buffer.put((byte) this.protocol);
            buffer.putShort((short) this.checksum);

            buffer.put(this.source.getAddress());
            buffer.put(this.target.getAddress());
        }
        public String toString()
        {
            return "IPV4 PACKET: \n" +
                    "\t\tVersion: " + this.version + "\n" +
                    "\t\tIHL (32 bit words): " + this.internetHeaderLen + "\n" +
                    "\t\tHeader Length (Bytes): " + this.headerLen + "\n" +
                    "\t\tDSCP: " + this.DSCP + "\n" +
                    "\t\tIdentifier: " + (BitUtility.getUnsignedInt(this.whyAreFlags3Bits)>> 16) + "\n" +
                    "\t\tFlags: " + ((this.whyAreFlags3Bits >> 13) & 0x7) + "\n" +
                    "\t\tFragment offset: " + (this.whyAreFlags3Bits & 0x1FFF) + "\n" +
                    "\t\tTime to Live: " + (this.timeToLive) + "\n" +
                    "\t\tprotocol: " + (this.protocolNum & 0xFF) + "\n" +
                    "\t\tchecksum: " + this.checksum + "\n" +
                    "\t\tsource address: " + this.source.getHostAddress() + "\n" +
                    "\t\ttarget address: " + this.target.getHostAddress() + "\n\n";


        }
    }

    public static class UDPHeader {

        public int sourcePort;
        public int destinationPort;

        public int length;
        public int checksum;


        private UDPHeader(ByteBuffer buffer) {
            this.sourcePort = BitUtility.getUnsignedShort(buffer.getShort());
            this.destinationPort = BitUtility.getUnsignedShort(buffer.getShort());

            this.length = BitUtility.getUnsignedShort(buffer.getShort());
            this.checksum = BitUtility.getUnsignedShort(buffer.getShort());
        }

        public void fillHeader(ByteBuffer buffer) {
            buffer.putShort((short) this.sourcePort);
            buffer.putShort((short) this.destinationPort);

            buffer.putShort((short) this.length);
            buffer.putShort((short) this.checksum);
        }

        @Override
        public String toString() {
            return "UDPHeader{ \nsourcePort=" + sourcePort + ", destinationPort=" + destinationPort +
                    ", length=" + length + ", checksum=" + checksum + "\n}";
        }


    }

    private static class BitUtility {
        private static short getUnsignedByte(byte value) {
            return (short) (value & 0xFF);
        }

        private static int getUnsignedShort(short value) {
            return value & 0xFFFF;
        }

        private static long getUnsignedInt(int value)
        {
            return value & 0xFFFFFFFFL;
        }
    }

}