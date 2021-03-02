package xyz.dunshow.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import lombok.RequiredArgsConstructor;
import xyz.dunshow.constants.ApiKey;
import xyz.dunshow.constants.Server;
import xyz.dunshow.dto.EnumCode;
import xyz.dunshow.entity.ShowRoom;
import xyz.dunshow.exception.BusinessException;
import xyz.dunshow.util.EncodeUtil;
import xyz.dunshow.util.EnumCodeUtil;

@Service
@RequiredArgsConstructor
public class OpenApiService {

	private final EnumCodeUtil enumCodeUtil;

    /**
     *  각 직업조회
     * @return
     */
    public String getJobs() {
        StringBuilder sb = new StringBuilder();
        sb.append(ApiKey.NEOPLE_API_URL);
        sb.append("jobs?apikey=");
        sb.append(ApiKey.NEOPLE_API_KEY);

        return callApi(sb.toString());
    }
    
    /**
     *  Jsoup Connect 
     * @param requestUrl
     * @return Document
     */
    public Document getJsoupConnect(String requestUrl) {
        Document doc = null;

        try {
            doc = Jsoup.connect(requestUrl).userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.116 Safari/537.36").validateTLSCertificates(false).get();
        } catch (IOException e) {
            throw new BusinessException(e.getMessage());
        }

        return doc;
    }
    
    /**
     *  아이템 검색
     * @param itemName
     * @param wordType(default-match) 동일 단어(match), 앞 단어 검색(front), 전문 검색(full)
     * https://api.neople.co.kr/df/items?itemName=<itemName>&wordType=<wordType>&q=minLevel:<minLevel>,maxLevel:<maxLevel>,rarity:<rarity>&limit=<limit>&apikey=OLejPB3xs8EIqMVrvRNOrYp1eY3UD8oP
     */
    public String getItemSearch(String itemName, String wordType) {
    	if (StringUtils.isEmpty(wordType)) {
    		wordType = "match";
    	}
        StringBuilder sb = new StringBuilder();
        sb.append(ApiKey.NEOPLE_API_URL);
        sb.append("items?itemName=");
        sb.append(EncodeUtil.encodeURIComponent(itemName));
        sb.append("&wordType=");
        sb.append(wordType);
        sb.append("&apikey=");
        sb.append(ApiKey.NEOPLE_API_KEY);

        return callApi(sb.toString());
    }
    
    /**
     *  경매장 등록 아이템 검색(id or name 필수)
     * @param itemId
     * @param itemName
     * @param limit default 10 max 400
     * @return
     */
    public String getAuction(String itemId, String itemName, String limit) {
    	if (StringUtils.isEmpty(itemId) && StringUtils.isEmpty(itemName)) {
    		throw new BusinessException("필수값이 누락되었습니다.");
    	}
    	if (StringUtils.isEmpty(limit)) {
    		limit = "10";
    	}
        StringBuilder sb = new StringBuilder();
        sb.append(ApiKey.NEOPLE_API_URL);
        sb.append("auction?");
        if (StringUtils.isEmpty(itemId)) {
        	sb.append("itemName=");
        	sb.append(EncodeUtil.encodeURIComponent(itemName));
        } else {
        	sb.append("itemId=");
        	sb.append(itemId);
        }
        sb.append("&limit=");
        sb.append(limit);
        sb.append("&sort=unitPrice:asc");
        sb.append("&apikey=");
        sb.append(ApiKey.NEOPLE_API_KEY);

        return callApi(sb.toString());
    }
    
    /**
     *  경매장 시세 검색 (최근 100개)
     * @param itemId
     * @param itemName
     * @param limit default 10 max 100
     * @return
     */
    public String getAuctionSold(String itemId, String itemName, String limit) {
    	if (StringUtils.isEmpty(itemId) && StringUtils.isEmpty(itemName)) {
    		throw new BusinessException("필수값이 누락되었습니다.");
    	}
    	if (StringUtils.isEmpty(limit)) {
    		limit = "10";
    	}
        StringBuilder sb = new StringBuilder();
        sb.append(ApiKey.NEOPLE_API_URL);
        sb.append("auction-sold?");
        if (StringUtils.isEmpty(itemId)) {
        	sb.append("itemName=");
        	sb.append(EncodeUtil.encodeURIComponent(itemName));
        } else {
        	sb.append("itemId=");
        	sb.append(itemId);
        }
        sb.append("&limit=");
        sb.append(limit);
        sb.append("&apikey=");
        sb.append(ApiKey.NEOPLE_API_KEY);

        return callApi(sb.toString());
    }
    
    /**
     *  캐릭터 고유아이디 조회
     * @param server 영문전달
     * @param name 
     * @param limit min 10 max 200
     * @return
     */
    public List<String> getCharacterId(String server, String name, String limit) {
    	if (StringUtils.isEmpty(enumCodeUtil.get(Server.CODE, server))) {
    		throw new BusinessException("서버 선택이 잘못되었습니다.");
    	}
        StringBuilder sb = new StringBuilder();
        sb.append(ApiKey.NEOPLE_API_URL);
        sb.append("servers/");
        sb.append(server);
        sb.append("/characters?characterName=");
        sb.append(EncodeUtil.encodeURIComponent(name));
        sb.append("&wordType=match");
        sb.append("&limit=");
        sb.append(limit);
        sb.append("&apikey=");
        sb.append(ApiKey.NEOPLE_API_KEY);
        
        String rs = this.callApi(sb.toString());
        JSONParser parse = new JSONParser();
        List<String> list = Lists.newArrayList();
        try {
        	// 하나도 없을때 어떻게되는지 해봐야함
			JSONObject obj = (JSONObject)parse.parse(rs);
			JSONArray jsonArr = (JSONArray) obj.get("rows");
			for (int i = 0; i < jsonArr.size(); i++) {
				JSONObject obj2 = (JSONObject) jsonArr.get(0);
				list.add(obj2.get("characterId").toString());
			}
		} catch (ParseException e) {
			throw new BusinessException("파싱 에러");
		}
        
        return list;
    }

    public String callApi(String requestUrl) {
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
