package collaborationmonitor.service;

import globalvariable.services.CellInstances;
import globalvariable.services.CurrentScore;
import hostactivator.HostActivator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import monitorcontroller.MonitorForestStatusController;
import userbehaviormodelgenerator.service.UBMGeneratorService;

public class CollaborationMonitorService implements UBMGeneratorService{
	static MonitorForestStatusController controller = new MonitorForestStatusController();
	

	@Override
	public List genCurBM(String dirPath) {
		// TODO Auto-generated method stub
		CurrentScore currentScore = (CurrentScore) HostActivator.getService("CurrentScore");
		
		if(isInBound())
		{
			List result = new ArrayList<>();
			result.add(true);
			return result;
		}
		
		return null;
	}
	
	private static boolean isInBound()
	{
		if(CurrentScore.optimalAgentList.size() == 9)
		{
			double score = controller.getScore(CellInstances.getCellList(),CurrentScore.optimalAgentList);
		
		//LoggerUtil.write( LoggerUtil.LOGPANE, "Current Score:" + score);
		if(score > 0.46)
			return true;
		return false;
		}
		return false;
	}
	
	
}
