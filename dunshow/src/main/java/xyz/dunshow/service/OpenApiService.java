package xyz.dunshow.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import xyz.dunshow.constants.ApiKey;
import xyz.dunshow.constants.Server;
import xyz.dunshow.dto.InfoDto;
import xyz.dunshow.exception.BusinessException;
import xyz.dunshow.repository.ShowRoomRepository;
import xyz.dunshow.util.EncodeUtil;
import xyz.dunshow.util.EnumCodeUtil;

@Service
@RequiredArgsConstructor
@Slf4j
public class OpenApiService {

	private final EnumCodeUtil enumCodeUtil;

	private final JSONParser parse = new JSONParser();
	
    /**
     *  각 직업조회
     * @return
     */
    public JSONObject getJobs() {
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
     * https://api.neople.co.kr/df/items?itemName=<itemName>&wordType=<wordType>&q=minLevel:<minLevel>,maxLevel:<maxLevel>,rarity:<rarity>&limit=<limit>&apikey=
     */
    public JSONObject getItemSearch(String itemName, String wordType) {
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
    public JSONObject getAuction(String itemId, String itemName, String limit) {
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
    public JSONObject getAuctionSold(String itemId, String itemName, String limit) {
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
    public List<InfoDto> getCharacterId(String server, String name, String limit) {
    	if (StringUtils.isEmpty(this.enumCodeUtil.get(Server.CODE, server))) {
    		throw new BusinessException("서버 선택이 잘못되었습니다.");
    	}
    	if (StringUtils.isEmpty(name)) {
    	    throw new BusinessException("캐릭터명을 입력해 주세요.");
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
        
        JSONObject obj = this.callApi(sb.toString());
        List<InfoDto> list = Lists.newArrayList();
        InfoDto info;
		JSONArray jsonArr = (JSONArray) obj.get("rows");
		for (int i = 0; i < jsonArr.size(); i++) {
			info = new InfoDto();
			JSONObject obj2 = (JSONObject) jsonArr.get(i);
			info.setCharacterId(obj2.get("characterId").toString());
			info.setCharacterName(obj2.get("characterName").toString());
			info.setJobGrowName(obj2.get("jobGrowName").toString());
			info.setServerName(this.enumCodeUtil.get(Server.CODE, obj2.get("serverId").toString()));
			info.setServerId(obj2.get("serverId").toString());
			info.setImgSrc("https://img-api.neople.co.kr/df/servers/" + info.getServerId() +"/characters/"+ info.getCharacterId() + "?zoom=1");
			
			list.add(info);
		}
        
        return list;
    }
    
    /**
     *  마켓 등록물품 조회
     * @param title
     * @param emblemCode
     * @param rarity
     * @param jobId
     * @param limit
     * @return
     */
    public JSONObject getMarket(String title, String emblemCode, String rarity, String jobId, String limit) {
        
        StringBuilder sb = new StringBuilder();
        sb.append(ApiKey.NEOPLE_API_URL);
        sb.append("avatar-market/sale?title=");
        sb.append(title);
        sb.append("&q=jobId:");
        sb.append(jobId);
        sb.append(",emblemCode:");
        sb.append(emblemCode);
        sb.append(",avatarRarity");
        sb.append(rarity);
        sb.append("&sort=price:asc");
        sb.append("&limit=");
        sb.append(limit);
        sb.append("&apikey=");
        sb.append(ApiKey.NEOPLE_API_KEY);
        
        return this.callApi(sb.toString());
    }

    
    /**
     *  상세 조회
     * @param server
     * @param characterId
     * https://api.neople.co.kr/df/servers/<server>/characters/<characterId>?apikey=
     */
    public JSONObject getEquipAvatar(String server, String characterId) {
    	StringBuilder sb = new StringBuilder();
    	sb.append(ApiKey.NEOPLE_API_URL);
        sb.append("servers/");
        sb.append(server);
        sb.append("/characters/");
        sb.append(characterId);
        sb.append("/equip/avatar?apikey=");
        sb.append(ApiKey.NEOPLE_API_KEY);
        
        return this.callApi(sb.toString());
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    // 교불 조회할때 사용할것.
    // https://api.neople.co.kr/df/auction?itemName=[마창사]&wordType=full&sort=unitPrice:asc&q=rarity:레어,maxLevel:1&apikey=OLejPB3xs8EIqMVrvRNOrYp1eY3UD8oP
    public JSONObject callApi(String requestUrl) {
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();
        JSONObject obj;
        HttpURLConnection conn = null;
        
        try {
            log.info(requestUrl);
            URL url = new URL(requestUrl);
            conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("GET");
            String line;

            br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            while((line = br.readLine()) != null) {
                sb.append(line);
            }

            obj = (JSONObject)this.parse.parse(sb.toString());
        } catch (Exception e) {
            String line;
            if (conn.getErrorStream() != null) {
                br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                
                try {
                    while ((line = br.readLine()) != null) {
                        sb.append(line);
                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

            }
            System.out.println(e.getMessage());
            System.out.println(sb.toString());
        	if (e.getMessage().contains("response code: 503")) {
        		throw new BusinessException("서버 점검중");
        	} else {
        		throw new BusinessException("API 응답 에러");
        	}
        	
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (Exception e) {
                    throw new BusinessException("I/O close 에러");
                }
            }
        }

        return obj;
    }
    
    
    
    
    
    
    
    
    
    
    
    
    private final ShowRoomRepository showRoomRepository;
    @Transactional
    public void ioIdInsert() {
//        List<ShowRoom> entity = this.showRoomRepository.findAll();
//        StringBuilder sb = new StringBuilder();
//
//        for (ShowRoom s : entity) {
//            sb.setLength(0);
//            sb.append(ApiKey.NEOPLE_API_URL);
//            sb.append("items?itemName=");
//            sb.append(EncodeUtil.encodeURIComponent(s.getAvatarName()));
//            sb.append("&apikey=");
//            sb.append(ApiKey.NEOPLE_API_KEY);
//            String rs = callApi(sb.toString());
//
//            JSONParser parse = new JSONParser();
//            JSONObject object;
//            sb.setLength(0);
//            try {
//                object = (JSONObject) parse.parse(rs);
//                JSONArray stat = (JSONArray) object.get("rows");
//                if (!stat.isEmpty()) {
//                    for (int i = 0; i < stat.size(); i++) {
//                        JSONObject obj = (JSONObject) stat.get(i);
//                        sb.append(obj.get("itemRarity"));
//                        if (i != (stat.size() -1) ) { sb.append(","); }
//                    }
//                    s.setRarity(sb.toString());
//                    System.out.println("Insert 완료 : " + sb.toString());
//                }
//
//            } catch (ParseException e) {
//                System.out.println(sb.toString());
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//
//        }
    }
}
