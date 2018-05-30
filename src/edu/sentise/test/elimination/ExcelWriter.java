package edu.sentise.test.elimination;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import edu.sentise.model.SentimentData;
import edu.sentise.util.Util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ExcelWriter {

    private static String[] columns = {"Text", "class"};
   // private static List<SentimentResults> sentiResults =  new ArrayList<>();

	public static void writeInExcel(ArrayList<SentimentResults> sentiResults, String outputPath )
	{
		 Workbook workbook = new XSSFWorkbook(); // new HSSFWorkbook() for generating `.xls` file

	        /* CreationHelper helps us create instances for various things like DataFormat, 
	           Hyperlink, RichTextString etc, in a format (HSSF, XSSF) independent way */
	        CreationHelper createHelper = workbook.getCreationHelper();

	        // Create a Sheet
	        Sheet sheet = workbook.createSheet("senti_res");

	        // Create a Font for styling header cells
	        Font headerFont = workbook.createFont();
	        headerFont.setBold(true);
	        headerFont.setFontHeight((short) 16);
	        headerFont.setColor(IndexedColors.RED.getIndex());

	        // Create a CellStyle with the font
	        CellStyle headerCellStyle = workbook.createCellStyle();
	        headerCellStyle.setFont(headerFont);

	        // Create a Row
	        Row headerRow = sheet.createRow(0);

	        // Creating cells
	        for(int i = 0; i < columns.length; i++) {
	            Cell cell = headerRow.createCell(i);
	            cell.setCellValue(columns[i]);
	            cell.setCellStyle(headerCellStyle);
	        }

	        // Create Cell Style for formatting Date
	       // CellStyle dateCellStyle = workbook.createCellStyle();
	        //dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd-MM-yyyy"));

	        // Create Other rows and cells with employees data
	        int rowNum = 1;
	        for(SentimentResults sr: sentiResults) {
	            Row row = sheet.createRow(rowNum++);

	            row.createCell(0)
	                    .setCellValue(sr.text);

	            row.createCell(1)
	                    .setCellValue(sr.neutral);

	           /* Cell dateOfBirthCell = row.createCell(2);
	            dateOfBirthCell.setCellValue(employee.getDateOfBirth());
	            dateOfBirthCell.setCellStyle(dateCellStyle);*/
	            
	            row.createCell(2)
	            .setCellValue(sr.negative);
	            row.createCell(3)
	                    .setCellValue(sr.positive);
	        }

			// Resize all columns to fit the content size
	        for(int i = 0; i < columns.length; i++) {
	            sheet.autoSizeColumn(i);
	        }
	        // Write the output to a file
	        try
	        {
		        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
		        String path = outputPath+ File.separator + timeStamp + "xlsx";
		     // Use relative path for Unix systems
		        File f = new File(path);
		        f.getParentFile().mkdirs(); 
		        f.createNewFile();
	       
		        FileOutputStream fileOut = new FileOutputStream(f);
		        workbook.write(fileOut);
		        fileOut.close();
		        Util.Logger("output file saved in "+ f.getAbsolutePath());
	        }
	        catch(Exception e)
	        {
	        	e.printStackTrace();
	        }
	}
	public static void writeResultsInExcel(ArrayList<SentimentData> sentiResults, String outputPath )
	{
		 Workbook workbook = new XSSFWorkbook(); // new HSSFWorkbook() for generating `.xls` file

	        /* CreationHelper helps us create instances for various things like DataFormat, 
	           Hyperlink, RichTextString etc, in a format (HSSF, XSSF) independent way */
	        CreationHelper createHelper = workbook.getCreationHelper();

	        // Create a Sheet
	        Sheet sheet = workbook.createSheet("senti_res");

	        // Create a Font for styling header cells
	        Font headerFont = workbook.createFont();
	        headerFont.setBold(true);
	        headerFont.setFontHeight((short) 16);
	        headerFont.setColor(IndexedColors.RED.getIndex());

	        // Create a CellStyle with the font
	        CellStyle headerCellStyle = workbook.createCellStyle();
	        headerCellStyle.setFont(headerFont);

	        // Create a Row
	        Row headerRow = sheet.createRow(0);

	        // Creating cells
	        for(int i = 0; i < columns.length; i++) {
	            Cell cell = headerRow.createCell(i);
	            cell.setCellValue(columns[i]);
	            cell.setCellStyle(headerCellStyle);
	        }

	        // Create Cell Style for formatting Date
	       // CellStyle dateCellStyle = workbook.createCellStyle();
	        //dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd-MM-yyyy"));

	        // Create Other rows and cells with employees data
	        int rowNum = 1;
	        for(SentimentData sr: sentiResults) {
	            Row row = sheet.createRow(rowNum++);

	            row.createCell(0)
	                    .setCellValue(sr.getText());

	            row.createCell(1)
	                    .setCellValue(sr.getRating());

	           /* Cell dateOfBirthCell = row.createCell(2);
	            dateOfBirthCell.setCellValue(employee.getDateOfBirth());
	            dateOfBirthCell.setCellStyle(dateCellStyle);*/
	            
	        }

			// Resize all columns to fit the content size
	        for(int i = 0; i < columns.length; i++) {
	            sheet.autoSizeColumn(i);
	        }
	        // Write the output to a file
	        try
	        {
		        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
		        String path = outputPath+ File.separator + timeStamp + ".xlsx";
		     // Use relative path for Unix systems
		        File f = new File(path);
		        f.getParentFile().mkdirs(); 
		        f.createNewFile();
	       
		        FileOutputStream fileOut = new FileOutputStream(f);
		        workbook.write(fileOut);
		        fileOut.close();
		        Util.Logger("output file saved in "+ f.getAbsolutePath());
	        }
	        catch(Exception e)
	        {
	        	e.printStackTrace();
	        }
	}
	
    public static void main(String[] args) throws IOException, InvalidFormatException {
        // Create a Workbook
       
    }
}