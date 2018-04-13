package edu.sentise;

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

		return "Accuracy, Kappa, Precision(pos), Recall(pos), Fmeasure(pos), Precision(neu), Recall(neu), Fmeasure(neu), Precision(neg), Recall(neg), Fmeasure(neg)";
	}

}
