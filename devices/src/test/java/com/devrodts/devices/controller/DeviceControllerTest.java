package com.devrodts.devices.controller;

import com.devrodts.devices.application.dto.DeviceDTO;
import com.devrodts.devices.domain.entity.Device;
import com.devrodts.devices.domain.repository.DeviceRepository;
import com.devrodts.devices.BaseIntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DeviceControllerTest extends BaseIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

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

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<DeviceDTO> request = new HttpEntity<>(deviceDTO, headers);

        ResponseEntity<DeviceDTO> response = restTemplate.exchange(
                "/api/devices",
                HttpMethod.POST,
                request,
                DeviceDTO.class
        );

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        DeviceDTO body = response.getBody();
        assertNotNull(body);
        assertNotNull(body.getId());
        assertEquals(deviceDTO.getName(), body.getName());
        assertEquals(deviceDTO.getType(), body.getType());
        assertEquals(deviceDTO.getStatus(), body.getStatus());
    }

    @Test
    void shouldGetDeviceById() {
        Device device = Device.builder()
                .name("Test Device")
                .type("SENSOR")
                .status("ACTIVE")
                .build();
        Device savedDevice = deviceRepository.save(device);

        ResponseEntity<DeviceDTO> response = restTemplate.getForEntity(
                "/api/devices/" + savedDevice.getId(),
                DeviceDTO.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        DeviceDTO body = response.getBody();
        assertNotNull(body);
        assertEquals(savedDevice.getId(), body.getId());
        assertEquals(device.getName(), body.getName());
    }

    @Test
    void shouldReturnNotFoundForNonExistentDevice() {
        Long nonExistentId = 999L;


        ResponseEntity<String> response = restTemplate.getForEntity(
                "/api/devices/" + nonExistentId,
                String.class
        );

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
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

        ResponseEntity<DeviceDTO[]> response = restTemplate.getForEntity(
                "/api/devices",
                DeviceDTO[].class
        );
        assertEquals(HttpStatus.OK, response.getStatusCode());
        DeviceDTO[] body = response.getBody();
        assertNotNull(body);
        assertEquals(2, body.length);
    }
}
