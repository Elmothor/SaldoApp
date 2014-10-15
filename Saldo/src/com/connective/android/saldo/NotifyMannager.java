package com.connective.android.saldo;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.TrafficStats;

public class NotifyMannager{
	long inicioBytes=0;
	long inicioBytesT=0;
	long inicioBytesm=0;
	long inicioBytesTm=0;
	int contador = 0;
	String consumo="";
	String recibido="";
	String enviado = "";
	DecimalFormat fmt = new DecimalFormat();
	TrafficStatsFile trafic = new TrafficStatsFile();
	 NotificationManager mNotificationManager;
	 int SIMPLE_NOTIFICATION_ID = 4010; 
public void inicio(){
	inicioBytesm = trafic.getMobileRxBytes();
	inicioBytesTm = trafic.getMobileTxBytes();
	inicioBytes = TrafficStats.getMobileRxBytes();
	inicioBytesT = TrafficStats.getMobileTxBytes();
	
}
private void Viejos(){
	 consumo = fmt
			.format(((((trafic.getMobileTxBytes() - inicioBytesTm) / 1000) * 0.00858))
					+ ((((trafic.getMobileRxBytes() - inicioBytesm) / 1000)) * 0.00858));
	 recibido = fmt
			.format((((trafic.getMobileRxBytes() - inicioBytesm) / 1000)));
	 enviado = fmt
			.format((((trafic.getMobileTxBytes() - inicioBytesTm) / 1000)));
	
}
private void Nuevos(){
	 consumo = fmt
				.format(((((TrafficStats.getMobileTxBytes() - inicioBytesT) / 1000) * 0.00858))
						+ ((((TrafficStats.getMobileRxBytes() - inicioBytes) / 1000)) * 0.00858));
		recibido = fmt
				.format((((TrafficStats.getMobileRxBytes() - inicioBytes) / 1000)));
		enviado = fmt
				.format((((TrafficStats.getMobileTxBytes() - inicioBytesT) / 1000)));
}
	public void playNotification(Context context, Class<?> cls, String textNotification, String titleNotification, int drawable){
		if(contador == 0){
			inicio();
			contador++;
		}
		
		
		DecimalFormatSymbols fmts = new DecimalFormatSymbols();
		fmts.setGroupingSeparator('.');
		fmt.setGroupingSize(3);
		fmt.setGroupingUsed(true);
		fmt.setDecimalFormatSymbols(fmts);
		
		/*bla bla bla*/
		
		final int currentapiVersion = android.os.Build.VERSION.SDK_INT;
		if (currentapiVersion > 13) {
			Nuevos();
		}else{
			Viejos();
		}
		
		
		 /*NOTIFICATION VARS*/
		
		
		 Notification notifyDetails;
		 /*NOTIFICATION INICIO*/
		 mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		 notifyDetails = new Notification(drawable,titleNotification,System.currentTimeMillis());
		 /*long[] vibrate = {100,100,200,300};
		 notifyDetails.vibrate = vibrate;*/
		 /*notifyDetails.defaults = Notification.DEFAULT_ALL;*/
		 notifyDetails.flags |= Notification.FLAG_ONGOING_EVENT;
		     
		 /*NOTIFICATION FIN*/ 
		  
		 CharSequence contentTitle = "Consumo ¢" + consumo;
		 CharSequence contentText = recibido + "kb/recibido     " + enviado + "kb/enviado";
		 
		 Intent notifyIntent = new Intent(context, cls );
		 
		 notifyIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		     
		 PendingIntent intent = PendingIntent.getActivity(context, 0, notifyIntent, android.content.Intent.FLAG_ACTIVITY_NEW_TASK);
		     
		 notifyDetails.tickerText = textNotification;
		 notifyDetails.setLatestEventInfo(context, contentTitle, contentText, intent);
		    
		 try{
		    mNotificationManager.notify(SIMPLE_NOTIFICATION_ID, notifyDetails);     
		 } catch(Exception e){
		      
		 }
		}
	public void cancelar(){
	mNotificationManager.cancel(SIMPLE_NOTIFICATION_ID);
	}
	
}