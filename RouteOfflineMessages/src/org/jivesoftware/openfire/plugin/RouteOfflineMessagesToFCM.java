package org.jivesoftware.openfire.plugin;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.jivesoftware.openfire.OfflineMessageListener;
import org.jivesoftware.openfire.OfflineMessageStrategy;
import org.jivesoftware.openfire.XMPPServer;
import org.jivesoftware.openfire.container.Plugin;
import org.jivesoftware.openfire.container.PluginManager;
import org.jivesoftware.util.PropertyEventDispatcher;
import org.jivesoftware.util.PropertyEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xmpp.component.Component;
import org.xmpp.component.ComponentException;
import org.xmpp.component.ComponentManager;
import org.xmpp.component.ComponentManagerFactory;
import org.xmpp.packet.JID;
import org.xmpp.packet.Message;
import org.xmpp.packet.Packet;

import com.google.gson.JsonObject;

public class RouteOfflineMessagesToFCM implements Component, Plugin, OfflineMessageListener {

	private ComponentManager componentManager;
	private PluginManager pluginManager;
	private String serviceName;
	private static String serverName;
	private OfflineMessageStrategy strategy;

	private static final Logger Log = LoggerFactory.getLogger(RouteOfflineMessagesToFCM.class);
	public static final String SERVICENAME = "plugin.route.RouteOfflineMessagesToFCM";

	public RouteOfflineMessagesToFCM() {
		serverName = XMPPServer.getInstance().getServerInfo().getXMPPDomain();
		strategy = XMPPServer.getInstance().getOfflineMessageStrategy();
	}

	@Override
	public void messageBounced(Message arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void messageStored(Message arg0) {

		JsonObject dataObject = new JsonObject();
		dataObject.addProperty("psID", arg0.getTo().toString().split("@")[0]);
		dataObject.addProperty("description", arg0.getBody());
		dataObject.addProperty("category", "chat");
		/*JsonObject notificationObject = new JsonObject();
		notificationObject.addProperty("KEY", "VALUE");*/
		try {
			/*FCMHelper.getInstance().sendNotifictaionAndData(FCMHelper.TYPE_TO, "itc.induction.inductionapp",
					notificationObject, dataObject);*/
			AWSHelper.getInstance().sendAWSMessage(dataObject);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void destroyPlugin() {
		// TODO Auto-generated method stub
		strategy.removeListener(this);
		pluginManager = null;
		try {
			componentManager.removeComponent(serviceName);
			componentManager = null;
		} catch (Exception e) {
			if (componentManager != null) {
				Log.error(e.getMessage(), e);
			}
		}
		serviceName = null;
		serverName = null;
	}

	@Override
	public void initializePlugin(PluginManager manager, File arg1) {
		// TODO Auto-generated method stub
		pluginManager = manager;

		componentManager = ComponentManagerFactory.getComponentManager();
		try {
			componentManager.addComponent(serviceName, this);
		} catch (ComponentException e) {
			Log.error(e.getMessage(), e);
		}
		strategy.addListener(this);

	}

	@Override
	public void initialize(JID arg0, ComponentManager arg1) throws ComponentException {
		// TODO Auto-generated method stub

	}

	@Override
	public void processPacket(Packet arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void shutdown() {
		// TODO Auto-generated method stub

	}

	@Override
	public void start() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.xmpp.component.Component#getName()
	 */
	public String getName() {
		return pluginManager.getName(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.xmpp.component.Component#getDescription()
	 */
	public String getDescription() {
		return pluginManager.getDescription(this);
	}

}
