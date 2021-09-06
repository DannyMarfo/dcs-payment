package com.casantey.dcspayment.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class InvoiceResponse {
	
	private String responseCode;
	private String status;
	private InvoiceResponseData data;

	

}
