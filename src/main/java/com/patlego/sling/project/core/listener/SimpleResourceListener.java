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

    private final String SYSTEM_LISTENER = "systemlistener";

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Reference
    private SlingRepository repository;

    @SuppressWarnings("AEM Rules:AEM-3") // used for observation
    private Session session;

    @Override
    public void onEvent(EventIterator events) {
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
        try {
            Session session = this.repository.loginService(SYSTEM_LISTENER, null);
            ObservationManager observation = session.getWorkspace().getObservationManager();

            observation.addEventListener(this, Event.NODE_REMOVED | Event.NODE_ADDED,
                    "/var/sitemaps", true, null,
                    new String[] { "nt:unstructured", "sling:Folder", "nt:file" }, true);
        } catch (RepositoryException e) {
            logger.error("Failed to activate the event listener", e);
        }

    }

    @Modified
    protected void modified() throws LoginException, RepositoryException {
        this.deactivate();
        this.activate();
    }

    @Deactivate
    protected void deactivate() throws LoginException, RepositoryException {
        try {
            ObservationManager observation = session.getWorkspace().getObservationManager();
            observation.removeEventListener(this);
            logger.info("Removed the event listener");
        } catch (RepositoryException e) {
            logger.error("Unable to successfully deactivate the event listener", e);
        } finally {
            if (session != null) {
                session.logout();
                session = null;
            }
        }
    }

}
