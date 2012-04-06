package curt.android.ginger;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import curt.android.ginger.CategoryObject.Category;
import curt.android.ginger.Part.*;

public class PartList extends ListActivity {

	// Async objects
	private static ProgressDialog progressDialog;
	private static Handler handler;
	private Thread downloadThread;
	
	// Application objects
	private List<Part> parts = null;
	private Number catID = null;
	private PartListAdapter adapter;
	private Activity partListContext;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.part_list);
		partListContext = this.getParent();
		
		// Create handler to update the UI
		handler = new Handler();
		
		Bundle bundle = this.getIntent().getExtras();
		this.catID = bundle.getDouble("catID", 0);
		
		parts = new ArrayList<Part>();
		
		adapter = new PartListAdapter(this, R.layout.part_list_row, parts);
		setListAdapter(adapter);
		
		downloadThread = (Thread) getLastNonConfigurationInstance();
		if(downloadThread != null && downloadThread.isAlive()){
			progressDialog = ProgressDialog.show(this, "Please wait...", "Retrieving parts...");
		}
		loadParts();
	}
	
	// Save the thread
	@Override
	public Object onRetainNonConfigurationInstance(){
		return downloadThread;
	}
	
	public void loadParts(){
		progressDialog = ProgressDialog.show(CURTIntialActivity.context, "Please Wait...", "Retrieving parts...");
		downloadThread = new MyThread();
		downloadThread.start();
	}
	
	// Dismiss dialog if activity is destroyed
	@Override
	protected void onDestroy(){
		if(progressDialog != null && progressDialog.isShowing()){
			progressDialog.dismiss();
			progressDialog = null;
		}
		super.onDestroy();
	}
	
	private List<Part> getCategoryParts(){
		parts = new ArrayList<Part>();
		Category cat = new Category();
		
		cat.setCatID(this.catID);
		parts = cat.GetCategoryParts();
		return parts;
	}
	
	public class MyThread extends Thread{
		@Override
		public void run(){
			try{
				// Simulate a slow network
				try{
					new Thread().sleep(5000);
				}catch(InterruptedException e){
					e.printStackTrace();
				}
				
				parts = getCategoryParts();			
				handler.post(new MyRunnable());
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	public class MyRunnable implements Runnable{
		public void run(){
			if(parts != null && parts.size() > 0){
				adapter = new PartListAdapter(partListContext,R.layout.part_list_row, parts);
				/*for(Iterator<Part> i = parts.iterator(); i.hasNext();){
					Part p = i.next();
					adapter.add(p);
					adapter.notifyDataSetChanged();
				}*/
				setListAdapter(adapter);
			}
			progressDialog.dismiss();
		}
	}
	
}
