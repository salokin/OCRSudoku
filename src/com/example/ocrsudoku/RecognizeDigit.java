package com.example.ocrsudoku;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Size;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Collections;

import org.opencv.core.MatOfPoint;
import org.opencv.ml.CvSVM;
import org.opencv.imgproc.Imgproc;

import android.util.Log;


public class RecognizeDigit {
	static CvSVM SVM1;
	static CvSVM SVM2;
	static boolean trained1 = false;
	static boolean trained2 = false;
	
	

	RecognizeDigit() {	
		
	}		
	
	public float[] identifyDigits(Mat input) {
		
		float[] returnVal = new float[81];
		Mat gray = new Mat();
		Mat thresh = new Mat();
		Mat predict;
		gray = input.clone();
		Imgproc.GaussianBlur(gray, gray, new Size(5, 5), 0);

		Imgproc.adaptiveThreshold(gray, thresh, 255, 1, 1, 11, 2);
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {

				Mat temp = thresh.submat(50 * i, 50 * (i + 1), 50 * j,
						50 * (j + 1));
				
				temp = flood(temp);				
				
				
				
				
				predict = temp.clone();
				predict = predict.reshape(1, 1);
				predict.convertTo(predict, CvType.CV_32FC1);
				float svm1float = SVM1.predict(predict);
				float svm2float = SVM2.predict(predict);
				if ((svm1float==7 && svm2float!=7)|| (svm2float==7 && svm1float!=7)){
					Log.w("SVM1FLOAT", Float.toString(svm1float));
					Log.w("SVM2FLOAT", Float.toString(svm2float));
					Log.w("i", Integer.toString(i));
					Log.w("j", Integer.toString(j));
				}
				if (svm1float == svm2float ) {
					returnVal[(i * 9) + j] = svm1float;
				}
				else if (svm1float!= 0 && svm2float != 0) {					
					if (svm2float == 3 && (svm1float ==9))
						returnVal[(i * 9) + j] = 8;					
					else if (svm2float == 5 && svm1float ==6){
						returnVal[(i * 9) + j] = svm1float;
					}
					else
						returnVal[(i * 9) + j] = svm2float;
				}
				else if (svm1float!= 0 && svm2float == 0) {
					returnVal[(i * 9) + j] = svm1float;
				}
				else {
					returnVal[(i * 9) + j] = 0;
				}
			}
		}
		
		return returnVal;
	}

	public Mat flood(Mat m) {
		Mat contour = m.clone();
		int[] iArray = findMaxContour(contour);
		if (iArray == null) {
			Mat cloneImage = new Mat(50, 50, CvType.CV_8UC1,
					new Scalar(0, 0, 0));
			return cloneImage;
		} else if ((iArray[3] - iArray[1]) < 15 || (iArray[2] - iArray[0]) < 10) {
			Mat cloneImage = new Mat(50, 50, CvType.CV_8UC1,
					new Scalar(0, 0, 0));
			return cloneImage;
		}
		Mat cloneImage = new Mat();
		if (((iArray[0] + iArray[2]) % 2) != 0)
			iArray[2] += 1;
		if (((iArray[1] + iArray[3]) % 2) != 0)
			iArray[3] += 1;
		Mat extract = m.submat(iArray[1], iArray[3], iArray[0], iArray[2]);
////////
		int height = iArray[3] - iArray[1];
		int width = iArray[2] - iArray[0];
		Imgproc.copyMakeBorder(extract, cloneImage, (50 - height) / 2,
				(50 - height) / 2, (50 - width) / 2, (50 - width) / 2,
				Imgproc.BORDER_CONSTANT, new Scalar(0));
		double[] black = { 0 };

		for (int i = 0; i < 50; i++) {
			for (int j = 0; j < (50 - width) / 2 - 2; j++) {
				cloneImage.put(i, j, black);
				cloneImage.put(i, 50 - j - 1, black);
			}
		}

		for (int i = 0; i < (50 - height) / 2 - 2; i++) {
			for (int j = 0; j < 50; j++) {
				cloneImage.put(i, j, black);
				cloneImage.put(50 - i - 1, j, black);
			}
		}
		return cloneImage;
	}

	public int[] findMaxContour(Mat m) {
		Mat mask = new Mat(m.height() + 2, m.width() + 2, CvType.CV_8UC1);
		for (int i = 0; i < m.width(); i++) {
			for (int j = 0; j < 1; j++) {
				
				Imgproc.floodFill(m, mask, new Point(j, i), new Scalar(0, 0, 0));
				
				Imgproc.floodFill(m, mask, new Point(m.width() - 1 - j, i),
						new Scalar(0, 0, 0));
				
				//Imgproc.floodFill(m, mask, new Point(i, j), new Scalar(0, 0, 0));
				//Imgproc.floodFill(m, mask, new Point(i, m.height() - 1 - j),	new Scalar(0, 0, 0));
			}
		}
		
		Mat hierarchy = new Mat();
		double area = 0;
		int tempIndex = 0;
		List<MatOfPoint> contours = new ArrayList<>();
		Imgproc.findContours(m, contours, hierarchy, Imgproc.RETR_EXTERNAL,
				Imgproc.CHAIN_APPROX_SIMPLE);
		for (int i = 0; i < contours.size(); i++) {
			if (Imgproc.contourArea(contours.get(i)) > area) {
				tempIndex = i;
				area = Imgproc.contourArea(contours.get(i));
			}
		}
		if (contours.isEmpty())
			return null;
		MatOfPoint mop = contours.get(tempIndex);
		
		List<Point> mopList = mop.toList();
		Collections.sort(mopList, new Comparator<Point>() {

			public int compare(Point o1, Point o2) {
				return Double.compare(o1.y, o2.y);
			}
		});
		int smally = (int) mopList.get(0).y;
		int largey = (int) mopList.get(mopList.size() - 1).y;
		Collections.sort(mopList, new Comparator<Point>() {

			public int compare(Point o1, Point o2) {
				return Double.compare(o1.x, o2.x);
			}
		});
		int smallx = (int) mopList.get(0).x;
		int largex = (int) mopList.get(mopList.size() - 1).x;
		int[] returnArray = { smallx, smally, largex, largey };
		return returnArray;

	}
}