package me.litzrsh.consbits.api;

import me.litzrsh.consbits.core.crypto.util.RsaUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.PublicKey;
import java.util.Base64;
import java.util.Date;

/**
 * 시스템 정보 및 공개 키를 제공하는 REST API 컨트롤러 클래스.
 * 시스템 상태 확인, 공개 키 제공, 현재 시간을 반환하는 기능을 제공합니다.
 */
@RestController
public class InfoController {

    /**
     * 시스템 상태를 확인합니다.
     *
     * @return 시스템 상태 확인 문자열 ("OK").
     */
    @GetMapping("/api/v1.0/.info")
    public String getInfo() {
        return "OK";
    }

    /**
     * RSA 공개 키를 Base64 인코딩된 문자열로 제공합니다.
     *
     * @return Base64 인코딩된 공개 키 문자열.
     */
    @GetMapping("/api/v1.0/.public")
    public String getPublicKey() {
        PublicKey publicKey = RsaUtils.getPublicKey();
        return Base64.getEncoder().encodeToString(publicKey.getEncoded());
    }

    /**
     * 현재 시스템 시간을 반환합니다.
     *
     * @return 현재 시스템 시간.
     */
    @GetMapping("/api/v1.0/time")
    public Date getTime() {
        return new Date();
    }
}
