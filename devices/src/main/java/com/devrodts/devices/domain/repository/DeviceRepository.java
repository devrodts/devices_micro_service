package com.devrodts.devices.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.devrodts.devices.domain.entity.Device;

import java.util.Optional;
import java.util.List;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Long> {
    Optional<Device> findByName(String name);
    boolean existsByName(String name);
    List<Device> findByType(String type);
    List<Device> findByStatus(String status);
    Optional<Device> findDeviceById(Long id);
    Optional<Device> findDeviceByStatus(String status);
    void deleteDeviceById(Long id);
}

