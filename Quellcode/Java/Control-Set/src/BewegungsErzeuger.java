import java.util.Set;


public abstract class BewegungsErzeuger {
	abstract public Bewegung erzeugeBewegung(Object arg);
	abstract public Set<Bewegung> erzeugeAlleBewegungen();

}
