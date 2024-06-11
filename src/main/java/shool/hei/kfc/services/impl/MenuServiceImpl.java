package shool.hei.kfc.services.impl;

import hei.tantely.managementofrestaurantchain.dtos.requests.CreateMenuRequest;
import hei.tantely.managementofrestaurantchain.dtos.responses.MenuResponse;
import hei.tantely.managementofrestaurantchain.entities.Menu;
import hei.tantely.managementofrestaurantchain.repositories.MenuRepository;
import hei.tantely.managementofrestaurantchain.services.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService {
    private final MenuRepository menuRepository;

    @Override
    public MenuResponse createMenu(CreateMenuRequest menuRequest) {
        var toCreate = Menu.builder()
                .name(menuRequest.name())
                .currentPrice(menuRequest.currentPrice())
                .build();
        var toCreated = menuRepository.save(toCreate);
        return new MenuResponse(toCreated.getId(), toCreated.getName(), toCreated.getCurrentPrice());
    }
}
