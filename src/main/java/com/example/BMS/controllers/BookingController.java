package com.example.BMS.controllers;

import com.example.BMS.dto.BlockSeatRequestDto;
import com.example.BMS.dto.BookSeatRequestDto;
import com.example.BMS.models.Ticket;
import com.example.BMS.services.BookingService;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("api/v1/booking")
public class BookingController {
    private BookingService bookingService;
    public BookingController(BookingService bookingService)
    {
        this.bookingService=bookingService;
    }

    @PostMapping("/block")
    public boolean blockSeats(@RequestBody BlockSeatRequestDto blockSeatRequestDto)
    {
        return bookingService.blockSeats(blockSeatRequestDto.getShowId(),blockSeatRequestDto.getSeatId(),blockSeatRequestDto.getUserId());

    }
    @DeleteMapping
    public void clearAllSeatLocked()
    {
        bookingService.clearAllSeatLocks();
    }
    @PostMapping("/confirm")
    public Optional<Ticket> confirmBooking(@RequestBody BookSeatRequestDto bookSeatRequestDto)
    {
        return bookingService.bookTicket(bookSeatRequestDto.getShowId(),bookSeatRequestDto.getSeatId(), bookSeatRequestDto.getUserId());
    }


}
