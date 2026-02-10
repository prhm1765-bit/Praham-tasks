package com.CustomerRegi.controller;

import com.CustomerRegi.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.CustomerRegi.constants.ApiPathConstants.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(REPORTS)
public class ReportController {

	private final ReportService reportService;

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping(CUSTOMERDATA)
	public ResponseEntity<byte[]> generateCustomerReport(@RequestParam String lang) throws Exception {

		// Export to PDF
		byte[] pdfBytes = reportService.exportToPdf("customer_details.jasper", lang);

		// Return as HTTP response
		return ResponseEntity.ok()
			.header(HttpHeaders.CONTENT_DISPOSITION,
				"inline; filename=customer-report.pdf")
			.contentType(MediaType.APPLICATION_PDF)
			.body(pdfBytes);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping(CUSTOMERADDRESSDATA)
	public ResponseEntity<byte[]> generateCustomerAddressReport(@RequestParam String lang) throws Exception {

		// Export to PDF
		byte[] pdfBytes = reportService.exportAddressToPdf("customer_address.jasper", lang);

		// Return as HTTP response
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION,  "inline; filename=customer-report.pdf")
				.contentType(MediaType.APPLICATION_PDF)
				.body(pdfBytes);
	}

}
