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
import org.example.dialog.CustomDialog;
import org.example.util.PsiClassUtils;
import org.example.util.PsiDocUtils;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 生成数据库表数据
 */
public class GenerateTableFieldAction extends AnAction {

    private static Map<String,String> typeMap;

    static {
        typeMap = new HashMap<>();
        typeMap.put("java.lang.String", "VARCHAR2");
        typeMap.put("java.util.Date", "Date");
        typeMap.put("java.lang.boolean", "boolean");
        typeMap.put("java.lang.Integer", "int");
    }


    public static final String FIELD_FORMAT = "%d\t%s\t%s\t%s\t%s\t%s";

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        PsiFile psiFile = e.getData(CommonDataKeys.PSI_FILE);
        PsiClass psiClass = PsiClassUtils.getPsiClass(psiFile);
        if (Objects.isNull(psiClass)) {
            return;
        }
        List<String> lines = new ArrayList<>();
        PsiField[] fields = psiClass.getFields();
        AtomicInteger integer = new AtomicInteger(1);
        lines.add(String.format(FIELD_FORMAT, integer.getAndIncrement(),"主键", "id", "VARCHAR2", "255", "唯一编号"));
        for (PsiField field : fields) {
            String fieldName = field.getName();
            PsiType type = field.getType();
            String canonicalText = type.getCanonicalText();
            canonicalText = typeMap.getOrDefault(canonicalText, canonicalText);
            PsiDocComment docComment = field.getDocComment();
            String comment = PsiDocUtils.getComment(docComment, false);
            String fieldText = String.format(FIELD_FORMAT, integer.getAndIncrement(),comment, fieldName, canonicalText, "255", "");
            lines.add(fieldText);
        }
        String classText = String.join("\n", lines);
        CustomDialog dialog = new CustomDialog(project, classText);
        dialog.show();
    }
}
