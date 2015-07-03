import java.util.HashMap;
import java.util.Map;
import java.util.Set;


public class Position
{
	private Map<String,Double> _values;
	protected Map<String,Double> _toleranzen;
	//Prefix for User Defined Dimension wich doesnt effect totalLength!
	public final  static String UDD_PREFIX="UDD_";
	
	protected Position()
	{
		
	}
	
	public Position(Map<String,Double> values,Map<String,Double> toleranzen)
	{
		this(toleranzen);
		_values = values;
	}
	
	public Position(Map<String, Double> toleranzen)
	{
		_toleranzen = toleranzen;
		_values=new HashMap<>();
	}
	
	public boolean isNear(Position pos)
	{
		
		for(String dim: _values.keySet())
		{
			double value = _values.get(dim);
			double foreignValue = pos.getValue(dim);
			double tolerance = _toleranzen.get(dim);
			if ((foreignValue < value -tolerance)||(foreignValue > value + tolerance)) return false;
		}
		return true;
	}
	
	public Map<String,Double> getValues()
	{
		return _values;
	}
	public Double getValue(String dim)
	{
		return _values.get(dim);
	}

	public boolean equals(Object arg)
	{
		if (arg instanceof Position) 
		{
			Position pos = (Position) arg;
			return isNear(pos);
		}
		return false;
	}
	
	public Set<String> keySet()
	{
		return _values.keySet();
	}
	
	public double getTotalDistance(Position pos)
	{
		double distance = 0;

		for(String dim: _values.keySet())
		{
			if (!dim.startsWith(UDD_PREFIX))
			{
				Double value = _values.get(dim);
				Double foreignValue = pos.getValue(dim);
				if (foreignValue!=null) foreignValue=0.0;
				distance += Math.pow(value-foreignValue,2.0);
			}
		}
		for(String dim: pos.keySet())
		{
			if (!dim.startsWith(UDD_PREFIX))
			{
				if(!_values.keySet().contains(dim))
				{
					Double foreignValue = pos.getValue(dim);
					distance+= Math.pow(foreignValue,2.0);
				}
			}
		}
		return Math.sqrt(distance);
	}
	
	public Position moveWith(Bewegung delta)
	{
		return Position.add(this, delta.getEndPosition());
	}
	
	public static Position add(Position a, Position b)
	{
		if(a instanceof NeutralePosition) return b;
		if(b instanceof NeutralePosition) return a;
		
		Map<String,Double> values= new HashMap<>();
		values.putAll(a.getValues());
		
		for(String dim: b.keySet())
		{
			if(!b.keySet().contains(dim))
			{
				values.put(dim, b.getValue(dim));;
			}else{
				values.put(dim, values.get(dim)+b.getValue(dim));
			}
		}
		
		
		return null;
	}

}
