package me.litzrsh.consbits.core;

import me.litzrsh.consbits.core.exception.RestfulAuthenticationException;
import me.litzrsh.consbits.core.exception.RestfulException;
import org.springframework.http.HttpStatus;

@SuppressWarnings({"unused"})
public abstract class CommonConstants {

    public static final Long SERIAL_VERSION = 0x0100L;

    public static final String SYSTEM = "SYSTEM";
    public static final String SYS_ADMIN = "SYS_ADMIN";
    public static final String ADMIN = "ADMIN";
    public static final String USER = "USER";
    public static final String GUEST = "GUEST";
    public static final String WEB = "WEB";
    public static final String MOBILE = "MOBILE";
    public static final String OAUTH = "OAUTH";
    public static final String APPLICATION = "APP";

    public static final String SE0001 = "SE0001";

    public static final RestfulException FORBIDDEN_EXCEPTION = new RestfulException(HttpStatus.FORBIDDEN, "SE0002");
    public static final RestfulException UNAUTHORIZED_EXCEPTION = new RestfulException(HttpStatus.FORBIDDEN, "SE0003");

    public static final RestfulAuthenticationException AUTHENTICATION_FAIL_EXCEPTION = new RestfulAuthenticationException("SE0004");

    public static final String USER_TYPE_APPLICATION = "APP";
    public static final String USER_TYPE_BACKOFFICE = "BO";

    public static final String USER_STATUS_NORMAL = "10";
    public static final String USER_STATUS_LOCK = "40";
    public static final String USER_STATUS_WITHDRAW = "60";

    public static final String CONTENT_TYPE_JSON = "application/json;charset=utf8";

    public static final String PATH_DELIM = "/";
    public static final String CODE_DELIM = "_";

    public static final String MENU_TYPE_DIR = "D";
    public static final String MENU_TYPE_PAGE = "P";

    public static final String SCRATCH_CATEGORY_RANDOM = "random";
    public static final String SCRATCH_CATEGORY_TODAY = "today";
    public static final String SCRATCH_CATEGORY_NONE = "00";

    public static final String SCRATCH_STATUS_START = "10";
    public static final String SCRATCH_STATUS_SUCCESS = "20";
    public static final String SCRATCH_STATUS_FAIL = "30";
    public static final String SCRATCH_STATUS_SKIP = "40";

    public static final String SCRATCH_CATEGORY_IMAGE_TYPE_MAIN = "10";
    public static final String SCRATCH_CATEGORY_IMAGE_TYPE_DETAIL = "20";

    public static final String SCRATCH_CATEGORY_PATH = "/SCRATCH/CTGRY";
}
