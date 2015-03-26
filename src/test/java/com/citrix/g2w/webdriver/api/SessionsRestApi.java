package com.citrix.g2w.webdriver.api;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * Class to support broker api calls for sessions
 * @author ankitag1
 *
 */
public class SessionsRestApi {

    @Value("${g2w.url}")
    private String serviceUrl;
    
    private static RestTemplate restTemplate = new RestTemplate();

    static {
        restTemplate.setRequestFactory(new SimpleClientHttpRequestFactory());
    }

    public ResponseEntity<String> getSessionsForSetOfOrganizers(String token, List<Long> organizerKeys, Date from, Date to, String includes) {

        HttpHeaders sessionsApiHeader = new HttpHeaders();
        sessionsApiHeader.add("Content-Type", "application/json");
        sessionsApiHeader.add("Accept", "application/json");
        sessionsApiHeader.add("Authorization", "Token "+token);
        
        HttpEntity<String> httpEntity = new HttpEntity<String>(organizerKeys.toString(), sessionsApiHeader);
        
        if(from == null) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_YEAR, -1);
            from = calendar.getTime();
        }
        if(to == null) {
            to = new Date();
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String fromTime = simpleDateFormat.format(from) + "T00:00:00-0800";
        String toTime = simpleDateFormat.format(to) + "T23:59:59-0700";
        
        ResponseEntity<String> response = null;
        int count = 0;
        boolean retry = true;
        while (count < 10 && retry) {
            response = restTemplate.exchange(
                        serviceUrl + "/api/sessions?bulkType=organizers&fromTime="
                                + fromTime + "&toTime=" + toTime + "&includes="
                                + includes, HttpMethod.POST, httpEntity, String.class);
            if("{}".equals(response.getBody())) {
                try {
                    Thread.sleep(4000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }   
                count++;
            } else {
                retry = false;
            }
        }
        return response; 
    }
    
}
