package com.home_manager;

import com.home_manager.model.dto.*;
import com.home_manager.model.enums.HomesGroupEnums;
import com.home_manager.model.enums.Notifications;
import com.home_manager.model.user.HomeManagerUserDetails;
import com.home_manager.service.EmailService;
import com.home_manager.utility.MailUtility;
import com.home_manager.utility.MonthsUtility;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;

import java.time.LocalDate;

@SpringBootTest
class HomeManagerApplicationTests {

    @Autowired
    private EmailService emailService;

    @Test
    void testPasswordResetMailSubject() {
        Assertions.assertEquals("Домоуправител забравена парола", MailUtility.passwordResetMailSubject());
    }
    @Test
    void testRegistrationMailSubject() {
        Assertions.assertEquals("Добре дошли в Домоуправител", MailUtility.registrationMailSubject());
    }

    @Test
    void testGetMonthName() {
        Assertions.assertEquals("Януари", MonthsUtility.getMonthName(1));
        Assertions.assertEquals("Април", MonthsUtility.getMonthName(4));
        Assertions.assertEquals("Юли", MonthsUtility.getMonthName(7));
        Assertions.assertEquals("Септември", MonthsUtility.getMonthName(9));
    }

    @Test
    void testAddExpenseDTO() {
        AddExpenseDTO addExpenseDTO = new AddExpenseDTO();
        addExpenseDTO.setName("Разход");
        addExpenseDTO.setValue(9.99);
        addExpenseDTO.setDocumentNumber("1234567890");
        addExpenseDTO.setDocumentDate(LocalDate.now());

        Assertions.assertEquals("Разход", addExpenseDTO.getName());
        Assertions.assertEquals(9.99, addExpenseDTO.getValue());
        Assertions.assertEquals("1234567890", addExpenseDTO.getDocumentNumber());
        Assertions.assertEquals(LocalDate.now(), addExpenseDTO.getDocumentDate());
    }

    @Test
    void testAddHomeDTO() {
        AddHomeDTO addHomeDTO = new AddHomeDTO();
        addHomeDTO.setFloor("1");
        addHomeDTO.setName("9");
        addHomeDTO.setOwnerFirstName("First name");
        addHomeDTO.setOwnerMiddleName("Middle name");
        addHomeDTO.setOwnerLastName("Last name");
        addHomeDTO.setOwnerPhoneNumber("0123456789");
        addHomeDTO.setResident(true);
        addHomeDTO.setResidentFirstName("Resident");
        addHomeDTO.setResidentMiddleName("Middle");
        addHomeDTO.setResidentLastName("Last");
        addHomeDTO.setResidentPhoneNumber("0987654321");


        Assertions.assertEquals("1", addHomeDTO.getFloor());
        Assertions.assertEquals("9", addHomeDTO.getName());
        Assertions.assertEquals("First name", addHomeDTO.getOwnerFirstName());
        Assertions.assertEquals("Middle name", addHomeDTO.getOwnerMiddleName());
        Assertions.assertEquals("Last name", addHomeDTO.getOwnerLastName());
        Assertions.assertEquals("0123456789", addHomeDTO.getOwnerPhoneNumber());
        Assertions.assertNull(addHomeDTO.getOwnerEmail());
        Assertions.assertTrue(addHomeDTO.isResident());
        Assertions.assertEquals("Resident", addHomeDTO.getResidentFirstName());
        Assertions.assertEquals("Middle", addHomeDTO.getResidentMiddleName());
        Assertions.assertEquals("Last", addHomeDTO.getResidentLastName());
        Assertions.assertEquals("0987654321", addHomeDTO.getResidentPhoneNumber());
        Assertions.assertNull(addHomeDTO.getResidentEmail());
    }

    @Test
    void testAddHomesGroupDTO() {
        AddHomesGroupDTO addHomesGroupDTO = new AddHomesGroupDTO();
        addHomesGroupDTO.setName("Name");
        addHomesGroupDTO.setSize(99);
        addHomesGroupDTO.setType(HomesGroupEnums.APARTMENT_BUILDING.getValue());

        Assertions.assertEquals("Name", addHomesGroupDTO.getName());
        Assertions.assertEquals(99, addHomesGroupDTO.getSize());
        Assertions.assertEquals("Жилищен блок", addHomesGroupDTO.getType());
    }

    @Test
    void testAddResidentDTO() {
        AddResidentDTO addResidentDTO = new AddResidentDTO();
        addResidentDTO.setFirstName("Tester");

        Assertions.assertEquals("Tester", addResidentDTO.getFirstName());
        Assertions.assertNull(addResidentDTO.getMiddleName());
        Assertions.assertNull(addResidentDTO.getLastName());
        Assertions.assertNull(addResidentDTO.getPhoneNumber());
        Assertions.assertNull(addResidentDTO.getEmail());

        addResidentDTO.setLastName("Later");
        addResidentDTO.setPhoneNumber("1234567890");

        Assertions.assertEquals("Later", addResidentDTO.getLastName());
        Assertions.assertEquals("1234567890", addResidentDTO.getPhoneNumber());
    }

    @Test
    void testFeePaymentDTO() {
        FeePaymentDTO feePaymentDTO = new FeePaymentDTO();
        feePaymentDTO.setId(999);
        feePaymentDTO.setTotal(123);
        feePaymentDTO.setTimesPaid(6);
        feePaymentDTO.setValue(9.99);

        Assertions.assertEquals(999, feePaymentDTO.getId());
        Assertions.assertEquals(123, feePaymentDTO.getTotal());
        Assertions.assertEquals(6, feePaymentDTO.getTimesPaid());
        Assertions.assertEquals(9.99, feePaymentDTO.getValue());
    }

    @Test
    void testHomePaymentsDTO() {
        HomePaymentsDTO homePaymentsDTO = new HomePaymentsDTO();
        homePaymentsDTO.setId(123);
        homePaymentsDTO.setName("Home");
        homePaymentsDTO.setValue(11.11);
        homePaymentsDTO.setTimes(77);
        homePaymentsDTO.setTotal(99.99);

        Assertions.assertEquals(123, homePaymentsDTO.getId());
        Assertions.assertEquals("Home", homePaymentsDTO.getName());
        Assertions.assertEquals(11.11, homePaymentsDTO.getValue());
        Assertions.assertEquals(77, homePaymentsDTO.getTimes());
        Assertions.assertEquals(99.99, homePaymentsDTO.getTotal());
    }

    @Test
    void testPasswordResetDTO() {
        PasswordResetDTO passwordResetDTO = new PasswordResetDTO();
        passwordResetDTO.setPassword("password");
        passwordResetDTO.setConfirmPassword("парола");

        Assertions.assertEquals("password", passwordResetDTO.getPassword());
        Assertions.assertEquals("парола", passwordResetDTO.getConfirmPassword());
    }

    @Test
    void testRegistrationDTO() {
        RegistrationDTO registrationDTO = new RegistrationDTO();
        registrationDTO.setName("Register");
        registrationDTO.setEmail("xaxa@xax.com");
        registrationDTO.setPassword("super-password");
        registrationDTO.setConfirmPassword("normal password");

        Assertions.assertEquals("Register", registrationDTO.getName());
        Assertions.assertEquals("xaxa@xax.com", registrationDTO.getEmail());
        Assertions.assertEquals("super-password", registrationDTO.getPassword());
        Assertions.assertEquals("normal password", registrationDTO.getConfirmPassword());
    }

    @Test
    void testYearDTO() {
        YearDTO yearDTO = new YearDTO();
        yearDTO.setMonths(null);
        yearDTO.setNumber(1988);

        Assertions.assertNull(yearDTO.getMonths());
        Assertions.assertEquals(1988, yearDTO.getNumber());
    }

    @Test
    void testNotifications() {
        Assertions.assertEquals("Успешна регистрация", Notifications.REGISTRATION_SUCCESSFULLY.getValue());
        Assertions.assertEquals("Успешна промяна", Notifications.UPDATED_SUCCESSFULLY.getValue());
        Assertions.assertEquals("Избраният касиер вече няма възложени групи", Notifications.CASHIER_NO_HOMES_GROUP.getValue());
        Assertions.assertEquals("Домът е добавен успешно", Notifications.HOME_ADDED_SUCCESSFULLY.getValue());
        Assertions.assertEquals("Живущ е добавен успешно", Notifications.RESIDENT_ADDED_SUCCESSFULLY.getValue());
        Assertions.assertEquals("Живущият е ИЗТРИТ успешно", Notifications.RESIDENT_DELETED_SUCCESSFULLY.getValue());
        Assertions.assertEquals("Невалиден адрес", Notifications.INVALID_EMAIL.getValue());
        Assertions.assertEquals("Паролата е сменена успешно", Notifications.PASSWORD_CHANGED_SUCCESSFULLY.getValue());
        Assertions.assertEquals("Успешно създаване", Notifications.CREATED_SUCCESSFULLY.getValue());
        Assertions.assertEquals("Успешно добавяне", Notifications.ADDED_SUCCESSFULLY.getValue());
    }

    @Test
    void testHomeManagerUserDetails() {

        HomeManagerUserDetails user = new HomeManagerUserDetails(99,
                "b_davidov@abv.bg",
                "Боби",
                "password",
                LocalDate.now(),
                null,
                null,
                null);

        Assertions.assertEquals("Боби", user.getName());
        Assertions.assertEquals("b_davidov@abv.bg", user.getEmail());
        Assertions.assertEquals("password", user.getPassword());
    }
//    @Test
//    void testSendAllEmailTemplates() {
//
//        String sendTo = "Bobogamen@students.softuni.bg";
//        HomeManagerUserDetails manager = new HomeManagerUserDetails(1, "b_davidov@abv.bg", "Борис Илиев",
//                "password", LocalDate.now(), null, null, null);
//
//        MockHttpServletRequest request = new MockHttpServletRequest();
//
//        emailService.sendRegistrationEmail(sendTo);
//        emailService.sendCashierRegistrationEmail(sendTo, manager, "appUrl.com");
//        emailService.sendRecoveryPasswordEmail(sendTo, request);
//    }
}
