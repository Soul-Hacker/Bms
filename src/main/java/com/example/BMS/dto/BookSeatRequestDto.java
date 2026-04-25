package com.example.BMS.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
public class BookSeatRequestDto {
    private long showId;
    private long userId;
    private List<Long> seatId;
}
