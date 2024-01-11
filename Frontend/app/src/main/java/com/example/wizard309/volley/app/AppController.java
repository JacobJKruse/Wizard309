package com.example.wizard309.volley.app;
import com.example.wizard309.volley.net_utils.LruBitmapCache; import android.app.Application;
import com.example.wizard309.volley.net_utils.Const;
import com.android.volley.Request; import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader; import com.android.volley.toolbox.Volley;


public class AppController extends Application { public static final String TAG = AppController.class
        .getSimpleName();

    private RequestQueue mRequestQueue; private ImageLoader mImageLoader;
    private static AppController mInstance; @Override
    /**
     * inits volley instance
     */
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    /**
     *
     * @return instance
     */
    public static synchronized AppController getInstance() { return mInstance;

//	You don’t have to change anything in the class.

    }

    /**
     * inits and returns rquest queue
     * @return
     */
    public RequestQueue getRequestQueue() { if (mRequestQueue == null) {
        mRequestQueue = Volley.newRequestQueue(getApplicationContext());
    }

        return mRequestQueue;
    }

    /**
     *
     * @return image loader
     */
    public ImageLoader getImageLoader() { getRequestQueue();
        if (mImageLoader == null) {
            mImageLoader = new ImageLoader(this.mRequestQueue, new LruBitmapCache());
        }
        return this.mImageLoader;
    }

    /**
     * to add to queue
     * @param req
     * @param tag
     * @param <T>
     */
    public <T> void addToRequestQueue(Request<T> req, String tag) {
// set the default tag if tag is empty req.setTag(TextUtils.isEmpty(tag) ? TAG : tag); getRequestQueue().add(req);
    }

    /**
     * to add to queue
     * @param req
     * @param <T>
     */
    public <T> void addToRequestQueue(Request<T> req) { req.setTag(TAG);
        getRequestQueue().add(req);
    }

    /**
     * to cancle rquest
     * @param tag
     */
    public void cancelPendingRequests(Object tag) { if (mRequestQueue != null) {
        mRequestQueue.cancelAll(tag);
    }
    }
}

