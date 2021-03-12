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
public class RankingDto extends BaseDto {

    private int rankingSeq;
    private String serverName;
    private String characterName;
    private String characterId;
    private String jobValue;
    private String price;
}
