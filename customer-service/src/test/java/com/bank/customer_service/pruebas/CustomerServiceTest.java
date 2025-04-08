package com.bank.customer_service.pruebas;

import com.bank.customer_service.exception.BadRequestException;
import com.bank.customer_service.model.Customer;
import com.bank.customer_service.model.enums.CustomerType;
import com.bank.customer_service.repository.CustomerRepository;
import com.bank.customer_service.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    private Customer validCustomer;
    private Customer invalidCustomer;

    @BeforeEach
    void setUp() {
        validCustomer = Customer.builder()
                .name("Juan Pérez")
                .documentNumber("123456789")
                .customerType(CustomerType.VIP)
                .hasCreditCard(true)  // Tarjeta de crédito válida
                .build();

        invalidCustomer = Customer.builder()
                .name("Ana Gómez")
                .documentNumber("987654321")
                .customerType(CustomerType.VIP)
                .hasCreditCard(false)  // Sin tarjeta de crédito
                .build();
    }

    @Test
    public void shouldCreateCustomerWhenValid() {
        // Simula que el cliente tiene el saldo promedio requerido
        Customer vipCustomer = Customer.builder()
                .customerType(CustomerType.VIP)
                .documentNumber("12345")
                .name("Juan Pérez")
                .hasCreditCard(true)
                .build();
        vipCustomer.setRequiredAverageBalance(new BigDecimal("1000.00"));  // Simula el saldo promedio

        // Mock del método save() para devolver un Mono<Customer>
        when(customerRepository.save(any(Customer.class))).thenReturn(Mono.just(vipCustomer));

        // Llama al método save() y verifica que no lanza excepciones
        Customer savedCustomer = customerService.save(vipCustomer).block();

        // Verifica que el cliente guardado no sea nulo y tenga los datos esperados
        assertNotNull(savedCustomer);
        assertEquals("12345", savedCustomer.getDocumentNumber());
        assertEquals("Juan Pérez", savedCustomer.getName());
        assertEquals(CustomerType.VIP, savedCustomer.getCustomerType());
        assertTrue(savedCustomer.isHasCreditCard());

        // Verifica que el método save() del repositorio fue llamado
        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    void shouldThrowBadRequestWhenCustomerHasNoCreditCard() {
        // Verifica que se lanza la excepción BadRequestException al intentar guardar un cliente inválido
        assertThrows(BadRequestException.class, () -> customerService.save(invalidCustomer).block());
    }
}
