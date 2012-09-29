/************************************************************************************
 * This code is part of Fermare il declino for android                              *
 * Copyright © 2012 ALI - Associazione Lavoro e Impresa per le Liberta' Economiche  *
 *   http://www.fermareildeclino.it info@fermareildeclino.it                        *
 *                                                                                  *
 * This program is free software; you can redistribute it and/or                    *
 * modify it under the terms of the GNU General Public License                      *
 * as published by the Free Software Foundation; either version 2                   *
 * of the License, or (at your option) any later version.                           *
 *                                                                                  *
 * This program is distributed in the hope that it will be useful,                  *
 * but WITHOUT ANY WARRANTY; without even the implied warranty of                   *
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the                    *
 * GNU General Public License for more details.                                     *
 *                                                                                  *
 * You should have received a copy of the GNU General Public License                *
 * along with this program; if not, write to the Free Software                      *
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.  *
 ************************************************************************************/ 
package it.fermareildeclino.app;

import java.util.ArrayList;
import java.util.List;

import android.app.Application;

public class Fid extends Application {
	static Fid _inst;
	static FidDatabase db;
	private HttpClient client = new HttpClient();
	List<Post> news= new ArrayList<Post>();
	public Fid(){
		super();
		_inst=this;
		db = FidDatabase.get(this);
	}
	public static FidDatabase getDb() {
		return db;
	}
	public static List<Post> getProposte() {
		ArrayList<Post> prop = new ArrayList<Post>();
		prop.add(new Post(
				1,
				"Ridurre l'ammontare del debito pubblico",
				"E' possibile scendere rapidamente sotto la soglia simbolica del 100% del PIL anche attraverso alienazioni del patrimonio pubblico, composto sia da immobili non vincolati sia da imprese o quote di esse.",
				""));
		prop.add(new Post(
				2,
				"Ridurre la spesa pubblica di almeno 6 punti percentuali del PIL nell'arco di 5 anni ",
				"Ridurre la spesa pubblica di almeno 6 punti percentuali del PIL nell'arco di 5 anni. La spending review deve costituire il primo passo di un ripensamento complessivo della spesa, a partire dai costi della casta politico-burocratica e dai sussidi alle imprese (inclusi gli organi di informazione). Ripensare in modo organico le grandi voci di spesa, quali sanità e istruzione, introducendo meccanismi competitivi all’interno di quei settori. Riformare il sistema pensionistico per garantire vera equità inter—e intra—generazionale.",
				""));
		prop.add(new Post(
				3,
				"Ridurre la pressione fiscale complessiva di almeno 5 punti in 5 anni",
				"Ridurre la pressione fiscale complessiva di almeno 5 punti in 5 anni, dando la priorità alla riduzione delle imposte sul reddito da lavoro e d'impresa. Semplificare il sistema tributario e combattere l'evasione fiscale destinando il gettito alla riduzione delle imposte.",
				""));
		prop.add(new Post(
				4,
				"Liberalizzare rapidamente i settori ancora non pienamente concorrenziali ",
				"Liberalizzare rapidamente i settori ancora non pienamente concorrenziali quali, a titolo di esempio: trasporti, energia, poste, telecomunicazioni, servizi professionali e banche (inclusi gli assetti proprietari). Privatizzare le imprese pubbliche con modalità e obiettivi pro-concorrenziali nei rispettivi settori. Inserire nella Costituzione il principio della concorrenza come metodo di funzionamento del sistema economico, contro privilegi e monopoli d'ogni sorta. Privatizzare la RAI, abolire canone e tetto pubblicitario, eliminare il duopolio imperfetto su cui il settore si regge favorendo la concorrenza. Affidare i servizi pubblici, incluso quello radiotelevisivo, tramite gara fra imprese concorrenti.",
				""));
		prop.add(new Post(
				5,
				"Sostenere i livelli di reddito di chi momentaneamente perde il lavoro anziché tutelare il posto di lavoro esistente o le imprese inefficienti ",
				"Sostenere i livelli di reddito di chi momentaneamente perde il lavoro anziché tutelare il posto di lavoro esistente o le imprese inefficienti. Tutti i lavoratori, indipendentemente dalla dimensione dell'impresa in cui lavoravano, devono godere di un sussidio di disoccupazione e di strumenti di formazione che permettano e incentivino la ricerca di un nuovo posto di lavoro quando necessario, scoraggiando altresì la cultura della dipendenza dallo Stato. Il pubblico impiego deve essere governato dalle stesse norme che sovrintendono al lavoro privato introducendo maggiore flessibilità sia del rapporto di lavoro che in costanza del rapporto di lavoro.",
				""));
		prop.add(new Post(
				6,
				"Adottare immediatamente una legislazione organica sui conflitti d'interesse",
				"Adottare immediatamente una legislazione organica sui conflitti d'interesse. Imporre effettiva trasparenza e pubblica verificabilità dei redditi, patrimoni e interessi economici di tutti i funzionari pubblici e di tutte le cariche elettive. Instaurare meccanismi premianti per chi denuncia reati di corruzione. Vanno allontanati dalla gestione di enti pubblici e di imprese quotate gli amministratori che hanno subito condanne penali per reati economici o corruttivi.",
				""));
		prop.add(new Post(
				7,
				"Far funzionare la giustizia",
				"Far funzionare la giustizia. Riformare il codice di procedura e la carriera dei magistrati, con netta distinzione dei percorsi e avanzamento basato sulla performance; no agli avanzamenti di carriera dovuti alla sola anzianità. Introdurre e sviluppare forme di specializzazione che siano in grado di far crescere l'efficienza e la prevedibilità delle decisioni. Difendere l'indipendenza di tutta la magistratura, sia inquirente che giudicante. Assicurare la terzietà dei procedimenti disciplinari a carico dei magistrati. Gestione professionale dei tribunali generalizzando i modelli adottati in alcuni di essi. Assicurare la certezza della pena da scontare in un sistema carcerario umanizzato.",
				""));
		prop.add(new Post(
				8,
				"Liberare le potenzialità di crescita, lavoro e creatività dei giovani e delle donne ",
				"Liberare le potenzialità di crescita, lavoro e creatività dei giovani e delle donne, oggi in gran parte esclusi dal mercato del lavoro e dagli ambiti più rilevanti del potere economico e politico. Non esiste una singola misura in grado di farci raggiungere questo obiettivo; occorre agire per eliminare il dualismo occupazionale, scoraggiare la discriminazione di età e sesso nel mondo del lavoro, offrire strumenti di assicurazione contro la disoccupazione, facilitare la creazione di nuove imprese, permettere effettiva mobilità meritocratica in ogni settore dell’economia e della società e, finalmente, rifondare il sistema educativo.",
				""));
		prop.add(new Post(
				9,
				"Ridare alla scuola e all'università il ruolo, perso da tempo, di volani dell'emancipazione socio-economica delle nuove generazioni",
				"Ridare alla scuola e all'università il ruolo, perso da tempo, di volani dell'emancipazione socio-economica delle nuove generazioni. Non si tratta di spendere di meno, occorre anzi trovare le risorse per spendere di più in educazione e ricerca. Però, prima di aggiungere benzina nel motore di una macchina che non funziona, occorre farla funzionare bene. Questo significa spendere meglio e più efficacemente le risorse già disponibili. Vanno pertanto introdotti cambiamenti sistemici: la concorrenza fra istituzioni scolastiche e la selezione meritocratica di docenti e studenti devono trasformarsi nelle linee guida di un rinnovato sistema educativo.Va abolito il valore legale del titolo di studio.",
				""));
		prop.add(new Post(
				10,
				"Introdurre il vero federalismo con l'attribuzione di ruoli chiari e coerenti ai diversi livelli di governo",
				"Introdurre il vero federalismo con l'attribuzione di ruoli chiari e coerenti ai diversi livelli di governo. Un federalismo che assicuri ampia autonomia sia di spesa che di entrata agli enti locali rilevanti ma che, al tempo stesso, punisca in modo severo gli amministratori di quegli enti che non mantengono il pareggio di bilancio rendendoli responsabili, di fronte ai propri elettori, delle scelte compiute. Totale trasparenza dei bilanci delle pubbliche amministrazioni e delle società partecipate da enti pubblici con l'obbligo della loro pubblicazione sui rispettivi siti Internet. La stessa \"questione meridionale\" va affrontata in questo contesto, abbandonando la dannosa e fallimentare politica di sussidi seguita nell'ultimo mezzo secolo.",
				""));
		return prop;
	}
	public List<Post> getNews() {
		return news;
	}
	public void setNews(List<Post> news) {
		this.news = news;
	}
	public static Fid get() {
		return _inst;
	}
	public HttpClient getClient() {
		return client;
	}
	public Post getPost(String link) {
		for(Post p:news)if(link.equals(p.getLink())) return p;
		return null;
	}
	
}
