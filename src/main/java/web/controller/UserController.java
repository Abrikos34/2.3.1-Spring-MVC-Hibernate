package web.controller;

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
    public String homeRedirect() {
        return "redirect:/users";
    }

    @GetMapping
    public String listUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "list";
    }

    @GetMapping("/new")
    public String newUserForm(Model model) {
        model.addAttribute("user", new User());
        return "create";
    }

    @PostMapping("/save")
    public String saveUser(@ModelAttribute User user) {
        userService.saveUser(user);
        return "redirect:/users";
    }

    @GetMapping("/edit")
    public String editUserForm(@RequestParam Long id, Model model) {
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

    @GetMapping("/delete")
    public String deleteUser(@RequestParam Long id) {
        if (id != null) {
            userService.deleteUser(id);
        }
        return "redirect:/users";
    }

    @GetMapping("/reset")
    public String resetIds() {
        try {
            userService.reorderIds();
            System.out.println("✅ IDs успешно обновлены.");
        } catch (Exception e) {
            System.err.println("❌ Ошибка при обновлении ID: " + e.getMessage());

        }
        return "redirect:/users";
    }

    @ControllerAdvice
    public class GlobalExceptionHandler {

        @ExceptionHandler(RuntimeException.class)
        public String handleRuntimeException(RuntimeException e, Model model) {
            model.addAttribute("errorMessage", "❌ Произошла ошибка: " + e.getMessage());
            return "error";
        }
    }

}
