// secondary activity
package com.example.ocrsudoku;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import android.support.v7.app.ActionBarActivity;

import org.opencv.ml.CvSVM;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;

public class DisplayMessageActivity extends ActionBarActivity {
	String[] str1 = { "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-",
			"-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-",
			"-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-",
			"-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-",
			"-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-",
			"-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-",
			"-", "-", "-", "-", "-" };
	String[] str;
	Sudoku s1;
	String[] answers = new String[81];
	static final Map<String, Integer> VALUES_BY_NAME = new HashMap<>();
	static {
		VALUES_BY_NAME.put("textView0", R.id.textView0);
		VALUES_BY_NAME.put("textView1", R.id.textView1);
		VALUES_BY_NAME.put("textView2", R.id.textView2);
		VALUES_BY_NAME.put("textView3", R.id.textView3);
		VALUES_BY_NAME.put("textView4", R.id.textView4);
		VALUES_BY_NAME.put("textView5", R.id.textView5);
		VALUES_BY_NAME.put("textView6", R.id.textView6);
		VALUES_BY_NAME.put("textView7", R.id.textView7);
		VALUES_BY_NAME.put("textView8", R.id.textView8);
		VALUES_BY_NAME.put("textView9", R.id.textView9);
		VALUES_BY_NAME.put("textView10", R.id.textView10);
		VALUES_BY_NAME.put("textView11", R.id.textView11);
		VALUES_BY_NAME.put("textView12", R.id.textView12);
		VALUES_BY_NAME.put("textView13", R.id.textView13);
		VALUES_BY_NAME.put("textView14", R.id.textView14);
		VALUES_BY_NAME.put("textView15", R.id.textView15);
		VALUES_BY_NAME.put("textView16", R.id.textView16);
		VALUES_BY_NAME.put("textView17", R.id.textView17);
		VALUES_BY_NAME.put("textView18", R.id.textView18);
		VALUES_BY_NAME.put("textView19", R.id.textView19);
		VALUES_BY_NAME.put("textView20", R.id.textView20);
		VALUES_BY_NAME.put("textView21", R.id.textView21);
		VALUES_BY_NAME.put("textView22", R.id.textView22);
		VALUES_BY_NAME.put("textView23", R.id.textView23);
		VALUES_BY_NAME.put("textView24", R.id.textView24);
		VALUES_BY_NAME.put("textView25", R.id.textView25);
		VALUES_BY_NAME.put("textView26", R.id.textView26);
		VALUES_BY_NAME.put("textView27", R.id.textView27);
		VALUES_BY_NAME.put("textView28", R.id.textView28);
		VALUES_BY_NAME.put("textView29", R.id.textView29);
		VALUES_BY_NAME.put("textView30", R.id.textView30);
		VALUES_BY_NAME.put("textView31", R.id.textView31);
		VALUES_BY_NAME.put("textView32", R.id.textView32);
		VALUES_BY_NAME.put("textView33", R.id.textView33);
		VALUES_BY_NAME.put("textView34", R.id.textView34);
		VALUES_BY_NAME.put("textView35", R.id.textView35);
		VALUES_BY_NAME.put("textView36", R.id.textView36);
		VALUES_BY_NAME.put("textView37", R.id.textView37);
		VALUES_BY_NAME.put("textView38", R.id.textView38);
		VALUES_BY_NAME.put("textView39", R.id.textView39);
		VALUES_BY_NAME.put("textView40", R.id.textView40);
		VALUES_BY_NAME.put("textView41", R.id.textView41);
		VALUES_BY_NAME.put("textView42", R.id.textView42);
		VALUES_BY_NAME.put("textView43", R.id.textView43);
		VALUES_BY_NAME.put("textView44", R.id.textView44);
		VALUES_BY_NAME.put("textView45", R.id.textView45);
		VALUES_BY_NAME.put("textView46", R.id.textView46);
		VALUES_BY_NAME.put("textView47", R.id.textView47);
		VALUES_BY_NAME.put("textView48", R.id.textView48);
		VALUES_BY_NAME.put("textView49", R.id.textView49);
		VALUES_BY_NAME.put("textView50", R.id.textView50);
		VALUES_BY_NAME.put("textView51", R.id.textView51);
		VALUES_BY_NAME.put("textView52", R.id.textView52);
		VALUES_BY_NAME.put("textView53", R.id.textView53);
		VALUES_BY_NAME.put("textView54", R.id.textView54);
		VALUES_BY_NAME.put("textView55", R.id.textView55);
		VALUES_BY_NAME.put("textView56", R.id.textView56);
		VALUES_BY_NAME.put("textView57", R.id.textView57);
		VALUES_BY_NAME.put("textView58", R.id.textView58);
		VALUES_BY_NAME.put("textView59", R.id.textView59);
		VALUES_BY_NAME.put("textView60", R.id.textView60);
		VALUES_BY_NAME.put("textView61", R.id.textView61);
		VALUES_BY_NAME.put("textView62", R.id.textView62);
		VALUES_BY_NAME.put("textView63", R.id.textView63);
		VALUES_BY_NAME.put("textView64", R.id.textView64);
		VALUES_BY_NAME.put("textView65", R.id.textView65);
		VALUES_BY_NAME.put("textView66", R.id.textView66);
		VALUES_BY_NAME.put("textView67", R.id.textView67);
		VALUES_BY_NAME.put("textView68", R.id.textView68);
		VALUES_BY_NAME.put("textView69", R.id.textView69);
		VALUES_BY_NAME.put("textView70", R.id.textView70);
		VALUES_BY_NAME.put("textView71", R.id.textView71);
		VALUES_BY_NAME.put("textView72", R.id.textView72);
		VALUES_BY_NAME.put("textView73", R.id.textView73);
		VALUES_BY_NAME.put("textView74", R.id.textView74);
		VALUES_BY_NAME.put("textView75", R.id.textView75);
		VALUES_BY_NAME.put("textView76", R.id.textView76);
		VALUES_BY_NAME.put("textView77", R.id.textView77);
		VALUES_BY_NAME.put("textView78", R.id.textView78);
		VALUES_BY_NAME.put("textView79", R.id.textView79);
		VALUES_BY_NAME.put("textView80", R.id.textView80);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_display_message);
		LinearLayout ll = (LinearLayout) findViewById(R.id.linearLayout1);
		Point size = new Point();
		this.getWindowManager().getDefaultDisplay().getSize(size);
		int screenHeight = size.y;
		ll.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT, screenHeight / 12));
		LinearLayout sll0 = (LinearLayout) findViewById(R.id.sudokuLinearLayout0);
		sll0.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT, screenHeight / 12));
		LinearLayout sll1 = (LinearLayout) findViewById(R.id.sudokuLinearLayout1);
		sll1.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT, screenHeight / 12));
		LinearLayout sll2 = (LinearLayout) findViewById(R.id.sudokuLinearLayout2);
		sll2.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT, screenHeight / 12));
		LinearLayout sll3 = (LinearLayout) findViewById(R.id.sudokuLinearLayout3);
		sll3.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT, screenHeight / 12));
		LinearLayout sll4 = (LinearLayout) findViewById(R.id.sudokuLinearLayout4);
		sll4.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT, screenHeight / 12));
		LinearLayout sll5 = (LinearLayout) findViewById(R.id.sudokuLinearLayout5);
		sll5.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT, screenHeight / 12));
		LinearLayout sll6 = (LinearLayout) findViewById(R.id.sudokuLinearLayout6);
		sll6.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT, screenHeight / 12));
		LinearLayout sll7 = (LinearLayout) findViewById(R.id.sudokuLinearLayout7);
		sll7.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT, screenHeight / 12));
		LinearLayout sll8 = (LinearLayout) findViewById(R.id.sudokuLinearLayout8);
		sll8.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT, screenHeight / 12));

		Intent intent = getIntent();
		String message = intent.getStringExtra(MainActivity.FILE_PATH);
		File root = Environment.getExternalStorageDirectory();
		str = str1;
		if (!RecognizeDigit.trained1) {
			File file1 = new File(root, "svmmodelwithdigits.yaml");
			CvSVM Sd1 = new CvSVM();
			Sd1.load(file1.getAbsolutePath());
			RecognizeDigit.SVM1 = Sd1;
			RecognizeDigit.trained1 = true;
		}
		if (!RecognizeDigit.trained2) {
			File file2 = new File(root, "svmmodelwithoutdigits.yaml");
			CvSVM Sd2 = new CvSVM();
			Sd2.load(file2.getAbsolutePath());
			RecognizeDigit.SVM2 = Sd2;
			RecognizeDigit.trained2 = true;
		}
		s1 = new Sudoku();
		s1.doHough(message);
		float[] floatTemp = s1.getDigits();
		
		for (int i = 0; i < 81; i++) {
			if (Integer.toString((int)floatTemp[i]).equals("0")) {
				str[i] = "-";
			} else {
				str[i] = Integer.toString((int)floatTemp[i]);
			}
		}
		updateTextView();

		if (s1.getDIW()) {
			int[][] intTemp = s1.getDigits2();
			for (int x = 0; x < 9; x++) {
				for (int y = 0; y < 9; y++) {
					answers[(x * 9) + y] = Integer.toString(intTemp[x][y]);
				}
			}
		} else {

			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					this);

			// set title
			alertDialogBuilder.setTitle("Error");

			// set dialog message
			alertDialogBuilder
					.setMessage("Could not find solution!")
					.setCancelable(false)
					.setNegativeButton("OK",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									// if this button is clicked, just close
									// the dialog box and do nothing
									dialog.cancel();
								}
							});

			// create alert dialog
			AlertDialog alertDialog = alertDialogBuilder.create();

			// show it
			alertDialog.show();
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void updateTextView() {
		String s = "";
		for (int i = 0; i < 81; i++) {
			s = "textView" + Integer.toString(i);
			TextView ts = (TextView) findViewById(VALUES_BY_NAME.get(s));
			ts.setText(str[i]);
			s = "";
		}

	}

	public void solvePuzzle(View view) {
		if (s1.getDIW()) {
			str = answers;
			updateTextView();
		}
	}

	public void solveHint(View view) {
		if (s1.getDIW() && !answers.equals(str)) {
			for (int i = 0; i < str.length; i++) {
				if (str[i] == "-") {
					str[i] = answers[i];
					break;
				}
			}
			updateTextView();
		}

	}

	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_display_message,
					container, false);

			return rootView;
		}
	}

}
