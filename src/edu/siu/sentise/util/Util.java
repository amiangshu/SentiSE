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

package edu.siu.sentise.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import edu.siu.sentise.Configuration;
import edu.siu.sentise.factory.BasePOSUtility;
import edu.siu.sentise.preprocessing.StanfordCoreNLPLemmatizer;
import edu.stanford.nlp.trees.Tree;
import weka.classifiers.Evaluation;
import weka.core.stemmers.SnowballStemmer;

public class Util {
	private static HashSet<String> negation_words = new HashSet<String>(Arrays.asList(DataLists.negation_words));
	private static HashSet<String> emoticon_words = new HashSet<String>(Arrays.asList(DataLists.emoticon_words));

	public static BufferedReader getBufferedreaderByFileName(String fileName) {

		try {

			FileReader fileReader = new FileReader(fileName);

			return new BufferedReader(fileReader);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void closeBufferedReader(BufferedReader bufferedReader) {
		try {
			bufferedReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static double computeWeightedKappa(Evaluation eval) {

		double[][] confusionMatrix = eval.confusionMatrix();
		double[][] expectedMatrix = new double[3][3];

		double totalInstances = eval.numInstances();

		double[] classActual = new double[3];
		double[] classGot = new double[3];

		for (int i = 0; i < 3; i++) {

			for (int j = 0; j < 3; j++) {
				classActual[i] += confusionMatrix[i][j];
				classGot[i] += confusionMatrix[j][i];
			}

		}

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				expectedMatrix[i][j] = classActual[i] * classGot[j] / totalInstances;
			}

		}

		double numerator = 0.0;
		double denominator = 0.0;
		for (int i = 0; i < 3; i++) {

			for (int j = 0; j < 3; j++) {
				numerator += confusionMatrix[i][j] * Math.abs(j - i);
				denominator += expectedMatrix[i][j] * Math.abs(j - i);

			}
		}

		double weightedKappa = 1.0 - (numerator / denominator);
		return weightedKappa;
	}

	public static BufferedWriter getBufferedWriterByFileName(String fileName) {

		try {

			FileWriter fileWriter = new FileWriter(fileName);

			return new BufferedWriter(fileWriter);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void closeBufferedWriter(BufferedWriter bufferedWriter) {
		try {
			bufferedWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void printFile(BufferedReader bufferedReader) {
		try {

			FileInputStream excelFile = new FileInputStream(new File(Configuration.ORACLE_FILE_NAME));
			Workbook workbook = new XSSFWorkbook(excelFile);
			Sheet datatypeSheet = workbook.getSheetAt(0);
			Iterator<Row> iterator = datatypeSheet.iterator();

			while (iterator.hasNext()) {

				Row currentRow = iterator.next();
				Iterator<Cell> cellIterator = currentRow.iterator();

				while (cellIterator.hasNext()) {

					Cell currentCell = cellIterator.next();
					// getCellTypeEnum shown as deprecated for version 3.15
					// getCellTypeEnum ill be renamed to getCellType starting from version 4.0
					if (currentCell.getCellTypeEnum() == CellType.STRING) {
						System.out.print(currentCell.getStringCellValue() + "--");
					} else if (currentCell.getCellTypeEnum() == CellType.NUMERIC) {
						System.out.print(currentCell.getNumericCellValue() + "--");
					}

				}
				System.out.println();
				workbook.close();

			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * only verb,adverb, adjective and modals contribute to negation of a
	 * sentence.So checking eligibility of that pos
	 * 
	 * @param pos
	 *            is the parts of speech
	 * @return is it eligible or not
	 */

	public static boolean isEligiblePos(String pos) {

		if (pos.startsWith("RB") || pos.startsWith("MD") || pos.startsWith("VB") || pos.startsWith("JJ"))
			return true;

		return false;
	}

	public static String negatedWord(String word, String pos) {
		if (negation_words.contains(word))
			return word;
		else if (emoticon_words.contains(word))
			return " " + word;
		else if (pos.startsWith("VB") || pos.startsWith("RB") || pos.startsWith("JJ") || pos.startsWith("MD"))
			return "NOT|" + word;
		else
			return word;
	}

	public static void main(String[] args) {
		// SnowballStemmer snowballStemmer = new SnowballStemmer();
		// String str = snowballStemmer.stem("please makes it work!");
		// System.out.println(str);
		// str = new StanfordCoreNLPLemmatizer().stem("please makes it work!");
		// System.out.println(str);

		double[][] matrix = { { 16, 6, 2 }, { 4, 10, 1 }, { 3, 0, 8 } };

		// System.out.println(computeWeightedKappa(matrix));
	}
}
