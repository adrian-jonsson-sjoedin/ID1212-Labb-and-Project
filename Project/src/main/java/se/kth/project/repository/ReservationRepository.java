package se.kth.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.kth.project.model.ReservationEntity;

public interface ReservationRepository extends JpaRepository<ReservationEntity, Integer> {

    // ListEntity<ReservationEntity> findByUserId(int userId);
}
