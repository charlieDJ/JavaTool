package org.example.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiType;
import com.intellij.psi.javadoc.PsiDocComment;
import org.apache.commons.lang3.StringUtils;
import org.example.dialog.CustomDrawIoDialog;
import org.example.util.PsiClassUtils;
import org.example.util.PsiDocUtils;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * 生成DrawIo的类图代码
 */
public class GenerateDrawIoClassAction extends AnAction {

    private static Map<String,String> typeMap;

    static {
        typeMap = new HashMap<>();
        typeMap.put("java.lang.String", "varchar");
        typeMap.put("java.util.Date", "Date");
        typeMap.put("java.lang.boolean", "boolean");
        typeMap.put("java.lang.Integer", "int");
    }


    public static final String FIELD_FORMAT = "-%s%s: %s";

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        PsiFile psiFile = e.getData(CommonDataKeys.PSI_FILE);
        PsiClass psiClass = PsiClassUtils.getPsiClass(psiFile);
        if (Objects.isNull(psiClass)) {
            return;
        }
        List<String> lines = new ArrayList<>();
        String className = psiClass.getName();
        PsiField[] fields = psiClass.getFields();
        lines.add(className);
        lines.add(String.format(FIELD_FORMAT, "id", surroundByBracket("主键"), "varchar"));
        for (PsiField field : fields) {
            String fieldName = field.getName();
            PsiType type = field.getType();
            String canonicalText = type.getCanonicalText();
            canonicalText = typeMap.getOrDefault(canonicalText, canonicalText);
            PsiDocComment docComment = field.getDocComment();
            String comment = PsiDocUtils.getComment(docComment, false);
            String fieldText;
            if (StringUtils.isNotEmpty(comment)) {
                fieldText = String.format(FIELD_FORMAT, fieldName, surroundByBracket(comment), canonicalText);
            } else {
                fieldText = String.format(FIELD_FORMAT, fieldName, "", canonicalText);
            }
            lines.add(fieldText);
        }
        String classText = String.join("\n", lines);
        CustomDrawIoDialog ioDialog = new CustomDrawIoDialog(project, classText);
        ioDialog.show();
    }

    private String surroundByBracket(String text){
        return "(".concat(text).concat(")");
    }
}
