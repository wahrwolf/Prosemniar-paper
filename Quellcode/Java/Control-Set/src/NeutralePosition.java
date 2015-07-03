import java.util.Map;



public class NeutralePosition extends Position {

	public NeutralePosition()
	{
		super();
	}
	
	public NeutralePosition(Map<String, Double> toleranzen)
	{
		super(toleranzen);
	}
	
	public Double getValue(String dim)
	{
		return 0.0;
	}
	
	public Map<String, Double> getToleranzen()
	{
		return _toleranzen;
	}

}
