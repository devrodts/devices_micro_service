package com.devrodts.devices.service;

import com.devrodts.devices.BaseIntegrationTest;
import com.devrodts.devices.application.dto.DeviceDTO;
import com.devrodts.devices.application.service.DeviceService;
import com.devrodts.devices.domain.entity.Device;
import com.devrodts.devices.domain.repository.DeviceRepository;
import com.devrodts.devices.infrastructure.exception.DeviceNotFoundException;
import com.devrodts.devices.infrastructure.exception.DuplicateDeviceException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DeviceServiceTest extends BaseIntegrationTest {

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private DeviceRepository deviceRepository;

    @BeforeEach
    void setUp() {
        deviceRepository.deleteAll();
    }

    @Test
    void shouldCreateDevice() {
        DeviceDTO deviceDTO = DeviceDTO.builder()
                .name("Test Device")
                .type("SENSOR")
                .status("ACTIVE")
                .build();

        DeviceDTO savedDevice = deviceService.createDevice(deviceDTO);

        assertNotNull(savedDevice.getId());
        assertEquals(deviceDTO.getName(), savedDevice.getName());
        assertEquals(deviceDTO.getType(), savedDevice.getType());
        assertEquals(deviceDTO.getStatus(), savedDevice.getStatus());
    }

    @Test
    void shouldNotCreateDeviceWithDuplicateName() {
        String deviceName = "Duplicate Device";
        Device existingDevice = Device.builder()
                .name(deviceName)
                .type("SENSOR")
                .status("ACTIVE")
                .build();
        deviceRepository.save(existingDevice);

        DeviceDTO duplicateDeviceDTO = DeviceDTO.builder()
                .name(deviceName)
                .type("ACTUATOR")
                .status("INACTIVE")
                .build();

        assertThrows(DuplicateDeviceException.class, 
            () -> deviceService.createDevice(duplicateDeviceDTO));
    }

    @Test
    void shouldGetDeviceById() {
        Device device = Device.builder()
                .name("Test Device")
                .type("SENSOR")
                .status("ACTIVE")
                .build();
        Device savedDevice = deviceRepository.save(device);

        DeviceDTO foundDevice = deviceService.getDeviceById(savedDevice.getId());


        assertNotNull(foundDevice);
        assertEquals(savedDevice.getId(), foundDevice.getId());
        assertEquals(device.getName(), foundDevice.getName());
    }

    @Test
    void shouldThrowExceptionWhenDeviceNotFound() {
        Long nonExistentId = 999L;

        assertThrows(DeviceNotFoundException.class, 
            () -> deviceService.getDeviceById(nonExistentId));
    }


    @Test
    void shouldGetAllDevices() {
        Device device1 = Device.builder()
                .name("Device 1")
                .type("SENSOR")
                .status("ACTIVE")
                .build();
        Device device2 = Device.builder()
                .name("Device 2")
                .type("ACTUATOR")
                .status("INACTIVE")
                .build();
        deviceRepository.save(device1);
        deviceRepository.save(device2);

        List<DeviceDTO> devices = deviceService.getAllDevices();

        assertEquals(2, devices.size());
        assertTrue(devices.stream().anyMatch(d -> d.getName().equals("Device 1")));
        assertTrue(devices.stream().anyMatch(d -> d.getName().equals("Device 2")));
    }
}
