package com.entity.eclipse.utils.scripting.wrappers;

import com.entity.eclipse.Eclipse;
import com.entity.eclipse.utils.types.DynamicValue;
import com.entity.eclipse.utils.types.ListValue;
import org.mozilla.javascript.Context;

import java.util.ArrayList;

public class ConfigWrapper {
    @SuppressWarnings("unchecked")
    private Class<DynamicValue<?>> giveClass(String className) {
        try {
            return (Class<DynamicValue<?>>) Class.forName(
                    "com.entity.eclipse.utils.types." + className + "Value"
            );
        } catch(ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Object of(Object jsDataType, Object jsData) {
        String dataType = (String) Context.jsToJava(jsDataType, String.class);
        String data = Context.jsToJava(jsData, String.class).toString();

        Class<DynamicValue<?>> _class = this.giveClass(dataType);
        if(_class == null)
            return Context.javaToJS(null, Eclipse.engineScope);

        try {
            return Context.javaToJS(
                    _class
                            .getConstructor()
                            .newInstance()
                            .fromString(data),
                    Eclipse.engineScope
            );
        } catch(Exception e) {
            e.printStackTrace();
            return Context.javaToJS(null, Eclipse.engineScope);
        }
    }

    public Object ofList(Object jsDataType, Object ...jsData) {
        String dataType = (String) Context.jsToJava(jsDataType, String.class);

        ArrayList<String> data = new ArrayList<>();
        for(Object jsObj : jsData)
            data.add(Context.jsToJava(jsObj, String.class).toString());

        Class<DynamicValue<?>> _class = this.giveClass(dataType);
        if(_class == null)
            return Context.javaToJS(null, Eclipse.engineScope);

        return Context.javaToJS(
                new ListValue(_class, data.toArray(new String[0])),
                Eclipse.engineScope
        );
    }
}
