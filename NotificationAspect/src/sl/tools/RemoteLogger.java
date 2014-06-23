package sl.tools;

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
	
	RemoteLogger(String url) throws MalformedURLException, MMTInitializationException, MMTConnectorException, IOException {
		connect(url);
	}

   public void connect(String url) throws MalformedURLException, MMTInitializationException, MMTConnectorException, IOException {
       MMTClientConnectorConfig connectorConfig = new MMTClientConnectorConfig();
       MMTProtocolConfig protoConfig;
       URL mmt_url = new URL(url);
       connectorConfig.setServerURL(mmt_url);
       protoConfig = new MMTProtocolConfig(10, "Inter-Trust");
       protoConfig.setEventValidationLevel(MMTProtocolConfig.NoEventValidation);
       connector = new MMTClientConnector(1);
       connector.setConnectorConfig(connectorConfig);
       connector.setProtoConfig(protoConfig);
       connector.setPayloadFormat(MMTClientConnector.JSON);
   }

   public boolean log(AttributeStore as){ 
	   try {
		   connector.processEvent(System.currentTimeMillis(), "log", as.to_remote_log());
		   return true;
	   } catch(Exception e) {
		   System.out.println("Remote log error: "+e);
	   }
	   return false;
   }
} 