package com.zzz.jobwork.task;

import com.squareup.okhttp.Response;
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
    public void onError(Response response, Exception e) {
        logger.info("onError:"+response+"    \n Exception"+e);
    }

    @Override
    public void onSuccess(Response response) {
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
