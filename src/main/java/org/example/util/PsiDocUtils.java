package org.example.util;

import com.intellij.psi.PsiElement;
import com.intellij.psi.javadoc.PsiDocComment;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class PsiDocUtils {

    public static String getComment(PsiDocComment comment, boolean isClass) {
        if (Objects.isNull(comment) || StringUtils.isEmpty(comment.getText())) {
            return "";
        }
        List<String> commentItems = new ArrayList<>();
        for (PsiElement child : comment.getChildren()) {
            String source = child.getText().replaceAll("[/* \n]+", StringUtils.EMPTY);
            if (isClass && child.getText().contains("@")) {
                continue;
            }
            commentItems.add(source);
        }
        return commentItems.stream()
                .filter(StringUtils::isNoneEmpty)
                .collect(Collectors.joining("。"));
    }

}
