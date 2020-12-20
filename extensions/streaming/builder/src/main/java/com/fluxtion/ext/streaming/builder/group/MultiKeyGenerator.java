/* 
 * Copyright (C) 2018 V12 Technology Ltd.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the Server Side Public License, version 1,
 * as published by MongoDB, Inc.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * Server Side Public License for more details.
 *
 * You should have received a copy of the Server Side Public License
 * along with this program.  If not, see 
 * <http://www.mongodb.com/licensing/server-side-public-license>.
 */
package com.fluxtion.ext.streaming.builder.group;

import com.fluxtion.builder.generation.GenerationContext;
import com.fluxtion.ext.streaming.api.group.MultiKey;
import com.fluxtion.ext.streaming.builder.util.FunctionGeneratorHelper;
import static com.fluxtion.ext.streaming.builder.util.FunctionKeys.functionClass;
import static com.fluxtion.ext.streaming.builder.util.FunctionKeys.imports;
import static com.fluxtion.ext.streaming.builder.util.FunctionKeys.sourceClass;
import static com.fluxtion.ext.streaming.builder.util.FunctionKeys.sourceMappingList;
import static com.fluxtion.ext.streaming.builder.util.FunctionKeys.targetMappingList;
import com.fluxtion.ext.streaming.builder.util.ImportMap;
import java.util.HashMap;
import java.util.List;
import org.apache.velocity.VelocityContext;

/**
 *
 * @author Greg Higgins
 */
public class MultiKeyGenerator {

    private static String TEMPLATE = "template/MultiKeyTemplate.vsl";

    public static <S> MultiKey<?> generate(List<MultiKeyInfo> keySet,
            Class<S> sourceClazz, HashMap<String, List<MultiKeyInfo>> multiKeySourceMap, ImportMap importMap,
            String genClassName) {
        try {
            VelocityContext ctx = new VelocityContext();
            ctx.put(functionClass.name(), genClassName);
            ctx.put(sourceClass.name(), importMap.addImport(sourceClazz));
            ctx.put(sourceMappingList.name(), multiKeySourceMap);
            ctx.put(targetMappingList.name(), keySet);
            ctx.put("targetInstanceId", "target");
            ctx.put(imports.name(), importMap.asString());
            Class<MultiKey<?>> aggClass = FunctionGeneratorHelper.generateAndCompile(null, TEMPLATE, GenerationContext.SINGLETON, ctx);
            MultiKey<?> multiKey = aggClass.newInstance();
            return multiKey;
        } catch (Exception e) {
            throw new RuntimeException("could not buuld function " + e.getMessage(), e);
        }
    }

}
