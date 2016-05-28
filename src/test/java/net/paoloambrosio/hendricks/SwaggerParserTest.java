package net.paoloambrosio.hendricks;

import com.google.common.collect.ImmutableMap;
import io.swagger.models.HttpMethod;
import io.swagger.parser.SwaggerParser;
import net.paoloambrosio.hendricks.model.HResponse;
import net.paoloambrosio.hendricks.model.HSwagger;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public class SwaggerParserTest {

    @Test
    public void basicParserWorks() throws Exception {
        String swaggerSpec = IOUtils.toString(getClass().getResourceAsStream("/api1.swagger"));
        HSwagger hendrics =  HSwagger.fromSwagger(new SwaggerParser().parse(swaggerSpec));
        Map<String, HResponse> hresponses = hendrics.getHPath("/status").getHOperationMap().get(HttpMethod.GET).getHResponses();

        HResponse response200 = hresponses.get("200");
        assertThat(response200.getDescription(), is("System is healthy"));
        assertThat(response200.getHHttpStatus(), is(nullValue()));
        assertThat(response200.getHBody(), is(ImmutableMap.of("$.status", "OK")));

        HResponse scenario1 = hresponses.get("default").getHScenarios().get(0);
        assertThat(scenario1.getDescription(), is("Something invalid"));
        assertThat(scenario1.getHHttpStatus(), is(Integer.valueOf(400)));
        assertThat(scenario1.getHBody(), is(ImmutableMap.of("$.code", "123")));

        HResponse scenario2 = hresponses.get("default").getHScenarios().get(1);
        assertThat(scenario2.getDescription(), is("Unexpected errors"));
        assertThat(scenario2.getHHttpStatus(), is(Integer.valueOf(500)));
        assertThat(scenario2.getHBody(), is(ImmutableMap.of("$.code", "000")));
    }
}
