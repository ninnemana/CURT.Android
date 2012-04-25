/**
 * 
 */
package curt.android.ginger;

import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;


/**
 * @author alexninneman
 *
 */
public class Search extends ListActivity {

	@Override
	public void onCreate(Bundle savedState){
		super.onCreate(savedState);
		setContentView(R.layout.search);
		
		handleIntent(getIntent());
	}
	
	@Override
	protected void onNewIntent(Intent intent){
		setIntent(intent);
		handleIntent(intent);
	}
	
	private void handleIntent(Intent intent){
		if(Intent.ACTION_SEARCH.equals(intent.getAction())){
			String query = intent.getStringExtra(SearchManager.QUERY);
			//doMySearch(query);
			Log.i("search: ", query);
		}
	}
}
