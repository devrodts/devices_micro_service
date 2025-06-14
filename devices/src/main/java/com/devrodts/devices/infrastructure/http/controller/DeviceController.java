package com.devrodts.devices.infrastructure.http.controller;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.devrodts.devices.application.dto.DeviceDTO;
import com.devrodts.devices.application.service.DeviceService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/devices")
@RequiredArgsConstructor
public class DeviceController {

    private final DeviceService deviceService;

    @PostMapping
    public ResponseEntity<DeviceDTO> createDevice(@Valid @RequestBody DeviceDTO deviceDTO) {
        DeviceDTO createdDevice = deviceService.createDevice(deviceDTO);
        return ResponseEntity
                .created(URI.create("/api/devices/" + createdDevice.getId()))
                .body(createdDevice);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DeviceDTO> getDevice(@PathVariable Long id) {
        DeviceDTO device = deviceService.getDeviceById(id);
        return ResponseEntity.ok(device);
    }

    @GetMapping
    public ResponseEntity<List<DeviceDTO>> getAllDevices() {
        List<DeviceDTO> devices = deviceService.getAllDevices();
        return ResponseEntity.ok(devices);
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<DeviceDTO>> getDevicesByType(@PathVariable String type) {
        List<DeviceDTO> devices = deviceService.getDeviceByType(type);
        return ResponseEntity.ok(devices);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<DeviceDTO> getDeviceById(@PathVariable Long id) {
        DeviceDTO device = deviceService.getDeviceById(id);
        return ResponseEntity.ok(device);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDevice(@PathVariable Long id) {
        deviceService.deleteDevice(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<DeviceDTO> updateDevice(@PathVariable Long id, @RequestBody DeviceDTO deviceDTO) {
        DeviceDTO updatedDevice = deviceService.updateDeviceById(id, deviceDTO);
        return ResponseEntity.ok(updatedDevice);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<DeviceDTO>> getDevicesByStatus(@PathVariable String status) {
        List<DeviceDTO> devices = deviceService.getDeviceByStatus(status);
        return ResponseEntity.ok(devices);
    }
}
