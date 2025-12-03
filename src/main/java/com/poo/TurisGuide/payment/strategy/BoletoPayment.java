package com.poo.TurisGuide.payment.strategy;

import org.springframework.stereotype.Component;

@Component("BOLETO")
public class BoletoPayment implements PaymentStrategy {

    @Override
    public boolean processarPagamento(double valor) {
        System.out.println("Pagamento com boleto processado");
        return true;
    }
    
}
