package se.kth.project.service;

import jakarta.persistence.EntityNotFoundException;
import se.kth.project.dto.BookingDTO;
import se.kth.project.dto.ListDTO;
import se.kth.project.model.CourseEntity;
import se.kth.project.model.ListEntity;
import se.kth.project.model.ReservationEntity;
import se.kth.project.service.impl.ReservationServiceImpl;

import java.util.List;
/**
 * Service interface for handling reservation-related operations.
 * <p>
 * This interface defines methods for retrieving, creating, and managing reservations and booking lists.
 *
 * @see ReservationServiceImpl
 */
public interface ReservationService {
    /**
     * Retrieves all reservations for a student and reservations where they are a cooperative partner.
     *
     * @param studentId The ID of the student.
     * @return A list of reservations for the specified student.
     */
    List<ReservationEntity> getAllReservationsForStudent(Integer studentId);

    /**
     * Finds a reservation by its ID.
     *
     * @param reservationId The ID of the reservation to be retrieved.
     * @return The reservation entity with the specified ID.
     * @throws EntityNotFoundException if the reservation is not found.
     */
    ReservationEntity findByReservationId(Integer reservationId);

    /**
     * Retrieves all booking lists from the database.
     *
     * @return A list of all booking lists.
     */
    List<ListEntity> getAllLists();

    /**
     * Creates a new reservation based on the provided booking DTO.
     *
     * @param booking The DTO containing booking information to be saved.
     * @throws EntityNotFoundException if needed database entity is not found.
     */
    void createReservation(BookingDTO booking);

    /**
     * Deletes a reservation by its ID.
     *
     * @param reservationId The ID of the reservation to be deleted.
     */
    void deleteReservation(Integer reservationId);

    /**
     * Deletes a reservation/booking list and its associated reservations.
     *
     * @param listId The ID of the booking list to be deleted.
     */
    void deleteReservationList(int listId);

    /**
     * Saves a new reservation/booking list to the database based on the provided list DTO.
     *
     * @param list The DTO containing booking list information to be saved.
     */
    void saveList(ListDTO list);

    /**
     * Creates a booking DTO containing information about available time slots for a booking list.
     *
     * @param listId The ID of the booking list.
     * @return The booking DTO with information about available time slots.
     */
    BookingDTO createBookingObject(Integer listId);

    /**
     * Calculates the number of available spots left in a booking list.
     *
     * @param listId The ID of the booking list.
     * @return The number of available spots in the booking list.
     * @throws IllegalStateException   if more bookings than available spots are detected.
     * @throws EntityNotFoundException if list entity is not found.
     */
    int getNumberOfAvailableSpotsLeft(Integer listId);

    /**
     * Finds a booking list by its ID.
     *
     * @param listId The ID of the booking list to be retrieved.
     * @return The DTO representing the booking list with the specified ID.
     * @throws EntityNotFoundException if the booking list is not found.
     */
    ListDTO findListById(Integer listId);

    /**
     * Retrieves booking lists that a student has access to based on their enrolled courses.
     *
     * @param courses The list of courses associated with the student.
     * @return A list of booking lists accessible to the student.
     */
    List<ListEntity> getAllListsThatStudentHasAccessTo(List<CourseEntity> courses);
}
