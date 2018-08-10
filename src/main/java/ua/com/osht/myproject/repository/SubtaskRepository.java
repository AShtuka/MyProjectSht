package ua.com.osht.myproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.com.osht.myproject.domain.Subtask;

import java.util.List;

public interface SubtaskRepository extends JpaRepository<Subtask, Long> {
    List<Subtask> findByTaskId(Long id);
}
