package com.devrodts.devices.domain; // test package remains

import org.junit.jupiter.api.Test;

import com.devrodts.devices.domain.entity.Device;

import static org.junit.jupiter.api.Assertions.*;

class DeviceTest {

    @Test
    void shouldCreateDeviceWithRequiredFields() {
        String name = "Device 1";
        String type = "SENSOR";
        String status = "ACTIVE";

        Device device = Device.builder()
                .name(name)
                .type(type)
                .status(status)
                .build();

        assertNotNull(device);
        assertEquals(name, device.getName());
        assertEquals(type, device.getType());
        assertEquals(status, device.getStatus());
        assertNotNull(device.getCreatedAt());
        assertNotNull(device.getUpdatedAt());
    }

    @Test
    void shouldUpdateDeviceFields() {
        Device device = Device.builder()
                .name("Old Name")
                .type("SENSOR")
                .status("INACTIVE")
                .build();

        String newName = "Updated Name";
        String newType = "ACTUATOR";
        String newStatus = "ACTIVE";

        device.setName(newName);
        device.setType(newType);
        device.setStatus(newStatus);

        assertEquals(newName, device.getName());
        assertEquals(newType, device.getType());
        assertEquals(newStatus, device.getStatus());
        assertNotEquals(device.getCreatedAt(), device.getUpdatedAt());
    }
}
