package ua.com.osht.myproject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.com.osht.myproject.domain.Subtask;
import ua.com.osht.myproject.repository.SubtaskRepository;

import java.util.List;

@Service
public class SubtaskServiceImpl implements SubtaskService {
    @Autowired
    private SubtaskRepository subtaskRepository;

    @Override
    public Subtask getSubtaskById(Long id) {
        return subtaskRepository.getOne(id);
    }

    @Override
    public void saveSubtask(Subtask subtask) {
        subtaskRepository.save(subtask);
    }

    @Override
    public void updateSubtask(Long id, String subtaskName) {
        Subtask subtask = getSubtaskById(id);
        subtask.setSubtaskName(subtaskName);
        subtaskRepository.save(subtask);
    }

    @Override
    public void deleteSubtask(Long id) {
        subtaskRepository.deleteById(id);
    }

    @Override
    public List<Subtask> findByTaskId(Long id) {
        return subtaskRepository.findByTaskId(id);
    }
}
