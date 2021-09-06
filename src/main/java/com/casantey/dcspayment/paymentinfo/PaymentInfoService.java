package com.casantey.dcspayment.paymentinfo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service @RequiredArgsConstructor @Slf4j
public class PaymentInfoService {

    private final  PaymentInfoRepository repo;

    PaymentInfo findByCheckoutId(String checkoutId){
        return repo.findByCheckoutId(checkoutId);
    }

    PaymentInfo findByEgcrNo(String egcrNo){
        return repo.findByEgcrNo(egcrNo);
    }

    PaymentInfo findByBillNo(String billNo){
        return repo.findByBillNo(billNo);
    }

    PaymentInfo findByClientReference(String clientReference){
        return repo.findByClientReference(clientReference);
    }

    PaymentInfo savePaymentInfo(PaymentInfo info){
        log.info("Saving into payment table: {}",info);
        return repo.save(info);
    }


}
