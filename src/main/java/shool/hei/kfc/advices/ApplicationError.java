package shool.hei.kfc.advices;


import java.time.LocalDate;

public record ApplicationError(
        String message,
        LocalDate errorDate,
        int status
) {
}
