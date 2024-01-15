package se.kth.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.kth.project.model.ReservationEntity;

import java.util.List;

public interface ReservationRepository extends JpaRepository<ReservationEntity, Integer> {

     List<ReservationEntity> findByUserId(int userId);

     void deleteById(int id);
}
