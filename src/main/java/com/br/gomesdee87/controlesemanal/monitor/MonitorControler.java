package com.br.gomesdee87.controlesemanal.monitor;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.OperatingSystemMXBean;
import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/monitor")
@CrossOrigin(origins = "*") // Permite o dashboard acessar
public class MonitorControler {
    
    @GetMapping
    public Map<String, Object> status() {
        MemoryMXBean memory = ManagementFactory.getMemoryMXBean();
        OperatingSystemMXBean os = ManagementFactory.getOperatingSystemMXBean();

        long memoriaUsadaMB = memory.getHeapMemoryUsage().getUsed() / (1024 * 1024);
        long memoriaMaxMB = memory.getHeapMemoryUsage().getMax() / (1024 * 1024);
        double usoMemoria = (memoriaUsadaMB * 100.0) / memoriaMaxMB;

        Map<String, Object> resp = new HashMap<>();
        resp.put("versao", "1.0.0");
        resp.put("status", "online");
        resp.put("memoriaUsadaMB", memoriaUsadaMB);
        resp.put("memoriaMaxMB", memoriaMaxMB);
        resp.put("usoMemoria", String.format("%.1f", usoMemoria));
        resp.put("processadores", os.getAvailableProcessors());
        resp.put("cargaSistema", String.format("%.1f", os.getSystemLoadAverage()));
        return resp;
    }
}
