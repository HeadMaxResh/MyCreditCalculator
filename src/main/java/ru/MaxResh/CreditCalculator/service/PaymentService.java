package ru.MaxResh.CreditCalculator.service;

import org.springframework.stereotype.Service;

import java.text.NumberFormat;
import java.text.ParseException;

@Service
public class PaymentService {
    private final NumberFormat nfPercent;
    private final NumberFormat nfCurrency;

    PaymentService()
    {
        nfPercent = NumberFormat.getPercentInstance();
        nfPercent.setMinimumFractionDigits(2);
        nfPercent.setMaximumFractionDigits(4);

        nfCurrency = NumberFormat.getCurrencyInstance();
        nfCurrency.setMinimumFractionDigits(2);
        nfCurrency.setMaximumFractionDigits(2);
    }

    public String formatCurrency(double number)
    {
        return nfCurrency.format(number);
    }

    public String formatPercent(double number)
    {
        return nfPercent.format(number);
    }

    public double stringToPercent(String s) throws ParseException
    {
        return nfPercent.parse(s).doubleValue();
    }

    public double getMonthlyInterestRate(double interestRate)
    {
        return interestRate / 100 / 12;
    }

    //вычисляет аннуитетный платеж/доходность за период.
    public double pmt(double r, int nper, double pv, double fv, int type)
    {
        if (r == 0) {
            return -(pv + fv) / nper;
        }

        double pmt = r / (Math.pow(1 + r, nper) - 1) * -(pv * Math.pow(1 + r, nper) + fv);

        if (type == 1) {
            pmt /= (1 + r);
        }

        return pmt;
    }

    public double pmt(double r, int nper, double pv, double fv)
    {
        return pmt(r, nper, pv, fv, 0);
    }

    public double pmt(double r, int nper, double pv)
    {
        return pmt(r, nper, pv, 0, 0);
    }

    public double fv(double r, int nper, double c, double pv, int type)
    {
        if (r == 0) return pv;

        if (type == 1) {
            c *= (1 + r);
        }

        double fv = -((Math.pow(1 + r, nper) - 1) / r * c + pv * Math.pow(1 + r, nper));

        return fv;
    }

    public double fv(double r, int nper, double c, double pv)
    {
        return fv(r, nper, c, pv, 0);
    }

    //вычисляет часть платежа в данный период, которая представляет собой проценты на предыдущий остаток.
    public double ipmt(double r, int per, int nper, double pv, double fv, int type)
    {
        double ipmt = fv(r, per - 1, pmt(r, nper, pv, fv, type), pv, type) * r;

        if (type == 1) {
            ipmt /= (1 + r);
        }

        return ipmt;
    }

    //вычисляет часть платежа в заданный период, которая будет применяться к основному долгу
    public double ppmt(double r, int per, int nper, double pv, double fv, int type)
    {
        return pmt(r, nper, pv, fv, type) - ipmt(r, per, nper, pv, fv, type);
    }
}
