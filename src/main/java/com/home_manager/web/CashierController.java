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

import javax.servlet.http.HttpServletRequest;
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
    private final HttpServletRequest request;

    public CashierController(UserService userService, HomesGroupService homesGroupService, MonthService monthService, PaymentsService paymentsService, ExpenseService expenseService, HttpServletRequest request) {
        this.userService = userService;
        this.homesGroupService = homesGroupService;
        this.monthService = monthService;
        this.paymentsService = paymentsService;
        this.expenseService = expenseService;
        this.request = request;
        this.now = LocalDate.now();
    }

    private boolean isAuthorized(long homesGroupId, long userId) {
        return this.userService.isOwner(homesGroupId, userId);
    }

    private boolean futureCheck(int month, int year) {
        return month > this.now.getMonthValue() || year > this.now.getYear();
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

    @GetMapping("/homesGroup{homesGroupId}")
    public ModelAndView cashierHomesGroup(@PathVariable long homesGroupId, @RequestParam int month, @RequestParam int year,
                                          @AuthenticationPrincipal HomeManagerUserDetails user) {

        if (futureCheck(month, year) || isAuthorized(homesGroupId, user.getId())) {
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.setViewName("cashier/cashier_homes_group");

            return monthModelAndView(homesGroupId, month, year, modelAndView);

        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }

    private ModelAndView monthModelAndView(long homesGroupId, int month, int year, ModelAndView modelAndView) {

        HomesGroup homesGroup = this.homesGroupService.getHomesGroupById(homesGroupId);

        if (year == homesGroup.getStartPeriod().getYear()) {
            int startPeriodMonth = homesGroup.getStartPeriod().getMonthValue();
            month = Math.max(month, startPeriodMonth);

        } else if (year == now.getYear()) {
            month = now.getMonthValue();
        }

        modelAndView.addObject("monthNumber", month);
        modelAndView.addObject("monthName", MonthsUtility.getMonthName(month));
        modelAndView.addObject("yearNumber", year);
        modelAndView.addObject("monthsList", this.monthService.getMonthsList(year, this.now.getYear(), homesGroup.getStartPeriod()));
        modelAndView.addObject("now", this.now);
        modelAndView.addObject("homesGroup", homesGroup);

        Month currnetMonth = homesGroup.getMonthByDate(month, year);

        if (currnetMonth != null) {

            currnetMonth = this.monthService.getMonthById(currnetMonth.getId());

            modelAndView.addObject("currentMonth", currnetMonth);
            modelAndView.addObject("previousMonth", currnetMonth.getPreviousMonth());
            modelAndView.addObject("years", homesGroup.getYears());
        }

        return modelAndView;
    }

    @PostMapping("/homesGroup{homesGroupId}/create-month")
    public String createMonth(@PathVariable long homesGroupId, @RequestParam int month, @RequestParam int year,
                              @AuthenticationPrincipal HomeManagerUserDetails user, RedirectAttributes redirectAttributes) {

        if (futureCheck(month, year) || isAuthorized(homesGroupId, user.getId())) {
            HomesGroup homesGroup = this.homesGroupService.getHomesGroupById(homesGroupId);

            this.monthService.setHomesToMonth(this.monthService.createMonth(month, year, homesGroup, this.monthService.getPreviousMonth(month, year, homesGroup)), homesGroup);

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

            this.monthService.setTotalPaymentForHome(currentMonth, monthHomes, this.paymentsService.makePayments(monthHomes, feePaymentDTOS), paidDate);

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

            this.monthService.calculateTotalExpense(currentMonth);

            redirectAttributes.addFlashAttribute("success", Notifications.ADDED_SUCCESSFULLY.getValue());
            return redirectURL;
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/homesGroup{homesGroupId}/edit-expense{expenseId}")
    public ModelAndView editExpense(@PathVariable long homesGroupId, @PathVariable long expenseId, @AuthenticationPrincipal HomeManagerUserDetails user) {

        if (this.request.isUserInRole("ADMIN") || (isAuthorized(homesGroupId, user.getId())) && this.request.isUserInRole("MANAGER")) {
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

            return "redirect:" + String.format("/cashier/homesGroup%d?month=%d&year=%d", homesGroupId, expense.getMonth().getNumber(), expense.getMonth().getYear());
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping("/homesGroup{homesGroupId}/completeMonth")
    public String completeMonth(@PathVariable long homesGroupId, @RequestParam(name = "month") int month, @RequestParam(name = "year") int year,
                                @AuthenticationPrincipal HomeManagerUserDetails user, RedirectAttributes redirectAttributes) {

        String returnString = "redirect:" + String.format("/cashier/homesGroup%d?month=%d&year=%d", homesGroupId, month, year);

        if (isAuthorized(homesGroupId, user.getId())) {

            Month monthToComplete = this.monthService.getMonthByNumberAndYearAndHomesGroupId(month, year, homesGroupId);

            if (this.monthService.isCompleted(monthToComplete)) {
                this.monthService.completeMonth(monthToComplete);
            } else {
                redirectAttributes.addFlashAttribute("fail", Notifications.MONTH_COMPLETE_NOT_ALL_PAYMENTS.getValue());
                return returnString;

            }

        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        redirectAttributes.addFlashAttribute("success", Notifications.MONTH_COMPLETION.getValue());
        return returnString;
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
