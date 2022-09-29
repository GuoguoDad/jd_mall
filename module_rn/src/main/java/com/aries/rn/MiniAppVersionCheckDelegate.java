package com.aries.rn;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.aries.rn.BundleEntity;
import com.aries.rn.VersionCheckerDelegate;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.aries.rn.Constants.FAILURE;
import static com.aries.rn.Constants.SUCCESS;

public class MiniAppVersionCheckDelegate implements VersionCheckerDelegate {

    private final Context mContext;

    private final OkHttpClient client = new OkHttpClient();
    private final Gson gson = new Gson();

    public MiniAppVersionCheckDelegate(Context context) {
        this.mContext = context;
    }

    @Override
    public void processCheckResult(Map<String, Object> params, Callback callback) {
        Request request = new Request.Builder()
//                .url("https://oss.suning.com/sffe/sffe/aries/index.json")
                .url("https://oss.suning.com/sffe/sffe/70new/index.json")
                .build();

        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                callback.onResult(FAILURE, null);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String body = response.body().string();
                BaseResponse<Map<String, String>> res = gson.fromJson(body, new TypeToken<BaseResponse<Map<String, String>>>() {
                }.getType());

                Map<String, String> result = res.getData();
                BundleEntity entity = new BundleEntity();
                entity.setVersion(result.get("version"));
                entity.setUrl(result.get("ossUrl"));
                entity.setVerifyCode(result.get("md5code"));
                callback.onResult(SUCCESS, entity);
            }
        });
    }

    @Override
    public BundleEntity lastVersion(String name) {
        SharedPreferences sp = getSharedPreferences();
        if (null != sp) {
            String content = sp.getString(name, "{}");
            return gson.fromJson(content, new TypeToken<BundleEntity>() {
            }.getType());
        }
        return null;
    }

    @Override
    public void putVersion(String name, BundleEntity version) {
        String content = gson.toJson(version);
        SharedPreferences sp = getSharedPreferences();
        if (null != sp) {
            sp.edit().putString(name, content).apply();
        }
    }

    private SharedPreferences getSharedPreferences() {
        return mContext.getSharedPreferences(SP_VERSION_KEY, Context.MODE_PRIVATE);
    }

    @Override
    public boolean hasVersion(@Nullable BundleEntity oldVersion, BundleEntity newVersion) {
        String oVer = oldVersion.getVersion();
        String nVer = newVersion.getVersion();
        return !Objects.equals(nVer, oVer);
    }


    private static final String SP_VERSION_KEY = "mask_hub";

    static class BaseResponse<T> {
        private T data;
        private long code;
        private String message;

        public T getData() {
            return data;
        }

        public void setData(T data) {
            this.data = data;
        }

        public long getCode() {
            return code;
        }

        public void setCode(long code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
