package trainedge.zapdiet;


import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationManagerCompat;

public class MyNewIntentService extends IntentService {
    private static final int NOTIFICATION_ID1 = 3;
    private static final int NOTIFICATION_ID2 = 4;
    private static final int NOTIFICATION_ID3 = 5;
    private static final int NOTIFICATION_ID4 = 6;
    private static final int NOTIFICATION_ID5 = 7;

    public MyNewIntentService() {
        super("MyNewIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Notification.Builder builder = new Notification.Builder(this);
        builder.setContentTitle("ZapDiet");
        builder.setContentText("Time to go for sleep");
        builder.setSmallIcon(R.drawable.happy);
        Intent notifyIntent = new Intent(this, Splash.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 2, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);
        Notification notificationCompat = builder.build();
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
        managerCompat.notify(NOTIFICATION_ID1, notificationCompat);
    }
}
