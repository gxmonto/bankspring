package com.kodigo.bank.controller;

import com.kodigo.bank.entity.People;
import com.kodigo.bank.service.PeopleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
@SessionAttributes("customerId")
public class LoginController {

    @Autowired
    private PeopleService peopleService;

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, RedirectAttributes redirectAttributes) {
        People user = peopleService.findByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            // Successful login, redirect to deposit page
            return "redirect:/index?customerId=" + user.getId();
        } else {
            // Invalid credentials, redirect back to login page with error message
            redirectAttributes.addFlashAttribute("error", "Invalid username or password");
            return "login";
        }
    }


}