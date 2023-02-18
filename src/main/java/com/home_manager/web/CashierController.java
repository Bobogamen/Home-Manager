package com.home_manager.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.home_manager.model.dto.AddExpenseDTO;
import com.home_manager.model.dto.FeePaymentDTO;
import com.home_manager.model.dto.HomePaymentsDTO;
import com.home_manager.model.entities.Expense;
import com.home_manager.model.entities.HomesGroup;
import com.home_manager.model.entities.Month;
import com.home_manager.model.enums.Months;
import com.home_manager.model.enums.Notifications;
import com.home_manager.model.user.HomeManagerUserDetails;
import com.home_manager.service.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/cashier")
public class CashierController {

    private final UserService userService;
    private final HomesGroupService homesGroupService;
    private final MonthService monthService;
    private final PaymentsService paymentsService;
    private final ExpenseService expenseService;
    private final LocalDate now;
    private int month;
    private int year;

    public CashierController(UserService userService, HomesGroupService homesGroupService, MonthService monthService, PaymentsService paymentsService, ExpenseService expenseService) {
        this.userService = userService;
        this.homesGroupService = homesGroupService;
        this.monthService = monthService;
        this.paymentsService = paymentsService;
        this.expenseService = expenseService;
        this.now = LocalDate.now();
        this.month = this.now.getMonthValue();
        this.year = this.now.getYear();
    }

    private boolean isAuthorized(long homesGroupId, long userId) {
        return this.userService.isOwner(homesGroupId, userId);
    }

    @GetMapping("")
    public ModelAndView cashier(@AuthenticationPrincipal HomeManagerUserDetails user) {

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("cashier");

        modelAndView.addObject("user", this.userService.getUserById(user.getId()));
        modelAndView.addObject("now", this.now);

        return modelAndView;
    }

    @GetMapping("/homesGroup{homesGroupId}")
    public ModelAndView cashierHomesGroup(@PathVariable long homesGroupId,
                                          @RequestParam int month,
                                          @RequestParam int year,
                                          @AuthenticationPrincipal HomeManagerUserDetails user) {

        if (isAuthorized(homesGroupId, user.getId())) {
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.setViewName("cashier/cashier_homes_group");

            this.month = month;
            this.year = year;

            if (month > 12) {
                month = 1;
                year++;
            }

            if (month < 1) {
                month = 12;
                year--;
            }

            modelAndView.addObject("monthNumber", month);
            modelAndView.addObject("monthName", getMonthName(month));
            modelAndView.addObject("yearNumber", year);
            modelAndView.addObject("now", this.now);

            HomesGroup homesGroup = this.homesGroupService.getHomesGroupById(homesGroupId);
            modelAndView.addObject("homesGroup", homesGroup);

            Month currnetMonth = homesGroup.getMonthByDate(month, year);

            if (currnetMonth != null) {
                modelAndView.addObject("currentMonth", this.monthService.getMonthById(currnetMonth.getId()));
                modelAndView.addObject("years", this.homesGroupService.getYears(homesGroup));
            }

            return modelAndView;
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }

    private String getMonthName(int month) {
        return switch (month) {
            case 1 -> Months.JANUARY.getValue();
            case 2 -> Months.FEBRUARY.getValue();
            case 3 -> Months.MARCH.getValue();
            case 4 -> Months.APRIL.getValue();
            case 5 -> Months.MAY.getValue();
            case 6 -> Months.JUNE.getValue();
            case 7 -> Months.JULY.getValue();
            case 8 -> Months.AUGUST.getValue();
            case 9 -> Months.SEPTEMBER.getValue();
            case 10 -> Months.OCTOBER.getValue();
            case 11 -> Months.NOVEMBER.getValue();
            case 12 -> Months.DECEMBER.getValue();
            default -> "Wrong month number!";
        };
    }

    @PostMapping("/homesGroup{homesGroupId}/create-month")
    public String createMonth(@PathVariable long homesGroupId, @RequestParam int month, @RequestParam int year,
                          @AuthenticationPrincipal HomeManagerUserDetails user, RedirectAttributes redirectAttributes) {

        if (isAuthorized(homesGroupId, user.getId())) {
            HomesGroup homesGroup = this.homesGroupService.getHomesGroupById(homesGroupId);

            Month updatedMonth = this.monthService.setHomesToMonth(this.monthService.createMonth(month, year, homesGroup), homesGroup);
            this.paymentsService.setPayments(updatedMonth);

            redirectAttributes.addAttribute("month", month);
            redirectAttributes.addAttribute("year",year);
            redirectAttributes.addFlashAttribute("success", Notifications.CREATED_SUCCESSFULLY.getValue());
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        return "redirect:/cashier/homesGroup{homesGroupId}";
    }

    @GetMapping("/homesGroup{homesGroupId}/get-payment")
    @ResponseBody
    public Map<String, List<HomePaymentsDTO>> getPaymentsByHomeId(@PathVariable long homesGroupId,
                                                                  @RequestParam long monthId, @RequestParam long monthHomeId,
                                                                  @AuthenticationPrincipal HomeManagerUserDetails user) {

        if (isAuthorized(homesGroupId, user.getId())) {

            List<HomePaymentsDTO> paymentsByHome =
                    this.paymentsService.getPaymentsByHome(this.monthService.getMonthHomeOfMonthById(this.monthService.getMonthById(monthId), monthHomeId));

            return Collections.singletonMap("response", paymentsByHome);

        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping("/homesGroup{homesGroupId}/pay")
    public String pay(@PathVariable long homesGroupId, @RequestParam int month, @RequestParam int year, @RequestParam long monthHomeId,
                      @RequestParam(name = "data") String data, @AuthenticationPrincipal HomeManagerUserDetails user, RedirectAttributes redirectAttributes) throws Exception {

        if (isAuthorized(homesGroupId, user.getId())) {
            ObjectMapper objectMapper = new ObjectMapper();
            FeePaymentDTO[] feePaymentDTOS = objectMapper.readValue(data, FeePaymentDTO[].class);

            this.monthService.setTotalPaymentForHome(month, year, monthHomeId, this.paymentsService.makePayments(monthHomeId, feePaymentDTOS));

            redirectAttributes.addFlashAttribute("success", Notifications.PAYMENT_SUCCESSFULLY.getValue());
            return "redirect:" + String.format("/cashier/homesGroup%d?month=%d&year=%d", homesGroupId, month, year);
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }

    @ModelAttribute("addExpenseDTO")
    public AddExpenseDTO addExpenseDTO() {
        return new AddExpenseDTO();
    }

    @PostMapping("/homesGroup{homesGroupId}/add-expense")
    public String addExpense(@PathVariable long homesGroupId, @RequestParam int month, @RequestParam int year, @Valid AddExpenseDTO addExpenseDTO,
                             BindingResult bindingResult, @AuthenticationPrincipal HomeManagerUserDetails user, RedirectAttributes redirectAttributes){

        if (isAuthorized(homesGroupId, user.getId())) {

            String redirectURL = "redirect:" + String.format("/cashier/homesGroup%d?month=%d&year=%d", homesGroupId, month, year);

            if (bindingResult.hasErrors()) {
                redirectAttributes.addFlashAttribute("addExpenseDTO", addExpenseDTO);
                redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.addExpenseDTO", bindingResult);

                return redirectURL;
            }

            Month currentMonth = this.monthService.getMonthById(month);
            this.expenseService.addExpenseToMonth(currentMonth, addExpenseDTO);
            this.monthService.calculateTotalExpense(currentMonth);

            redirectAttributes.addFlashAttribute("success", Notifications.ADDED_SUCCESSFULLY.getValue());
            return redirectURL;
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/homesGroup{homesGroupId}/edit-expense{expenseId}")
    public ModelAndView editExpense(@PathVariable long homesGroupId, @PathVariable long expenseId,@AuthenticationPrincipal HomeManagerUserDetails user) {

        if (isAuthorized(homesGroupId, user.getId())) {
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.setViewName("cashier/edit-expense");
            modelAndView.addObject("expense", this.expenseService.getExpenseById(expenseId));

            return modelAndView;
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping("/homesGroup{homesGroupId}/edit-expense{expenseId}")
    public String editExpense(@PathVariable long homesGroupId, @PathVariable long expenseId,
                              @RequestParam String name, @RequestParam double value, @RequestParam String documentNumber,
                              @AuthenticationPrincipal HomeManagerUserDetails user, RedirectAttributes redirectAttributes) {

        if (isAuthorized(homesGroupId, user.getId())) {

            Expense expense = this.expenseService.editExpense(name, value, documentNumber, expenseId);
            this.monthService.calculateTotalExpense(expense.getMonth());

            redirectAttributes.addFlashAttribute("success", Notifications.UPDATED_SUCCESSFULLY.getValue());
            return "redirect:" + String.format("/cashier/homesGroup%d?month=%d&year=%d", homesGroupId, this.month, this.year);

        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/homesGroup{homesGroupId}/year{yearNumber}")
    public ModelAndView getYear(@PathVariable long homesGroupId, @PathVariable int yearNumber) {

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("cashier/year");

        modelAndView.addObject("homesGroup", this.homesGroupService.getHomesGroupById(homesGroupId));
        modelAndView.addObject("year", this.monthService.getYear(yearNumber));

        return modelAndView;
    }
}
