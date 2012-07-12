package com.zybnet;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.HashMap;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellReference;

public class Main {

	private static Logger logger;
	
	// args[0] must be the directory
	// args[1] cella
	// args[2] output pdf filename
	public static void main(String[] args) throws InvalidFormatException {
		
		double total = 0;
		
		File baseDirectory = new File(args[0]);
		
		for (File filename : baseDirectory.listFiles(new Filter())) {
			OPCPackage file = null;
			Workbook workbook = null;
			
			try {
				file = OPCPackage.open(filename.getCanonicalPath());
				workbook = WorkbookFactory.create(file);
			} catch (Exception e) {
				logger.error(e);
				continue;
			}
			
			Sheet sheet = workbook.getSheetAt(0);
			CellReference cell = new CellReference(args[1]);
			
			total += sheet.getRow(cell.getRow()).getCell(cell.getCol()).getNumericCellValue();
			
			try {
				file.close();
			} catch (IOException e) {
				logger.warn(e);
			}
			
			logger.info(filename + " [OK]");
		}
		
		logger.info(String.format("Il totale è %.2f€%n", total));
		
		// Generate the report
		try {
			JasperReport report = JasperCompileManager.compileReport(
					Main.class.getResourceAsStream("design.jrxml"));
			
			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put("TOTALE", new Double(total));
			
			JasperPrint print = JasperFillManager.fillReport(
					report,
					params,
					new JREmptyDataSource());
			
			File pdfOutput = new File(args[2]);
			JasperExportManager.exportReportToPdfFile(print, pdfOutput.getCanonicalPath());
			
			logger.info("Generated report " +  pdfOutput.getCanonicalPath());
		} catch (JRException e) {
			logger.error(e);
		} catch (IOException e) {
			logger.error(e);
		}
	}

	static  {
		logger = Logger.getLogger(Main.class);
		PropertyConfigurator.configure(Main.class.getResource("log4j.properties"));
	}
	
	private static class Filter implements FilenameFilter {

		public boolean accept(File parent, String name) {
			name = name.toUpperCase();
			return (name.endsWith("XLS") || name.endsWith("XLSX")) ? true : false;
		}

	}
	
}
