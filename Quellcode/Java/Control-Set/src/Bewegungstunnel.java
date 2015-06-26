import java.util.Map;
import java.util.Set;


public class Bewegungstunnel {

	private Set<Bewegung> _content;
	private Position _endPosition;
	private Position _startPosition;
	
	Map<String, Double[]> _borders;
	private final int INDEX_MAX=1;
	private final int INDEX_MIN=0;
	
	public Bewegungstunnel(Set<Bewegung> content)
	{
		_content = content;
		if (!content.isEmpty())
		{
			_startPosition=content.iterator().next().getStartPosition();
			_endPosition=content.iterator().next().getStartPosition();
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
		return _content.contains(pos);
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
}
