package GameOfLife;

public class Cellule {
	private boolean etat;
	private Faction camp;
	
	/** Constructeur de la classe Cellule
	 *
	 */
	public Cellule(){
		this.etat = false;
		this.camp = null;
	}
	
	/** Accesseur retournant l'état de la cellule
	 * 
	 * @return Etat de la cellule
	 */
	public boolean getEtat(){
		return this.etat;
	}
	
	/** Accesseur retournant la faction de la cellule
	 * 
	 * @return Faction de la cellule
	 */
	public Faction getFaction(){
		return this.camp;
	}
	
	/** Selecteur donnant vie à la cellule
	 * 
	 */
	public void setLife(Faction faction){
		this.etat = true;
		this.camp = faction;
	}

	/** Méthode tuant une cellule
	 * 
	 */
	public void freeCellule(){
		this.etat = false;
		this.camp = null;
	}


	@Override
	public String toString() {
		if (this.etat)return this.camp.getNom().substring(0,1);
		else return " ";
	}
}
