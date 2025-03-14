package com.test.mdm.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.mdm.config.GlobalExceptionHandler;
import com.test.mdm.dto.DeviceDto;
import com.test.mdm.dto.request.DeviceRequest;
import com.test.mdm.dto.request.UpdateDeviceStatusRequest;
import com.test.mdm.entity.Status;
import com.test.mdm.exception.NotFoundException;
import com.test.mdm.service.DeviceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import(GlobalExceptionHandler.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class DeviceControllerTest {

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private DeviceService deviceService;

    @InjectMocks
    private DeviceController deviceController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(new DeviceController(deviceService))
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void testGetDevices_thenReturnDevicesList() throws Exception {
        DeviceDto deviceDto = new DeviceDto(1L, 1L, "New Device", Status.ACTIVE, null);
        List<DeviceDto> deviceDtos = Collections.singletonList(deviceDto);

        when(deviceService.getDevices()).thenReturn(deviceDtos);

        mockMvc.perform(get("/devices"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(deviceDto.getId()))
                .andExpect(jsonPath("$[0].deviceId").value(deviceDto.getDeviceId()))
                .andExpect(jsonPath("$[0].deviceName").value(deviceDto.getDeviceName()))
                .andExpect(jsonPath("$[0].status").value(deviceDto.getStatus().name()));
    }

    @Test
    void getDeviceById_shouldReturnDevice() throws Exception {
        DeviceDto deviceDto = new DeviceDto(1L, 101L, "New Device", Status.ACTIVE, null);
        when(deviceService.getDeviceById(1L)).thenReturn(deviceDto);

        mockMvc.perform(get("/devices/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(deviceDto.getId()))
                .andExpect(jsonPath("$.deviceId").value(deviceDto.getDeviceId()))
                .andExpect(jsonPath("$.deviceName").value(deviceDto.getDeviceName()))
                .andExpect(jsonPath("$.status").value(deviceDto.getStatus().name()));
    }

    @Test
    void getDeviceById_withInvalidId_shouldReturnDevice() throws Exception {
        Long nonExistentId = 999L;
        when(deviceService.getDeviceById(nonExistentId)).thenThrow(new NotFoundException("Device not found"));

        mockMvc.perform(get("/devices/" + nonExistentId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Device not found"));
    }

    @Test
    void getDevicesByDeviceId_shouldReturnDevice() throws Exception {
        DeviceDto deviceDto = new DeviceDto(1L, 101L, "New Device", Status.ACTIVE, null);
        when(deviceService.getDevicesByDeviceId(101L)).thenReturn(deviceDto);

        mockMvc.perform(get("/devices/deviceId/101"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(deviceDto.getId()))
                .andExpect(jsonPath("$.deviceId").value(deviceDto.getDeviceId()))
                .andExpect(jsonPath("$.deviceName").value(deviceDto.getDeviceName()))
                .andExpect(jsonPath("$.status").value(deviceDto.getStatus().name()));
    }

    @Test
    void createDevice_shouldReturnCreatedDevice() throws Exception {
        DeviceRequest request = new DeviceRequest(102L, "New Device", "ACTIVE");
        DeviceDto response = new DeviceDto(1L, 102L, "New Device", Status.ACTIVE, null);
        when(deviceService.createDevice(any(DeviceRequest.class))).thenReturn(response);

        mockMvc.perform(post("/devices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.deviceId").value(102))
                .andExpect(jsonPath("$.deviceName").value("New Device"));
    }

    @ParameterizedTest
    @MethodSource("invalidParametersForDeviceRequest")
    void createDevice_withInvalidData__thenShouldResponseBadRequest(String fieldName, Object value, String message) throws Exception {
        DeviceRequest request = new DeviceRequest(102L, "New Device", "ACTIVE");
        setFieldValue(request, fieldName, value);

        mockMvc.perform(post("/devices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    void updateDevice_shouldReturnUpdatedDevice() throws Exception {
        DeviceRequest request = new DeviceRequest(101L, "Updated Device", "BLOCKED");
        DeviceDto response = new DeviceDto(1L, 101L, "Updated Device", Status.BLOCKED, null);
        when(deviceService.updateDevice(eq(1L), any(DeviceRequest.class))).thenReturn(response);

        mockMvc.perform(put("/devices/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.deviceName").value("Updated Device"))
                .andExpect(jsonPath("$.status").value("BLOCKED"));
    }

    @ParameterizedTest
    @MethodSource("invalidParametersForDeviceRequest")
    void updateDevice_withInvalidData_thenShouldResponseBadRequest(String fieldName, Object value, String message) throws Exception {
        DeviceRequest request = new DeviceRequest(102L, "New Device", "ACTIVE");
        setFieldValue(request, fieldName, value);

        mockMvc.perform(put("/devices/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    void updateDeviceStatus_shouldReturnUpdatedStatus() throws Exception {
        UpdateDeviceStatusRequest request = new UpdateDeviceStatusRequest(Status.BLOCKED.name());
        DeviceDto response = new DeviceDto(1L, 101L, "New Device", Status.BLOCKED, null);
        when(deviceService.updateDeviceStatus(eq(1L), any(UpdateDeviceStatusRequest.class))).thenReturn(response);

        mockMvc.perform(patch("/devices/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("BLOCKED"));
    }

    @Test
    void deleteDevice_shouldReturnNoContent() throws Exception {
        doNothing().when(deviceService).deleteDevice(1L);

        mockMvc.perform(delete("/devices/1"))
                .andExpect(status().isOk());

        verify(deviceService, times(1)).deleteDevice(1L);
    }

    private static Stream<Arguments> invalidParametersForDeviceRequest() {
        return Stream.of(
                Arguments.of("deviceId", null, "Device id is required"),
                Arguments.of("deviceName", null, "Device name is required"),
                Arguments.of("deviceName", "", "Device name is required"),
                Arguments.of("status", null, "Status is required")
        );
    }

    private void setFieldValue(Object request, String fieldName, Object value) throws Exception {
        Field field = request.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(request, value);
    }
}