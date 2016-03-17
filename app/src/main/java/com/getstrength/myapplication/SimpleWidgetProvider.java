package com.getstrength.myapplication;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by Joe on 07/03/2016.
 */
public class SimpleWidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int count = appWidgetIds.length;

        for (int i = 0; i < count; i++) {
            int widgetId = appWidgetIds[i];

            Date curDate = new Date();
            SimpleDateFormat format = new SimpleDateFormat(ExerciseStore.DATE_FORMAT);
            String dateToday = format.format(curDate);

            ExerciseStore.initialise(context);
            List<String> mExercises = ExerciseStore.getInstance().getExerciseDates();
            Collections.sort(mExercises, new ExerciseStore.StringDateComparator());
            String lastDate = mExercises.get(mExercises.size() - 1);

            Date aDate = null;
            try {
                aDate = format.parse(lastDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Date bDate = null;
            try {
                bDate = format.parse(dateToday);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            Calendar with = Calendar.getInstance();
            with.setTime(aDate);
            Calendar to = Calendar.getInstance();
            to.setTime(bDate);
            to.set(Calendar.YEAR, with.get(Calendar.YEAR));
            int withDAY = with.get(Calendar.DAY_OF_YEAR);
            int toDAY = to.get(Calendar.DAY_OF_YEAR);

            int diffDay = toDAY - withDAY;
            String difference = Integer.toString(diffDay);

            RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                    R.layout.widget);
            remoteViews.setTextViewText(R.id.textViewWidget, difference);

            Intent intent = new Intent(context, SimpleWidgetProvider.class);
            intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                    0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setOnClickPendingIntent(R.id.actionButton, pendingIntent);
            appWidgetManager.updateAppWidget(widgetId, remoteViews);
        }
    }


}
