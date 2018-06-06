# SentiSE
SentiSE is a sentiment analysis tool for Software Engineering interactions

SentiSE, a supervised learning based sentiment analysis tool that incorporates ten supervised learning algorithms and 
fourteen different optional pre-processing steps that are commonly used to improve the performance of sentiment analysis tools. 
We empirically evaluated each of the algorithms and preprocessing steps to determine the best configuration. 
We evaluated SentiSE using a large-scale labeled dataset of 13K comments from three different types of SE interactions.
<br><br>
**Performance Evaluation:**
We compare SentiSE with other sentiment analysis tool available in software engineering domain.  We use two dataset for this evaluation.  _Orcale1_ with 13 k labeled dataset with 21% positive, 60% neutral and 19% negative data and _Oracle2_ with 30% positive, 40% neutral and 30% negative data. Table bellow shows the performance comparison:

| Oracle | tool  |Precision<br> (Positive) | Recall<br> (Positive) | F-measure<br> (Positive) | Precision<br> (Positive) | Recall<br> (Positive) | F-measure<br> (Positive) |  Precision<br> (Positive) | Recall<br> (Positive) | F-measure<br> (Positive) | Accuracy | Weighted <br> Kappa |
|--|--| -- | -- |  -- | -- | --|  -- | -- | --|  -- | -- | --|
|_Orcal1_|SentiSE  | 85.63% | 75.27% | 80.11% | 81.51% | 92.78% | 86.78% | 81.03% | 55.92%|66.16%|82.23%|0.681
|_Oracle1_| SentiCR |  81.81% | 76.59%|  79.04%|  80.04% | 92.77% | 85.92%  | 82.71% | 46.38% | 59.40% | 80.6655% | 0.647
|_Oracle1_| SentiStrength-SE |  75.81% | 81.45% | 78.53% | 84.68% | 83.64% | 84.16% | 66.50% | 63.42% | 64.92% | 79.32% | 0.6587
|_Oracle2_| SentiSE | 88.83% | 85.09% | 86.92% | 86.62% | 91.52% | 89.00% | 85.87% | 78.61% | 82.07% | 86.92% | 0.788
|_Oracle2_|SentiCR |  84.32% | 84.73% | 84.50% | 80.70% | 92.08% | 86.00%  | 86.45% | 59.49\% | 70.40%  | 82.47% | 0.716
|_Oracle2_| SentiStrength-SE | 79.56% | 83.57% | 81.52% | 80.73% | 84.15% | 82.41% | 80.41% | 69.31% | 74.45% | 80.34% | 0. 696

<br><br><br>
**Usage Instructions:**
Downaload and import SentiSe Project. Run the build.xml file and generate the sentise.jar. SentiSE is a commandline base tool. Use the command <code> java -jar sentise.jar -help</code> to find all the commands avialable in sentiSE.

<br><br>
**ScreenShot**
![SentiSE-cli](https://github.com/amiangshu/SentiSE/blob/tanzeerH-readme/models/cli.png)
