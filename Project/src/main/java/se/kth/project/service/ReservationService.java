package se.kth.project.service;

import se.kth.project.model.Reservation;

import java.util.List;

public interface ReservationService {
    List<Reservation> getAllReservations();

    Reservation getReservationById(int reservationId);

    void createReservation(Reservation reservation);

    void updateReservation(Reservation reservation);

    void deleteReservation(int reservationId);
}
