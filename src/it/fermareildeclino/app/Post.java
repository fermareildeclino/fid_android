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

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;

public class Post {
	private static final long NEW_POST = -1;
	private long id;
	private String titolo, riassunto, testo, link, autore, source;
	private Date pubDate;
	boolean nuovo, letto;
	private static FidGetter getter;
	private static SimpleDateFormat df = new SimpleDateFormat(
			"yyyy-MM-dd'T'HH:mm:ss"), outdf = new SimpleDateFormat(
			"dd/mm/yy HH:mm:ss");

	private static enum PFIELDS {
		id, pubdate, titolo, riassunto, autore, link, letto, nuovo, testo, source
	};

	private static HashMap<PFIELDS, Integer> fieldIds = null;

	private Post() {
		initialize();
	}

	public Post(long id, String titolo, String riassunto, String testo) {
		initialize();
		this.id = id;
		this.titolo = titolo;
		this.riassunto = riassunto;
		this.testo = testo;
	}

	public Post(long id, String titolo, String riassunto, String link,
			String autore, String pubdate, String source) throws ParseException {
		initialize();
		this.id = id;
		this.titolo = titolo;
		this.riassunto = riassunto;
		this.link = link;
		this.autore = autore;
		this.pubDate = df.parse(pubdate.substring(0, 19));
		this.source = source;
		nuovo = true;
		letto = false;
	}

	private void initialize() {
		id = NEW_POST;
		titolo = riassunto = testo = link = autore = source = "";
		pubDate = new Date(0);
		nuovo = true;
		letto = false;
	}

	public boolean isNuovo() {
		return nuovo;
	}

	public boolean isLetto() {
		return letto;
	}

	public String getTesto() {
		return testo;
	}

	public void setTesto(String testo) {
		this.testo = testo;
	}

	public long getId() {
		return id;
	}

	public String getTitolo() {
		return titolo;
	}

	public String getRiassunto() {
		return riassunto;
	}

	static public String getStringValue(Element el, String tag) {
		String s = "";
		try {
			NodeList children = el.getElementsByTagName(tag).item(0)
					.getChildNodes();
			for (int i = 0; i < children.getLength(); i++) {
				if (children.item(i) instanceof CharacterData) {
					CharacterData cd = (CharacterData) children.item(i);
					s = s + cd.getData();
				}
			}
		} catch (Exception e) {
			Log.d("TE", e.toString());
		}
		return s;
	}

	public String getLink() {
		return link;
	}

	public String getAutore() {
		return autore;
	}

	public Date getPubDate() {
		return pubDate;
	}

	public CharSequence getPubDateStr() {
		return outdf.format(pubDate);
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public static List<Post> decodeFeed(Document doc) {
		ArrayList<Post> posts = new ArrayList<Post>();
		NodeList items;
		items = doc.getElementsByTagName("channel").item(0).getChildNodes();
		if (items != null) {
			for (int i = 0; i < items.getLength(); i++) {
				Node item = items.item(i);
				if (item.getNodeName().equals("item")) {
					Element el = (Element) item;
					Post p;
					try {
						p = new Post(NEW_POST, getStringValue(el, "title"),
								getStringValue(el, "description"),
								getStringValue(el, "link"), getStringValue(el,
										"author"),
								getStringValue(el, "pubDate"), getStringValue(
										el, "source"));
						posts.add(p);
					} catch (ParseException e) {
						Log.w("FID", "Post date format error", e);
					}

				}
			}
		}
		return posts;
	}

	public static void savenew(List<Post> posts) {
		for (Post post : posts) {
			if (post.id == NEW_POST) {// comes from the web
				post.dbCheck();
			}
		}
	}

	private void dbCheck() {
		Cursor c = getCursorFromLink();
		if (c.moveToFirst()) {
			getValuesFromCursor(c);
		} else {
			save();
		}
	}

	private boolean getValuesFromCursor(Cursor c) {
		HashMap<PFIELDS, Integer> fieldIds = getFieldIds(c);
		id = c.getLong(fieldIds.get(PFIELDS.id));
		pubDate = new Date(c.getLong(fieldIds.get(PFIELDS.pubdate)));
		titolo = c.getString(fieldIds.get(PFIELDS.titolo));
		riassunto = c.getString(fieldIds.get(PFIELDS.riassunto));
		autore = c.getString(fieldIds.get(PFIELDS.autore));
		link = c.getString(fieldIds.get(PFIELDS.link));
		letto = c.getInt(fieldIds.get(PFIELDS.letto)) != 0;
		nuovo = c.getInt(fieldIds.get(PFIELDS.nuovo)) != 0;
		testo = c.getString(fieldIds.get(PFIELDS.testo));
		source = c.getString(fieldIds.get(PFIELDS.source));
		return false;
	}

	private HashMap<PFIELDS, Integer> getFieldIds(Cursor c) {
		if (fieldIds == null) {
			fieldIds = new HashMap<Post.PFIELDS, Integer>();
			for (PFIELDS f : PFIELDS.values())
				fieldIds.put(f, c.getColumnIndex(f.toString()));
		}
		return fieldIds;
	}

	private Cursor getCursorFromLink() {
		SQLiteDatabase db = Fid.getDb().getWrdb();
		return db.rawQuery("select * from posts where link = ?;",
				new String[] { link });
	}

	private synchronized void save() {
		SQLiteDatabase db = Fid.getDb().getWrdb();
		if (id == NEW_POST) {
			ContentValues values = getValues(false);
			id = db.insert("posts", null, values);
		} else {
			ContentValues values = getValues(false);
			db.update("posts", values, " id = ?", new String[] { new Long(
					id).toString() });
		}
	}

	private ContentValues getValues(boolean withid) {
		ContentValues values = new ContentValues();
		if (withid)
			values.put("id", id);
		values.put("pubdate", pubDate.getTime());
		values.put("titolo", titolo);
		values.put("riassunto", riassunto);
		values.put("autore", autore);
		values.put("link", link);
		values.put("letto", letto ? 1 : 0);
		values.put("nuovo", nuovo ? 1 : 0);
		values.put("testo", testo);
		values.put("source", source);
		return values;
	}

	public Spanned getTestoSpan() {
		if (getter == null)
			getter = new FidGetter(Fid.get());
		return Html.fromHtml(testo, getter, null);
	}

	public void download() throws IOException {
		testo = Fid.get().getClient().geturl_string(link);
		getTestoSpan();
		save();
	}

}
