package com.devrodts.devices.application.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devrodts.devices.application.dto.*;
import com.devrodts.devices.domain.entity.Device;
import com.devrodts.devices.domain.repository.DeviceRepository;
import com.devrodts.devices.infrastructure.exception.DeviceNotFoundException;
import com.devrodts.devices.infrastructure.exception.DuplicateDeviceException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DeviceService {

    private final DeviceRepository deviceRepository;

    @Transactional
    public DeviceDTO createDevice(DeviceDTO deviceDTO) {
        if (deviceRepository.existsByName(deviceDTO.getName())) {
            throw new DuplicateDeviceException(deviceDTO.getName());
        }
        Device device = deviceDTO.toEntity();
        Device savedDevice = deviceRepository.save(device);
        return DeviceDTO.fromEntity(savedDevice);
    }

    @Transactional(readOnly = true)
    public DeviceDTO getDeviceById(Long id) {
        return deviceRepository.findById(id)
                .map(DeviceDTO::fromEntity)
                .orElseThrow(() -> new DeviceNotFoundException(id));
    }

    @Transactional(readOnly = true)
    public List<DeviceDTO> getAllDevices() {
        return deviceRepository.findAll().stream()
                .map(DeviceDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteDevice(Long id) {
        deviceRepository.deleteById(id);
    }

    @Transactional
    public List<DeviceDTO> getDeviceByType(String type) {
        return deviceRepository.findByType(type).stream()
                .map(DeviceDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public DeviceDTO updateDeviceById(Long id, DeviceDTO deviceDTO) {
        Device device = deviceRepository.findById(id)
                .orElseThrow(() -> new DeviceNotFoundException(id));
        device.setName(deviceDTO.getName());
        device.setType(deviceDTO.getType());
        device.setStatus(deviceDTO.getStatus());
        Device updatedDevice = deviceRepository.save(device);
        return DeviceDTO.fromEntity(updatedDevice);
    }

    @Transactional
    public List<DeviceDTO> getDeviceByStatus(String status) {
        return deviceRepository.findByStatus(status).stream()
                .map(DeviceDTO::fromEntity)
                .collect(Collectors.toList());
    }

}
