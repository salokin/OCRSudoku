package com.example.ocrsudoku;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.imgproc.Imgproc;
import org.opencv.core.Size;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.highgui.*;


public class Sudoku {

	private float [] digits;
	private int [] [] digits2 = new int[9][9];
	private boolean doesItWork;
	
	public boolean getDIW() {
		return doesItWork;
	}
	
	public float [] getDigits() {
		return digits;
	}
	
	public int [][] getDigits2() {
		return digits2;
	}
	public void doHough(String s) {
		
		Mat newImage;
		Mat inputImage = Highgui.imread(s, 0);
		if ((double)inputImage.width()/(double)inputImage.height() == (double)(16)/(double)(9)) {
			newImage = new Mat(360,640,CvType.CV_8UC3);
		}
		else if ((double)inputImage.height()/(double)inputImage.width() == (double)(16)/(double)(9)){
			newImage = new Mat(640,360,CvType.CV_8UC3);
		}
		else if ((double)inputImage.width()/(double)inputImage.height() == .75){
			newImage = new Mat(450,600,CvType.CV_8UC3);
		}
		else {
			newImage = new Mat(450,450,CvType.CV_8UC3);
		}
		
		
		Imgproc.resize(inputImage, newImage, newImage.size(), 0, 0, Imgproc.INTER_AREA);
		Mat original = newImage.clone();
		Mat outerBox = new Mat(newImage.size(), CvType.CV_8UC1);
		Imgproc.GaussianBlur(newImage, newImage, new Size(11, 11), 0);
		Imgproc.adaptiveThreshold(newImage, outerBox, 255,
				Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY, 5, 2);
		Core.bitwise_not(outerBox, outerBox);
		Mat newKernel = new Mat(3, 3, CvType.CV_8UC1);
		newKernel.put(0, 0, 0);
		newKernel.put(1, 0, 1);
		newKernel.put(2, 0, 0);
		newKernel.put(0, 1, 1);
		newKernel.put(1, 1, 1);
		newKernel.put(2, 1, 1);
		newKernel.put(0, 2, 0);
		newKernel.put(1, 2, 1);
		newKernel.put(2, 2, 0);
		Imgproc.dilate(outerBox, outerBox, newKernel);
		int newMax = -1;
		Point newMaxPt = new Point();
		for (int y = 0; y < outerBox.height(); y++) {
			Mat newRow = outerBox.row(y);
			for (int x = 0; x < outerBox.width(); x++) {
				if (newRow.get(0, x)[0] >= 128) {
					int newArea = Imgproc.floodFill(outerBox, new Mat(),
							new Point(x, y), new Scalar(64));

					if (newArea > newMax) {
						newMaxPt.x = x;
						newMaxPt.y = y;
						newMax = newArea;
					}
				}
			}
		}
		Imgproc.floodFill(outerBox, new Mat(), newMaxPt, new Scalar(255));
		for (int y = 0; y < outerBox.height(); y++) {
			Mat newRow = outerBox.row(y);
			for (int x = 0; x < outerBox.width(); x++) {
				if (newRow.get(0, x)[0] == 64 && x != newMaxPt.x
						&& y != newMaxPt.y) {
					int newArea = Imgproc.floodFill(outerBox, new Mat(),
							new Point(x, y), new Scalar(0));
				}
			}
		}
		Imgproc.erode(outerBox, outerBox, newKernel);
		Mat lines = new Mat();
		Imgproc.HoughLines(outerBox, lines, 1, Math.PI / 180, 200);
		lines = mergeRelatedLines(lines, newImage);
		
		for (int i = 0; i < lines.cols(); i++) {
			drawLine(lines.get(0, i), outerBox, new Scalar(128));
		}
		// Now detect the lines on extremes
		double [] topEdge = {1000,1000};	double topYIntercept=100000, topXIntercept=0;
		double [] bottomEdge = {-1000,-1000};		double bottomYIntercept=0, bottomXIntercept=0;
		double [] leftEdge = {1000,1000};	double leftXIntercept=100000, leftYIntercept=0;
		double [] rightEdge = {-1000,-1000};		double rightXIntercept=0, rightYIntercept=0;
		for(int i=0;i<lines.cols();i++)
		{
			double [] current = lines.get(0, i);

			double p=current[0];
			double theta=current[1];

			if(p==0 && theta==-100)
				continue;

			double xIntercept, yIntercept;
			xIntercept = p/Math.cos(theta);
			yIntercept = p/(Math.cos(theta)*Math.sin(theta));

			if(theta>Math.PI*80/180 && theta<Math.PI*100/180)
			{
				if(p<topEdge[0])
					topEdge = current;

				if(p>bottomEdge[0])
					bottomEdge = current;

			}
			else if(theta<Math.PI*10/180 || theta>Math.PI*170/180)
			{

				if(xIntercept>rightXIntercept)
				{
					rightEdge = current;
					rightXIntercept = xIntercept;
				} 
				else if(xIntercept<=leftXIntercept)
				{
					leftEdge = current;
					leftXIntercept = xIntercept;
				}
			}
		}


		drawLine(topEdge, newImage, new Scalar(0,0,0));
		drawLine(bottomEdge, newImage, new Scalar(0,0,0));
		drawLine(leftEdge, newImage, new Scalar(0,0,0));
		drawLine(rightEdge, newImage, new Scalar(0,0,0));

		Point left1 = new Point(), left2= new Point(), right1= new Point(), right2= new Point(), bottom1= new Point(), bottom2= new Point(), top1= new Point(), top2= new Point();

		int height=outerBox.height();
		int width=outerBox.width();

		if(leftEdge[1]!=0)
		{
			left1.x=0;		left1.y=leftEdge[0]/Math.sin(leftEdge[1]);
			left2.x=width;	left2.y=-left2.x/Math.tan(leftEdge[1]) + left1.y;
		}
		else
		{
			left1.y=0;		left1.x=leftEdge[0]/Math.cos(leftEdge[1]);
			left2.y=height;	left2.x=left1.x - height*Math.tan(leftEdge[1]);
		}

		if(rightEdge[1]!=0)
		{
			right1.x=0;		right1.y=rightEdge[0]/Math.sin(rightEdge[1]);
			right2.x=width;	right2.y=-right2.x/Math.tan(rightEdge[1]) + right1.y;
		}
		else
		{
			right1.y=0;		right1.x=rightEdge[0]/Math.cos(rightEdge[1]);
			right2.y=height;	right2.x=right1.x - height*Math.tan(rightEdge[1]);
		}

		bottom1.x=0;	bottom1.y=bottomEdge[0]/Math.sin(bottomEdge[1]);
		bottom2.x=width;bottom2.y=-bottom2.x/Math.tan(bottomEdge[1]) + bottom1.y;

		top1.x=0;		top1.y=topEdge[0]/Math.sin(topEdge[1]);
		top2.x=width;	top2.y=-top2.x/Math.tan(topEdge[1]) + top1.y;

		// Next, we find the intersection of  these four lines
	    double leftA = left2.y-left1.y;
	    double leftB = left1.x-left2.x;
	    double leftC = leftA*left1.x + leftB*left1.y;
	 
	    double rightA = right2.y-right1.y;
	    double rightB = right1.x-right2.x;
	    double rightC = rightA*right1.x + rightB*right1.y;
	 
	    double topA = top2.y-top1.y;
	    double topB = top1.x-top2.x;
	    double topC = topA*top1.x + topB*top1.y;
	 
	    double bottomA = bottom2.y-bottom1.y;
	    double bottomB = bottom1.x-bottom2.x;
	    double bottomC = bottomA*bottom1.x + bottomB*bottom1.y;
	 
	    // Intersection of left and top
	    double detTopLeft = leftA*topB - leftB*topA;
	    Point ptTopLeft = new Point((topB*leftC - leftB*topC)/detTopLeft, (leftA*topC - topA*leftC)/detTopLeft);
	 
	    // Intersection of top and right
	    double detTopRight = rightA*topB - rightB*topA;
	    Point ptTopRight = new Point((topB*rightC-rightB*topC)/detTopRight, (rightA*topC-topA*rightC)/detTopRight);
	 
	    // Intersection of right and bottom
	    double detBottomRight = rightA*bottomB - rightB*bottomA;
	    Point ptBottomRight = new Point((bottomB*rightC-rightB*bottomC)/detBottomRight, (rightA*bottomC-bottomA*rightC)/detBottomRight);
	 
	    // Intersection of bottom and left
	    double detBottomLeft = leftA*bottomB-leftB*bottomA;
	    Point ptBottomLeft = new Point((bottomB*leftC-leftB*bottomC)/detBottomLeft, (leftA*bottomC-bottomA*leftC)/detBottomLeft);

		Core.line(newImage, ptTopRight, ptTopRight, new Scalar(255,0,0), 10);
		Core.line(newImage, ptTopLeft, ptTopLeft, new Scalar(255,0,0), 10);
		Core.line(newImage, ptBottomRight, ptBottomRight, new Scalar(255,0,0), 10);
		Core.line(newImage, ptBottomLeft, ptBottomLeft, new Scalar(255,0,0), 10);

		// Correct the perspective transform
		int maxLength = (int)((ptBottomLeft.x-ptBottomRight.x)*(ptBottomLeft.x-ptBottomRight.x) + (ptBottomLeft.y-ptBottomRight.y)*(ptBottomLeft.y-ptBottomRight.y));
		int temp = (int)((ptTopRight.x-ptBottomRight.x)*(ptTopRight.x-ptBottomRight.x) + (ptTopRight.y-ptBottomRight.y)*(ptTopRight.y-ptBottomRight.y));
		if(temp>maxLength) maxLength = temp;

		temp = (int)((ptTopRight.x-ptTopLeft.x)*(ptTopRight.x-ptTopLeft.x) + (ptTopRight.y-ptTopLeft.y)*(ptTopRight.y-ptTopLeft.y));
		if(temp>maxLength) maxLength = temp;

		temp = (int)((ptBottomLeft.x-ptTopLeft.x)*(ptBottomLeft.x-ptTopLeft.x) + (ptBottomLeft.y-ptTopLeft.y)*(ptBottomLeft.y-ptTopLeft.y));
		if(temp>maxLength) maxLength = temp;

		maxLength = (int)Math.sqrt((double)maxLength);

		
		Mat src = new Mat(1,4,CvType.CV_32FC2);
		Mat dst = new Mat(1,4,CvType.CV_32FC2);
		double [] tempArray = {ptTopLeft.x, ptTopLeft.y};
		src.put(0, 0, tempArray);	
		tempArray[0] = 0;
		tempArray[1] = 0;
	    dst.put(0, 0, tempArray);
	    tempArray[0] = ptTopRight.x;
		tempArray[1] = ptTopRight.y;
		src.put(0, 1, tempArray);
		tempArray[0] = maxLength-1;
		tempArray[1] = 0;
		dst.put(0, 1, tempArray);
		tempArray[0] = ptBottomRight.x;
		tempArray[1] = ptBottomRight.y;
		src.put(0, 2, tempArray);
		tempArray[0] = maxLength-1;
		tempArray[1] = maxLength-1;
		dst.put(0, 2, tempArray);
		tempArray[0] = ptBottomLeft.x;
		tempArray[1] = ptBottomLeft.y;
		src.put(0, 3, tempArray);
		tempArray[0] = 0;
		tempArray[1] = maxLength-1;
		dst.put(0, 3, tempArray);

		Mat undistorted = new Mat(new Size(maxLength, maxLength), CvType.CV_8UC1);	
		Imgproc.warpPerspective(original, undistorted, Imgproc.getPerspectiveTransform(src, dst), new Size(maxLength, maxLength));
		Mat resizedUndistorted = new Mat(450, 450, CvType.CV_8UC1); 
		Imgproc.resize(undistorted, resizedUndistorted, resizedUndistorted.size(), 0, 0, Imgproc.INTER_AREA);
		RecognizeDigit testTraining = new RecognizeDigit();
		
		digits = testTraining.identifyDigits(resizedUndistorted);		
		for (int x = 0; x < 9; x++) {
			for (int y = 0; y < 9; y++){
				digits2[x][y] = (int)digits[(x*9)+y];
			}
		}
		
		doesItWork = Solver.solve(0,0,digits2);		
		
	}
	
	public Mat mergeRelatedLines(Mat lines, Mat img) {
		for (int i = 0; i < lines.cols(); i++) {
			if (lines.get(0, i)[0] == 0 && lines.get(0, i)[1] == -100) {
				continue;
			}
			double p1 = lines.get(0, i)[0];
			double theta1 = lines.get(0, i)[1];
			Point pt1current, pt2current;
			if (theta1 > Math.PI * 45 / 180 && theta1 < Math.PI * 135 / 180) {
				pt1current = new Point(0, p1 / Math.sin(theta1));
				pt2current = new Point(img.width(),
						((-img.width()) / Math.tan(theta1)) + p1
								/ Math.sin(theta1));
			} else {
				pt1current = new Point(p1 / Math.cos(theta1), 0);
				pt2current = new Point(((-img.height()) / Math.tan(theta1))
						+ p1 / Math.cos(theta1), img.height());
			}
			for (int j = 0; j < lines.cols(); j++) {
				if (j == i) {
					continue;
				}
				if (Math.abs(lines.get(0, j)[0] - lines.get(0, i)[0]) < 20
						&& Math.abs(lines.get(0, j)[1] - lines.get(0, i)[1]) < Math.PI * 10 / 180) {
					double p = lines.get(0, j)[0];
					double theta = lines.get(0, j)[1];
					Point pt1, pt2;
					if (theta > Math.PI * 45 / 180
							&& theta < Math.PI * 135 / 180) {
						pt1 = new Point(0, p / Math.sin(theta));
						pt2 = new Point(img.width(),
								((-img.width()) / Math.tan(theta)) + p
										/ Math.sin(theta));
					} else {
						pt1 = new Point(p / Math.cos(theta), 0);
						pt2 = new Point(((-img.height()) / Math.tan(theta)) + p
								/ Math.cos(theta), img.height());
					}
					if (((double) (pt1.x - pt1current.x)
							* (pt1.x - pt1current.x) + (pt1.y - pt1current.y)
							* (pt1.y - pt1current.y) < 64 * 64)
							&& ((double) (pt2.x - pt2current.x)
									* (pt2.x - pt2current.x)
									+ (pt2.y - pt2current.y)
									* (pt2.y - pt2current.y) < 64 * 64)) {
						// Merge the two
						double [] temp = {0,0};
						temp[0] = (lines.get(0, i)[0] + lines.get(0,
								j)[0]) / 2;
						temp[1] = (lines.get(0, i)[1] + lines.get(0,
								j)[1]) / 2;
						lines.put(0, i, temp);
						temp[0] = 0;
						temp[1] = -100;
						lines.put(0, j,temp);
					}
				}
			}
		}
		
		return lines;

	}

	public void drawLine(double[] line, Mat img, Scalar rgb) {
		if (line[1] != 0) {
			double m = -1 / Math.tan(line[1]);
			double c = line[0] / Math.sin(line[1]);

			Core.line(img, new Point(0, c),
					new Point(img.size().width, m * img.size().width + c), rgb);
		} else {
			Core.line(img, new Point(line[0], 0), new Point(line[0],
					img.size().height), rgb);
		}
	}
	
}