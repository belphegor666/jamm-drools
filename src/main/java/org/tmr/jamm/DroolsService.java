package org.tmr.jamm;

import org.kie.api.cdi.KSession;
import org.kie.api.runtime.KieSession;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/drools")
public class DroolsService {

    @Inject
    @KSession
    KieSession kSession;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Response ping() {
        return echo("ping");
    }

    @GET
    @Path("{message}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response echo(@PathParam("message") String message) {
        String result = "rules not fired!";

        if (kSession == null) {
            result = "kSession is null";
        } else {
            // Create a new 'fact' for input to the rule engine
            Data d = new Data("INPUT", message);
            // Set a debug print output stream
            kSession.setGlobal("out", System.out);
            // Add the fact to the working memory
            kSession.insert(d);
            // Fire the rules
            kSession.fireAllRules();
            // Read the result - the rules should have modified the fact
            result = d.getText();
        }
        return Response.ok(result).build();
    }

}
