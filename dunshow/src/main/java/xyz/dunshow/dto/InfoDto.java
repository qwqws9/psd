package xyz.dunshow.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonView;

import lombok.Getter;
import lombok.Setter;
import xyz.dunshow.view.Views;

@Getter
@Setter
@JsonInclude(value = Include.NON_NULL)
@JsonView(Views.Simple.class)
public class InfoDto extends BaseDto {

	// 다건 조회
    private String characterName;
    private String jobGrowName;
    private String serverName;
    private String serverId;
    private String characterId;
    private String imgSrc;
    
    // 단건 조회
    private String slotName;
    private String itemId;
    private String itemName;
    private String unitPrice;
    private String averagePrice;
    private String totalPrice;
    private String totalAverage;
    
    private List<EmblemRateDto> emblemRates;
    private List<OptionAbilityDto> optionAbilitys;
}
