package ua.com.osht.myproject.service;

import ua.com.osht.myproject.domain.Subtask;
import ua.com.osht.myproject.domain.Task;

import java.util.List;

public interface SubtaskService {
    Subtask getSubtaskById(Long id);
    void saveSubtask(Subtask subtask);
    void updateSubtask(Long id, String subtaskName);
    void deleteSubtask(Long id);
    List<Subtask> findByTaskId(Long id);
}
