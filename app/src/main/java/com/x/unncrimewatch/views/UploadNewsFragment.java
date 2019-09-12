package com.x.unncrimewatch.views;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.x.unncrimewatch.BuildConfig;
import com.x.unncrimewatch.R;
import com.x.unncrimewatch.adapter.ImagesAdapter;
import com.x.unncrimewatch.model.Image;
import com.x.unncrimewatch.util.FileCompressor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;

public class UploadNewsFragment extends Fragment {

    static final int REQUEST_TAKE_PHOTO = 1;
    static final int REQUEST_GALLERY_PHOTO = 2;
    FileCompressor mCompressor;
    private ImageButton cam;

    private RecyclerView mRecyclerView;
    private ArrayList<Image> mImages = new ArrayList<>();

    private ImagesAdapter mImagesAdapter = new ImagesAdapter(mImages);
    private static final String TAG = "UploadNewsFragment";
    private Bitmap im;
    private Uri photoURI;
    private File mfilepath, mPhotoFile;
    private Object[] mdata = new Object[2];


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle("Upload news");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View layout = inflater.inflate(R.layout.upload_news, container, false);

        mRecyclerView = layout.findViewById(R.id.pic_list);
        mRecyclerView.setAdapter(mImagesAdapter);
        mRecyclerView.setHasFixedSize(true);

        cam = layout.findViewById(R.id.camera);
        cam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();

            }
        });


        // Inflate the layout for this fragment
        return layout;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Context context = view.getContext();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCompressor = new FileCompressor(context);
    }

    /**
     * Alert dialog for capture or select from galley
     */

    public void selectImage() {
        final CharSequence[] items = {
                "Take Photo", "Choose from Library",
                "Cancel"
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(getView().getContext());
        builder.setItems(items, (dialog, item) -> {
            if (items[item].equals("Take Photo")) {
                requestStoragePermission(true);
            } else if (items[item].equals("Choose from Library")) {
                requestStoragePermission(false);
            } else if (items[item].equals("Cancel")) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    /**
     * Capture image from camera
     */
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
                // Error occurred while creating the File
            }
            if (photoFile != null) {
                photoURI = FileProvider.getUriForFile(getView().getContext(),
                        BuildConfig.APPLICATION_ID + ".provider",
                        photoFile);

                mPhotoFile = photoFile;
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    /**
     * Select image fro gallery
     */
    private void dispatchGalleryIntent() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickPhoto.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(pickPhoto, REQUEST_GALLERY_PHOTO);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_TAKE_PHOTO) {
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            mPhotoFile = mCompressor.compressToFile(mPhotoFile);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });

                mdata = new Object[]{photoURI, photoURI};
                new loadImageTask().execute(mdata);

            } else if (requestCode == REQUEST_GALLERY_PHOTO) {
                Uri selectedImage = data.getData();
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        im = getUriImage(selectedImage);
                        mfilepath = saveImage(im);
                    }

                });
                mdata = new Object[]{selectedImage, selectedImage};
                new loadImageTask().execute(mdata);
            }
        }

    }


    /**
     * Requesting multiple permissions (storage and camera) at once
     * This uses multiple permission model from dexter
     * On permanent denial opens settings dialog
     */
    private void requestStoragePermission(boolean isCamera) {
        Dexter.withActivity(getActivity())
                .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            if (isCamera) {
                                dispatchTakePictureIntent();
                            } else {
                                dispatchGalleryIntent();
                            }
                        }
                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions,
                                                                   PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                })
                .withErrorListener(
                        error -> Toast.makeText(getActivity().getApplicationContext(), "Error occurred! ", Toast.LENGTH_SHORT)
                                .show())
                .onSameThread()
                .check();
    }

    /**
     * Showing Alert Dialog with Settings option
     * Navigates user to app settings
     * NOTE: Keep proper title and message depending on your app
     */
    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getView().getContext());
        builder.setTitle("Need Permissions");
        builder.setMessage(
                "This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", (dialog, which) -> {
            dialog.cancel();
            openSettings();
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    // navigating user to app settings
    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }

    /**
     * Create file with current timestamp name
     *
     * @throws IOException
     */
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String mFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File mFile = File.createTempFile(mFileName, ".jpg", storageDir);
        return mFile;
    }


    private Bitmap getUriImage(@NonNull Uri uri) {

        Bitmap image = null;
        try {
            // Use the MediaStore to load the image.
            image = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);

        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "Error: " + e.getMessage() + "Could not open URI: "
                    + uri.toString());
        }


        return image;
    }

    public File saveImage(Bitmap image) {
        final String fileName = UUID.randomUUID().toString() + ".png";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File file = new File(storageDir, fileName);

        try (FileOutputStream fos = new FileOutputStream(file)) {
            image.compress(Bitmap.CompressFormat.PNG, 85, fos);
        } catch (IOException e) {
            Log.e(TAG, "Error during saving of image: " + e.getMessage());
            return null;
        }

        return file;

    }


    private class loadImageTask extends AsyncTask<Object, Void, Bitmap> {

        Bitmap image = null;
        Uri uri, uriSource;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        protected Bitmap doInBackground(Object... obj) {
            uri = (Uri) obj[1];
            uriSource = (Uri) obj[0];
            try {
                // Use the MediaStore to load the image.
                image = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);

            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG, "Error: " + e.getMessage() + "Could not open URI: "
                        + uri.toString());
            }

            return null;
        }


        protected void onPostExecute(Bitmap resultimage) {

            mImages.add(new Image(uriSource, image));
            mImagesAdapter.notifyDataSetChanged();
            mRecyclerView.smoothScrollToPosition(mImagesAdapter.getItemCount() - 1);


        }
    }

}

