/**
 * Created by Leonardo Gandini - leonardogandini.com on 3/12/15.
 */
package com.leonardogandini.circuswatchfaces;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.wearable.watchface.CanvasWatchFaceService;
import android.support.wearable.watchface.WatchFaceService;
import android.support.wearable.watchface.WatchFaceStyle;
import android.text.format.Time;
import android.view.SurfaceHolder;

//import java.util.Calendar;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;


public class Elephant extends CanvasWatchFaceService {
    private class Engine extends CanvasWatchFaceService.Engine {
        private final long INTERACTIVE_UPDATE_RATE_MS = TimeUnit.SECONDS.toMillis(1);

        static final int MSG_UPDATE_TIME = 0;

        Bitmap background;
        Bitmap minuteHand;
        Bitmap hourHand;
        Bitmap secondHand;

        Bitmap backgroundScaled;
        Bitmap minuteHandScaled;
        Bitmap hourHandScaled;
        Bitmap secondHandScaled;

        Bitmap backgroundAmbient;
        Bitmap minuteHandAmbient;
        Bitmap hourHandAmbient;

        Bitmap backgroundScaledAmbient;
        Bitmap minuteHandScaledAmbient;
        Bitmap hourHandScaledAmbient;
        Bitmap secondHandScaledAmbient;

        Paint handsPaint;

        boolean mute;
        boolean registeredTimeZoneReceiver = false;
        Time time;

        final BroadcastReceiver timeZoneReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                time.clear(intent.getStringExtra("time-zone"));
                time.setToNow();
            }
        };



        /*final Handler updateTimeHandler = new Handler() {
            @Override
            public void handleMessage(Message message) {
                if (message.what != MSG_UPDATE_TIME)
                    return;
                invalidate();
                if (!timerShouldBeRunning())
                    return;
                long updateRate = TimeUnit.SECONDS.toMillis(3);
                long timeMs = System.currentTimeMillis();
                long delayMs = updateRate - (timeMs % updateRate);
                updateTimeHandler.sendEmptyMessageDelayed(MSG_UPDATE_TIME, delayMs);
            }
        };*/

        /* handler to update the time once a second in interactive mode */
        final Handler updateTimeHandler = new Handler() {
            @Override
            public void handleMessage(Message message) {
                switch (message.what) {
                    case MSG_UPDATE_TIME:
                        invalidate();
                        if (timerShouldBeRunning()) {
                            long timeMs = System.currentTimeMillis();
                            long delayMs = INTERACTIVE_UPDATE_RATE_MS
                                    - (timeMs % INTERACTIVE_UPDATE_RATE_MS);
                            updateTimeHandler
                                    .sendEmptyMessageDelayed(MSG_UPDATE_TIME, delayMs);
                        }
                        break;
                }
            }
        };

        @Override
        public void onAmbientModeChanged(boolean inAmbientMode) {
            super.onAmbientModeChanged(inAmbientMode);
            invalidate();
            updateTimer();
        }

        @Override
        public void onCreate(SurfaceHolder holder) {
            super.onCreate(holder);

            setWatchFaceStyle(new WatchFaceStyle.Builder(Elephant.this)
                    .setCardPeekMode(WatchFaceStyle.PEEK_MODE_SHORT)
                    .setBackgroundVisibility(WatchFaceStyle.BACKGROUND_VISIBILITY_INTERRUPTIVE)
                    .setViewProtection(WatchFaceStyle.PROTECT_STATUS_BAR |
                            WatchFaceStyle.PROTECT_HOTWORD_INDICATOR)
                    .build());

            System.gc();
            background = ((BitmapDrawable)
                    getDrawable(R.drawable.elephant_base)).getBitmap();
            hourHand = ((BitmapDrawable)
                    getDrawable(R.drawable.elephant_ore)).getBitmap();
            minuteHand = ((BitmapDrawable)
                    getDrawable(R.drawable.elephant_minuti)).getBitmap();
            backgroundAmbient = ((BitmapDrawable)
                    getDrawable(R.drawable.elephant_ambient_base)).getBitmap();
            hourHandAmbient = ((BitmapDrawable)
                    getDrawable(R.drawable.elephant_ambient_ore)).getBitmap();
            minuteHandAmbient = ((BitmapDrawable)
                    getDrawable(R.drawable.elephant_ambient_minuti)).getBitmap();
            secondHand = ((BitmapDrawable)
                    getDrawable(R.drawable.elephant_secondi)).getBitmap();


            handsPaint = new Paint();
            handsPaint.setFilterBitmap(true);
            handsPaint.setAntiAlias(false);

            time = new Time();

        }

        @Override
        public void onDestroy() {

            updateTimeHandler.removeMessages(MSG_UPDATE_TIME);
            updateTimeHandler.removeCallbacksAndMessages(null);
            updateTimeHandler.removeCallbacksAndMessages(MSG_UPDATE_TIME);
            super.onDestroy();

        }

        @Override
        public void onDraw(Canvas canvas, Rect bounds) {
            time.setToNow();



            /**-4 sweep-**/long now = System.currentTimeMillis();
            /**-4 sweep-**/int milliseconds = (int) (now % 1000);

            int width = bounds.width();
            int height = bounds.height();


            float ratio = (float) width / background.getWidth();

            if (backgroundScaled == null
                    || backgroundScaled.getWidth() != width
                    || backgroundScaled.getHeight() != height) {
                backgroundScaled =
                        Bitmap.createScaledBitmap(background,
                                (int) (background.getWidth() * ratio),
                                (int) (background.getHeight() * ratio), true);
                minuteHandScaled = Bitmap.createScaledBitmap(minuteHand,
                        (int) (minuteHand.getWidth() * ratio),
                        (int) (minuteHand.getHeight() * ratio), true);
                hourHandScaled = Bitmap.createScaledBitmap(hourHand,
                        (int) (hourHand.getWidth() * ratio),
                        (int) (hourHand.getHeight() * ratio), true);
                secondHandScaled = Bitmap.createScaledBitmap(secondHand,
                        (int) (secondHand.getWidth() * ratio),
                        (int) (secondHand.getHeight() * ratio), true);
                backgroundScaledAmbient =
                        Bitmap.createScaledBitmap(backgroundAmbient,
                                (int) (backgroundAmbient.getWidth() * ratio),
                                (int) (backgroundAmbient.getHeight() * ratio), true);
                minuteHandScaledAmbient = Bitmap.createScaledBitmap(minuteHandAmbient,
                        (int) (minuteHandAmbient.getWidth() * ratio),
                        (int) (minuteHandAmbient.getHeight() * ratio), true);
                hourHandScaledAmbient = Bitmap.createScaledBitmap(hourHandAmbient,
                        (int) (hourHandAmbient.getWidth() * ratio),
                        (int) (hourHandAmbient.getHeight() * ratio), true);
                           /* secondHandScaledAmbient = Bitmap.createScaledBitmap(secondHandAmbient,
                                    (int) (secondHandAmbient.getWidth() * ratio),
                                    (int) (secondHandAmbient.getHeight() * ratio), true);*/
            }
            canvas.drawBitmap(isInAmbientMode() ? backgroundScaledAmbient : backgroundScaled, 0, 0, null);



            float centerX = width / 2f;
            float centerY = height / 2f;


            float minuteRotation = time.minute / 30f * (float) Math.PI;
            float hourRotation = ((time.hour + time.minute / 60f) / 6f) * (float) Math.PI;

            float seconds = time.second + milliseconds / 1000f;
            float secondRotation = seconds / 30f * (float) Math.PI;

//***DEBUG***//

           /* float minuteRotation = time.second + milliseconds / 1000f;
            float hourRotation = time.second + milliseconds / 1000f -10;
            float secondRotation = time.second + milliseconds / 1000f -175;*/

            // Minute hand.
            //minuteRotation -= .42;
            minuteRotation -= .42;
            //float minuteCenterX = 0.34f;
            //float minuteCenterY = 0.79f;
            float minuteCenterX = 0.34f;
            float minuteCenterY = 0.79f;
            canvas.save();
            canvas.translate(centerX, centerY);
            canvas.rotate(minuteRotation / (float) Math.PI * 180);
            canvas.translate(-minuteCenterX * minuteHandScaled.getWidth(), -minuteCenterY * minuteHandScaled.getHeight());
            canvas.drawBitmap(isInAmbientMode() ? minuteHandScaledAmbient : minuteHandScaled, 0, 0, handsPaint);
            canvas.restore();


            float hourCenterX = 0.34f;
            float hourCenterY = 0.79f;
            canvas.save();
            hourRotation -= .42;
            // canvas.scale(1.2f,1.5f);
            canvas.translate(centerX, centerY);
            canvas.rotate(hourRotation / (float) Math.PI * 180);
            canvas.translate(-hourCenterX * hourHandScaled.getWidth(), -hourCenterY * hourHandScaled.getHeight());
            canvas.drawBitmap(isInAmbientMode() ? hourHandScaledAmbient : hourHandScaled, 0, 0, handsPaint);
            canvas.restore();


            if (!isInAmbientMode()) {
                // Second hand.
                secondRotation -= .42;
                float secondCenterX = 0.34f;
                float secondCenterY = 0.79f;
                canvas.save();
                canvas.translate(centerX, centerY);
                canvas.rotate(secondRotation / (float) Math.PI * 180);
                // canvas.rotate(secondRotation / (float) Math.PI));
                canvas.translate(-secondCenterX * secondHandScaled.getWidth(), -secondCenterY * secondHandScaled.getHeight());
                canvas.drawBitmap(isInAmbientMode() ? secondHandScaledAmbient : secondHandScaled, 0, 0, handsPaint);
                canvas.restore();
            }

            //**-4sweep Draw every frame as long as we're visible and in interactive mode.
            if (isVisible() && !isInAmbientMode()) {
                invalidate();
            }
        }

        @Override
        public void onInterruptionFilterChanged(int interruptionFilter) {
            super.onInterruptionFilterChanged(interruptionFilter);
            boolean inMuteMode = (interruptionFilter == WatchFaceService.INTERRUPTION_FILTER_NONE);
            if (mute == inMuteMode)
                return;
            mute = inMuteMode;
            invalidate();
        }

        @Override
        public void onTimeTick() {
            super.onTimeTick();
            invalidate();
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            super.onVisibilityChanged(visible);

            if (visible) {
                registerReceiver();
                time.clear(TimeZone.getDefault().getID());
                time.setToNow();
            } else {
                unregisterReceiver();
            }

            updateTimer();
        }

        private void registerReceiver() {
            if (registeredTimeZoneReceiver)
                return;

            registeredTimeZoneReceiver = true;
            Elephant.this.registerReceiver(timeZoneReceiver,
                    new IntentFilter(Intent.ACTION_TIMEZONE_CHANGED));
        }

        private boolean timerShouldBeRunning() {
            return isVisible() && !isInAmbientMode();
        }

        private void unregisterReceiver() {
            if (!registeredTimeZoneReceiver)
                return;
            registeredTimeZoneReceiver = false;
            Elephant.this.unregisterReceiver(timeZoneReceiver);
        }

        private void updateTimer() {
            updateTimeHandler.removeMessages(MSG_UPDATE_TIME);
            if (!timerShouldBeRunning())
                return;
            updateTimeHandler.sendEmptyMessage(MSG_UPDATE_TIME);
        }
    }

    @Override
    public Engine onCreateEngine() {
        return new Engine();
    }
}

