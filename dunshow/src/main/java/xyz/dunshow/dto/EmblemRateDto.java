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
public class EmblemRateDto extends BaseDto {

    private int emblemRateSeq;
    private int jobDetailSeq;
    private String emblemId;
    private String emblemName;
    private String rate;
    private String emblemColor;
    
    private String price;
    private String jobValue;
}
