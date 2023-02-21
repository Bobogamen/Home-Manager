package com.home_manager.service;

import com.home_manager.model.dto.FeePaymentDTO;
import com.home_manager.model.dto.HomePaymentsDTO;
import com.home_manager.model.entities.MonthHomes;
import com.home_manager.model.entities.Payment;
import com.home_manager.repository.FeeRepository;
import com.home_manager.repository.MonthRepository;
import com.home_manager.repository.PaymentsRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PaymentsService {

    private final PaymentsRepository paymentsRepository;
    private final FeeRepository feeRepository;
    private final MonthRepository monthRepository;

    public PaymentsService(PaymentsRepository paymentsRepository, FeeRepository feeRepository, MonthRepository monthRepository) {
        this.paymentsRepository = paymentsRepository;
        this.feeRepository = feeRepository;
        this.monthRepository = monthRepository;
    }

    public List<HomePaymentsDTO> getPaymentsByHome(MonthHomes monthHome) {
        return monthHome.getHome().getFees().stream().map(f -> {
            HomePaymentsDTO homePaymentsDTO = new HomePaymentsDTO();

            homePaymentsDTO.setId(f.getFee().getId());
            homePaymentsDTO.setName(f.getFee().getName());
            homePaymentsDTO.setValue(f.getFee().getValue());
            homePaymentsDTO.setTimes(f.getTimes());
            homePaymentsDTO.calculateTotal();

            return homePaymentsDTO;
        }).toList();
    }

    public double makePayments(MonthHomes monthHome, FeePaymentDTO[] feePaymentDTOS) throws Exception {
        List<Payment> payments = new ArrayList<>();

        for (FeePaymentDTO feePaymentDTO : feePaymentDTOS) {
            Payment payment = new Payment();

            payment.setFeeValue(feePaymentDTO.getValue());
            payment.setTimesPaid(feePaymentDTO.getTimesPaid());
            payment.setValuePaid(feePaymentDTO.getTotal());
            payment.setFee(this.feeRepository.getFeeById(feePaymentDTO.getId()));
            payment.setMonthHomes(monthHome);

            payments.add(payment);
        }

        this.paymentsRepository.saveAll(payments);

        return payments.stream().mapToDouble(Payment::getValuePaid).sum();
    }

    public List<HomePaymentsDTO> viewPaymentsByHome(MonthHomes monthHome) {

        List<HomePaymentsDTO> payments = new ArrayList<>();

        monthHome.getPayments().forEach(p -> {
            HomePaymentsDTO homePaymentsDTO = new HomePaymentsDTO();

            homePaymentsDTO.setName(p.getFee().getName());
            homePaymentsDTO.setValue(p.getFeeValue());
            homePaymentsDTO.setTimes(p.getTimesPaid());
            homePaymentsDTO.setTotal(p.getValuePaid());

            payments.add(homePaymentsDTO);
        });

        return payments;
    }
}
