package com.test.mdm.dto;

import com.test.mdm.entity.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Data Transfer Object representing a device.")
public class DeviceDto {

    @Schema(description = "Unique identifier of the device.", example = "1")
    private Long id;

    @Schema(description = "Device ID assigned by the user.", example = "101")
    private Long deviceId;

    @Schema(description = "Name of the device.", example = "Test")
    private String deviceName;

    @Schema(description = "Current status of the device.", example = "ACTIVE")
    private Status status;

    @Schema(description = "Timestamp of when the device was created.", example = "2025-03-14T12:00:00")
    private LocalDateTime createdAt;
}
