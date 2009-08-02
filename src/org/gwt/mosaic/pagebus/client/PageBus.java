/*
 * Copyright (c) 2008-2009 GWT Mosaic Georgios J. Georgopoulos.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.gwt.mosaic.pagebus.client;

import com.google.gwt.core.client.JavaScriptException;

/**
 * A <a href="http://developer.tibco.com/pagebus/" target="_blank">TIBCO PageBus
 * v1.2</a> wrapper class.
 * <p>
 * It provides static methods to send and receive messages between your Ajax
 * components and a repository to store information in a way that allows other
 * components to look it up on demand.
 * <p>
 * Example code:
 * <p>
 * The following code demonstrates the {@link PageBus#publish(String, Object)}
 * operation.
 * 
 * <pre><code>
 *  // Create the message
 *  WorkItem workItem = new WorkItem("654321", "WI-123", "Here is some info.");
 *  
 *  // Publish the message using PageBus
 *  PageBus.publish("eg.workItem.onSelect", message);
 * </code></pre>
 * <p>
 * The following code demonstrates the
 * {@link PageBus#subscribe(String, SubscriberCallback)} operation.
 * 
 * <pre><code>
 *  // Create the subscriberData
 *  // This could be an object or simply a string, or null
 *  MySubscriberData mySubscriberData = new MySubscriberData("Whathever I, "want to put here");
 *  
 *  // Subscribe to a subject
 *  Subscription mySubscription = PageBus.subscribe("eg.workItem.onSelect", new SubscriberCallback() {
 *      public void execute(String subject, Object message, Object subscriberData) {
 *
 *        // Do something with message, subscriberData
 *        // and possibly subject.
 *        
 *        WorkItem workItem = (WorkItem) message;
 *        MySubscriberData mySubscriberData = (MySubscriberData) subscriberData;
 *
 *      }
 *    }, mySubscriberData);
 * </code></pre>
 * <p>
 * The following example demonstrates the {@link PageBus#store(String, Object)}
 * operation.
 * 
 * <pre><code>
 *  // Create a value
 *  MyValue myValue = new MyValue(123, "def");
 *  // Store it
 *  PageBus.store("com.example.sample", myValue);
 *  // Clear it
 *  PageBus.store("com.example.sample", null);
 * </code></pre>
 * <p>
 * The following example demonstrates the
 * {@link PageBus#query(String, QueryCallback)} operation.
 * 
 * <pre><code>
 *  // Query 1
 *  PageBus.query("com.example.sample", new QueryCallback() {
 *      public boolean onResult(String subject, Object value, Object data) {
 *        Window.alert(value);
 *        return true;
 *      }
 *    });
 *  
 *  // Query 2
 *  PageBus.query("com.example.foo.*.bar", new QueryCallback() {
 *      public boolean onResult(String subject, Object value, Object data) {
 *        Window.alert(value + " (data: " + data + ")");
 *        return true;
 *      }
 *    }, "Some Data");
 * </code></pre>
 * 
 * @author georgopoulos.georgios(at)gmail.com
 * 
 */
public class PageBus {

  private static final String BAD_NAME = "BadName";
  private static final String BAD_PARAMETER = "BadParameter";
  private static final String STACK_OVERFLOW = "StackOverflow";

  /**
   * Default constructor.
   */
  private PageBus() {
    // Nothing to do here!
  }

  /**
   * The subscribe operation subscribes to a subject and passes in the reference
   * to a callback function. The callback function is invoked with the
   * subscriber data and message whenever someone publishes a message on the
   * subscribed subject. Subscribed subjects can be wildcard subjects.
   * 
   * @param subject subject name (period-delimited string, for example:
   *          eg.customer.onSelect and log.info) to which to create the
   *          subscription
   * @param subscriberCallback function for PageBus to invoke when a message is
   *          published on the subscribed subject (the parameter must NOT be
   *          {@code null})
   * @return a {@link Subscription} object
   * @throws BadNameException the specified subject name did not follow the
   *           subject rules
   * @throws BadParameterException a {@code null} value was specified for the
   *           parameter callback
   */
  public static Subscription subscribe(final String subject,
      final SubscriberCallback subscriberCallback) {
    return subscribe(subject, null, subscriberCallback, null, null);
  }

  /**
   * The subscribe operation subscribes to a subject and passes in the reference
   * to a callback function. The callback function is invoked with the
   * subscriber data and message whenever someone publishes a message on the
   * subscribed subject. Subscribed subjects can be wildcard subjects.
   * 
   * @param subject subject name (period-delimited string, for example:
   *          eg.customer.onSelect and log.info) to which to create the
   *          subscription
   * @param subscriberCallback function for PageBus to invoke when a message is
   *          published on the subscribed subject (the parameter must NOT be
   *          {@code null})
   * @param subscriberData user-defined data ({@code null} values are permitted)
   * @return a {@link Subscription} object
   * @throws BadNameException the specified subject name did not follow the
   *           subject rules
   * @throws BadParameterException a {@code null} value was specified for the
   *           parameter callback
   */
  public static Subscription subscribe(final String subject,
      final SubscriberCallback subscriberCallback, final Object subscriberData) {
    return subscribe(subject, null, subscriberCallback, subscriberData, null);
  }

  /**
   * The subscribe operation subscribes to a subject and passes in the reference
   * to a callback function. The callback function is invoked with the
   * subscriber data and message whenever someone publishes a message on the
   * subscribed subject. Subscribed subjects can be wildcard subjects.
   * 
   * @param subject subject name (period-delimited string, for example:
   *          eg.customer.onSelect and log.info) to which to create the
   *          subscription
   * @param subscriberCallback function for PageBus to invoke when a message is
   *          published on the subscribed subject (the parameter must NOT be
   *          {@code null})
   * @param filter optional filter callback function
   * @return a {@link Subscription} object
   * @throws BadNameException the specified subject name did not follow the
   *           subject rules
   * @throws BadParameterException a {@code null} value was specified for the
   *           parameter callback
   */
  public static Subscription subscribe(final String subject,
      final SubscriberCallback subscriberCallback, final Filter filter) {
    return subscribe(subject, null, subscriberCallback, null, filter);
  }

  /**
   * The subscribe operation subscribes to a subject and passes in the reference
   * to a callback function. The callback function is invoked with the
   * subscriber data and message whenever someone publishes a message on the
   * subscribed subject. Subscribed subjects can be wildcard subjects.
   * 
   * @param subject subject name (period-delimited string, for example:
   *          eg.customer.onSelect and log.info) to which to create the
   *          subscription
   * @param subscriberCallback function for PageBus to invoke when a message is
   *          published on the subscribed subject (the parameter must NOT be
   *          {@code null})
   * @param subscriberData user-defined data ({@code null} values are permitted)
   * @param filter optional filter callback function
   * @return a {@link Subscription} object
   * @throws BadNameException the specified subject name did not follow the
   *           subject rules
   * @throws BadParameterException a {@code null} value was specified for the
   *           parameter callback
   */
  public static Subscription subscribe(final String subject,
      final SubscriberCallback subscriberCallback, final Object subscriberData,
      final Filter filter) {
    return subscribe(subject, null, subscriberCallback, subscriberData, filter);
  }

  /**
   * The subscribe operation subscribes to a subject and passes in the reference
   * to a callback function. The callback function is invoked with the
   * subscriber data and message whenever someone publishes a message on the
   * subscribed subject. Subscribed subjects can be wildcard subjects.
   * 
   * @param subject subject name (period-delimited string, for example:
   *          eg.customer.onSelect and log.info) to which to create the
   *          subscription
   * @param scope if the value is non-{@code null}, the object specified here
   *          becomes the context of the callback function, if the value is
   *          {@code null}, then the object window is used as the default
   *          context of the callback function
   * @param subscriberCallback function for PageBus to invoke when a message is
   *          published on the subscribed subject (the parameter must NOT be
   *          {@code null})
   * @param subscriberData user-defined data ({@code null} values are permitted)
   * @param filter optional filter callback function
   * 
   * @return a {@link Subscription} object
   * 
   * @throws BadNameException the specified subject name did not follow the
   *           subject rules
   * @throws BadParameterException a {@code null} value was specified for the
   *           parameter callback
   */
  public static Subscription subscribe(final String subject,
      final Object scope, final SubscriberCallback subscriberCallback,
      final Object subscriberData, final Filter filter) {
    try {
      return subscribeImpl(subject, scope, subscriberCallback, subscriberData,
          filter);
    } catch (JavaScriptException e) {
      if (BAD_NAME.equals(e.getDescription())) {
        throw new BadNameException(e.getDescription());
      } else if (BAD_PARAMETER.equals(e.getDescription())) {
        throw new BadParameterException(e.getDescription());
      } else {
        throw e;
      }
    }
  }

  private static native Subscription subscribeImpl(final String subject,
      final Object scope, final SubscriberCallback subscriberCallback,
      final Object subscriberData, final Filter filter)
  /*-{
    var handle;
    
    if (filter) {
    
      handle = $wnd.PageBus.subscribe(
      
        subject,
        
        scope, 
      
        function(subject, message, subscriberData) {
          subscriberCallback.@org.gwt.mosaic.pagebus.client.SubscriberCallback::onMessage(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)(subject,message,subscriberData);
        },
      
        subscriberData,
        
        function(subject, message, subscriberData) {
          return filter.@org.gwt.mosaic.pagebus.client.Filter::execute(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)(subject,message,subscriberData);
        }

      )    
    } else {
    
      handle = $wnd.PageBus.subscribe(
  	    
  	    subject,
  	    
  	    scope, 
  	  
  	    function(subject, message, subscriberData) {
  	      subscriberCallback.@org.gwt.mosaic.pagebus.client.SubscriberCallback::onMessage(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)(subject,message,subscriberData);
  	    },
  	  
  	    subscriberData

      );

    }
    
    return { "handle" : handle };

  }-*/;

  /**
   * The publish operation publishes one message on a subject. The message is
   * delivered to all current subscribers whose subscriptions match the subject.
   * <p>
   * If there are multiple subscribers, the order in which the message is
   * delivered to the various subscribers is arbitrary.
   * <p>
   * If publish is called twice, so that two messages are published in sequence,
   * then subscribers will always receive the message that was published first
   * before they receive the second message.
   * 
   * @param subject the subject name on which to publish the message (this must
   *          <em>not</em> be a wildcard subject)
   * @param message the message content (the reference to the original message
   *          is received by all subscribers rather that a copy)
   * 
   * @throws BadNameException the specified subject name did not follow the
   *           subject rules
   * @throws StackOverflowException probable infinite publisher-subscriber loop
   *           detected and interrupted
   * @see #subscribe(String, SubscriberCallback)
   * @see #subscribe(String, SubscriberCallback, Object)
   * @see #subscribe(String, SubscriberCallback, Filter)
   * @see #subscribe(String, SubscriberCallback, Object, Filter)
   * @see #subscribe(String, Object, SubscriberCallback, Object, Filter)
   * @see #unsubscribe(Subscription)
   */
  public static void publish(final String subject, final Object message) {
    try {
      publishImpl(subject, message);
    } catch (JavaScriptException e) {
      if (STACK_OVERFLOW.equals(e.getDescription())) {
        throw new StackOverflowException(e.getDescription());
      } else {
        throw e;
      }
    }
  }

  private static native void publishImpl(final String subject,
      final Object message)
  /*-{
    $wnd.PageBus.publish(subject, message);
  }-*/;

  /**
   * The unsubscribe operation cancels a subscription, using the
   * {@link Subscription} object returned by {@code PageBus.subscribe} as a
   * handle to the subscription.
   * 
   * @param subscription the {@link Subscription} object returned
   * 
   * @throws BadParameterException the value of the subscription parameter is
   *           not a valid subscription
   */
  public static void unsubscribe(final Subscription subscription) {
    try {
      unsubscribeImpl(subscription);
    } catch (JavaScriptException e) {
      if (BAD_PARAMETER.equals(e.getDescription())) {
        throw new BadParameterException(e.getDescription());
      } else {
        throw e;
      }
    }
  }

  private static native void unsubscribeImpl(final Subscription subscription)
  /*-{
    $wnd.PageBus.unsubscribe(subscription.handle);
  }-*/;

  /**
   * The store operation stores a value in the PageBus repository under the
   * specified name. It then publishes a message on the specified name to notify
   * any subscribers of the change. If the value is {@code null}, then the store
   * operation removes the specified repository entry.
   * 
   * @param subject the name under which value is stored in repository, and on
   *          which repository then publishes notification message (must
   *          <em>not</em> be a wildcard subject or {@code null}
   * @param value the value that is to be stored (typically object or string)
   * 
   * @throws BadNameException the specified subject name did not follow the
   *           subject rules
   * @throws StackOverflowException propable infinite publisher-subscriber loop
   *           detected and interrupted
   * @see #query(String, QueryCallback)
   * @see #query(String, QueryCallback, Filter)
   * @see #query(String, QueryCallback, Object)
   * @see #query(String, QueryCallback, Object, Filter)
   * @see #query(String, Object, QueryCallback, Object, Filter)
   */
  public static void store(final String subject, final Object value) {
    storeImpl(subject, value, false);
  }

  /**
   * The store operation stores a value in the PageBus repository under the
   * specified name. It then publishes a message on the specified name to notify
   * any subscribers of the change. If the value is {@code null}, then the store
   * operation removes the specified repository entry.
   * 
   * @param subject the name under which value is stored in repository, and on
   *          which repository then publishes notification message (must
   *          <em>not</em> be a wildcard subject or {@code null}
   * @param value the value that is to be stored (typically object or string)
   * @param quiet if {@code true} {@link PageBus#store(String, Object, boolean)}
   *          does not publish a notification message
   * @throws BadNameException the specified subject name did not follow the
   *           subject rules
   * @throws StackOverflowException propable infinite publisher-subscriber loop
   *           detected and interrupted
   * @see #query(String, QueryCallback)
   * @see #query(String, QueryCallback, Filter)
   * @see #query(String, QueryCallback, Object)
   * @see #query(String, QueryCallback, Object, Filter)
   * @see #query(String, Object, QueryCallback, Object, Filter)
   */
  public static void store(final String subject, final Object value,
      boolean quiet) {
    try {
      storeImpl(subject, value, quiet);
    } catch (JavaScriptException e) {
      if (BAD_NAME.equals(e.getDescription())) {
        throw new BadNameException(e.getDescription());
      } else if (BAD_PARAMETER.equals(e.getDescription())) {
        throw new BadParameterException(e.getDescription());
      } else {
        throw e;
      }
    }
  }

  private static native void storeImpl(final String subject,
      final Object value, boolean quiet)
  /*-{
    $wnd.PageBus.store(subject, value, { "quiet": quiet });
  }-*/;

  /**
   * The query operation finds all PageBus repository entries that match the
   * specified name and invokes a specified callback function to deliver these
   * values to the application.
   * 
   * @param subject the subject name (may include wildcards)
   * @param queryCallback the callback function for PageBus to invoke for each
   *          query result
   * @see #store(String, Object)
   * @see #store(String, Object, boolean)
   */
  public static void query(final String subject, QueryCallback queryCallback) {
    query(subject, null, queryCallback, null, null);
  }

  /**
   * The query operation finds all PageBus repository entries that match the
   * specified name and invokes a specified callback function to deliver these
   * values to the application.
   * 
   * @param subject the subject name (may include wildcards)
   * @param queryCallback the callback function for PageBus to invoke for each
   *          query result
   * @param userData user-defined data ({@code null} values are permitted)
   * @see #store(String, Object)
   * @see #store(String, Object, boolean)
   */
  public static void query(final String subject, QueryCallback queryCallback,
      Object userData) {
    queryImpl(subject, null, queryCallback, userData, null);
  }

  /**
   * The query operation finds all PageBus repository entries that match the
   * specified name and invokes a specified callback function to deliver these
   * values to the application.
   * 
   * @param subject the subject name (may include wildcards)
   * @param queryCallback the callback function for PageBus to invoke for each
   *          query result
   * @param filter optional filter callback function
   * @see #store(String, Object)
   * @see #store(String, Object, boolean)
   */
  public static void query(final String subject, QueryCallback queryCallback,
      final Filter filter) {
    queryImpl(subject, null, queryCallback, null, filter);
  }

  /**
   * The query operation finds all PageBus repository entries that match the
   * specified name and invokes a specified callback function to deliver these
   * values to the application.
   * 
   * @param subject the subject name (may include wildcards)
   * @param queryCallback the callback function for PageBus to invoke for each
   *          query result
   * @param userData user-defined data ({@code null} values are permitted)
   * @param filter optional filter callback function
   * @see #store(String, Object)
   * @see #store(String, Object, boolean)
   */
  public static void query(final String subject, QueryCallback queryCallback,
      Object userData, final Filter filter) {
    queryImpl(subject, null, queryCallback, userData, filter);
  }

  /**
   * The query operation finds all PageBus repository entries that match the
   * specified name and invokes a specified callback function to deliver these
   * values to the application.
   * 
   * @param subject the subject name (may include wildcards)
   * @param scope if the value is non-{@code null}, the object specified here
   *          becomes the context of the callback function
   * @param queryCallback the callback function for PageBus to invoke for each
   *          query result
   * @param userData user-defined data ({@code null} values are permitted)
   * @param filter optional filter callback function
   * @see #store(String, Object)
   * @see #store(String, Object, boolean)
   */
  public static void query(final String subject, final Object scope,
      QueryCallback queryCallback, Object userData, final Filter filter) {
    queryImpl(subject, scope, queryCallback, userData, filter);
  }

  public static native void queryImpl(final String subject, final Object scope,
      QueryCallback queryCallback, Object userData, final Filter filter)
  /*-{
    if (filter) {
    
      $wnd.PageBus.query(
      
        subject,
        
        scope, 
      
        function(subject,value,userData) {
          return queryCallback.@org.gwt.mosaic.pagebus.client.QueryCallback::onResult(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)(subject,value,userData);
        },
      
        userData,
        
        function(subject,value,userData) {
          return filter.@org.gwt.mosaic.pagebus.client.Filter::execute(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)(subject,value,userData);
        }

      )    
    } else {
    
      $wnd.PageBus.query(
        
        subject,
        
        scope, 
      
        function(subject, value, userData) {
          return queryCallback.@org.gwt.mosaic.pagebus.client.QueryCallback::onResult(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)(subject,value,userData);
        },
      
        userData

      );

    }
  }-*/;

}
