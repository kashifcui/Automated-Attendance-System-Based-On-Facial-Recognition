package com.atharvakale.facerecognition.moduleclasses;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.YuvImage;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.util.Pair;
import android.util.Size;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.airbnb.lottie.animation.content.Content;
import com.atharvakale.facerecognition.AdopterClass.PresentAdapter;
import com.atharvakale.facerecognition.AttandnessDashBoard;
import com.atharvakale.facerecognition.ModelClasses.Present;
import com.atharvakale.facerecognition.PresentItemClickListner;
import com.atharvakale.facerecognition.R;
import com.atharvakale.facerecognition.databinding.ActivityMainBinding;
import com.atharvakale.facerecognition.listener.SimilarityClassifier;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.face.Face;
import com.google.mlkit.vision.face.FaceDetection;
import com.google.mlkit.vision.face.FaceDetector;
import com.google.mlkit.vision.face.FaceDetectorOptions;

import org.tensorflow.lite.Interpreter;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.ReadOnlyBufferException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity implements PresentItemClickListner {


    private RecyclerView listRecyclerView1;
    private RecyclerView.LayoutManager listLayoutManager;
    ArrayList<String> arrayList = new ArrayList<>();
    ArrayList<String> arrayList_roll = new ArrayList<>();

    private static final String TAG = "MainActivity";
    FaceDetector detector;

    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    PreviewView previewView;
    ImageView face_preview;
    Interpreter tfLite;
    TextView reco_name, preview_info, textAbove_preview;
    Button recognize, save_attance, camera_switch, actions;
    ImageButton add_face;
    CameraSelector cameraSelector;
    boolean developerMode = false;
    float distance = 1.0f;
    boolean start = true, flipX = false;
    Context context = MainActivity.this;
    int cam_face = CameraSelector.LENS_FACING_BACK; //Default Back Camera

    FirebaseFirestore firestore;
    int[] intValues;
    int inputSize = 112;  //Input size for model
    boolean isModelQuantized = false;
    float[][] embeedings;
    float IMAGE_MEAN = 128.0f;
    float IMAGE_STD = 128.0f;
    int OUTPUT_SIZE = 192; //Output size of model
    private static int SELECT_PICTURE = 1;
    ProcessCameraProvider cameraProvider;
    private static final int MY_CAMERA_REQUEST_CODE = 100;

    String modelFile = "mobile_face_net.tflite"; //model name

    private HashMap<String, SimilarityClassifier.Recognition> registered = new HashMap<>();
    private List<HashMap<String, SimilarityClassifier.Recognition>> registeredList = new ArrayList<>();
    private HashMap<String, String> registeredList2 = new HashMap<>();
    private ActivityMainBinding binding;

    String courseSt, classSt, courid, currentdate, currenttime;
    private int mYear, mMonth, mDay, mHour, mMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(binding.getRoot());
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        save_attance = findViewById(R.id.btn_save_attandness);
        Intent intent = getIntent();
        courseSt = intent.getStringExtra("course");
        classSt = intent.getStringExtra("myclass");
        courid = intent.getStringExtra("courseid");
        firestore = FirebaseFirestore.getInstance();
        reco_name = binding.textView;
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat timeforemate = new SimpleDateFormat("HH:mm:ss");
        currentdate = sdf.format(c.getTime());
        currenttime = timeforemate.format(c.getTime());
        readFromFirestore(new RegisterFaceActivity.FirestoreCallback() {
            @Override
            public void onDataLoaded(HashMap<String, SimilarityClassifier.Recognition> data) {
                Log.d(TAG, "onDataLoaded: data loadeed");
                registered.putAll(data);
            }

            @Override
            public void onDataLoadFailed(Exception e) {

            }
        });
//        binding.floatingActionButton.setOnClickListener(v -> {
//            Intent intent = new Intent(this, RegisterFaceActivity.class);
//            startActivity(intent);
//        });
        binding.btnCameraSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cam_face == CameraSelector.LENS_FACING_BACK) {
                    cam_face = CameraSelector.LENS_FACING_FRONT;
                    flipX = true;
                } else {
                    cam_face = CameraSelector.LENS_FACING_BACK;
                    flipX = false;
                }
                cameraProvider.unbindAll();
                cameraBind();
            }
        });

        save_attance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PickDate();

            }
        });
        try {
            tfLite = new Interpreter(loadModelFile(MainActivity.this, modelFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Initialize Face Detector
        FaceDetectorOptions highAccuracyOpts =
                new FaceDetectorOptions.Builder()
                        .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
                        .build();
        detector = FaceDetection.getClient(highAccuracyOpts);
        binding.btnRecognize.setOnClickListener(v -> {
            try {
                cameraBind();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        cameraBind();
    }
    public void  saveAttandendnessFirebase()
    {
                        HashMap<String,String> datasaved=new HashMap<>();
                for (int i=0;i<arrayList.size();i++)
                {
                    datasaved.put("name",arrayList.get(i));
                    datasaved.put("rollno",arrayList_roll.get(i));
                    datasaved.put("calssName",classSt);
                    datasaved.put("courseId",courseSt);
                    datasaved.put("date",mYear+"-"+mMonth+"-"+mDay);
                    datasaved.put("time",mHour+":"+mMinute);
                }
                firestore.collection("attandenseMark").document(classSt).collection(courseSt)
                        .add(datasaved).addOnSuccessListener(
                        new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Toast.makeText(context, "Attadness Saved" , Toast.LENGTH_SHORT).show();
                                Intent i=new Intent(MainActivity.this, AttandnessDashBoard.class);
                                i.putExtra("myclass", classSt);
                                i.putExtra("course", courseSt);
                                i.putExtra("courseid", courid);
                                startActivity(i);
                            }
                        }
                ).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, ""+e, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void cameraBind() {
        cameraProviderFuture = ProcessCameraProvider.getInstance(this);

        previewView = findViewById(R.id.previewView);
        cameraProviderFuture.addListener(() -> {
            try {
                cameraProvider = cameraProviderFuture.get();
                bindPreview(cameraProvider);
            } catch (ExecutionException | InterruptedException e) {

            }
        }, ContextCompat.getMainExecutor(this));
    }

    public void PickDate() {
        // Get Current Date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        mDay = dayOfMonth;
                        mMonth = monthOfYear;
                        mYear = year;
                        timePicker();
                        Toast.makeText(context, "Year Select:" + mYear + "Month Select: " + mMonth, Toast.LENGTH_SHORT).show();
//                     txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
//     Toast.makeText(context, "Year Select:"+mYear+"Month Select: "+mMonth, Toast.LENGTH_SHORT).show();
    }

    public void timePicker() {
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        mHour=hourOfDay;
                        mMinute=minute;
                        saveAttandendnessFirebase();
                        Toast.makeText(context, "Hours:" + mHour + "mint: " + mMinute, Toast.LENGTH_SHORT).show();
//
//                        txtTime.setText(hourOfDay + ":" + minute);
                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }

    public void readFromFirestore(final RegisterFaceActivity.FirestoreCallback callback) {
        firestore.collection("UserData").document(classSt).collection(courseSt).get()
                .addOnSuccessListener(querySnapshot -> {
                    for (QueryDocumentSnapshot documentSnapshot : querySnapshot) {
                        // Access the document ID
                        if (documentSnapshot.exists()) {
                            String distance = documentSnapshot.getString("distance");
                            String course = documentSnapshot.getString("course");
                            String id = documentSnapshot.getString("id");
                            String extra = documentSnapshot.getString("extra");
                            String rollno = documentSnapshot.getString("rollno");
                            String className  = documentSnapshot.getString("className");
                                SimilarityClassifier.Recognition recognition = new SimilarityClassifier.Recognition(
                                        id,
                                        course,
                                        distance,
                                        extra,
                                        rollno,
                                        className
                                );
                                HashMap<String, SimilarityClassifier.Recognition> hashMap = new HashMap<>();
                                hashMap.put(id, recognition);
                          /*  recyclerList.add(hashMap);
                            setupRecycler();*/
//                            if (className.equals(classSt) && rollno.equals(courseSt)) {
                                registeredList.add(hashMap);
                                registeredList2.put(id,rollno);

//                            }
                                callback.onDataLoaded(hashMap);
                            }
                        }

                        // Pass the retrieved data to the callback

                })
                .addOnFailureListener(e -> {
                    // Handle the failure case
                    callback.onDataLoadFailed(e);
                    System.out.println("Failed to retrieve documents from collection: " + e.getMessage());
                });

    }

    void bindPreview(@NonNull ProcessCameraProvider cameraProvider) {
        Preview preview = new Preview.Builder().build();
        cameraSelector = new CameraSelector.Builder().requireLensFacing(cam_face).build();
        preview.setSurfaceProvider(previewView.getSurfaceProvider());
        ImageAnalysis imageAnalysis =
                new ImageAnalysis.Builder().setTargetResolution(new Size(640, 480))
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST) //Latest frame is shown
                        .build();

        Executor executor = Executors.newSingleThreadExecutor();
        imageAnalysis.setAnalyzer(executor, new ImageAnalysis.Analyzer() {
            @Override
            public void analyze(@NonNull ImageProxy imageProxy) {
                try {
                    Thread.sleep(0);  //Camera preview refreshed every 10 millisec(adjust as required)
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                InputImage image = null;


                @SuppressLint("UnsafeExperimentalUsageError")
                // Camera Feed-->Analyzer-->ImageProxy-->mediaImage-->InputImage(needed for ML kit face detection)

                Image mediaImage = imageProxy.getImage();

                if (mediaImage != null) {
                    image = InputImage.fromMediaImage(mediaImage, imageProxy.getImageInfo().getRotationDegrees());
//                    System.out.println("Rotation "+imageProxy.getImageInfo().getRotationDegrees());
                }

//                System.out.println("ANALYSIS");

                //Process acquired image to detect faces
                Task<List<Face>> result =
                        detector.process(image)
                                .addOnSuccessListener(
                                        new OnSuccessListener<List<Face>>() {
                                            @Override
                                            public void onSuccess(List<Face> faces) {
                                                if (faces.size() != 0) {
                                                    Face face = faces.get(0); //Get first face from detected faces
//                                                    System.out.println(face);
                                                    //mediaImage to Bitmap
                                                    Bitmap frame_bmp = toBitmap(mediaImage);
                                                    int rot = imageProxy.getImageInfo().getRotationDegrees();
                                                    //Adjust orientation of Face
                                                    Bitmap frame_bmp1 = rotateBitmap(frame_bmp, rot, false, false);

                                                    //Get bounding box of face
                                                    RectF boundingBox = new RectF(face.getBoundingBox());

                                                    //Crop out bounding box from whole Bitmap(image)
                                                    Bitmap cropped_face = getCropBitmapByCPU(frame_bmp1, boundingBox);

                                                    if (flipX)
                                                        cropped_face = rotateBitmap(cropped_face, 0, flipX, false);
                                                    //Scale the acquired Face to 112*112 which is required input for model
                                                    Bitmap scaled = getResizedBitmap(cropped_face, 112, 112);

                                                    if (start)
                                                        recognizeImage(scaled); //Send scaled bitmap to create face embeddings.
//                                                    System.out.println(boundingBox);

                                                } else {
                                                    if (registered.isEmpty())
                                                        reco_name.setText("Add Face");
                                                    else
                                                        reco_name.setText("No Face Detected!");
                                                }

                                            }
                                        })
                                .addOnFailureListener(
                                        new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                // Task failed with an exception
                                                // ...
                                            }
                                        })
                                .addOnCompleteListener(new OnCompleteListener<List<Face>>() {
                                    @Override
                                    public void onComplete(@NonNull Task<List<Face>> task) {

                                        imageProxy.close(); //v.important to acquire next frame for analysis
                                    }
                                });


            }
        });


        try{
            cameraProvider.bindToLifecycle((LifecycleOwner) this, cameraSelector, imageAnalysis, preview);
        }catch (Exception e){
            Log.d(TAG, "bindPreview: exception: "+e.getMessage());
        }


    }

    public void recognizeImage(final Bitmap bitmap) {

        // set Face to Preview
        //  face_preview.setImageBitmap(bitmap);

        //Create ByteBuffer to store normalized image

        ByteBuffer imgData = ByteBuffer.allocateDirect(1 * inputSize * inputSize * 3 * 4);

        imgData.order(ByteOrder.nativeOrder());

        intValues = new int[inputSize * inputSize];

        //get pixel values from Bitmap to normalize
        bitmap.getPixels(intValues, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());

        imgData.rewind();

        for (int i = 0; i < inputSize; ++i) {
            for (int j = 0; j < inputSize; ++j) {
                int pixelValue = intValues[i * inputSize + j];
                if (isModelQuantized) {
                    // Quantized model
                    imgData.put((byte) ((pixelValue >> 16) & 0xFF));
                    imgData.put((byte) ((pixelValue >> 8) & 0xFF));
                    imgData.put((byte) (pixelValue & 0xFF));
                } else { // Float model
                    imgData.putFloat((((pixelValue >> 16) & 0xFF) - IMAGE_MEAN) / IMAGE_STD);
                    imgData.putFloat((((pixelValue >> 8) & 0xFF) - IMAGE_MEAN) / IMAGE_STD);
                    imgData.putFloat(((pixelValue & 0xFF) - IMAGE_MEAN) / IMAGE_STD);

                }
            }
        }
        //imgData is input to our model
        Object[] inputArray = {imgData};

        Map<Integer, Object> outputMap = new HashMap<>();


        embeedings = new float[1][OUTPUT_SIZE]; //output of model will be stored in this variable

        outputMap.put(0, embeedings);

        tfLite.runForMultipleInputsOutputs(inputArray, outputMap); //Run model


        float distance_local = Float.MAX_VALUE;
        String id = "0";
        String label = "?";

        //Compare new face with saved Faces.

            if (registered.size() > 0) {

                final List<Pair<String, Float>> nearest = findNearest(embeedings[0],registered);//Find 2 closest matching face

                if (nearest.get(0) != null) {

                    final String name = nearest.get(0).first; //get name and distance of closest matching face
                    // label = name;
                    distance_local = nearest.get(0).second;
                    if (developerMode) {
                        if (distance_local < distance) //If distance between Closest found face is more than 1.000 ,then output UNKNOWN face.
                            reco_name.setText("Nearest: " + name + "\nDist: " + String.format("%.3f", distance_local) + "\n2nd Nearest: " + nearest.get(1).first + "\nDist: " + String.format("%.3f", nearest.get(1).second));
                        else
                            reco_name.setText("Unknown " + "\nDist: " + String.format("%.3f", distance_local) + "\nNearest: " + name + "\nDist: " + String.format("%.3f", distance_local) + "\n2nd Nearest: " + nearest.get(1).first + "\nDist: " + String.format("%.3f", nearest.get(1).second));

//                    System.out.println("nearest: " + name + " - distance: " + distance_local);
                    } else {
                        if (distance_local < distance) {
                            //If distance between Closest found face is more than 1.000 ,then output UNKNOWN face.
                            reco_name.setText(name);
//                            Toast.makeText(context, ""+name, Toast.LENGTH_SHORT).show();
                            if (arrayList.contains(name)){

                            }

                            else {
                                String rollno= registeredList2.get(name);
//                                arrayList.add(new Present(name));
                                arrayList.add(name);
                                arrayList_roll.add(rollno);
                                Collections.reverse(arrayList);
                                listRecyclerView1 = findViewById(R.id.presentRecyclerView);
                                listRecyclerView1.setHasFixedSize(true);
                                listLayoutManager = new LinearLayoutManager(MainActivity.this);
                                PresentAdapter presentAdapter = new PresentAdapter(arrayList, MainActivity.this,MainActivity.this);
                                listRecyclerView1.setAdapter(presentAdapter);
                                listRecyclerView1.setLayoutManager(listLayoutManager);
                                presentAdapter.notifyDataSetChanged();
                            }
                        }
                        else
                            reco_name.setText("Unknown");
//                    System.out.println("nearest: " + name + " - distance: " + distance_local);
                    }


                }
            }


//            final int numDetectionsOutput = 1;
//            final ArrayList<SimilarityClassifier.Recognition> recognitions = new ArrayList<>(numDetectionsOutput);
//            SimilarityClassifier.Recognition rec = new SimilarityClassifier.Recognition(
//                    id,
//                    label,
//                    distance);
//
//            recognitions.add( rec );

    }
//    public void register(String name, SimilarityClassifier.Recognition rec) {
//        registered.put(name, rec);
//    }

    //Compare Faces by distance between face embeddings
    private List<Pair<String, Float>> findNearest(float[] emb, HashMap<String, SimilarityClassifier.Recognition> registered) {
        Log.d(TAG, "findNearest: ");
        List<Pair<String, Float>> neighbour_list = new ArrayList<Pair<String, Float>>();
        Pair<String, Float> ret = null; //to get closest match
        Pair<String, Float> prev_ret = null; //to get second closest match
        for (Map.Entry<String, SimilarityClassifier.Recognition> entry : registered.entrySet()) {

            final String name = entry.getKey();
            Log.d(TAG, "findNearest: name: "+name);
            final float[] knownEmb = (Utils.convertToArray(entry.getValue().getExtra()))[0];

            float distance = 0;
            for (int i = 0; i < emb.length; i++) {
                float diff = emb[i] - knownEmb[i];
                distance += diff * diff;
            }
            distance = (float) Math.sqrt(distance);
            if (ret == null || distance < ret.second) {
                prev_ret = ret;
                ret = new Pair<>(name, distance);
            }
        }
        if (prev_ret == null) prev_ret = ret;
        neighbour_list.add(ret);
        neighbour_list.add(prev_ret);

        return neighbour_list;

    }

    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }

    private static Bitmap getCropBitmapByCPU(Bitmap source, RectF cropRectF) {
        Bitmap resultBitmap = Bitmap.createBitmap((int) cropRectF.width(),
                (int) cropRectF.height(), Bitmap.Config.ARGB_8888);
        Canvas cavas = new Canvas(resultBitmap);

        // draw background
        Paint paint = new Paint(Paint.FILTER_BITMAP_FLAG);
        paint.setColor(Color.WHITE);
        cavas.drawRect(
                new RectF(0, 0, cropRectF.width(), cropRectF.height()),
                paint);

        Matrix matrix = new Matrix();
        matrix.postTranslate(-cropRectF.left, -cropRectF.top);

        cavas.drawBitmap(source, matrix, paint);

        if (source != null && !source.isRecycled()) {
            source.recycle();
        }

        return resultBitmap;
    }

    private static Bitmap rotateBitmap(
            Bitmap bitmap, int rotationDegrees, boolean flipX, boolean flipY) {
        Matrix matrix = new Matrix();

        // Rotate the image back to straight.
        matrix.postRotate(rotationDegrees);

        // Mirror the image along the X or Y axis.
        matrix.postScale(flipX ? -1.0f : 1.0f, flipY ? -1.0f : 1.0f);
        Bitmap rotatedBitmap =
                Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

        // Recycle the old bitmap if it has changed.
        if (rotatedBitmap != bitmap) {
            bitmap.recycle();
        }
        return rotatedBitmap;
    }

    //IMPORTANT. If conversion not done ,the toBitmap conversion does not work on some devices.
    private static byte[] YUV_420_888toNV21(Image image) {

        int width = image.getWidth();
        int height = image.getHeight();
        int ySize = width * height;
        int uvSize = width * height / 4;

        byte[] nv21 = new byte[ySize + uvSize * 2];

        ByteBuffer yBuffer = image.getPlanes()[0].getBuffer(); // Y
        ByteBuffer uBuffer = image.getPlanes()[1].getBuffer(); // U
        ByteBuffer vBuffer = image.getPlanes()[2].getBuffer(); // V

        int rowStride = image.getPlanes()[0].getRowStride();
        assert (image.getPlanes()[0].getPixelStride() == 1);

        int pos = 0;

        if (rowStride == width) { // likely
            yBuffer.get(nv21, 0, ySize);
            pos += ySize;
        } else {
            long yBufferPos = -rowStride; // not an actual position
            for (; pos < ySize; pos += width) {
                yBufferPos += rowStride;
                yBuffer.position((int) yBufferPos);
                yBuffer.get(nv21, pos, width);
            }
        }

        rowStride = image.getPlanes()[2].getRowStride();
        int pixelStride = image.getPlanes()[2].getPixelStride();

        assert (rowStride == image.getPlanes()[1].getRowStride());
        assert (pixelStride == image.getPlanes()[1].getPixelStride());

        if (pixelStride == 2 && rowStride == width && uBuffer.get(0) == vBuffer.get(1)) {
            // maybe V an U planes overlap as per NV21, which means vBuffer[1] is alias of uBuffer[0]
            byte savePixel = vBuffer.get(1);
            try {
                vBuffer.put(1, (byte) ~savePixel);
                if (uBuffer.get(0) == (byte) ~savePixel) {
                    vBuffer.put(1, savePixel);
                    vBuffer.position(0);
                    uBuffer.position(0);
                    vBuffer.get(nv21, ySize, 1);
                    uBuffer.get(nv21, ySize + 1, uBuffer.remaining());

                    return nv21; // shortcut
                }
            } catch (ReadOnlyBufferException ex) {
                // unfortunately, we cannot check if vBuffer and uBuffer overlap
            }

            // unfortunately, the check failed. We must save U and V pixel by pixel
            vBuffer.put(1, savePixel);
        }

        // other optimizations could check if (pixelStride == 1) or (pixelStride == 2),
        // but performance gain would be less significant

        for (int row = 0; row < height / 2; row++) {
            for (int col = 0; col < width / 2; col++) {
                int vuPos = col * pixelStride + row * rowStride;
                nv21[pos++] = vBuffer.get(vuPos);
                nv21[pos++] = uBuffer.get(vuPos);
            }
        }

        return nv21;
    }

    private Bitmap toBitmap(Image image) {

        byte[] nv21 = YUV_420_888toNV21(image);


        YuvImage yuvImage = new YuvImage(nv21, ImageFormat.NV21, image.getWidth(), image.getHeight(), null);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        yuvImage.compressToJpeg(new Rect(0, 0, yuvImage.getWidth(), yuvImage.getHeight()), 75, out);

        byte[] imageBytes = out.toByteArray();
        //System.out.println("bytes"+ Arrays.toString(imageBytes));

        //System.out.println("FORMAT"+image.getFormat());

        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
    }


    //Load Faces from Shared Preferences.Json String to Recognition object


    //Load Photo from phone storage
    private void loadphoto() {
        start = false;
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
    }

    //Similar Analyzing Procedure
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                try {
                    InputImage impphoto = InputImage.fromBitmap(getBitmapFromUri(selectedImageUri), 0);
                    detector.process(impphoto).addOnSuccessListener(new OnSuccessListener<List<Face>>() {
                        @Override
                        public void onSuccess(List<Face> faces) {

                            if (faces.size() != 0) {
                                recognize.setText("Recognize");
                                add_face.setVisibility(View.VISIBLE);
                                reco_name.setVisibility(View.INVISIBLE);
                                face_preview.setVisibility(View.VISIBLE);
                                preview_info.setText("1.Bring Face in view of Camera.\n\n2.Your Face preview will appear here.\n\n3.Click Add button to save face.");
                                Face face = faces.get(0);
//                                System.out.println(face);

                                //write code to recreate bitmap from source
                                //Write code to show bitmap to canvas

                                Bitmap frame_bmp = null;
                                try {
                                    frame_bmp = getBitmapFromUri(selectedImageUri);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                Bitmap frame_bmp1 = rotateBitmap(frame_bmp, 0, flipX, false);

                                //face_preview.setImageBitmap(frame_bmp1);


                                RectF boundingBox = new RectF(face.getBoundingBox());


                                Bitmap cropped_face = getCropBitmapByCPU(frame_bmp1, boundingBox);

                                Bitmap scaled = getResizedBitmap(cropped_face, 112, 112);
                                // face_preview.setImageBitmap(scaled);

                                recognizeImage(scaled);

//                                System.out.println(boundingBox);
                                try {
                                    Thread.sleep(100);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            start = true;
                            Toast.makeText(context, "Failed to add", Toast.LENGTH_SHORT).show();
                        }
                    });
                    face_preview.setImageBitmap(getBitmapFromUri(selectedImageUri));
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        }
    }

    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    private MappedByteBuffer loadModelFile(Activity activity, String MODEL_FILE) throws IOException {
        AssetFileDescriptor fileDescriptor = activity.getAssets().openFd(MODEL_FILE);
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }

    @Override
    public void deleteSingleItem(int i) {
        arrayList.remove(i);
        PresentAdapter presentAdapter = new PresentAdapter(arrayList, MainActivity.this,MainActivity.this);
        listRecyclerView1.setAdapter(presentAdapter);
        listRecyclerView1.setLayoutManager(listLayoutManager);
        presentAdapter.notifyDataSetChanged();
    }
}