/*
 * Copyright (C) 2018 Southern Illinois University Carbondale, SoftSearch Lab
 *
 * Author: Amiangshu Bosu
 *
 * Licensed under GNU LESSER GENERAL PUBLIC LICENSE Version 3, 29 June 2007
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.gnu.org/licenses/lgpl.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package edu.siu.sentise.model;

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

	public static ArrayList<SentimentData> parseSentimentData(String fileName) {

		ArrayList<SentimentData> sentimentDataList = new ArrayList<>();
		
		int rowCount=0;
		try {

			FileInputStream excelFile = new FileInputStream(new File(fileName));
			Workbook workbook = new XSSFWorkbook(excelFile);
			Sheet datatypeSheet = workbook.getSheetAt(0);
			Iterator<Row> iterator = datatypeSheet.iterator();

			while (iterator.hasNext()) {

				Row currentRow = iterator.next();	
				rowCount++;
				String text = "";
				int rating = 0;

				try {
					Cell textCell=currentRow.getCell(0);
					if(textCell.getCellTypeEnum()==CellType.STRING)// first column is text					
						text = textCell.getStringCellValue(); 
					else if(textCell.getCellTypeEnum()==CellType.NUMERIC)
						text = Double.toString(textCell.getNumericCellValue()); 
					else if(textCell.getCellTypeEnum()==CellType.FORMULA)
						text = textCell.getCellFormula();
					
					
					rating = (int) currentRow.getCell(1).getNumericCellValue(); // second column is rating
					
					if (rating <= 1 && rating >= -1) {
						SentimentData sentimentData = new SentimentData(text, rating);
						sentimentDataList.add(sentimentData);
						//System.out.println(sentimentData.toString());

					}
					else {
						System.out.println("Error: "+currentRow+"-> "+rating);
					}
				} catch (Exception e) {
					System.out.println("Error parsing row:"+rowCount);
					System.out.println(e.getMessage());
					
				}

			}
			workbook.close();
		} catch (FileNotFoundException e) {
			System.out.println("Unable to open oracle file!");
			System.out.println(e.getMessage());
			System.exit(1);
		} catch (IOException e) {
			System.out.println("Unable to parse oracle file!");
			System.exit(1);
		}
		
		return sentimentDataList;
	}
	
	public String toString() {
		return text+" ["+rating+"]";
		
	}
}
