package ru.MaxResh.CreditCalculator.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.MaxResh.CreditCalculator.model.User;
import ru.MaxResh.CreditCalculator.model.MonthlyAmortizationSchedule;
import ru.MaxResh.CreditCalculator.repos.RequestRepo;
import ru.MaxResh.CreditCalculator.repos.UserRepo;
import ru.MaxResh.CreditCalculator.service.AmortizationService;
import ru.MaxResh.CreditCalculator.service.UserService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Random;

@Controller
public class RequestController {
    @Autowired
    private AmortizationService amortizationService;

    @Autowired
    private RequestRepo requestRepo;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepo userRepo;

    public void setMonthlyAmortizationSchedule(MonthlyAmortizationSchedule monthlyAmortizationSchedule) {
        this.monthlyAmortizationSchedule = monthlyAmortizationSchedule;
    }

    MonthlyAmortizationSchedule monthlyAmortizationSchedule;

    @RequestMapping(value = "/addRequest", method = RequestMethod.GET)
    public String sendRequest() throws IOException {

        User user = (User) userService.getAuthenticatedUser();
        URL url = new URL("http://192.168.43.214:8090/rate?series="+user.getSeriesPassport()+"&id="+user.getNumberPassport());
        URLConnection yc = url.openConnection();
        yc.setConnectTimeout(500);
        String i = null;
        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(
                        yc.getInputStream()))) {
            i = in.readLine();
        }catch (ConnectException | SocketTimeoutException e){}
        if (i != null && !(i.equals(""))) {
            var inpRate = Integer.parseInt(i);
            user.setRate(inpRate);
        }
        else {
            user.setRate(300);
        }


        var rate = new Random().nextInt(2000)-1000;
        System.out.println(rate);
        if (user.getRate() < 0) {
            return "tooLowRate";
        }

        userRepo.save(user);
        monthlyAmortizationSchedule.setUser(user);

        requestRepo.save(monthlyAmortizationSchedule);

        return "request";
    }

}
