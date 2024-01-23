package se.kth.project.service.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.kth.project.dto.BookingDTO;
import se.kth.project.dto.ListDTO;
import se.kth.project.model.CourseEntity;
import se.kth.project.model.ListEntity;
import se.kth.project.model.ReservationEntity;
import se.kth.project.model.UserEntity;
import se.kth.project.repository.ListRepository;
import se.kth.project.repository.ReservationRepository;
import se.kth.project.repository.UserRepository;
import se.kth.project.service.ReservationService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the {@link ReservationService} interface that provides functionality for managing reservations
 * and booking lists.This service includes methods for retrieving, creating, and deleting reservations, as well as
 * managing booking lists.
 *
 * @see ReservationService
 */
@Service
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final ListRepository listRepository;
    private final UserRepository userRepository;

    /**
     * Constructs a new instance of the {@code ReservationServiceImpl} class.
     *
     * @param reservationRepository The repository for reservation-related database operations.
     * @param listRepository        The repository for booking list-related database operations.
     * @param userRepository        The repository for user-related database operations.
     */
    @Autowired
    public ReservationServiceImpl(ReservationRepository reservationRepository,
                                  ListRepository listRepository,
                                  UserRepository userRepository) {
        this.reservationRepository = reservationRepository;
        this.listRepository = listRepository;
        this.userRepository = userRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ReservationEntity> getAllReservationsForStudent(Integer studentId) {
        List<ReservationEntity> reservations = reservationRepository.findByUserId(studentId);
        List<ReservationEntity> reservationsByPartner = reservationRepository.findByCoopId_Id(studentId);
        reservations.addAll(reservationsByPartner);
        return reservations;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ReservationEntity findByReservationId(Integer reservationId) {
        return reservationRepository.findById(reservationId).orElseThrow(() -> new EntityNotFoundException("Reservation not found"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ListDTO findListById(Integer listId) {
        ListEntity list = listRepository.findById(listId).orElseThrow(() -> new EntityNotFoundException("List not found"));
        return convertToListDTO(list);
    }

    /**
     * Converts a {@link ListEntity} to a {@link ListDTO}.
     *
     * @param list The {@link ListEntity} to convert.
     * @return The corresponding {@link ListDTO}.
     */
    private ListDTO convertToListDTO(ListEntity list) {
        ListDTO listDTO = new ListDTO();
        listDTO.setId(list.getId());
        listDTO.setCourse(list.getCourse());
        listDTO.setDescription(list.getDescription());
        listDTO.setLocation(list.getLocation());
        listDTO.setIntervall(list.getIntervall());
        return listDTO;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ListEntity> getAllLists() {
        return listRepository.findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ListEntity> getAllListsThatStudentHasAccessTo(List<CourseEntity> courses) {
        List<ListEntity> list = new ArrayList<>();
        //we need to filter out all elements from list that are null before returning null. If all elements are
        // null we can return null
        for (CourseEntity course : courses) {
            ListEntity temp = listRepository.findByCourse_Id(course.getId());
            if (temp != null) {
                list.add(temp);
            }
        }
        return list;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void createReservation(BookingDTO booking) {
        ReservationEntity reservation = new ReservationEntity();
        UserEntity user = userRepository.findById(booking.getStudentId()).orElseThrow(() -> new EntityNotFoundException("User not found"));
        ListEntity list = listRepository.findById(booking.getListId()).orElseThrow(() -> new EntityNotFoundException("List not found"));
        reservation.setUser(user);
        reservation.setList(list);
        if (booking.getCoopId() != null) {
            UserEntity coop = userRepository.findById(booking.getCoopId()).orElseThrow(() -> new EntityNotFoundException("Coop user not found"));
            reservation.setCoopId(coop);
        }
        reservation.setSequence(booking.getSelectedTime());
        reservationRepository.save(reservation);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void deleteReservation(Integer reservationId) {
        reservationRepository.deleteById(reservationId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void saveList(ListDTO listDTO) {
        ListEntity list = new ListEntity();
        list.setUser(listDTO.getUser());
        list.setCourse(listDTO.getCourse());
        list.setDescription(listDTO.getDescription());
        list.setLocation(listDTO.getLocation());
        list.setStart(listDTO.getStart());
        list.setIntervall(listDTO.getIntervall());
        list.setMaxSlots(listDTO.getMaxSlots());
        listRepository.save(list);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void deleteReservationList(int listId) {
        reservationRepository.deleteByListId(listId);
        listRepository.deleteById(listId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getNumberOfAvailableSpotsLeft(Integer listId) {
        ListEntity list = listRepository.findById(listId).orElseThrow(() -> new EntityNotFoundException("List not found"));
        int numberOfBookings = reservationRepository.countReservationsForListByListId(listId);
        int availableSpots = list.getMaxSlots() - numberOfBookings;
        if (availableSpots < 0) {
            throw new IllegalStateException("More bookings than available spots detected");
        }
        return availableSpots;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BookingDTO createBookingObject(Integer listId) {
        List<LocalDateTime> timeSlots = getAvailableTimeSlots(listId);
        BookingDTO booking = new BookingDTO();
        booking.setListId(listId);
        booking.setTimeSlots(timeSlots);
        return booking;
    }

    /**
     * Calculates the free time slots for a given booking list by comparing all available time slots
     * with the time slots that have been booked.
     *
     * @param listId The ID of the booking list.
     * @return A list of LocalDateTime representing the free time slots.
     */
    private List<LocalDateTime> getAvailableTimeSlots(Integer listId) {
        ListEntity list = listRepository.findById(listId).orElseThrow(() -> new EntityNotFoundException("List not found"));
        List<LocalDateTime> bookedTimeSlots = reservationRepository.getSequenceForReservationByListId(listId);
        List<LocalDateTime> listsTimeSlots = calculateTimeSlots(list.getStart(), list.getIntervall(), list.getMaxSlots());
        return calculateFreeTimeSlots(listsTimeSlots, bookedTimeSlots);
    }

    /**
     * Calculates time slots based on the start time, time interval, and the number of slots.
     *
     * @param startTime    The start time of the time slots.
     * @param timeInterval The time interval between slots in minutes.
     * @param slots        The number of slots to be calculated.
     * @return A list of LocalDateTime representing the calculated time slots.
     */
    private List<LocalDateTime> calculateTimeSlots(LocalDateTime startTime, int timeInterval, int slots) {
        ArrayList<LocalDateTime> timeSlots = new ArrayList<>();
        timeSlots.add(startTime);
        for (int i = 1; i < slots; i++) {
            LocalDateTime next = timeSlots.get(i - 1).plusMinutes(timeInterval);
            timeSlots.add(next);
        }
        return timeSlots;
    }

    /**
     * Calculates the free time slots by removing booked slots from the list of all available slots.
     *
     * @param allSlots    The list of all available time slots.
     * @param bookedSlots The list of time slots that have been booked.
     * @return A list of LocalDateTime representing the free time slots.
     */
    private List<LocalDateTime> calculateFreeTimeSlots(List<LocalDateTime> allSlots, List<LocalDateTime> bookedSlots) {
        List<LocalDateTime> freeSlots = new ArrayList<>(allSlots);
        freeSlots.removeAll(bookedSlots);
        return freeSlots;
    }
}
