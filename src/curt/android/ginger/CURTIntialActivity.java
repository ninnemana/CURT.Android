package curt.android.ginger;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabContentFactory;
import android.widget.TabHost.TabSpec;
import android.widget.TableLayout.LayoutParams;
import android.widget.TextView;

public class CURTIntialActivity extends TabActivity {
	
	TabHost mTabHost;
	public static TabActivity context;
	
	private void setupTabHost(){
		mTabHost = (TabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup();
		mTabHost.getTabWidget().setOrientation(LinearLayout.VERTICAL);
	}
	
	private View createIndicatorView(TabHost tabHost, CharSequence label, int imgId) {

	       LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	       View tabIndicator = inflater.inflate(R.layout.tab_indicator,
	               tabHost.getTabWidget(), // tab widget is the parent
	               false); // no inflate params

	       final TextView tv = (TextView) tabIndicator.findViewById(R.id.title);
	       tv.setText(label);

	       final ImageView iconView = (ImageView) tabIndicator.findViewById(R.id.icon);
	       iconView.setImageResource(imgId);

	       return tabIndicator;
	   }
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        context = this;
        
        setupTabHost();
        
        // Create our icon image
        ImageView imgView = new ImageView(getApplicationContext());
        imgView.setImageResource(R.drawable.icon_trans);
        
        // Set set the dimensions and spacing our the icon
        LayoutParams params = new LayoutParams();
        params.width = 72;
        params.height = 72;
        params.setMargins(10, 10, 20, 10);
        params.gravity = Gravity.CENTER_VERTICAL;
        imgView.setLayoutParams(params);
        
        // Add the image to the TabHost by turning the image into a tab
        final View view = new TextView(this);
        TabSpec spec = mTabHost.newTabSpec("logo").setIndicator(imgView).setContent(new TabContentFactory(){
    		public View createTabContent(String tag) { return view; }
    	});
        mTabHost.addTab(spec);
        
        // Make the image tab unclickabled
        if(mTabHost.getTabWidget().getChildCount() > 0){
	        View tab = mTabHost.getTabWidget().getChildAt(0);
	        tab.setClickable(false);
        }
        
        // Add the other Tabs
        setupTab(new TextView(this), "cats", new Intent().setClass(this, CategoryGroup.class), R.drawable.tab_categories);
        setupTab(new TextView(this), "find", new Intent().setClass(this, LookupGroup.class), R.drawable.tab_lookup);
        setupTab(new TextView(this), "scan", new Intent().setClass(this, Scanner.class), R.drawable.tab_barcode);
        
        mTabHost.setOnTabChangedListener(new OnTabChangeListener(){
			public void onTabChanged(String tabId) {
				int spec = mTabHost.getCurrentTab();
				if(spec > 0){
					for(int i = 1; i < mTabHost.getTabWidget().getChildCount(); i++){
						View tView = mTabHost.getTabWidget().getChildAt(i);
						ImageView tabImg = (ImageView)tView.findViewById(R.id.tabImg);
						if(i == 1){
							if(spec != i){
								tabImg.setImageResource(R.drawable.tab_categories);
							}else{
								tabImg.setImageResource(R.drawable.tab_categories_selected);
							}
						}else if(i == 2){
							if(spec != i){
								tabImg.setImageResource(R.drawable.tab_lookup);
							}else{
								tabImg.setImageResource(R.drawable.tab_lookup_selected);
							}
						}else if(i == 3){
							if(spec != i){
								tabImg.setImageResource(R.drawable.tab_barcode);
							}else{
								tabImg.setImageResource(R.drawable.tab_barcode_selected);
							}
						}
					}
				}
			}
        });
        
        // Restore tab location to previous state or default to category tab
        if(savedInstanceState != null){
        	mTabHost.setCurrentTabByTag(savedInstanceState.getString("tab"));
        }else{
        	mTabHost.setCurrentTab(1);
        }
    }
    
    @Override
    protected void onSaveInstanceState(Bundle outState){
    	super.onSaveInstanceState(outState);
    	outState.putString("tab", mTabHost.getCurrentTabTag());
    }
    
    private void setupTab(final View view, final String tag){
    	setupTab(view, tag, null, 0);
    }
    
    private void setupTab(final View view, final String tag, Intent intent, int imgId){
    	View tabView = createTabView(mTabHost.getContext(),tag, imgId);
    	
    	TabSpec setContent;
    	if(intent == null){
	    	setContent = mTabHost.newTabSpec(tag).setIndicator(tabView).setContent(new TabContentFactory(){
	    		public View createTabContent(String tag) { return view; }
	    	});
    	}else{
    		//setContent = mTabHost.newTabSpec(tag).setIndicator(tabView).setContent(intent);
    		setContent = mTabHost.newTabSpec(tag).setIndicator(createIndicatorView(mTabHost, tag, imgId));
    	}
    	mTabHost.addTab(setContent);
    }
    
    private static View createTabView(final Context context, final String text, int imgId){
    	View view = LayoutInflater.from(context).inflate(R.layout.tabs_bg,null);
    	if(imgId > 0){
    		ImageView imgView = (ImageView) view.findViewById(R.id.tabImg);
    		imgView.setImageResource(imgId);
    	}
    	/*TextView tv = (TextView) view.findViewById(R.id.tabsText);
    	tv.setText(text);*/
    	return view;
    }
    
}

