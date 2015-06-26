import java.util.List;


public class Bewegung {
	private BewegungsErzeuger _erzeuger;
	private Object _erzeugerArg;
	
	private List<Position> _verlauf;
	int _laenge;
	
	public Position getEndPosition()
	{
		return _verlauf.get(_verlauf.size()-1);
	}
	
	public void fuehreBewegungAus()
	{
		_erzeuger.erzeugeBewegung(_erzeugerArg);
	}
	
	public int getEntfernung()
	{
		return _laenge;
	}
	
	public Position getStartPosition()
	{
		return _verlauf.get(0);
	}
	
	public List<Position> getVerlauf()
	{
		return _verlauf;
	}

}
