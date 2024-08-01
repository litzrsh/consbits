package me.litzrsh.consbits.app.authority;

import lombok.Getter;

/**
 * 메뉴 권한을 나타내는 열거형 클래스.
 * 읽기 권한과 쓰기 권한을 정의합니다.
 */
@Getter
public enum MenuAuthority {

    READ(0x01), // 읽기 권한
    WRITE(0x02); // 쓰기 권한

    private final int value; // 권한 값

    /**
     * MenuAuthority 생성자.
     *
     * @param value 권한 값.
     */
    MenuAuthority(int value) {
        this.value = value;
    }
}
