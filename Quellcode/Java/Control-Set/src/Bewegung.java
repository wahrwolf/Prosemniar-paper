import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class Bewegung {
	public static final String VARIABLE_FORMAT="";
	public static final String MULIPLIKATION_FORMAT="";
	public static final String DIVISION_FORMAT="";
	public static final String ADDITION_FORMAT="";
	public static final String SUBSTRAKION_FORMAT="";
	public static final String KLAMMER_FORMAT="";
	public static final String MODULO_FORMAT="";
	
	
	
	
	
	
	private BewegungsErzeuger _erzeuger;
	private Object _erzeugerArg;
	
	private Position _startPosition;
	private List<Map<String, String>> _relativerVerlauf;
	
	public Position getEndPosition()
	{
		List<Position> verlauf=getVerlauf();
		return verlauf.get(verlauf.size()-1);
	}
	
	
	public void fuehreBewegungAus()
	{
		_erzeuger.erzeugeBewegung(_erzeugerArg);
	}
	
	public int getEntfernung()
	{
		return (int) geometrischeLaenge();
	}
	
	public Position getStartPosition()
	{
		return _startPosition;
	}
	
	public void setStartPosition(Position neuerStart)
	{
		_startPosition = neuerStart;
	}
	
	public List<Position> getVerlauf()
	{
		List<Position> verlauf = new LinkedList<>();
		Position markiertePosition = new NeutralePosition();
		
		for(Map<String, String> delta: _relativerVerlauf)
		{
			Map<String, Double> dimensions = new HashMap<>();
			for(String dim: delta.keySet())
			{
				dimensions.put(dim, parseSchritt(delta.get(dim), markiertePosition));
			}
			markiertePosition = new Position(dimensions,_startPosition.getToleranzen());
			verlauf.add(markiertePosition);
		}
		
		return verlauf;
	}
	
	
	public double geometrischeLaenge()
	{
		double puffer=0.0;
		List<Position> verlauf = getVerlauf();
		for(int i=0; i-1<verlauf.size();i++)
		{
			puffer+= verlauf.get(i).getTotalDistance(verlauf.get(i+1));
		}
		return puffer;
	}
	
	private double parseSchritt(String formula, Position pos) throws IllegalArgumentException, NumberFormatException
	{
		if (formula.matches(VARIABLE_FORMAT))
		{
			if(pos.getValue(formula) == null) return 0.0;
			return pos.getValue(formula);
		} else if (formula.matches(ADDITION_FORMAT)){
			String summand[]=formula.split("+");
			return parseSchritt(summand[0], pos)+  parseSchritt(summand[1], pos);
		} else if (formula.matches(SUBSTRAKION_FORMAT)){
			String summand[]=formula.split("-");
			return parseSchritt(summand[0], pos)-  parseSchritt(summand[1], pos);
		} else if (formula.matches(MULIPLIKATION_FORMAT)){
			String faktor[]=formula.split("\\*");
			return parseSchritt(faktor[0], pos)*  parseSchritt(faktor[1], pos);
		} else if (formula.matches(DIVISION_FORMAT)){
			String faktor[]=formula.split("/");
			return parseSchritt(faktor[0], pos) /  parseSchritt(faktor[1], pos);
		} else if (formula.matches(KLAMMER_FORMAT)){
			return parseSchritt(formula.substring(1, formula.length()-1), pos);
		} else if (formula.matches(MODULO_FORMAT)){
			String faktor[]=formula.split("%");
			return parseSchritt(faktor[0], pos) %  parseSchritt(faktor[1], pos);
		}
	
		
		return Double.parseDouble(formula);
	}

}
