package xyz.dunshow.service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import lombok.RequiredArgsConstructor;
import xyz.dunshow.constants.ClassPath;
import xyz.dunshow.dto.EmblemDto;
import xyz.dunshow.dto.EmblemRateDto;
import xyz.dunshow.dto.JobDetailDto;
import xyz.dunshow.dto.OptionAbilityDto;
import xyz.dunshow.dto.ShowRoomDto;
import xyz.dunshow.entity.Emblem;
import xyz.dunshow.entity.EmblemRate;
import xyz.dunshow.entity.Job;
import xyz.dunshow.entity.JobDetail;
import xyz.dunshow.entity.OptionAbility;
import xyz.dunshow.entity.RankData;
import xyz.dunshow.entity.ShowRoom;
import xyz.dunshow.exception.BusinessException;
import xyz.dunshow.mapper.JobDetailMapper;
import xyz.dunshow.repository.EmblemRateRepository;
import xyz.dunshow.repository.EmblemRepository;
import xyz.dunshow.repository.JobDetailRepository;
import xyz.dunshow.repository.JobRepository;
import xyz.dunshow.repository.OptionAbilityRepository;
import xyz.dunshow.repository.RankDataRepository;
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
    
    private final JobDetailMapper jobDetailMapper;
    
    private final RankDataRepository rankdataRepository;
    
    private final OptionAbilityRepository optionAbilityRepository;
    
    private final EmblemRateRepository emblemRateRepository;
    
    private final List<String> buff = Arrays.asList(
            "오버드라이브",
            "잔영의 케이가",
            "폭주",
            "살의의 파동",
            "귀혼일체",
            "신검합일",
            "광폭화",
            "오기조원",
            "컨제스트",
            "역혈기공",
            "강권",
            "뒷골목 싸움법",
            "반드시 잡는다!",
            "카이",
            "독 바르기",
            "데스 바이 리볼버",
            "미라클 비전",
            "로보틱스",
            "오버 차지",
            "마나 폭주",
            "공명",
            "윈드니스",
            "블러드 번",
            "경계망상",
            "엘레멘탈 번",
            "고대의 도서관",
            "환수 폭주",
            "전장의 여신",
            "금단의 저주",
            "영광의 축복",
            "성령의 메이스",
            "섀도우 박서",
            "추락하는 영혼",
            "열정의 챠크라",
            "광명의 챠크라",
            "용맹의 축복",
            "광적인 믿음",
            "신탁의 기원",
            "일곱개의 대죄",
            "셰이크 다운",
            "암흑의 의식",
            "화둔:홍염",
            "암살자의 마음가짐",
            "워크라이",
            "브레인 스톰",
            "페이스풀",
            "폭음폭식",
            "마창 해방",
            "오러 랜스",
            "마나 익스트랙트",
            "다크니스",
            "증폭",
            "오버플로우",
            "임무 시작",
            "역전의 승부사",
            "전술 지휘",
            "코어 프렉시스"
            );

    /**
     *  classPath 파일 읽어오기
     * @param classPath
     * @return
     */
    private List<String> initData(String classPath) {
        ClassPathResource resource = new ClassPathResource(classPath);
        try {
            Path path = Paths.get(resource.getURI());
            return Files.readAllLines(path);
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
    }

    /**
     *  showroom data 초기화
     */
    @Transactional
    public void initShowRoomData() {
        String[] arr;
        ShowRoomDto room;
        this.showRoomRepository.deleteAll();
        List<String> content = this.initData(ClassPath.SHOWROOM);

        for (String s : content) {
            arr = s.split("▦");
            room = new ShowRoomDto();
            room.setAvatarName(arr[0]);
            room.setItemId(arr[1]);
            room.setPartsName(arr[2]);
            room.setPreviewIndex(arr[3]);
            room.setJobValue(arr[4]);
            room.setRarity(arr[5]);

            this.showRoomRepository.save(room.toEntity(ShowRoom.class));
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

        ClassPathResource resource = new ClassPathResource(ClassPath.SHOWROOM);

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
                br.append(s.getJobValue());
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
     *  emblem data 초기화
     */
    @Transactional
    public void initEmblemData() {
        String[] arr;
        EmblemDto emblem;
        this.emblemRepository.deleteAll();

        List<String> content = this.initData(ClassPath.EMBLEM);

        for (String s : content) {
            arr = s.split("▦");
            emblem = new EmblemDto();
            emblem.setBuffYn(arr[0]);
            emblem.setEmblemId(arr[1]);
            emblem.setEmblemName(arr[2]);
            emblem.setJobDetailSeq(Integer.parseInt(arr[3]));

            this.emblemRepository.save(emblem.toEntity(Emblem.class));
        }
    }

    /**
     *  emblem 테이블에 있는 data 백업
     */
    public void bakEmblemData() {
        List<Emblem> entity = this.emblemRepository.findAll();
        if (entity == null) {
            throw new BusinessException("추출할 데이터가 없습니다.");
        }

        ClassPathResource resource = new ClassPathResource(ClassPath.EMBLEM);

        try {
            Path path = Paths.get(resource.getURI());
            BufferedWriter br = new BufferedWriter(new FileWriter(path.toString(), false));

            for (Emblem s : entity) {
                br.append(s.getBuffYn());
                br.append("▦");
                br.append(s.getEmblemId());
                br.append("▦");
                br.append(s.getEmblemName());
                br.append("▦");
                br.append(s.getJobDetailSeq()+"");
                br.newLine();
            }

            br.flush();
            br.close();
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
    }
    
    /**
     *  dnfnow emblem 조회 후 저장
     */
    @Transactional
    public void getEmblem() {
        List<JobDetail> list = this.jobDetailRepository.findAll();
        if (CollectionUtils.isEmpty(list)) { throw new BusinessException("JobDetail data empty"); }
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
                sb.append(EncodeUtil.encodeURIComponent(j.getSecondJob()));
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
                for (String s : this.buff) {
                    if (emblem.getEmblemName().contains(s)) {
                        emblem.setBuffYn("Y");
                        break;
                    }
                    emblem.setBuffYn("N");
                }
                
                emblemList.add(emblem);
            }
        }
        
        this.emblemRepository.saveAll(ObjectMapperUtils.mapList(emblemList, Emblem.class));
    }
    
    // https://dundam.xyz/newVer/dealerRanking.jsp?page=0&type=4&job=%E7%9C%9E%20%EC%9B%A8%ED%8E%80%EB%A7%88%EC%8A%A4%ED%84%B0&baseJob=%EA%B7%80%EA%B2%80%EC%82%AC(%EB%82%A8)&weaponType=%EC%A0%84%EC%B2%B4
    // page 0 ->
    // type 4 고정
    // job
    // baseJob 외전
    // weaponType 전체
    // .image class characterid
    /**
     *  랭킹 1~200위 조회
     */
    @Transactional
    public void getRankByDundam() {
        List<JobDetailDto> list = this.jobDetailMapper.selectJobDetailList(new JobDetailDto());
        StringBuilder sb = new StringBuilder();
        this.rankdataRepository.deleteAll();
        RankData rank;

        for (JobDetailDto j : list) {
            List<RankData> rankList = Lists.newArrayList();
            for (int i = 0; i < 20; i++) {
                sb.setLength(0);
                sb.append("https://dundam.xyz/newVer/dealerRanking.jsp?page=");
                sb.append(i);
                sb.append("&type=4&job=");
                if ("9".equals(j.getJobValue()) || "10".equals(j.getJobValue())) {
                    sb.append(EncodeUtil.encodeURIComponent(j.getJob().getJobName()));
                    sb.append("&baseJob=");
                    sb.append(EncodeUtil.encodeURIComponent("외전"));
                } else {
                    if (StringUtils.isEmpty(j.getFourthJob())) {
                        sb.append(EncodeUtil.encodeURIComponent(j.getThirdJob()));
                    } else {
                        sb.append(EncodeUtil.encodeURIComponent(j.getFourthJob()));
                    }
                    
                    sb.append("&baseJob=");
                    sb.append(EncodeUtil.encodeURIComponent(j.getJob().getJobName()));
                }
                
                sb.append("&weaponType=%EC%A0%84%EC%B2%B4");
                Document doc = this.openApiService.getJsoupConnect(sb.toString());
                Elements els = doc.select(".image");
                for (Element e : els) {
                    rank = new RankData();
                    rank.setCharacterId(e.select("img").attr("characterid"));
                    rank.setServerId(e.select("img").attr("server"));
                    rank.setJobDetailSeq(j.getJobDetailSeq());
                    System.out.println(rank.getCharacterId() + "_____" + rank.getServerId());
                    rankList.add(rank);
                }
            }
            
            this.rankdataRepository.saveAll(rankList);
        }
        
    }
    
    /**
     *  Rank Data 추출
     */
    public void bakRankData() {
        ClassPathResource resource = new ClassPathResource(ClassPath.RANK);
        List<RankData> list = this.rankdataRepository.findAll();
        try {
            Path path = Paths.get(resource.getURI());
            BufferedWriter br = new BufferedWriter(new FileWriter(path.toString(), false));
            
            for (RankData r : list) {
                br.append(r.getCharacterId());
                br.append("▦");
                br.append(r.getServerId());
                br.append("▦");
                br.append(r.getJobDetailSeq()+"");
                br.newLine();
            }
            
            br.flush();
            br.close();
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
    }
    
    
    
    /**
     *  rank data 초기화
     */
    @Transactional
    public void initRankData() {
        this.rankdataRepository.deleteAll();
        List<String> content = this.initData(ClassPath.RANK);
        String[] arr;
        RankData rank;

        for (String s : content) {
            arr = s.split("▦");
            rank = new RankData();
            rank.setCharacterId((arr[0]));
            rank.setServerId(arr[1]);
            rank.setJobDetailSeq(Integer.parseInt(arr[2]));

            this.rankdataRepository.save(rank);
        }
    }
    
    /**
     *  Rank Data로 Emblem,Option 비율 초기화
     */
    @Transactional
    public void initOptionAndEmblemByRankData() {
        List<RankData> entity = this.rankdataRepository.findAll();
        if (CollectionUtils.isEmpty(entity)) {
            throw new BusinessException("Rank Data Empty");
        }

        this.emblemRateRepository.deleteAll();
        this.optionAbilityRepository.deleteAll();

        int prevDetailSeq = 0;
        EmblemRateDto emblemRateDto;
        List<EmblemRateDto> emblemDtoList;
        OptionAbilityDto optionAbilityDto;
        List<OptionAbilityDto> optionDtoList;
        Map<String, Map<String, Integer>> optionAbilityMap = Maps.newHashMap();
        Map<String, Map<String, Integer>> emblemsMap = Maps.newHashMap();

        for (RankData r : entity) {
            if (prevDetailSeq != r.getJobDetailSeq()) {
                // emblemsMap 처리
                for (Map.Entry<String, Map<String, Integer>> entry : emblemsMap.entrySet()) {
                    int detailCount = 0;
                    emblemDtoList = Lists.newArrayList();
                    for (Map.Entry<String, Integer> el : entry.getValue().entrySet()) {
                        emblemRateDto = new EmblemRateDto();
                        emblemRateDto.setEmblemName(el.getKey()); // detail Emblem Name
                        emblemRateDto.setEmblemColor(entry.getKey());
                        emblemRateDto.setRate(String.valueOf(el.getValue())); // 개별 카운트
                        emblemRateDto.setJobDetailSeq(prevDetailSeq);
                        detailCount += el.getValue(); // count
                        emblemDtoList.add(emblemRateDto);
                    }

                    // 비율 = 일부값 / 전체값 * 100
                    for (EmblemRateDto e : emblemDtoList) {
                        e.setRate(String.format("%.2f", (Double.parseDouble(e.getRate()) / detailCount * 100)));
                    }

                    this.emblemRateRepository.saveAll(ObjectMapperUtils.mapList(emblemDtoList, EmblemRate.class));
                }

                // optionAbilityMap 처리
                for (Map.Entry<String, Map<String, Integer>> entry : optionAbilityMap.entrySet()) {
                    int detailCount = 0;
                    optionDtoList = Lists.newArrayList();
                    for (Map.Entry<String, Integer> el : entry.getValue().entrySet()) {
                        optionAbilityDto = new OptionAbilityDto();
                        optionAbilityDto.setChoiceOption(entry.getKey());
                        optionAbilityDto.setPartsName(el.getKey()); // partsName
                        optionAbilityDto.setRate(String.valueOf(el.getValue())); // 개별 카운트
                        optionAbilityDto.setJobDetailSeq(prevDetailSeq);
                        detailCount += el.getValue();
                        optionDtoList.add(optionAbilityDto);
                    }

                    // 비율 = 일부값 / 전체값 * 100
                    for (OptionAbilityDto e : optionDtoList) {
                        e.setRate(String.format("%.2f", (Double.parseDouble(e.getRate()) / detailCount * 100)));
                    }

                    this.optionAbilityRepository.saveAll(ObjectMapperUtils.mapList(optionDtoList, OptionAbility.class));
                }

                emblemsMap = Maps.newHashMap();
                optionAbilityMap = Maps.newHashMap();
                prevDetailSeq = r.getJobDetailSeq();
            }

            JSONObject rs = this.openApiService.getEquipAvatar(r.getServerId(), r.getCharacterId());
            JSONArray jsonArr = (JSONArray) rs.get("avatar");

            for (Object o : jsonArr) {
                JSONObject j = (JSONObject) o;
                String slotId = (String) j.get("slotId");

                // 오라나 무기압타는 제외
                if ("AURORA".equals(slotId) || "WEAPON".equals(slotId) || "AURA_SKIN".equals(slotId)) { continue; }
                String slotName = j.get("slotName").toString();
                String optionAbility = j.get("optionAbility").toString(); // 선택옵션

                // slotName 키값이 없으면 detailMap 생성
                optionAbilityMap.computeIfAbsent(slotName, key -> new HashMap<String, Integer>());
                // detailMap count 처리
                optionAbilityMap.computeIfPresent(slotName, (key, value) -> processEmblemDetail(optionAbility, value));

                JSONArray emblems = (JSONArray) j.get("emblems");

                for (Object o2 : emblems) {
                    JSONObject j2 = (JSONObject) o2;
                    String slotColor = j2.get("slotColor").toString();
                    String itemName = j2.get("itemName").toString();

                    // slotColor 키값이 없으면 detailMap 생성
                    emblemsMap.computeIfAbsent(slotColor, key -> new HashMap<String, Integer>());
                    // detailMap count 처리
                    emblemsMap.computeIfPresent(slotColor, (key, value) -> processEmblemDetail(itemName, value));
                }
            }
        }

        // emblemsMap 처리
        for (Map.Entry<String, Map<String, Integer>> entry : emblemsMap.entrySet()) {
            int detailCount = 0;
            emblemDtoList = Lists.newArrayList();
            for (Map.Entry<String, Integer> el : entry.getValue().entrySet()) {
                emblemRateDto = new EmblemRateDto();
                emblemRateDto.setEmblemName(el.getKey()); // detail Emblem Name
                emblemRateDto.setEmblemColor(entry.getKey());
                emblemRateDto.setRate(String.valueOf(el.getValue())); // 개별 카운트
                emblemRateDto.setJobDetailSeq(prevDetailSeq);
                detailCount += el.getValue(); // count
                emblemDtoList.add(emblemRateDto);
            }

            // 비율 = 일부값 / 전체값 * 100
            for (EmblemRateDto e : emblemDtoList) {
                e.setRate(String.format("%.2f", (Double.parseDouble(e.getRate()) / detailCount * 100)));
            }

            this.emblemRateRepository.saveAll(ObjectMapperUtils.mapList(emblemDtoList, EmblemRate.class));
        }

        // optionAbilityMap 처리
        for (Map.Entry<String, Map<String, Integer>> entry : optionAbilityMap.entrySet()) {
            int detailCount = 0;
            optionDtoList = Lists.newArrayList();
            for (Map.Entry<String, Integer> el : entry.getValue().entrySet()) {
                optionAbilityDto = new OptionAbilityDto();
                optionAbilityDto.setChoiceOption(entry.getKey());
                optionAbilityDto.setPartsName(el.getKey()); // partsName
                optionAbilityDto.setRate(String.valueOf(el.getValue())); // 개별 카운트
                optionAbilityDto.setJobDetailSeq(prevDetailSeq);
                detailCount += el.getValue();
                optionDtoList.add(optionAbilityDto);
            }

            // 비율 = 일부값 / 전체값 * 100
            for (OptionAbilityDto e : optionDtoList) {
                e.setRate(String.format("%.2f", (Double.parseDouble(e.getRate()) / detailCount * 100)));
            }

            this.optionAbilityRepository.saveAll(ObjectMapperUtils.mapList(optionDtoList, OptionAbility.class));
        }
    }

    /**
     *  Map 2 Depth Count 처리
     * @param detailKey
     * @param detailMap
     * @return
     */
    private Map<String, Integer> processEmblemDetail(String detailKey, Map<String, Integer> detailMap) {
        if (detailMap.containsKey(detailKey)) {
            detailMap.computeIfPresent(detailKey, (s,count) -> ++count);
        } else {
            detailMap.computeIfAbsent(detailKey, key -> 1);
        }

        return detailMap;
    }

    public void test() {
    	
    }
}
