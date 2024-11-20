package com.home.nettracker.controller;

import com.home.nettracker.service.PortScannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PortScannerController {

    @Autowired
    private PortScannerService portScannerService;

    @GetMapping("/scan-ports/{host}")
    public List<Integer> scanPorts(@PathVariable String host,
                                    @RequestParam(defaultValue = "1") int startPort,
                                    @RequestParam(defaultValue = "65535") int endPort) {
        return portScannerService.findOpenPorts(host, startPort, endPort);
    }
}