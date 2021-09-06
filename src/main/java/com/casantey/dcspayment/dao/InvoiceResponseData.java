package com.casantey.dcspayment.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class InvoiceResponseData {
	
	private String checkoutUrl;
	private String checkoutId;
	private String clientReference;
	private String message;
	private String checkoutDirectUrl;
	
	

}
