package xyz.dunshow.util;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;

import xyz.dunshow.dto.Code;
import xyz.dunshow.dto.EnumCode;

public class EnumCodeUtil {
    private Map<String, List<Code>> codeMap = Maps.newHashMap();

    public List<Code> get(String cls) {
        return codeMap.get(cls);
    }

    public String get(String cls, String key) {
        for (Code code : get(cls)) {
            if (code.getKey().equals(key)) {
                return code.getValue();
            }
        }
        return "";
    }

    public Map<String, String> getMap(String cls) {
        return get(cls).stream().collect(Collectors.toMap(Code::getKey, code -> code.getValue()));
    }

    public Map<String, List<Code>> get(String[] keys) {
        return Arrays.stream(keys).filter(key -> !Strings.isNullOrEmpty(key))
                .collect(Collectors.toMap(Function.identity(), key -> codeMap.get(key)));
    }

    public void put(String key, Class<? extends EnumCode> e) {
        codeMap.put(key, toEnumValues(e));
    }

    private List<Code> toEnumValues(Class<? extends EnumCode> e) {
        return Arrays.stream(e.getEnumConstants()).map(Code::new).collect(Collectors.toList());
    }
}
