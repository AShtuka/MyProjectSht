package ua.com.osht.myproject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.com.osht.myproject.domain.Category;
import ua.com.osht.myproject.domain.Subtask;
import ua.com.osht.myproject.domain.Task;
import ua.com.osht.myproject.repository.TaskRepository;

import java.util.Date;
import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {
    @Autowired
    private TaskRepository taskRepository;

    @Override
    public Task getTaskById(Long id) {
        return taskRepository.getOne(id);
    }

    @Override
    public void saveTask(Task task) {
        taskRepository.save(task);
    }

    @Override
    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

    @Override
    public void updateTask(Task task, String taskName, Date dateCrete, Date dateCompletion, List<Subtask> subtasks, Category category, String comment, Boolean taskDone) {
        task.setTaskName(taskName);
        task.setDateCreate(dateCrete);
        task.setDateCompletion(dateCompletion);
        task.getSubtasks().clear();
        task.getSubtasks().addAll(subtasks);
        task.setCategory(category);
        task.setComment(comment);
        task.setTaskDone(taskDone);
        taskRepository.save(task);
    }

    @Override
    public List<Task> findAllByCategory_IdOrderByDateCreateAsc(Long id) {
        return taskRepository.findAllByCategory_IdOrderByDateCreateAsc(id);
    }

    @Override
    public List<Task> findAllByCategory_IdOrderByDateCompletionAsc(Long id) {
        return taskRepository.findAllByCategory_IdOrderByDateCompletionAsc(id);
    }

    @Override
    public List<Task> findAllByCategory_IdOrderByTaskNameAsc(Long id) {
        return taskRepository.findAllByCategory_IdOrderByTaskNameAsc(id);
    }

    @Override
    public List<Task> findAllByCategory_IdOrderByDateCreateDesc(Long id) {
        return taskRepository.findAllByCategory_IdOrderByDateCreateDesc(id);
    }

    @Override
    public List<Task> findAllByCategory_IdOrderByDateCompletionDesc(Long id) {
        return taskRepository.findAllByCategory_IdOrderByDateCompletionDesc(id);
    }

    @Override
    public List<Task> findAllByCategory_IdOrderByTaskNameDesc(Long id) {
        return taskRepository.findAllByCategory_IdOrderByTaskNameDesc(id);
    }


}
