package config;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 * This is a default method used to activate Resteasy JAX-WS.
 *
 * Don't touch it unless you know what you're doing!
 */
@ApplicationPath("/root")
public class JAXActivator extends Application {}