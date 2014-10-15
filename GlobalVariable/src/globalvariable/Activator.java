package globalvariable;

import globalvariable.services.AgentInstances;
import globalvariable.services.CellInstances;
import globalvariable.services.CurrentScore;
import globalvariable.services.MonitorForestStatusPolicy;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		context.registerService(AgentInstances.class.getName(),new AgentInstances(), null);
		context.registerService(CellInstances.class.getName(), new CellInstances(), null);
		context.registerService(CurrentScore.class.getName(), new CurrentScore(), null);
		context.registerService(MonitorForestStatusPolicy.class.getName(), new MonitorForestStatusPolicy(), null);
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
	}

}
