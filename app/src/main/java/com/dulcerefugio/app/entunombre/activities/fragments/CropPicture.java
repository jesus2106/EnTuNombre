package com.dulcerefugio.app.entunombre.activities.fragments;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.dulcerefugio.app.entunombre.R;
import com.dulcerefugio.app.entunombre.logic.BitmapProcessor;
import com.edmodo.cropper.CropImageView;
import com.orhanobut.logger.Logger;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.InstanceState;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.io.IOException;

/**
 * Created by eperez on 8/15/15.
 */

@EFragment(R.layout.f_cropper)
public class CropPicture extends Fragment {

    //======================================================
    //                      CONSTANTS
    //======================================================
    private static final int DEFAULT_ASPECT_RATIO_VALUES = 20;
    public static final String PICTURE_PATH_EXTRA = "PICTURE_PATH_EXTRA";

    //======================================================
    //                      FIELDS
    //======================================================
    private Context mContext;
    private onCropPictureListener mListener;
    @InstanceState
    public int mAspectRatioX = DEFAULT_ASPECT_RATIO_VALUES;
    @InstanceState
    public int mAspectRatioY = DEFAULT_ASPECT_RATIO_VALUES;
    @FragmentArg(PICTURE_PATH_EXTRA)
    public String mPicturePath;
    @ViewById(R.id.f_cropper_civ_crop)
    public CropImageView mCropImageView;
    @ViewById(R.id.f_cropper_iv_cancel)
    public ImageView mIvCancel;
    @ViewById(R.id.f_cropper_iv_crop)
    public ImageView mIvCrop;
    @ViewById(R.id.f_cropper_iv_rotate)
    public ImageView mIvRotate;
    private boolean mIsImageCrop;
    private Bitmap mBitmap;

    //======================================================
    //                    CONSTRUCTORS
    //======================================================

    //======================================================
    //                  OVERRIDEN METHODS
    //======================================================
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mContext = getActivity();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (onCropPictureListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement onCropPictureListener");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mCropImageView!=null){
            mCropImageView.setImageBitmap(null);
            mBitmap.recycle();
            mBitmap = null;
            System.gc();
        }
    }

    //======================================================
    //                      METHODS
    //======================================================

    @AfterViews
    public void initialize() {
        mCropImageView.setFixedAspectRatio(true);
        mCropImageView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        mCropImageView.setAspectRatio(DEFAULT_ASPECT_RATIO_VALUES, DEFAULT_ASPECT_RATIO_VALUES);

        mIvCrop.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!mIsImageCrop) {
                            mIsImageCrop = true;
                            Bitmap bitmap = mCropImageView.getCroppedImage();
                            mCropImageView.setImageBitmap(null);
                            mListener.onCropImage(bitmap);
                            bitmap = null;
                            System.gc();
                        }
                    }
                });
        mIvCancel.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListener.onCropCancel();
                    }
                });

        mIvRotate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCropImageView.rotateImage(90);
            }
        });

        if (mPicturePath != null) {
            Uri uri = Uri.fromFile(new File(mPicturePath));
            mBitmap = fromGallery(uri);
            mCropImageView.setImageBitmap(mBitmap);
            mCropImageView.requestLayout();
        }else{
            Toast.makeText(getActivity(), "No se puede mostrar esta imagen", Toast.LENGTH_LONG).show();
            getActivity().finish();
        }
    }

    private Bitmap fromGallery(final Uri selectedImageUri) {
        try {
            Bitmap bm = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImageUri);

            ExifInterface exif = new ExifInterface(selectedImageUri.getPath());
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

            int angle = 0;
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    angle = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    angle = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    angle = 270;
                    break;
                default:
                    angle = 0;
                    break;
            }
            Matrix mat = new Matrix();
            if (angle == 0 && bm.getWidth() > bm.getHeight())
                mat.postRotate(90);
            else
                mat.postRotate(angle);

            return Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), mat, true);

        } catch (IOException e) {
            Log.e("", "-- Error in setting image");
        } catch (OutOfMemoryError oom) {
            Log.e("", "-- OOM Error in setting image");
        }
        return null;
    }

    public interface onCropPictureListener {
        void onCropImage(Bitmap croppedImage);

        void onCropCancel();
    }
}
