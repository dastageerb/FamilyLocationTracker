package com.example.familyLocationTracker.fcm;


//public class NotificationHelper
//{

//    private static NotificationHelper instance;
//
//
//    private static NotificationHelper getInstance()
//    {
//        if(instance==null)
//        {
//          return   instance = new NotificationHelper();
//        }
//        return instance;
//    }
//

//    public static void buildNotification(Context context,String title,String body,String jobId)
//    {
//
//        NotificationChannel notificationChannel
//                = null;
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
//        {
//            notificationChannel = new NotificationChannel(
//                    Constants.NOTIFICATION_CHANNEL_ID,
//            Constants.NOTIFICATION_NAME,
//            NotificationManager.IMPORTANCE_HIGH);
//        }
//        //PendingIntent pendingIntent=PendingIntent.getActivity(getApplicationContext(),1,intent,0);
//
//
//
//
//        Intent jobDescription = new Intent(context, NotificationActivity.class);
//        jobDescription.putExtra(Constants.JOB_ID_INTENT_KEY,jobId);
//        jobDescription.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//
//        PendingIntent pendingIntent = PendingIntent.getActivity(context,2,jobDescription, PendingIntent.FLAG_UPDATE_CURRENT);
//
//
//
//        Notification notification= null;
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
//        {
//            notification = new Notification.Builder(context, Constants.NOTIFICATION_CHANNEL_ID)
//                    .setContentText(body)
//                    .setContentTitle(title)
//                    .setSmallIcon(R.drawable.logo)
//                    .setContentIntent(pendingIntent)
//                    .setAutoCancel(true)
//                    .build();
//        }
//
//        NotificationManager notificationManager=(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
//        {
//            notificationManager.createNotificationChannel(notificationChannel);
//        }
//        notificationManager.notify(1,notification);
//
//    } // buildNotification closed
//


//} // NotificationHelper closed
