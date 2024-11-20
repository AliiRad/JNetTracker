package com.home.nettracker;

import org.pcap4j.core.PcapNativeException;
import org.pcap4j.core.PcapNetworkInterface;
import org.pcap4j.core.Pcaps;

import java.util.List;

public class TestAvailableNetwork {
    public static void main(String[] args) throws PcapNativeException {
        PcapNetworkInterface nif = Pcaps.getDevByName("en0");
        System.out.println(nif);
    }
}
