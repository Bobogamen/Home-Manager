package com.home_manager.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.home_manager.model.dto.AddExpenseDTO;
import com.home_manager.model.dto.FeePaymentDTO;
import com.home_manager.model.dto.HomePaymentsDTO;
import com.home_manager.model.entities.Expense;
import com.home_manager.model.entities.HomesGroup;
import com.home_manager.model.entities.Month;
import com.home_manager.model.entities.MonthHomes;
import com.home_manager.model.enums.Notifications;
import com.home_manager.model.user.HomeManagerUserDetails;
import com.home_manager.service.*;
import com.home_manager.utility.MonthsUtility;
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

    @GetMapping("/homesGroup{homesGroupId}/print")
    public ModelAndView printMonth(@PathVariable long homesGroupId,
                                   @RequestParam int month,
                                   @RequestParam int year,
                                   @AuthenticationPrincipal HomeManagerUserDetails user) {

        if (isAuthorized(homesGroupId, user.getId())) {
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.setViewName("cashier/print_month");

            return monthModelAndView(homesGroupId, month, year, modelAndView);
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }

    private ModelAndView monthModelAndView(@PathVariable long homesGroupId,
                                           @RequestParam int month,
                                           @RequestParam int year,
                                           ModelAndView modelAndView) {
        HomesGroup homesGroup = this.homesGroupService.getHomesGroupById(homesGroupId);
        modelAndView.addObject("monthNumber", month);
        modelAndView.addObject("monthName", MonthsUtility.getMonthName(month));
        modelAndView.addObject("yearNumber", year);
        modelAndView.addObject("now", this.now);

        modelAndView.addObject("homesGroup", homesGroup);

        Month currnetMonth = homesGroup.getMonthByDate(month, year);

        if (currnetMonth != null) {
            modelAndView.addObject("currentMonth", this.monthService.getMonthById(currnetMonth.getId()));
            modelAndView.addObject("years", homesGroup.getYears());
        }

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

            this.year = year;
            this.month = year;

            if (month > 12) {
                month = 1;
                year++;
            }

            if (month < 1) {
                month = 12;
                year--;
            }

            return monthModelAndView(homesGroupId, month, year, modelAndView);
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping("/homesGroup{homesGroupId}/create-month")
    public String createMonth(@PathVariable long homesGroupId, @RequestParam int month, @RequestParam int year,
                              @AuthenticationPrincipal HomeManagerUserDetails user, RedirectAttributes redirectAttributes) {

        if (isAuthorized(homesGroupId, user.getId())) {
            HomesGroup homesGroup = this.homesGroupService.getHomesGroupById(homesGroupId);

            this.monthService.setHomesToMonth(this.monthService.createMonth(month, year, homesGroup), homesGroup);

            redirectAttributes.addAttribute("month", month);
            redirectAttributes.addAttribute("year", year);
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

    @GetMapping("/homesGroup{homesGroupId}/view-payment")
    @ResponseBody
    public Map<String, List<HomePaymentsDTO>> viewPaymentsByHomeId(@PathVariable long homesGroupId,
                                                                   @RequestParam long monthId, @RequestParam long monthHomeId,
                                                                   @AuthenticationPrincipal HomeManagerUserDetails user) {

        if (isAuthorized(homesGroupId, user.getId())) {

            List<HomePaymentsDTO> paymentsByHome =
                    this.paymentsService.viewPaymentsByHome(this.monthService.getMonthHomeOfMonthById(this.monthService.getMonthById(monthId), monthHomeId));

            return Collections.singletonMap("response", paymentsByHome);

        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping("/homesGroup{homesGroupId}/pay")
    public String pay(@PathVariable long homesGroupId, @RequestParam int month, @RequestParam int year, @RequestParam long monthHomeId,
                      @RequestParam(name = "data") String data, @RequestParam(name = "paidDate") String date, @AuthenticationPrincipal HomeManagerUserDetails user, RedirectAttributes redirectAttributes) throws Exception {

        if (isAuthorized(homesGroupId, user.getId())) {
            ObjectMapper objectMapper = new ObjectMapper();
            FeePaymentDTO[] feePaymentDTOS = objectMapper.readValue(data, FeePaymentDTO[].class);

            Month currentMonth = this.monthService.getMonthByNumberAndYearAndHomesGroupId(month, year, homesGroupId);
            MonthHomes monthHomes = currentMonth.getHomeById(monthHomeId);

            LocalDate paidDate = LocalDate.parse(date);

            this.monthService.setTotalPaymentForHome(currentMonth, monthHomes, this.paymentsService.makePayments(monthHomes, feePaymentDTOS), paidDate ,homesGroupId);

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
                             BindingResult bindingResult, @AuthenticationPrincipal HomeManagerUserDetails user, RedirectAttributes redirectAttributes) {

        if (isAuthorized(homesGroupId, user.getId())) {

            String redirectURL = "redirect:" + String.format("/cashier/homesGroup%d?month=%d&year=%d", homesGroupId, month, year);

            if (bindingResult.hasErrors()) {
                redirectAttributes.addFlashAttribute("addExpenseDTO", addExpenseDTO);
                redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.addExpenseDTO", bindingResult);

                return redirectURL;
            }

            Month currentMonth = this.monthService.getMonthByNumberAndYearAndHomesGroupId(month, year, homesGroupId);
            currentMonth.setExpenses(this.expenseService.addExpenseToMonth(currentMonth, addExpenseDTO));

            this.monthService.calculateTotalExpense(currentMonth, homesGroupId);

            redirectAttributes.addFlashAttribute("success", Notifications.ADDED_SUCCESSFULLY.getValue());
            return redirectURL;
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/homesGroup{homesGroupId}/edit-expense{expenseId}")
    public ModelAndView editExpense(@PathVariable long homesGroupId, @PathVariable long expenseId, @AuthenticationPrincipal HomeManagerUserDetails user) {

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
            this.monthService.calculateTotalExpense(expense.getMonth(), homesGroupId);

            redirectAttributes.addFlashAttribute("success", Notifications.UPDATED_SUCCESSFULLY.getValue());

            return "redirect:" + String.format("/cashier/homesGroup%d?month=%d&year=%d", homesGroupId, month, year);
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/homesGroup{homesGroupId}/years")
    public ModelAndView getYears(@PathVariable long homesGroupId, @AuthenticationPrincipal HomeManagerUserDetails user) {

        if (isAuthorized(homesGroupId, user.getId())) {

            ModelAndView modelAndView = new ModelAndView();
            modelAndView.setViewName("cashier/years");

            HomesGroup homesGroup = this.homesGroupService.getHomesGroupById(homesGroupId);
            modelAndView.addObject("homesGroup", homesGroup);
            modelAndView.addObject("years", this.monthService.years(homesGroup.getYears(), homesGroupId));
            modelAndView.addObject("now", this.now);

            return modelAndView;
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/homesGroup{homesGroupId}/year{yearNumber}")
    public ModelAndView showYear(@PathVariable int homesGroupId, @PathVariable int yearNumber, @AuthenticationPrincipal HomeManagerUserDetails user) {

        if (isAuthorized(homesGroupId, user.getId())) {

            ModelAndView modelAndView = new ModelAndView();
            modelAndView.setViewName("cashier/showYear");
            modelAndView.addObject("homesGroup", this.homesGroupService.getHomesGroupById(homesGroupId));
            modelAndView.addObject("year", this.monthService.getYear(yearNumber, homesGroupId));
            modelAndView.addObject("now", this.now);



            return modelAndView;
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }
}
