package ua.com.osht.myproject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.com.osht.myproject.domain.Category;
import ua.com.osht.myproject.domain.Subtask;
import ua.com.osht.myproject.domain.Task;
import ua.com.osht.myproject.domain.User;
import ua.com.osht.myproject.service.CategoryService;
import ua.com.osht.myproject.service.SubtaskService;
import ua.com.osht.myproject.service.TaskService;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/taskManager")
@PreAuthorize("hasAuthority('USER')")
public class MainController {
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private SubtaskService subtaskService;

    private Category categoryTmp;
    private Task taskTmp;
    private List<Subtask> subtasksTmp;
    private String sortDateMethod = "ASC_Create";

    @GetMapping
    public String list(@AuthenticationPrincipal User user, Model model) {
        List<Category> categories = categoryService.findByUserId(user.getId());
        if (categoryTmp != null){
            List<Task> tasks = filterAndSort(categoryTmp.getId());
            model.addAttribute("tasks", tasks);
        }
        if (taskTmp != null){
            subtasksTmp = subtaskService.findByTaskId(taskTmp.getId());
            model.addAttribute("subtasks", subtasksTmp);
        }
        model.addAttribute("user", user);
        model.addAttribute("task", taskTmp);
        model.addAttribute("categories", categories);
        model.addAttribute("category", categoryTmp);
        return "taskManager";
    }

    @PostMapping("addCategory")
    public String addCategory(@AuthenticationPrincipal User user, @RequestParam String categoryName){
        Category category = new Category(categoryName, user);
        categoryService.saveCategory(category);
        categoryTmp = category;
        taskTmp = null;
        return "redirect:/taskManager";
    }

    @GetMapping("delete/category/{id}")
    public String deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        categoryTmp = null;
        taskTmp = null;
        return "redirect:/taskManager";
    }

    @GetMapping("category/{id}")
    public String listTask(@PathVariable Long id) {
        categoryTmp = categoryService.getCategoryById(id);
        taskTmp = null;
        return "redirect:/taskManager";
    }

    @PostMapping("addTask")
    public String addTask(@RequestParam String taskName, @RequestParam Long categoryId){
        categoryTmp = categoryService.getCategoryById(categoryId);
        Task task = new Task(taskName, categoryTmp);
        taskService.saveTask(task);
        taskTmp = task;
        return "redirect:/taskManager";
    }

    @GetMapping("task/{id}")
    public String listSubtask(@PathVariable Long id) {
        taskTmp = taskService.getTaskById(id);
        taskTmp.getTaskName();
        return "redirect:/taskManager";
    }

    @GetMapping("delete/task/{taskId}/{categoryId}")
    public String deleteTask(@PathVariable Long taskId, @PathVariable Long categoryId) {
        Category category = categoryService.getCategoryById(categoryId);
        category.removeTask(taskService.getTaskById(taskId));
        taskService.deleteTask(taskId);
        taskTmp = null;
        return "redirect:/taskManager";
    }

    @PostMapping("addSubtask")
    public String addSubtask(@RequestParam String subtaskName){
            Subtask subtask = new Subtask(subtaskName, taskTmp);
            subtaskService.saveSubtask(subtask);
        return "redirect:/taskManager";
    }

    @GetMapping("delete/subtask/{subtaskId}/{taskId}")
    public String deleteSubtask(@PathVariable Long subtaskId, @PathVariable Long taskId) {
        Task task = taskService.getTaskById(taskId);
        task.removeSubtask(subtaskService.getSubtaskById(subtaskId));
        subtaskService.deleteSubtask(subtaskId);
        return "redirect:/taskManager";
    }

    @GetMapping("sortCreateDate/{sortDate}")
    public String sortCreateDate(@PathVariable String sortDate) {
        sortDateMethod = sortDate;
        return "redirect:/taskManager";
    }

    @GetMapping("sortCompletionDate/{sortDate}")
    public String sortCompletionDate(@PathVariable String sortDate) {
        sortDateMethod = sortDate;
        return "redirect:/taskManager";
    }

    @GetMapping("sortTaskName/{sortDate}")
    public String sortTaskName(@PathVariable String sortDate) {
        sortDateMethod = sortDate;
        return "redirect:/taskManager";
    }

    @PostMapping("setCompletionDate")
    public String setCompletionDate(@RequestParam Date dateCompletion){
        taskTmp.setDateCompletion(dateCompletion);
        taskService.saveTask(taskTmp);
        return "redirect:/taskManager";
    }

    @PostMapping("isDone")
    public String subtaskIsDone(@RequestParam(value = "value", required = false) String[] value){
        subtaskDoneList(value);
        return "redirect:/taskManager";
    }

    @PostMapping("isTaskDone")
    public String taskIsDone(@RequestParam(value = "isTaskDone", required = false) String isTaskDone){
        if (isTaskDone == null){
            taskTmp.setTaskDone(true);
        } else {
            taskTmp.setTaskDone(false);
        }
        taskService.saveTask(taskTmp);
        return "redirect:/taskManager";
    }

    @PostMapping("addComment")
    public String addComment(@RequestParam String comment){
        taskTmp.setComment(comment);
        taskService.saveTask(taskTmp);
        return "redirect:/taskManager";
    }

    private void subtaskDoneList(String[] value){
        boolean[] doneList = new boolean[value.length];
        for(int index = 0; index < value.length; index++){
            if (value[index].equals("true")){
                doneList[index] = true;
            } else {
                doneList[index] = false;
            }
        }
        Subtask subtask = null;
        for (int x = 0; x < doneList.length; x++){
            if (subtasksTmp.get(x).isDone().equals(doneList[x])){
                continue;
            } else {
                subtask = subtasksTmp.get(x);
                subtask.setDone(doneList[x]);
                subtaskService.saveSubtask(subtask);
            }
        }
    }

    private List<Task> filterAndSort(Long id) {
        List<Task> tasks = null;
        switch (sortDateMethod) {
            case "ASC_Create":
                tasks = taskService.findAllByCategory_IdOrderByDateCreateAsc(id);
                break;
            case "DESC_Create":
                tasks = taskService.findAllByCategory_IdOrderByDateCreateDesc(id);
                break;
            case "ASC_Completion":
                tasks = taskService.findAllByCategory_IdOrderByDateCompletionAsc(id);
                break;
            case "DESC_Completion":
                tasks = taskService.findAllByCategory_IdOrderByDateCompletionDesc(id);
                break;
            case "ASC_TaskName":
                tasks = taskService.findAllByCategory_IdOrderByTaskNameAsc(id);
                break;
            case "DESC_TaskName":
                tasks = taskService.findAllByCategory_IdOrderByTaskNameDesc(id);
                break;
        }
        return tasks;
    }
}
