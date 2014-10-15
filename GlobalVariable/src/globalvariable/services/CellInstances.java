package globalvariable.services;

import java.util.ArrayList;

import Environment.ForestCell;

public class CellInstances {
	private static ArrayList<ForestCell> cellList = new ArrayList<ForestCell>();
	public static double monitoringDistance = 100;

	public static ArrayList<ForestCell> getCellList() {
		return cellList;
	}

	public static void setCellList(ArrayList<ForestCell> cellList) {
		CellInstances.cellList = cellList;
	}
	
	public static void printCell()
	{
		for(ForestCell c : cellList)
		{
			System.out.println(c.getFeatureList().get(0).toString());
		}
	}
	
	public static ForestCell getCell(int index)
	{
		return cellList.get(index);
	}
	
	
	
}
