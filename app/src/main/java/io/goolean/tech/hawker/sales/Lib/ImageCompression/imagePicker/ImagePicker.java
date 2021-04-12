package io.goolean.tech.hawker.sales.Lib.ImageCompression.imagePicker;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.goolean.tech.hawker.sales.Lib.ImageCompression.imageCompression.ImageCompression;
import io.goolean.tech.hawker.sales.Lib.ImageCompression.imageCompression.ImageCompressionListener;
import io.goolean.tech.hawker.sales.Location.Location_Update;
import io.goolean.tech.hawker.sales.Storage.SharedPrefrence_Seller;
import io.goolean.tech.hawker.sales.View.HomeActivity;


public class ImagePicker {
    private Activity activity;
    private Fragment fragment;
    private boolean isCompress = true, isCamera = true, isGallery = true;
    public static final int SELECT_IMAGE = 121;
    private ImageCompressionListener imageCompressionListener;
    private String latlon;
    Location_Update location_update;

    public ImagePicker withActivity(Activity activity) {
        this.activity = activity;
        location_update = new Location_Update(activity);
        return this;
    }

    public ImagePicker withFragment(Fragment fragment) {
        this.fragment = fragment;
        return this;
    }

    public ImagePicker chooseFromCamera(boolean isCamera) {
        this.isCamera = isCamera;
        return this;
    }

    public ImagePicker chooseFromGallery(boolean isGallery) {
        this.isGallery = isGallery;
        return this;
    }

    public ImagePicker withCompression(boolean isCompress) {
        this.isCompress = isCompress;
        return this;
    }

    public void start() {
        if (activity != null && fragment != null) {
            throw new IllegalStateException("Cannot add both activity and fragment");
        } else if (activity == null && fragment == null) {
            throw new IllegalStateException("Activity and fragment both are null");
        } else {
            if (!checkPermission()) {
                throw new IllegalStateException("Write External Permission not found");
            } else {
                if (!isCamera && !isGallery) {
                    throw new IllegalStateException("select source to pick image");
                } else {
                    if (activity != null)
                        activity.startActivityForResult(getPickImageChooserIntent(), SELECT_IMAGE);
                    else
                        fragment.startActivityForResult(getPickImageChooserIntent(), SELECT_IMAGE);
                }
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private boolean checkPermission() {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        return currentAPIVersion < Build.VERSION_CODES.M || ContextCompat.checkSelfPermission(activity != null ? activity : fragment.getActivity(), android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    private Intent getPickImageChooserIntent() {

        // Determine Uri of camera image to save.
        Uri outputFileUri = getCaptureImageOutputUri();

        List<Intent> allIntents = new ArrayList<>();
        PackageManager packageManager = activity != null ? activity.getPackageManager() : fragment.getActivity().getPackageManager();

        if (isCamera) {
            // collect all camera intents
            Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            captureIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
            List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
            for (ResolveInfo res : listCam) {
                Intent intent = new Intent(captureIntent);
                intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
                intent.setPackage(res.activityInfo.packageName);
                if (outputFileUri != null) {
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
                }
                allIntents.add(intent);
            }
        }

     /*   if (isGallery) {
            // collect all gallery intents
            Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
            galleryIntent.setType("image/*");
            List<ResolveInfo> listGallery = packageManager.queryIntentActivities(galleryIntent, 0);
            for (ResolveInfo res : listGallery) {
                Intent intent = new Intent(galleryIntent);
                intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
                intent.setPackage(res.activityInfo.packageName);
                allIntents.add(intent);
            }
        }*/

        Intent mainIntent = allIntents.get(allIntents.size() - 1);
        for (Intent intent : allIntents) {
            if (intent.getComponent().getClassName().equals("com.android.documentsui.DocumentsActivity")) {
                mainIntent = intent;
                break;
            }
        }
        allIntents.remove(mainIntent);

        // Create a chooser from the main intent
        Intent chooserIntent = Intent.createChooser(mainIntent, "Select source");

        // Add all other intents
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, allIntents.toArray(new Parcelable[allIntents.size()]));

        return chooserIntent;
    }

    private Uri getCaptureImageOutputUri() {
        Uri outputFileUri = null;
        File getImage = activity != null ? activity.getExternalFilesDir("") : fragment.getActivity().getExternalFilesDir("");
        if (getImage != null) {
            if ((Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT)){
                outputFileUri = FileProvider.getUriForFile(activity != null ? activity : fragment.getActivity(),
                        activity != null ? activity.getApplicationContext().getPackageName() + ".provider" : fragment.getActivity().getApplicationContext().getPackageName() + ".provider",
                        new File(getImage.getPath(), "profile.png"));
            }else {
                outputFileUri = Uri.fromFile(new File(getImage.getPath(), "profile.png"));
            }
        }
        return outputFileUri;
    }

    private String getPickImageResultFilePath(Intent data) {
        boolean isCamera = data == null || data.getData() == null;
        //Log.e("data", +"");
        /*if (data != null) {
            isCamera = false;
            String action = data.getAction();
            isCamera = action != null && action.equals(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        } else {
            isCamera = true;
        }*/

        //Log.e("isCamera", isCamera ? "true" : "false");
        if (isCamera) return getCaptureImageOutputUri().getPath();
        else return getRealPathFromURI(data.getData());
        //return isCamera ? getCaptureImageOutputUri() : data.getData();
    }

    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Audio.Media.DATA};
        Cursor cursor = activity != null ?
                activity.getContentResolver().query(contentUri, proj, null, null, null) : fragment.getActivity().getContentResolver().query(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    public String getImageFilePath(Intent data) {
        if (!isCompress)
            return getPickImageResultFilePath(data);
        else if (getPickImageResultFilePath(data) != null) {
            latlon = ReadExif(getPickImageResultFilePath(data));
            if(latlon == null){
                SharedPrefrence_Seller.saveIMAGELATITUDELOGITUDE(activity,location_update.LATTITUDE+","+location_update.LONGITUDE);
            }else if(latlon.equals("-0.0")){
                SharedPrefrence_Seller.saveIMAGELATITUDELOGITUDE(activity,location_update.LATTITUDE+","+location_update.LONGITUDE);
            }else {
                SharedPrefrence_Seller.saveIMAGELATITUDELOGITUDE(activity,latlon);
            }
            new ImageCompression(activity != null ? activity : fragment.getActivity(), getPickImageResultFilePath(data), imageCompressionListener).execute();

        }
        return null;
    }

    public void addOnCompressListener(ImageCompressionListener imageCompressionListener) {
        this.imageCompressionListener = imageCompressionListener;
    }

    public String ReadExif(String file) {
        String exif="Exif: " + file;
        try {
            ExifInterface exifInterface = new ExifInterface(file);
            exif += "\n IMAGE_LENGTH: " + exifInterface.getAttribute(ExifInterface.TAG_IMAGE_LENGTH);
            exif += "\n IMAGE_WIDTH: " + exifInterface.getAttribute(ExifInterface.TAG_IMAGE_WIDTH);
            exif += "\n DATETIME: " + exifInterface.getAttribute(ExifInterface.TAG_DATETIME);
            exif += "\n TAG_MAKE: " + exifInterface.getAttribute(ExifInterface.TAG_MAKE);
            exif += "\n TAG_MODEL: " + exifInterface.getAttribute(ExifInterface.TAG_MODEL);
            exif += "\n TAG_ORIENTATION: " + exifInterface.getAttribute(ExifInterface.TAG_ORIENTATION);
            exif += "\n TAG_WHITE_BALANCE: " + exifInterface.getAttribute(ExifInterface.TAG_WHITE_BALANCE);
            exif += "\n TAG_FOCAL_LENGTH: " + exifInterface.getAttribute(ExifInterface.TAG_FOCAL_LENGTH);
            exif += "\n TAG_FLASH: " + exifInterface.getAttribute(ExifInterface.TAG_FLASH);
            exif += "\n TAG_LOT: " + exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE);
            exif += "\n TAG_LON: " + exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);
            exif += "\n GPS related:";

            float[] LatLong = new float[2];
            if(exifInterface.getLatLong(LatLong)){
                exif += "\n latitude= " + LatLong[0];
                exif += "\n longitude= " + LatLong[1];
                latlon = LatLong[0]+","+LatLong[1];
            }else{
                exif += "Exif tags are not available!";
            }
            // Toast.makeText(HomeActivity.this, "finished", Toast.LENGTH_LONG).show();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return latlon;

    }
}
