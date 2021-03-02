package xyz.dunshow.dto;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Code {
    private final String key;
    private final String value;

    public Code(EnumCode enumCode) {
        this.key = enumCode.getValue();
        this.value = enumCode.getDesc();
    }

    public Code(String key, String value) {
        this.key = key;
        this.value = value;
    }
}
