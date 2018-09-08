package ua.com.osht.myproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.com.osht.myproject.domain.Task;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByCategory_Id(Long id);
    List<Task> findAllByCategory_IdOrderByDateCreateAsc(Long id);
    List<Task> findAllByCategory_IdOrderByDateCompletionAsc(Long id);
    List<Task> findAllByCategory_IdOrderByTaskNameAsc(Long id);
    List<Task> findAllByCategory_IdOrderByDateCreateDesc(Long id);
    List<Task> findAllByCategory_IdOrderByDateCompletionDesc(Long id);
    List<Task> findAllByCategory_IdOrderByTaskNameDesc(Long id);
}
