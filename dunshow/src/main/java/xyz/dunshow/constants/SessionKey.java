package xyz.dunshow.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum SessionKey {

    LOGIN("user");

    private final String value;
}
