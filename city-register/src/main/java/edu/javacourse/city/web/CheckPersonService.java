package edu.javacourse.city.web;

import edu.javacourse.city.dao.PersonCheckDao;
import edu.javacourse.city.dao.PoolConnectionBuilder;
import edu.javacourse.city.domain.PersonRequset;
import edu.javacourse.city.domain.PersonResponse;
import edu.javacourse.city.exception.PersonCheckException;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Singleton;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Path("/check")
@Singleton
public class CheckPersonService {

    private static final Logger logger = LoggerFactory.getLogger(CheckPersonService.class);

    private PersonCheckDao dao;


    @PostConstruct
    public void init() {
        logger.info("SERVICE is created");
        dao = new PersonCheckDao();
        dao.setConnectionBuilder(new PoolConnectionBuilder());
    }



    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public PersonResponse checkPerson(PersonRequset requset) throws PersonCheckException {
        logger.info(requset.toString());
        return dao.checkPerson(requset);
    }
}
