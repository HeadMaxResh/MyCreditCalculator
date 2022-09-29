package ru.MaxResh.CreditCalculator.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.MaxResh.CreditCalculator.model.MonthlyAmortizationSchedule;
import ru.MaxResh.CreditCalculator.repos.RequestRepo;
import ru.MaxResh.CreditCalculator.service.AmortizationService;

import javax.validation.Valid;
import java.util.Map;

@Controller
public class ScheduleController {
    @Autowired
    private AmortizationService amortizationService;
    @Autowired
    private RequestController requestController;
    @Autowired
    private RequestRepo requestRepo;


    @RequestMapping(value = "/showSchedule", method = RequestMethod.POST)
    public String calculatePayments(@Valid @ModelAttribute MonthlyAmortizationSchedule monthlyAmortizationSchedule, BindingResult bindingResult, Model model)
    {
        if (bindingResult.hasErrors()){
            Map<String, String> errors = ControllerUtils.getErrors(bindingResult);

            model.mergeAttributes(errors);

            return "home";
        }

        //monthlyAmortizationSchedule.setFutureValue(monthlyAmortizationSchedule.getFutureValue() * (-1));

        amortizationService.initializeUnknownFields(monthlyAmortizationSchedule);
        model.addAttribute(monthlyAmortizationSchedule);

        requestController.setMonthlyAmortizationSchedule(monthlyAmortizationSchedule);

        return "schedule";
    }

    @RequestMapping(value = "/showFinishedSchedule/{id}", method = RequestMethod.GET)
    public String calculateFinishedPayments(Model model, @PathVariable Long id)
    {
        MonthlyAmortizationSchedule monthlyAmortizationSchedule = requestRepo.findById(id).orElseThrow();
        amortizationService.initializeUnknownFields(monthlyAmortizationSchedule);
        model.addAttribute(monthlyAmortizationSchedule);

        requestController.setMonthlyAmortizationSchedule(monthlyAmortizationSchedule);

        return "finishedSchedule";
    }
    @PreAuthorize("hasAuthority('MODER')")
    @GetMapping("uncheckedRequest")
    public String getUnconfirmedRequests(Model model) {
        model.addAttribute("unchecked", requestRepo.findConsidered());
        return "uncheckedRequest";
    }

    @PreAuthorize("hasAuthority('MODER')")
    @PostMapping("uncheckedRequest/confirm/{id}")
    public String confirmRequest(Model model, @PathVariable Long id) {
        MonthlyAmortizationSchedule monthlyAmortizationSchedule = requestRepo.findById(id).orElseThrow();
        monthlyAmortizationSchedule.setStatus(true);
        requestRepo.save(monthlyAmortizationSchedule);
        return "redirect:/uncheckedRequest";
    }

    @PreAuthorize("hasAuthority('MODER')")
    @PostMapping("uncheckedRequest/unconfirm/{id}")
    public String unconfirmRequest(Model model, @PathVariable Long id) {
        MonthlyAmortizationSchedule monthlyAmortizationSchedule = requestRepo.findById(id).orElseThrow();
        monthlyAmortizationSchedule.setStatus(false);
        requestRepo.save(monthlyAmortizationSchedule);
        return "redirect:/uncheckedRequest";
    }

    @PreAuthorize("hasAuthority('MODER')")
    @GetMapping("checkedRequest")
    public String getCheckedRequests(Model model) {
        model.addAttribute("checked", requestRepo.findConfirmed());
        return "checkedRequest";
    }

    @PreAuthorize("hasAuthority('MODER')")
    @GetMapping("rejectedRequest")
    public String getRejectedRequests(Model model) {
        model.addAttribute("rejected", requestRepo.findRejected());
        return "rejectedRequest";
    }
}
