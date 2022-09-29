package ru.MaxResh.CreditCalculator.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.MaxResh.CreditCalculator.model.MonthlyAmortizationSchedule;
import ru.MaxResh.CreditCalculator.model.Payment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class AmortizationService {
    @Autowired
    private PaymentService paymentService;

    public void initializeUnknownFields(MonthlyAmortizationSchedule monthlyAmortizationSchedule)
    {
        Date startDate = monthlyAmortizationSchedule.getStartDate();
        double initialBalance = monthlyAmortizationSchedule.getInitialBalance();
        double interestRate = monthlyAmortizationSchedule.getInterestRate();
        int durationInMonths = monthlyAmortizationSchedule.getDurationInMonths();
        double futureValue = -monthlyAmortizationSchedule.getFutureValue();
        int paymentType = monthlyAmortizationSchedule.getPaymentType();

        double monthlyPayment = paymentService.pmt(paymentService.getMonthlyInterestRate(interestRate), durationInMonths, initialBalance, futureValue, paymentType);
        monthlyAmortizationSchedule.setMonthlyPayment(monthlyPayment);

        List<Payment> paymentList = calculatePaymentList(startDate, initialBalance, durationInMonths, paymentType, interestRate, futureValue);
        monthlyAmortizationSchedule.addAllPayments(paymentList);
    }
    public List<Payment> calculatePaymentList(Date startDate, double initialBalance, int durationInMonths, int paymentType, double interestRate, double futureValue)
    {
        List<Payment> paymentList = new ArrayList<Payment>();
        Date loopDate = startDate;
        double balance = initialBalance;
        double accumulatedInterest = 0;
        for (int paymentNumber = 1; paymentNumber <= durationInMonths; paymentNumber++)
        {
            if (paymentType == 0)
            {
                loopDate = addOneMonth(loopDate);
            }
            double principalPaid = paymentService.ppmt(paymentService.getMonthlyInterestRate(interestRate), paymentNumber, durationInMonths, initialBalance, futureValue, paymentType);
            double interestPaid = paymentService.ipmt(paymentService.getMonthlyInterestRate(interestRate), paymentNumber, durationInMonths, initialBalance, futureValue, paymentType);
            balance = balance + principalPaid;
            accumulatedInterest += interestPaid;

            Payment payment = new Payment(paymentNumber, loopDate, balance, principalPaid, interestPaid, accumulatedInterest);

            paymentList.add(payment);

            if (paymentType == 1)
            {
                loopDate = addOneMonth(loopDate);
            }
        }
        return paymentList;
    }
    private Date addOneMonth(Date date)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, 1);
        return cal.getTime();
    }
}
