package web.controller;

import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import web.model.User;
import web.service.UserService;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String redirectToUserList() {
        return "redirect:/users";
    }

    @GetMapping
    public String showUserList(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "list";
    }

    @GetMapping("/new")
    public String showCreateUserForm(Model model) {
        model.addAttribute("user", new User());
        return "create";
    }

    @PostMapping("/save")
    public String saveUser(@ModelAttribute User user, Model model) {
        try {
            userService.saveUser(user);
        } catch (Exception e) {
            model.addAttribute("error", "Ошибка: Пользователь с таким email уже существует!");
            return "create";
        }
        return "redirect:/users";
    }

    @GetMapping("/edit")
    public String showEditUserForm(@RequestParam Long id, Model model) {
        User user = userService.getUserById(id);
        if (user == null) {
            return "redirect:/users";
        }
        model.addAttribute("user", user);
        return "edit";
    }

    @PostMapping("/update")
    public String updateUser(@ModelAttribute User user) {
        userService.saveUser(user);
        return "redirect:/users";
    }

    @PostMapping("/delete")
    public String deleteUser(@RequestParam("id") Long id, Model model) {
        try {
            userService.deleteUser(id);
            model.addAttribute("success", "User successfully deleted!");
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", "Invalid User ID: " + e.getMessage());
        } catch (Exception e) {
            model.addAttribute("error", "Failed to delete user: " + e.getMessage());
        }
        return "redirect:/users";
    }
}
