package com.test.mdm.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Request DTO for updating the status of a device.")
public class UpdateDeviceStatusRequest {


    @Schema(description = "New status of the device.", example = "ACTIVE", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Status is required")
    private String status;
}
