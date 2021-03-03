package xyz.dunshow.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.transaction.Transactional;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import lombok.RequiredArgsConstructor;
import xyz.dunshow.constants.ApiKey;
import xyz.dunshow.dto.JobDetailDto;
import xyz.dunshow.dto.ShowRoomDto;
import xyz.dunshow.entity.Emblem;
import xyz.dunshow.entity.Job;
import xyz.dunshow.entity.JobDetail;
import xyz.dunshow.entity.ShowRoom;
import xyz.dunshow.exception.BusinessException;
import xyz.dunshow.repository.EmblemRepository;
import xyz.dunshow.repository.JobDetailRepository;
import xyz.dunshow.repository.JobRepository;
import xyz.dunshow.repository.ShowRoomRepository;
import xyz.dunshow.util.EncodeUtil;
import xyz.dunshow.util.ObjectMapperUtils;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final ShowRoomRepository showRoomRepository;

    private final JobRepository jobRepository;

    private final JobDetailRepository jobDetailRepository;
    
    private final OpenApiService openApiService;
    
    private final EmblemRepository emblemRepository;

    private final String[] wearInfoArr = {"hair", "cap", "face", "neck", "coat", "belt", "pants", "shoes", "skin"};

    /**
     *  showroom data 초기화
     */
    @Transactional
    public void initShowRoomData() {
        ClassPathResource resource = new ClassPathResource("static/initData/showroomdata.txt");

        try {
            this.showRoomRepository.deleteAll();
            Path path = Paths.get(resource.getURI());
            List<String> content = Files.readAllLines(path);
            String[] arr;
            ShowRoomDto room;

            for (String s : content) {
                arr = s.split("▦");
                room = new ShowRoomDto();
                room.setAvatarName(arr[0]);
                room.setItemId(arr[1]);
                room.setPartsName(arr[2]);
                room.setPreviewIndex(arr[3]);
                room.setJobSeq(arr[4]);
                room.setRarity(arr[5]);

                this.showRoomRepository.save(room.toEntity(ShowRoom.class));
            }

        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
    }

    /**
     *  showroom 테이블에 있는 data 백업
     */
    public void bakShowRoomData() {
        List<ShowRoom> entity = this.showRoomRepository.findAll();
        if (entity == null) {
            throw new BusinessException("추출할 데이터가 없습니다.");
        }

        ClassPathResource resource = new ClassPathResource("static/initData/showroomdata.txt");

        try {
            Path path = Paths.get(resource.getURI());
            BufferedWriter br = new BufferedWriter(new FileWriter(path.toString(), false));

            for (ShowRoom s : entity) {
                br.append(s.getAvatarName());
                br.append("▦");
                br.append(s.getItemId());
                br.append("▦");
                br.append(s.getPartsName());
                br.append("▦");
                br.append(s.getPreviewIndex());
                br.append("▦");
                br.append(s.getJobSeq());
                br.append("▦");
                br.append(s.getRarity());
                br.newLine();
            }

            br.flush();
            br.close();
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
    }

    /**
     *  각 직업명 조회 후 저장
     */
    @Transactional
    public void getJobDetail() {
    	JSONObject object = this.openApiService.getJobs();
        this.jobDetailRepository.deleteAll();

        JobDetail jobDetail;
        List<JobDetail> list = Lists.newArrayList();

        JSONArray stat = (JSONArray) object.get("rows");
        for (int i = 0; i < stat.size(); i++) {
            JSONObject obj = (JSONObject) stat.get(i);
            String mainJob = (String) obj.get("jobName");
            Job entity = this.jobRepository.findByJobName(mainJob);

            if (entity == null) {
                throw new BusinessException("");
            }

            JSONArray childJob = (JSONArray) obj.get("rows");
            for (int j = 0; j < childJob.size(); j++) {
                jobDetail = new JobDetail();
                jobDetail.setJobValue(entity.getJobValue());
                JSONObject obj2 = (JSONObject) childJob.get(j);
                jobDetail.setFirstJob((String) obj2.get("jobGrowName"));
                //String firstId = (String) obj2.get("jobGrowId");
                if (obj2.containsKey("next")) {
                    JSONObject obj3 = (JSONObject) obj2.get("next");
                    jobDetail.setSecondJob((String) obj3.get("jobGrowName"));
                    //String secondId = (String) obj3.get("jobGrowId");
                    if (obj3.containsKey("next")) {
                        JSONObject obj4 = (JSONObject) obj3.get("next");
                        jobDetail.setThirdJob((String) obj4.get("jobGrowName"));
                        //String thirdId = (String) obj4.get("jobGrowId");
                        if (obj4.containsKey("next")) {
                            JSONObject obj5 = (JSONObject) obj4.get("next");
                            jobDetail.setFourthJob((String) obj5.get("jobGrowName"));
                            //String fourthId = (String) obj5.get("jobGrowId");
                        }
                    }
                }

                list.add(jobDetail);
            }
        }

        this.jobDetailRepository.saveAll(list);
    }

    /**
     *  dnfnow emblem 조회 후 저장
     */
    @Transactional
    public void getEmblem() {
    	List<JobDetail> list = this.jobDetailRepository.findAll();
    	if (list == null) { throw new BusinessException("data empty"); }
    	this.emblemRepository.deleteAll();
    	StringBuilder sb = new StringBuilder();
    	Document doc = null;
    	Emblem emblem;
    	List<Emblem> emblemList = Lists.newArrayList();
    	for (JobDetail j : list) {
    		sb.setLength(0);
    		sb.append("http://dnfnow.xyz/emblem?emblem_search=");
    		if ("9".equals(j.getJobValue())) {
    			sb.append(EncodeUtil.encodeURIComponent("다크나이트"));
    		} else if ("10".equals(j.getJobValue())) {
    			sb.append(EncodeUtil.encodeURIComponent("크리에이터"));
    		} else {
    			sb.append(EncodeUtil.encodeURIComponent(j.getFirstJob()));
    		}
    		
    		doc = this.openApiService.getJsoupConnect(sb.toString());
    		
    		Elements el = doc.select("#emblemtable > tbody > tr");
    		for (Element e : el) {
    			Element e2 = e.selectFirst("button");
    			if (e2 == null) { continue; }
    			emblem = new Emblem();
    			emblem.setEmblemId(e2.attr("onclick").substring(30).replace("'", ""));
    			emblem.setEmblemName(e2.text().trim());
    			emblem.setJobDetailSeq(j.getJobDetailSeq());
    			emblem.setBuffYn("N");
    			emblemList.add(emblem);
    		}
    	}
    	
    	this.emblemRepository.saveAll(ObjectMapperUtils.mapList(emblemList, Emblem.class));
    }
    
    // talisman
    
    
    // https://dundam.xyz/newVer/dealerRanking.jsp?page=0&type=4&job=%E7%9C%9E%20%EC%9B%A8%ED%8E%80%EB%A7%88%EC%8A%A4%ED%84%B0&baseJob=%EA%B7%80%EA%B2%80%EC%82%AC(%EB%82%A8)&weaponType=%EC%A0%84%EC%B2%B4
    // page 0 ->
    // type 4 고정
    // job
    // baseJob 외전
    // weaponType 전체
    // .image class characterid
    
    public void test() {
    	List<JobDetail> list = this.jobDetailRepository.findAll();
    	List<JobDetailDto> list2 = ObjectMapperUtils.mapList(list, JobDetailDto.class);
    	String a = "";
    	String b = "";
    	
    }
    
}
