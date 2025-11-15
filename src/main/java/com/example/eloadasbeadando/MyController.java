package com.example.eloadasbeadando;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.*;
import java.util.*;

@Controller
public class MyController {
    @GetMapping("/soapform")
    public String showSoapForm(Model model) {
        model.addAttribute("param", new MessagePrice());
        return "soapform"; //
    }

    @GetMapping("/forexacc")
    public String showForexAcc() {
        return "forexacc";
    }

    @GetMapping("/forexact_form")
    public String showForexActForm(Model model) {
        model.addAttribute("param", new MessageActPrice());
        return "forexact_form";
    }

    @GetMapping("/forexhist_form")
    public String showForexHistForm(Model model) {
        model.addAttribute("param", new MessageHistPrice());
        return "forexhist_form";
    }

    @GetMapping("/forexopen_form")
    public String showForexOpenForm(Model model) {
        model.addAttribute("param", new MessageOpenPosition());
        return "forexopen_form";
    }

    @GetMapping("forexclose_form")
    public String showForexCloseForm(Model model) {
    model.addAttribute("param", new MessageClosePosition());
        return "forexclose_form";
    }
}
