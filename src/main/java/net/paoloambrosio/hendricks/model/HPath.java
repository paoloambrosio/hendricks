package net.paoloambrosio.hendricks.model;

import io.swagger.models.HttpMethod;
import io.swagger.models.Path;
import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

public class HPath extends Path {

    public List<HOperation> getHOperations() {
        return getOperations().stream()
                .map(o -> HOperation.fromSwagger(o)).collect(toList());
    }

    public Map<HttpMethod, HOperation> getHOperationMap() {
        return getOperationMap().entrySet().stream()
                .collect(toMap(Map.Entry::getKey, e -> HOperation.fromSwagger(e.getValue())));
    }

    public static HPath fromSwagger(Path p) {
        HPath hp = new HPath();
        try {
            BeanUtils.copyProperties(hp, p);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        return hp;
    }
}
