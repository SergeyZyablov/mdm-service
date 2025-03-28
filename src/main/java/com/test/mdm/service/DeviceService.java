package com.test.mdm.service;

import com.test.mdm.dto.DeviceDto;
import com.test.mdm.dto.request.DeviceRequest;
import com.test.mdm.dto.request.UpdateDeviceStatusRequest;
import com.test.mdm.entity.DeviceEntity;
import com.test.mdm.entity.Status;
import com.test.mdm.repository.DeviceRepository;
import com.test.mdm.util.AssertUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.test.mdm.util.Constant.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeviceService {

    private final DeviceRepository deviceRepository;

    @Transactional(readOnly = true)
    public List<DeviceDto> getDevices() {
        return deviceRepository.findAll().stream().map(DeviceEntity::toDto).toList();
    }

    @Transactional(readOnly = true)
    public DeviceDto getDeviceById(Long id) {
        DeviceEntity device = AssertUtil.notNull(deviceRepository.findById(id),
                String.format(DEVICE_WITH_ID_NOT_FOUND, id));
        return device.toDto();
    }

    @Transactional(readOnly = true)
    public DeviceDto getDevicesByDeviceId(Long deviceId) {
        DeviceEntity device = AssertUtil.notNull(deviceRepository.findByDeviceId(deviceId),
                String.format(DEVICE_WITH_DEVICE_ID_NOT_FOUND, deviceId));
        return device.toDto();
    }

    @Transactional
    public DeviceDto createDevice(DeviceRequest deviceRequest) {
        DeviceEntity device = deviceRequest.toDeviceEntity(new DeviceEntity());
        DeviceDto savedDevice = deviceRepository.save(device).toDto();
        log.info("Created new device: {}", savedDevice);
        return savedDevice;
    }

    @Transactional
    public DeviceDto updateDevice(Long id, DeviceRequest createDeviceRequest) {
        DeviceEntity device = AssertUtil.notNull(deviceRepository.findById(id),
                String.format(DEVICE_WITH_ID_NOT_FOUND, id));
        device = createDeviceRequest.toDeviceEntity(device);
        DeviceDto savedDevice = deviceRepository.save(device).toDto();
        log.info(DEVICE_SUCCESSFULLY_UPDATED, savedDevice.getId(), savedDevice);
        return savedDevice;
    }

    @Transactional
    public DeviceDto updateDeviceStatus(Long id, UpdateDeviceStatusRequest updateDeviceStatusRequest) {
        DeviceEntity device = AssertUtil.notNull(deviceRepository.findById(id),
                String.format(DEVICE_WITH_ID_NOT_FOUND, id));
        device.setStatus(Status.valueOf(updateDeviceStatusRequest.getStatus()));
        DeviceDto savedDevice = deviceRepository.save(device).toDto();
        log.info(DEVICE_STATUS_SUCCESSFULLY_UPDATED, savedDevice.getId(), savedDevice.getStatus());
        return savedDevice;
    }

    @Transactional
    public void deleteDevice(Long id) {
        DeviceEntity device = AssertUtil.notNull(deviceRepository.findById(id),
                String.format(DEVICE_WITH_ID_NOT_FOUND, id));
        deviceRepository.delete(device);
        log.info(DEVICE_DELETED, id);
    }
}
