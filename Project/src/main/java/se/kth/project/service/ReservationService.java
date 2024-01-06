package se.kth.project.service;

import se.kth.project.dto.ListDTO;
import se.kth.project.model.ListEntity;
import se.kth.project.model.ReservationEntity;
import se.kth.project.model.UserEntity;

import java.util.List;

public interface ReservationService {
    List<ReservationEntity> getAllReservations();
    List<ListEntity> getAllLists();

    ReservationEntity getReservationById(int reservationId);

    void createReservation(ReservationEntity reservation);

    void updateReservation(ReservationEntity reservation);

    void deleteReservation(int reservationId);

    void bookReservation(int listId, UserEntity user);

    void saveList(ListDTO list);

    List<ReservationEntity> getReservationsByUserId(int userId);
}
