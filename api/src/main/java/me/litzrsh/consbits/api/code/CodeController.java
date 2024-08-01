package me.litzrsh.consbits.api.code;

import me.litzrsh.consbits.app.code.Code;
import me.litzrsh.consbits.app.code.service.CodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 코드 관리를 위한 REST API 컨트롤러 클래스.
 * 특정 경로(path)에 해당하는 코드 리스트를 제공합니다.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1.0/code")
public class CodeController {

    // CodeService 주입 (의존성 주입)
    private final CodeService codeService;

    /**
     * 특정 경로에 해당하는 모든 코드를 조회합니다.
     *
     * @param path 조회할 코드의 경로.
     * @return 경로에 해당하는 코드 리스트.
     */
    @GetMapping("")
    public List<Code> findAll(@RequestParam String path, @RequestParam(required = false, defaultValue = "false") Boolean all) {
        // 서비스 레이어를 호출하여 경로에 해당하는 모든 코드를 가져옵니다.
        return codeService.findByPath(path, all);
    }
}
