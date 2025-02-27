package org.nbody.controllers;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.nbody.models.Body;
import org.nbody.services.NBodyService;

import java.util.List;


@Path("/bodies")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class NBodyController {

    private final NBodyService nbodyService;

    public NBodyController(NBodyService nbodyService){
        this.nbodyService = nbodyService;
    }

    @POST
    public Response addBody(Body body){
        nbodyService.addBody(body);
        return Response.ok().build();
    }

    @DELETE
    @Path("/{index}")
    public Response removeBody(@PathParam("index") int index){
        nbodyService.deleteBody(index);
        return Response.ok().build();
    }

    @GET
    public List<Body> getBodies(){
        return nbodyService.getAllBodies();
    }
}
