package client;

import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Slf4jRestAssuredFilter implements Filter {

    @Override
    public Response filter(FilterableRequestSpecification req, FilterableResponseSpecification resSpec, FilterContext ctx) {
        log.info("HTTP {} {}", req.getMethod(), req.getURI());
        Response res = ctx.next(req, resSpec);
        log.info("-> {}", res.getStatusCode());
        return res;
    }
}
