package com.warehousing.controller;

import com.warehousing.controller.main.Attributes;
import com.warehousing.model.Product;
import com.warehousing.model.Stat;
import com.warehousing.model.Warehouse;
import com.warehousing.model.enums.Category;
import com.warehousing.model.enums.ProductStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

@Controller
@RequestMapping("/product")
public class ProductCont extends Attributes {
    @GetMapping("/all")
    String products(Model model) {
        AddAttributesProducts(model);
        return "products";
    }

    @GetMapping("/category/{category}")
    String productsCategory(Model model, @PathVariable Category category) {
        AddAttributesProductsCategory(model, category);
        return "products";
    }

    @GetMapping("/warehouse/{warehouse}")
    String productsWarehouse(Model model, @PathVariable Long warehouse) {
        AddAttributesProductsWarehouse(model, warehouse);
        return "products";
    }

    @PostMapping("/search")
    String productsSearch(Model model, @RequestParam Category category, @RequestParam Long warehouseId, @RequestParam String search, @RequestParam String desk) {
        AddAttributesProductsSearch(model, category, search, desk, warehouseId);
        return "products";
    }

    @GetMapping("/add")
    String productAdd(Model model) {
        AddAttributesProductAdd(model);
        return "productAdd";
    }

    @PostMapping("/add")
    String productAddNew(Model model, @RequestParam MultipartFile img, @RequestParam String name, @RequestParam String cell, @RequestParam String provider, @RequestParam Category category, @RequestParam int quantity, @RequestParam int price, @RequestParam Long warehouse, @RequestParam int weight, @RequestParam int keep, @RequestParam String description) {
        Warehouse wh = warehouseRepo.getReferenceById(warehouse);

        if (wh.getCurrent() + quantity > wh.getMax()) {
            model.addAttribute("message", "Недостаточно места на складе!");
            AddAttributesProductAdd(model);
            return "productAdd";
        }


        Product product = productRepo.saveAndFlush(new Product(name, category, price, quantity, weight, keep, description, provider, cell));

        if (img != null && !Objects.requireNonNull(img.getOriginalFilename()).isEmpty()) {
            String uuidFile = UUID.randomUUID().toString();
            boolean createDir = true;
            String res = "";
            try {
                File uploadDir = new File(uploadImg);
                if (!uploadDir.exists()) createDir = uploadDir.mkdir();
                if (createDir) {
                    res = uuidFile + "_" + img.getOriginalFilename();
                    img.transferTo(new File(uploadImg + "/" + res));
                }
            } catch (IOException e) {
                model.addAttribute("message", "Не удалось загрузить изображение");
                AddAttributesProductAdd(model);
                return "productAdd";
            }

            product.setImg(res);
            productRepo.save(product);
        }

        wh.addProduct(product);
        wh.setCurrent(wh.getCurrent() + quantity);
        warehouseRepo.save(wh);
        Stat stat = statRepo.saveAndFlush(new Stat(product));
        product.setStat(stat);
        productRepo.save(product);
        return "redirect:/product/all";
    }

    @GetMapping("/edit/{id}")
    String productEdit(Model model, @PathVariable Long id) {
        AddAttributesProductEdit(model, id);
        return "productEdit";
    }

    @GetMapping("/move/{id}")
    String productMove(Model model, @PathVariable Long id) {
        AddAttributesProductMove(model, id);
        return "productMove";
    }

    @PostMapping("/edit/{id}")
    String productEditOld(@PathVariable Long id, Model model, @RequestParam MultipartFile img, @RequestParam String name, @RequestParam String cell, @RequestParam String provider, @RequestParam Category category, @RequestParam int quantity, @RequestParam int price, @RequestParam Long warehouse, @RequestParam int weight, @RequestParam int keep, @RequestParam String description) {
        Product product = productRepo.getReferenceById(id);
        Warehouse wh = warehouseRepo.getReferenceById(warehouse);

        if ((wh.getCurrent() - product.getQuantity()) + quantity > wh.getMax()) {
            model.addAttribute("message", "Недостаточно места на складе!");
            AddAttributesProductEdit(model, id);
            return "productEdit";
        }


        product.setName(name);
        product.setWarehouse(wh);
        product.setProvider(provider);
        product.setCategory(category);
        product.setPrice(price);
        product.setCell(cell);
        product.setWeight(weight);
        product.setKeep(keep);
        product.setDescription(description);

        wh.setCurrent(wh.getCurrent() - product.getQuantity());
        wh.setCurrent(wh.getCurrent() + quantity);
        product.setQuantity(quantity);

        if (img != null && !Objects.requireNonNull(img.getOriginalFilename()).isEmpty()) {
            String uuidFile = UUID.randomUUID().toString();
            boolean createDir = true;
            String res = "";
            try {
                File uploadDir = new File(uploadImg);
                if (!uploadDir.exists()) createDir = uploadDir.mkdir();
                if (createDir) {
                    res = uuidFile + "_" + img.getOriginalFilename();
                    img.transferTo(new File(uploadImg + "/" + res));
                }
            } catch (IOException e) {
                model.addAttribute("message", "Не удалось загрузить изображение");
                AddAttributesProductEdit(model, id);
                return "productEdit";
            }

            product.setImg(res);
            productRepo.save(product);
        }

        productRepo.save(product);

        return "redirect:/product/all";
    }

    @PostMapping("/move/{id}")
    String productMove(Model model, @PathVariable Long id, @RequestParam Long warehouse, @RequestParam String cell) {
        Product product = productRepo.getReferenceById(id);
        Warehouse wh = warehouseRepo.getReferenceById(warehouse);

        if (wh.getCurrent() + product.getQuantity() > wh.getMax()) {
            model.addAttribute("message", "Недостаточно места на складе!");
            AddAttributesProductMove(model, id);
            return "productMove";
        }

        product.setWarehouse(warehouseRepo.getReferenceById(warehouse));
        product.setStatus(ProductStatus.WAITING);
        product.setCell(cell);
        productRepo.save(product);
        return "redirect:/product/all";
    }

    @GetMapping("/delete/{id}")
    String productDelete(@PathVariable Long id) {
        productRepo.deleteById(id);
        return "redirect:/product/all";
    }

}
