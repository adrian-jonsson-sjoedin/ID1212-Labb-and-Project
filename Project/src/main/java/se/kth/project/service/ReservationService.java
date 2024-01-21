package se.kth.project.service;

import se.kth.project.dto.ListDTO;
import se.kth.project.model.Booking;
import se.kth.project.model.CourseEntity;
import se.kth.project.model.ListEntity;
import se.kth.project.model.ReservationEntity;

import java.util.List;

public interface ReservationService {
    List<ReservationEntity> getAllReservations();

    List<ReservationEntity> getAllReservationsForStudent(Integer studentId, Integer coopId);

    ReservationEntity findByReservationId(Integer reservationId);
    List<ListEntity> getAllLists();


    void createReservation(Booking booking);

    void deleteReservation(Integer reservationId);

    void deleteReservationList(int listId);

    void saveList(ListDTO list);

    Booking createBookingObject(Integer listId);
    int getNumberOfAvailableSpotsLeft(Integer listId);

    ListDTO findListById(Integer listId);

    List<ListEntity> getAllListsThatStudentHasAccessTo(List<CourseEntity> courses);
}
