package com.devrodts.devices.repository; // test package can remain, but DeviceRepository import update below

import com.devrodts.devices.BaseIntegrationTest;
import com.devrodts.devices.domain.entity.Device;
import com.devrodts.devices.domain.repository.DeviceRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class DeviceRepositoryTest extends BaseIntegrationTest {

    @BeforeEach
    void clearDatabase() {
        deviceRepository.deleteAll();
    }

    @Autowired
    private DeviceRepository deviceRepository;

    @Test
    void shouldSaveAndRetrieveDevice() {
        Device device = Device.builder()
                .name("Test Device")
                .type("SENSOR")
                .status("ACTIVE")
                .build();
        Device savedDevice = deviceRepository.save(device);
        Optional<Device> foundDevice = deviceRepository.findById(savedDevice.getId());

        assertTrue(foundDevice.isPresent());
        assertEquals(device.getName(), foundDevice.get().getName());
        assertEquals(device.getType(), foundDevice.get().getType());
        assertEquals(device.getStatus(), foundDevice.get().getStatus());
    }

    @Test
    void shouldNotSaveDeviceWithDuplicateName() {
        String deviceName = "Unique Device";
        Device device1 = Device.builder()
                .name(deviceName)
                .type("SENSOR")
                .status("ACTIVE")
                .build();

        Device device2 = Device.builder()
                .name(deviceName)
                .type("ACTUATOR")
                .status("INACTIVE")
                .build();

        deviceRepository.save(device1);

        assertThrows(DataIntegrityViolationException.class, () -> deviceRepository.save(device2));
    }

    @Test 
    void shouldFindDeviceById(){
        Device device = Device.builder()
                .name("Test Device")
                .type("SENSOR")
                .status("ACTIVE")
                .build();
        Device savedDevice = deviceRepository.save(device);
        Optional<Device> foundDevice = deviceRepository.findById(savedDevice.getId());

        assertTrue(foundDevice.isPresent());
        assertEquals(device.getName(), foundDevice.get().getName());
        assertEquals(device.getType(), foundDevice.get().getType());
        assertEquals(device.getStatus(), foundDevice.get().getStatus());
    }

    @Test
    void shouldFindDeviceByState(){
        Device device = Device.builder()
                .name("Test Device")
                .type("SENSOR")
                .status("ACTIVE")
                .build();
        Device savedDevice = deviceRepository.save(device);
        List<Device> foundDevices = deviceRepository.findByStatus("ACTIVE");

        assertFalse(foundDevices.isEmpty());
        assertTrue(foundDevices.stream().anyMatch(d -> d.getId().equals(savedDevice.getId())));
    }

    


}
