package com.example.activitytracker.Service;

import com.example.activitytracker.DTO.TaskDTO;
import com.example.activitytracker.DTO.UserDTO;
import com.example.activitytracker.Model.Status;
import com.example.activitytracker.Model.Task;
import com.example.activitytracker.Model.User;
import java.util.List;

public interface UserService {

    User registerUser(UserDTO userDTO);

    String loginUser(String email, String password);

    Task createNewTask(Task task, int userId);

    Task updateTask(Task task , int id);

    Task getTaskById(int id);

    List<Task> viewAllTasks();

    boolean updateTaskByStatus(Task task);

    List<Task> viewAllTaskByStatus(String status, int userId);

    boolean deleteById(int id);

    User getUserByEmail(String email);

}
