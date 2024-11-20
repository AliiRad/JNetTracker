package com.home.nettracker.sniffer;

import org.pcap4j.core.*;
import org.pcap4j.packet.*;
import org.pcap4j.packet.namednumber.TcpPort;

import java.net.InetAddress;

public class TrafficSniffer {

    private static final String PCAP_INTERFACE_NAME = "en0";

    public static void main(String[] args) throws PcapNativeException, NotOpenException {
        startSniffing();
    }

    public static void startSniffing() throws PcapNativeException, NotOpenException {
        PcapNetworkInterface nif = Pcaps.getDevByName(PCAP_INTERFACE_NAME);

        if (nif == null) {
            System.err.println("Network Interface Founded: " + PCAP_INTERFACE_NAME);
            return;
        }

        int snapLen = 65536;
        int timeout = 10;
        PcapHandle handle = nif.openLive(snapLen, PcapNetworkInterface.PromiscuousMode.PROMISCUOUS, timeout);

        String filter = "tcp port 80 or tcp port 443";
        handle.setFilter(filter, BpfProgram.BpfCompileMode.OPTIMIZE);

        System.out.println("HTTP/HTTPS Monitoring ...");


        PacketListener listener = packet -> {
            if (packet.contains(IpV4Packet.class) && packet.contains(TcpPacket.class)) {
                IpV4Packet ipPacket = packet.get(IpV4Packet.class);
                TcpPacket tcpPacket = packet.get(TcpPacket.class);

                InetAddress srcAddr = ipPacket.getHeader().getSrcAddr();
                InetAddress dstAddr = ipPacket.getHeader().getDstAddr();
                TcpPort dstPort = tcpPacket.getHeader().getDstPort();


                if (dstPort.equals(TcpPort.HTTP) || dstPort.equals(TcpPort.HTTPS)) {
                    System.out.println("آدرس مبدأ: " + srcAddr.getHostAddress());
                    System.out.println("آدرس مقصد: " + dstAddr.getHostAddress());
                    System.out.println("پورت مقصد: " + dstPort);
                    System.out.println("-----------------------------------");
                }
            }
        };

        try {
            handle.loop(-1, listener);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            handle.close();
        }
    }
}