package xyz.dunshow.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

import javax.transaction.Transactional;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import lombok.RequiredArgsConstructor;
import xyz.dunshow.constants.ApiKey;
import xyz.dunshow.dto.ShowRoomDto;
import xyz.dunshow.entity.ShowRoom;
import xyz.dunshow.exception.BusinessException;
import xyz.dunshow.repository.JobRepository;
import xyz.dunshow.repository.ShowRoomRepository;
import xyz.dunshow.util.ObjectMapperUtils;

@Service
@RequiredArgsConstructor
public class FileIoService {

    private final ShowRoomRepository showRoomRepository;
    
    private final JobRepository jobRepository;

    private final String[] wearInfoArr = {"hair", "cap", "face", "neck", "coat", "belt", "pants", "shoes", "skin"};
    
    @Transactional
    public void fileReader(String job, String target) {
        BufferedReader inFile = null;
        
        try{
            String fileName = "";
            ShowRoomDto dto = null;
            List<ShowRoomDto> list = null;
            
            list = Lists.newArrayList();
            fileName = "C:\\node\\avatarInfoBak\\" + job + "_" + target + ".txt";
            
            //파일 객체 생성
            File file = new File(fileName);
            if(file.exists()) {
                inFile = new BufferedReader(new FileReader(file));
                String sLine = null;
                while( (sLine = inFile.readLine()) != null ) {
                    dto = new ShowRoomDto();
                    System.out.println(sLine);
                    String[] lineArr = sLine.split(",");
                    
                    if (lineArr.length == 4) {
                        dto.setJobSeq(lineArr[0]);
                        dto.setAvatarName(lineArr[1]);
                        dto.setPreviewIndex(lineArr[2]);
                        dto.setPartsName(lineArr[3]);
                        
                        list.add(dto);
                    }
                }

                this.showRoomRepository.saveAll(ObjectMapperUtils.mapList(list, ShowRoom.class));
            }

            inFile.close();
        }catch (FileNotFoundException e) {
            // TODO: handle exception
        }catch(IOException e){
            System.out.println(e);
        } finally {
            
        }
    }
    
    @Transactional
    public void ioIdInsert(String job, String target) {
        // List<ShowRoom> entity = this.showRoomRepository.findByJobSeqAndPartsName(job, target);
        List<ShowRoom> entity = this.showRoomRepository.findByItemIdIsNull();
        StringBuilder sb = new StringBuilder();
        
        for (ShowRoom s : entity) {
            sb.setLength(0);
            sb.append(ApiKey.NEOPLE_API_URL);
            sb.append("items?itemName=");
            sb.append(encodeURIComponent(s.getAvatarName()));
            sb.append("&apikey=");
            sb.append(ApiKey.NEOPLE_API_KEY);
            String rs = callApi(sb.toString());
            
            JSONParser parse = new JSONParser();
            JSONObject object;
            sb.setLength(0);
            try {
                object = (JSONObject) parse.parse(rs);
                JSONArray stat = (JSONArray) object.get("rows");
                if (!stat.isEmpty()) {
                    for (int i = 0; i < stat.size(); i++) {
                        JSONObject obj = (JSONObject) stat.get(i);
                        sb.append(obj.get("itemId"));
                        if (i != (stat.size() -1) ) { sb.append(","); }
                    }
                    s.setItemId(sb.toString());
                    System.out.println("Insert 완료 : " + sb.toString());
                }
                
            } catch (ParseException e) {
                System.out.println(sb.toString());
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            
        }
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
            e.printStackTrace();
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
    
    public String encodeURIComponent(String component) {
        String result = null;

        try {
            result = URLEncoder.encode(component, "UTF-8");
        } catch (Exception e) {
            result = component;
        }

        return result;
    }
    
    public void getJobDetail() {
        String rs = callApi("https://api.neople.co.kr/df/jobs?apikey=OLejPB3xs8EIqMVrvRNOrYp1eY3UD8oP");
        
        JSONParser parse = new JSONParser();
        JSONObject object;
        
        try {
            object = (JSONObject) parse.parse(rs);
            JSONArray stat = (JSONArray) object.get("rows");
            for (int i = 0; i < stat.size(); i++) {
                JSONObject obj = (JSONObject) stat.get(i);
                obj.containsKey("next");
                /*
                 * jobName으로 jobValue 조회해서 가지고있기
                 * (JSONArray) object.get("rows"); 로 하위 job 가져오기
                 * obj.containsKey("next"); 확인후 1,2,3 가져오기 jobdetail dto entity 만들기
                 * 
                 */
            }
            
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        
        
        
    }
}
