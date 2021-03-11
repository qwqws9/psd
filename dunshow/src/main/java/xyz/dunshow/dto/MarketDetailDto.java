package xyz.dunshow.dto;

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
public class MarketDetailDto extends BaseDto {

    private int marketDetailSeq;
    private int marketMstSeq;
    private String itemName;
    private String slotId;
    private String choiceOption;
    private String emblemName1;
    private String emblemColor1;
    private String emblemName2;
    private String emblemColor2;
    private String emblemName3;
    private String emblemColor3;
    
    private String emblemRate1;
    private String emblemRate2;
    private String emblemRate3;
    private String choiceOptionRate;
    
}
