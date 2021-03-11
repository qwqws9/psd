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
public class MarketMasterDto extends BaseDto {

    private int marketMstSeq;
    private String title;
    private int jobDetailSeq;
    private String price;
    private String emblemCode;
    private String jobValue;
    private String degree;
    
    private List<MarketDetailDto> details;
}
