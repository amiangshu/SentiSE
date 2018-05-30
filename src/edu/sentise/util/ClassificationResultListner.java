package edu.sentise.util;
import java.util.ArrayList;

import edu.sentise.model.*;

public interface ClassificationResultListner {

	public void onClassificationDone(ArrayList<SentimentData> sentiData);
}
