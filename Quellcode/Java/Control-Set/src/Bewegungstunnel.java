import java.util.Map;
import java.util.Set;


public class Bewegungstunnel {

	private Set<Bewegung> _content;
	private Position _endPosition;
	
	Map<String, Double[]> _borders;
	private final int INDEX_MAX=1;
	private final int INDEX_MIN=0;
	
	public Bewegungstunnel(Set<Bewegung> content)
	{
		_content = content;
		if (!content.isEmpty())
		{
			_endPosition=content.iterator().next().getEndPosition();
		}
		for(Bewegung bew : _content)
		{
			
			for(Position pos: bew.getVerlauf())
			{
				for(String dim: pos.keySet())
				{
					Double[] border = _borders.get(dim);
					if (border == null)
					{
						border= new Double[2];
						border[INDEX_MAX]=0.0;
						border[INDEX_MIN]=0.0;
					}
					if(pos.getValue(dim) > border[INDEX_MAX]) border[INDEX_MAX]  = pos.getValue(dim);
					if(pos.getValue(dim) < border[INDEX_MIN]) border[INDEX_MIN]  = pos.getValue(dim);
					_borders.put(dim, border);
				}
			}
		}
		
	}
	
	public boolean containsPosition(Position pos)
	{
		for(String key:pos.keySet())
		{
			if(_borders.get(key)!= null)
			{
				Double posValue = pos.getValue(key);
				Double max = _borders.get(key)[INDEX_MAX];
				Double min = _borders.get(key)[INDEX_MIN];
				if (posValue > max || posValue < min) return false;
			}else{
				return false;
			}
		}
		return true;
	}
	
	public Position getEndPosition()
	{
		return _endPosition;
	}
	
	public Bewegung getMittelWeg(String dim)
	{
		//To-Do Implementieren !!!
		return null;
	}
	
	
	public Bewegung getKuerzestenWeg()
	{
		Bewegung puffer=null;
		for (Bewegung bew: _content)
		{
			if(puffer==null)
			{
				puffer = bew;
			}else if(puffer.geometrischeLaenge() > bew.geometrischeLaenge())
			{
				puffer = bew;
			}
		}
		return puffer;
	}
	
	public void setStartPosition(Position neuerStart)
	{
		for (Bewegung bew: _content)
		{
			bew.setStartPosition(neuerStart);
		}
		if (!_content.isEmpty()) _endPosition=_content.iterator().next().getEndPosition();
	}
}
