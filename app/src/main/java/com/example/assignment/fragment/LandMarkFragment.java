package com.example.assignment.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Camera;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraX;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureConfig;
import androidx.camera.core.Preview;
import androidx.camera.core.PreviewConfig;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.util.Rational;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.assignment.R;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import static android.content.ContentValues.TAG;

public class LandMarkFragment extends Fragment {
    private int REQUEST_CODE_PERMISSIONS = 10;
    private String[] REQUIRED_PERMISSIONS = new String[]{"android.permission.CAMERA", "android.permission.WRITE_EXTERNAL_STORAGE"};
    TextureView textureView;
    ImageButton button;
    TextView test;
    private FirebaseFunctions mFunctions;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_land_mark, container, false);
        textureView = root.findViewById(R.id.viewFinder);
        test = root.findViewById(R.id.test);
        button = root.findViewById(R.id.camera_capture_button);
        if (allPermissionGranted()) {
            starCamera();
        } else {
            ActivityCompat.requestPermissions(getActivity(), REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
        }
        return root;
    }

    private void starCamera() {
        CameraX.unbindAll();
        Rational aspectRatio = new Rational(textureView.getWidth(), textureView.getHeight());
        Size screen = new Size(textureView.getWidth(), textureView.getHeight());
        PreviewConfig previewConfig = new PreviewConfig.Builder().setTargetAspectRatio(aspectRatio).setTargetResolution(screen).build();
        Preview preview = new Preview(previewConfig);
        preview.setOnPreviewOutputUpdateListener(new Preview.OnPreviewOutputUpdateListener() {
            @Override
            public void onUpdated(Preview.PreviewOutput output) {
                ViewGroup viewGroup = (ViewGroup) textureView.getParent();
                viewGroup.removeView(textureView);
                viewGroup.addView(textureView);
                textureView.setSurfaceTexture(output.getSurfaceTexture());
                updateTransform();
            }
        });
        ImageCaptureConfig imageCaptureConfig = new ImageCaptureConfig.Builder().setCaptureMode(ImageCapture.CaptureMode.MIN_LATENCY).
                setTargetRotation(getActivity().getWindowManager().getDefaultDisplay().getRotation()).build();
        final ImageCapture imageCapture = new ImageCapture(imageCaptureConfig);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                File file = new File(path, System.currentTimeMillis() + ".jpg");
                imageCapture.takePicture(file, new ImageCapture.OnImageSavedListener() {
                    @Override
                    public void onImageSaved(@NonNull File file) {
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), Uri.fromFile(file));
                            bitmap = scaleBitmapDown(bitmap, 640);
                            // Convert bitmap to base64 encoded string
                            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                            byte[] imageBytes = byteArrayOutputStream.toByteArray();
                            String base64encoded = Base64.encodeToString(imageBytes, Base64.NO_WRAP);
                            mFunctions = FirebaseFunctions.getInstance();
// Create json request to cloud vision
                            JsonObject request = new JsonObject();
// Add image to request
                            JsonObject image = new JsonObject();
                            image.add("content", new JsonPrimitive(base64encoded));
                            request.add("image", image);
//Add features to the request
                            JsonObject feature = new JsonObject();
                            feature.add("maxResults", new JsonPrimitive(5));
                            feature.add("type", new JsonPrimitive("LANDMARK_DETECTION"));
                            JsonArray features = new JsonArray();
                            features.add(feature);
                            request.add("features", features);
                            annotateImage(request.toString())
                                    .addOnCompleteListener(new OnCompleteListener<JsonElement>() {
                                        @Override
                                        public void onComplete(@NonNull Task<JsonElement> task) {
                                            if (!task.isSuccessful()) {
                                                test.setText(String.valueOf(Uri.fromFile(file)));

                                            } else {
                                                for (JsonElement label : task.getResult().getAsJsonArray().get(0).getAsJsonObject().get("landmarkAnnotations").getAsJsonArray()) {
                                                    JsonObject labelObj = label.getAsJsonObject();
                                                    String landmarkName = labelObj.get("description").getAsString();
                                                    String entityId = labelObj.get("mid").getAsString();
                                                    float score = labelObj.get("score").getAsFloat();
                                                    JsonObject bounds = labelObj.get("boundingPoly").getAsJsonObject();
                                                    // Multiple locations are possible, e.g., the location of the depicted
                                                    // landmark and the location the picture was taken.
                                                    for (JsonElement loc : labelObj.get("locations").getAsJsonArray()) {
                                                        JsonObject latLng = loc.getAsJsonObject().get("latLng").getAsJsonObject();
                                                        double latitude = latLng.get("latitude").getAsDouble();
                                                        double longitude = latLng.get("longitude").getAsDouble();
                                                    }
                                                }

                                            }
                                        }
                                    });

                        } catch (IOException e) {
                            e.printStackTrace();
                            test.setText("IOException");

                        }
                    }

                    @Override
                    public void onError(@NonNull ImageCapture.UseCaseError useCaseError, @NonNull String message, @Nullable Throwable cause) {
                        if (cause != null) {
                            test.setText("on99");

                        }
                    }
                });
            }
        });
        CameraX.bindToLifecycle(this, preview, imageCapture);
    }

    private Task<JsonElement> annotateImage(String requestJson) {
        return mFunctions
                .getHttpsCallable("annotateImage")
                .call(requestJson)
                .continueWith(new Continuation<HttpsCallableResult, JsonElement>() {
                    @Override
                    public JsonElement then(@NonNull Task<HttpsCallableResult> task) {
                        // This continuation runs on either success or failure, but if the task
                        // has failed then getResult() will throw an Exception which will be
                        // propagated down.
                        return JsonParser.parseString(new Gson().toJson(task.getResult().getData()));
                    }
                });
    }

    private Bitmap scaleBitmapDown(Bitmap bitmap, int maxDimension) {
        int originalWidth = bitmap.getWidth();
        int originalHeight = bitmap.getHeight();
        int resizedWidth = maxDimension;
        int resizedHeight = maxDimension;

        if (originalHeight > originalWidth) {
            resizedHeight = maxDimension;
            resizedWidth = (int) (resizedHeight * (float) originalWidth / (float) originalHeight);
        } else if (originalWidth > originalHeight) {
            resizedWidth = maxDimension;
            resizedHeight = (int) (resizedWidth * (float) originalHeight / (float) originalWidth);
        } else if (originalHeight == originalWidth) {
            resizedHeight = maxDimension;
            resizedWidth = maxDimension;
        }
        return Bitmap.createScaledBitmap(bitmap, resizedWidth, resizedHeight, false);
    }

    private void updateTransform() {
        Matrix matrix = new Matrix();
        float w = textureView.getMeasuredWidth();
        float h = textureView.getMeasuredHeight();
        float cx = w / 2f;
        float cy = h / 2f;
        int rotationDgr;
        int rotation = (int) textureView.getRotation();

        switch (rotation) {
            case Surface.ROTATION_0:
                rotationDgr = 0;
                break;
            case Surface.ROTATION_90:
                rotationDgr = 90;
                break;
            case Surface.ROTATION_180:
                rotationDgr = 180;
                break;
            case Surface.ROTATION_270:
                rotationDgr = 270;
                break;
            default:
                return;
        }
        matrix.postRotate((float) rotationDgr, cx, cy);
        textureView.setTransform(matrix);
    }

    private boolean allPermissionGranted() {
        for (String permission : REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(getContext(), permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }
}