// PacketSniffer.java
package com.example.networksniffer;

import org.jnetpcap.Pcap;
import org.jnetpcap.PcapIf;
import org.jnetpcap.PcapPacket;
import org.jnetpcap.PcapSockAddr;
import org.jnetpcap.callback.PcapPacketHandler;

import java.util.ArrayList;
import java.util.List;

public class PacketSniffer {

    public static void main(String[] args) {
        List<PcapIf> devices = new ArrayList<>();
        StringBuilder errorBuffer = new StringBuilder();

        // پیدا کردن دستگاه‌های شبکه
        if (Pcap.findAllDevs(devices, errorBuffer) != Pcap.NOT_OK && devices.size() > 0) {
            PcapIf device = devices.get(0); // اولین دستگاه شبکه را انتخاب کنید

            // باز کردن دستگاه برای رهگیری بسته‌ها
            Pcap handle = device.openLive(65536, Pcap.MODE_PROMISCUOUS, 10000, errorBuffer);

            // تعریف handler برای پردازش بسته‌ها
            PcapPacketHandler<String> packetHandler = new PcapPacketHandler<String>() {
                public void nextPacket(PcapPacket packet, String user) {
                    String sourceIP = packet.toString(); // IP مبدأ
                    String destIP = packet.toString(); // IP مقصد

                    // اگر IP مقصد مربوط به گوگل باشد
                    if (destIP.contains("google")) {
                        System.out.println("Request to Google: " + destIP);
                    } else {
                        System.out.println("Other request: " + destIP);
                    }
                }
            };

            // شروع رهگیری بسته‌ها
            handle.loop(Pcap.LOOP_INFINITE, packetHandler, "jNetPcap rocks!");

            // بستن دستگاه
            handle.close();
        } else {
            System.err.println("Cannot find any device.");
            System.err.println(errorBuffer.toString());
        }
    }
}