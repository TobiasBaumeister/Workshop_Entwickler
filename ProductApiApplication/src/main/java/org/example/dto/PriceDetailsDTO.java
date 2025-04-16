package org.example.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import org.example.model.DiscountInfo;


@Schema(description = "Preisinformationen inklusive angewendetem Rabatt und Steuern")
public record PriceDetailsDTO(

    @Schema(description = "ID des Produkts", example = "42")
    @NotNull
    Long productId,

    @Schema(description = "Basispreis des Produkts vor Rabatt", example = "1000.00")
    @NotNull
    @DecimalMin(value = "0.0", message = "Der Basispreis darf nicht negativ sein")
    BigDecimal basePrice,

    @Schema(description = "Informationen zum angewendeten Rabatt")
    DiscountInfo appliedDiscount,

    @Schema(description = "Höhe des abgezogenen Rabatts", example = "100.00")
    @NotNull
    @DecimalMin(value = "0.0")
    BigDecimal discountAmount,

    @Schema(description = "Preis nach Anwendung des Rabatts", example = "900.00")
    @NotNull
    @DecimalMin(value = "0.0")
    BigDecimal priceAfterDiscount,

    @Schema(description = "Angewendeter Steuersatz", example = "0.19")
    @PositiveOrZero
    double taxRateApplied,

    @Schema(description = "Berechneter Steuerbetrag", example = "171.00")
    @NotNull
    @DecimalMin(value = "0.0")
    BigDecimal taxAmount,

    @Schema(description = "Endpreis inkl. Steuern", example = "1071.00")
    @NotNull
    @DecimalMin(value = "0.0")
    BigDecimal finalPrice

) {

}
