package com.fuj.enjoytv.activity.video;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fuj.enjoytv.R;
import com.fuj.enjoytv.base.BaseFragment;
import com.fuj.enjoytv.camera.DeviceUtils;
import com.fuj.enjoytv.camera.FileUtils;
import com.fuj.enjoytv.camera.CameraUtils;
import com.fuj.enjoytv.camera.MediaObject;
import com.fuj.enjoytv.camera.MediaRecorderBase;
import com.fuj.enjoytv.camera.MediaRecorderConfig;
import com.fuj.enjoytv.camera.MediaRecorderNative;
import com.fuj.enjoytv.utils.LogUtils;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by gang
 */
public class VideoFragment extends BaseFragment implements IVideoContact.View, MediaRecorderBase.OnErrorListener,
        View.OnClickListener, MediaRecorderBase.OnPreparedListener, MediaRecorderBase.OnEncodeListener {
    private boolean isRunning;

    CheckBox mCameraSwitch; // 前后摄像头切换
    CheckBox mRecordLed; // 闪光灯
    ImageView back;

    @Bind(R.id.video_delete)
    CheckedTextView mVideoDelete; // 回删按钮、延时按钮、滤镜按钮

    @Bind(R.id.video_record)
    TextView mVideoRecord; // 拍摄按钮

    @Bind(R.id.bottom_layout)
    RelativeLayout mBottomLayout; // 底部条

    @Bind(R.id.record_preview)
    SurfaceView mSurfaceView; // 摄像头数据显示画布

    @Bind(R.id.video_camera)
    FrameLayout videoView;

    private int RECORD_TIME_MIN = (int) (1.5f * 1000);
    private int RECORD_TIME_MAX = 6 * 1000; // 录制最长时间
    private static final int HANDLE_INVALIDATE_PROGRESS = 0; // 刷新进度条
    private static final int HANDLE_STOP_RECORD = 1; // 延迟拍摄停止
    private volatile boolean mPressedStatus; // 是否是点击状态
    private volatile boolean mReleased; // 是否已经释放
    private MediaRecorderBase mMediaRecorder; // SDK视频录制对象
    private MediaObject mMediaObject; // 视频信息
    public final static String VIDEO_URI = "video_uri"; // 视屏地址
    public final static String OUTPUT_DIRECTORY = "output_directory"; // 本次视频保存的文件夹地址
    public final static String VIDEO_SCREENSHOT = "video_screenshot"; // 视屏截图地址
    public final static String OVER_ACTIVITY_NAME = "over_activity_name"; // 录制完成后需要跳转的activity
    public final static String MEDIA_RECORDER_MAX_TIME_KEY = "media_recorder_max_time_key"; // 最大录制时间的key
    public final static String MEDIA_RECORDER_MIN_TIME_KEY = "media_recorder_min_time_key"; // 最小录制时间的key
    public final static String MEDIA_RECORDER_CONFIG_KEY = "media_recorder_config_key"; // 录制配置key

    private boolean GO_HOME;
    private boolean NEED_FULL_SCREEN = false;
    private IVideoContact.Presenter mPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSmallVideo();
    }

    protected void findViews(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = (ViewGroup) inflater.inflate(R.layout.view_video, container, false);
        ButterKnife.bind(this, root);
        initView();
        initData();
    }

    @Override
    public void setPresenter(IVideoContact.Presenter presenter) {
        this.mPresenter = presenter;
        mPresenter.start();
    }

    @Override
    protected void initTitle(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.initTitle(inflater, container, savedInstanceState);
        title = inflater.inflate(R.layout.title_video, container, false);
        back = (ImageView) findTitle(R.id.title_back);
        mRecordLed = (CheckBox) findTitle(R.id.title_video_flash);
        mCameraSwitch = (CheckBox) findTitle(R.id.title_video_camera);
        setCustomView();
    }

    private void initView() {
        setMarginTop(videoView);
    }

    @Override
    protected void setListeners() {
        super.setListeners();
        back.setOnClickListener(this);
        mVideoRecord.setOnClickListener(this);

        if (MediaRecorderBase.isSupportFrontCamera()) {
            mCameraSwitch.setOnClickListener(this);
        } else {
            mCameraSwitch.setVisibility(View.GONE);
        }

        if (DeviceUtils.isSupportCameraLedFlash(getActivity().getPackageManager())) {
            mRecordLed.setOnClickListener(this);
        } else {
            mRecordLed.setVisibility(View.GONE);
        }
    }

    private void initData() {
        Intent intent = new Intent();
        MediaRecorderConfig mediaRecorderConfig = intent.getParcelableExtra(MEDIA_RECORDER_CONFIG_KEY);
        if (mediaRecorderConfig == null) {
            return;
        }
        NEED_FULL_SCREEN = mediaRecorderConfig.getFullScreen();
        RECORD_TIME_MAX = mediaRecorderConfig.getRecordTimeMax();
        RECORD_TIME_MIN = mediaRecorderConfig.getRecordTimeMin();
        MediaRecorderBase.MAX_FRAME_RATE = mediaRecorderConfig.getMaxFrameRate();
        MediaRecorderBase.NEED_FULL_SCREEN = NEED_FULL_SCREEN;
        MediaRecorderBase.MIN_FRAME_RATE = mediaRecorderConfig.getMinFrameRate();
        MediaRecorderBase.SMALL_VIDEO_HEIGHT = mediaRecorderConfig.getSmallVideoHeight();
        MediaRecorderBase.SMALL_VIDEO_WIDTH = mediaRecorderConfig.getSmallVideoWidth();
        MediaRecorderBase.mVideoBitrate = mediaRecorderConfig.getVideoBitrate();
        MediaRecorderBase.CAPTURE_THUMBNAILS_TIME = mediaRecorderConfig.getCaptureThumbnailsTime();
        GO_HOME = mediaRecorderConfig.isGO_HOME();
    }

    private void initSurfaceView() {
        if (NEED_FULL_SCREEN) {
            mBottomLayout.setBackgroundColor(0);
            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mSurfaceView.getLayoutParams();
            lp.setMargins(0, 0, 0, 0);
            mSurfaceView.setLayoutParams(lp);
        } else {
            final int w = DeviceUtils.getScreenWidth(getContext());
            ((RelativeLayout.LayoutParams) mBottomLayout.getLayoutParams()).topMargin = (int) (w / (MediaRecorderBase.SMALL_VIDEO_HEIGHT / (MediaRecorderBase.SMALL_VIDEO_WIDTH * 1.0f)));
            int height = (int) (w * ((MediaRecorderBase.mSupportedPreviewWidth * 1.0f) / MediaRecorderBase.SMALL_VIDEO_HEIGHT));
            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mSurfaceView.getLayoutParams();
            lp.width = w;
            lp.height = height;
            mSurfaceView.setLayoutParams(lp);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mMediaRecorder == null) {
            initMediaRecorder();
        } else {
            mRecordLed.setChecked(false);
            mMediaRecorder.prepare();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    public static void initSmallVideo() {
        File dcim = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM); // 设置拍摄视频缓存路径
        if (DeviceUtils.isZte()) {
            if (dcim.exists()) {
                CameraUtils.setVideoCachePath(dcim + "/enjoytv/");
            } else {
                CameraUtils.setVideoCachePath(dcim.getPath().replace("/sdcard/", "/sdcard-ext/") + "/enjoytv/");
            }
        } else {
            CameraUtils.setVideoCachePath(dcim + "/enjoytv/");
        }
        CameraUtils.initialize(false, null); // 初始化拍摄
    }

    private void initMediaRecorder() {
        mMediaRecorder = new MediaRecorderNative();
        mMediaRecorder.setOnErrorListener(this);
        mMediaRecorder.setOnEncodeListener(this);
        mMediaRecorder.setOnPreparedListener(this);

        File f = new File(CameraUtils.getVideoCachePath());
        if (!FileUtils.checkFile(f)) {
            boolean result = f.mkdirs();
            LogUtils.i(" [mk dir] " + result);
        }
        String key = String.valueOf(System.currentTimeMillis());
        mMediaObject = mMediaRecorder.setOutputDirectory(key, CameraUtils.getVideoCachePath() + key);
        mMediaRecorder.setSurfaceHolder(mSurfaceView.getHolder());
        mMediaRecorder.prepare();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.video_record:
                switchRecord();
                break;
            case R.id.title_back:
                getActivity().onBackPressed();
                break;
            case R.id.title_video_flash:
                switchFlash();
                break;
            case R.id.title_video_camera:
                switchCamera();
                break;
            default:
                break;
        }
    }

    private void switchCamera() {
        if (mRecordLed.isChecked()) {
            if (mMediaRecorder != null) {
                mMediaRecorder.toggleFlashMode();
            }
            mRecordLed.setChecked(false);
        }

        if (mMediaRecorder != null) {
            mMediaRecorder.switchCamera();
        }

        if (mMediaRecorder.isFrontCamera()) {
            mRecordLed.setEnabled(false);
        } else {
            mRecordLed.setEnabled(true);
        }
    }

    private void switchFlash() {
        if (mMediaRecorder != null) {
            if (mMediaRecorder.isFrontCamera()) {
                return;
            }
        }

        if (mMediaRecorder != null) {
            mMediaRecorder.toggleFlashMode();
        }
    }

    private void switchRecord() {
        if(!isRunning) {
            startRecord();
            isRunning = true;
            return;
        }
        stopRecord();
        isRunning = false;
    }

    private void startRecord() {
        if(mMediaRecorder != null) {
            if(mMediaRecorder.startRecord() == null) {
                return;
            }
            mMediaObject.buildMediaPart(mMediaRecorder.mCameraId);
            mMediaRecorder.setRecordState(true);
            setStartUI();
        }
    }

    private void stopRecord() {
        if(mMediaRecorder != null) {
            mMediaRecorder.stopRecord();
            mMediaRecorder.setRecordState(false);
            mMediaRecorder.setStopDate();
            setStopUI();
        }
    }

    private void setStartUI() {
        mPressedStatus = true;
        mVideoRecord.animate().scaleX(0.8f).scaleY(0.8f).setDuration(500).start();
        mVideoRecord.setBackgroundResource(R.mipmap.ic_record_stop);
        mCameraSwitch.setEnabled(false);
        mRecordLed.setEnabled(false);
    }

    private void setStopUI() {
        mPressedStatus = false;
        mVideoRecord.animate().scaleX(1).scaleY(1).setDuration(500).start();
        mVideoRecord.setBackgroundResource(R.mipmap.ic_record_start);
        mCameraSwitch.setEnabled(true);
        mRecordLed.setEnabled(true);
    }

    @Override
    public void onPrepared() {
        initSurfaceView();
    }

    @Override
    public void onVideoError(int what, int extra) {

    }

    @Override
    public void onAudioError(int what, String message) {

    }

    @Override
    public void onEncodeStart() {

    }

    @Override
    public void onEncodeProgress(int progress) {

    }

    @Override
    public void onEncodeComplete() {

    }

    @Override
    public void onEncodeError() {

    }
}
