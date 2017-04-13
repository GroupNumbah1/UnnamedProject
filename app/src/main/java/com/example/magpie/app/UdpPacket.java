package com.example.magpie.app;

import java.net.InetAddress;
import java.nio.ByteBuffer;

/**
 * Created by mattpatera on 4/12/17.
 */

public class UdpPacket {

    public static final int UDP_HEADER_BYTE_SIZE = 8;
    public static final int IP4_HEADER_BYTE_SIZE = 20;

    public static class IP4Header {

        public static int UDP_PROTOCOL_NUM = 17;
        public static int EVERYTHING_ELSE = 0xFF;

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
        public InetAddress destination;
        public int optionsAndPadding;

        public IP4Header(ByteBuffer buff) {
            byte versionAndIHL = buff.get();
            this.version = (byte) (versionAndIHL >> 4); // TODO See if we need to do this.
            this.internetHeaderLen = (byte) (versionAndIHL & 0x0F);
            this.headerLen = this.internetHeaderLen << 2;

        }
    }
}
