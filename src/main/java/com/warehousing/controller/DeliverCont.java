package com.warehousing.controller;

import com.warehousing.controller.main.Attributes;
import com.warehousing.model.Cart;
import com.warehousing.model.Product;
import com.warehousing.model.Stat;
import com.warehousing.model.enums.CartStatus;
import com.warehousing.model.enums.ProductStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/deliver")
public class DeliverCont extends Attributes {

    @GetMapping()
    String deliver(Model model) {
        AddAttributesDeliver(model);
        return "deliver";
    }

    @GetMapping("/delivered/{id}")
    String Confirmation(Model model, @PathVariable Long id) {
        Product product = productRepo.getReferenceById(id);
        product.setStatus(ProductStatus.DELIVERED);
        productRepo.save(product);
        return "redirect:/deliver";
    }

}
