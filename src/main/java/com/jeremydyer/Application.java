package com.jeremydyer;

import com.jeremydyer.cli.DummyCommand;
import com.jeremydyer.jdbi.DummyDAO;
import com.jeremydyer.managed.DummyManaged;
import com.jeremydyer.resource.DummyResource;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.skife.jdbi.v2.DBI;

/**
 * Created by jeremydyer on 11/19/14.
 */
public class Application
        extends io.dropwizard.Application<ApplicationConfiguration> {

    @Override
    public void initialize(Bootstrap<ApplicationConfiguration> bootstrap) {
        bootstrap.addCommand(new DummyCommand());

        //Creates an Asset bundle to serve up static content. Served from http://localhost:8080/assets/
        bootstrap.addBundle(new AssetsBundle());
    }

    @Override
    public void run(ApplicationConfiguration configuration, Environment environment) throws Exception {

        final DBIFactory factory = new DBIFactory();
        final DBI jdbi = factory.build(environment, configuration.getDataSourceFactory(), "mysql");
        final DummyDAO dao = jdbi.onDemand(DummyDAO.class);

        //Add managed instances.
        environment.lifecycle().manage(new DummyManaged());

        //Register your Web Resources like below.
        final DummyResource dummyResource = new DummyResource();
        environment.jersey().register(dummyResource);
    }

    public static void main(String[] args) throws Exception {
        new Application().run(args);
    }
}
