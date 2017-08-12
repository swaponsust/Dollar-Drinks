package com.aapbd.partylink;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aapbd.gpsservice.model.DrinkDollarInfo;
import com.aapbd.partylink.contact.ContactListActivity;
import com.aapbd.partylink.db.DatabaseHandler;
import com.aapbd.partylink.utils.AppConstant;
import com.aapbd.partylink.utils.NukeSSLCerts;
import com.aapbd.utils.nagivation.StartActivity;
import com.aapbd.utils.notification.BusyDialog;
import com.flurry.android.FlurryAgent;
import com.squareup.picasso.Picasso;

import java.util.Vector;

public class DollarDrinkActivity extends Activity  {

	Context con;
	private BusyDialog busyNow;
	TextView textviewSend,textviewDasboard,textviewReport;
	ListView listviewDollar;
	Typeface normal,boldFont,colorFont;
	ImageView imageviewNoDollar;
	RelativeLayout relativelayoutMain;
	DatabaseHandler db;
	Vector<DrinkDollarInfo> temp;
	long timesInMilisecond;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dollar_drink);
		con = this;
		new NukeSSLCerts().nuke();
		db=new DatabaseHandler(con);
		initUI();

	}

	private void initUI() {
		// TODO Auto-generated method stubGe
		relativelayoutMain=(RelativeLayout)findViewById(R.id.relativelayoutMain);
		imageviewNoDollar=(ImageView)findViewById(R.id.imageviewNoDollar);
		textviewReport=(TextView)findViewById(R.id.textviewReport);
		textviewDasboard=(TextView)findViewById(R.id.textviewDasboard);
		textviewSend=(TextView)findViewById(R.id.textviewSend);
		listviewDollar=(ListView)findViewById(R.id.listviewDollar);
		normal = Typeface.createFromAsset(con.getAssets(), "fonts/HelveticaNeueLTStd Lt.otf");
		boldFont = Typeface.createFromAsset(con.getAssets(), "fonts/HelveticaNeueLTStd Md.OTF");
		colorFont = Typeface.createFromAsset(con.getAssets(), "fonts/Anodyne_0.otf");
		textviewSend.setTypeface(normal);
		textviewDasboard.setTypeface(normal);
		textviewReport.setTypeface(normal);
		if(temp!=null){
			temp.removeAllElements();
		}
		textviewSend.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AppConstant.adMessage="Get $1 Drinks, Any Time, Any Day With PartyLink Download https://bnc.lt/dollar_drinks";
				StartActivity.toActivity(con, ContactListActivity.class);
			}
		});
		
		textviewDasboard.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		textviewReport.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						AppConstant.webUrl="http://partylinkevents.com/report-a-redemption-problem/";
						StartActivity.toActivity(con, WebActivity.class);
					}
				});

	}

	public void backBtn(View v){
			finish();
		}
		
		
@Override
protected void onResume() {
	// TODO Auto-generated method stub
	super.onResume();
	timesInMilisecond=System.currentTimeMillis();
	if(AppConstant.mDrinkDollarInfoList.size()>0){
		relativelayoutMain.setVisibility(View.VISIBLE);
		imageviewNoDollar.setVisibility(View.GONE);
		DollarDrinkAdapter mDollarDrinkAdapter=new DollarDrinkAdapter(con);
		listviewDollar.setAdapter(mDollarDrinkAdapter);
		mDollarDrinkAdapter.notifyDataSetChanged();
		
	}else{
		relativelayoutMain.setVisibility(View.GONE);
		imageviewNoDollar.setVisibility(View.VISIBLE);
	}
	
}
public long getHours(long milisecond){
	long time=(milisecond)/(60*60*1000);
	return time;
	
}
	
	class DollarDrinkAdapter extends ArrayAdapter<DrinkDollarInfo> {
		Context context;
		
		DollarDrinkAdapter(Context context) {
			super(context, R.layout.dolar_drink_rows, AppConstant.mDrinkDollarInfoList);
			this.context = context;
		}

		private class ViewHolder {
			TextView textviewBarName,textviewCity;
			TextView textviewBrandOffer,textviewAddress,textviewOverlayLeft,textviewOverlayRight;
			ImageView imageviewDoller;
			LinearLayout linearOverlayout;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			final ViewHolder holder;
			final LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			if (convertView == null) {
				convertView = inflater.inflate(R.layout.dolar_drink_rows, null);
				holder = new ViewHolder();
				holder.textviewCity=(TextView)convertView.findViewById(R.id.textviewCity);
				holder.linearOverlayout=(LinearLayout)convertView.findViewById(R.id.linearOverlayout);
				holder.textviewOverlayRight=(TextView)convertView.findViewById(R.id.textviewOverlayRight);
				holder.textviewOverlayLeft=(TextView)convertView.findViewById(R.id.textviewOverlayLeft);
				holder.imageviewDoller = (ImageView) convertView.findViewById(R.id.imageviewDoller);
				holder.textviewBarName = (TextView) convertView.findViewById(R.id.textviewBarName);
				holder.textviewBrandOffer = (TextView) convertView.findViewById(R.id.textviewBrandOffer);
				holder.textviewAddress = (TextView) convertView.findViewById(R.id.textviewAddress);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			
				if(AppConstant.mDrinkDollarInfoList.size()>30){
					if (position < 30) {
						final DrinkDollarInfo query = AppConstant.mDrinkDollarInfoList.get(position);
						
						holder.textviewBarName.setText("" + query.getBar_name());
						holder.textviewBrandOffer.setText("$"+query.getDrink_price()+" " + query.getBrand_offer());
						holder.textviewAddress.setText(query.getBar_address());
						holder.textviewCity.setText(query.getBar_city());
						//holder.textviewOverlayRight.setText("Hours");
						holder.textviewBarName.setTypeface(boldFont);
						holder.textviewBrandOffer.setTypeface(boldFont);
						holder.textviewAddress.setTypeface(normal);
						holder.textviewOverlayLeft.setTypeface(colorFont);
						holder.textviewOverlayRight.setTypeface(colorFont);
						holder.textviewCity.setTypeface(normal);
						try {
							Picasso.with(con).load(query.getBrand_logo_url())
									.error(R.drawable.ic_launcher)
									.into(holder.imageviewDoller);
						} catch (final Exception e) {
							e.printStackTrace();
						}
						if(db.ifDollarDrinkExist(query.getBrand_offer_id(), query.getBar_name())){
							temp=db.getDollarDrinksInfo(query.getBrand_offer_id(), query.getBar_name());
							 long newhours =(((getHours(temp.get(0).getTime())+(18)))-(getHours(timesInMilisecond)));
							 Log.e("newhours", ">>"+newhours);
							 if(newhours==1){
								 holder.textviewOverlayRight.setText(newhours+" Hour");
							 }else{
								 holder.textviewOverlayRight.setText(newhours+" Hours");
							 }
							if(((getHours(temp.get(0).getTime())+18))<=(getHours(timesInMilisecond))){
								
								db.deleteDollarDrink(query.getBrand_offer_id(), query.getBar_name());
								DollarDrinkAdapter mDollarDrinkAdapter=new DollarDrinkAdapter(con);
								listviewDollar.setAdapter(mDollarDrinkAdapter);
								mDollarDrinkAdapter.notifyDataSetChanged();
							}
							holder.linearOverlayout.setVisibility(View.VISIBLE);
						}else{
							holder.linearOverlayout.setVisibility(View.GONE);
						}
	
						convertView.setOnClickListener(new OnClickListener() {
	
							@Override
							public void onClick(View v) {
								DrinkDollarInfo query = AppConstant.mDrinkDollarInfoList.get(position);
								
								if(db.ifDollarDrinkExist(query.getBrand_offer_id(), query.getBar_name())){
								
								}else{
									AppConstant.mDrinkDollarInfo=query;
									StartActivity.toActivity(con, DollarDrinkDetailsActivity.class);
								}
								
	
							}
						});
					}
					
				}else{
					if (position < AppConstant.mDrinkDollarInfoList.size()) {
						final DrinkDollarInfo query = AppConstant.mDrinkDollarInfoList.get(position);
						
						holder.textviewBarName.setText("" + query.getBar_name());
						holder.textviewBrandOffer.setText("$"+query.getDrink_price()+" " + query.getBrand_offer());
						holder.textviewAddress.setText(query.getBar_address());
						holder.textviewCity.setText(query.getBar_city());
						//holder.textviewOverlayRight.setText("Hours");
						holder.textviewBarName.setTypeface(boldFont);
						holder.textviewBrandOffer.setTypeface(boldFont);
						holder.textviewAddress.setTypeface(normal);
						holder.textviewOverlayLeft.setTypeface(colorFont);
						holder.textviewOverlayRight.setTypeface(colorFont);
						holder.textviewCity.setTypeface(normal);
						try {
							Picasso.with(con).load(query.getBrand_logo_url())
									.error(R.drawable.ic_launcher)
									.into(holder.imageviewDoller);
						} catch (final Exception e) {
							e.printStackTrace();
						}
						
						if(db.ifDollarDrinkExist(query.getBrand_offer_id(), query.getBar_name())){
							holder.linearOverlayout.setVisibility(View.VISIBLE);
							
							temp=db.getDollarDrinksInfo(query.getBrand_offer_id(), query.getBar_name());

                            if(temp.size()>0) {
                                long newhours = (((getHours(temp.get(0).getTime()) + (18))) - (getHours(timesInMilisecond)));
                                Log.e("newhours", ">>" + newhours);
                                if (newhours == 1) {
                                    holder.textviewOverlayRight.setText(newhours + " Hour");
                                } else {
                                    holder.textviewOverlayRight.setText(newhours + " Hours");
                                }

                                if (((getHours(temp.get(0).getTime()) + 18)) <= (getHours(timesInMilisecond))) {

                                    db.deleteDollarDrink(query.getBrand_offer_id(), query.getBar_name());
                                    DollarDrinkAdapter mDollarDrinkAdapter = new DollarDrinkAdapter(con);
                                    listviewDollar.setAdapter(mDollarDrinkAdapter);
                                    mDollarDrinkAdapter.notifyDataSetChanged();
                                }
                            }
							
						}else{
							holder.linearOverlayout.setVisibility(View.GONE);
						}
	
						convertView.setOnClickListener(new OnClickListener() {
	
							@Override
							public void onClick(View v) {
								DrinkDollarInfo query = AppConstant.mDrinkDollarInfoList.get(position);
								if(db.ifDollarDrinkExist(query.getBrand_offer_id(), query.getBar_name())){
									
								}else{
									AppConstant.mDrinkDollarInfo=query;
									StartActivity.toActivity(con, DollarDrinkDetailsActivity.class);
								}
	
							}
						});
					}
					
				}

			
			return convertView;

		}
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
