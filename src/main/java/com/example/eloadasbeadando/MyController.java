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



}
