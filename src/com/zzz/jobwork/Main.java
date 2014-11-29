package com.zzz.jobwork;

import com.zzz.jobwork.dao.MongoConnectManager;
import com.zzz.jobwork.dao.WorkRecordDAO;
import com.zzz.jobwork.http.HttpRequest;
import com.zzz.jobwork.model.WorkRecord;
import com.zzz.jobwork.task.TaskThreadPool;
import com.zzz.jobwork.utils.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.HttpCookie;
import java.util.List;
import java.util.Map;


public class Main {

    static Logger logger= LogManager.getLogger(Main.class);

    public static void main(String[] args) {

        try {

            TaskThreadPool.scanPool();
            //TaskThreadPool.scanWork(10);
            // for(int i=0;i<1;i++) {
            TaskThreadPool.scanWork(3);
            TaskThreadPool.scanWork(6);
            TaskThreadPool.scanWork(5);
             TaskThreadPool.scanWork(4);
            // TaskThreadPool.scanWork(1);
            // }

            if (true)
                return;

            HttpRequest req = HttpRequest.get("http://www.baidu.com");
            req.useProxy(null,80);
            //System.out.println(req.code()+"  __  "+req.body());

            Map<String, List<String>> headers = req.headers();

            for (Map.Entry<String, List<String>> stringListEntry : headers.entrySet()) {
                System.out.println(stringListEntry.getKey());
                System.out.println(stringListEntry.getValue());
                System.out.println("----------");
            }



            String[] header= req.headers("Set-Cookie");

            System.out.println(StringUtils.toString(header));



            System.out.println(HttpCookie.parse(StringUtils.toString(header)));






            WorkRecordDAO workRecordDAO = MongoConnectManager.getWorkRecordDAO();

            workRecordDAO.deleteAll();
            WorkRecord wr = new WorkRecord();
            wr.setStatus("200");







        }catch (Exception e){

        }

    }





}
