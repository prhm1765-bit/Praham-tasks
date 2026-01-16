package com.CustomerRegi.service;

public interface ReportService {

	/**
	 * @param reportName is file name of report
	 * @return it is returning byte for PDF generation
	 * */
	byte[] exportToPdf(String reportName) throws Exception;

}



