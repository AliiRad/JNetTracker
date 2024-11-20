package com.home.nettracker.service;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

@Service
public class PortScannerService {

    public List<Integer> findOpenPorts(String host, int startPort, int endPort) {
        List<Integer> openPorts = new ArrayList<>();

        for (int port = startPort; port <= endPort; port++) {
            if (isPortOpen(host, port)) {
                openPorts.add(port);
            }
        }

        return openPorts;
    }

    private boolean isPortOpen(String host, int port) {
        try (Socket socket = new Socket(host, port)) {
            return true; // Port is open
        } catch (IOException e) {
            return false; // Port is closed
        }
    }
}