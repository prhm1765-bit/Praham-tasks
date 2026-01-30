package com.CustomerRegi.service;

import com.CustomerRegi.dto.CustomerAddressReportDTO;
import com.CustomerRegi.dto.CustomerReportDTO;
import com.CustomerRegi.dto.CustomerResDTO;
import com.CustomerRegi.mapper.CustomerMapper;
import com.CustomerRegi.model.Customer;
import com.CustomerRegi.repository.CustomerRepo;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements  ReportService {

	private final CustomerRegistrationService customerService;
	private final CustomerMapper customerMapper;

	/**
	* @param reportName is the file name of the report
	* {@inheritDoc}
	* @return it is returning bytes for pdf generation
	* */
	@Override
	public byte[] exportToPdf(String reportName, String lang) throws Exception {
		List<CustomerResDTO> allCustomers = customerService.findAll();
		List<CustomerReportDTO> data = customerMapper.toReportDTOList(allCustomers);
		JasperReport jasperReport;
		try (InputStream inputStream = new ClassPathResource("reports/" + reportName).getInputStream()) {
			jasperReport = (JasperReport) JRLoader.loadObject(inputStream);
		}
		JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(data);
		Locale locale = (lang == null || lang.isBlank()) ? Locale.ENGLISH : new Locale(lang);
		Map<String, Object> parameters = getAllLabels(locale);
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
		return JasperExportManager.exportReportToPdf(jasperPrint);
	}

	private Map<String, Object> getAllLabels(Locale locale) {
		ResourceBundle bundle = ResourceBundle.getBundle("reports/i18n/labels", locale);
		Map<String, Object> params = new HashMap<>();
		for (String key : bundle.keySet()) {
			params.put(key, bundle.getString(key));
		}
		return params;
	}

	/**
	 * @param reportName is the file name of the report
	 * {@inheritDoc}
	 * @return it is returning bytes for pdf generation
	 * */
	@Override
	public byte[] exportAddressToPdf(String reportName, String lang) throws Exception {
		List<CustomerResDTO> allCustomers = customerService.findAll();
		List<CustomerAddressReportDTO> data = customerMapper.toAddressReportDTOList(allCustomers);
		JasperReport jasperReport;
		try (InputStream inputStream = new ClassPathResource("reports/" + reportName).getInputStream()) {
			jasperReport = (JasperReport) JRLoader.loadObject(inputStream);
		}
		JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(data);
		Locale locale = (lang == null || lang.isBlank()) ? Locale.ENGLISH : new Locale(lang);
		Map<String, Object> parameters = getAllLabels(locale);
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
		return JasperExportManager.exportReportToPdf(jasperPrint);
	}
}
