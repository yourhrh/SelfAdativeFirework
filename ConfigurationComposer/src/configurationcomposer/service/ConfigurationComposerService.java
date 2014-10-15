package configurationcomposer.service;

import monitorcontroller.MonitorForestStatusController;

public class ConfigurationComposerService implements IConfigurationComposerService{
	
	public void selectOptimalConfigSet()
	{
		MonitorForestStatusController monitorForestStatusController = new MonitorForestStatusController();
		monitorForestStatusController.getOptimalConfiguration();
	}
}