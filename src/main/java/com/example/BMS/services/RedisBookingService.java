package com.example.BMS.services;

import com.example.BMS.models.*;
import com.example.BMS.repositories.ShowRepository;
import com.example.BMS.repositories.ShowSeatRepository;
import com.example.BMS.repositories.TicketRepository;
import com.example.BMS.repositories.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;

import java.util.List;
import java.util.Optional;
@Service
public class RedisBookingService implements BookingService{
    private final CacheService cacheService;
    private final ShowSeatRepository showSeatRepository;
    private final UserRepository userRepository;
    private final TicketRepository ticketRepository;
    private final ShowRepository showRepository;
    public RedisBookingService(CacheService cacheService,ShowSeatRepository showSeatRepository, ShowRepository showRepository,UserRepository userRepository,TicketRepository ticketRepository)
    {
        this.cacheService=cacheService;
        this.showSeatRepository=showSeatRepository;
        this.showRepository=showRepository;
        this.ticketRepository=ticketRepository;
        this.userRepository=userRepository;
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
    public Optional<Ticket> bookTicket(long showId, List<Long> showSeatIds, long userId) {
        // 1. In redis check if the user has lock for all the seats that they are trying to book

        for(Long seatId: showSeatIds) {
            String status = (String) cacheService.get("seatId-"+seatId+"-userId-"+userId);
            System.out.println("status: "+status + " seatId: "+seatId + " userId: "+userId);
            if(status == null) {
                return Optional.empty();
            }
        }

        System.out.println("All seats available");

        User user = userRepository.findById(userId).get();
        Show show = showRepository.findById(showId).get();
        // Create a new ticket

        // Go to all the rows of show_Seats and update the status to booked and update ticket id in one query

        Ticket t = createTicketAndBookSeat(show, user, showSeatIds);


        System.out.println("ticket created");
        return Optional.of(t);
    }
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public Ticket createTicketAndBookSeat(Show show, User user, List<Long> seatIds) {

        Ticket ticket = new Ticket();
        ticket.setUser(user);
        ticket.setShow(show);
        ticket.setStatus(TicketStatus.BOOKED);

        ticket = ticketRepository.save(ticket);

        showSeatRepository.bookShowSeatsBulk(seatIds, ticket);

        return ticket;

    }
    @Override
    public void clearAllSeatLocks() {

    }
}
