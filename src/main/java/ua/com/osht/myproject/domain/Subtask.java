package ua.com.osht.myproject.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
public class Subtask {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String subtaskName;
    private Boolean done;

    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;

    public Subtask() {
    }

    public Subtask(String subtaskName, Task task) {
        this.subtaskName = subtaskName;
        this.task = task;
        this.done = false;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSubtaskName() {
        return subtaskName;
    }

    public void setSubtaskName(String subtaskName) {
        this.subtaskName = subtaskName;
    }
    @JsonIgnore
    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public Boolean isDone() {
        return done;
    }

    public void setDone(Boolean done) {
        this.done = done;
    }

    /*
          <script>
                function getCheckedCheckBoxes() {
                    var checkboxes = document.getElementsByName('isDone');
                    var checkboxesChecked = []; // можно в массиве их хранить, если нужно использовать
                    for (var index = 0; index < checkboxes.length; index++) {
                        if (checkboxes[index].checked) {
                            checkboxesChecked.push(checkboxes[index].value); // положим в массив выбранный
                        }
                    }
                    return checkboxesChecked; // для использования в нужном месте
                }
                function getCheckedCheckBox() {
                    var formData = new FormData(document.forms.isSubTaskDone);
                    formData.append("value[]", getCheckedCheckBoxes());
                    var xhr = new XMLHttpRequest();
                    xhr.open("POST", "/isDone");
                    xhr.send(formData);
                }

            </script>

              <script>
            function getCheckedCheckBoxes() {
                var checkboxes = document.getElementsByName('isDone');
                var checkboxesChecked = []; // можно в массиве их хранить, если нужно использовать
                for (var index = 0; index < checkboxes.length; index++) {
                    if (checkboxes[index].checked) {
                        checkboxesChecked.push(index); // положим в массив выбранный
                        checkboxesChecked.push(checkboxes[index].value); // положим в массив выбранный
                    }
                }
                return checkboxesChecked; // для использования в нужном месте
            }
            function getCheckedCheckBox() {
                var formData = new FormData(document.forms.isSubTaskDone);
                formData.append("value", getCheckedCheckBoxes());
                var xhr = new XMLHttpRequest();
                xhr.open("GET", "/taskManager/isDone");
                xhr.send(formData);
            }
        </script>
        <script>
            function confirmButton() {
                document.getElementById("confirmButton").style.display = 'block';
            }
        </script>
     */
}
