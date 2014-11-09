package com.example.comfine;

import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle; // A mapping from String values to various Parcelable types.
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity; // Required to create an activity.
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu; // Interface for managing the items in a menu.
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class TextBox extends Activity implements OnClickListener { // all classes extends activity

	Button deviceSearchButton;
	Button webSearchButton;

	ArrayAdapter<String> appsSearchAdapter;
	ArrayAdapter<String> webSearchAdapter;
	ListView appList;

	@Override
	protected void onCreate(Bundle savedInstanceState) { // Create an activity/ screen

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_textbox); // display activity textbox  when app starts (Main)

		appList = (ListView) findViewById(R.id.list_apps);
		LinkedList<String> l = new LinkedList<String>();
		
		LinkedList<String> m = new LinkedList<String>();
		appsSearchAdapter = new ArrayAdapter<String>(this, R.layout.apps_list,l);
		
		webSearchAdapter = new ArrayAdapter<String>(this,R.layout.web_list,m);
		
		deviceSearchButton = (Button) findViewById(R.id.button_device_search);
		deviceSearchButton.setOnClickListener((OnClickListener) this);
		
		webSearchButton = (Button) findViewById(R.id.button_web_search);
		webSearchButton.setOnClickListener((OnClickListener) this);
		
		appList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			public void onItemClick(AdapterView<?> adapter, View view, int position, long arg) {

				Object listItem = appList.getItemAtPosition(position);
				Log.d("kps",(String)listItem);
				Intent intent = null;

				if (((String)listItem).contains("/")){

					intent = new Intent (Intent.ACTION_VIEW,Uri.parse((String)"fb://profile/100000151692941"));
					//intent.setDataAndType(Uri.parse((String)listItem) ,"html");
					intent.addCategory(Intent.CATEGORY_BROWSABLE);

				}else {

					intent = getPackageManager().getLaunchIntentForPackage((String)listItem);

				}

				try {

					if(intent == null){

						Log.d("kps","intent is null");

					}else{
						Log.d("kps-intent"," " + intent.getDataString());
						startActivity(intent);
					}
				} catch (Exception e) {

					Log.d("exception-KPS",e.getLocalizedMessage());
					startActivity(new Intent(Intent.ACTION_VIEW,
							Uri.parse("https://www.facebook.com/1692920864")));

				}
				//startNewActivity(,listItem);
			}
		});

		//addListenerOnDeviceSearchButton();
		//addListenerOnWebSearchButton();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_textbox, menu);
		return true;
	}

	public LinkedList<String> getAllApps(String query){

		final PackageManager pm = getPackageManager();

		//get a list of installed apps.
		List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
		LinkedList<String> a = new LinkedList<String>();


		for (ApplicationInfo packageInfo : packages) {

			//Log.d("APPLIST", "Source dir : " + packageInfo.sourceDir);

			//Log.d("APPLIST", "Launch Activity :" + pm.getLaunchIntentForPackage(packageInfo.packageName)); 
			Intent intent = getPackageManager().getLaunchIntentForPackage(packageInfo.packageName); 

			String[] appNames = packageInfo.packageName.split("\\.");
			String appName = (appNames.length == 0) ? "" : appNames[appNames.length - 1];

			if (intent != null && appName.toLowerCase().contains(query.toLowerCase())){

				a.add(packageInfo.packageName);

			}
		}

		//this is just for adding a deeplink of facebook app
		a.add("grid6.us:/xyzzy000");

		//Log.d("STRING_APP_LIST",display_app_list);
		// the getLaunchIntentForPackage returns an intent that you can use with startActivity() 

		return a;
	}
	
	@SuppressLint("NewApi") 
	public void onClick(View v){
		
		if (v == deviceSearchButton){
			EditText searchBar = (EditText) findViewById(R.id.searchtext);
			String queryString = searchBar.getText().toString();

			LinkedList<String> apps = getAllApps(queryString);
			//String web_search = getSearchResultFromAPI(queryString);
			//Log.d("web_search", web_search );

			ListView listViewApps = (ListView) findViewById(R.id.list_apps);
			appsSearchAdapter.clear();

			appsSearchAdapter.addAll(apps);
			listViewApps.setAdapter(appsSearchAdapter);
		} else if (v == webSearchButton) {
			
			EditText searchBar = (EditText) findViewById(R.id.searchtext);
			String queryString = searchBar.getText().toString();

			//InputStream webSearch = getSearchResultFromAPI(queryString);
			LinkedList<String> webSearchList = new LinkedList<String>();
			Log.d("web_search", queryString + " query str ing" );

			ListView listViewWeb = (ListView) findViewById(R.id.list_web);
			webSearchAdapter.clear();
			
			webSearchAdapter.addAll(webSearchList);
			listViewWeb.setAdapter(webSearchAdapter);
		}
		
	}
	public void addListenerOnDeviceSearchButton() {

		deviceSearchButton = (Button) findViewById(R.id.button_device_search);
		
		deviceSearchButton.setOnClickListener(new OnClickListener() {

			@TargetApi(Build.VERSION_CODES.HONEYCOMB) 
			@Override
			public void onClick(View arg0) {

				EditText searchBar = (EditText) findViewById(R.id.searchtext);
				String queryString = searchBar.getText().toString();

				LinkedList<String> apps = getAllApps(queryString);
				//String web_search = getSearchResultFromAPI(queryString);
				//Log.d("web_search", web_search );

				ListView listViewApps = (ListView) findViewById(R.id.list_apps);
				appsSearchAdapter.clear();

				appsSearchAdapter.addAll(apps);
				listViewApps.setAdapter(appsSearchAdapter);

				//setListAdapter(new ArrayAdapter<String>(this, R.layout.activity_textbox,apps));
			}

		});

	}
	
	public void addListenerOnWebSearchButton(){
		
		webSearchButton = (Button)findViewById(R.id.button_web_search);
		
		deviceSearchButton.setOnClickListener(new OnClickListener() {

			@TargetApi(Build.VERSION_CODES.HONEYCOMB) 
			
			public void onClick(View arg0) {

				EditText searchBar = (EditText) findViewById(R.id.searchtext);
				String queryString = searchBar.getText().toString();

				//InputStream webSearch = getSearchResultFromAPI(queryString);
				LinkedList<String> webSearchList = new LinkedList<String>();
				Log.d("web_search", queryString + " query str ing" );

				ListView listViewWeb = (ListView) findViewById(R.id.list_web);
				webSearchAdapter.clear();
				
				webSearchAdapter.addAll(webSearchList);
				listViewWeb.setAdapter(webSearchAdapter);

				
				//setListAdapter(new ArrayAdapter<String>(this, R.layout.activity_textbox,apps));
			}

		});

	}


	public void startNewActivity(Context context, String packageName)
	{
		Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
		if (intent != null)
		{
			/* we found the activity now start the activity */
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
		}
		else
		{
			/* bring user to the market or let them choose an app? */
			intent = new Intent(Intent.ACTION_VIEW);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.setData(Uri.parse("market://details?id="+packageName));
			startActivity(intent);
		}
	}


	public static InputStream getSearchResultFromAPI(String queryString) {
		InputStream content = null;
		String url = "http://localhost:5000/query?q=" + queryString;

		try {

			HttpClient httpclient = new DefaultHttpClient();
			HttpResponse response = httpclient.execute(new HttpGet(url));
			content = response.getEntity().getContent();
			//String myString = IOUtils.toString(myInputStream, "UTF-8");


		} catch (Exception e) {
			Log.d("[GET REQUEST]" , "Network exception", e);
		}
		return content;
	}
}