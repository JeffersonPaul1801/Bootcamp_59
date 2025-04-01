package com.bank.accounts_service.model;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.math.BigDecimal;


@Document(collection = "accounts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BankAccount {
    @Id
    private String id;

    @NotBlank(message = "Customer ID is required")
    private String customerId; // Relación con Customer

    @NotBlank(message = "Account type is required")
    @Pattern(regexp = "SAVINGS|CHECKING|FIXED_TERM", message = "Invalid account type. Allowed: SAVINGS, CHECKING, FIXED_TERM")
    private String accountType; // SAVINGS, CHECKING, FIXED_TERM

    @NotNull(message = "Balance is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Balance must be greater than zero")
    private BigDecimal balance;

    private int maxTransactions; // Para cuentas de ahorro y plazo fijo
    private boolean hasMaintenanceFee; // Para cuenta corriente
}

