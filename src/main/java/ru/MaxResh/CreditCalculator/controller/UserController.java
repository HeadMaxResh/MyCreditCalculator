package ru.MaxResh.CreditCalculator.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.MaxResh.CreditCalculator.model.MonthlyAmortizationSchedule;
import ru.MaxResh.CreditCalculator.model.Role;
import ru.MaxResh.CreditCalculator.model.User;
import ru.MaxResh.CreditCalculator.repos.RequestRepo;
import ru.MaxResh.CreditCalculator.repos.UserRepo;
import ru.MaxResh.CreditCalculator.service.UserService;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private RequestRepo requestRepo;
    @Autowired
    private UserService userService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public String userList(Model model) {
        model.addAttribute("users", userRepo.findAll());
        model.addAttribute("userRole", Role.USER);
        return "userList";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("{user}")
    public String userEditForm(@PathVariable User user, Model model) {
        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values());
        return "userEdit";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("{user}/requestList")
    public String userRequestList(User user, Model model) {
        model.addAttribute("requests", requestRepo.findByUser(user));
        return "userRequests";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public String userSave(
            @RequestParam String userLogin,
            @RequestParam Map<String, String> form,
            @RequestParam("userId") User user,
            @RequestParam String userName,
            @RequestParam String phone,
            @RequestParam String dateBirthday,
            @RequestParam String seriesPassport,
            @RequestParam String numberPassport
    ){

        userService.saveUser(user, userLogin,
                            form, userName,
                            phone, dateBirthday,
                            seriesPassport, numberPassport);

        return "redirect:/user";
    }

    @GetMapping("profile")
    @PreAuthorize("hasAuthority('USER')")
    public String getProfile(Model model, @AuthenticationPrincipal User user) {
        List<MonthlyAmortizationSchedule> monthlyAmortizationSchedules = requestRepo.findByUser((User)userService.getAuthenticatedUser());
        model.addAttribute("empty", monthlyAmortizationSchedules.isEmpty());
        model.addAttribute("requests", monthlyAmortizationSchedules);
        model.addAttribute("user", user);

        return "profile";
    }

    @PostMapping("profile/delete/{id}")
    @PreAuthorize("hasAuthority('USER')")
    public String delete(Model model, @PathVariable Long id) {
        MonthlyAmortizationSchedule monthlyAmortizationSchedule = requestRepo.findById(id).orElseThrow();

        requestRepo.delete(monthlyAmortizationSchedule);
        return "redirect:/user/profile";
    }
}
