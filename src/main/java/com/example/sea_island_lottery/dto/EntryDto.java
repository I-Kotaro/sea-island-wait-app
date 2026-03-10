package com.example.sea_island_lottery.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EntryDto {
    private Long id;
    private Long eventId;
    private String eventName;
    private String status;
    private LocalDateTime createdAt;
}
