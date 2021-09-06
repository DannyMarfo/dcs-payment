package com.casantey.dcspayment.paymentinfo;

import com.casantey.dcspayment.util.Constants;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@ToString
public class PaymentInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String dateCreated;
    private String paymentSource;
    private String name;
    private String phone;
    private String paymentMode;
    private String billNo;
    private double billAmount;
    private double serviceCharge;
    private double totalAmount;
    private String billType;
    private String email;
    private String description;
    private String callbackUrl;
    private String returnUrl;
    @JsonIgnore
    private String merchantAccountNumber;
    private String cancellationUrl;
    private String clientReference;
    private String status;
    private double paymentAmount;
    private String checkoutId;
    private String paymentDate;
    private String egcrDate;
    private String egcrNo;
    private String egcrLink;
    private String egcrRef;

    public PaymentInfo() {
        this.merchantAccountNumber = Constants.MERCHANT_ID;
    }


}
