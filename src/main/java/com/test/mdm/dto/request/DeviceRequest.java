package com.test.mdm.dto.request;

import com.test.mdm.entity.DeviceEntity;
import com.test.mdm.entity.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Device request DTO for creating or updating a device.")
public class DeviceRequest {

    @Schema(description = "Unique device identifier.", example = "101", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Device id is required")
    private Long deviceId;


    @Schema(description = "Name of the device.", example = "Device", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Device name is required")
    private String deviceName;

    @Schema(description = "Status of the device.", example = "ACTIVE", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Status is required")
    private String status;


    public DeviceEntity toDeviceEntity(DeviceEntity deviceEntity) {
        deviceEntity.setDeviceId(deviceId);
        deviceEntity.setDeviceName(deviceName);
        deviceEntity.setStatus(Status.valueOf(status));
        return deviceEntity;
    }

}
