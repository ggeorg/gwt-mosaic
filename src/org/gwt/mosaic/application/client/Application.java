/*
 * Copyright (C) 2009 Georgios J. Georgopoulos, All rights reserved.
 * 
 * This is free software; you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option) any
 * later version.
 * 
 * This software is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this software; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA, or see the FSF
 * site: http://www.fsf.org.
 */
package org.gwt.mosaic.application.client;

import org.gwt.mosaic.application.client.util.ApplicationFramework;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Window.ClosingEvent;
import com.google.gwt.user.client.Window.ClosingHandler;

/**
 * The base class for GWT Mosaic applications.
 * <p>
 * This class defines simple lifecycle for GWT Mosaic applications: {@code
 * initialize}, {@startup}, {@ready}, and {@shutdown}. The {@code Application's
 * startup} method is responsible for creating the initial GUI and making it
 * visible, and the {@code shutdown} method for hiding the GUI and performing
 * any other cleanup actions before the application exists. The {@initialize}
 * method can be used to configure system properties that must be set before the
 * GUI is constructed and the {@code ready} method is for applications that want
 * to do a little bit extra work once the GUI is "ready" to use. Concrete
 * subclasses must provide the {@code startup} method.
 * <p>
 * Applications are started with the static launch method. Applications use the
 * {@code ApplicationContext singleton} to find resources, actions, local
 * storage, and so on.
 * <p>
 * All {@code Application} subclasses must override {@code startup} and they
 * should call {@code #exit} (which calls {@code shutdown}) to exit. Here's an
 * example of a complete "Hello World" Application:
 * 
 * <pre>
 * ...
 * </pre>
 * 
 * @author georgopoulos.georgios(at)gmail.com
 * 
 */
public abstract class Application extends AbstractBean {

  private static Application application = null;

  /**
   * The {@code Application} singleton.
   * 
   * @return the Application singleton
   * @see #launch(Application)
   * @see #getInstance()
   */
  public static Application getInstance() {
    if (application == null) {
      throw new IllegalStateException("application is null");
    }
    return application;
  }

  /**
   * Calls the {@code Application's} {@code #startup()} method. The {@code
   * #launch(Application)} method is typically called from the application's
   * {#code EntryPoint#onModuleLoad{}} method:
   * 
   * <pre>
   *  public void onModuleLoad() {
   *    Application.launch(this);
   *  }
   * </pre>
   * 
   * @param <T>
   * @param application the {@code Application} instance to launch
   * @param args {@code main} style arguments
   * 
   * @see #shutdown()
   */
  public static synchronized <T extends Application> void launch(
      final T application, ApplicationResources resources) {

    Application.application = application;
    ApplicationContext context = application.getContext();

    if (resources.getApplicationConstants() == null) {
      context.setConstants((ApplicationConstants) GWT.create(ApplicationConstants.class));
    } else {
      context.setConstants(resources.getApplicationConstants());
    }

    if (resources.getApplicationImageBundle() == null) {
      context.setImageBundle((ApplicationImageBundle) GWT.create(ApplicationImageBundle.class));
    } else {
      context.setImageBundle(resources.getApplicationImageBundle());
    }

    ApplicationFramework applicationFramework = GWT.create(ApplicationFramework.class);
    applicationFramework.setupActions();

    Command doCreateAndShowGUI = new Command() {
      public void execute() {
        try {
          Application.application.initialize();
          Application.application.startup();
          // Application.application.waitForReady();
          Application.application.ready();
        } catch (Exception e) {
          GWT.log(e.getMessage(), e);
        }
      }
    };
    DeferredCommand.addCommand(doCreateAndShowGUI);
  }

  public static synchronized <T extends Application> void launch(
      final T application) {
    launch(application, new ApplicationResources() {
      public ApplicationConstants getApplicationConstants() {
        return (ApplicationConstants) GWT.create(ApplicationConstants.class);
      }

      public ApplicationImageBundle getApplicationImageBundle() {
        return (ApplicationImageBundle) GWT.create(ApplicationImageBundle.class);
      }
    });
  }

  private final ApplicationContext context;

  /**
   * Not to be called directly, see {@link #launch}.
   * <p>
   * Subclasses can provide a no-args constructor to initialize private final
   * state however GUI initialization, and anything else that might refer to
   * public API, should be done in the {@link #startup()} method.
   */
  protected Application() {
    context = GWT.create(ApplicationContext.class);

    /*
     * Initialize the ApplicationContext and application properties.
     */
    context.setApplication(this);

    /*
     * Setup window closing and close handlers.
     */
    Window.addWindowClosingHandler(new ClosingHandler() {
      public void onWindowClosing(ClosingEvent event) {
        event.setMessage("");
      }
    });
    Window.addCloseHandler(new CloseHandler<Window>() {
      public void onClose(CloseEvent<Window> event) {
        shutdown();
      }
    });
  }

  /**
   * The ApplicationContext singleton for this Application.
   * 
   * @return the Application's ApplicationContext singleton
   */
  public final ApplicationContext getContext() {
    return context;
  }

  /**
   * Responsible for initializations that must occur before the GUI is
   * constructed by {@code #startup()}.
   * <p>
   * This method is called by the static {@link #launch(Application, String[])}
   * method, before {@code #startup()} is called. Subclasses that want to do any
   * initialization work before {@code startup} must override it.
   * <p>
   * By default {@code initialize()} does nothing.
   * 
   * @see #launch(Application, String[])
   * @see #startup()
   * @see #shutdown()
   */
  protected void initialize() {
    // Nothing to do here!
  }

  /**
   * Responsible for starting the application; for creating and showing the
   * initial GUI.
   * <p>
   * This method is called by the static {@link #launch(Application, String[])}
   * method, subclasses must override it.
   * 
   * @see #launch(Application, String[])
   * @see #initialize()
   * @see #shutdown()
   */
  protected abstract void startup();

  /**
   * Called after the {@link #startup()} method has returned. When this method
   * is called, the application's GUI is ready to use.
   * <p>
   * It's usually important for an application to start up as quickly as
   * possible. Applications can override this method to do some additional start
   * up work, after the GUI is up and ready to use.
   * 
   * @see #launch(Application, String[])
   * @see #startup()
   * @see #shutdown()
   */
  protected void ready() {
    // Nothing to do here!
  }

  /**
   * Called when the application is unloaded. Subclasses may override this
   * method to do any cleanup tasks that are necessary before exiting.
   * Obviously, you'll want to try and do as little as possible at this point.
   * 
   * @see #startup()
   * @see #ready()
   */
  protected void shutdown() {
    // Nothing to do here!
  }

}
