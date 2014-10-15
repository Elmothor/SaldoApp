package com.connective.android.saldo;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class NotifyMannagerMega{


	 NotificationManager mNotificationManager;
	 int SIMPLE_NOTIFICATION_ID = 4010; 

	public void playNotification(Context context, Class<?> cls, String textNotification, String titleNotification, int drawable,String fecha){
		
		
		 Notification notifyDetails;
		 /*NOTIFICATION INICIO*/
		 mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		 notifyDetails = new Notification(drawable,titleNotification,System.currentTimeMillis());
		 /*long[] vibrate = {100,100,200,300};
		 notifyDetails.vibrate = vibrate;*/
		 /*notifyDetails.defaults = Notification.DEFAULT_ALL;*/
		 notifyDetails.flags |= Notification.FLAG_ONGOING_EVENT;
		     
		 /*NOTIFICATION FIN*/ 
		  
		 CharSequence contentTitle = titleNotification;
		 CharSequence contentText = fecha;
		 
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