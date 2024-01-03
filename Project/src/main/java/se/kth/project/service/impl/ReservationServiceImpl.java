package se.kth.project.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.kth.project.dto.ListDTO;
import se.kth.project.model.ListEntity;
import se.kth.project.model.ReservationEntity;
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
    public void deleteReservation(int reservationId) {
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
}
