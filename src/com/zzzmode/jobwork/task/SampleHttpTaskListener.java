package com.zzzmode.jobwork.task;


import com.zzzmode.jobwork.http.HttpResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by zl on 14/11/26.
 */
public class SampleHttpTaskListener implements OnHttpTaskListener {
    static Logger logger= LogManager.getLogger(SampleHttpTaskListener.class);

    @Override
    public void onStart() {

        logger.info("onStart");
    }

    @Override
    public void onError(HttpResponse response, Exception e) {
        logger.info("onError:"+response+"    \n Exception"+e);
    }

    @Override
    public void onSuccess(HttpResponse response) {
        logger.info("onSuccess:"+response);
    }

    @Override
    public void onRetry(int overRetry) {
        logger.info("onRetry:"+overRetry);
    }

    @Override
    public void onFinsh() {
        logger.info("onFinsh");
    }
}
