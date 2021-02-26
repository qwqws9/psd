package xyz.dunshow.dto;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonView;
import com.google.common.collect.Maps;

import lombok.Getter;
import lombok.Setter;
import xyz.dunshow.view.Views;

@Getter
@Setter
@JsonView(Views.Simple.class)
public class AjaxResponse {
    private String code = "";
    private String message = "";
    private Map<String, String> errors;
    @JsonInclude(value = Include.NON_NULL)
    private Object data;

    public AjaxResponse() {
    }
    public AjaxResponse(String code, String message) {
        this.code = code;
        this.message = message;
    }
    public AjaxResponse(Object data) {
        this.data = data;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public void putError(String key, String value) {
        if (this.errors == null) {
            this.errors = Maps.newHashMap();
        }
        this.errors.put(key, value);
    }
    @JsonIgnore
    public Map<String, String> getMap() {
        Map<String, String> map = Maps.newHashMap();
        map.put("code", this.code);
        map.put("message", this.message);
        return map;
    }
}
