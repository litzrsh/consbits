package me.litzrsh.consbits.app.user.service;

import me.litzrsh.consbits.app.user.User;
import me.litzrsh.consbits.app.user.UserImage;
import me.litzrsh.consbits.app.user.props.UserImageConfigurationProperties;
import me.litzrsh.consbits.app.user.repository.UserImageRepository;
import me.litzrsh.consbits.app.user.repository.UserRepository;
import me.litzrsh.consbits.core.exception.RestfulException;
import me.litzrsh.consbits.core.util.RestUtils;
import me.litzrsh.consbits.core.util.SessionUtils;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

import static me.litzrsh.consbits.core.CommonConstants.UNAUTHORIZED_EXCEPTION;

@Service
@RequiredArgsConstructor
public class UserImageService {

    @Value("classpath:icon_profile.png")
    private Resource image;

    private final UserRepository userRepository;

    private final UserImageRepository userImageRepository;

    private final UserImageConfigurationProperties properties;

    @SuppressWarnings("DuplicatedCode")
    public void getImage(String id, HttpServletResponse response) {
        UserImage entity = userImageRepository.findById(id).orElse(null);
        if (entity == null) {
            handleException(response);
        } else {
            String path = RestUtils.append(properties.getBasePath(), entity.getPath());
            try (FileInputStream fis = new FileInputStream(path)) {
                response.setStatus(200);
                response.setContentType(entity.getType());
                IOUtils.copy(fis, response.getOutputStream());
            } catch (IOException e) {
                handleException(response);
            }
        }
    }

    public User updateProfileImage(MultipartHttpServletRequest request) throws RestfulException {
        User user = userRepository.findById(SessionUtils.getUsername())
                .orElseThrow(() -> new RestfulException(UNAUTHORIZED_EXCEPTION));
        File dir = new File(properties.getBasePath());
        if (!dir.isDirectory()) {
            //noinspection ResultOfMethodCallIgnored
            dir.mkdirs();
        }
        UserImage entity = userImageRepository.findById(user.getId()).orElse(new UserImage());
        Map<String, MultipartFile> fileMap = request.getFileMap();
        if (fileMap.isEmpty()) return user;
        try {
            String filePath = user.getId();
            MultipartFile file = fileMap.get(fileMap.keySet().iterator().next());
            File output = new File(dir, filePath);
            if (!output.exists()) {
                //noinspection ResultOfMethodCallIgnored
                output.createNewFile();
            }
            entity.setId(user.getId());
            entity.setName(file.getOriginalFilename());
            entity.setPath(filePath);
            entity.setSize(file.getSize());
            entity.setType(file.getContentType());
            entity.setUrl(RestUtils.append("/api/v1.0/profile/image", user.getId()));
            try (FileOutputStream fos = new FileOutputStream(output)) {
                IOUtils.copy(file.getInputStream(), fos);
            }
            userImageRepository.save(entity);
            user.setImageUrl(entity.getUrl());
        } catch (Exception ignored) {
        }
        return userRepository.save(user);
    }

    protected void handleException(HttpServletResponse response) {
        try {
            response.setStatus(200);
            response.setContentType("image/png");
            IOUtils.copy(image.getInputStream(), response.getOutputStream());
        } catch (IOException ignored) {
        }
    }
}
