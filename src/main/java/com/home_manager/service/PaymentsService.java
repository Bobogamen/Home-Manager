package com.home_manager.service;

import com.home_manager.model.entities.Month;
import com.home_manager.model.entities.MonthHomes;
import com.home_manager.model.entities.Payment;
import com.home_manager.repository.PaymentsRepository;
import com.home_manager.model.dto.FeePaymentDTO;
import com.home_manager.model.dto.HomePaymentsDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PaymentsService {

    private final PaymentsRepository paymentsRepository;

    public PaymentsService(PaymentsRepository paymentsRepository) {
        this.paymentsRepository = paymentsRepository;
    }

    public void setPayments(Month updatedMonth) {

        List<Payment> paymentList = new ArrayList<>();

        updatedMonth.getHomes().forEach(h -> h.getHome().getFees().forEach(f -> {
            Payment payment = new Payment();
            payment.setFee(f.getFee());
            payment.setMonthHomes(h);
            payment.setValuePaid(0);
            payment.setTimesPaid(0);

            paymentList.add(payment);
        }));

        this.paymentsRepository.saveAll(paymentList);
    }

    public List<HomePaymentsDTO> getPaymentsByHome(MonthHomes monthHome) {
        return monthHome.getHome().getFees().stream().map(f -> {
            HomePaymentsDTO homePaymentsDTO = new HomePaymentsDTO();

            homePaymentsDTO.setId(f.getFee().getId());
            homePaymentsDTO.setName(f.getFee().getName());
            homePaymentsDTO.setValue(f.getFee().getValue());
            homePaymentsDTO.setTimes(f.getTimes());
            homePaymentsDTO.setTotal();

            return homePaymentsDTO;
        }).toList();
    }

    public double makePayments(long monthHomeId, FeePaymentDTO[] feePaymentDTOS) throws Exception {
        List<Payment> monthHomesPayments = this.paymentsRepository.findPaymentByMonthHomesId(monthHomeId);

        if (monthHomesPayments.size() != feePaymentDTOS.length) {
            throw new Exception("Month payments size is different to requested payments size");
        }

        for (Payment payment : monthHomesPayments) {
            FeePaymentDTO feePaymentDTO = getFeePaymentDTO(payment.getFee().getId(), feePaymentDTOS);

            payment.setFeeValue(feePaymentDTO.getValue());
            payment.setTimesPaid(feePaymentDTO.getTimesPaid());
            payment.setValuePaid(feePaymentDTO.getTotal());
        }

        this.paymentsRepository.saveAll(monthHomesPayments);

        return monthHomesPayments.stream().mapToDouble(Payment::getValuePaid).sum();
    }

    private FeePaymentDTO getFeePaymentDTO(long id, FeePaymentDTO[] feePaymentDTOS) throws Exception {
        for (FeePaymentDTO feePaymentDTO : feePaymentDTOS) {
            if (feePaymentDTO.getId() == id) {
                return feePaymentDTO;
            }
        }
        throw new Exception("FeePaymentDTO is not found!");
    }
}
