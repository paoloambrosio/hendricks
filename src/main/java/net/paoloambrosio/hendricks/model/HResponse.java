package net.paoloambrosio.hendricks.model;

import com.fasterxml.jackson.databind.node.ObjectNode;
import io.swagger.models.Response;
import io.swagger.parser.util.SwaggerDeserializer;
import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Spliterators;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toMap;

public class HResponse extends Response {

    public Map<String, String> getHBody() {
        ObjectNode bodyNode = (ObjectNode) getVendorExtensions().get("x-body");
        if (bodyNode == null) {
            return null;
        } else {
            return StreamSupport.stream(Spliterators.spliteratorUnknownSize(bodyNode.fields(), 0), false)
                    .filter(e -> e.getValue().isTextual())
                    .collect(toMap(Map.Entry::getKey, e -> e.getValue().asText()));
        }
    }

    public Integer getHHttpStatus() {
        Object httpStatusNode = getVendorExtensions().get("x-httpstatus");
        if (httpStatusNode != null && Integer.class.isInstance(httpStatusNode)) {
            return (Integer) httpStatusNode;
        } else {
            return null;
        }
    }

    public List<HResponse> getHScenarios() {
        Object scenarios = getVendorExtensions().get("x-scenarios");

        SwaggerDeserializer deserializer = new SwaggerDeserializer();
        List<HResponse> out = new ArrayList<>();
        if (scenarios != null && List.class.isInstance(scenarios)) {
            List<?> responseList = (List<?>) scenarios;
            responseList.forEach(rNode -> {
                if (rNode != null && ObjectNode.class.isInstance(rNode)) {
                    Response r = deserializer.response((ObjectNode) rNode, "", null);
                    out.add(HResponse.fromSwagger(r));
                }
            });
        }
        return out;
    }

    /**
     * Apparently they forgot this in the Response object. Also here all
     * fields are private so have to write this horrible code.
     *
     * @param vendorExtensions
     */
    public void setVendorExtensions(Map<String, Object> vendorExtensions) {
        getVendorExtensions().clear();
        getVendorExtensions().putAll(vendorExtensions);
    }

    public static HResponse fromSwagger(Response r) {
        HResponse hr = new HResponse();
        try {
            BeanUtils.copyProperties(hr, r);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        return hr;
    }
}
