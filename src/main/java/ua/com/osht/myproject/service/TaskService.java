package ua.com.osht.myproject.service;

import ua.com.osht.myproject.domain.Category;
import ua.com.osht.myproject.domain.Subtask;
import ua.com.osht.myproject.domain.Task;

import java.util.Date;
import java.util.List;

public interface TaskService {
    Task getTaskById(Long id);
    void saveTask(Task task);
    void updateTask(Long id, String taskName);
    void deleteTask(Long id);
    void updateTask(Task task, String taskName, Date dateCrete, Date dateCompletion, List<Subtask> subtasks, Category category, String comment, Boolean taskDone);
    List<Task> findByCategoryId(Long id);
    List<Task> findAllByCategory_IdOrderByDateCreateAsc(Long id);
    List<Task> findAllByCategory_IdOrderByDateCompletionAsc(Long id);
    List<Task> findAllByCategory_IdOrderByTaskNameAsc(Long id);
    List<Task> findAllByCategory_IdOrderByDateCreateDesc(Long id);
    List<Task> findAllByCategory_IdOrderByDateCompletionDesc(Long id);
    List<Task> findAllByCategory_IdOrderByTaskNameDesc(Long id);
}
