import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class ControlSet{

	Map<Position, Bewegung> _movements;

public static ControlSet generateControlSet(Set<BewegungsErzeuger> bewFactories, int intervall, Map<String, Double> toleranzen)
{
	//erzeuge alle möglichen Bewegungen
	Set<Bewegung> alleBewegungen;

	for(BewegungsErzeuger factory: bewFactories)
	{
		alleBewegungen.addAll(factory.erzeugeAlleBewegungen());
	}

	//Fasse Bewegungen zu Tunneln zusammen

	Map<Position, Set<Bewegung >> endPunkte;

	for(Bewegung bewegung: alleBewegungen)
	{
		if(bewegung.getEntfernung() <= intervall)
		{
			Set<Bewegung> puffer= new HashSet<>();
			puffer.addAll(endPunkte.get(bewegung.getEndPosition()));
			puffer.add(bewegung);
			endPunkte.put(bewegung.getEndPosition(), puffer);
		}
		alleBewegungen.remove(bewegung);
	}

	Map<Position, Bewegungstunnel> moeglicheBewegungsTunnel;

	for(Set<Bewegung> bewegungen: endPunkte.values())
	{
		Bewegungstunnel tunnel=new Bewegungstunnel(bewegungen);
		moeglicheBewegungsTunnel.put(tunnel.getEndPosition(), tunnel);
	}

	//Versuche mit Sub-Bewgungen Tunnel zu "durchqueren"
	Set<Bewegungstunnel> sortedTunnel= new HashSet<>();
	 sortedTunnel.addAll(moeglicheBewegungsTunnel.values());

	for(Bewegungstunnel tunnel:sortedTunnel)
	{
		if(endeIstErreichbar(tunnel, sortedTunnel))
		{
			moeglicheBewegungsTunnel.remove(tunnel);
		}
	}

	//Normalisieren der übrigen Tunnel

	for(Bewegungstunnel tunnel :sortedTunnel)
	{
		//ToDo besser ausformulieren und impl

		//wähle bewegung die am dichtesten am mittlweg ist aus
		//füge bewegung + end pos zum control-set hinzu
	}

	return null;


}

private static boolean endeIstErreichbar(Bewegungstunnel tunnel, Set<Bewegungstunnel> subTunnels)
{
	for(Bewegungstunnel subTunnel: subTunnels)
	{
		//ToDo rekursive Prüffunktion !!!
	}
	return false;
}



}