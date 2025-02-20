package com.ipn.HolaSpring.Controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.stereotype.Controller;

@Controller  
public class HolaSpringController {

    @GetMapping("/hola")  
    public String decirHola() {
        return "Hola";
    }
}