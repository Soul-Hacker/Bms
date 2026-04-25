package com.example.BMS.services;

import com.example.BMS.models.ShowSeat;
import com.example.BMS.models.ShowSeatStatus;
import com.example.BMS.models.Ticket;
import com.example.BMS.repositories.ShowSeatRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class RedisBookingService implements BookingService{
    private final CacheService cacheService;
    private final ShowSeatRepository showSeatRepository;
    public RedisBookingService(CacheService cacheService,ShowSeatRepository showSeatRepository)
    {
        this.cacheService=cacheService;
        this.showSeatRepository=showSeatRepository;
    }

    @Override
    public boolean blockSeats(long showId, List<Long> seatIds, long userId) {
//        First we will check whether the seats are available or not
//
//            a. Check whether someone else has booked it or not
        List<ShowSeat> showSeats=showSeatRepository.findAllByShowIdAndSeatIdIn(showId,seatIds);
        for (ShowSeat seats:showSeats)
        {
            if(seats.getStatus().equals(ShowSeatStatus.BOOKED))
                return false;
        }
//        b. Check whether the seats are locked or not
        for(ShowSeat seat:showSeats)
        {
            String status=(String) cacheService.get("seatId-"+seat.getId()+"-userId-"+userId);
            if(status!=null)
            {
                return  false;
            }
        }

//        If all seats are available then we will block the seat in redis based on seatId->userId
        for (ShowSeat seat:showSeats)
        {
            cacheService.set("seatId-"+seat.getId()+"-userId-"+userId,"LOCKED");
        }
        return true;
    }

    @Override
    public Optional<Ticket> bookTicket(long showId, List<Long> seatIds, long userId) {
        return Optional.empty();
    }

    @Override
    public void clearAllSeatLocks() {

    }
}
