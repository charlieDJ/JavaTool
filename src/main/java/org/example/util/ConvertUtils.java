package org.example.util;

import org.apache.commons.lang3.StringUtils;
import org.example.entity.Meta;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ConvertUtils {

    private static String COMMENT = "/** %s */";
    private static String SWAGGER_FIELD_ANNO = "@ApiModelProperty(value = \"%s\", required = false, example = \"\")";
    private static String FIELD = "private %s %s;";
    private static String LINE = "\n";

    public static String fieldToCode(String fieldText) {
        List<String> lines = Arrays.asList(fieldText.split("\n"));
        List<Meta> metas = lines.stream()
                .map(e -> {
                    String[] split = e.split("\t");
                    int length = split.length;
                    Meta meta = new Meta();
                    meta.setCode(split[0]);
                    meta.setName(split[1]);
                    if (length == 3) {
                        meta.setType(split[2]);
                    }
                    return meta;
                }).collect(Collectors.toList());
        StringBuilder sb = new StringBuilder();
        for (Meta meta : metas) {
            sb.append(String.format(COMMENT, meta.getName())).append(LINE);
            sb.append(String.format(SWAGGER_FIELD_ANNO, meta.getName())).append(LINE);
            String type = StringUtils.isEmpty(meta.getType())? "String": meta.getType();
            sb.append(String.format(FIELD, type, meta.getCode())).append(LINE);
        }
        return sb.toString();
    }

}
