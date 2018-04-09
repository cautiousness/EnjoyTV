package com.fuj.enjoytv.activity.qrcode;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.fuj.enjoytv.R;
import com.fuj.enjoytv.base.BaseFragment;
import com.fuj.enjoytv.qrcode.CameraManager;
import com.fuj.enjoytv.utils.LogUtils;
import com.google.zxing.BarcodeFormat;

import java.util.Vector;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by gang
 */
public class QRCodeFragment extends BaseFragment implements IQRCodeContact.View, View.OnClickListener, SurfaceHolder.Callback {
    private boolean hasSurface;
    private boolean playBeep;
    private boolean vibrate;

    private MediaPlayer mediaPlayer;
    private String characterSet;
    private Vector<BarcodeFormat> decodeFormats;

    ImageView back;
    CheckBox mFlashLed;
    TextView mAlbum;

    @Bind(R.id.qrcode_scanner)
    SurfaceView mSurfaceView;

    @Bind(R.id.qrcode_root)
    FrameLayout mRootView;

    private IQRCodeContact.Presenter mPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CameraManager.init(getActivity().getApplication());
    }

    @Override
    protected void findViews(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = (ViewGroup) inflater.inflate(R.layout.view_qrcode, container, false);
        ButterKnife.bind(this, root);
        initView();
        initData();
    }

    public void setPresenter(IQRCodeContact.Presenter presenter) {
        mPresenter = presenter;
        mPresenter.start();
    }

    @Override
    protected void initTitle(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.initTitle(inflater, container, savedInstanceState);
        title = inflater.inflate(R.layout.title_qrcode, container, false);
        back = (ImageView) findTitle(R.id.title_back);
        mFlashLed = (CheckBox) findTitle(R.id.title_qrcode_flash);
        mAlbum = (TextView) findTitle(R.id.title_qrcode_album);
        setCustomView();
    }

    private void initView() {
        setMarginTop(mRootView);
    }

    private void initData() {}

    @Override
    public void onResume() {
        super.onResume();
        SurfaceHolder surfaceHolder = mSurfaceView.getHolder();
        if (hasSurface) {
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
        decodeFormats = null;
        characterSet = null;

        playBeep = true;
        AudioManager audioService = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
            playBeep = false;
        }
        //initBeepSound();
        vibrate = true;
    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        try {
            CameraManager.get().openDriver(surfaceHolder);
        } catch (Exception e) {
            LogUtils.e(" [error] " + e);
        }

        /*if (handler == null) {
            handler = new CaptureActivityHandler(this, decodeFormats, characterSet);
        }*/
    }

    private void initBeepSound() {
        /*if (playBeep && mediaPlayer == null) {
            // The volume on STREAM_SYSTEM is not adjustable, and users found it
            // too loud,
            // so we now play on the music stream.
            setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnCompletionListener(beepListener);

            AssetFileDescriptor file = getResources().openRawResourceFd(R.raw.beep);
            try {
                mediaPlayer.setDataSource(file.getFileDescriptor(), file.getStartOffset(), file.getLength());
                file.close();
                mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
                mediaPlayer.prepare();
            } catch (IOException e) {
                mediaPlayer = null;
            }
        }*/
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                getActivity().onBackPressed();
                break;
            case R.id.title_qrcode_flash:

                break;
            case R.id.title_qrcode_album:
                clickAlbum();
                break;
            default:
                break;
        }
    }

    private void clickAlbum() {
        Intent innerIntent = new Intent(Intent.ACTION_GET_CONTENT);
        innerIntent.setType("image/*");
        startActivityForResult(innerIntent, 1);
    }

    @Override
    public void onActivityResult(final int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case 1:
                    getQRCode(data);
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void getQRCode(Intent data) {}

    @Override
    public void surfaceCreated(SurfaceHolder holder) {}

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {}

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {}
}
