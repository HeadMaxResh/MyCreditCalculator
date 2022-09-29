package ru.MaxResh.CreditCalculator.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import ru.MaxResh.CreditCalculator.model.User;
import ru.MaxResh.CreditCalculator.service.UserService;

import javax.validation.Valid;
import java.util.Map;

@Controller
public class RegistrationController {
    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String register(Model model){
        model.addAttribute("user", new User());
        return "register";
    }
    @PostMapping("/register")
    public String addUser(@Valid User user, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()){
            Map<String, String> errors = ControllerUtils.getErrors(bindingResult);

            model.mergeAttributes(errors);

            return "register";
        }
        if (user.getPassword() != null && !user.getPassword().equals(user.getPasswordConfirmation())){
            bindingResult.addError(new FieldError("user", "password", "Passwords are different!"));

            return "register";
        }
        if (!userService.addUser(user)){
            model.addAttribute("userNameError", "User exist!");
            return "register";
        }

        return "redirect:/login";
    }
}
