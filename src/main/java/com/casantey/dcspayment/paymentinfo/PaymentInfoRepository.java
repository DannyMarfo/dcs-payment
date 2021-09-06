package com.casantey.dcspayment.paymentinfo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentInfoRepository extends JpaRepository<PaymentInfo, Long> {

    PaymentInfo findByCheckoutId(String checkoutId);
    PaymentInfo findByEgcrNo(String egcrNo);
    PaymentInfo findByBillNo(String billNo);
    PaymentInfo findByClientReference(String clientReference);
}
