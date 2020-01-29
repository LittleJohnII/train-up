package org.train;

import org.jboss.logging.Logger;

import javax.inject.Inject;
import javax.jms.JMSException;
import javax.servlet.annotation.HttpConstraint;
import javax.servlet.annotation.ServletSecurity;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import java.util.Map;

@ApplicationPath("/")
public class TrainApplication extends Application {

    @Path("/")
//    @ServletSecurity(@HttpConstraint(rolesAllowed = { "JBossPain" }))
    public static class SecuredTrainResource {
        private static final Logger log = Logger.getLogger(SecuredTrainResource.class.getName());

        @Inject
        TrainService trainService;

        @POST
        @Consumes(MediaType.APPLICATION_JSON)
        @Path("/log")
        public void doPOST(TrainData data) throws JMSException {
            log.info("processing HTTP POST request");
            trainService.log(data);
        }
    }

    @Path("/")
    public static class TrainResource {
        private static final Logger log = Logger.getLogger(TrainResource.class.getName());

        @Inject
        TrainService trainService;

        @GET
        @Path("/info")
        @Produces(MediaType.APPLICATION_JSON)
        public Map<String, String> doGET() {
            log.info("processing HTTP GET request");
            return trainService.getInfo();
        }
    }


}