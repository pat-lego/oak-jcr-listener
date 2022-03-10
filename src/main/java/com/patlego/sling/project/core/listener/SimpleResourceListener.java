package com.patlego.sling.project.core.listener;

import javax.jcr.LoginException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.observation.EventIterator;
import javax.jcr.observation.EventListener;
import javax.jcr.observation.ObservationManager;

import org.apache.sling.jcr.api.SlingRepository;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.observation.Event;

@Component(immediate = true)
public class SimpleResourceListener implements EventListener {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    private Session session;

    @Reference
    private SlingRepository repository;

    @Override
    public void onEvent(EventIterator events) {
        logger.info("Something triggered the onEvent listener");
        if (null != events) {
            while (events.hasNext()) {
                Event event = events.nextEvent();
                try {
                    logger.info("JCR event logged at path - {} with type {}", event.getPath(), event.getType());
                } catch (RepositoryException e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }

    }

    @Activate
    protected void activate() throws LoginException, RepositoryException {
        logger.info("!!!!!!Activating {} bundle", this.getClass().getName());

        this.session = this.repository.loginService("systemlistener", null);
        ObservationManager observation = session.getWorkspace().getObservationManager();

        logger.info("About to add the event listener");
        observation.addEventListener(this, Event.NODE_ADDED | Event.PERSIST | Event.PROPERTY_ADDED | Event.PROPERTY_CHANGED , "/", true, null, new String[]{"nt:file","nt:unstructured", "nt:base", "rep:User", "sling:Folder"} , true);
        logger.info("Added the event listener");
    }

    @Modified
    protected void modified() throws LoginException, RepositoryException {
        this.deactivate();
        this.activate();
    }

    @Deactivate
    protected void deactivate() throws LoginException, RepositoryException {
        logger.info("Deactivating {} bundle", this.getClass().getName());

        ObservationManager observation = this.session.getWorkspace().getObservationManager();

        logger.info("About to remove the event listener");
        observation.removeEventListener(this);
        logger.info("Removed the event listener");

        session.logout();
    }

}
