package com.test.mdm.controller;

import com.test.mdm.dto.DeviceDto;
import com.test.mdm.dto.request.DeviceRequest;
import com.test.mdm.dto.request.UpdateDeviceStatusRequest;
import com.test.mdm.service.DeviceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/devices")
@RequiredArgsConstructor
@Validated
public class DeviceController {

    private final DeviceService deviceService;

    @Operation(summary = "Get all devices", description = "Fetches all available devices.")
    @ApiResponse(responseCode = "200", description = "Successful retrieval",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = DeviceDto.class)))
    @GetMapping
    public List<DeviceDto> getDevices() {
        return deviceService.getDevices();
    }

    @Operation(summary = "Get device by ID", description = "Fetches a device using its unique identifier.")
    @ApiResponse(responseCode = "200", description = "Device found",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = DeviceDto.class)))
    @ApiResponse(responseCode = "404", description = "Device not found")
    @GetMapping(path = "/{id}")
    public DeviceDto getDeviceById(@Parameter(description = "Device ID", required = true) @PathVariable Long id) {
        return deviceService.getDeviceById(id);
    }

    @Operation(summary = "Get device by deviceId", description = "Fetches a device using its unique device identifier.")
    @ApiResponse(responseCode = "200", description = "Device found",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = DeviceDto.class)))
    @ApiResponse(responseCode = "404", description = "Device not found")
    @GetMapping(path = "/deviceId/{deviceId}")
    public DeviceDto getDevicesByDeviceId(@Parameter(description = "Device ID", required = true) @PathVariable Long deviceId) {
        return deviceService.getDevicesByDeviceId(deviceId);
    }

    @Operation(summary = "Create a new device", description = "Creates a new device entry in the system.")
    @ApiResponse(responseCode = "201", description = "Device successfully created",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = DeviceDto.class)))
    @ApiResponse(responseCode = "400", description = "Invalid request data")
    @PostMapping
    public DeviceDto createDevice(
            @Parameter(description = "Device request", required = true) @Valid @RequestBody DeviceRequest deviceRequest) {
        return deviceService.createDevice(deviceRequest);
    }

    @Operation(summary = "Update device", description = "Updates an existing device based on its ID.")
    @ApiResponse(responseCode = "200", description = "Device successfully updated",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = DeviceDto.class)))
    @ApiResponse(responseCode = "404", description = "Device not found")
    @PutMapping(path = "/{id}")
    public DeviceDto updateDevice(
            @Parameter(description = "Device ID", required = true) @PathVariable Long id,
            @Parameter(description = "Device request", required = true) @Valid @RequestBody DeviceRequest updateDeviceRequest) {
        return deviceService.updateDevice(id, updateDeviceRequest);
    }

    @Operation(summary = "Update device status", description = "Updates only the status of an existing device.")
    @ApiResponse(responseCode = "200", description = "Device status successfully updated",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = DeviceDto.class)))
    @ApiResponse(responseCode = "404", description = "Device not found")
    @PatchMapping(path = "/{id}")
    public DeviceDto updateDeviceStatus(
            @Parameter(description = "Device ID", required = true) @PathVariable Long id,
            @Parameter(description = "Device status request", required = true) @Valid @RequestBody UpdateDeviceStatusRequest updateDeviceStatusRequest) {
        return deviceService.updateDeviceStatus(id, updateDeviceStatusRequest);
    }

    @Operation(summary = "Delete device", description = "Deletes a device from the system.")
    @ApiResponse(responseCode = "204", description = "Device successfully deleted")
    @ApiResponse(responseCode = "404", description = "Device not found")
    @DeleteMapping(path = "/{id}")
    public void deleteDevice(@Parameter(description = "Device ID", required = true) @PathVariable Long id) {
        deviceService.deleteDevice(id);
    }
}