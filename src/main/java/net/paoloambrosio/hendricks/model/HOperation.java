package net.paoloambrosio.hendricks.model;

import io.swagger.models.Operation;
import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

public class HOperation extends Operation {

    public Map<String, HResponse> getHResponses() {
        return getResponses().entrySet().stream()
                .collect(toMap(Map.Entry::getKey, e -> HResponse.fromSwagger(e.getValue())));
    }

    public static HOperation fromSwagger(Operation o) {
        HOperation ho = new HOperation();
        try {
            BeanUtils.copyProperties(ho, o);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        return ho;
    }
}
