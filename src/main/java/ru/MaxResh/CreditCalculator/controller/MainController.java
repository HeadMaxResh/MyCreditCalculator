package ru.MaxResh.CreditCalculator.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.MaxResh.CreditCalculator.model.MonthlyAmortizationSchedule;
import ru.MaxResh.CreditCalculator.service.AmortizationService;

import javax.servlet.http.HttpServletRequest;

@Controller
public class MainController {
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String home(@ModelAttribute MonthlyAmortizationSchedule monthlyAmortizationSchedule, Model model) {
        model.addAttribute("title", "Credit calculator");
        return "home";
    }
}
