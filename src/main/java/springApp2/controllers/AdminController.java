package springApp2.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import springApp2.services.UserService;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
//@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    private final UserService userService;

    @GetMapping("")
    public String index(Model model) {
        model.addAttribute("users", userService.getUsers());
        return "admin/indexTemplate";
    }

    @PostMapping("/user/ban/{id}")
    public String userBan(@PathVariable("id") Integer id) {
        userService.userBan(id);
        return "redirect:/admin";

    }

    @GetMapping("/user/edit/{id}")
    public String userEdit(@PathVariable("id") Integer id) {
        userService.setRole(id);
        return "redirect:/admin";

    }

}



