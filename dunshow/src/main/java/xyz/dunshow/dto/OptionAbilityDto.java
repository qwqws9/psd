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
public class OptionAbilityDto extends BaseDto {

    private int optionAbilitySeq;
    private int jobDetailSeq;
    private String partsName;
    private String choiceOption;
    private String rate;
}
