package io.baris.petclinic.vertxkafka.system;

import io.vertx.ext.web.RoutingContext;

/**
 * Web utilities for Vertx
 */
public class WebUtils {

    public static int pathParamAsInt(
        final RoutingContext context,
        final String name
    ) {
        return Integer.parseInt(context.pathParam(name));
    }

    public static void accepted(
        final RoutingContext context,
        final String message
    ) {
        context.response().setStatusCode(202);
        context.json(new Message(message));
    }
}
