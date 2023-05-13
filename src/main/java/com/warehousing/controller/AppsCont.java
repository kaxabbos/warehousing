package com.warehousing.controller;

import com.warehousing.controller.main.Attributes;
import com.warehousing.model.Cart;
import com.warehousing.model.Product;
import com.warehousing.model.Stat;
import com.warehousing.model.enums.CartStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/apps")
public class AppsCont extends Attributes {
    @GetMapping()
    String cart(Model model) {
        AddAttributesApps(model);
        return "apps";
    }

    @GetMapping("/conf/{id}")
    String Confirmation(Model model, @PathVariable Long id) {
        Cart cart = cartRepo.getReferenceById(id);

        if (cart.getQuantity() > cart.getProduct().getQuantity()) {
            AddAttributesCart(model);
            model.addAttribute("message", "Недостаточно продуктов для подтверждения!");
            return "apps";
        }
        Product product = cart.getProduct();

        Stat stat = cart.getProduct().getStat();

        stat.setQuantity(stat.getQuantity() + cart.getQuantity());
        stat.setPrice(stat.getPrice() + (cart.getQuantity() * product.getPrice()));

        product.setQuantity(product.getQuantity() - cart.getQuantity());
        cart.setStatus(CartStatus.APPROVED);
        productRepo.save(product);

        return "redirect:/apps";
    }

    @GetMapping("/unconf/{id}")
    String UnConfirmation(@PathVariable Long id) {
        Cart cart = cartRepo.getReferenceById(id);
        cart.setStatus(CartStatus.REFUSED);
        cartRepo.save(cart);
        return "redirect:/apps";
    }


}
