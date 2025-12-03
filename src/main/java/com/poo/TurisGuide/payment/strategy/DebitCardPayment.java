package com.poo.TurisGuide.payment.strategy;

import org.springframework.stereotype.Component;

@Component("DEBITO")
public class DebitCardPayment implements PaymentStrategy{
    @Override
    public boolean processarPagamento(double valor) {
        System.out.println("Pagamento com cartão de débito processado");
        return true;
    }
}
