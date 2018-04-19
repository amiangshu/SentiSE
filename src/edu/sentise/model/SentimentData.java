package edu.sentise.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class SentimentData {

	private String text;
	private int rating;
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public int getRating() {
		return rating;
	}
	public void setRating(int rating) {
		this.rating = rating;
	}
	public SentimentData(String text, int rating) {
	
		this.text = text;
		this.rating = rating;
	}
	public static ArrayList<SentimentData> parseSentimentData(String fileName)
	{
	
		//BufferedReader bufferedReader=Util.getBufferedreaderByFileName(Constants.ORACLE_FILE_NAME);
		ArrayList<SentimentData> sentimentDataList =  new ArrayList<>();
		 try {

	            FileInputStream excelFile = new FileInputStream(new File(fileName));
	            Workbook workbook = new XSSFWorkbook(excelFile);
	            Sheet datatypeSheet = workbook.getSheetAt(0);
	            Iterator<Row> iterator = datatypeSheet.iterator();

	            int positive=0;
	            int negative=0;
	            int neutral=0;
	            while (iterator.hasNext()) {

	            	
	            	//if(count>1600)
	            		//break;
	                Row currentRow = iterator.next();
	                Iterator<Cell> cellIterator = currentRow.iterator();
	                //SentimentData sentimentData=new SentimentData(text, rating)
	                String text="";
	                int rating=0;
	                while (cellIterator.hasNext()) {

	                    Cell currentCell = cellIterator.next();
	                  
	                    //getCellTypeEnum shown as deprecated for version 3.15
	                    //getCellTypeEnum ill be renamed to getCellType starting from version 4.0
	                    if (currentCell.getCellTypeEnum() == CellType.STRING) {
	                    	text=currentCell.getStringCellValue();
	                       // System.out.print(currentCell.getStringCellValue() + "--");
	                    } else if (currentCell.getCellTypeEnum() == CellType.NUMERIC) {
	                       // System.out.print(currentCell.getNumericCellValue() + "--");
	                    	rating=(int)currentCell.getNumericCellValue(); 
	                    }

	                }
	                if(rating<2)
	                {
	                SentimentData sentimentData=new SentimentData(text, rating);
	               // String[] data=text.split("\\s+");
	                //if(data.length>20)
		                //if((rating == 0 && neutral<800) || (rating == 1 && positive<400) ||(rating == -1 && negative<400))
		               // {
		                	sentimentDataList.add(sentimentData);
		                	if(rating == 0)
		                		neutral++;
		                	else if(rating == 1)
		                		positive++;
		                	else if(rating == -1)
		                		negative++;
		                	
		                }
	               // }
	                //System.out.println();

	            }
	        } catch (FileNotFoundException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
		 return sentimentDataList;
	}
}
