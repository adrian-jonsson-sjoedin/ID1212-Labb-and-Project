package se.kth.project.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.kth.project.model.Reservation;
import se.kth.project.repository.ReservationRepository;
import se.kth.project.service.ReservationService;

import java.util.List;

@Service
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;

    @Autowired
    public ReservationServiceImpl(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @Override
    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    @Override
    public Reservation getReservationById(int reservationId) {
        return reservationRepository.findById(reservationId).orElse(null);
    }

    @Override
    public void createReservation(Reservation reservation) {
        reservationRepository.save(reservation);
    }

    @Override
    public void updateReservation(Reservation reservation) {
        if (reservation.getId() != 0) {
            reservationRepository.save(reservation);
        }
    }

    @Override
    public void deleteReservation(int reservationId) {
        reservationRepository.deleteById(reservationId);
    }
}
