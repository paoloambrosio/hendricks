package net.paoloambrosio.hendricks.model;

import io.swagger.models.Swagger;
import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.InvocationTargetException;

public class HSwagger extends Swagger {

    public HPath getHPath(String path) {
        return HPath.fromSwagger(getPath(path));
    }

    /**
     * Swagger's Java parser is just not designed to be extended.
     * Need to use this ugly hack to create the Hendricks domain model.
     *
     * @param s The Swagger domain model
     * @return The Handricks domain model
     */
    public static HSwagger fromSwagger(Swagger s) {
        HSwagger hs = new HSwagger();
        try {
            BeanUtils.copyProperties(hs, s);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        return hs;
    }
}
