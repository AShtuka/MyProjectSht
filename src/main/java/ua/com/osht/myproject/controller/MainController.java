package ua.com.osht.myproject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.com.osht.myproject.domain.Category;
import ua.com.osht.myproject.domain.Subtask;
import ua.com.osht.myproject.domain.Task;
import ua.com.osht.myproject.service.CategoryService;
import ua.com.osht.myproject.service.SubtaskService;
import ua.com.osht.myproject.service.TaskService;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Controller
public class MainController {
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private SubtaskService subtaskService;

    private Category categoryTmp;
    private Task taskTmp;
    private String sortDateMethod = "ASC";

    @GetMapping("/")
    public String list(Model model) {
        List<Category> categories = categoryService.findAll();
        if (categoryTmp != null){
            List<Task> tasks = filterAndSort(categoryTmp.getId());
            model.addAttribute("tasks", tasks);
        }
        if (taskTmp != null){
            List<Subtask> subtasks = subtaskService.findByTaskId(taskTmp.getId());
            model.addAttribute("subtasks", subtasks);
        }
        model.addAttribute("task", taskTmp);
        model.addAttribute("categories", categories);
        model.addAttribute("category", categoryTmp);
        return "greeting";
    }

    @PostMapping("/addCategory")
    public String addCategory(@RequestParam String categoryName){
        Category category = new Category(categoryName);
        categoryService.saveCategory(category);
        categoryTmp = category;
        taskTmp = null;
        return "redirect:/";
    }

    @GetMapping("/delete/category/{id}")
    public String deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        categoryTmp = null;
        taskTmp = null;
        return "redirect:/";
    }

    @GetMapping("/category/{id}")
    public String listTask(@PathVariable Long id) {
        categoryTmp = categoryService.getCategoryById(id);
        taskTmp = null;
        return "redirect:/";
    }

    @PostMapping("/addTask")
    public String addTask(@RequestParam String taskName, @RequestParam Long categoryId){
        categoryTmp = categoryService.getCategoryById(categoryId);
        Task task = new Task(taskName, categoryTmp);
        taskService.saveTask(task);
        taskTmp = task;
        return "redirect:/";
    }

    @GetMapping("/task/{id}")
    public String listSubtask(@PathVariable Long id) {
        taskTmp = taskService.getTaskById(id);
        taskTmp.getTaskName();
        return "redirect:/";
    }

    @GetMapping("/delete/task/{taskId}/{categoryId}")
    public String deleteTask(@PathVariable Long taskId, @PathVariable Long categoryId) {
        Category category = categoryService.getCategoryById(categoryId);
        category.removeTask(taskService.getTaskById(taskId));
        taskService.deleteTask(taskId);
        taskTmp = null;
        return "redirect:/";
    }

    @PostMapping("/addSubtask")
    public String addSubtask(@RequestParam String subtaskName){
            Subtask subtask = new Subtask(subtaskName, taskTmp);
            subtaskService.saveSubtask(subtask);
        return "redirect:/";
    }

    @GetMapping("/delete/subtask/{subtaskId}/{taskId}")
    public String deleteSubtask(@PathVariable Long subtaskId, @PathVariable Long taskId) {
        Task task = taskService.getTaskById(taskId);
        task.removeSubtask(subtaskService.getSubtaskById(subtaskId));
        subtaskService.deleteSubtask(subtaskId);
        return "redirect:/";
    }

    @GetMapping("/sort/{sortDate}")
    public String sortChoose(@PathVariable String sortDate) {
        sortDateMethod = sortDate;
        return "redirect:/";
    }

    @PostMapping("/setCompletionDate")
    public String setCompletionDate(@RequestParam Date dateCompletion) throws ParseException {
        dateCompletion = setDate(dateCompletion);
        taskService.updateTask(taskTmp, taskTmp.getTaskName(), taskTmp.getDateCreate(), dateCompletion, taskTmp.getSubtasks(), taskTmp.getCategory());
        return "redirect:/";
    }

    private List<Task> filterAndSort(Long id) {
        List<Task> tasks = null;
        switch (sortDateMethod) {
            case "ASC":
                tasks = taskService.findAllByCategory_IdOrderByDateCreateAsc(id);
                break;
            case "DESC":
                tasks = taskService.findAllByCategory_IdOrderByDateCreateDesc(id);
                break;
        }
        return tasks;
    }

    private Date setDate(Date date) throws ParseException {
        String [] time = date.toString().split(" ");
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        Date dateNaw = new Date();
        String tmp = dateFormat.format(dateNaw);
        time[3] = tmp;
        String newTime = "";
        for (String str : time){
            newTime = str + " ";
        }
        String s = newTime.substring(0, newTime.length()-1);
        dateNaw = dateFormat.parse(s);
        System.out.println(dateNaw);
        return dateNaw;
    }
}
