package ru.MaxResh.CreditCalculator.model;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "requests")
public class MonthlyAmortizationSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startDate;
    @Min(10000) @Max(1000000000)
    private double initialBalance;
    @Min(1) @Max(366)
    private double interestRate;
    @Min(1) @Max(10000)
    private int durationInMonths;
    @Min(0) @Max(9999)
    private double futureValue;
    private int paymentType;
    private double monthlyPayment;
    private Boolean status;
    @Transient
    private List<Payment> paymentList = new ArrayList<Payment>();

    @ManyToOne(fetch = FetchType.EAGER)
    private User user;


    public MonthlyAmortizationSchedule() {
    }

    public MonthlyAmortizationSchedule(Date startDate, double initialBalance, double interestRate, int durationInMonths, double futureValue, int paymentType, double monthlyPayment) {
        this.startDate = startDate;
        this.initialBalance = initialBalance;
        this.interestRate = interestRate;
        this.durationInMonths = durationInMonths;
        this.futureValue = futureValue;
        this.paymentType = paymentType;
        this.monthlyPayment = monthlyPayment;
    }

    public Date getStartDate()
    {
        return this.startDate;
    }
    public void setStartDate(Date startDate)
    {
        this.startDate = startDate;
    }

    public double getInitialBalance()
    {
        return this.initialBalance;
    }
    public void setInitialBalance(double initialBalance)
    {
        this.initialBalance = initialBalance;
    }

    public double getInterestRate()
    {
        return this.interestRate;
    }
    public void setInterestRate(double interestRate)
    {
        this.interestRate = interestRate;
    }

    public int getDurationInMonths()
    {
        return this.durationInMonths;
    }
    public void setDurationInMonths(int durationInMonths)
    {
        this.durationInMonths = durationInMonths;
    }

    public double getFutureValue()
    {
        return this.futureValue;
    }
    public void setFutureValue(double futureValue)
    {
        this.futureValue = futureValue;
    }

    public int getPaymentType()
    {
        return this.paymentType;
    }
    public void setPaymentType(int paymentType)
    {
        this.paymentType = paymentType;
    }

    public double getMonthlyPayment()
    {
        return this.monthlyPayment;
    }
    public void setMonthlyPayment(double monthlyPayment) { this.monthlyPayment = monthlyPayment; }

    public List<Payment> getPaymentList() { return this.paymentList; }
    public void setPaymentList(List<Payment> paymentList) { this.paymentList = paymentList; }

    public void addAllPayments(List<Payment> paymentList) { this.paymentList.addAll(paymentList); }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString()
    {
        return "[" + startDate + "," + initialBalance + "," + interestRate + "," + durationInMonths + "," + futureValue + "," + paymentType + "," + monthlyPayment + "]";
    }

}
