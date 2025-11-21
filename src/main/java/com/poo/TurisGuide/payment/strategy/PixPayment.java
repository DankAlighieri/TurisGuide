package com.poo.TurisGuide.payment.strategy;

import org.springframework.stereotype.Component;

@Component("PIX")
public class PixPayment implements PaymentStrategy {
    @Override
    public boolean processarPagamento(double valor) {
        System.out.println("Pagamento via PIX processado: " + valor);
        return true;
    }
}
