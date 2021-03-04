package xyz.dunshow.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.Setter;
import xyz.dunshow.view.Views;

@Getter
@Setter
@JsonInclude(value = Include.NON_NULL)
@JsonView(Views.Simple.class)
public class ShowRoomDto extends BaseDto {

    private int showRoomSeq;
    private String avatarName;
    private String previewIndex;
    private String jobValue;
    private String partsName;
    private String itemId;
    private String rarity;
}
