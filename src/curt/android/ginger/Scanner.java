package curt.android.ginger;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import curt.android.camera.*;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.VideoView;

public class Scanner extends Activity {
	
	private static final int ACTION_TAKE_PHOTO_B = 1;
	private static final int ACTION_TAKE_PHOTO_S = 2;
	private static final int ACTION_TAKE_VIDEO = 3;
	
	private static final String BITMAP_STORAGE_KEY = "viewbitmap";
	private static final String IMAGEVIEW_VISIBILITY_STORAGE_KEY = "imageviewvisibility";
	private ImageView mImageView;
	private Bitmap mImageBitmap;
	
	private static final String VIDEO_STORAGE_KEY = "viewvideo";
	private static final String VIDEOVIEW_VISIBILITY_STORAGE_KEY = "videoviewvisibility";
	private VideoView mVideoView; 
	private Uri mVideoUri;
	
	private String mCurrentPhotoPath;
	
	private static final String JPEG_FILE_PREFIX = "IMG_";
	private static final String JPEG_FILE_SUFFIX = ".jpg";
	
	private AlbumStorageDirFactory mAlbumStorageDirFactory = null;
	
	/* Photo album for this application */
	private String getAlbumName(){
		return getString(R.string.album_name);
	}
	
	/* Photo album directory for this application */
	private File getAlbumDir(){
		File storageDir = null;
		
		if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
			storageDir = mAlbumStorageDirFactory.getAlbumStorageDir(getAlbumName());
			
			if(storageDir != null){
				if(!storageDir.mkdirs()){
					if(!storageDir.exists()){
						Log.d("Scanner", "failed to create directory");
						return null;
					}
				}
			}
		}else{
			Log.v(getString(R.string.app_name), "External storage is not mounted READ/WRITE");
		}
		return storageDir;
	}
	
	private File createImageFile() throws IOException{
		// Create an image file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		String imgFilename = JPEG_FILE_PREFIX + timeStamp + "_";
		File albumF = getAlbumDir();
		File imgF = File.createTempFile(imgFilename, JPEG_FILE_SUFFIX, albumF);
		return imgF;
	}
	
	private File setupPhotoFile() throws IOException{
		File f = createImageFile();
		mCurrentPhotoPath = f.getAbsolutePath();
		
		return f;
	}
	
	private void setPic(){
		
		/* There isn't enough memory to open more than a couple photos */
		/* So pre-scale the target bitmap into which the file is decoded */
		
		// Get the size of the ImageView
		int targetW = mImageView.getWidth();
		int targetH = mImageView.getHeight();
		
		// Get the size of the image
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(mCurrentPhotoPath, opts);
		int photoW = opts.outWidth;
		int photoH = opts.outHeight;
		
		// Figure out which way needs to be reduced less
		int scaleFactor = 1;
		if((targetW > 0) || (targetH > 0)){
			scaleFactor = Math.min(photoW/targetW, photoH/targetH);
		}
		
		// Set the bitmap options to scale the image decode target
		opts.inJustDecodeBounds = false;
		opts.inSampleSize = scaleFactor;
		opts.inPurgeable = true;
		
		// Decode the JPEG into a Bitmap
		Bitmap bmp = BitmapFactory.decodeFile(mCurrentPhotoPath, opts);
		
		// Associate the Bitmap to the ImageView
		mImageView.setImageBitmap(bmp);
		mVideoUri = null;
		mImageView.setVisibility(View.VISIBLE);
		mVideoView.setVisibility(View.INVISIBLE);
	}
	
	private void galleryAddPic(){
		Intent mediaScanIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
		File f = new File(mCurrentPhotoPath);
		Uri contentUri = Uri.fromFile(f);
		mediaScanIntent.setData(contentUri);
		this.sendBroadcast(mediaScanIntent);
	}
	
	private void dispatchTakePictureIntent(int actionCode){
		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		
		switch(actionCode){
			case ACTION_TAKE_PHOTO_B:
				File f = null;
				try{
					f = setupPhotoFile();
					mCurrentPhotoPath = f.getAbsolutePath();
					takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
				}catch(IOException e){
					e.printStackTrace();
					f = null;
					mCurrentPhotoPath = null;
				}
				break;
			
			default:
				break;
		} // End Switch
		
		startActivityForResult(takePictureIntent, actionCode);
	}
	
	private void dispatchTakeVideoIntent(){
		Intent videoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
		startActivityForResult(videoIntent, ACTION_TAKE_VIDEO);
	}
	
	private void handleSmallCameraPhoto(Intent intent){
		Bundle extras = intent.getExtras();
		mImageBitmap = (Bitmap) extras.get("data");
		mImageView.setImageBitmap(mImageBitmap);
		mVideoUri = null;
		mImageView.setVisibility(View.VISIBLE);
		mVideoView.setVisibility(View.INVISIBLE);
	}
	
	private void handleBigCameraPhoto(){
		if(mCurrentPhotoPath != null){
			setPic();
			galleryAddPic();
			mCurrentPhotoPath = null;
		}
	}
	
	private void handleCameraVideo(Intent intent){
		mVideoUri = intent.getData();
		mVideoView.setVideoURI(mVideoUri);
		mImageBitmap = null;
		mVideoView.setVisibility(View.VISIBLE);
		mImageView.setVisibility(View.INVISIBLE);
	}
	
	// Create onClickListeners for big photo, small photo, and video
	Button.OnClickListener mTakePicOnClickListener = new Button.OnClickListener(){
		public void onClick(View v){
			dispatchTakePictureIntent(ACTION_TAKE_PHOTO_B);
		}
	};
	
	Button.OnClickListener mTakePicSOnClickListener = new Button.OnClickListener(){
		public void onClick(View v){
			dispatchTakePictureIntent(ACTION_TAKE_PHOTO_S);
		}
	};
	Button.OnClickListener mTakeVidOnClickListener = new Button.OnClickListener(){
		public void onClick(View v){
			dispatchTakeVideoIntent();
		}
	};
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.scanner);
		
		mImageView = (ImageView) findViewById(R.id.imageView);
		mVideoView = (VideoView) findViewById(R.id.videoView);
		mImageBitmap = null;
		mVideoUri = null;
		
		Button picBtn = (Button) findViewById(R.id.btnIntent);
		setBtnListenerOrDisable(picBtn, mTakePicOnClickListener, MediaStore.ACTION_IMAGE_CAPTURE);
		
		Button picSBtn = (Button) findViewById(R.id.btnIntentS);
		setBtnListenerOrDisable(picSBtn, mTakePicSOnClickListener, MediaStore.ACTION_IMAGE_CAPTURE);
		
		Button vidBtn = (Button) findViewById(R.id.btnIntentV);
		setBtnListenerOrDisable(vidBtn, mTakeVidOnClickListener, MediaStore.ACTION_VIDEO_CAPTURE);
		
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO){
			mAlbumStorageDirFactory = new FroyoAlbumDirFactory();
		}else{
			mAlbumStorageDirFactory = new BaseAlbumDirFactory();
		}
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		switch(requestCode){
			case ACTION_TAKE_PHOTO_B: {
				if(resultCode == RESULT_OK){
					handleBigCameraPhoto();
				}
			} // ACTION_TAKE_PHOTO_B
			
			case ACTION_TAKE_PHOTO_S: {
				if(resultCode == RESULT_OK){
					handleSmallCameraPhoto(data);
				}
			} // ACTION_TAKE_PHOTO_S
			
			case ACTION_TAKE_VIDEO: {
				if(resultCode == RESULT_OK){
					handleCameraVideo(data);
				}
			} // ACTION_TAKE_VIDEO
		} // SWITCH
	}
	
	// Some lifecycle callbacks so that the image can survive orientation change
	@Override
	protected void onSaveInstanceState(Bundle outState){
		outState.putParcelable(BITMAP_STORAGE_KEY, mImageBitmap);
		outState.putParcelable(VIDEO_STORAGE_KEY, mVideoUri);
		outState.putBoolean(IMAGEVIEW_VISIBILITY_STORAGE_KEY, (mImageBitmap != null));
		outState.putBoolean(VIDEOVIEW_VISIBILITY_STORAGE_KEY, (mVideoUri != null));
		super.onSaveInstanceState(outState);
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState){
		super.onRestoreInstanceState(savedInstanceState);
		mImageBitmap = savedInstanceState.getParcelable(BITMAP_STORAGE_KEY);
		mVideoUri = savedInstanceState.getParcelable(VIDEO_STORAGE_KEY);
		mImageView.setImageBitmap(mImageBitmap);
		mImageView.setVisibility(
				savedInstanceState.getBoolean(IMAGEVIEW_VISIBILITY_STORAGE_KEY) ?
						ImageView.VISIBLE : ImageView.INVISIBLE
						);
		mVideoView.setVideoURI(mVideoUri);
		mVideoView.setVisibility(
				savedInstanceState.getBoolean(VIDEOVIEW_VISIBILITY_STORAGE_KEY) ?
						VideoView.VISIBLE : VideoView.INVISIBLE
				);
	}
	
	
	/**
	 * Indicates whether the specified action can be used as an intent. This
	 * method queries the package manager for installed packages that can
	 * response to an intent with the specified action. If no suitable package
	 * is found, this method returns false.
	 * http://android-developers.blogspot.com/2009/01/can-i-use-this-intent.html
	 * 
	 * @param context 	The application's environment
	 * @param action	The intent action to check for availability
	 * 
	 * @return			True if an Intent with the specified action can be sent
	 * 					and responded to, false otherwise.
	 * 
	 */
	public static boolean isIntentAvailable(Context context, String action){
		final PackageManager pkgManager = context.getPackageManager();
		final Intent intent = new Intent(action);
		List<ResolveInfo> list = pkgManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
		return list.size() > 0;
	}
	
	private void setBtnListenerOrDisable(Button btn, Button.OnClickListener listener, String intentName){
		if(isIntentAvailable(this,intentName)){
			btn.setOnClickListener(listener);
		}else{
			btn.setText(
					getText(R.string.cannot).toString() + " " + btn.getText()
			);
			btn.setClickable(false);
		}
	}
}
