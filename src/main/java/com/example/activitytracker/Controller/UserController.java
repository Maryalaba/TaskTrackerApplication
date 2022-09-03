package com.example.activitytracker.Controller;

import com.example.activitytracker.DTO.UserDTO;
import com.example.activitytracker.Exception.TaskNotFoundException;
import com.example.activitytracker.Model.Status;
import com.example.activitytracker.Model.Task;
import com.example.activitytracker.Model.User;
import com.example.activitytracker.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
//@RequiredArgsConstructor
@RequestMapping(value = "/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/login")
    public String displayLoginPage(Model model){
        model.addAttribute("userDetails" , new UserDTO());
        return "loginForm";
    }

    @PostMapping(value = "/loginUser")
    public String loginUser(@ModelAttribute("userDetails") UserDTO userDTO, HttpSession session , Model model){
        String message =  userService.loginUser(userDTO.getEmail(), userDTO.getPassword());
        User user = userService.getUserByEmail(userDTO.getEmail());

        if(message.equals("Success")){
            session.setAttribute("loggedInUser" , user);
            session.setAttribute("id" , user.getId());
            session.setAttribute("name" , user.getName());
            return "redirect:/user/dashboard";
        }else{
            model.addAttribute("errorMessage" , message);
            return  "redirect:/user/login";
        }
    }

    @GetMapping(value = "/register")
    public  String showRegistrationForm(Model model){
        model.addAttribute("userRegistrationDetails" , new UserDTO());
        return "registerForm";
    }

    @PostMapping(value = "/userRegistration")
    public String registration(@ModelAttribute UserDTO userDTO){

        User registeredUser = userService.registerUser(userDTO);
        if (registeredUser != null){

            return "redirect:/user/login";
        }else {
            return "redirect:/user/register";
        }
    }

    @GetMapping("/dashboard")
    public String index(Model model, HttpSession session){
     User user = (User) session.getAttribute("loggedInUser");
     List<Task> allTasks = userService.viewAllTasks();
      model.addAttribute("tasks" , allTasks);
      model.addAttribute("user" , user);
        return "dashboard";
    }


    @GetMapping(value = "/task/pending")
    public String taskByPendingStatus(Model model, HttpSession session){
        User user = (User) session.getAttribute("loggedInUser");
        List<Task> listOfTaskByStatus = userService.viewAllTaskByStatus(String.valueOf(Status.PENDING), user.getId());
        model.addAttribute("tasks" , listOfTaskByStatus);
        return "dashboard";
    }

    @GetMapping(value = "/task/ongoing")
    public String taskByOnGoingStatus(Model model, HttpSession session){
        User user = (User) session.getAttribute("loggedInUser");
        List<Task> listOfTaskByStatus = userService.viewAllTaskByStatus(String.valueOf(Status.IN_PROGRESS), user.getId());
        model.addAttribute("tasks" , listOfTaskByStatus);
        return "dashboard";
    }

    @GetMapping(value = "/task/completed")
    public String taskByCompletedStatus(Model model, HttpSession session){
        User user = (User) session.getAttribute("loggedInUser");
        List<Task> listOfTaskByStatus = userService.viewAllTaskByStatus(String.valueOf(Status.DONE), user.getId());
        model.addAttribute("tasks" , listOfTaskByStatus);
        return "dashboard";
    }

    @GetMapping("/delete/{id}")
    public String deleteTask(@PathVariable(name = "id") Integer id){
        userService.deleteById(id);
        return "redirect:/user/dashboard";
    }

    @GetMapping(value = "/editPage/{id}")
    public String showEditPage(@PathVariable(name = "id") Integer id , Model model){
        Task task = userService.getTaskById(id);
        model.addAttribute("singleTask" , task);
        model.addAttribute("taskBody", new Task());
        return "editPage";
    }

    @PostMapping(value = "/edit/{id}")
    public String editTask(@PathVariable( name = "id") Integer id, @ModelAttribute Task task){
        userService.updateTask(task, id);
        return "redirect:/user/dashboard";
    }

    @GetMapping(value = "/addNewTask")
    public String addTask(Model model){
        model.addAttribute("newTask" , new Task());
        return "createTaskForm";
    }

    @PostMapping(value = "/addTask")
    public String createNewTask(@ModelAttribute("newTask") Task task, HttpSession session){
        User user = (User) session.getAttribute("loggedInUser");
        task.setUser(user);
        userService.createNewTask(task, user.getId());
        return "redirect:/user/dashboard";
    }

    @GetMapping(value = "/start/{id}")
    public String updateTaskStatus(@PathVariable(name="id") Integer id,
                                HttpSession session){
        User user = (User) session.getAttribute("loggedInUser");
        Task task = userService.getTaskById(id);
//        if(task == null){
//            throw new TaskNotFoundException("task not found");
//        }
//        if(!Objects.equals(task.getUser().getId(), user.getId())){
//            throw new TaskNotFoundException("unauthorized");
//        }
//        if (Objects.equals(task.getStatus(), Status.DONE.name())){
//            throw new TaskNotFoundException("cannot edit done task");
//        }
        task.setStatus(String.valueOf(Status.IN_PROGRESS));
        userService.updateTaskByStatus(task);
        return "redirect:/user/dashboard";
    }
}
