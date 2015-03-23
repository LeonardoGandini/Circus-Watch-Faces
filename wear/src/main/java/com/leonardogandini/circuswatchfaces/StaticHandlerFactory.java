package com.leonardogandini.circuswatchfaces;

/**
 * Created by Leonardo Gandini - leonardogandini.com on 3/23/15.
 */
import java.lang.ref.WeakReference;

import android.os.Handler;
import android.os.Message;

public class StaticHandlerFactory {

    public static StaticHandler create(IStaticHandler ref) {
        return new StaticHandler(ref);
    }

    // This has to be nested.
    static class StaticHandler extends Handler {
        WeakReference<IStaticHandler> weakRef;

        public StaticHandler(IStaticHandler ref) {
            this.weakRef = new WeakReference<IStaticHandler>(ref);
        }

        @Override
        public void handleMessage(Message message) {
            if (weakRef.get() == null) {
                throw new RuntimeException("Something goes wrong.");
            } else {
                weakRef.get().handleMessage(message);
            }
        }
    }
}
