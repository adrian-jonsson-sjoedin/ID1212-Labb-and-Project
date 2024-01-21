package se.kth.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.kth.project.model.ListEntity;

public interface ListRepository extends JpaRepository<ListEntity, Integer> {

    ListEntity findByCourse_Id(int courseId);
}
