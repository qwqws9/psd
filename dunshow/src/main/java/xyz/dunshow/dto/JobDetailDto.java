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
public class JobDetailDto extends BaseDto {

    private int jobDetailSeq;
    private String firstJob;
    private String secondJob;
    private String thirdJob;
    private String fourthJob;
    private String jobValue;
    private JobDto job;
    private List<EmblemDto> emblems;
    
}
