package com.test.mdm.service;

import com.test.mdm.dto.DeviceDto;
import com.test.mdm.dto.request.DeviceRequest;
import com.test.mdm.dto.request.UpdateDeviceStatusRequest;
import com.test.mdm.entity.DeviceEntity;
import com.test.mdm.exception.NotFoundException;
import com.test.mdm.repository.DeviceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.test.mdm.entity.Status.ACTIVE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeviceServiceTest {

    @Mock
    private DeviceRepository deviceRepository;

    @InjectMocks
    private DeviceService deviceService;

    @Test
    void testGetDevices_thenReturnDevicesDtoList() {
        DeviceEntity testDevice = createTestDeviceEntity();

        when(deviceRepository.findAll()).thenReturn(List.of(testDevice));

        List<DeviceDto> result = deviceService.getDevices();

        assertThat(result)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1)
                .first()
                .isEqualTo(testDevice.toDto());
    }

    @Test
    void testGetDevicesByDeviceId_withInvalidDeviceId_thenThrowNotFoundException() {
        Long deviceId = 1L;

        when(deviceRepository.findByDeviceId(deviceId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> deviceService.getDevicesByDeviceId(deviceId))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(String.format(DeviceService.DEVICE_WITH_DEVICE_ID_NOT_FOUND, deviceId));
        verify(deviceRepository, times(1)).findByDeviceId(deviceId);
    }

    @Test
    void testGetDevicesByDeviceId_thenReturnDeviceDto() {
        DeviceEntity testDevice = createTestDeviceEntity();

        when(deviceRepository.findByDeviceId(testDevice.getDeviceId())).thenReturn(Optional.of(testDevice));

        DeviceDto result = deviceService.getDevicesByDeviceId(testDevice.getDeviceId());

        assertThat(result)
                .isNotNull()
                .isEqualTo(testDevice.toDto());
        verify(deviceRepository, times(1)).findByDeviceId(testDevice.getDeviceId());
    }

    @Test
    void testGetDeviceById_withValidId_thenReturnDeviceDto() {
        DeviceEntity testDevice = createTestDeviceEntity();

        when(deviceRepository.findById(testDevice.getDeviceId())).thenReturn(Optional.of(testDevice));

        DeviceDto result = deviceService.getDeviceById(testDevice.getDeviceId());

        assertThat(result)
                .isNotNull()
                .isEqualTo(testDevice.toDto());
        verify(deviceRepository, times(1)).findById(testDevice.getDeviceId());
    }

    @Test
    void testGetDeviceById_withInvalidId_thenThrowNotFoundException() {
        Long id = 1L;

        when(deviceRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> deviceService.getDeviceById(id))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(String.format(DeviceService.DEVICE_WITH_ID_NOT_FOUND, id));
        verify(deviceRepository, times(1)).findById(id);
    }

    @Test
    void testCreateDevice_thenReturnDeviceDto() {
        DeviceEntity testDevice = createTestDeviceEntity();

        DeviceRequest deviceRequest = new DeviceRequest();
        deviceRequest.setDeviceId(testDevice.getDeviceId());
        deviceRequest.setDeviceName(testDevice.getDeviceName());
        deviceRequest.setStatus(testDevice.getStatus().name());

        when(deviceRepository.save(testDevice)).thenReturn(testDevice);

        DeviceDto result = deviceService.createDevice(deviceRequest);

        assertThat(result)
                .isNotNull()
                .isEqualTo(testDevice.toDto());
        verify(deviceRepository, times(1)).save(testDevice);
    }

    @Test
    void testUpdateDevice_withValidId_thenReturnDeviceDto() {
        DeviceEntity testDevice = createTestDeviceEntity();

        DeviceRequest deviceRequest = new DeviceRequest();
        deviceRequest.setDeviceId(testDevice.getDeviceId());
        deviceRequest.setDeviceName(testDevice.getDeviceName());
        deviceRequest.setStatus(String.valueOf(testDevice.getStatus()));

        when(deviceRepository.findById(testDevice.getDeviceId())).thenReturn(Optional.of(testDevice));
        when(deviceRepository.save(testDevice)).thenReturn(testDevice);

        DeviceDto result = deviceService.updateDevice(testDevice.getDeviceId(), deviceRequest);

        assertThat(result)
                .isNotNull()
                .isEqualTo(testDevice.toDto());
        verify(deviceRepository, times(1)).findById(testDevice.getDeviceId());
        verify(deviceRepository, times(1)).save(testDevice);
    }

    @Test
    void testUpdateDevice_withInvalidId_thenThrowNotFoundException() {
        Long id = 1L;
        DeviceRequest deviceRequest = new DeviceRequest();

        when(deviceRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> deviceService.updateDevice(id, deviceRequest))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(String.format(DeviceService.DEVICE_WITH_ID_NOT_FOUND, id));
        verify(deviceRepository, times(1)).findById(id);
    }

    @Test
    void testUpdateDeviceStatus_withValidId_thenReturnDeviceDto() {
        DeviceEntity testDevice = createTestDeviceEntity();

        UpdateDeviceStatusRequest updateDeviceStatusRequest = new UpdateDeviceStatusRequest();
        updateDeviceStatusRequest.setStatus(testDevice.getStatus().name());

        when(deviceRepository.findById(testDevice.getDeviceId())).thenReturn(Optional.of(testDevice));
        when(deviceRepository.save(testDevice)).thenReturn(testDevice);

        DeviceDto result = deviceService.updateDeviceStatus(testDevice.getDeviceId(), updateDeviceStatusRequest);

        assertThat(result)
                .isNotNull()
                .isEqualTo(testDevice.toDto());
        verify(deviceRepository, times(1)).findById(testDevice.getDeviceId());
        verify(deviceRepository, times(1)).save(testDevice);
    }

    @Test
    void testUpdateDeviceStatus_withInvalidId_thenThrowNotFoundException() {
        Long id = 1L;
        UpdateDeviceStatusRequest updateDeviceStatusRequest = new UpdateDeviceStatusRequest();

        when(deviceRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> deviceService.updateDeviceStatus(id, updateDeviceStatusRequest))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(String.format(DeviceService.DEVICE_WITH_ID_NOT_FOUND, id));
        verify(deviceRepository, times(1)).findById(id);
    }

    @Test
    void testDeleteDevice_withValidId_thenNoExceptionThrown() {
        DeviceEntity testDevice = createTestDeviceEntity();

        when(deviceRepository.findById(testDevice.getDeviceId())).thenReturn(Optional.of(testDevice));

        deviceService.deleteDevice(testDevice.getDeviceId());

        verify(deviceRepository, times(1)).findById(testDevice.getDeviceId());
    }

    @Test
    void testDeleteDevice_withInvalidId_thenThrowNotFoundException() {
        Long id = 1L;

        when(deviceRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> deviceService.deleteDevice(id))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(String.format(DeviceService.DEVICE_WITH_ID_NOT_FOUND, id));
        verify(deviceRepository, times(1)).findById(id);
    }

    private DeviceEntity createTestDeviceEntity() {
        DeviceEntity testDevice = new DeviceEntity();
        testDevice.setDeviceId(1L);
        testDevice.setDeviceName("Test Device");
        testDevice.setStatus(ACTIVE);
        testDevice.setCreatedAt(null);
        return testDevice;
    }
}