package com.warehousing.controller;

import com.warehousing.controller.main.Attributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/archive")
public class ArchiveCont extends Attributes {
    @GetMapping()
    String cart(Model model) {
        AddAttributesArchive(model);
        return "archive";
    }
}
