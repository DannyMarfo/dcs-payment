package com.casantey.dcspayment.email;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class EmailMessaging {
	private String email;
	private String recipientName;
	private String subject;
	private String message;
}
