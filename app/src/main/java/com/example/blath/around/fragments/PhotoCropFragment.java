package com.example.blath.around.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.blath.around.R;
import com.example.blath.around.activities.ProfileActivity;
import com.example.blath.around.commons.Utils.UIUtils;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.app.Activity.RESULT_OK;

public class PhotoCropFragment extends Fragment implements LoaderManager.LoaderCallbacks<PhotoCropFragment.LoadPhotoAsyncLoaderDataHolder> {
    private final static String LOG_TAG = PhotoCropFragment.class.getSimpleName();

    // Static final constants
    private static final int DEFAULT_ASPECT_RATIO_VALUES = 10;
    private static final int ROTATE_NINETY_DEGREES = 90;
    private static final String INTENT_URI = "intent_uri";

    public Bitmap mCroppedImage = null;
    private boolean mSaving;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_photo_crop, container, false);
    }

    private final static int PHOTO_LOADER_ID = 1;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Context context = getActivity();
        if (context == null) {
            return;
        }
        View rootView = getView();
        if (rootView == null) {
            return;
        }
        Intent intent = getActivity().getIntent();
        Uri uri = intent.getExtras().getParcelable(ProfileActivity.INTENT_EXTRA_FRAGMENT_ARGS);
        if (uri != null) {
            Bundle args = new Bundle();
            args.putParcelable(INTENT_URI, uri);
            getLoaderManager().initLoader(PHOTO_LOADER_ID, args, this);
        }
    }

    public static class LoadPhotoAsyncLoaderDataHolder {
        public Bitmap mBitmap;
        public Uri mUri;

        public LoadPhotoAsyncLoaderDataHolder(Bitmap bitmap, Uri uri) {
            mBitmap = bitmap;
            mUri = uri;
        }
    }

    @Override
    public Loader<LoadPhotoAsyncLoaderDataHolder> onCreateLoader(int id, Bundle args) {
        return new PhotoAsyncTaskLoader(getActivity(), ((Uri) args.get(INTENT_URI)));
    }

    @Override
    public void onLoadFinished(Loader<LoadPhotoAsyncLoaderDataHolder> loader, LoadPhotoAsyncLoaderDataHolder dataHolder) {
        View rootView = getView();
        if (rootView == null) {
            return;
        }
        if (dataHolder == null || dataHolder.mBitmap == null || dataHolder.mUri == null) {
            return;
        }
        Bitmap selectedBitmap = dataHolder.mBitmap;
        Uri uri = dataHolder.mUri;
        // the initial rotation of the photo.
        // need to get this info from exif.
        int rotateDegrees = 0;
        try {
            // calculate the rotation from exif
            // later we need to rotate the cropImageView based on the rotateDegrees
            ExifInterface exifInterface = new ExifInterface(uri.getPath());
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotateDegrees = ROTATE_NINETY_DEGREES;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotateDegrees = 2 * ROTATE_NINETY_DEGREES;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotateDegrees = 3 * ROTATE_NINETY_DEGREES;
                    break;
            }
        } catch (IOException exp) {
//            if (BuildConfig.DEBUG) {
//                Log.e(LOG_TAG, exp.getMessage());
//            }
        }
        final CropImageView cropImageView = (CropImageView) rootView.findViewById(R.id.CropImageView);

        cropImageView.setImageBitmap(selectedBitmap);
        cropImageView.setVisibility(View.VISIBLE);

        cropImageView.setAspectRatio(DEFAULT_ASPECT_RATIO_VALUES, DEFAULT_ASPECT_RATIO_VALUES);
        cropImageView.setFixedAspectRatio(true);
        cropImageView.rotateImage(rotateDegrees);
        cropImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        setupCallbacks(cropImageView, uri);
    }

    @Override
    public void onLoaderReset(Loader<LoadPhotoAsyncLoaderDataHolder> loader) {
    }

    private static class PhotoAsyncTaskLoader extends AsyncTaskLoader<LoadPhotoAsyncLoaderDataHolder> {
        private Uri mUri;
        private Bitmap mBitmap;

        public PhotoAsyncTaskLoader(Context context, Uri uri) {
            super(context);
            mUri = uri;
        }

        @Override
        public void deliverResult(LoadPhotoAsyncLoaderDataHolder dataHolder) {
            if (isReset()) {
                return;
            }
            mBitmap = dataHolder.mBitmap;
            super.deliverResult(dataHolder);
        }

        @Override
        public LoadPhotoAsyncLoaderDataHolder loadInBackground() {
            if (getContext() == null) {
                return null;
            }
            mBitmap = UIUtils.getBitmap(mUri, getContext().getContentResolver());
            return new LoadPhotoAsyncLoaderDataHolder(mBitmap, mUri);
        }

        @Override
        protected void onStartLoading() {
            if (mBitmap != null) {
                deliverResult(new LoadPhotoAsyncLoaderDataHolder(mBitmap, mUri));
            }
            if (takeContentChanged() || mBitmap == null) {
                forceLoad();
            }
        }

        @Override
        protected void onStopLoading() {
            cancelLoad();
        }

        @Override
        protected void onReset() {
            super.onReset();
            onStartLoading();
            mBitmap = null;
        }
    }

    private void setupCallbacks(final CropImageView cropImageView, final Uri uri) {
        View rootView = getView();
        if (rootView == null) {
            return;
        }
        final TextView cropButton = (TextView) rootView.findViewById(R.id.crop_photo);
        cropButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSaving) return;
                mSaving = true;
                try {
                    mCroppedImage = cropImageView.getCroppedImage();
                } catch (IllegalArgumentException e) {
                    // This crash is from the 3rd party library.
                    // Let's catch the exception and not crash the app
                    Log.e(LOG_TAG, "Illegal argument");
                    return;
                }
                Activity activity = getActivity();
                if (activity == null) {
                    return;
                }
                //Test storing the image and getting the url perfect
                saveProfileImageInDirectory();
                Intent returnedUri = new Intent();
                returnedUri.putExtra(ProfileActivity.INTENT_EXTRA_FRAGMENT_ARGS, uri);
                activity.setResult(RESULT_OK, returnedUri);
                activity.finish();
            }
        });
        final TextView rotateButton = (TextView) rootView.findViewById(R.id.rotate_photo);
        rotateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cropImageView.rotateImage(ROTATE_NINETY_DEGREES);
            }
        });
    }

    public void saveProfileImageInDirectory(){
//        ByteArrayOutputStream stream = new ByteArrayOutputStream();
//        mCroppedImage.compress(Bitmap.CompressFormat.JPEG, 100, stream);
//        byte[] byteArray = stream.toByteArray();


        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/around/profile_images");
        myDir.mkdirs();
        String fname = "testing.jpg";
        File file = new File (myDir, fname);
        if (file.exists ())
            file.delete ();
        try {
            FileOutputStream out = new FileOutputStream(file);
            mCroppedImage.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
