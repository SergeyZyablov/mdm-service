package com.test.mdm.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.test.mdm.dto.DeviceDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "device")
public class DeviceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "device_id_generator")
    @SequenceGenerator(
            name = "device_id_generator",
            sequenceName = "device_id_seq",
            allocationSize = 1)
    @Column(name = "id", updatable = false, insertable = false)
    private Long id;

    @Column(name = "device_id")
    private Long deviceId;

    @Column(name = "device_name")
    private String deviceName;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "created_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss Z")
    private LocalDateTime createdAt;

    public  DeviceDto toDto() {
        DeviceDto deviceDto = new DeviceDto();
        deviceDto.setId(getId());
        deviceDto.setDeviceId(getDeviceId());
        deviceDto.setDeviceName(getDeviceName());
        deviceDto.setStatus(getStatus());
        deviceDto.setCreatedAt(getCreatedAt());
        return deviceDto;
    }
}
