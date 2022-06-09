package com.revature.reimbursement.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.reimbursement.daos.UserRolesDAO;
import com.revature.reimbursement.daos.UsersDAO;
import com.revature.reimbursement.services.TokenService;
import com.revature.reimbursement.services.UserRolesService;
import com.revature.reimbursement.services.UsersService;
import com.revature.reimbursement.servlets.LoginServlet;
import com.revature.reimbursement.servlets.UsersServlet;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;

/* Need this ContextLoaderListener for our dependency injection upon deployment. */
public class ContextLoaderListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("\nInitializing reimbursement web application");

        /* ObjectMapper provides functionality for reading and writing JSON, either to and from basic POJOs (Plain Old Java Objects) */
        ObjectMapper mapper = new ObjectMapper();

        /* Dependency injection. */
        UsersServlet userServlet = new UsersServlet(mapper, new UsersService(new UsersDAO()), new UserRolesService(new UserRolesDAO()), new TokenService(new JwtConfig()));
        LoginServlet loginServlet = new LoginServlet(mapper, new UsersService(new UsersDAO()), new TokenService(new JwtConfig()));

        /* Need ServletContext class to map whatever servlet to url path. */
        ServletContext context = sce.getServletContext();
        context.addServlet("UsersServlet", userServlet).addMapping("/users/*");
        context.addServlet("LoginServlet", loginServlet).addMapping("/login/*");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("\nShutting down reimbursement web application");
    }
}
