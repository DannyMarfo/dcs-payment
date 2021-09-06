package com.casantey.dcspayment.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class InvoiceModel {

	private String clientReference;
	private String description;
	private String callbackUrl;
	private String returnUrl;
	private String merchantAccountNumber;
	private String cancellationUrl;
	private double totalAmount;


}
