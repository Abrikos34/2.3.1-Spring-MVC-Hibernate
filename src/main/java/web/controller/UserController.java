package web.controller;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    public String deleteUser(@RequestParam("id") Long id, RedirectAttributes redirectAttributes) {
        if (id != null) {
            try {
                userService.deleteUser(id);
                redirectAttributes.addFlashAttribute("success", "User successfully deleted!");
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("error", "Failed to delete user: " + e.getMessage());
            }
        } else {
            redirectAttributes.addFlashAttribute("error", "User ID cannot be null!");
        }
        return "redirect:/users";
    }


    @GetMapping("/reset")
    public String resetUserIds() {
        try {
            userService.reorderIds();
            System.out.println("✅ IDs успешно обновлены.");
        } catch (Exception e) {
            System.err.println("❌ Ошибка при обновлении ID: " + e.getMessage());
        }
        return "redirect:/users";
    }
}
