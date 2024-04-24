package org.example.virtualtapcash.controllers;

import org.example.virtualtapcash.services.TapcashCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/v1/card")
public class CardController {

    @Autowired
    TapcashCardService tapcashCardService;

    @PostMapping("/")
}
