package com.zzz.jobwork.task;


import com.zzz.jobwork.http.HttpResponse;

/**
 * Created by zl on 14/11/26.
 */
public interface OnHttpTaskListener {

    void onStart();

    void onError(HttpResponse response,Exception e);

    void onSuccess(HttpResponse response);

    void onRetry(int overRetry);

    void onFinsh();
}
