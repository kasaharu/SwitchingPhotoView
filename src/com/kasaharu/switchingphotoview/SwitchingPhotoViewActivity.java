package com.kasaharu.switchingphotoview;

import java.util.Timer;
import java.util.TimerTask;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class SwitchingPhotoViewActivity extends Activity {

	static final int NUMOFTHUMBNAIL = 4;

	ImageView mImageViewTop;
	ImageView[] mImageThumbnail = new ImageView[NUMOFTHUMBNAIL];

	int[] mThumbnailLayout = {R.id.imgIcon1, R.id.imgIcon2, R.id.imgIcon3, R.id.imgIcon4};
	int[] mThumbnailResId  = {R.drawable.photo1, R.drawable.photo2, R.drawable.photo3, R.drawable.photo4};

	int mSelectedThumbnailNo = 0;

	Timer mTimer;
	SwitchingHandler mHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.switchingphoto_view);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onStart() {
		super.onStart();
		memInit();
		runMainProgress();
	}

	@Override
	public void onStop() {
		super.onStop();
		memFinal();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	/**
	 * Param Initialize
	 */
	public void memInit() {
		mImageViewTop = (ImageView)findViewById(R.id.imgTopPhpto);

		for (int i = 0; i < NUMOFTHUMBNAIL; i++) {
			mImageThumbnail[i] = (ImageView)findViewById(mThumbnailLayout[i]);
			mImageThumbnail[i].setImageResource(mThumbnailResId[i]);
			mImageThumbnail[i].setOnClickListener(new OnClickThumbnailLisener(i));
		}
  		viewPhoto(mSelectedThumbnailNo);
	}

	/**
	 * Param Finalize
	 */
	public void memFinal() {
		mImageViewTop = null;
		for (int i = 0; i < NUMOFTHUMBNAIL; i++) {
			mImageThumbnail[i] = null;
		}

		mTimer.cancel();
		mTimer.purge();
		mTimer = null;

	}

	/**
	 * この Activity のメイン処理
	 * 3[sec] ごとに写真を切り替える
	 */
	public void runMainProgress() {
        mHandler = new SwitchingHandler();
		SwitchingPhoteTimerTask switchtimertask = new SwitchingPhoteTimerTask();
		mTimer = new Timer(true);
		mTimer.schedule(switchtimertask, 3000, 3000);
	}

	public void viewPhoto(int setId) {
    	for (int i = 0; i < NUMOFTHUMBNAIL; i++) {
    		mImageThumbnail[i].setColorFilter(0x88FFFFFF);
    	}

  		mImageThumbnail[setId].setColorFilter(null);
    	mImageViewTop.setImageResource(mThumbnailResId[setId]);
	}

    class OnClickThumbnailLisener implements OnClickListener {
    	int mSelectedRes;
    	public OnClickThumbnailLisener(int selectedRes) {
    		mSelectedRes = selectedRes;
    	}
    	@Override
    	public void onClick(View v) {
    		viewPhoto(mSelectedRes);
    	}
    }

    class SwitchingPhoteTimerTask extends TimerTask {
		@Override
		public void run() {
			if (mSelectedThumbnailNo >= NUMOFTHUMBNAIL - 1) {
				mSelectedThumbnailNo = 0;
			} else {
				mSelectedThumbnailNo++;
			}

			Message msg = new Message();
			msg.what = mSelectedThumbnailNo;
			mHandler.sendMessage(msg);
		}
    }

    class SwitchingHandler extends Handler {
    	@Override
    	public void handleMessage(Message msg) {
    		for (int i = 0; i < NUMOFTHUMBNAIL; i++) {
    			mImageThumbnail[i].setColorFilter(0x88FFFFFF);
    		}

    		int index = msg.what;
  			mImageThumbnail[index].setColorFilter(null);
    		mImageViewTop.setImageResource(mThumbnailResId[index]);
    	}
    }

}
