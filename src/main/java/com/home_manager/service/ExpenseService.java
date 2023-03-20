package com.home_manager.service;

import com.home_manager.model.dto.AddExpenseDTO;
import com.home_manager.model.entities.Expense;
import com.home_manager.model.entities.Month;
import com.home_manager.repository.ExpenseRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepository;

    public ExpenseService(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    public Expense addExpenseToMonth(Month month, AddExpenseDTO addExpenseDTO) {

        Expense expense = new Expense();
        expense.setMonth(month);
        expense.setAddedOn(LocalDate.now());
        expense.setName(addExpenseDTO.getName());
        expense.setValue(addExpenseDTO.getValue());
        expense.setDocumentNumber(addExpenseDTO.getDocumentNumber());

        if (addExpenseDTO.getDocumentDate() == null) {
            expense.setDocumentDate(LocalDate.now());
        } else {
            expense.setDocumentDate(addExpenseDTO.getDocumentDate());
        }

        return this.expenseRepository.save(expense);
    }

    public Expense getExpenseById(long expenseId) {
        return this.expenseRepository.getExpenseById(expenseId);
    }

    public Expense editExpense(String name, double value, String documentNumber, long expenseId) {

        Expense expense = getExpenseById(expenseId);
        expense.setName(name);
        expense.setValue(value);
        expense.setDocumentNumber(documentNumber);

        return this.expenseRepository.save(expense);
    }
}
