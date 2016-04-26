package GameOfLife;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;


public class Jeu{

	private Plateau lePlateau;
	private List<Faction> factions;
	private int nbFactions;
	private List<Alliance> alliances;

	/** Constructeur de la classe Jeu
	 *
	 */
	public Jeu(int leNbJoueurs){
		this.lePlateau = new Plateau(20, 20); // 20x20

		this.nbFactions = leNbJoueurs;
		this.factions = initialiserJoueurs(this.nbFactions); // HALALA !!
		this.initialiser(); // C'est mieux si on connais nbFactions avant de lancer itinialiser()...	(a)
		this.alliances = new ArrayList<Alliance>();
	}

	/** Méthode permettant d'initialiser le jeu
	 *
	 */
	public void initialiser(){
		List<ArrayList<Coordonnee>> positions = new ArrayList<ArrayList<Coordonnee>>(); // C'est bien de commenter et de pas appeler une liste "liste" juste parce que c'est une liste !!
		for(int j=0; j<this.nbFactions; j++){ // ...sinon, ici ca déconne...	(a)
			positions.add(new ArrayList<Coordonnee>());
		}
		Random r = new Random();
		int minX = 0, minY = 0;
		int addX = Math.floorDiv(this.lePlateau.getTailleHorizontale(),this.nbFactions), addY= Math.floorDiv(this.lePlateau.getTailleHorizontale(),this.nbFactions);
		int maxX = addX, maxY = addY;

		for(int f=0; f<this.nbFactions;f++) {
			for (int nCels = 0; nCels < (addX * addY) / 2; nCels++) {
				int x, y;
				Boolean next;
				do {
					next = true;
					x = Math.abs(Math.floorMod((int)(Math.random()*100),addX-1)) + minX ;
					y = Math.abs(Math.floorMod((int)(Math.random()*100),addY-1)) + minY ;

					for(int check=0; check < nCels; check++){
						Coordonnee c = positions.get(f).get(check);
						if (c.getX() == x && c.getY() == y) {next = false;}
					}
				}while(!next);

				positions.get(f).add(new Coordonnee(x, y));

			}
			minX += addX; minY += maxY;
			maxX += addX; maxY += maxY;
		}
		this.lePlateau.naissance(positions, this.factions);
	}

	/** Méthode permettant l'initialisation des joueurs
	 *
	 * @param nbJoueurs - Le nombre de joueurs
	 * @return Une liste de joueurs
	 */
	public List<Faction> initialiserJoueurs(int nbJoueurs){
		List<Faction> joueurs = new ArrayList<Faction>();
		Scanner sc = new Scanner(System.in);
		for(int i=0; i<nbJoueurs; i++){
			String nom = sc.nextLine();
			String couleur = sc.nextLine();
			joueurs.add(new Faction(nom,couleur));
		}
		sc.close();
		return joueurs;
	}

	/** Méthode permettant de jouer
	 *
	 */
	public void jouer(){
		this.checkLife();
		this.checkWar();
	}

	/** Méthode permettant de faire vivre ou mourir une cellule
	 *
	 */
	public void checkLife() {

		//L'ensemble des cellules vivantes
		List<ArrayList<Coordonnee>> vivantes = new ArrayList<ArrayList<Coordonnee>>();
		for (int z = 0; z < this.nbFactions; z++) {
			vivantes.add(new ArrayList<Coordonnee>());
		}
		//L'ensemble des cellules mortes
		List<Coordonnee> mortes = new ArrayList<Coordonnee>();
		//La faction qui prend la cellule
		List<Faction> appartenanceFaction = new ArrayList<Faction>();
		for (int i = 0; i < this.lePlateau.getTailleVerticale(); i++) {
			for (int j = 0; j < this.lePlateau.getTailleVerticale(); j++) {
				if (!this.lePlateau.getCellule(i, j).getEtat()) {
					List<Coordonnee> voisins = this.lePlateau.getVoisins(i, j);
					if (voisins.size() == 3) {
						boolean memeFaction = true;
						boolean memeAlliance = true;
						Faction faction = this.lePlateau.getCellule(voisins.get(0).getX(), voisins.get(0).getY()).getFaction();
						for (int c = 1; c < voisins.size(); c++) {
							if (!faction.equals(this.lePlateau.getCellule(voisins.get(c).getX(), voisins.get(c).getY()).getFaction())) {
								memeFaction = false;
							} else if (!faction.getAlliance().equals(this.lePlateau.getCellule(voisins.get(c).getX(), voisins.get(c).getY()).getFaction().getAlliance())) {
								memeAlliance = false;
							}
						}
						//Si les cellules sont de la même faction, on les insère directement dans la liste de celles devant vivre
						if (memeFaction) {
							vivantes.get(this.lePlateau.getCellule(voisins.get(0).getX(), voisins.get(0).getY()).getFaction().getNumFaction()).add(new Coordonnee(i,j));
							appartenanceFaction = this.lePlateau.getCellule(voisins.get(0).getX(), voisins.get(0).getY()).getFaction();
						//Sinon on vérifie qui de l'alliance va possèder la cellule
						}else if(memeAlliance){
							for (int d = 0; d < voisins.size(); d++){

							}
						}
					}
				}
			}
			this.lePlateau.naissance(vivantes, appartenanceFaction);
			this.lePlateau.mort(mortes);
		}
	}

	/** Méthode permettant de vérifier les conditions de guerre
	 *
	 */
	public void checkWar(){
		for (int i = 0; i < this.lePlateau.getTailleVerticale(); i++){
			for (int j = 0; j < this.lePlateau.getTailleHorizontale(); j++){
				List<Coordonnee> ennemis = this.lePlateau.getEnnemis(i, j);
				for (int k=0; k<ennemis.size(); k++){
					if((this.lePlateau.getVoisins(i, j)).size()< this.lePlateau.getVoisins(ennemis.get(k).getX() , ennemis.get(k).getY()).size()){
						(this.lePlateau.getCellule(i, j)).freeCellule();
					}
				}
			}
		}
	}

	public String toString(){
		return this.lePlateau.toString();
	}

}
