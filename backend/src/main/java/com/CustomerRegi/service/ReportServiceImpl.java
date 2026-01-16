package com.CustomerRegi.service;

import com.CustomerRegi.dto.CustomerReportDTO;
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
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements  ReportService {

	private final CustomerRepo customerRepo;

	@Autowired
	private CustomerMapper customerMapper;

	/**
	* @param reportName is the file name of the report
	* {@inheritDoc}
	* @return it is returning bytes for pdf generation
	* */
	@Override
	public byte[] exportToPdf(String reportName) throws Exception {

		// Fetch all customers data
		List<Customer> customers = customerRepo.findAll();

		List<CustomerReportDTO> data = customerMapper.toReportDTOList(customers);

		//Report path
		ClassPathResource resource = new ClassPathResource("reports/" + reportName);

		JasperReport jasperReport;
		try (InputStream inputStream = resource.getInputStream()) {
			// Compile JRXML to JasperReport
			jasperReport = (JasperReport) JRLoader.loadObject(inputStream);
		}

		JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(data);

		// Parameters
		Map<String, Object> parameters = new HashMap<>();

		// Fill report
		JasperPrint jasperPrint = JasperFillManager.fillReport( jasperReport, parameters, dataSource);

		return JasperExportManager.exportReportToPdf(jasperPrint);
	}

}
