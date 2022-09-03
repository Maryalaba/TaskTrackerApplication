package com.example.activitytracker.ServiceImpl;

import com.example.activitytracker.DTO.TaskDTO;
import com.example.activitytracker.DTO.UserDTO;
import com.example.activitytracker.Model.Status;
import com.example.activitytracker.Model.Task;
import com.example.activitytracker.Model.User;
import com.example.activitytracker.Repository.TaskRepository;
import com.example.activitytracker.Repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class UserServiceImplTest {

    @Mock
    UserRepository userRepository;

    @Mock
    TaskRepository taskRepository;

    @InjectMocks
    UserServiceImpl userServiceImpl;

    private User user;
    private UserDTO userDTO;
    private TaskDTO taskDTO;
    private Task task;
    private Date date;
    List<Task> taskList;
    @BeforeEach
    void setUp() {
        date = new Date(2022, 9, 05);
        taskList = new ArrayList<>();
        taskList.add(task);
        this.user = new User(1, "Mary" , "mary@gmail.com" , "1234" , taskList);
        this.task = new Task(1, "Algorithms", "Solve algorithms on codewars", "PENDING", date, date, date, user);
        this.userDTO = new UserDTO("Mary" , "mary@gmail.com", "1234");
        when(userRepository.save(user)).thenReturn(user);
        when(taskRepository.save(task)).thenReturn(task);
        when(taskRepository.findAll()).thenReturn(taskList);
        when(taskRepository.viewListOfTasksByStatus(String.valueOf(Status.PENDING), user.getId())).thenReturn(taskList);
        when(taskRepository.findById(1)).thenReturn(Optional.ofNullable(task));
        when(userRepository.findUserByEmail("mary@gmail.com")).thenReturn(Optional.of(user));
        when(taskRepository.viewListOfTasksByStatus(String.valueOf(Status.IN_PROGRESS), 1)).thenReturn(taskList);
    }

    @Test
    void registerUser() {
        when(userServiceImpl.registerUser(userDTO)).thenReturn(user);
        var actual = userServiceImpl.registerUser(userDTO);
        var expected = user;
        assertEquals( expected , actual );
    }

    @Test
    void loginUser_Successful() {
        String message = "Success";
        assertEquals(message , userServiceImpl.loginUser("mary@gmail.com" , "1234"));
    }

    @Test
    void loginUser_Unsuccessful() {
        String message = "incorrect password";
        assertEquals(message , userServiceImpl.loginUser("mary@gmail.com" , "123456"));
    }

    @Test
    void createTask() {
        when(userServiceImpl.createNewTask(task, user.getId())).thenReturn(task);
        assertEquals(task , userServiceImpl.createNewTask(task, user.getId()));
    }

    @Test
    void EditTask() {
        when(userServiceImpl.updateTask(task, task.getId())).thenReturn(task);
        var actual = userServiceImpl.updateTask(task, task.getId());
        var expected = task;
        assertEquals(expected, actual);
    }

    @Test
    void viewAllTasks() {
        when(userServiceImpl.viewAllTasks()).thenReturn(taskList);
        var actual = userServiceImpl.viewAllTasks().size();
        var expected = 1;
        assertEquals(expected, actual);
    }

    @Test
    void viewAllTaskByStatus() {
        assertEquals(taskList , userServiceImpl.viewAllTaskByStatus(String.valueOf(Status.PENDING), user.getId()));
    }

    @Test
    void deleteTaskById() {
        assertTrue(userServiceImpl.deleteById(task.getId()));
    }

    @Test
    void updateTaskStatus() {
        assertTrue(userServiceImpl.updateTaskByStatus(task));
    }

    @Test
    void getUserByEmail() {
        assertEquals(user , userServiceImpl.getUserByEmail("mary@gmail.com"));
    }

    @Test
    void getTaskById() {
        assertEquals(task, userServiceImpl.getTaskById(1));
    }
}