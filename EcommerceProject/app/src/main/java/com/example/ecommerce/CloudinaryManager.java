package com.example.ecommerce;

import android.content.Context;

import com.cloudinary.android.MediaManager;

import java.util.HashMap;
import java.util.Map;

public class CloudinaryManager {
    private static boolean isInitialized = false;

    public static void init(Context context) {
        if (!isInitialized) {
            Map<String, Object> config = new HashMap<>();
            config.put("cloud_name", "dku2oqsgy");
            config.put("api_key", "881488226939423");
            config.put("api_secret", "XVTKFf5IFYzFv6GajJXeUpRuI70");
            MediaManager.init(context, config);
            isInitialized = true;
        }
    }
    public static MediaManager getInstance() {
        if (!isInitialized) {
            throw new IllegalStateException("MediaManager is not initialized. Call init() first.");
        }
        return MediaManager.get();
    }
}
