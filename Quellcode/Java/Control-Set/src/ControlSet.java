import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class ControlSet{

	Map<Position, Bewegung> _movements;

public ControlSet(Map<Position, Bewegung> movements) {
	_movements=movements;
}

public ControlSet (Set<BewegungsErzeuger> bewFactories, int intervall)
{
	//erzeuge alle möglichen Bewegungen
	Set<Bewegung> alleBewegungen=new HashSet<>();

	for(BewegungsErzeuger factory: bewFactories)
	{
		alleBewegungen.addAll(factory.erzeugeAlleBewegungen());
	}

	//Fasse Bewegungen zu Tunneln zusammen

	Map<Position, Set<Bewegung >> endPunkte = new HashMap<>();

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

	Map<Position, Bewegungstunnel> moeglicheBewegungsTunnel = new HashMap<>();

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
	
	_movements = new HashMap<>();

	for(Bewegungstunnel tunnel :sortedTunnel)
	{
		Bewegung shortestMovment=tunnel.getKuerzestenWeg();
		_movements.put(shortestMovment.getEndPosition(), shortestMovment);
	}



}

private boolean endeIstErreichbar(Bewegungstunnel tunnel, Set<Bewegungstunnel> subTunnels)
{
	for(Bewegungstunnel subTunnel: subTunnels)
	{
		boolean endeIstErreicht=false;
		Set<Bewegungstunnel> geprüfteTunnel = new HashSet<>();
		
		while(!endeIstErreicht)
		{
			
		}
	}
	return false;
}





}