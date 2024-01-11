package com.example.wizard309.volley.net_utils;
import com.android.volley.toolbox.ImageLoader.ImageCache; import android.graphics.Bitmap;
import android.util.LruCache;

public class LruBitmapCache extends LruCache<String, Bitmap> implements
        ImageCache {
    /**
     * return cachesize
     * @return
     */
    public static int getDefaultLruCacheSize() {
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory()
                / 1024);
        final int cacheSize = maxMemory / 8;

        return cacheSize;
    }

    /**
     * gets default size
     */
    public LruBitmapCache() {
        this(getDefaultLruCacheSize());
    }

    /**
     *
     * @param sizeInKiloBytes
     */
    public LruBitmapCache(int sizeInKiloBytes) {
        super(sizeInKiloBytes);

    }

    /**
     * returns size
     * @param key
     * @param value
     * @return
     */
    @Override
    protected int sizeOf(String key, Bitmap value) {
        return value.getRowBytes() * value.getHeight() / 1024;
    }

    /**
     * gets bitmap
     * @param url
     * @return
     */
    @Override
    public Bitmap getBitmap(String url) {
        return get(url);
    }

    /**
     * puts bitmap
     * @param url
     * @param bitmap
     */
    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        put(url, bitmap);
    }
}