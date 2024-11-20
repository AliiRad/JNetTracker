package com.home.nettracker;

import com.home.nettracker.sniffer.PacketSniffer;
import org.pcap4j.core.NotOpenException;
import org.pcap4j.core.PcapNativeException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class NetTrackerApplication {

    public static void main(String[] args) throws NotOpenException, PcapNativeException, InterruptedException {
        SpringApplication.run(NetTrackerApplication.class, args);

//        PacketSniffer sniffer = new PacketSniffer();
//        sniffer.startSniffing();
    }

}
