package se.kth.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.kth.project.model.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation, Integer> {

    // List<Reservation> findByUserId(int userId);
}
