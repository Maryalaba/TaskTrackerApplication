package com.example.activitytracker.ServiceImpl;
import com.example.activitytracker.DTO.TaskDTO;
import com.example.activitytracker.DTO.UserDTO;
import com.example.activitytracker.Exception.TaskNotFoundException;
import com.example.activitytracker.Exception.UserNotFoundException;
import com.example.activitytracker.Model.Status;
import com.example.activitytracker.Model.Task;
import com.example.activitytracker.Model.User;
import com.example.activitytracker.Repository.TaskRepository;
import com.example.activitytracker.Repository.UserRepository;
import com.example.activitytracker.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private  final TaskRepository taskRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, TaskRepository taskRepository) {
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
    }


    @Override
    public User registerUser(UserDTO userDTO) {
        User user = new User();
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        return userRepository.save(user);
    }

    @Override
    public String loginUser(String email, String password) {
        String message = "";
        User user = getUserByEmail(email);
        if (user.getPassword().equals(password)){
          message = "Success";
        }else {
            message = "incorrect password";
        }
        return message;
    }

    @Override
    public Task createNewTask(Task task, int userId) {
        task.setCreatedAt(new Date());
        task.setUpdatedAt(new Date());
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()){
            throw new UserNotFoundException("user not found");
        }
        task.setUser(user.get());
        if (new Date().before(task.getDueDate())){
            task.setStatus(String.valueOf(Status.PENDING));
        }
        if (new Date().equals(task.getDueDate()) || new Date().after(task.getDueDate())){
            task.setStatus(String.valueOf(Status.DONE));
        }
        return taskRepository.save(task);
    }

    @Override
    public Task updateTask(Task task , int id) {
        Task existingTask = getTaskById(id);
        existingTask.setTitle(task.getTitle());
        existingTask.setDescription(task.getDescription());
        return taskRepository.save(task);
    }

    @Override
    public List<Task> viewAllTasks() {
        return taskRepository.findAll();
    }

    @Override
    public List<Task> viewAllTaskByStatus(String status, int userId) {
        return taskRepository.viewListOfTasksByStatus(status, userId);
    }

    @Override
    public boolean deleteById(int id) {
         taskRepository.deleteById(id);
         return true;
    }

    @Override
    public boolean updateTaskByStatus(Task task){
        taskRepository.save(task);
        return true;
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findUserByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email + "not found in the database"));
    }

    @Override
    public Task getTaskById(int id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException( "Task not found in the database"));
    }
}
