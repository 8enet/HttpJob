package com.zzz.jobwork.task;


import com.squareup.okhttp.Response;

/**
 * Created by zl on 14/11/26.
 */
public interface OnHttpTaskListener {

    void onStart();

    void onError(Response response,Exception e);

    void onSuccess(Response response);

    void onRetry(int overRetry);

    void onFinsh();
}
