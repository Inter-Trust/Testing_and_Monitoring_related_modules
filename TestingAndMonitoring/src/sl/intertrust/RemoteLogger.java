package sl.intertrust;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import com.montimage.mmt.client.connector.MMTClientConnector;
import com.montimage.mmt.client.connector.MMTClientConnectorConfig;
import com.montimage.mmt.client.connector.MMTProtocolConfig;
import com.montimage.mmt.client.exception.MMTConnectorException;
import com.montimage.mmt.client.exception.MMTInitializationException;

public class RemoteLogger {
	
	MMTClientConnector connector;
	boolean isConnected;
	long reconnectTimeout;
	
	URL mmt_url;
	int serviceID;
	
	RemoteLogger(String url, int sID) throws MMTInitializationException, MMTConnectorException, IOException {
		isConnected = false;
		
		try {
			mmt_url = new URL(url);
		}
		catch (MalformedURLException e) {
			throw new MMTInitializationException(e.getMessage());
		}
		
		serviceID = sID;

		connect();
	}

	private void connect() throws MMTInitializationException, MMTConnectorException, IOException {
       
	   MMTClientConnectorConfig connectorConfig = new MMTClientConnectorConfig();
       connectorConfig.setServerURL(mmt_url);
       connectorConfig.setKeepAlive(true);

       MMTProtocolConfig protoConfig;
       protoConfig = new MMTProtocolConfig(65537, "INTER-TRUST");
       protoConfig.setEventValidationLevel(MMTProtocolConfig.NoEventValidation);
       
       connector = new MMTClientConnector(serviceID);
       connector.setConnectorConfig(connectorConfig);
       connector.setProtoConfig(protoConfig);
       connector.setPayloadFormat(MMTClientConnector.JSON);
       
       isConnected = true;
    }

	public boolean send(AttributeStore as, String eventType){ 
		if (!isConnected) {
			if (System.currentTimeMillis() < reconnectTimeout) {
				System.err.println("There was a remote log error earlier, now skipping messages for a few seconds...");
				return false;
			}
		}
		
		// we are over the timeout, so trying to send, and see if connection has been restored
		try {
			connector.processEvent(System.currentTimeMillis(), eventType, as.to_remote_log());
			isConnected = true;
			return true;
		} catch (Exception e) {
			System.err.println("Remote log error: " + e);
			isConnected = false;
			reconnectTimeout = System.currentTimeMillis() + 5000;
			return false;
		}
	}
} 