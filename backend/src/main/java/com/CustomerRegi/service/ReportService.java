package com.CustomerRegi.service;

import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

import java.util.List;

public interface ReportService {

	//JasperReport loadAndCompileReport(String reportName) throws Exception;

	//JasperPrint fillReport(JasperReport jasperReport, List<?> data) throws Exception;

	byte[] exportToPdf(String reportName) throws Exception;


}



