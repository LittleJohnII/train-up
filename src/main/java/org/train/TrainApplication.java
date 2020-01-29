package org.train;

import org.jboss.logging.Logger;

import javax.inject.Inject;
import javax.jms.JMSException;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.GET;
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
    public static class TrainResource {
        private static final Logger log = Logger.getLogger(TrainResource.class.getName());

        @Inject
        TrainService trainService;

        @PUT
        @Path("/log")
        public void doPUT(
                @QueryParam("trainID") String trainID,
                @QueryParam("timestamp") int timestamp,
                @QueryParam("data") int data) throws JMSException {

            log.info("processing HTTP PUT request");
            trainService.log(trainID, timestamp, data);
        }

        @GET
        @Path("/info")
        @Produces(MediaType.APPLICATION_JSON)
        public Map<String, Integer> doGET() {
            log.info("processing HTTP GET request");
            return trainService.getInfo();
        }
    }
}