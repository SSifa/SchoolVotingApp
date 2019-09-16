package com.example.schoolvotingapp;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public class Vote extends Activity {

	Spinner spinner;
	ArrayAdapter<CharSequence> adapter;

	AlertDialog.Builder viewSummary;
	// Progress Dialog
	private ProgressDialog pDialog;

	// onBackPressed
	private boolean doubleBackToExitPressedOnce = false;

	String voterID;
	String malerep;
	String malerepID;
	String femalerep;
	String femalerepID;
	String resident;
	String residentID;
	String nonresident;
	String nonresidentID;

	Boolean moveToMain;

	private static final String SUBMIT_URL = "http://192.168.0.123/Electrovots/android/submit_vote.php";

	// Json ID's
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_MESSAGE = "message";
	private static final String TAG_NAME = "name";
	
	SharedPreferences preferences;
	public static final String PrefFile = "Prefs";
	public static final String Voter_Username = "Voter_Username";
	public static final String Voter_ID = "Voter_ID";

	public static final String cMalerep = "cMalerep";
	public static final String cMalerepID = "cMalerepID";
	public static final String cMalerepSelected = "cMalerepSelected";

	public static final String cFemalerep = "cFemalerep";
	public static final String cFemalerepID = "cFemalerepID";
	public static final String cFemalerepSelected = "cFemalerepSelected";

	public static final String cResident = "cResident";
	public static final String cResidentID = "cResidentID";
	public static final String cResidentSelected = "cResidentSelected";

	public static final String cNonresident = "cNonresident";
	public static final String cNonresidentID = "cNonresidentID";
	public static final String cNonresidentSelected = "cNonresidentSelected";

	public static final String cVote_StraightSelected = "cVote_StraightSelected";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.vote_container);

		preferences = getSharedPreferences("PrefFile", MODE_PRIVATE);
		SharedPreferences.Editor edit = preferences.edit();

		viewSummary = new AlertDialog.Builder(Vote.this);
		
		moveToMain=false;

		// SharedPreferences.Editor editor = preferences.edit();
		voterID = preferences.getString(Voter_ID, "No Name");
		malerep = preferences.getString(cMalerep, "None");
		malerepID = preferences.getString(cMalerepID, "00");
		femalerep = preferences.getString(cFemalerep, "None");
		femalerepID = preferences.getString(cFemalerepID, "00");
		resident = preferences.getString(cResident, "None");
		residentID = preferences.getString(cResidentID, "00");
		nonresident = preferences.getString(cNonresident, "None");
		nonresidentID = preferences.getString(cNonresidentID, "00");

		// imageChange
		boolean bpres = preferences.getBoolean(cMalerepSelected, false);
		boolean bvice_pres = preferences.getBoolean(cFemalerepSelected, false);
		boolean bsec = preferences.getBoolean(cResidentSelected, false);
		boolean btrea = preferences.getBoolean(cNonresidentSelected, false);
		boolean vote_strt = preferences.getBoolean(cVote_StraightSelected, false);

		//
		TextView maleRep = findViewById(R.id.textViewPosition1);
		TextView femaleRep = findViewById(R.id.textViewPosition2);
		final TextView resident = findViewById(R.id.textViewPosition3);
		final TextView non_resident = findViewById(R.id.textViewPosition4);
		ImageView votest1 = (ImageView) findViewById(R.id.imageViewVote);
		ImageView votest2 = (ImageView) findViewById(R.id.imageViewVote2);

		spinner = findViewById(R.id.sppinner);
		adapter = ArrayAdapter.createFromResource(this, R.array.residential, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				switch (position){
					case 0:
						resident.setVisibility(View.VISIBLE);
						break;
					case 1:
						non_resident.setVisibility(View.VISIBLE);
						break;
					default:
						break;
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
					spinner.setAutofillHints("Residential");
				}
			}
		});

		// only for android 4.7-3.7 large category
		
		
		if (vote_strt) {
			votest1.setVisibility(View.GONE);
			votest2.setVisibility(View.VISIBLE);
			
			
			
		}
		else{
			votest1.setVisibility(View.VISIBLE);
			votest2.setVisibility(View.GONE);
		}
	
		
		if (bpres) {
			maleRep.setVisibility(View.GONE);
		}else{
			maleRep.setVisibility(View.VISIBLE);
		}
		
		if (bvice_pres) {
			femaleRep.setVisibility(View.GONE);
		}else{
			femaleRep.setVisibility(View.VISIBLE);
		}
		
		if (bsec) {
			resident.setVisibility(View.GONE);
		}else{
			resident.setVisibility(View.VISIBLE);
		}
		
		if (btrea) {
			non_resident.setVisibility(View.GONE);
		}else{
			non_resident.setVisibility(View.VISIBLE);
		}
		
		// This is for testing
		/*
		 * Toast toast = Toast.makeText(Vote.this, bvice_pres+"haha",
		 * Toast.LENGTH_SHORT); toast.setGravity(Gravity.CENTER, 0, 0);
		 * toast.show();
		 */

	}

	public void showMaleRep(View v) {
		Intent activityIntent = new Intent(this, Malerep.class);
		startActivity(activityIntent);
		finish();
	}

	public void showFemaleRep(View v) {
		Intent activityIntent = new Intent(this, Femalerep.class);
		startActivity(activityIntent);
		finish();
	}

	public void showResident(View v) {
		Intent activityIntent = new Intent(this, Resident.class);
		startActivity(activityIntent);
		finish();
	}

	public void showNonResident(View v) {
		Intent activityIntent = new Intent(this, NonResident.class);
		startActivity(activityIntent);
		finish();
	}

	public void submit(View v) {

		viewSummary.setTitle("Vote Summary");
		viewSummary.setMessage("Male rep: "      + "\n" + malerep + "\n\n" +
								"Female rep: "+ "\n" + femalerep + "\n\n" +
								"Resident: "     + "\n" + resident + "\n\n" +
								"Non-Resident: "     + "\n" + nonresident + "\n"
								+ "\n");
		viewSummary.setPositiveButton("OK",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						/*
						 * Toast toast = Toast.makeText(Vote.this, malerep +
						 * grade_rep + nonresident, Toast.LENGTH_SHORT);
						 * toast.setGravity(Gravity.CENTER, 0, 0); toast.show();
						 */

						//
						new PostComment().execute();

						//
						moveToMain=true;
						
					}
				});
		viewSummary.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				});
		viewSummary.show();
	}

	class PostComment extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(Vote.this);
			pDialog.setMessage("Submitting Votes...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... args) {
			// TODO Auto-generated method stub
			// Check for success tag
			int success;

			// We need to change this:

			try {
				// Building Parameters
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("voterId", voterID));
				params.add(new BasicNameValuePair("presidentId", malerepID));
				params.add(new BasicNameValuePair("vice_presidentId", femalerepID));
				params.add(new BasicNameValuePair("secretaryId", residentID));
				params.add(new BasicNameValuePair("treasurerId", nonresidentID));

				Log.d("request!", "starting");

				// Posting user data to script
				JSONParser jsonParser = new JSONParser();
				JSONObject json = jsonParser.makeHttpRequest(SUBMIT_URL,
						"POST", params);

				// full json response
				Log.d("Submitting your Votes..", json.toString());

				// json success element
				success = json.getInt(TAG_SUCCESS);
				if (success == 1) {
					Log.d("Vote submitted!", json.toString());
					finish();
					return json.getString(TAG_MESSAGE);
				} else {
					Log.d("Vote Failed!", json.getString(TAG_MESSAGE));
					return json.getString(TAG_MESSAGE);

				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;

		}

		protected void onPostExecute(String file_url) {
			// dismiss the dialog once product deleted
			pDialog.dismiss();
			if (file_url != null) {
				if(moveToMain){
				Toast.makeText(Vote.this, file_url, Toast.LENGTH_LONG).show();
				Intent activityIntent = new Intent(Vote.this, Main.class);
				startActivity(activityIntent);
				finish();
				}
			}

		}

	}

	@Override
	public void onBackPressed() {
		if (doubleBackToExitPressedOnce) {
			super.onBackPressed();
			Intent activityIntent = new Intent(this, Main.class);
			startActivity(activityIntent);
			finish();
		}

		this.doubleBackToExitPressedOnce = true;
		Toast toast = Toast.makeText(Vote.this,
				"Please click Back again to back", Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				doubleBackToExitPressedOnce = false;
			}
		}, 2000);
	}

}
