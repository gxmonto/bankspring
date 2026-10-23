package com.kodigo.bank.controller;

import com.kodigo.bank.entity.Customer;
import com.kodigo.bank.entity.People;
import com.kodigo.bank.service.CustomerService;
import com.kodigo.bank.service.PeopleService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class LoginController {
    public static final String CUSTOMER_ID = "customerId";
    public static final String USERNAME = "username";

    @Autowired
    private PeopleService peopleService;
    @Autowired
    private CustomerService customerService;

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, HttpSession session, Model model) {
        People user = peopleService.findByUsername(username);
        if (user == null || !password.equals(user.getPassword())) {
            model.addAttribute("error", "Invalid username or password");
            return "login";
        }
        Optional<Customer> customer = customerService.findByPeopleId(user.getId());
        if (customer.isEmpty()) {
            model.addAttribute("error", "This user has no bank account");
            return "login";
        }
        // Spring guarda la sesion en el servidor y le manda al navegador la cookie JSESSIONID;
        // no hace falta manejar tokens ni cookies a mano.
        session.setAttribute(CUSTOMER_ID, customer.get().getCustomerId());
        session.setAttribute(USERNAME, user.getUsername());
        return "redirect:/index";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
