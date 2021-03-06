package com.dulcerefugio.app.entunombre.activities;

import java.io.File;
import java.math.BigInteger;
import java.security.SecureRandom;

import android.content.ActivityNotFoundException;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ProgressBar;
import com.dulcerefugio.app.entunombre.R;
import com.dulcerefugio.app.entunombre.logic.BitmapProcessor;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class GeneratePictureActivity extends Activity {

    //=============================CONSTANTS======================================
    private final String TAG = "GENERATE_PICTURE_ACTIVITY";
	protected static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 123443;
    private static final int PIC_CROP = 34556;

    //=============================FIELDS======================================
    static String cameraPhotoImagePath = "";
    //static String cameraCropImagePath = "";
    private static String randomNumber = "";
    //private static String cropRandomNumber = "";
    public static String saveFolderName;
    private static boolean mImageIsGenerated=false;
    private static boolean isTablet = false;

    //=============================OBJECTS======================================
    private static File imageFile;
    //private static File cropImageFile;
    private static File wallpaperDirectory;
    private static Uri picUri;
    private static BitmapProcessor mBitmapProcessor;
    private static Bitmap picture;
    //private static Bitmap result;
    private static ImageView mImageResult;
    private static ProgressBar mProgressBar;
    //private File finalImage;
    private Button btnShare;
    private Button btnAgain;


    //=============================OVERRIDEN METHODS======================================
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_generate_picture);

        initialize();

        //if(!mImageIsGenerated) {
            //initCamera();
        //}
        /*else {
            if (result != null) {
                mImageResult.setImageBitmap(result);
                mImageResult.setVisibility(View.VISIBLE);
                mProgressBar.setVisibility(View.GONE);
            }else{
                initCamera();
            }
        }*/
	}

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {

                //Getting the pic uri
                picUri = Uri.fromFile(imageFile);

                //Performing the crop
                //performCrop();

                //Opening Cropper Activity

            }else if(requestCode == PIC_CROP){
                //get the returned data
                Bundle extras = data.getExtras();

                //get the cropped bitmap
                //picture = extras.getParcelable("data");
                //picture = mBitmapProcessor.decodeSampledBitmapFromFile(cropImageFile, frame.getWidth(), frame.getHeight() - 200);
            }
        }
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.generate_picture, menu);
		return true;
	}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                mImageIsGenerated = false;
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        mImageIsGenerated = false;
    }

    //=============================METHODS======================================
	public void initialize(){
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setDisplayShowHomeEnabled(true);

		mImageResult = (ImageView) findViewById(R.id.ivMerge);
		mBitmapProcessor = new BitmapProcessor();
        mProgressBar = (ProgressBar) findViewById(R.id.pbGeneratePicture);

        saveFolderName = Environment.getExternalStorageDirectory()
                .getAbsolutePath()
                + "/entunombre";

        //randomNumber = String.valueOf(nextSessionId());
        //cropRandomNumber = String.valueOf(nextSessionId());
        cameraPhotoImagePath = saveFolderName + "/" + randomNumber + ".jpg";
        //cameraCropImagePath = saveFolderName + "/" + cropRandomNumber + ".jpg";
        imageFile = new File(cameraPhotoImagePath);
        //cropImageFile = new File(cameraCropImagePath);
        isTablet = getResources().getBoolean(R.bool.isLargeScreen);

        wallpaperDirectory = new File(saveFolderName);

        if (!wallpaperDirectory.exists())
            wallpaperDirectory.mkdirs();

        //Log.d(TAG, " str_Camera_Photo_ImagePath  " + cameraPhotoImagePath);

        mImageResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //final Uri path = Uri.fromFile(finalImage);

                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                //intent.setDataAndType(path, "image/*");
                startActivity(intent);
            }
        });

        btnShare= (Button) findViewById(R.id.open);
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("image/jpeg");
                //share.putExtra(Intent.EXTRA_STREAM,Uri.fromFile(finalImage));
                startActivity(Intent.createChooser(share, "Share Image"));
            }
        });

        /*btnAgain= (Button) findViewById(R.id.doAgain);
        btnAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(GeneratePictureActivity.this, MainActivity.class);
                startActivity(i);
                GeneratePictureActivity.this.finish();
            }
        });*/
	}

}
