package se.kth.project.service;

import se.kth.project.dto.ListDTO;
import se.kth.project.model.ReservationEntity;

import java.util.List;

public interface ReservationService {
    List<ReservationEntity> getAllReservations();

    ReservationEntity getReservationById(int reservationId);

    void createReservation(ReservationEntity reservation);

    void updateReservation(ReservationEntity reservation);

    void deleteReservation(int reservationId);

    void saveList(ListDTO list);
}
