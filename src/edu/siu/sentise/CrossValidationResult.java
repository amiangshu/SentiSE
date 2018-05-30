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

package edu.siu.sentise;

public class CrossValidationResult {

	private float accuracy;
	private float posPrecision;
	private float negPrecision;
	private float neuPrecision;
	private float posRecall;
	private float negRecall;
	private float neuRecall;
	private float posFmeasure;
	private float negFmeasure;
	private float neuFmeasure;
	private float kappa;

	public float getAccuracy() {
		return accuracy;
	}

	public void setAccuracy(float accuracy) {
		this.accuracy = accuracy;
	}

	public float getPosPrecision() {
		return posPrecision;
	}

	public void setPosPrecision(float posPrecision) {
		this.posPrecision = posPrecision;
	}

	public float getNegPrecision() {
		return negPrecision;
	}

	public void setNegPrecision(float negPrecision) {
		this.negPrecision = negPrecision;
	}

	public float getNeuPrecision() {
		return neuPrecision;
	}

	public void setNeuPrecision(float neuPrecision) {
		this.neuPrecision = neuPrecision;
	}

	public float getPosRecall() {
		return posRecall;
	}

	public void setPosRecall(float posRecall) {
		this.posRecall = posRecall;
	}

	public float getNegRecall() {
		return negRecall;
	}

	public void setNegRecall(float negRecall) {
		this.negRecall = negRecall;
	}

	public float getNeuRecall() {
		return neuRecall;
	}

	public void setNeuRecall(float neuRecall) {
		this.neuRecall = neuRecall;
	}

	public float getPosFmeasure() {
		return posFmeasure;
	}

	public void setPosFmeasure(float posFmeasure) {
		this.posFmeasure = posFmeasure;
	}

	public float getNegFmeasure() {
		return negFmeasure;
	}

	public void setNegFmeasure(float negFmearue) {
		this.negFmeasure = negFmearue;
	}

	public float getNeuFmeasure() {
		return neuFmeasure;
	}

	public void setNeuFmeasure(float neuFmeasure) {
		this.neuFmeasure = neuFmeasure;
	}

	public float getKappa() {
		return kappa;
	}

	public void setKappa(float kappa) {
		this.kappa = kappa;
	}

	public String toString() {

		return accuracy + ", " + kappa+ ", "+ posPrecision + "," + posRecall + "," + posFmeasure + "," + neuPrecision + ","
				+ neuRecall + "," + neuFmeasure + "," + negPrecision + "," + negRecall + "," + negFmeasure;
	}

	public static String getResultHeader() {

		return "Accuracy,  Weighted-Kappa, Precision(pos), Recall(pos), Fmeasure(pos), Precision(neu), Recall(neu), Fmeasure(neu), Precision(neg), Recall(neg), Fmeasure(neg)";
	}

}
