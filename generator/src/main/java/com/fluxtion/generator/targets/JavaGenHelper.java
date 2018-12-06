/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fluxtion.generator.targets;

import com.fluxtion.runtime.event.Event;
import com.fluxtion.api.generation.FilterDescription;
import com.fluxtion.generator.model.InvokerFilterTarget;
import com.fluxtion.runtime.lifecycle.FilteredHandlerInvoker;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 *
 * @author greg
 */
public interface JavaGenHelper {

    public static final StringBuilder builder = new StringBuilder(1 * 1000 * 1000);

    public static String generateMapDisaptch(ArrayList<InvokerFilterTarget> filteredInvokerList, List<Class<?>> importClassList) {
//        importClassList.add(Int2ObjectOpenHashMap.class);
        builder.delete(0, builder.length());
        if (filteredInvokerList == null || filteredInvokerList.isEmpty()) {
            return "";
        }
        importClassList.add(FilteredHandlerInvoker.class);

        builder.append("//int filter maps\n");
        filteredInvokerList.stream().filter(i -> i.filterDescription.isIntFilter).map((invoker) -> invoker.intMapName)
                .collect(Collectors.toSet()).stream().forEach(
//                        (methodName) -> builder.append("\tprivate final HashMap<Integer, FilteredHandlerInvoker> ")
                        (methodName) -> builder.append("\tprivate final Int2ObjectOpenHashMap<FilteredHandlerInvoker> ")
                        .append(methodName).append(" = init").append(methodName).append("();\n\n")
                );
        builder.append("//String filter maps\n");
        filteredInvokerList.stream().filter(i -> !i.filterDescription.isIntFilter).map((invoker) -> invoker.stringMapName)
                .collect(Collectors.toSet()).stream().forEach(
                        (methodName) -> builder.append("\tprivate final HashMap<String, FilteredHandlerInvoker> ")
                        .append(methodName).append(" = init").append(methodName).append("();\n\n")
                );

        HashSet<Class< ? extends Event>> setIntClasses = new HashSet<>();
        HashSet<Class< ? extends Event>> setStrClasses = new HashSet<>();

        for (InvokerFilterTarget invoker1 : filteredInvokerList) {
            Class< ? extends Event> e = invoker1.filterDescription.eventClass;
            if (invoker1.filterDescription.isIntFilter && !setIntClasses.contains(e)) {
//                builder.append( "\tprivate HashMap<Integer, FilteredHandlerInvoker> init" + invoker1.intMapName + "(){\n"
//                        + "\t\tHashMap<Integer, FilteredHandlerInvoker> dispatchMap = new HashMap<>();\n");
                builder.append( "\tprivate Int2ObjectOpenHashMap<FilteredHandlerInvoker> init" + invoker1.intMapName + "(){\n"
                        + "\t\tInt2ObjectOpenHashMap<FilteredHandlerInvoker> dispatchMap = new Int2ObjectOpenHashMap<>();\n");
                
                filteredInvokerList.stream().filter(i -> i.filterDescription.eventClass == e).filter(i -> i.filterDescription.isIntFilter).forEach(
                        (invoker)
                        -> builder.append("\t\tdispatchMap.put( " + invoker.filterDescription.value + ", new FilteredHandlerInvoker() {\n"
                        + "\n"
                        + "\t\t\t@Override\n"
                        + "\t\t\tpublic void invoke(Object event) {\n"
                        + "\t\t\t\t"
                        + generateFilteredDispatchMethodName(invoker.filterDescription)
                        + "( (" + invoker.filterDescription.eventClass.getCanonicalName() + ")event);\n"
                        + "\t\t\t}\n"
                        + "\t\t});"
                        + "\t\t\n"));

                builder.append( "\t\treturn dispatchMap;\n"
                        + "\t}\n\n");

                setIntClasses.add(e);

            }

            if (!invoker1.filterDescription.isIntFilter && !setStrClasses.contains(e)) {
                builder.append( "\tprivate HashMap<String, FilteredHandlerInvoker> init" + invoker1.stringMapName + "(){\n"
                        + "\t\tHashMap<String, FilteredHandlerInvoker> dispatchMap = new HashMap<>();\n");

                filteredInvokerList.stream().filter(i -> i.filterDescription.eventClass == e).filter(i -> !i.filterDescription.isIntFilter).forEach(
                        (invoker)
                        -> builder.append("\t\tdispatchMap.put(\"" + invoker.filterDescription.stringValue + "\", new FilteredHandlerInvoker() {\n"
                        + "\n"
                        + "\t\t\t@Override\n"
                        + "\t\t\tpublic void invoke(Object event) {\n"
                        + "\t\t\t\t"
                        + generateFilteredDispatchMethodName(invoker.filterDescription)
                        + "( (" + invoker.filterDescription.eventClass.getCanonicalName() + ")event);\n"
                        + "\t\t\t}\n"
                        + "\t\t});"
                        + "\t\t\n"));
                builder.append( "\t\treturn dispatchMap;\n"
                        + "\t}\n\n");

                setStrClasses.add(e);
            }
            
            if(setStrClasses.size()>0){
                importClassList.add(HashMap.class);
            }
        }
        //loop and generate the methods - check no name clashes on the method names
        filteredInvokerList.stream().forEach(
                (invoker)
                -> builder.append("\tprivate void " + invoker.methodName
                + "(" + invoker.filterDescription.eventClass.getCanonicalName() + " typedEvent){\n"
                + "\t\t//method body - invoke call tree\n"
                + "\t\t" + invoker.methodBody
                + "\t}\n\n"));
        
        return builder.toString();

    }


    public static String generateFilteredDispatchMethodName(FilterDescription filter) {
        String filterName = filter.variableName;
        if (filterName == null) {
            filterName = (filter.isIntFilter ? filter.value : filter.stringValue) + "";
        }
        filterName = filter.isFiltered ? filterName : "NoFilter";
        String filterClass = "noFilterClass";
        if (filter.eventClass != null) {
            filterClass = filter.eventClass.getSimpleName();
        }
        return getIdentifier("handle_" + filterClass + "_" + filterName);
    }

    public static String generateFilteredDispatchMap(Class clazz, boolean isInt) {
        FilterDescription filter = new FilterDescription(clazz, 0);
        if (!isInt) {
            filter = new FilterDescription(clazz, "");
        }
        return generateFilteredDispatchMap(filter);
    }

    public static String generateFilteredDispatchMap(FilterDescription filter) {
        String type = (filter.isIntFilter ? "Int" : "String");
        String filterClass = "noFilterClass";
        if (filter.eventClass != null) {
            filterClass = filter.eventClass.getSimpleName();
        }
        return getIdentifier("dispatch" + type + "Map" + filterClass);
    }

    public static String getIdentifier(String str) {
        StringBuilder sb = new StringBuilder();
        if(!Character.isJavaIdentifierStart(str.charAt(0))) {
            sb.append("_");
        }
        for (char c : str.toCharArray()) {
            if(!Character.isJavaIdentifierPart(c)) {
                sb.append("_");
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }
    
    public static Class mapWrapperToPrimitive(Class clazz){
        Class retClass = void.class;
        switch(clazz.getSimpleName()){
            case "Integer":
                retClass = int.class;
                break;
            case "Double":
                retClass = double.class;
                break;
            case "Float":
                retClass = float.class;
                break;
            case "Short":
                retClass = short.class;
                break;
            case "Byte":
                retClass = byte.class;
                break;
            case "Long":
                retClass = long.class;
                break;
            case "Character":
                retClass = char.class;
                break;
        }
        return retClass;
    }
}