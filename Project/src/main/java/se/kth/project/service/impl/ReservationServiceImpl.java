package se.kth.project.service.impl;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.kth.project.dto.ListDTO;
import se.kth.project.model.Booking;
import se.kth.project.model.ListEntity;
import se.kth.project.model.ReservationEntity;
import se.kth.project.model.UserEntity;
import se.kth.project.repository.ListRepository;
import se.kth.project.repository.ReservationRepository;
import se.kth.project.service.ReservationService;

import java.util.List;

@Service
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final ListRepository listRepository;

    @Autowired
    public ReservationServiceImpl(ReservationRepository reservationRepository, ListRepository listRepository) {
        this.reservationRepository = reservationRepository;
        this.listRepository = listRepository;
    }

    @Override
    public List<ReservationEntity> getAllReservations() {
        return reservationRepository.findAll();
    }

    @Override
    public List<ListEntity> getAllLists() {
        return listRepository.findAll();
    }

    @Override
    public ReservationEntity getReservationById(int reservationId) {
        return reservationRepository.findById(reservationId).orElse(null);
    }

    @Override
    public void createReservation(ReservationEntity reservation) {
        reservationRepository.save(reservation);
    }

    @Override
    public void updateReservation(ReservationEntity reservation) {
        if (reservation.getId() != 0) {
            reservationRepository.save(reservation);
        }
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
    public List<ReservationEntity> getReservationsByUserId(int userId) {
        return reservationRepository.findByUserId(userId);
    }

    @Override
    public void deleteReservationList(int listId) {
        listRepository.deleteById(listId);
    }




    @Override
    public Booking createBookingObject(Integer listId) {
        //retrieve the reservation list in question
        ListEntity list = listRepository.findById(listId).orElseThrow(() -> new EntityNotFoundException("List not found"));
        //retrieve all reservations for the same list
        List<ReservationEntity> reservations = reservationRepository.findAllByListId(listId);
        int[] sequences = new int[list.getMaxSlots()];
        //see which slots the reservation have booked
        if (reservations != null) {
            int i = 0;
            for (ReservationEntity reservation : reservations) {
                sequences[i] = reservation.getSequence();
                i++;
            }
        }
        Booking booking = new Booking(listId,
                list.getMaxSlots(),
                list.getStart(),
                list.getIntervall(),
                sequences,
                list.getLocation(),
                list.getDescription());
        int maxSlotsForReservation = listRepository.findMaxSlotsById(listId);
        return booking;
    }


}
