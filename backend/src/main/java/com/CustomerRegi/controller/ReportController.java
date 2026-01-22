package com.CustomerRegi.controller;

import com.CustomerRegi.service.ReportService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.CustomerRegi.constants.ApiPathConstants.CUSTOMERDATA;
import static com.CustomerRegi.constants.ApiPathConstants.REPORTS;

@RestController
@RequestMapping(REPORTS)
public class ReportController {

	private final ReportService reportService;

	public ReportController(ReportService reportService) {
		this.reportService = reportService;
	}

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping(CUSTOMERDATA)
	public ResponseEntity<byte[]> generateCustomerReport() throws Exception {

		// Export to PDF
		byte[] pdfBytes = reportService.exportToPdf("customer_details.jasper");

		// Return as HTTP response
		return ResponseEntity.ok()
			.header(HttpHeaders.CONTENT_DISPOSITION,
				"inline; filename=customer-report.pdf")
			.contentType(MediaType.APPLICATION_PDF)
			.body(pdfBytes);
	}

}
