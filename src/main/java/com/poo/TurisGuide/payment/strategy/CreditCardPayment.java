package com.poo.TurisGuide.payment.strategy;

import org.springframework.stereotype.Component;

@Component("CREDITO")
public class CreditCardPayment implements PaymentStrategy {
    @Override
    public boolean processarPagamento(double valor) {
        System.out.println("Pagamento via Cartão de Crédito processado: " + valor);
        return true;
    }
}
