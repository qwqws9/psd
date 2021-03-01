package xyz.dunshow.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import xyz.dunshow.exception.BusinessException;

@Service
@RequiredArgsConstructor
public class OpenApiService {

    /**
     *  URL 인코딩
     * @param component
     * @return
     */
    public String encodeURIComponent(String component) {
        String result = null;

        try {
            result = URLEncoder.encode(component, "UTF-8");
        } catch (Exception e) {
            result = component;
        }

        return result;
    }

    
    private String callApi(String requestUrl) {
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

        try {
            URL url = new URL(requestUrl);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("GET");
            br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

            String line;
            while((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (Exception e) {
            throw new BusinessException("API 응답 에러");
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (Exception e) {
                    throw new BusinessException("I/O close 에러");
                }
            }
        }

        return sb.toString();
    }
}
