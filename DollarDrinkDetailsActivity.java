package com.aapbd.partylink;

import java.text.SimpleDateFormat;
import java.util.Date;



import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.aapbd.gpsservice.ServiceAlarm;
import com.aapbd.gpsservice.model.RedeemResponse;
import com.aapbd.partylink.db.DatabaseHandler;
import com.aapbd.partylink.utils.AppConstant;
import com.aapbd.partylink.utils.NukeSSLCerts;
import com.aapbd.partylink.utils.SharedPreferencesHelper;
import com.aapbd.utils.network.NetInfo;
import com.aapbd.utils.notification.AlertMessage;
import com.aapbd.utils.notification.BusyDialog;
import com.aapbd.utils.storage.PersistData;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.flurry.android.FlurryAgent;
import com.google.gson.Gson;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import static android.R.attr.password;
import static com.aapbd.partylink.R.id.url;
import static com.facebook.FacebookSdk.getCacheDir;

public class DollarDrinkDetailsActivity extends Activity {

	Context con;
	private BusyDialog busyNow;
	TextView textviewRedeemNow,textviewNotNow,texeviewDetailName,textviewBrandOffer,textviewDetailsAddreess;
	Typeface normal,boldFont;
	ImageView imageviewDetails;
	TextView textviewBottom;
	long timesInMilisecond;
	String currentDate,currentTime;
	Dialog dialog;
	DatabaseHandler db;
	TextView textviewDetailsCity;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dollar_drink_details);
		con = this;
		new NukeSSLCerts().nuke();
		db=new DatabaseHandler(con);
		initUI();

	}

	private void initUI() {
		// TODO Auto-generated method stub
		textviewDetailsCity=(TextView)findViewById(R.id.textviewDetailsCity);
		normal = Typeface.createFromAsset(con.getAssets(), "fonts/HelveticaNeueLTStd Lt.otf");
		boldFont = Typeface.createFromAsset(con.getAssets(), "fonts/HelveticaNeueLTStd Md.OTF");
		textviewRedeemNow=(TextView)findViewById(R.id.textviewRedeemNow);
		textviewRedeemNow.setTypeface(normal);
		textviewNotNow=(TextView)findViewById(R.id.textviewNotNow);
		textviewNotNow.setTypeface(normal);
		texeviewDetailName=(TextView)findViewById(R.id.texeviewDetailName);
		textviewBrandOffer=(TextView)findViewById(R.id.textviewBrandOffer);
		textviewDetailsAddreess=(TextView)findViewById(R.id.textviewDetailsAddreess);
		texeviewDetailName.setTypeface(boldFont);
		textviewBrandOffer.setTypeface(boldFont);
		textviewDetailsAddreess.setTypeface(normal);
		textviewDetailsCity.setTypeface(normal);
		imageviewDetails=(ImageView)findViewById(R.id.imageviewDetails);
		textviewBottom=(TextView)findViewById(R.id.textviewBottom);
		textviewBottom.setText(Html.fromHtml(getResources().getString(R.string.instruction)));
		timesInMilisecond=System.currentTimeMillis();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); 
		currentDate = formatter.format(new Date(timesInMilisecond));
		SimpleDateFormat formatter1 = new SimpleDateFormat("HH:mm"); 
		currentTime = formatter1.format(new Date(timesInMilisecond));
		try {
			texeviewDetailName.setText(AppConstant.mDrinkDollarInfo.getBar_name());
			textviewBrandOffer.setText("$"+AppConstant.mDrinkDollarInfo.getDrink_price()+" " +AppConstant.mDrinkDollarInfo.getBrand_offer()); // new code
			textviewDetailsAddreess.setText(AppConstant.mDrinkDollarInfo.getBar_address());
			textviewDetailsCity.setText(AppConstant.mDrinkDollarInfo.getBar_city());
			Picasso.with(con).load(AppConstant.mDrinkDollarInfo.getBrand_logo_url()).error(R.drawable.ic_launcher).into(imageviewDetails);

		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		textviewRedeemNow.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				String barName=AppConstant.mDrinkDollarInfo.getBar_name().replaceAll(" ","%20");

				sendRedeemNow(SharedPreferencesHelper.getUserEmail(con),barName,AppConstant.mDrinkDollarInfo.getBrand_offer_id(),AppConstant.mDrinkDollarInfo.getAdvertiser_offer_id(),PersistData.getStringData(con, AppConstant.lat),PersistData.getStringData(con, AppConstant.lon),currentDate,currentTime, AppConstant.mDrinkDollarInfo.getDrink_price() );
			}
		});
		
		textviewNotNow.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

	}

	public void backBtn(View v){
		finish();
	}
	


	protected void sendRedeemNow(String email,String bar_name,String brand_offer_id,String advertiser_offer_id,String latitude,String longitude,String date,String time, String drink_price) {

		busyNow = new BusyDialog(con, true);
		busyNow.show();


      String url="https://ipartypal.com/api/trunk/drinksoffer/redeemed?";
		url+="email"+"="+email+"&";
		url+="bar_name"+"="+bar_name+"&";
		url+="brand_offer_id"+"="+brand_offer_id+"&";
		url+="advertiser_offer_id"+"="+advertiser_offer_id+"&";
		url+="latitude"+"="+latitude+"&";
		url+="longitude"+"="+longitude+"&";
		url+="date"+"="+date+"&";
		url+="time"+"="+time+"&";
		url+="drink_price"+"="+drink_price+"";





		RequestQueue mRequestQueue;

// Instantiate the cache
		Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap

// Set up the network to use HttpURLConnection as the HTTP client.
		Network network = new BasicNetwork(new HurlStack());

// Instantiate the RequestQueue with the cache and network.
		mRequestQueue = new RequestQueue(cache, network);

// Start the queue
		mRequestQueue.start();

		//String url ="http://www.example.com";

// Formulate the request and handle the response.
		StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						// Do something with the response

						if (busyNow != null) {
							busyNow.dismis();
						}

						Log.e("Login response : ", ":"+ new String(response));
						Gson g=new Gson();
						RedeemResponse mRedeemResponse=g.fromJson(new String(response), RedeemResponse.class);
						if (mRedeemResponse.getError().equalsIgnoreCase("false")) {
							if(!db.ifDollarDrinkExist(AppConstant.mDrinkDollarInfo.getBrand_offer_id(), AppConstant.mDrinkDollarInfo.getBar_name())){
								AppConstant.mDrinkDollarInfo.setTime(timesInMilisecond);
								db.addDollarDrink(AppConstant.mDrinkDollarInfo);
								//Log.e("db size", ">>"+db.getAllGeneralActivity().size());
							}else{
								Toast.makeText(con,"You already Redeem That",Toast.LENGTH_LONG).show();
							}
							if(!PersistData.getBooleanData(con, AppConstant.isRatingShow)){
								dialog = new Dialog(con);
								dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
								dialog.setContentView(R.layout.signout_dialogue);
								TextView textviewYes = (TextView) dialog.findViewById(R.id.textviewYes);
								TextView textviewNo = (TextView) dialog.findViewById(R.id.textviewNo);
								textviewNo.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										// TODO Auto-generated method stub
										dialog.dismiss();
										finish();
									}
								});

								textviewYes.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										// TODO Auto-generated method stub
										PersistData.setBooleanData(con, AppConstant.isRatingShow, true);
										startActivity(new Intent(Intent.ACTION_VIEW, Uri
												.parse("https://play.google.com/store/apps/details?id="
														+ getPackageName())));
										dialog.dismiss();
										finish();
									}
								});

								dialog.show();
							}else{
								finish();
							}

						} else {
							Toast.makeText(con,mRedeemResponse.getMessage(),Toast.LENGTH_LONG).show();

							return;
						}



					}
				},
				new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						// Handle error

						if (busyNow != null) {
							busyNow.dismis();



						}
						Toast.makeText(con, "Registration failed!", Toast.LENGTH_LONG)
								.show();


					}
				});

// Add the request to the RequestQueue.
		mRequestQueue.add(stringRequest);




//
//
//
//		final AsyncHttpClient client = new AsyncHttpClient(true, 80, 443);
//		final RequestParams dataSet = new RequestParams();
//
//		dataSet.put("email", email);
//
//
//
//		client.get(con, "https://ipartypal.com/api/trunk/drinksoffer/redeemed", dataSet, new AsyncHttpResponseHandler() {
//
//					@Override
//					public void onStart() {
//						// called before request is started
//					}
//
//					@Override
//					public void onSuccess(int statusCode, Header[] headers,
//							byte[] response) {
//						// called when response HTTP status is "200 OK"
//
//						if (busyNow != null) {
//							busyNow.dismis();
//						}
//
//						Log.e("Login Server response : ", ":"+ new String(response));
//						Gson g=new Gson();
//						RedeemResponse mRedeemResponse=g.fromJson(new String(response), RedeemResponse.class);
//						if (mRedeemResponse.getError().equalsIgnoreCase("false")) {
//							if(!db.ifDollarDrinkExist(AppConstant.mDrinkDollarInfo.getBrand_offer_id(), AppConstant.mDrinkDollarInfo.getBar_name())){
//								AppConstant.mDrinkDollarInfo.setTime(timesInMilisecond);
//								db.addDollarDrink(AppConstant.mDrinkDollarInfo);
//								//Log.e("db size", ">>"+db.getAllGeneralActivity().size());
//							}else{
//								Toast.makeText(con,"You already Redeem That",Toast.LENGTH_LONG).show();
//							}
//							if(!PersistData.getBooleanData(con, AppConstant.isRatingShow)){
//								dialog = new Dialog(con);
//								dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//								dialog.setContentView(R.layout.signout_dialogue);
//								TextView textviewYes = (TextView) dialog.findViewById(R.id.textviewYes);
//								TextView textviewNo = (TextView) dialog.findViewById(R.id.textviewNo);
//								textviewNo.setOnClickListener(new OnClickListener() {
//
//									@Override
//									public void onClick(View v) {
//										// TODO Auto-generated method stub
//										dialog.dismiss();
//										finish();
//									}
//								});
//
//								textviewYes.setOnClickListener(new OnClickListener() {
//
//									@Override
//									public void onClick(View v) {
//										// TODO Auto-generated method stub
//									PersistData.setBooleanData(con, AppConstant.isRatingShow, true);
//									startActivity(new Intent(Intent.ACTION_VIEW, Uri
//											.parse("https://play.google.com/store/apps/details?id="
//													+ getPackageName())));
//									dialog.dismiss();
//									finish();
//									}
//								});
//
//								dialog.show();
//							}else{
//								finish();
//							}
//
//						} else {
//							Toast.makeText(con,mRedeemResponse.getMessage(),Toast.LENGTH_LONG).show();
//
//							return;
//						}
//
//					}
//
//					@Override
//					public void onFailure(int statusCode, Header[] headers,
//							byte[] errorResponse, Throwable e) {
//						// called when response HTTP status is "4XX" (eg. 401,
//						// 403, 404)
//
//						if (busyNow != null) {
//							busyNow.dismis();
//
//							Toast.makeText(con, "Registration failed!", 3000)
//									.show();
//
//						}
//					}
//
//					@Override
//					public void onRetry(int retryNo) {
//						// called when request is retried
//
//					}
//				});

	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		FlurryAgent.onStartSession(con);
		
	}
	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		FlurryAgent.onStartSession(con);
	}
	
	
}
