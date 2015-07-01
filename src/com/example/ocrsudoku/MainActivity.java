package com.example.ocrsudoku;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.opencv.android.OpenCVLoader;

import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.provider.MediaStore;
import android.content.Intent;
import android.database.Cursor;

public class MainActivity extends ActionBarActivity {
	String mCurrentPhotoPath;
	public final static String FILE_PATH = "com.example.ocrsudoku.PATH";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);		
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		
		if (!OpenCVLoader.initDebug()) {
	    }
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
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
	
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
	    super.onActivityResult(requestCode, resultCode, data); 

	    switch(requestCode) { 
	    case 1000:
	        if(resultCode == RESULT_OK){  
	            Uri selectedImage = data.getData();
	            String[] filePathColumn = {MediaStore.Images.Media.DATA};

	            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
	            cursor.moveToFirst();

	            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
	            String filePath = cursor.getString(columnIndex);
	            cursor.close();

	            Intent pictureIntent = new Intent(this, DisplayMessageActivity.class);
	            pictureIntent.putExtra(FILE_PATH, filePath);
	        	startActivity(pictureIntent);
	        }
	        break;
	    case 1001:
	        if(resultCode == RESULT_OK){  
	        	//galleryAddPic();
	        	Intent pictureIntent = new Intent(this, DisplayMessageActivity.class);
	        	pictureIntent.putExtra(FILE_PATH, mCurrentPhotoPath);
	        	startActivity(pictureIntent);
	        }
	        break;
	    }

	};
	
	public void loadGallery(View view) {
		Intent i = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
     final int ACTIVITY_SELECT_IMAGE = 1000;
     startActivityForResult(i, ACTIVITY_SELECT_IMAGE); 
	}
	
	public void takePicture(View view) {
		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    	final int ACTIVITY_TAKE_PICTURE = 1001;
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
        	File photoFile = null;
        	 try {
                 photoFile = createImageFile();
             } catch (IOException ex) {                 
            	 Log.w("TEST", "We got an exception");
             }
             // Continue only if the File was successfully created
             if (photoFile != null) {
                 takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                         Uri.fromFile(photoFile));                 
                 startActivityForResult(takePictureIntent, ACTIVITY_TAKE_PICTURE);
             }
        }
	}
	
	private File createImageFile() throws IOException {
	    // Create an image file name
	    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
	    String imageFileName = "JPEG_" + timeStamp + "_";	  
	    File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
	    if (!storageDir.exists()){
	    	storageDir.mkdirs();
	    }
	    File image = File.createTempFile(
	        imageFileName,  /* prefix */
	        ".jpg",         /* suffix */
	        storageDir      /* directory */
	    );
	    // Save a file: path for use with ACTION_VIEW intents
	   // mCurrentPhotoPath = "file:" + image.getAbsolutePath();
	    mCurrentPhotoPath = image.getAbsolutePath();
	    return image;
	}
	
	/*
	private void galleryAddPic() {
	    Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
	    File f = new File(mCurrentPhotoPath);
	    Uri contentUri = Uri.fromFile(f);
	    mediaScanIntent.setData(contentUri);
	    this.sendBroadcast(mediaScanIntent);
	}	
	*/

	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			
			return rootView;
		}
	}

}
