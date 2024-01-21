package se.kth.project.service.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.kth.project.dto.ListDTO;
import se.kth.project.model.Booking;
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

@Service
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final ListRepository listRepository;
    private final UserRepository userRepository;

    @Autowired
    public ReservationServiceImpl(ReservationRepository reservationRepository,
                                  ListRepository listRepository,
                                  UserRepository userRepository) {
        this.reservationRepository = reservationRepository;
        this.listRepository = listRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<ReservationEntity> getAllReservations() {
        return reservationRepository.findAll();
    }

    @Override
    public List<ReservationEntity> getAllReservationsForStudent(Integer studentId, Integer coopId) {
        List<ReservationEntity> reservations = reservationRepository.findByUserId(studentId);
        List<ReservationEntity> reservationsByPartner = reservationRepository.findByCoopId_Id(coopId);
        reservations.addAll(reservationsByPartner);
        return reservations;
    }


    @Override
    public ReservationEntity findByReservationId(Integer reservationId) {
        return reservationRepository.findById(reservationId).orElseThrow(() -> new EntityNotFoundException("Reservation not found"));
    }

    @Override
    public ListDTO findListById(Integer listId) {
        ListEntity list = listRepository.findById(listId).orElseThrow(() -> new EntityNotFoundException("List not found"));
        return convertToListDTO(list);
    }

    private ListDTO convertToListDTO(ListEntity list) {
        ListDTO listDTO = new ListDTO();
        listDTO.setId(list.getId());
        listDTO.setCourse(list.getCourse());
        listDTO.setDescription(list.getDescription());
        listDTO.setLocation(list.getLocation());
        listDTO.setIntervall(list.getIntervall());
        return listDTO;

    }

    @Override
    public List<ListEntity> getAllLists() {
        return listRepository.findAll();
    }


    @Override
    public void createReservation(Booking booking) {
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

    @Override
    public void deleteReservation(Integer reservationId) {
        reservationRepository.deleteById(reservationId);
    }

    @Override
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


    @Override
    @Transactional
    public void deleteReservationList(int listId) {
        reservationRepository.deleteByListId(listId);
        listRepository.deleteById(listId);
    }


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


    @Override
    public Booking createBookingObject(Integer listId) {
        List<LocalDateTime> timeSlots = getAvailableTimeSlots(listId);
        Booking booking = new Booking();
        booking.setListId(listId);
        booking.setTimeSlots(timeSlots);
        return booking;
    }

    private List<LocalDateTime> getAvailableTimeSlots(Integer listId) {
        ListEntity list = listRepository.findById(listId).orElseThrow(() -> new EntityNotFoundException("List not found"));
        List<LocalDateTime> bookedTimeSlots = reservationRepository.getSequenceForReservationByListId(listId);
        List<LocalDateTime> listsTimeSlots = calculateTimeSlots(list.getStart(), list.getIntervall(), list.getMaxSlots());
        return calculateFreeTimeSlots(listsTimeSlots, bookedTimeSlots);
    }

    private List<LocalDateTime> calculateTimeSlots(LocalDateTime startTime, int timeInterval, int slots) {
        ArrayList<LocalDateTime> timeSlots = new ArrayList<>();
        timeSlots.add(startTime);
        for (int i = 1; i < slots; i++) {
            LocalDateTime next = timeSlots.get(i - 1).plusMinutes(timeInterval);
            timeSlots.add(next);
        }
        return timeSlots;
    }

    private List<LocalDateTime> calculateFreeTimeSlots(List<LocalDateTime> allSlots, List<LocalDateTime> bookedSlots) {
        List<LocalDateTime> freeSlots = new ArrayList<>(allSlots);
        freeSlots.removeAll(bookedSlots);
        return freeSlots;
    }

}
