package com.infinityco.infinitygallery.Activitys;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import com.infinityco.infinitygallery.Adapters.rvAdapterFilters;
import com.infinityco.infinitygallery.Helpers.PhotoEditor.Cropper.CropImageView;
import com.infinityco.infinitygallery.Helpers.PhotoEditor.PictureThread;
import com.infinityco.infinitygallery.Objects.Berreivement_values;
import com.infinityco.infinitygallery.Objects.Media;
import com.infinityco.infinitygallery.R;


import java.io.File;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;


public class PhotoEditorActivity extends Activity {

    private TextView tvBrightnessValue,tvContrastValue,tvFilterName;
    private CropImageView ivImageCut;
    private ImageView ivImage;
    private Media photo;
    private Bitmap savedImage;
    private PictureThread pictureThread;
    private LinearLayout llSliders,llCut,llColors,llFilters,llFilterBar,llFilterBarNeon;
    private Button btCutCut,btCutCancel;
    private RecyclerView rvFilters;
    private SeekBar sbFilterRN,sbFilterGN,sbFilterBN,sbFilter;


    private void setupFullscreen(){
        int currentApiVersion;

        // This work only for android 4.4+
        if(android.os.Build.VERSION.SDK_INT >= 19)
        {
            final int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

            getWindow().getDecorView().setSystemUiVisibility(flags);

            // Code below is to handle presses of Volume up or Volume down.
            // Without this, after pressing volume buttons, the navigation bar will
            // show up and won't hide
            final View decorView = getWindow().getDecorView();
            decorView
                    .setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener()
                    {

                        @Override
                        public void onSystemUiVisibilityChange(int visibility)
                        {
                            if((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0)
                            {
                                decorView.setSystemUiVisibility(flags);
                            }
                        }
                    });
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent iin = getIntent();
        Bundle bundle = iin.getExtras();
        if(bundle!=null) {
            File photoFile = new File((String) bundle.get("path"));
            photo = new Media(this, photoFile.getName(), photoFile);
        }

        if(new Berreivement_values(this).getFullscreenStatus()){
            setupFullscreen();
        }

        setContentView(R.layout.activity_photo_editor);

        initViews();

    }

    private void initViews() {

        //Tools Layout
        llColors = findViewById(R.id.llColors);
        llCut = findViewById(R.id.llCut);
        llSliders = findViewById(R.id.llSliders);
        llFilters = findViewById(R.id.llFilters);
        llFilterBarNeon = findViewById(R.id.llFIlterBarNeon);

        //Views Init
        ivImageCut = findViewById(R.id.ivEditorCut);
        ivImage = findViewById(R.id.ivEditor);
        tvBrightnessValue = findViewById(R.id.txtBrightnessValue);
        tvContrastValue = findViewById(R.id.txtContrastValue);

        //PictreTread Init
        pictureThread = new PictureThread(ivImage,BitmapFactory.decodeFile(photo.getPath()));

        //setUp Image View
        ivImage.setImageBitmap(pictureThread.getBitmap());




        //Brightness and Contrast
        SeekBar sbContrast = findViewById(R.id.sbContrast);
        SeekBar sbBrightness = findViewById(R.id.sbBrightness);

        sbBrightness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                changeContrastBrightness(contrast,((float) i-100));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        sbContrast.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(i<260&i>250){
                    i=255;
                }
                changeContrastBrightness(((float) i)/255,brightness);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //Colors
        SeekBar sbRed = findViewById(R.id.sbRed);
        SeekBar sbGreen = findViewById(R.id.sbGreen);
        SeekBar sbBlue = findViewById(R.id.sbBlue);

        sbRed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(i<105 & i>95){
                    i=100;
                }
                pictureThread.adjustRed(((float)i)/100f);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        sbGreen.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(i<105 & i>95){
                    i=100;
                }
                pictureThread.adjustGreen(((float)i)/100f);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        sbBlue.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(i<150 & i>95){
                    i=100;
                }
                pictureThread.adjustBlue(((float)i)/100f);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //Buttons
        ivImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llFilterBarNeon.setVisibility(View.GONE);
                llFilters.setVisibility(View.GONE);
                llSliders.setVisibility(View.GONE);
                llColors.setVisibility(View.GONE);
            }
        });

        Button btCut = findViewById(R.id.btEditorCut);
        btCut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setUpCutLayout();
            }
        });

        Button btSliders = findViewById(R.id.btEditSliders);
        btSliders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setUpSlidersLayout();
            }
        });

        Button btback = findViewById(R.id.btEditorBack);
        btback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        Button btbackText = findViewById(R.id.btEditorBackText);
        btbackText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        Button btSave = findViewById(R.id.imgSave);
        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveImage();
            }
        });

        Button btColors = findViewById(R.id.btEditorColors);
        btColors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setUpColorLayout();
            }
        });

        Button btFilters = findViewById(R.id.btEditorFilters);
        btFilters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setUpFiltersLayout();
            }
        });

        Button btOpenExtern = findViewById(R.id.btEditorOpenExtern);
        btOpenExtern.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri sendUri2;
                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                        sendUri2 = FileProvider.getUriForFile(
                                PhotoEditorActivity.this,
                                "com.infinityco.infinitygallery.provider", photo.getFile());
                    else {
                        sendUri2 = Uri.fromFile(photo.getFile());
                    }
                    editExtern(sendUri2);
            }
        });

        Button btUndo = findViewById(R.id.imgUndo);
        btUndo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                undoDialog();
            }
        });
    }

    private void editExtern(Uri photoUri){
        Intent editIntent = new Intent(Intent.ACTION_EDIT);
        editIntent.setDataAndType(photoUri, "image/*");
        editIntent.putExtra("mimeType", "image/*");
        editIntent.putExtra("com.infinityco.infinitygallery", getPackageName());
        editIntent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(Intent.createChooser(editIntent, null),1);
    }


    //SetupLayouts----------------------------------------------------------------------------------
    private void setUpCutLayout(){
        if(llCut.getVisibility()==View.VISIBLE){
            llCut.setVisibility(View.GONE);
            ivImageCut.setVisibility(View.GONE);
            if(ivImage.getVisibility()!=View.VISIBLE) {
                ivImage.setVisibility(View.VISIBLE);
            }
        }
        else {
            llCut.setVisibility(View.VISIBLE);
            ivImageCut.setVisibility(View.VISIBLE);
            ivImage.setVisibility(View.GONE);
            ivImageCut.setImageBitmap(pictureThread.getOriginalBitmap());
            if(btCutCut==null||btCutCancel==null){
                btCutCut = findViewById(R.id.btEditorCutCut);
                btCutCancel = findViewById(R.id.btEditorCutCancel);

                btCutCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        setUpCutLayout();
                    }
                });

                btCutCut.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        cropImage();
                        setUpCutLayout();
                    }
                });
            }
        }

        llFilterBarNeon.setVisibility(View.GONE);
        llFilters.setVisibility(View.GONE);
        llColors.setVisibility(View.GONE);
        llSliders.setVisibility(View.GONE);
    }

    private void setUpSlidersLayout(){
        if(llSliders.getVisibility()==View.VISIBLE){
            llSliders.setVisibility(View.GONE);
        }
        else {
            llSliders.setVisibility(View.VISIBLE);
        }

        llFilterBarNeon.setVisibility(View.GONE);
        llFilters.setVisibility(View.GONE);
        llColors.setVisibility(View.GONE);
        ivImageCut.setVisibility(View.GONE);
        llCut.setVisibility(View.GONE);
        if(ivImage.getVisibility()!=View.VISIBLE) {
            ivImage.setVisibility(View.VISIBLE);
        }
    }

    private void setUpColorLayout(){
        if(llColors.getVisibility()==View.VISIBLE){
            llColors.setVisibility(View.GONE);
        }
        else {
            llColors.setVisibility(View.VISIBLE);
        }

        llFilterBarNeon.setVisibility(View.GONE);
        llFilters.setVisibility(View.GONE);
        llSliders.setVisibility(View.GONE);
        ivImageCut.setVisibility(View.GONE);
        llCut.setVisibility(View.GONE);
        if(ivImage.getVisibility()!=View.VISIBLE) {
            ivImage.setVisibility(View.VISIBLE);
        }
    }

    private void setUpFiltersLayout(){
        if(llFilters.getVisibility()==View.VISIBLE){
            llFilters.setVisibility(View.GONE);
        }
        else {
            llFilters.setVisibility(View.VISIBLE);
        }

        if(rvFilters == null){
            rvFilters = findViewById(R.id.rvFilters);

            rvFilters.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            rvFilters.setHasFixedSize(true);

            rvFilters.setAdapter(new rvAdapterFilters(this,pictureThread.getOriginalBitmap()));

            sbFilterRN = findViewById(R.id.sbRedNeon);
            sbFilterGN = findViewById(R.id.sbGreenNeon);
            sbFilterBN = findViewById(R.id.sbBlueNeon);

            llFilterBar = findViewById(R.id.llFIlterBar);
            tvFilterName = findViewById(R.id.tvFilterName);
            sbFilter = findViewById(R.id.sbFilter);

            Button btNeonClose = findViewById(R.id.btEditorFiltersNeonClose);
            btNeonClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    llFilters.setVisibility(View.VISIBLE);
                    llFilterBarNeon.setVisibility(View.GONE);
                }
            });

            ((rvAdapterFilters) rvFilters.getAdapter()).setOnItemClickListener(new rvAdapterFilters.MyClickListener() {
                @Override
                public void onItemClick(final int position, View v) {

                    selectFilter(position);
                }

            });
        }


        llFilterBarNeon.setVisibility(View.GONE);
        llSliders.setVisibility(View.GONE);
        ivImageCut.setVisibility(View.GONE);
        llCut.setVisibility(View.GONE);
        llColors.setVisibility(View.GONE);
        if(ivImage.getVisibility()!=View.VISIBLE) {
            ivImage.setVisibility(View.VISIBLE);
        }
    }



    //Tools-----------------------------------------------------------------------------------------

    //Cut
    private void cropImage(){

        pictureThread.upDateBitmap(ivImageCut.getCroppedImage());

        ivImage.setImageBitmap(pictureThread.getBitmap());

    }

    //ContrastBrightness
    float brightness=0,contrast=1;
    public void changeContrastBrightness(float ct,float bn) {

        brightness = bn;
        contrast = ct;


        pictureThread.changeContrastBrightness(contrast,brightness);



        tvBrightnessValue.setText(brightness+"");
        tvContrastValue.setText(((int)(contrast*100))/10+"");
    }

    //Filters
    private void selectFilter(final int position){
        if(position==3){
            llFilterBarNeon.setVisibility(View.GONE);
            llFilterBar.setVisibility(View.VISIBLE);
            tvFilterName.setText(getString(R.string.PhotoEditorFilterAVERAGE_BLUR));
            sbFilter.setProgress(0);
            sbFilter.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(final SeekBar seekBar) {
                    showLoading(getString(R.string.PhotoEditorApplying));
                    new Timer().schedule(new TimerTask() {

                        @Override
                        public void run() {
                            int[] confg = new int[1];
                            confg[0] = seekBar.getProgress();
                            pictureThread.applayFilter(position,confg);
                            hideLoading();
                        }
                    }, 1);
                }
            });
        }
        else if(position ==4){
            llFilterBarNeon.setVisibility(View.VISIBLE);
            llFilterBar.setVisibility(View.GONE);
            llFilters.setVisibility(View.GONE);
            sbFilterRN.setProgress(100);
            sbFilterGN.setProgress(100);
            sbFilterBN.setProgress(100);

            sbFilterRN.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(final SeekBar seekBar) {
                    showLoading(getString(R.string.PhotoEditorApplying));
                    new Timer().schedule(new TimerTask() {

                        @Override
                        public void run() {
                            int[] confg = new int[3];

                            confg[0] = sbFilterRN.getProgress();
                            confg[1] = sbFilterGN.getProgress();
                            confg[2] = sbFilterBN.getProgress();

                            pictureThread.applayFilter(position,confg);
                            hideLoading();
                        }
                    }, 1);
                }
            });
            sbFilterGN.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(final SeekBar seekBar) {
                    showLoading(getString(R.string.PhotoEditorApplying));
                    new Timer().schedule(new TimerTask() {

                        @Override
                        public void run() {
                            int[] confg = new int[3];

                            confg[0] = sbFilterRN.getProgress();
                            confg[1] = sbFilterGN.getProgress();
                            confg[2] = sbFilterBN.getProgress();

                            pictureThread.applayFilter(position,confg);
                            hideLoading();
                        }
                    }, 1);
                }
            });
            sbFilterBN.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(final SeekBar seekBar) {
                    showLoading(getString(R.string.PhotoEditorApplying));
                    new Timer().schedule(new TimerTask() {

                        @Override
                        public void run() {
                            int[] confg = new int[3];

                            confg[0] = sbFilterRN.getProgress();
                            confg[1] = sbFilterGN.getProgress();
                            confg[2] = sbFilterBN.getProgress();

                            pictureThread.applayFilter(position,confg);
                            hideLoading();
                        }
                    }, 1);
                }
            });
        }
        else if(position==5){
            llFilterBarNeon.setVisibility(View.GONE);
            llFilterBar.setVisibility(View.VISIBLE);
            tvFilterName.setText(getString(R.string.PhotoEditorFilterPIXELATE));
            sbFilter.setProgress(0);
            sbFilter.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(final SeekBar seekBar) {
                    showLoading(getString(R.string.PhotoEditorApplying));
                    new Timer().schedule(new TimerTask() {

                        @Override
                        public void run() {
                            int[] confg = new int[1];
                            confg[0] = seekBar.getProgress();
                            pictureThread.applayFilter(position,confg);
                            hideLoading();
                        }
                    }, 1);
                }
            });
        }
        else if(position==10){
            llFilterBarNeon.setVisibility(View.GONE);
            llFilterBar.setVisibility(View.VISIBLE);
            tvFilterName.setText(getString(R.string.PhotoEditorFilterLIGHT));
            sbFilter.setProgress(0);
            sbFilter.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(final SeekBar seekBar) {
                    showLoading(getString(R.string.PhotoEditorApplying));
                    new Timer().schedule(new TimerTask() {

                        @Override
                        public void run() {
                            int[] confg = new int[1];
                            confg[0] = seekBar.getProgress();
                            pictureThread.applayFilter(position,confg);
                            hideLoading();
                        }
                    }, 1);
                }
            });
        }
        else if(position==16){
            llFilterBarNeon.setVisibility(View.GONE);
            llFilterBar.setVisibility(View.VISIBLE);
            tvFilterName.setText(getString(R.string.PhotoEditorFilterGAUSSIANBLUR));
            sbFilter.setProgress(0);
            sbFilter.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(final SeekBar seekBar) {
                    showLoading(getString(R.string.PhotoEditorApplying));
                    new Timer().schedule(new TimerTask() {

                        @Override
                        public void run() {
                            int[] confg = new int[1];
                            confg[0] = seekBar.getProgress();
                            pictureThread.applayFilter(position,confg);
                            hideLoading();
                        }
                    }, 1);
                }
            });
        }
        else {

            llFilterBarNeon.setVisibility(View.GONE);
            llFilterBar.setVisibility(View.GONE);
            showLoading(getString(R.string.PhotoEditorApplying));
            new Timer().schedule(new TimerTask() {

                @Override
                public void run() {
                    pictureThread.applayFilter(position,null);
                    hideLoading();
                }
            }, 1);

        }
    }

    //Undo
    private void undoDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.PhotoEditorUndoMessage));
        builder.setPositiveButton(getString(R.string.PhotoEditorUndoMessagePositive), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                pictureThread.undo(BitmapFactory.decodeFile(photo.getPath()));
            }
        });

        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.create().show();

    }

    //Progress dialog
    private ProgressDialog mProgressDialog;
    protected void showLoading(@NonNull String message) {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage(message);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
    }

    protected void hideLoading() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }



    private boolean saving = false;
    @SuppressLint("MissingPermission")
    private void saveImage() {
        if(!saving) {
            saving = true;
            showLoading(getString(R.string.savingMensage));
            new Timer().schedule(new TimerTask() {

                @Override
                public void run() {
                    final Berreivement_values bv = new Berreivement_values(PhotoEditorActivity.this);
                    createDirIfNotExists(Environment.getExternalStorageDirectory() + "/" + bv.getEditedPhotoDirectory());

                    final File file = new File(Environment.getExternalStorageDirectory() + "/" + bv.getEditedPhotoDirectory()
                            + File.separator + ""
                            + System.currentTimeMillis() + ".png");

                    try {
                        FileOutputStream fOut = new FileOutputStream(file);

                        pictureThread.getBitmap().compress(Bitmap.CompressFormat.PNG, 85, fOut);
                        fOut.flush();
                        fOut.close();
                        MediaScannerConnection.scanFile(PhotoEditorActivity.this,
                                new String[]{file.getPath()}, null, new MediaScannerConnection.MediaScannerConnectionClient() {
                                    @Override
                                    public void onMediaScannerConnected() {

                                    }

                                    @Override
                                    public void onScanCompleted(String s, Uri uri) {

                                        savedImage = Bitmap.createBitmap(pictureThread.getBitmap());
                                        saving = false;
                                        hideLoading();
                                        //showSnackbar(getString(R.string.savingSucessMensage)+" "+Environment.getExternalStorageDirectory() + "/"+bv.getEditedPhotoDirectory());
                                    }
                                });
                    } catch (Exception e) {
                        saving = false;
                        hideLoading();
                    }
                }
            }, 1);

        }
    }

    private void createDirIfNotExists(String path) {

        File file = new File(path);
        if (!file.exists()) {
            if (!file.mkdirs()) {
                Log.e("TravellerLog :: ", "Problem creating Image folder");
            }
        }
    }

    private void showSaveDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.PhotoEditorSaveMessage));
        builder.setPositiveButton(getString(R.string.Save), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                saveImage();
            }
        });
        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setNeutralButton(getString(R.string.PhotoEditorDiscard), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.create().show();

    }

    private  boolean equals(Bitmap b1, Bitmap b2) {
        if (b1.getWidth() == b2.getWidth() && b1.getHeight() == b2.getHeight()) {
            int[] pixels1 = new int[b1.getWidth() * b1.getHeight()];
            int[] pixels2 = new int[b2.getWidth() * b2.getHeight()];
            b1.getPixels(pixels1, 0, b1.getWidth(), 0, 0, b1.getWidth(), b1.getHeight());
            b2.getPixels(pixels2, 0, b2.getWidth(), 0, 0, b2.getWidth(), b2.getHeight());
            if (Arrays.equals(pixels1, pixels2)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public void onBackPressed() {

        if(!equals(pictureThread.getOriginalBitmap(),pictureThread.getBitmap())){
            if(savedImage==null){
                showSaveDialog();
            }
            else {
                if(!equals(savedImage,pictureThread.getBitmap())){
                    showSaveDialog();
                }
                else {
                    finish();
                }
            }
        }
        else {
            finish();
        }

        llFilterBarNeon.setVisibility(View.GONE);
        llFilters.setVisibility(View.GONE);
        llSliders.setVisibility(View.GONE);
        llCut.setVisibility(View.GONE);
        llColors.setVisibility(View.GONE);

    }
}
