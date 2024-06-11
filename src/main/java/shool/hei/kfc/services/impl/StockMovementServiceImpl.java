package shool.hei.kfc.services.impl;

import hei.tantely.managementofrestaurantchain.dtos.requests.IntervalDate;
import hei.tantely.managementofrestaurantchain.dtos.responses.MovementStock;
import hei.tantely.managementofrestaurantchain.dtos.responses.TopXUsedIngredient;
import hei.tantely.managementofrestaurantchain.exceptions.BadRequestException;
import hei.tantely.managementofrestaurantchain.exceptions.DateFormatException;
import hei.tantely.managementofrestaurantchain.repositories.StockMovementRepository;
import hei.tantely.managementofrestaurantchain.services.StockMovementService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class StockMovementServiceImpl implements StockMovementService {
    private final StockMovementRepository stockMovementRepository;

    public List<MovementStock> findAllDetailMovements(Integer restaurantId, IntervalDate intervalDate) {
        var instants= this.converterStringToInstant(intervalDate);
        return stockMovementRepository.findAllDetailMovements(restaurantId, instants.get("startDate"), instants.get("endDate"));
    }

    @Override
    public List<TopXUsedIngredient> findTopXUsedIngredients(Integer restaurantId, IntervalDate intervalDate, Integer limit) {
        var instants= this.converterStringToInstant(intervalDate);
        return stockMovementRepository.findTopXUsedIngredients(restaurantId, instants.get("startDate"), instants.get("endDate"), limit);
    }


    public Map<String, Instant> converterStringToInstant(IntervalDate intervalDate) {
        var formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        LocalDateTime localDateStart;
        LocalDateTime localDateEnd;

        try {
            localDateStart = LocalDateTime.parse(intervalDate.startDate(), formatter);
            localDateEnd = LocalDateTime.parse(intervalDate.endDate(), formatter);
        } catch (DateTimeParseException e) {
            throw new DateFormatException("Invalid date format, expected format is dd/MM/yyyy HH:mm");
        }

        var instantStart = localDateStart.atZone(ZoneId.systemDefault()).toInstant();
        var instantEnd = localDateEnd.atZone(ZoneId.systemDefault()).toInstant();

        if (instantEnd.isBefore(instantStart)) {
            throw new BadRequestException("StartDate must be before endDate");
        }
        return Map.of("startDate", instantStart, "endDate", instantEnd);
    }
}
