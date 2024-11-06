package org.dows;

import com.mybatisflex.annotation.Table;
import org.dows.core.code.CodeGenerator;
import org.dows.core.code.CodeModel;
import org.dows.core.code.CodeTypeEnum;
import org.dows.modules.user.entity.UserWxEntity;

import java.util.List;

public class RadeCodeGeneratorTest {
    public static void main(String[] args) {
        CodeGenerator codeGenerator = new CodeGenerator();
        codeGenerator.init();
        List<Class> list = List.of(UserWxEntity.class);

        list.forEach(o -> {
            Table annotation = (Table) o.getAnnotation(Table.class);
            CodeModel codeModel = new CodeModel();
            codeModel.setType(CodeTypeEnum.APP);
            codeModel.setName(annotation.comment());
            codeModel.setModule(getFirstWord(o.getSimpleName()));
            codeModel.setEntity(o);
            // 生成 controller
//            codeGenerator.controller(codeModel);
            // 生成 mapper
            codeGenerator.mapper(codeModel);
            // 生成 service
            codeGenerator.service(codeModel);
        });
    }

    public static String getFirstWord(String className) {
        if (className == null || className.isEmpty()) {
            return "";
        }

        StringBuilder firstWord = new StringBuilder();
        boolean foundFirstWord = false;

        for (char c : className.toCharArray()) {
            if (Character.isUpperCase(c)) {
                if (foundFirstWord) {
                    break;
                }
                firstWord.append(c);
                foundFirstWord = true;
            } else {
                if (foundFirstWord) {
                    firstWord.append(c);
                }
            }
        }

        return firstWord.toString().toLowerCase();
    }
}
