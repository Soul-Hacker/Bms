package com.example.BMS.dto;

import com.example.BMS.models.Seat;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BlockSeatRequestDto {
    private long showId;
    private long userId;
    private List<Long> seatId;
}
