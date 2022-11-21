package com.magician.route.commons.util;

import com.magician.route.commons.annotation.Verification;
import com.magician.route.commons.constant.DataType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Verify front-end parameters
 */
public class ParamsCheckUtil {

    private static Logger logger = LoggerFactory.getLogger(ParamsCheckUtil.class);

    /**
     * Check parameters
     *
     * @param params parameter set
     * @param url    request path
     * @return Check result
     */
    public static String checkParam(String url, Object... params) {
        if (params == null) {
            return null;
        }

        for (Object obj : params) {
            if (obj == null) {
                continue;
            }
            String result = checkParam(obj.getClass(), obj, PathUtil.getPath(PathUtil.getUri(url)));
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    /**
     * Check parameters
     *
     * @param cls
     * @param obj
     * @return Check result
     */
    private static String checkParam(Class<?> cls, Object obj, String url) {
        try {
            Field[] fields = cls.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                /* Get validation annotations */
                Verification verification = field.getAnnotation(Verification.class);
                if (verification == null) {
                    continue;
                }

                /* Determine whether this annotation is valid for the current api, if not, jump directly to the next loop */
                String[] apis = verification.apis();
                if (!isThisApi(apis, url)) {
                    continue;
                }

                /* Check whether the parameters conform to the rules */
                Object val = field.get(obj);

                String fieldTypeName = field.getType().getSimpleName().toUpperCase();

                switch (fieldTypeName) {
                    case DataType.INT:
                    case DataType.INTEGER:
                        if (notNull(verification, val) == false) {
                            return verification.msg();
                        }

                        if (StringUtil.isNull(verification.min()) == false) {
                            if (val == null) {
                                return verification.msg();
                            }
                            if (Integer.parseInt(val.toString()) < Integer.parseInt(verification.min())) {
                                return verification.msg();
                            }
                        }
                        if (StringUtil.isNull(verification.max()) == false) {
                            if (val == null) {
                                return verification.msg();
                            }
                            if (Integer.parseInt(val.toString()) > Integer.parseInt(verification.max())) {
                                return verification.msg();
                            }
                        }
                        continue;
                    case DataType.LONG:
                        if (notNull(verification, val) == false) {
                            return verification.msg();
                        }

                        if (StringUtil.isNull(verification.min()) == false) {
                            if (val == null) {
                                return verification.msg();
                            }
                            if (Long.parseLong(val.toString()) < Long.parseLong(verification.min())) {
                                return verification.msg();
                            }
                        }
                        if (StringUtil.isNull(verification.max()) == false) {
                            if (val == null) {
                                return verification.msg();
                            }
                            if (Long.parseLong(val.toString()) > Long.parseLong(verification.max())) {
                                return verification.msg();
                            }
                        }
                        continue;
                    case DataType.DOUBLE:
                        if (notNull(verification, val) == false) {
                            return verification.msg();
                        }

                        if (StringUtil.isNull(verification.min()) == false) {
                            if (val == null) {
                                return verification.msg();
                            }
                            if (Double.parseDouble(val.toString()) < Double.parseDouble(verification.min())) {
                                return verification.msg();
                            }
                        }
                        if (StringUtil.isNull(verification.max()) == false) {
                            if (val == null) {
                                return verification.msg();
                            }
                            if (Double.parseDouble(val.toString()) > Double.parseDouble(verification.max())) {
                                return verification.msg();
                            }
                        }
                        continue;
                    case DataType.FLOAT:
                        if (notNull(verification, val) == false) {
                            return verification.msg();
                        }

                        if (StringUtil.isNull(verification.min()) == false) {
                            if (val == null) {
                                return verification.msg();
                            }
                            if (Float.parseFloat(val.toString()) < Float.parseFloat(verification.min())) {
                                return verification.msg();
                            }
                        }
                        if (StringUtil.isNull(verification.max()) == false) {
                            if (val == null) {
                                return verification.msg();
                            }
                            if (Float.parseFloat(val.toString()) > Float.parseFloat(verification.max())) {
                                return verification.msg();
                            }
                        }
                        continue;
                    case DataType.BIGDECIMAL:
                        if (notNull(verification, val) == false) {
                            return verification.msg();
                        }

                        if (StringUtil.isNull(verification.min()) == false) {
                            if (val == null) {
                                return verification.msg();
                            }
                            if (new BigDecimal(val.toString()).compareTo(new BigDecimal(verification.min())) < 0) {
                                return verification.msg();
                            }
                        }
                        if (StringUtil.isNull(verification.max()) == false) {
                            if (val == null) {
                                return verification.msg();
                            }
                            if (new BigDecimal(val.toString()).compareTo(new BigDecimal(verification.max())) > 0) {
                                return verification.msg();
                            }
                        }
                        continue;
                    case DataType.STRING:
                        if (!reg(val, verification.reg())) {
                            return verification.msg();
                        }
                        if (!notNull(verification, val)) {
                            return verification.msg();
                        }
                        continue;
                    case DataType.SHORT:
                    case DataType.BOOLEAN:
                    case DataType.DATE:
                    case DataType.BYTE:
                    case DataType.CHAR:
                    case DataType.CHARACTER:
                        if (!notNull(verification, val)) {
                            return verification.msg();
                        }
                        continue;
                    default:
                        if (field.getType().equals(String[].class) && !notNull(verification, val)) {
                            return verification.msg();
                        }
                        continue;
                }
            }
            return null;
        } catch (Exception e) {
            logger.error("Validation parameter exception", e);
            return null;
        }
    }

    /**
     * Validate parameter values against regular expressions
     *
     * @param val
     * @param reg
     * @return
     */
    private static boolean reg(Object val, String reg) {
        if (StringUtil.isNull(reg)) {
            return true;
        }
        if (StringUtil.isNull(val)) {
            return false;
        }
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(val.toString());
        return matcher.matches();
    }

    /**
     * non-null check
     *
     * @param verification
     * @param val
     * @return
     */
    private static boolean notNull(Verification verification, Object val) {
        if (verification.notNull() == false) {
            return true;
        }
        if (StringUtil.isNull(val)) {
            return false;
        }
        return true;
    }

    /**
     * Check if this api is included in the apis list
     *
     * @param url  此api
     * @param apis api列表
     * @return
     */
    private static boolean isThisApi(String[] apis, String url) {
        if (apis == null || apis.length < 1) {
            return true;
        }

        for (String api : apis) {
            if (MatchUtil.isMatch(PathUtil.getPath(api.toUpperCase()), url.toUpperCase())) {
                return true;
            }
        }
        return false;
    }
}
