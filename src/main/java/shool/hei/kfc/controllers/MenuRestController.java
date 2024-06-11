package shool.hei.kfc.controllers;

import hei.tantely.managementofrestaurantchain.dtos.requests.CreateMenuRequest;
import hei.tantely.managementofrestaurantchain.dtos.responses.MenuResponse;
import hei.tantely.managementofrestaurantchain.services.MenuService;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/menus")
public class MenuRestController {
    private final MenuService menuService;

    public MenuRestController(MenuService menuService) {
        this.menuService = menuService;
    }

    @PostMapping
    public MenuResponse createMenu(@RequestBody CreateMenuRequest menuRequest){
        return menuService.createMenu(menuRequest);
    }
}
